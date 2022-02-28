package com.chs.mt.xf_dap.bluetooth.spp_ble;
/**
 * 描述：蓝牙服务核心类
 */

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.chs.mt.xf_dap.datastruct.Define;
import com.chs.mt.xf_dap.datastruct.MacCfg;
import com.chs.mt.xf_dap.util.ByteUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothChatService {
    // 测试数据
    private static final String TAG = "BluetoothChatService";
    private static final boolean D = true;

    private static final String NAME = "BluetoothChat";

    // 声明一个唯一的UUID
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");	//change by chongqing jinou	

    private final BluetoothAdapter mAdapter;
    private final Handler mHandler;
    private AcceptThread mAcceptThread;
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;
    private int mState;
    private Context mContext;
    // 常量,显示当前的连接状态
    public static final int STATE_NONE = 0;
    public static final int STATE_LISTEN = 1;
    public static final int STATE_CONNECTING = 2; 
    public static final int STATE_CONNECTED = 3;
    
    public BluetoothChatService(Context context, Handler handler) {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        mState = STATE_NONE;
        mHandler = handler;
        mContext = context;
    }

    /**
     * 设置当前的连接状态
     * @param state  连接状态
     */
    private synchronized void setState(int state) {
        if (D) Log.d(TAG, "setState() " + mState + " -> " + state);
        mState = state;

        // 通知Activity更新UI
        mHandler.obtainMessage(Define.MESSAGE_STATE_CHANGE, state, -1).sendToTarget();
    }

    /**
     * 返回当前连接状态 
     * 
     */
    public synchronized int getState() {
        return mState;
    }

    /**
     *开始聊天服务
     *
     */
    public synchronized void start() {
        if (D) Log.d(TAG, "start");

        if (mConnectThread != null) {mConnectThread.cancel(); mConnectThread = null;}

        if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}

        if (mAcceptThread == null) {
            mAcceptThread = new AcceptThread();
            mAcceptThread.start();
        }
        setState(STATE_LISTEN);
    }

    /**
     * 连接远程设备
     * @param device  连接
     */
    public synchronized void connect(BluetoothDevice device) {
        if (D) Log.d(TAG, "连接到: " + device);

        if (mState == STATE_CONNECTING) {
            if (mConnectThread != null) {mConnectThread.cancel(); mConnectThread = null;}
        }

        if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}

        mConnectThread = new ConnectThread(device);
        mConnectThread.start();
        setState(STATE_CONNECTING);
    }

    /**
     * 启动ConnectedThread开始管理一个蓝牙连接
     */
    public synchronized void connected(BluetoothSocket socket, BluetoothDevice device) {
        if (D) Log.d(TAG, "连接");

        if (mConnectThread != null) {mConnectThread.cancel(); mConnectThread = null;}

        if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}

        if (mAcceptThread != null) {mAcceptThread.cancel(); mAcceptThread = null;}

        mConnectedThread = new ConnectedThread(socket);
        mConnectedThread.start();

        Message msg = mHandler.obtainMessage(Define.MESSAGE_DEVICE_NAME);
        Bundle bundle = new Bundle();
        bundle.putString(MacCfg.DEVICE_NAME, device.getName());
        msg.setData(bundle);
        mHandler.sendMessage(msg);

        setState(STATE_CONNECTED);
    }

    /**
     * 停止所有线程
     */
    public synchronized void stop() {
        if (D) Log.d(TAG, "stop");
        setState(STATE_NONE);
        if (mConnectThread != null) {mConnectThread.cancel(); mConnectThread = null;}
        if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}
        if (mAcceptThread != null) {mAcceptThread.cancel(); mAcceptThread = null;}
    }

    /**
     * 以非同步方式写入ConnectedThread
     * @param out 
     */
    public void write(byte[] out) {
        ConnectedThread r;
        synchronized (this) {
            if (mState != STATE_CONNECTED) return;
            r = mConnectedThread;
        }
//        System.out.println("FUCK r.write(out)");
        r.write(out);
    }

    /**
     * 无法连接，通知Activity
     */
    private void connectionFailed() {
        setState(STATE_LISTEN);
        
        Message msg = mHandler.obtainMessage(Define.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(MacCfg.TOAST, "无法连接设备");
        msg.setData(bundle);
        mHandler.sendMessage(msg);
    }

    /**
     * 设备断开连接，通知Activity
     */
    private void connectionLost() {
 
        Message msg = mHandler.obtainMessage(Define.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(MacCfg.TOAST, "设备断开连接");
        msg.setData(bundle);
        mHandler.sendMessage(msg);
    }

    /**
     * 监听传入的连接
     */
    private class AcceptThread extends Thread {
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            BluetoothServerSocket tmp = null;

            try {
                if(mAdapter != null){
                    tmp = mAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
                }
            } catch (IOException e) {
                Log.e(TAG, "listen() failed", e);
            }
            mmServerSocket = tmp;
        }

        public void run() {
            if (D) Log.d(TAG, "BEGIN mAcceptThread" + this);
            setName("AcceptThread");
            BluetoothSocket socket = null;

            while (mState != STATE_CONNECTED) {
                try {

                    if(mmServerSocket!=null){
                        socket = mmServerSocket.accept();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "accept() 失败", e);
                    //Motinlu
                    //cancel();
                    Intent intentw=new Intent();
                    intentw.setAction("android.intent.action.CHS_Broad_BroadcastReceiver");
                    intentw.putExtra("msg", Define.BoardCast_OPT_DisonnectDeviceBT);
                    if(mContext != null){
                        mContext.sendBroadcast(intentw);
                    }
                    break;
                }

                // 如果连接被接受
                if (socket != null) {
                    synchronized (BluetoothChatService.this) {
                        switch (mState) {
                        case STATE_LISTEN:
                        case STATE_CONNECTING:
                            // 开始连接线程
                            connected(socket, socket.getRemoteDevice());
                            break;
                        case STATE_NONE:
                        case STATE_CONNECTED:
                            // 没有准备好或已经连接
                            try {
                                socket.close();
                            } catch (IOException e) {
                                Log.e(TAG, "不能关闭这些连接", e);
                            }
                            break;
                        }
                    }
                }
            }
            if (D) Log.i(TAG, "结束mAcceptThread");
        }

        public void cancel() {
            if (D) Log.d(TAG, "取消 " + this);
            try {
                if(mmServerSocket!=null){
                    mmServerSocket.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "关闭失败", e);
            }
        }
    }


    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            mmDevice = device;
            BluetoothSocket tmp = null;

            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                Log.e(TAG, "create() 失败", e);
            }
            mmSocket = tmp;
        }

        public void run() {
            Log.i(TAG, "开始mConnectThread");
            setName("ConnectThread");

            mAdapter.cancelDiscovery();

            try {
                mmSocket.connect();
            } catch (IOException e) {
                connectionFailed();
                try {
                    mmSocket.close();
                } catch (IOException e2) {
                    Log.e(TAG, "关闭连接失败", e2);
                }
                BluetoothChatService.this.start();
                return;
            }

            synchronized (BluetoothChatService.this) {
                mConnectThread = null;
            }

            connected(mmSocket, mmDevice);
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "关闭连接失败", e);
            }
        }
    }

    /**
     * 处理所有传入和传出的传输
     */
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            Log.d(TAG, "创建 ConnectedThread");
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // 得到BluetoothSocket输入和输出流
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "temp sockets not created", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

		public void run() {
            Log.i(TAG, "BEGIN mConnectedThread");

            // 循环监听消息
            while (true) {

                try {
                	byte[] buffer = new byte[1024];
                	if(mmInStream == null){
                	    continue;
                    }
                    int len = mmInStream.read(buffer);
                    if(len != -1) {
                    	byte[] tempbuffer = ByteUtil.range(buffer, 0, len);

                        String st=",Data<";
                        String ss="";
                        for(int i=0;i<len;i++){
                            ss=Integer.toHexString(tempbuffer[i]&0xff).toLowerCase();
                            if(ss.length()==1){
                                ss="0"+ss;
                            }
                            if((((i+1)%4)==0)&&((i+1)!=len)){
                                st=st+ss+" ";
                            }else {
                                st+=ss;
                            }

                        }


                		mHandler.obtainMessage(Define.MESSAGE_READ, len, len, tempbuffer)
                        .sendToTarget();
                       // System.out.println("BUG 最开始的值为"+st);
                        //System.out.println("BUG 发送至为"+Bytes2HexString(Sendbytes));
                    }
                } catch (IOException e) {
                    Log.e(TAG, "disconnected", e);
                    connectionLost();
                    
                    if(mState != STATE_NONE) {
                    	// 在重新启动监听模式启动该服务
                    	BluetoothChatService.this.start();
                    }
                    break;
                }catch (Exception e){
                    e.printStackTrace();
                }


            }
        }

        /**
         * 写入OutStream连接
         * @param buffer  要写的字节
         */
        public void write(byte[] buffer) {
            try {
                mmOutStream.write(buffer);
               // System.out.println("BUG 发送至为"+Bytes2HexString(buffer));
                //Motinlu
//                try{
//    	  			sleep(50);
//    	  		}
//    	  		catch(Exception e){
//    	  			///LogUtil.log(e);
//    	  			Log.e("##write", "BT Close");
//    	  		}
                // 把消息传给UI
                //mHandler.obtainMessage(Define.MESSAGE_WRITE, -1, -1, buffer)
                //        .sendToTarget();
            } catch (IOException e) {
                Log.e(TAG, "Exception during write", e);
            }

//            String st="BUG-COM---发送命令:<";
//            String ss="";
//            int packsize = buffer.length;
//            for(int i=0;i<packsize;i++){
//                ss=Integer.toHexString(buffer[i]&0xff).toLowerCase();
//                if(ss.length()==1){
//                    ss="0"+ss;
//                }
//                if((((i+1)%4)==0)&&((i+1)!=packsize)){
//                    st=st+ss+" ";
//                }else {
//                    st+=ss;
//                }
//            }
//            System.out.println(st+">");
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }
    /**
     * 从字节数组到十六进制字符串转换 
     */
    public static String bytes2HexString(byte[] b) {
  	  String ret = "";
  	  for (int i = 0; i < b.length; i++) {
  	   String hex = Integer.toHexString(b[ i ] & 0xFF);
  	   if (hex.length() == 1) {
  	    hex = '0' + hex;
  	   }
  	   ret += hex.toUpperCase();
  	  }
  	  return ret;
  	}
}
