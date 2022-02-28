package com.chs.mt.xf_dap.service;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;

import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;


import com.chs.mt.xf_dap.MusicBox.MDOptUtil;
import com.chs.mt.xf_dap.MusicBox.MDef;
import com.chs.mt.xf_dap.R;
import com.chs.mt.xf_dap.bluetooth.spp_ble.BluetoothChatService;
import com.chs.mt.xf_dap.datastruct.DataStruct;
import com.chs.mt.xf_dap.datastruct.Define;
import com.chs.mt.xf_dap.datastruct.MacCfg;
import com.chs.mt.xf_dap.operation.DataOptUtil;
import com.chs.mt.xf_dap.operation.JsonRWUtil;
import com.chs.mt.xf_dap.util.ConnectionUtil;
import com.chs.mt.xf_dap.util.LocationUtils;
import com.chs.mt.xf_dap.util.ToastUtil;
import com.chs.mt.xf_dap.util.ToolsUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.UUID;

import static java.lang.Math.abs;

public class ServiceOfCom extends Service {
    public static boolean DEBUG = false;
    public static final int MAX_Name_Size = 13;
    public static final int EndFlag = 0xee;
    /*********************************************************************/
    /****************************   消息发送     ****************************/
    /*********************************************************************/
    public static final int NO = 0;  // 定义静态变量
    public static final int YES = 1; // 定义静态变量
    public static final int WHAT_IS_NULL = 0x00;            // 保留
    public static final int WHAT_IS_CONNECT_ERROR = 0x01;   // 连接错误
    public static final int WHAT_IS_CONNECT_RIGHT = 0x02;   // 连接网络正常
    public static final int WHAT_IS_SYNC_SUCESS = 0x03;     // 初始化同步数据
    public static final int WHAT_IS_NEW_DATA = 0x04;        // 收到新数据消息
    public static final int WHAT_IS_SEND_DATA = 0x05;       // 发送数据消息
    public static final int WHAT_IS_PROGRESSDIALOG = 0x06;  // 更新progressDialog
    public static final int WHAT_IS_LEDUP_SOURCE = 0x07;    // 信号灯实时更新音源
    public static final int WHAT_IS_OFF_LINE_INFO = 0x08;   // 未连接提示信息
    public static final int WHAT_IS_SYNC_GROUP_NAME = 0x09;   // 同步用户组名字
    public static final int WHAT_IS_LED_FLASH = 0x0A;         // 已连接设备
    public static final int WHAT_IS_RESET_EQ_CHNAME = 0x0B;   // 刷新EQ通道名字
    public static final int WHAT_IS_LongPress_INC_SUB = 0x0d; // 用于长按按键时的连续增减响应处理
    public static final int WHAT_IS_GroupClick = 0x0e;        // 用于用户组的单双击响应处理
    public static final int WHAT_IS_Wait = 0x0f;                //用于发送队列延时
    public static final int WHAT_IS_LOADING = 0x10;           //需要等待时显示加载图
    public static final int WHAT_IS_UPDATE_UI = 0x11;         // 用于更新界面
    public static final int WHAT_IS_FLASH_BT_CONNECTED = 0x12;// 刷新蓝牙的音频通道是否已经连接本机
    public static final int WHAT_IS_INIT_LOADING = 0x13;      //当开机发送数据后，到一个正解的应答后开始显示加载进度
    public static final int WHAT_IS_CLOSE_BT = 0x14;          //正在关闭蓝牙
    public static final int WHAT_IS_BT_TIME_OUT = 0x15;       //蓝牙连接超时
    public static final int WHAT_IS_MENU_LOCKED = 0x16;       //
    public static final int WHAT_IS_BLUETOOTH_SCAN = 0x17;    //
    public static final int WHAT_IS_RETURN_EXIT = 0x18;     // 按2次返回键处理
    public static final int WHAT_IS_LOGOUT_SUCCESS = 0x19;
    public static final int WHAT_IS_LOGOUT_FAILED = 0x1a;
    public static final int WHAT_IS_FLASH_NETWORK = 0x1b;     //网络是否在连接
    public static final int WHAT_IS_BT_SCAN = 0x1c;           //蓝牙连接超时
    public static final int WHAT_IS_FLASH_NET_HOME_UI = 0x1d;// 刷界面
    public static final int WHAT_IS_FLASH_SYSTEM_DATA = 0x1e;// 刷界面
    public static final int WHAT_IS_TRYS_TO_CON_DSPPLAYMSG = 0x1f;// 刷界面
    public static final int WHAT_IS_ShowGetERRMasterVolMsg = 0x20;//
    public static final int WHAT_IS_Show_UnKnowMacType_Msg = 0x21;//
    public static final int WHAT_IS_Show_Timer = 0x22;//
    public static final int WHAT_IS_BOOT_Start = 0x23;//
    public static final int WHAT_IS_Max = 0x23;// 刷界面
    public static final int WHAT_IS_Address = 0x24;

    public static final int WHAT_IS_FlashUI_ConnectStateBtn = 0x60;
    public static final int WHAT_IS_FlashUI_AllPage = 0x61;
    public static final int WHAT_IS_FlashUI_MasterPage = 0x62;
    public static final int WHAT_IS_FlashUI_DelayPage = 0x63;
    public static final int WHAT_IS_FlashUI_XOverPage = 0x64;
    public static final int WHAT_IS_FlashUI_XOverOutputPage = 0x65;
    public static final int WHAT_IS_FlashUI_EQPage = 0x66;
    public static final int WHAT_IS_FlashUI_MixerPage = 0x67;
    public static final int WHAT_IS_FlashUI_DeviceVersionsErr = 0x68;
    public static final int WHAT_IS_FlashUI_InputSource = 0x69;
    public static final int WHAT_IS_FlashUI_ConnectState = 0x6a;
    public static final int WHAT_IS_FlashUI_ShowLoadingDialog = 0x6b;
    public static final int WHAT_IS_FlashUI_FlashLoadingDialog = 0x6c;

    public static final int WHAT_IS_Opt_ConnectDevice = 0x70;
    public static final int WHAT_IS_Opt_DisconnectDevice = 0x71;

    public static final int Arg_ConnectStateBtn_OFF = 0x0;
    public static final int Arg_ConnectStateBtn_ON = 0x1;
    public static final int WHAT_IS_Opt_DisconnectDeviceSPP_LE = 0x72;


    /****************************   扫描本地，更新文件数据        ****************************/
    public static final String ACTION_MEDIA_SCANNER_SCAN_DIR = "android.intent.action.MEDIA_SCANNER_SCAN_DIR";

    private Toast mToast;
    /*********************************************************************/
    /*******************  USB Host  蓝牙    WIFI通信     **********************/
    /*********************************************************************/

    /****************************************************************************/
    /****************************     蓝牙通信        ********************************/
    /****************************   蓝牙的连接状态        ****************************/
    public static final int BT_SEND_DATA_PACK_SIZE = 20; //蓝牙发送数据包的最大字节数

    /*********************************************************************/
    /****************************    蓝牙SPP通信   ********************************/
    private static byte[] BTSendBuf20 = new byte[20];//较多常用24字节
    private byte[] BTRecBuf = new byte[1024];

    private static boolean BT_COther = false;      //true:用户手动连接其他设备
    private static boolean bool_OpBT = false;      //false:可操作蓝牙，避免快速开关断蓝牙 正在打开
    private static boolean bool_CloseBT = false;   //false:可操作蓝牙，避免快速开关断蓝牙 正在关闭
    /* true:从已经连接到DSP HP-XXXX蓝牙到8秒内，无法连接，测弹出提示：“蓝牙连接超时，请确定所连接的机器是
     * 否正确。若正确，请手动重启蓝牙，再进行连接。*/
    private static boolean bool_BT_CTO_Send = false;
    private static boolean bool_BT_ConTimeOut = false;
    private static boolean bool_FristStart = false;
    /*用于处理发送数失败后，收到错误应答后，再次重新发送数据*/
    private static boolean BTS_Again = false;/*true:要再次发送，1：其他*/
    /*************************************************************************/
    /****************************  蓝牙BLE通信   *******************************/
    public static boolean BLE_DEVICE_STATUS = false;
    private static byte[] BLESendBuf = new byte[Define.BLE_MaxT];
    @SuppressWarnings("unused")
    private String mDeviceName = null;
    private String mDeviceAddress = null;
    /***************************************************************************/
    /****************************     WFIF通信       *******************************/
    private static Socket mSocketClient = null;
    @SuppressWarnings("unused")
    private static BufferedReader mBufferedReaderClient = null;
    private String recvMessageClient = "";

    private static int WifiInfoTimerCnt = 0;


    private static boolean DeviceVerErrorFlg = false;// 设备版本信息错误标志
    private static boolean WIFI_CanConnect = false;
    /***************************************************************************/
    /****************************  USB Host通信    *******************************/
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private UsbManager USBManager; // USB管理器
    private static UsbDevice mUsbDevice;  // 找到的USB设备
    private UsbInterface mUsbInterface;
    private static UsbDeviceConnection mDeviceConnection;
    private static UsbEndpoint epOut;
    private UsbEndpoint epIn;
    private static byte[] USBSendBuf = new byte[Define.USB_MaxT];
    private byte[] Receiveytes = new byte[Define.USB_MaxT]; // 接收信息字节
    /*********************************************************************/
    /*******************  蓝牙SPP-LE   通信     **********************/
    /*********************************************************************/
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    // 连接设备的名称
    private String mConnectedDeviceName = null;
    private StringBuffer mOutStringBuffer;
    // 本地蓝牙适配器
    private BluetoothAdapter mBluetoothAdapter = null;
    // 成员对象聊天服务
    private static BluetoothChatService mChatService = null;
    private static BluetoothDevice deviceSPPBLE;
    /***************************************************************************/
    /****************************     系统线程       *******************************/
    /***************************************************************************/
    private static int progressDialogStep = 0; // progressDialog分几步跑完

    public static final String TAG = "BUG ###ServiceOfCom";

    private static Context mContext;
    private Thread sThread = null;
    private Thread rThread = null;
    private Thread tThread = null;
    private Thread aThread = null;

    private CHS_Broad_BroadcastReceiver CHS_Broad_Receiver;

    //与Activity通信
    private Messenger mActivityMessenger;
    private Messenger mServiceMessenger;
    private Handler mhandlerService;
    private Messenger mReSendMessenger;

    public ServiceOfCom() {
        Log.d(TAG, TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate() executed");
        initService();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() executed");


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i("TAG", "onBind()");

        return mServiceMessenger.getBinder();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        serviceOnDestroy();
        Log.d(TAG, "onDestroy() executed");
    }

    private void sendMsgToActivity(int what, int arg1, int arg2) {
//        System.out.println("## BUG sendMsgToActivity  msg.what:"+what+",arg1"+arg1+",arg2"+arg2);
//
//        Message message = Message.obtain();
//        message.what = what;
//        message.arg1 = arg1;
//        message.arg2 = 0xEE;
//        try {
//            mActivityMessenger.send(message);
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
    }

    /*********************************************************************/
    /*****************************   数据通信       ****************************/
    /*********************************************************************/
    /*********************************************************************/

    private void initService() {
        mContext = getApplicationContext();

        //rThread = new Thread(rRunnable);
//		rThread.start();
        if (MacCfg.Mcu == 7||MacCfg.Mcu==8) {
            aThread = new Thread(aRunnable);
            aThread.start();
        } else {
            sThread = new Thread(sRunnable);
            sThread.start();
        }

//        new Thread(sRunnable);
////        sThread.start();

        tThread = new Thread(tRunnable);
//
//        aThread=new Thread(aRunnable);
        tThread.start();

//		tThread.start(); // 打开定时器线程
        int IdNum = (int) Thread.currentThread().getId();
        System.out.println(TAG + " initService currentThread Id:" + IdNum);
        initHandlerService();
        //动态注册CHS_Broad_BroadcastReceiver
        CHS_Broad_Receiver = new CHS_Broad_BroadcastReceiver();
        IntentFilter CHS_Broad_filter = new IntentFilter();
        CHS_Broad_filter.addAction("android.intent.action.CHS_Broad_BroadcastReceiver");
        //注册receiver
        registerReceiver(CHS_Broad_Receiver, CHS_Broad_filter);
        DataStruct.jsonRWOpt = new JsonRWUtil();

        DataOptUtil.InitApp(mContext);
        //初始化通信方式
        initCommunicationMode();


//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//
//            @Override
//            public void run() {
//                // TODO Auto-generated method stub
//
//            }
//
//        }, 20000);


    }

    private void initHandlerService() {
        /**
         * HandlerThread是Android系统专门为Handler封装的一个线程类，
         通过HandlerThread创建的Hanlder便可以进行耗时操作了
         * HandlerThread是一个子线程,在调用handlerThread.getLooper()之前必须先执行
         * HandlerThread的start方法。
         */
        HandlerThread handlerThread = new HandlerThread("serviceCalculate");
        handlerThread.start();
        mhandlerService = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (mActivityMessenger == null) {
                    mActivityMessenger = msg.replyTo;
                }
                System.out.println("## BUG mhandlerService Rec msg.what=" + msg.what + ",msg.arg1=" + msg.arg1 + ",msg.arg2=" + msg.arg2);
                if (msg.what == WHAT_IS_Opt_ConnectDevice) {
                    if (msg.arg1 == Arg_ConnectStateBtn_ON) {

                    } else {

                    }
                }

            }
        };
        mServiceMessenger = new Messenger(mhandlerService);
    }

    /**
     * 是否已经连接本机，true:已经连接
     */
    private static boolean isConnected() {
        switch (MacCfg.COMMUNICATION_MODE) {
            case Define.COMMUNICATION_WITH_WIFI:
                if (DataStruct.isConnecting && mSocketClient != null) {
                    return true;
                }
                break;
            case Define.COMMUNICATION_WITH_BLUETOOTH_LE:
                return BLE_DEVICE_STATUS;
            case Define.COMMUNICATION_WITH_BLUETOOTH_SPP:
//                if((mSocketClient == null)&&(CheckBTStata())&& (btManager.getStatus()== Common.BTConnectStatusConnect)){
//                    return true;
//                }
                break;
            case Define.COMMUNICATION_WITH_USB_HOST:
                return MacCfg.USBConnected;
            case Define.COMMUNICATION_WITH_UART:

                break;
            default:
                break;
        }

        return false;
    }

    public void printSendPack(byte[] btdata, int packsize) {
        String packString = "Send[";
        for (int i = 0; i < packsize; i++) {
            packString += (btdata[i] + ",");
        }
        System.out.println("BUG  printSendPack=" + packString);
    }

    /*****************************  WIFI TODO ****************************/
    /*********************************************************************/

    private boolean NewSocketClient() {
        String msgText = Define.WIFI_IP_PORT;
        if (msgText.length() <= 0) {
            recvMessageClient = "IP不能为空!";// 消息换行
            Message msg = Message.obtain();
            msg.what = WHAT_IS_CONNECT_ERROR;
            mHandler.sendMessage(msg);

            System.out.println("BUG WHAT_IS_CONNECT_ERROR = " + "IP不能为空!");
            return false;
        }
        int start = msgText.indexOf(":");
        if ((start == -1) || (start + 1 >= msgText.length())) {
            recvMessageClient = "IP地址不合法!";// 消息换行
            Message msg = Message.obtain();
            msg.what = WHAT_IS_CONNECT_ERROR;
            mHandler.sendMessage(msg);

            System.out.println("BUG WHAT_IS_CONNECT_ERROR = " + "IP地址不合法!!");
            return false;
        }
        String sIP = msgText.substring(0, start);
        String sPort = msgText.substring(start + 1);
        int port = Integer.parseInt(sPort);

        System.out.println("BUG WIFI  " + "IP:" + sIP + ",port:" + port);
        try {
            if (mSocketClient == null) {
                // 连接服务器
                mSocketClient = new Socket(sIP, port); // portnum
                if (mSocketClient != null) {
                    // 取得输入、输出流
                    mBufferedReaderClient = new BufferedReader(
                            new InputStreamReader(mSocketClient.getInputStream()));
                    recvMessageClient = "已经连接到server!";// 消息换行
                    // 发送连接命令
                    Message msg = Message.obtain();
                    msg.what = WHAT_IS_CONNECT_RIGHT;
                    mHandler.sendMessage(msg);

                    System.out.println("BUG mSocketClient new Socket 已经连接到server!");
                    return true;
                } else {
                    System.out.println("BUG mSocketClient new Socket ERROR!!!");
                    return false;
                }
            }
            return false;
        } catch (Exception e) {
            recvMessageClient = "连接IP异常:" + e.toString() + e.getMessage();// 消息换行
            System.out.println("BUG WIFI error-leon");
            Message msg = Message.obtain();
            msg.what = WHAT_IS_CONNECT_ERROR;
            mHandler.sendMessage(msg);
            System.out.println("BUG WIFI  :" + recvMessageClient);
            return false;
        }
    }

    /**
     * 判断wifi连接状态
     *
     * @param ctx
     * @return
     */
    private boolean isWifiAvailable(Context ctx) {
        ConnectivityManager conMan = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo.State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();
        if (NetworkInfo.State.CONNECTED == wifi) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isWifiIpOfCHS() {
        WifiManager wifiManager = (WifiManager) mContext.getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        // 获取32位整型IP地址
        int ipAddress = wifiInfo.getIpAddress();
        String ipString = "";
        if (ipAddress != 0) {
            ipString = ((ipAddress & 0xff) + "." + (ipAddress >> 8 & 0xff) + "."
                    + (ipAddress >> 16 & 0xff) + "." + (ipAddress >> 24 & 0xff));
        }
        System.out.println("BUG WIFI  isWifiIpOfCHS A ipString:" + ipString);

        // 判断IP地址是不是10.10.100.xxx
        if ((ipAddress & 0xffffff) == 0x640a0a) {

            if (ipAddress != 0) {
                ipString = ((ipAddress & 0xff) + "." + (ipAddress >> 8 & 0xff) + "."
                        + (ipAddress >> 16 & 0xff) + "." + (ipAddress >> 24 & 0xff));
            }

            System.out.println("BUG WIFI  isWifiIpOfCHS B ipString:" + ipString);
            System.out.println("BUG WIFI  isWifiAvailable 4");
            Define.WIFI_IP = ipString;
            //Define.WIFI_IP_PORT=Define.WIFI_IP+DataStruct.WIFI_PORT;
            return true;
        } else {
            return false;
        }
    }

    public boolean CheckWifiStata() {
        if (DataStruct.isConnecting) {
            return false;
        }
        System.out.println("BUG WIFI  isWifiAvailable 0");
        if (DataStruct.ManualConnecting == true) {
            return false;
        }

        if (!isWifiAvailable(mContext)) {
            return false;
        }
        System.out.println("BUG WIFI  isWifiAvailable 3");
        return isWifiIpOfCHS();
    }

    //打开返回true
    public static boolean CheckBTStata() {
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!btAdapter.isEnabled()) {
            return false;
        }
        return true;
    }

    //TODO
    public void WIFI_LinkButtonCmd() {
        WIFI_CanConnect = false;
        System.out.println("BUG WIFI DataStruct.U0SynDataSucessFlg:" + DataStruct.U0SynDataSucessFlg + ",mSocketClient:" + mSocketClient);
        //已经连接了的
        if ((DataStruct.U0SynDataSucessFlg == true) && (mSocketClient != null) && (DataStruct.isConnecting)) {
            DataStruct.U0SynDataSucessFlg = false;
            DataStruct.isConnecting = false;
            DataStruct.ManualConnecting = true; // 客戶手动断开的
            // 发送断开命令
            Message msg = Message.obtain();
            msg.what = WHAT_IS_CONNECT_ERROR;
            mHandler.sendMessage(msg);

            System.out.println("BUG WHAT_IS_CONNECT_ERROR = " + "DataStruct.ManualConnecting = true; // 客戶手动断开的");
        } else {
            if (isWifiAvailable(mContext)) {//是否有连接WIFI
                if (isWifiIpOfCHS()) {//是否ＣＨＳ的WIFI
                    WIFI_CanConnect = true;
                } else {
                    WIFI_CanConnect = false;
                }
            }
            if (WIFI_CanConnect == false) {
                //ToastMsg(getResources().getString(R.string.Open_wifi));
                startActivity(new Intent(
                        android.provider.Settings.ACTION_WIFI_SETTINGS));
                return;
            }

            DataStruct.ManualConnecting = false; // 客戶手动连接
        }
    }


    /*********************************************************************/
    /*************************  蓝牙SPP通信方式 TODO  *********************/
    /*********************************************************************/
    private void initCommunicationMode() {
        switch (MacCfg.COMMUNICATION_MODE) {
            case Define.COMMUNICATION_WITH_WIFI:

                break;
            case Define.COMMUNICATION_WITH_BLUETOOTH_LE:
                //提示用户打开蓝牙
                if (CheckBTStata() == false) {
                    // ShowOpenBTDialog();
                }
                break;
            case Define.COMMUNICATION_WITH_USB_HOST:
                //RegUSBBroadcastReceiver();
                break;
            case Define.COMMUNICATION_WITH_UART:

                break;

            default:
                break;
        }

    }

    /* Check the audio channel of the Bluetooth connection device,
     * if none, close bluetooth connection.
     */
    public static void FlashBTConnectedMusicChannel() {
//        if((!MacCfg.CHS_BT_CONNECTED)&&(DataStruct.U0SynDataSucessFlg)){
//            System.out.println("BUG FlashBTConnectedMusicChannel closeSessionBt");
//
//            DataStruct.isConnecting=false;
//            Message msg = Message.obtain();
//            msg.what = WHAT_IS_CONNECT_ERROR;
//            mHandler.sendMessage(msg);
//            bool_OpBT = false;
//        }
//
//        if((MacCfg.CHS_BT_CONNECTED)&&(mSocketClient == null)&&(!DataStruct.isConnecting)){
//
//            if((!MacCfg.BTManualConnect)||(BT_COther)){
//                System.out.println("BUG FlashBTConnectedMusicChannel openSessionBt");
//                if(MacCfg.CHS_BT_CONNECTED==true){
//                    openSessionBt(1,MacCfg.BT_ConnectedID);
//                }
//
//                //连接成功
//                DataStruct.isConnecting=true;
//                Message msg = Message.obtain();
//                msg.what = WHAT_IS_CONNECT_RIGHT;
//                mHandler.sendMessage(msg);
//            }
//        }
    }

    private static void startScanBluetooth() {
        mContext.startActivity(new Intent(
                android.provider.Settings.ACTION_BLUETOOTH_SETTINGS));
    }

    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private static void CheckBTConnectNaneAnaAdderss() {
        if (CheckBTStata() == false) {
            if ((!bool_FristStart) && (!MacCfg.BTManualConnect)) {
                bool_FristStart = true;
            }
            MacCfg.LCBT.clear();
            bool_OpBT = false;
            MacCfg.BT_ConnectedID = "NULL";
            MacCfg.BT_ConnectedName = "NULL";

            MacCfg.CHS_BT_CONNECTED = false;
            MacCfg.BTManualConnect = false;//true:用户手动断开
            bool_BT_ConTimeOut = false;
            DataStruct.isConnecting = false;

            if (mChatService != null) {
                mChatService.stop();
                mChatService = null;
            }

            return;
        }
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        int a2dp = btAdapter.getProfileConnectionState(BluetoothProfile.A2DP);
        int headset = btAdapter.getProfileConnectionState(BluetoothProfile.HEADSET);
        int health = btAdapter.getProfileConnectionState(BluetoothProfile.HEALTH);

        int flag = -1;
        if (a2dp == BluetoothProfile.STATE_CONNECTED) {
            flag = a2dp;
        } else if (headset == BluetoothProfile.STATE_CONNECTED) {
            flag = headset;
        } else if (health == BluetoothProfile.STATE_CONNECTED) {
            flag = health;
        }
        //System.out.println("BUG CheckBTConnect A flag:"+flag);
        if (flag != -1) {
            btAdapter.getProfileProxy(mContext, new BluetoothProfile.ServiceListener() {

                @Override
                public void onServiceDisconnected(int profile) {
                }

                @Override
                public void onServiceConnected(int profile, BluetoothProfile proxy) {
                    List<BluetoothDevice> mDevices = proxy.getConnectedDevices();
                    if (mDevices != null && mDevices.size() > 0) {
                        MacCfg.LCBT.clear();
                        for (BluetoothDevice device : mDevices) {
                            Log.e("##BUG BT Connect BT", "BT name:" +
                                    device.getName() + ",BT Address " + device.getAddress() + "\n");
                            MacCfg.BT_GetName = device.getName();
                            MacCfg.BT_GetID = device.getAddress();

                            if ((MacCfg.BT_GetName.contains(Define.BT_Paired_Name_DSP_HD_))

                                    || (MacCfg.BT_GetName.contains(Define.BT_Paired_Name_DSP_NAKAMICHI))
                                    || (MacCfg.BT_GetName.contains(Define.BT_Paired_Name_DSP_HDx))

                                    || (MacCfg.BT_GetName.contains(Define.BT_Paired_Name_Pioneer))
                                    || (MacCfg.BT_GetName.contains(Define.BT_Paired_Name_DSP_Play))
                                    || (MacCfg.BT_GetName.contains(Define.BT_Paired_Name_DSP_ZD))
                                    || (MacCfg.BT_GetName.contains(Define.BT_Paired_Name_DSP_BD_NAKAMICHI))
                            ) {

                                MacCfg.CHS_BT_CONNECTED = true;
                                if (MacCfg.BT_GetName.contains(Define.BT_Paired_Name_DSP_HD_)) {
                                    MacCfg.COMMUNICATION_MODE = Define.COMMUNICATION_WITH_BLUETOOTH_SPP;
                                    MacCfg.BT_ConnectedID = MacCfg.BT_GetID;
                                    MacCfg.BT_ConnectedName = MacCfg.BT_GetName;
                                }

                                MacCfg.LCBT.add(device);
                            }
                        }
                    } else {
                        MacCfg.BT_ConnectedID = "NULL";
                        MacCfg.BT_ConnectedName = "NULL";

                        MacCfg.CHS_BT_CONNECTED = false;
                        //System.out.println("BUG CheckBTConnect mDevices is null0");
                    }
                }
            }, flag);
        } else if (flag == -1) {
            MacCfg.BT_ConnectedID = "NULL";
            MacCfg.BT_ConnectedName = "NULL";
            MacCfg.CHS_BT_CONNECTED = false;
            bool_OpBT = false;
            //System.out.println("BUG CheckBTConnect mDevices is null.1");
        }
    }

    private void SaveBTRecBuf(byte[] data) {
        int i = 0;
        for (byte b : data) {
            BTRecBuf[i] = (byte) (b & 0xff);
            ReceiveDataFromDevice((0xff & BTRecBuf[i]), Define.COMMUNICATION_WITH_BLUETOOTH_SPP);
            //System.out.println("BUG BTRecBufByte="+BTRecBuf[i]);
            ++i;
        }
    }

    /*********************************************************************/
    /********************  蓝牙BLE GATT通信方式  TODO   ********************/
    /*********************************************************************/


    /**********************  Bluetooth SPP BLE通信方式   TODO  **********************/
    private class ServerThread extends Thread {
        // 固定的UUID
        final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
        UUID uuid = UUID.fromString(SPP_UUID);
        //private BluetoothServerSocket serverSocket;
        String tag = "BUG FUCK";

        @Override
        public void run() {

            try {
                BluetoothSocket socket = deviceSPPBLE.createRfcommSocketToServiceRecord(uuid);
                socket.connect();
                System.out.println("####- BUG socket.connect()");

            } catch (Exception e) {
                System.out.println("BUG ServerThread run Exception!");
            }


//            try {
//                serverSocket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(tag, UUID.fromString(SPP_UUID));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            Log.d(tag, "等待客户连接...");
//            while (true) {
//                try {
//                    BluetoothSocket socket = serverSocket.accept();
//
//                    BluetoothDevice device = socket.getRemoteDevice();
//                    Log.d(tag, "接受客户连接 , 远端设备名字:" + device.getName() + " , 远端设备地址:" + device.getAddress());
//
//                    if (socket.isConnected()) {
//                        Log.d(tag, "已建立与客户连接.");
//                    }
//
//                } catch (Exception e) {
//                    Log.d(tag, "等待客户连接.Exception..");
//                    e.printStackTrace();
//                }
//            }
        }
    }

    private void connect(BluetoothDevice device) throws IOException {
        deviceSPPBLE = device;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter != null)
            new Thread(new ServerThread()).start();

//        final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
//        UUID uuid = UUID.fromString(SPP_UUID);

//        BluetoothSocket socket = deviceSPPBLE.createRfcommSocketToServiceRecord(uuid);
//        socket.connect();
//        System.out.println("####- BUG socket.connect()");


//        String SS="BUG FUCK";
//        BluetoothServerSocket serverSocket;
//        serverSocket=mBluetoothAdapter.listenUsingRfcommWithServiceRecord(SS, uuid);
//
//        while (true) {
//            BluetoothSocket socket = serverSocket.accept();
//            if (socket.isConnected()) {
//                System.out.println("####- BUG socket.connect()");
//            }
//        }
    }

    // 此Handler处理BluetoothChatService传来的消息
    private final Handler mHandlerOfSPP_LE = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Define.MESSAGE_STATE_CHANGE:
                    Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            Message msgg = Message.obtain();
                            msgg.what = WHAT_IS_CONNECT_RIGHT;
                            mHandler.sendMessage(msgg);
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            //ToastUtil.showShortToast(mContext,mContext.getResources().getString(R.string.LinkOfMsg));
                            System.out.println("BUG====- mHandlerOfSPP_LE WHAT_IS_CONNECT_ERROR");
                            //断开蓝牙
                            Message msg_err = Message.obtain();
                            msg_err.what = WHAT_IS_CONNECT_ERROR;
                            mHandler.sendMessage(msg_err);
                            break;
                    }
                    break;
                case Define.MESSAGE_WRITE:
                    break;
                case Define.MESSAGE_READ:
//            	System.out.println("BUG MESSAGE_READ");
                    byte[] readBuf = (byte[]) msg.obj;
                    int cnt = msg.arg1;

//                    String st=",Data<";
//                    String ss="";
//                    for(int i=0;i<cnt;i++){
//                        ss=Integer.toHexString(readBuf[i]&0xff).toLowerCase();
//                        if(ss.length()==1){
//                            ss="0"+ss;
//                        }
//                        if((((i+1)%4)==0)&&((i+1)!=cnt)){
//                            st=st+ss+" ";
//                        }else {
//                            st+=ss;
//                        }
//
//                    }
//                    System.out.println("BUG-COM-Service-接收N:"+cnt+st+">");


                    for (int i = 0; i < cnt; i++) {
                        ReceiveDataFromDevice((readBuf[i] & 0xff), Define.COMMUNICATION_WITH_BLUETOOTH_SPP_TWO);
                    }
                    break;
                case Define.MESSAGE_DEVICE_NAME:
                    break;
                case Define.MESSAGE_TOAST:

                    break;
            }
        }
    };

    public static void SPP_LESendPack(byte[] btdata, int packsize) {

        System.out.print(Bytes2HexString(btdata));
        int temp1 = 0, temp2 = 0;
        temp1 = packsize / Define.BLE_MaxT;
        if (temp1 > 0) {
            for (int i = 0; i < temp1; i++) {
                for (int j = 0; j < Define.BLE_MaxT; j++) {
                    BLESendBuf[j] = btdata[i * Define.BLE_MaxT + j];
                }
                SPP_LESend(BLESendBuf);
            }
        }

        temp2 = packsize % Define.BLE_MaxT;
        //清0
        for (int i = 0; i < Define.BLE_MaxT; i++) {
            BLESendBuf[i] = (byte) 0x00;
        }

        if (temp2 > 0) {
            for (int i = 0; i < temp2; i++) {
                BLESendBuf[i] = btdata[temp1 * Define.BLE_MaxT + i];
            }
            SPP_LESend(BLESendBuf);
        }
    }

    private static void SPP_LESend(byte[] Sendbytes) {
        if (mChatService != null) {
            mChatService.write(Sendbytes);

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                //   System.out.println("BUG sThread BLESend");
            }
        }
    }


    /**********************  USB Host通信方式   TODO  **********************/
//    private void RegUSBBroadcastReceiver() {
//
//        //USBsend = (Button) findViewById(R.id.btsend);
//        //USBsend.setOnClickListener(btsendListener);
//
//        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);//PERMISSION
//        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);  //ATTACHED
//        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);  //DETACHED
//        registerReceiver(mUsbReceiver, filter);
//
//      //  searchUsbDevice();
//    }

//    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            System.out.println("BUG USB BroadcastReceiver="+ action);
//            UsbDevice UsbDevice = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
//            //USBsend.setText("UsbDevice#VID:"+UsbDevice.getVendorId()+",PID:"+UsbDevice.getProductId());
//            //ToastMsg("mUsbReceiver USB.getVendorId()="+UsbDevice.getVendorId());
//
////		    System.out.println("BUG USB DSP-UsbDevice getVendorId:"+UsbDevice.getVendorId());
////            System.out.println("BUG USB DSP-UsbDevice getProductId:"+UsbDevice.getProductId());
////            System.out.println("BUG USB DSP-UsbDevice getDeviceId:"+UsbDevice.getDeviceId());
////            System.out.println("BUG USB DSP-UsbDevice getDeviceName:"+UsbDevice.getDeviceName());
////            System.out.println("BUG USB DSP-UsbDevice getDeviceProtocol:"+UsbDevice.getDeviceProtocol());
////            System.out.println("BUG USB DSP-UsbDevice getDeviceSubclass:"+UsbDevice.getDeviceSubclass());
////            System.out.println("BUG USB DSP-UsbDevice getInterfaceCount:"+UsbDevice.getInterfaceCount());
//            //只有本公司的产品才作处理
//            if(UsbDevice!=null){
//                if (UsbDevice.getVendorId() == Define.USB_DSPHD_VID &&
//                        UsbDevice.getProductId() == Define.USB_DSPHD_PID) {
//                    mUsbDevice = UsbDevice;
//
//                    if (ACTION_USB_PERMISSION.equals(action)) {
//                        synchronized (this) {
//                            //UsbDevice = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
//                            //允许权限申请
//                            if(mUsbDevice!=null){
//                                if (mUsbDevice.getVendorId() == Define.USB_DSPHD_VID &&
//                                        mUsbDevice.getProductId() == Define.USB_DSPHD_PID) {
//                                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
//                                        findIntfAndEpt();
//                                    } else {
//                                        System.out.println("BUG USB permission denied for device " + mUsbDevice);
//                                    }
//                                }
//                            }
//
//                        }
//                    } else if (action.equals(UsbManager.ACTION_USB_DEVICE_ATTACHED)) {
//                        //ToastMsg(getResources().getString(R.string.USB_ATTACHED));
//                        if(mUsbDevice!=null){
//                            if (mUsbDevice.getVendorId() == Define.USB_DSPHD_VID &&
//                                    mUsbDevice.getProductId() == Define.USB_DSPHD_PID) {
//                                System.out.println("BUG USB DSPHD #ACTION_USB_DEVICE_ATTACHED");
//
//                                findIntfAndEpt();
////        	        			//
////        	        			MacCfg.USBConnected=true;
////        	        			Message msg = Message.obtain();
////        						msg.what = WHAT_IS_CONNECT_RIGHT;
////        						mHandler.sendMessage(msg);
//                            }
//                        }
//
//                        //searchUsbDevice();
//                    } else if (action.equals(UsbManager.ACTION_USB_DEVICE_DETACHED)) {
//                        System.out.println("BUG USB #ACTION_USB_DEVICE_DETACHED !");
//                        //ToastMsg(getResources().getString(R.string.USB_DETACHED));
//                        if(mUsbDevice!=null){
//                            if (mUsbDevice.getVendorId() == Define.USB_DSPHD_VID &&
//                                    mUsbDevice.getProductId() == Define.USB_DSPHD_PID) {
//
//                                mUsbDevice=null;
//                                MacCfg.USBConnected=false;
//                                Message msg = Message.obtain();
//                                msg.what = WHAT_IS_CONNECT_ERROR;//
//                                mHandler.sendMessage(msg);
//
//                                System.out.println("BUG USB DSPHD #ACTION_USB_DEVICE_DETACHED WHAT_IS_CONNECT_ERROR");
//                                //USBsend.setText("Did not found DSPHD");
//
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    };

//    private void searchUsbDevice() {
//        USBManager = (UsbManager) getSystemService(Context.USB_SERVICE);
//        if(USBManager!=null){
//            HashMap<String, UsbDevice> deviceList = USBManager.getDeviceList();
//
//            System.out.println("BUG USB USBManager.toString：" + String.valueOf(USBManager.toString()));
//            System.out.println("BUG USB deviceList.size：" + String.valueOf(deviceList.size()));
//            String size=String.valueOf(deviceList.size());
//            String vidString="VID=";
//            ////ToastMsg("deviceList.size()="+deviceList.size());
//
//            Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
//            ArrayList<String> USBDeviceList = new ArrayList<String>(); // 存放USB设备的数量
//            while (deviceIterator.hasNext()) {
//                UsbDevice device = deviceIterator.next();
//
//                USBDeviceList.add(String.valueOf(device.getVendorId()));
//                USBDeviceList.add(String.valueOf(device.getProductId()));
//
//                vidString+=device.getVendorId()+",";
//                ////ToastMsg("device.getVendorId()="+device.getVendorId());
//                // 在这里添加处理设备的代码
//                if (device.getVendorId() == Define.USB_DSPHD_VID &&
//                        device.getProductId() ==Define.USB_DSPHD_PID) {
//                    mUsbDevice = device;
//
//                    // 寻找接口和分配结点
//                    findIntfAndEpt();
//
//                    System.out.println("BUG USB DSP-HD getVendorId:"+device.getVendorId());
//                    System.out.println("BUG USB DSP-HD getProductId:"+device.getProductId());
//                    System.out.println("BUG USB DSP-HD getDeviceId:"+device.getDeviceId());
//                    System.out.println("BUG USB DSP-HD getDeviceName:"+device.getDeviceName());
//                    System.out.println("BUG USB DSP-HD getDeviceProtocol:"+device.getDeviceProtocol());
//                    System.out.println("BUG USB DSP-HD getDeviceSubclass:"+device.getDeviceSubclass());
//                    System.out.println("BUG USB DSP-HD getInterfaceCount:"+device.getInterfaceCount());
//
//                    //USBsend.setText("USB DSP-HD#VID:"+device.getVendorId()+",PID:"+device.getProductId());
//
//                    break;
//                }
//            }
//            //ToastMsg(size+"==="+vidString);
//        }
//    }

    // 寻找接口和分配结点
//    private void findIntfAndEpt() {
//        if (mUsbDevice == null) {
//            System.out.println("BUG USB 没有 USB 设备!");
//            //ToastMsg(getResources().getString(R.string.DidNotFindUSB));
//            return;
//        }
//        for (int i = 0; i < mUsbDevice.getInterfaceCount();) {
//            // 获取设备接口，一般都是一个接口，你可以打印getInterfaceCount()方法查看接
//            // 口的个数，在这个接口上有两个端点，OUT 和 IN
//            UsbInterface intf = mUsbDevice.getInterface(i);
//            System.out.println("BUG usb "+ i + " " + intf);
//            mUsbInterface = intf;
//
//            break;
//        }
//
//        if (mUsbInterface != null) {
//            UsbDeviceConnection connection = null;
//            // 判断是否有权限
//            if (USBManager.hasPermission(mUsbDevice)) {
//                // 打开设备，获取 UsbDeviceConnection 对象，连接设备，用于后面的通讯
//                connection = USBManager.openDevice(mUsbDevice);
//                if (connection == null) {
//                    return;
//                }
//                if (connection.claimInterface(mUsbInterface, true)) {
//                    System.out.println("BUG usb 找到接口");
//                    //DisplayToast("找到接口");
//                    mDeviceConnection = connection;
//                    getEndpoint(mDeviceConnection, mUsbInterface);
//
//
//                    MacCfg.USBConnected=true;
//                    Message msg = Message.obtain();
//                    msg.what = WHAT_IS_CONNECT_RIGHT;
//                    mHandler.sendMessage(msg);
//
//                } else {
//                    connection.close();
//                }
//            } else {
//                PendingIntent pi = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION),0);
//                USBManager.requestPermission(mUsbDevice, pi); //该代码执行后，系统弹出一个对话框，
//                System.out.println("BUG USB 没有权限");
//                //ToastMsg(getResources().getString(R.string.HaveNotPermission));
//            }
//        } else {
//            System.out.println("BUG usb 没有找到接口");
//            //ToastMsg(getResources().getString(R.string.HaveNotInterface));
//        }
//    }
    public static String Bytes2HexString(byte[] b) {
        String ret = "";
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            ret += hex.toUpperCase();
        }
        return ret;
    }

    public static byte uniteBytes(byte src0, byte src1) {
        byte _b0 = Byte.decode("0x" + new String(new byte[]{src0})).byteValue();
        _b0 = (byte) (_b0 << 4);
        byte _b1 = Byte.decode("0x" + new String(new byte[]{src1})).byteValue();
        byte ret = (byte) (_b0 ^ _b1);
        return ret;
    }

    public static byte[] HexString2Bytes(String src) {
        byte[] ret = new byte[8];
        byte[] tmp = src.getBytes();
        for (int i = 0; i < 8; i++) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
        }
        return ret;
    }

    /*********************************************************************/
    /*************************    UART通信方式 TODO  **********************/
    /*********************************************************************/


    /*********************************************************************/
    /*************************    测试包头 TODO  **********************/
    /*********************************************************************/

    private static int getBluetoothDeviceIDCom(String dev) {
        if (dev == null) {
            return Define.BT_Android_Type;
        }
        if (dev.contains(Define.BT_Paired_Name_DSP_HD_)) {
            return Define.BT_Android_Type;
        } else if (dev.contains(Define.BT_Paired_Name_DSP_NAKAMICHI)) {
            return Define.BT_ATS2825_Type;
        } else if (dev.contains(Define.BT_Paired_Name_DSP_HDx)) {
            return Define.BT_ATS2825_Type;
        } else if (dev.contains(Define.BT_Paired_Name_DEQ_80CH)) {
            return Define.BT_ATS2825_Type;
        } else if (dev.contains(Define.BT_Paired_Name_Pioneer)) {
            return Define.BT_ATS2825_Type;
        } else if (dev.contains(Define.BT_Paired_Name_DSP_Play)) {
            return Define.BT_ATS2825_Type;
        } else if (dev.contains(Define.BT_Paired_Name_DSP_ZD)) {
            return Define.BT_ATS2825_Type;
        } else {
            return Define.BT_Android_Type;
        }

    }

    //TODO
    public static void getCheckHeadFromBuf() {


        System.out.println("BUG LOADINIT getCheckHeadFromBuf MacCfg.HEAD_DATA++++++++=" + Integer.toHexString(MacCfg.HEAD_DATA) + "\n"
                + ",BT_CUR_ConnectedName=" + MacCfg.BT_CUR_ConnectedName + "\n"
                + ",BT_CUR_ConnectedID=" + MacCfg.BT_CUR_ConnectedID + "\n"
        );
        MacCfg.BluetoothDeviceID = getBluetoothDeviceIDCom(MacCfg.BT_CUR_ConnectedName);
        //MacCfg.BluetoothDeviceID = 0;
        MacCfg.HEAD_DATA_Index = 0;
        MacCfg.cntDSP = 0;
        //切换了蓝牙
        if (MacCfg.BT_CUR_ConnectedID == null) {
            return;
        }

        if (!MacCfg.BT_CUR_ConnectedID.equals(MacCfg.BT_OLD_ConnectedID)) {
            DataStruct.BOOL_CheckHEADOK = false;
            DataStruct.BOOL_CheckHEAD_ST = false;
            MacCfg.BT_OLD_ConnectedID = MacCfg.BT_CUR_ConnectedID;

        }
    }

    /*********************************************************************/
    /*************************   通信方式 TODO  **********************/
    /*********************************************************************/
    public static void APP_SendPack(byte[] btdata, int packsize) {
        if (MacCfg.COMMUNICATION_MODE == Define.COMMUNICATION_WITH_WIFI) {
            try {
                if (mSocketClient != null) {
                    OutputStream os = mSocketClient.getOutputStream();
                    os.write(btdata, 0, packsize); // 发送到数据给设备
                    os.flush();
                }
            } catch (IOException e) {
                System.out.println("BUG sThread buffer send error-leon");
            }
        } else if (MacCfg.COMMUNICATION_MODE == Define.COMMUNICATION_WITH_BLUETOOTH_SPP) {
        } else if (MacCfg.COMMUNICATION_MODE == Define.COMMUNICATION_WITH_BLUETOOTH_SPP_TWO) {
            SPP_LESendPack(btdata, packsize);
        } else if (MacCfg.COMMUNICATION_MODE == Define.COMMUNICATION_WITH_USB_HOST) {
            //USB_SendPack(btdata, packsize);
        } else if (MacCfg.COMMUNICATION_MODE == Define.COMMUNICATION_WITH_UART) {

        }
    }

    private void checkLedPackage() {
        int OutTimeCnt = 0;
        int RetryCnt = 0;
        boolean OK = true;
        ++MacCfg.LedPackageCnt;
        if (MacCfg.LedPackageCnt > 2) {
            MacCfg.LedPackageCnt = 0;

            //DSP发送LED包
            // 清空标志，避免数据乱串
            clearRecFlag();
            DataStruct.SendDeviceData.FrameType = 0xa2;
            DataStruct.SendDeviceData.DeviceID = 0x01;
            DataStruct.SendDeviceData.UserID = 0x00;
            DataStruct.SendDeviceData.DataType = Define.SYSTEM;
            DataStruct.SendDeviceData.ChannelID = Define.LED_DATA;
            // 请求信号灯
            DataStruct.SendDeviceData.DataID = 0x00;
            DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;
            DataStruct.SendDeviceData.PcCustom = 0x00;
            DataStruct.SendDeviceData.DataLen = 0x00;

            DataStruct.U0SendFrameFlg = NO;//有数据更新需要发送，清除标志
            DataStruct.U0RcvFrameFlg = NO; //有新接收到数据的标志，清除标志
            OutTimeCnt = 0;
            RetryCnt = 0;

            DataOptUtil.SendDataToDevice(true);//发送数据到设备
            while (DataStruct.U0RcvFrameFlg == NO) {
                if (!DataStruct.isConnecting) {
                    break;
                }
                // 连接超时处理
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    System.out.println("BUG sThread bt pack LED_DATA  InterruptedException");
                }
                if (BTS_Again == true) {
                    BTS_Again = false;
                    System.out.println("BUG ## Channel OutTimeCnt BTS_Again,LED_DATA Send again2");
                    // 清空标志，避免数据乱串
                    clearRecFlag();
                    DataOptUtil.SendDataToDevice(true);
                }
                //发送数据无回应时，单次重试的时间间隔，约1ms一个单位
                if (++OutTimeCnt > 1000) {
                    OutTimeCnt = 0;
                    // 清空标志，避免数据乱串
                    clearRecFlag();
                    DataOptUtil.SendDataToDevice(true);
                    // 4次发送数据无回应时，断开连接
                    if (++RetryCnt >= 2) {
                        Message msg = Message.obtain();
                        msg.what = WHAT_IS_CONNECT_ERROR;//
                        mHandler.sendMessage(msg);

                        Intent intentw = new Intent();
                        intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
                        intentw.putExtra("msg", Define.BoardCast_FlashUI_ConnectStateOFMsg);
                        intentw.putExtra("state", false);
                        mContext.sendBroadcast(intentw);
                        System.out.println("BUG sThread spp WHAT_IS_CONNECT_ERROR LED_DATA");
                        break;
                    }
                }
            }
        }
    }

    /**
     * 检查是否可以与DSP通信
     */
    private void checkClientHeadDataSendCmd() {
        int OutTimeCnt = 0;
        int RetryCnt = 0;
        boolean OK = true;

        if (DataStruct.BOOLDSPHeadFlg != Define.Statues_YES) {
            clearRecFlag();
            DataStruct.SendDeviceData.FrameType = 0xa2;
            DataStruct.SendDeviceData.DeviceID = 0x01;
            DataStruct.SendDeviceData.UserID = 0x00;
            DataStruct.SendDeviceData.DataType = Define.SYSTEM;
            DataStruct.SendDeviceData.ChannelID = Define.LED_DATA;
            DataStruct.SendDeviceData.DataID = 0x00;
            DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;
            DataStruct.SendDeviceData.PcCustom = 0x00;
            DataStruct.SendDeviceData.DataLen = 0x00;

            DataStruct.U0SendFrameFlg = NO;//有数据更新需要发送，清除标志
            DataStruct.U0RcvFrameFlg = NO; //有新接收到数据的标志，清除标志
            OutTimeCnt = 0;
            OK = true;
            DataOptUtil.SendDataToDevice(true);//发送数据到设备
            while (DataStruct.U0RcvFrameFlg == NO) {
                if (!DataStruct.isConnecting) {
                    OK = false;
                    break;
                }
                // 连接超时处理
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                }
                //发送数据无回应时，单次重试的时间间隔，约1ms一个单位
                if (++OutTimeCnt > 100) {
                    OutTimeCnt = 0;
                    OK = false;
                    break;
                }
            }
            if (OK) {
                DataStruct.BOOLDSPHeadFlg = Define.Statues_YES;
                DataStruct.comDSP = Define.COMT_DSP;
                DataStruct.BOOL_CheckHEADOK = true;
                DataStruct.BOOL_CheckHEAD_ST = false;
                Log.d(TAG, "BUG ##Get BOOLDSPHeadFlg OK");
                DataStruct.BOOLHaveMasterVol = false;
                //刷新界面连接进度
                Intent intentw = new Intent();
                intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
                intentw.putExtra("msg", Define.BoardCast_FlashUI_CancelDSPDataLoading);
                mContext.sendBroadcast(intentw);
                //在发送线程中取消音量，要在这里增加初始化包
                DataOptUtil.InitLoad();// 插入初始化申请包

            } else {//
                DataStruct.BOOLDSPHeadFlg = Define.Statues_NO;
                Log.e(TAG, "BUG ##Get BOOLDSPHeadFlg ERROR!!!");
                DataStruct.BOOL_CheckHEADOK = false;

            }
        }
    }


    private void checkDSPPlayStatuesSendCmd() {
        boolean OK = true;
        try {

            if (DataStruct.BOOLPlayHeadFlg == Define.Statues_Null) {
                if (MacCfg.BT_CUR_ConnectedName.contains(Define.BT_Paired_Name_DSP_Play)
                        || (MacCfg.BT_CUR_ConnectedName.contains(Define.BT_Paired_Name_Pioneer))
                        || (MacCfg.BT_CUR_ConnectedName.contains(Define.BT_Paired_Name_DSP_ZD))
                ) {
                    int mOutTimeCnt = 0;
                    int mRetryCnt = 0;
                    MDef.U0RecFrameFlg = false;
                    MDOptUtil.clearMusicRecFrameFlg();
                    MDOptUtil.getStatus(true);
                    while (!MDef.U0RecFrameFlg) {
                        if (!DataStruct.isConnecting) {
                            OK = false;
                            break;
                        }
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {

                        }
                        //发送数据无回应时，单次重试的时间间隔，约1ms一个单位
                        if (++mOutTimeCnt > 500) {
                            mOutTimeCnt = 0;
                            MDOptUtil.clearMusicRecFrameFlg();
                            MDOptUtil.getStatus(true);
                            // 4次发送数据无回应时，断开连接
                            if (++mRetryCnt >= 2) {
                                break;
                            }
                        }
                    }
                    if (MDef.U0RecFrameFlg) {
                        DataStruct.comPlay = Define.COMT_PLAY;
                        DataStruct.BOOLPlayHeadFlg = Define.Statues_YES;
                        DataOptUtil.saveBluetoothInfo(mContext);
                    } else {
                        DataStruct.comPlay = Define.COMT_OFF;
                        DataStruct.BOOLPlayHeadFlg = Define.Statues_NO;
                        //断开连接
                        Message msg = Message.obtain();
                        msg.what = WHAT_IS_CONNECT_ERROR;//
                        mHandler.sendMessage(msg);

                        Intent intentw = new Intent();
                        intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
                        intentw.putExtra("msg", Define.BoardCast_FlashUI_ConnectStateOFMsg);
                        intentw.putExtra("state", false);
                        mContext.sendBroadcast(intentw);

                        Log.e(TAG, "BUG ##Get BOOLPlayHeadFlg ERROR!!!");
                    }
                } else {
                    DataStruct.BOOLPlayHeadFlg = Define.Statues_NO;
                    DataStruct.comPlay = Define.COMT_OFF;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 播放器数据
     */
    private void DSPPlaySendCmd() {
        //DSP Play
        if (DataStruct.comPlay == Define.COMT_PLAY) {
            MDef.U0RecFrameFlg = false;
            //其他
            if ((DataStruct.MSendbufferList.size() > 0) && (!DataStruct.MSendbufferList.isEmpty())) {
                SPP_LESendPack(DataStruct.MSendbufferList.get(0), DataStruct.MSendbufferList.get(0).length);
                if (DataStruct.MSendbufferList.size() > 0) {//Dug:会引发空指针错误
                    DataStruct.MSendbufferList.remove(0);//成功发送，清除已经发送的列表
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    System.out.println("BUG sThread MDOptUtil.MusicBox_CMD InterruptedException");
                }
            } else {
               MDOptUtil.MusicBox_CMD(true);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    System.out.println("BUG sThread MDOptUtil.MusicBox_CMD InterruptedException");
                }
            }
            //有时DSP Play没有回任何数据，要增加延时，要不会蓝牙中断。
            if (!MDef.U0RecFrameFlg) {
                if (DataStruct.RcvDeviceData.SYS.input_source == 5) {
                   MDOptUtil.getStatus(true);//在更新列表中拔掉U盘
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    System.out.println("BUG sThread MDOptUtil InterruptedException");
                }
            }
        }
    }


    Runnable aRunnable = new Runnable() {
        @Override
        public void run() {
            while (true) {
                if (DataStruct.isConnecting) {
                    //发送LED包检测包头是否可用 //0：未知是否可用，1：明确不可用，2:明确可用
                    checkClientHeadDataSendCmd();
                    checkDSPPlayStatuesSendCmd();//获取播放状态(需要一直获取)
//                getMasterVolSendCmd(); 此客户改为在initLoad里获取
                    //包头是否确认OK,这里获取包头和DSPPlay 通信共用
                    if ((!DataStruct.BOOL_CheckHEADOK) && (DataStruct.BOOLDSPHeadFlg != Define.Statues_YES)) {
                        //DSP Play
                        DSPPlaySendCmd();
                    }

                    //主程序发送
                    DataStruct.comType = DataStruct.comDSP + DataStruct.comPlay;

                    //主程序发送
                    if ((DataStruct.comType == Define.COMT_DSP) || (DataStruct.comType == Define.COMT_DSPPLAY)) {
                        if (!DataOptUtil.isListNull()) {
                            sendListPackage();
                            DataStruct.comType = DataStruct.comDSP + DataStruct.comPlay;
                            if ((DataStruct.comType == Define.COMT_DSP) || (DataStruct.comType == Define.COMT_DSPPLAY)) {
                                if (!DataOptUtil.isListNull()) {//Dug:会引发空指针错误
                                    DataStruct.SendbufferList.remove(0);//成功发送，清除已经发送的列表
                                }

                                if (DataOptUtil.isListNull()) {
                                    if (!DataStruct.U0SynDataSucessFlg) {
                                        if (DataStruct.SEFF_USER_GROUP_OPT > 0) {//Bug:连接上蓝牙设备，但版本号不对
                                            DataStruct.SEFF_USER_GROUP_OPT = 0;
                                            Message msgss = Message.obtain();
                                            msgss.what = WHAT_IS_SYNC_SUCESS;
                                            mHandler.sendMessage(msgss);
                                        }
                                    }
                                }
                                // 发送进度条消息给主线程
                                if (progressDialogStep > 0) {
                                    Message msg = Message.obtain();
                                    msg.what = WHAT_IS_PROGRESSDIALOG;
                                    msg.arg1 = DataStruct.SendbufferList.size();
                                    mHandler.sendMessage(msg);
                                }
                            }
                        } else {
                            if ((DataStruct.comType == Define.COMT_DSP) || (DataStruct.comType == Define.COMT_DSPPLAY)) {
                                if (DataStruct.U0SynDataSucessFlg) {//同步初始化数据完成
                                    DataOptUtil.ComparedToSendData(true);//经比较之前的数据（初始化数等），如有更新则发送新数据
                                    MDef.BOOL_BluetoothBusy = false;
                                    if (DataStruct.SendDeviceData == null) {
                                        break;
                                    }
                                    if (DataStruct.U0SendFrameFlg == NO) {
                                        try {
                                            Thread.sleep(200);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        checkLedPackage();
                                        try {
                                            Thread.sleep(20);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        DSPPlaySendCmd();

                                    }
                                }
                            }

                        }
                    } else if (DataStruct.comType == Define.COMT_PLAY) {
                        //DSP Play
                        DSPPlaySendCmd();
                    }

                }

                //空闲时
                if (!DataStruct.isConnecting) {

                }

            }
        }
    };


    private void sendListPackage() {
        int OutTimeCnt = 0;
        int RetryCnt = 0;
        clearRecFlag();
        MDef.BOOL_BluetoothBusy = true;
        DataStruct.U0SendFrameFlg = NO;
        DataStruct.U0RcvFrameFlg = NO;//有新接收到数据的标志，清除标志
        OutTimeCnt = 0;
        RetryCnt = 0;
        if (!DataOptUtil.isListNull()) {//Dug:会引发空指针错误
            APP_SendPack(DataStruct.SendbufferList.get(0),
                    (DataStruct.SendbufferList.get(0).length));
        }
        while (DataStruct.U0RcvFrameFlg == NO) {
            if (!DataStruct.isConnecting) {
                break;
            }
            //Delay
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                System.out.println("BUG sThread Delay Thread.sleep InterruptedException");
            }
            if (BTS_Again) {
                BTS_Again = false;
                // 清空标志，避免数据乱串
                clearRecFlag();
                if (!DataOptUtil.isListNull()) {//Dug:会引发空指针错误
                    APP_SendPack(DataStruct.SendbufferList.get(0),
                            (DataStruct.SendbufferList.get(0).length));
                }
            }
            // 发送数据无回应时，单次重试的时间间隔
            if (++OutTimeCnt > 1000) {
                OutTimeCnt = 0;
                System.out.println("BUG BT Send OutTimeCnt !!! =" + OutTimeCnt);
                if (!DataOptUtil.isListNull()) {//Dug:会引发空指针错误
                    // 清空标志，避免数据乱串
                    clearRecFlag();
                    APP_SendPack(DataStruct.SendbufferList.get(0),
                            (DataStruct.SendbufferList.get(0).length));
                }
                if (++RetryCnt >= 3) {
                    /**/
                    Message msg = Message.obtain();
                    msg.what = WHAT_IS_CONNECT_ERROR;
                    mHandler.sendMessage(msg);
                    System.out.println("BUG sThread spp WHAT_IS_CONNECT_ERROR OutTimeCnt");

                    Intent intentw = new Intent();
                    intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
                    intentw.putExtra("msg", Define.BoardCast_FlashUI_ConnectStateOFMsg);
                    intentw.putExtra("state", false);
                    mContext.sendBroadcast(intentw);

                    break;

                }
            }
        }
    }

    /**
     * 系统主线程(不夹杂播放器的时候发送)   TODO
     */
    private Runnable sRunnable = new Runnable() {
        @Override
        public void run() {
            int OutTimeCnt = 0;
            int RetryCnt = 0;
            boolean OK = false;
            int IdNum = (int) Thread.currentThread().getId();
            System.out.println("BUG sRunnable thread:" + IdNum);
            Thread.currentThread().setName("sThread");
            while (true) {

                //LocationUtil.getNetIp();
                if (DataStruct.isConnecting) {
                    if (!DataOptUtil.isListNull()) {
                        DataStruct.U0HeadFlg = NO; // start rcv new head flag
                        DataStruct.U0DataCnt = 0;  // rcv counter set 0
                        DataStruct.U0HeadCnt = 0;
                        for (int i = 0; i <= 2; i++) {
                            MacCfg.HEAD[i] = 0;
                        }

                        DataStruct.U0SendFrameFlg = NO;
                        DataStruct.U0RcvFrameFlg = NO;//有新接收到数据的标志，清除标志
                        OutTimeCnt = 0;
                        RetryCnt = 0;
                        if (!DataOptUtil.isListNull()) {//Dug:会引发空指针错误
                            APP_SendPack(DataStruct.SendbufferList.get(0),
                                    (DataStruct.SendbufferList.get(0).length));
                        }
                        while (DataStruct.U0RcvFrameFlg == NO) {
                            if (!DataStruct.isConnecting) {
                                break;
                            }
                            //Delay
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException e) {
                                System.out.println("BUG sThread Delay Thread.sleep InterruptedException");
                            }
                            if (BTS_Again == true) {
                                BTS_Again = false;
                                // 清空标志，避免数据乱串
                                clearRecFlag();
                                if (!DataOptUtil.isListNull()) {//Dug:会引发空指针错误
                                    APP_SendPack(DataStruct.SendbufferList.get(0),
                                            (DataStruct.SendbufferList.get(0).length));
                                }
                            }
                            // 发送数据无回应时，单次重试的时间间隔
                            if (++OutTimeCnt > 800) {
                                OutTimeCnt = 0;
                                System.out.println("BUG BT Send OutTimeCnt !!! =" + OutTimeCnt);
                                if (!DataOptUtil.isListNull()) {//Dug:会引发空指针错误
                                    // 清空标志，避免数据乱串
                                    clearRecFlag();
                                    APP_SendPack(DataStruct.SendbufferList.get(0),
                                            (DataStruct.SendbufferList.get(0).length));
                                }
                                if (++RetryCnt >= 3) {

                                    /**/
                                    Message msg = Message.obtain();
                                    msg.what = WHAT_IS_CONNECT_ERROR;
                                    mHandler.sendMessage(msg);
                                    System.out.println("BUG sThread spp WHAT_IS_CONNECT_ERROR OutTimeCnt");

                                    Intent intentw = new Intent();
                                    intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
                                    intentw.putExtra("msg", Define.BoardCast_FlashUI_ConnectStateOFMsg);
                                    intentw.putExtra("state", false);
                                    mContext.sendBroadcast(intentw);

                                    break;

                                }
                            }
                        }
                        try {
                            if (!DataOptUtil.isListNull()) {//Dug:会引发空指针错误
                                DataStruct.SendbufferList.remove(0);//成功发送，清除已经发送的列表
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        if (DataOptUtil.isListNull()) {
                            if (!DataStruct.U0SynDataSucessFlg) {
                                if (DataStruct.SEFF_USER_GROUP_OPT > 0) {//Bug:连接上蓝牙设备，但版本号不对
                                    DataStruct.SEFF_USER_GROUP_OPT = 0;
                                    Message msgss = Message.obtain();
                                    msgss.what = WHAT_IS_SYNC_SUCESS;
                                    mHandler.sendMessage(msgss);
                                }
                            }
                        }
                        // 发送进度条消息给主线程
                        if (progressDialogStep > 0) {
                            Message msg = Message.obtain();
                            msg.what = WHAT_IS_PROGRESSDIALOG;
                            msg.arg1 = DataStruct.SendbufferList.size();
                            mHandler.sendMessage(msg);
                        }
                    } else {


                        if (DataStruct.U0SynDataSucessFlg) {//同步初始化数据完成
                            DataOptUtil.ComparedToSendData(true);//经比较之前的数据（初始化数等），如有更新则发送新数据
                            if (DataStruct.SendDeviceData == null) {
                                break;
                            }
                            if (DataStruct.U0SendFrameFlg == NO) {
                                checkLedPackage();
                                try {
                                    Thread.sleep(200);
                                } catch (InterruptedException e) {
                                    System.out.println("BUG sThread MDOptUtil InterruptedException");
                                }
                            }
                        }
                    }
                }


                //空闲时
                if (!DataStruct.isConnecting) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                    //检测通信以便再次连接
                    if (MacCfg.COMMUNICATION_MODE == Define.COMMUNICATION_WITH_WIFI) {
                        if ((mSocketClient == null) && (!DataStruct.isConnecting)) {
                            boolean wifiuse = CheckWifiStata();// 检测网络是否可用，并创建socket
                            System.out.println("BUG WIFI tRunnable CheckWifiStata():" + wifiuse);
                            if (wifiuse == true) {
                                NewSocketClient(); // 新建socket，注意在android4.0以后要在线程中创建socket，否则会抛出异常
                            }
                        }
                    } else if (MacCfg.COMMUNICATION_MODE == Define.COMMUNICATION_WITH_BLUETOOTH_SPP) {
                        if (CheckBTStata() && (mSocketClient == null)) {
                            if (!DataStruct.ManualConnecting) {
                                Message msg = Message.obtain();
                                msg.what = WHAT_IS_FLASH_BT_CONNECTED;
                                msg.arg1 = 0;
                                mHandler.sendMessage(msg);// 发送定时消息
                            }
                        }
                    } else if (MacCfg.COMMUNICATION_MODE == Define.COMMUNICATION_WITH_BLUETOOTH_LE) {
                        if (BLE_DEVICE_STATUS) {
//                        Message msg = Message.obtain();
//                        msg.what = WHAT_IS_CONNECT_RIGHT;//
//                        mHandler.sendMessage(msg);
                        }
                    } else if (MacCfg.COMMUNICATION_MODE == Define.COMMUNICATION_WITH_USB_HOST) {

                    } else if (MacCfg.COMMUNICATION_MODE == Define.COMMUNICATION_WITH_UART) {

                    }
                }

//            Message msg = Message.obtain();
//            msg.what = WHAT_IS_Show_Timer;
//            msg.arg1 = 0;
//            mHandler.sendMessage(msg);// 发送定时消息
            }
        }
    };


    private static void clearRecFlag() {
        DataStruct.U0HeadFlg = NO; // start rcv new head flag
        DataStruct.U0DataCnt = 0;  // rcv counter set 0
        DataStruct.U0HeadCnt = 0;
        BTS_Again = false;
        DataStruct.U0RcvFrameFlg = NO;
    }

    //用接收广播刷新UI TODO
    public class CHS_Broad_BroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getExtras().get("msg").toString();
            System.out.println("BUG msg:" + msg);

            if (msg.equals(Define.BoardCast_Load_LocalJson)) {
                if (!DataStruct.isConnecting) {
                    //ToastMsg(getResources().getString(R.string.off_line_mode));
                    return;
                }

                final String filePath = intent.getExtras().get("filePath").toString();

                int fileType = DataStruct.jsonRWOpt.getSEFFFileType(mContext, filePath);
                ////ToastMsg("GetFileType="+String.valueOf(fileType));
                if (fileType == 1) {
                    DataOptUtil.UpdateForJsonSingleData(filePath, mContext);
                } else if (fileType == 2) {
                    DataOptUtil.loadMacEffJsonData(filePath, mContext);
                } else {
                    //ToastMsg(getResources().getString(R.string.LoadSEff_Fail));
                }
            } else if (msg.equals(Define.BoardCast_SHARE_MAC_SEFF)) {
                DataOptUtil.ReadMacGroup(mContext);
            } else if (msg.equals(Define.BoardCast_SHARE_SEFF)) {
                DataOptUtil.ShareEffData(mContext);
            } else if (msg.equals(Define.BoardCast_SAVE_LOCAL_SEFF)) {
                Intent intentsave = new Intent();
                intentsave.setAction("android.intent.action.CHS_SEffUploadPage_BroadcastReceiver");
                intentsave.putExtra("msg", Define.BoardCast_EXIT_SEFFUploadPage);
                context.sendBroadcast(intentsave);

                boolean res = DataOptUtil.SaveSingleSEFF_JSON2Local(DataStruct.user.Get_fileName(), DataStruct.user.Get_fileName(), mContext);
                if (res) {
                    //ToastMsg(getResources().getString(R.string.Save_success));
                } else {
                    //ToastMsg(getResources().getString(R.string.Save_error));
                }
            } else if (msg.equals(Define.BoardCast_LOAD_SEFF_FROM_OTHER)) {
                if (!DataStruct.isConnecting) {
                    //ToastMsg(getResources().getString(R.string.off_line_mode));
                    return;
                }

                Intent intentsave = new Intent();
                intentsave.setAction("android.intent.action.CHS_SEffUploadPage_BroadcastReceiver");
                intentsave.putExtra("msg", Define.BoardCast_EXIT_SEFFUploadPage);
                context.sendBroadcast(intentsave);

                String path = intent.getExtras().get("URL").toString();
                System.out.println("BUG BUG msg.equals(Define.BoardCast_LOAD_SEFF_FROM_OTHER) URL:" + path);
                int fileType = DataStruct.jsonRWOpt.getSEFFFileType(mContext, path);
                ////ToastMsg("GetFileType="+String.valueOf(fileType));
                if (fileType == 1) {
                    DataOptUtil.UpdateForJsonSingleData(path, mContext);
                } else if (fileType == 2) {
                    DataOptUtil.loadMacEffJsonData(path, mContext);
                } else {
                    //ToastMsg(getResources().getString(R.string.LoadSEff_Fail));
                }
            } else if (msg.equals(Define.BoardCast_OPT_DisonnectDeviceBT)) {
                if (mChatService != null) {
                    mChatService.stop();
                    mChatService = null;
                }
                ToastUtil.showShortToast(mContext, mContext.getResources().getString(R.string.LinkOfMsg));
            } else if (msg.equals(Define.BoardCast_ConnectToSomeoneDevice)) {
                String address = intent.getStringExtra("address");
                String device = intent.getStringExtra("device");
                String type = intent.getStringExtra("type");

                MacCfg.BT_CUR_ConnectedName = device;
                MacCfg.BT_CUR_ConnectedID = address;

                System.out.println("####- BUG BLUETOOTH address->" + address
                        + ",device->" + device + ",type->" + type);

                mChatService = new BluetoothChatService(mContext, mHandlerOfSPP_LE);
                mOutStringBuffer = new StringBuffer("");
                MacCfg.COMMUNICATION_MODE = Define.COMMUNICATION_WITH_BLUETOOTH_SPP_TWO;
                // 获取本地蓝牙适配器
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                BluetoothDevice Dev = mBluetoothAdapter.getRemoteDevice(address);


                System.out.println("BUG 连接网络正常");
                if (ConnectionUtil.isConn(mContext)) {
                    System.out.println("BUG 连接上网络" + ConnectionUtil.isConn(mContext));
                    if (ConnectionUtil.isOpen(mContext)) {
                        // 试图连接到设备
                        mChatService.connect(Dev);
                        ToolsUtil.Toast(mContext, mContext.getString(R.string.TriesToConnectTo));
                    } else {
                        Intent intentw = new Intent();
                        intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
                        intentw.putExtra("msg", Define.BoardCast_FlashUI_TOOPENGPS);
                        //intentw.putExtra("state", status);
                        mContext.sendBroadcast(intentw);
                    }
                } else {
                    Intent intentw = new Intent();
                    intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
                    intentw.putExtra("msg", Define.BoardCast_FlashUI_TOOPENINTENT);
                    //intentw.putExtra("state", status);
                    mContext.sendBroadcast(intentw);
                }


                // ToolsUtil.Toast(mContext,mContext.getString(R.string.TriesToConnectTo));
            } else if (msg.equals(Define.BoardCast_FlashUI_MusicPage)) {
                /*
                final String type=intent.getExtras().get(MDef.MUSICMSG_MSGTYPE).toString();
                if(type.equals(MDef.MUSICPAGE_Status)){
                }else if(type.equals(MDef.MUSICPAGE_ID3)){
                }else if(type.equals(MDef.MUSICPAGE_List)){
                }else if(type.equals(MDef.MUSICPAGE_UpdateSongInList)){
                }else if(type.equals(MDef.MUSICPAGE_FileList)){
                }else if(type.equals(MDef.MUSICPAGE_FListShowLoading)){
                }else if(type.equals(MDef.MUSICPAGE_MListShowLoading)){
                }else if(type.equals(MDef.MUSICPAGE_ShowConnectAgainMsg)){
                }
                */
            } else if (msg.equals(Define.BoardCast_StopService)) {
//                MacCfg.BOOL_CanLinkUART = false;
                if (rThread != null) {
                    rThread.interrupt(); // 关闭接收线程
                    rThread = null;
                }
                if (sThread != null) {
                    sThread.interrupt(); // 关闭接收线程
                    sThread = null;
                }
                if (aThread != null) {
                    aThread.interrupt(); // 关闭接收线程
                    aThread = null;
                }

                if (tThread != null) {
                    tThread.interrupt(); // 关闭接收线程
                    tThread = null;
                }

                DataStruct.U0SynDataSucessFlg = false;
                DataStruct.isConnecting = false;
                MacCfg.bool_ReadMacGroup = false;
                DataStruct.U0HeadFlg = NO; // start rcv new head flag
                DataStruct.U0DataCnt = 0;  // rcv counter set 0
                DataStruct.U0HeadCnt = 0;

                DataStruct.BOOLDSPHeadFlg = Define.Statues_Null;//0：未知是否可用，1：明确不可用，2:明确可用
                DataStruct.BOOLPlayHeadFlg = Define.Statues_Null;//0：未知是否可用，1：明确不可用，2:明确可用
                DataStruct.BOOLHaveMasterVol = false;//false：未知获取主音量，true:已获取主音量

                DataStruct.U0RcvFrameFlg = NO; // 有新接收到数据的标志
                DataStruct.U0SendFrameFlg = NO; // 有数据要发送的标志
                DataStruct.U0SynDataError = false; // 同步初始化数据是否出错
                DataStruct.PcConnectFlg = NO;
                DataStruct.PcConnectCnt = 0;


                bool_OpBT = false; //打开蓝牙完成，连接不成功
                DataStruct.U0HeadFlg = NO;  //设置包头无效
                MacCfg.UpdataAduanceData = false;

                try {

                    if (mSocketClient != null) {
                        mSocketClient.close();
                        mSocketClient = null;
                    }

                    if (mChatService != null) {
                        mChatService.stop();
                        mChatService = null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                Timer timer = new Timer();
//                timer.schedule(new TimerTask(){
//                    @Override
//                    public void run(){
//                        disconnectSet();
//                        //closeSerialPort();
//                        stopSelf();//结束服务
//                    }
//                }, 388);

            }

        }
    }

    private void ToastMsg(String Msg) {
        if (null != mToast) {
            mToast.setText(Msg);
        } else {
            mToast = Toast.makeText(mContext, Msg, Toast.LENGTH_LONG);
        }
        mToast.show();
    }


    Runnable tRunnable = new Runnable() {
        @Override
        public void run() {
            int IdNum = (int) Thread.currentThread().getId();
            System.out.println("BUG tRunnable thread:" + IdNum);
            Thread.currentThread().setName("tThread");
            while (true) {


            }
        }
    };


    // TODO -------Client模式下，监听服务器消息线程的消息处理
    static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // int IdNum = (int)Thread.currentThread().getId();
            // System.out.println("mHandler thread:"+IdNum);
            super.handleMessage(msg);

            if (msg.what == WHAT_IS_NULL) {
                ;
            } else if (msg.what == WHAT_IS_CONNECT_ERROR) { // 连接失败、错误或者用户断开连接

                disconnectSet();

            } else if (msg.what == WHAT_IS_CONNECT_RIGHT) { // 连接网络正常

                System.out.println("BUG 连接网络正常");
                if (ConnectionUtil.isConn(mContext)) {
                    System.out.println("BUG 连接上网络" + ConnectionUtil.isConn(mContext));
                    if (ConnectionUtil.isOpen(mContext)) {
                        getCheckHeadFromBuf();
                        DataStruct.isConnecting = true;
                        DataOptUtil.InitLoad();// 插入初始化申请包
                        // LocationUtils.getInstance().getLocations(mContext);
                        ToolsUtil.Toast(mContext, mContext.getString(R.string.TriesToConnectTo));
                    } else {
                        Intent intentw = new Intent();
                        intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
                        intentw.putExtra("msg", Define.BoardCast_FlashUI_TOOPENGPS);
                        //intentw.putExtra("state", status);
                        mContext.sendBroadcast(intentw);
                    }
                } else {
                    Intent intentw = new Intent();
                    intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
                    intentw.putExtra("msg", Define.BoardCast_FlashUI_TOOPENINTENT);
                    //intentw.putExtra("state", status);
                    mContext.sendBroadcast(intentw);
                }


            } else if (msg.what == WHAT_IS_TRYS_TO_CON_DSPPLAYMSG) { // 连接网络正常
                //ToolsUtil.Toast(mContext,mContext.getString(R.string.TriesToConnectTo)+" DSP Play");
            } else if (msg.what == WHAT_IS_ShowGetERRMasterVolMsg) { // 连接网络正常
                ToolsUtil.Toast(mContext, mContext.getString(R.string.GetMasterVolMsg));
            } else if (msg.what == WHAT_IS_Show_UnKnowMacType_Msg) { // 连接网络正常
                ToolsUtil.Toast(mContext, mContext.getString(R.string.UnknowMacType));
            } else if (msg.what == WHAT_IS_LongPress_INC_SUB) {  // 刷新长按按键的连续增减界面
                //FlashButtonLongPressUI();
            } else if (msg.what == WHAT_IS_Show_Timer) {  // 刷新长按按键的连续增减界面
                //FlashButtonLongPressUI();
                System.out.println("BUG 老是inlaid");
//                boolean status = DataOptUtil.getSoundStatus(mContext);
//                if(status != MacCfg.BOOL_SoundStatues){
//                    MacCfg.BOOL_SoundStatues = status;
//                    //刷新界面连接进度
//                    Intent intentw=new Intent();
//                    intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
//                    intentw.putExtra("msg", Define.BoardCast_FlashUI_FlashSoundStatus);
//                    intentw.putExtra("state", status);
//                    mContext.sendBroadcast(intentw);
//                }
            } else if (msg.what == WHAT_IS_FLASH_BT_CONNECTED) {  // 刷新蓝牙的音频通道是否已经连接本机

            } else if (msg.what == WHAT_IS_Address) {
                Intent intentw = new Intent();
                intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
                intentw.putExtra("msg", Define.BoardCast_FlashUI_ADDRESS);
                intentw.putExtra("txtAddress", LocationUtils.getInstance().getLocations(mContext));
                mContext.sendBroadcast(intentw);
            } else if (msg.what == WHAT_IS_INIT_LOADING) {//当开机发送数据后，到一个正解的应答后开始显示加载进度
                //刷新界面连接进度
                Intent intentw = new Intent();
                intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
                intentw.putExtra("msg", Define.BoardCast_FlashUI_ShowLoading);
                intentw.putExtra("state", false);
                mContext.sendBroadcast(intentw);
            } else if (msg.what == WHAT_IS_SYNC_SUCESS) {
                connectSet();
                DataOptUtil.saveBluetoothInfo(mContext);
                DataOptUtil.saveHEAD_DATA(mContext);
            } else if (msg.what == WHAT_IS_NEW_DATA) { // 有新数据到来
            } else if (msg.what == WHAT_IS_SEND_DATA) { // 定时器消息,发送数据给设备
                if (++WifiInfoTimerCnt > 10) {
                    WifiInfoTimerCnt = 0;
                }
            } else if (msg.what == WHAT_IS_PROGRESSDIALOG) { // 更新progressDialog

            } else if (msg.what == WHAT_IS_FLASH_SYSTEM_DATA) {
                Intent intentw = new Intent();
                intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
                intentw.putExtra("msg", Define.BoardCast_FlashUI_SYSTEM_DATA);
                intentw.putExtra("state", false);
                mContext.sendBroadcast(intentw);
            } else if (msg.what == WHAT_IS_LEDUP_SOURCE) { // 信号灯实时更新音源
                //if (DataStruct.CurMacMode.BOOL_INPUT_SOURCE) {
                DataStruct.RcvDeviceData.SYS.input_source = msg.arg1;
                DataStruct.SendDeviceData.SYS.input_source = msg.arg1;
                System.out.println("BUG 这里刷新了嘛");
                //刷新界面连接
                Intent intentw = new Intent();
                intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
                intentw.putExtra("msg", Define.BoardCast_FlashUI_InputSource_Val);
                intentw.putExtra("state", false);
                mContext.sendBroadcast(intentw);
                //}

            } else if (msg.what == WHAT_IS_SYNC_GROUP_NAME) {
            } else if (msg.what == WHAT_IS_RETURN_EXIT) {
                // isExit = false;
            } else if (msg.what == WHAT_IS_CLOSE_BT) {
                bool_CloseBT = false;
            } else if (msg.what == WHAT_IS_RESET_EQ_CHNAME) {

            } else if (msg.what == WHAT_IS_GroupClick) {

            } else if (msg.what == WHAT_IS_BT_TIME_OUT) {//蓝牙超时
                if ((mSocketClient == null) && (!DataStruct.isConnecting)) {
                    bool_BT_ConTimeOut = true;
                    bool_BT_CTO_Send = false;

                }
            } else if (msg.what == WHAT_IS_MENU_LOCKED) {
//				bool_MenuLocked=false;
            } else if (msg.what == WHAT_IS_Wait) {
//				bool_Wait=false;
            } else if (msg.what == WHAT_IS_LOADING) {
                //  LoadingCancle();
            } else if (msg.what == WHAT_IS_BT_SCAN) {
                startScanBluetooth();
            }
        }
    };


    public static void disconnectSet() {
        DataStruct.U0SynDataSucessFlg = false;
        DataStruct.isConnecting = false;
        MacCfg.bool_ReadMacGroup = false;
        DataStruct.U0HeadFlg = NO; // start rcv new head flag
        DataStruct.U0DataCnt = 0;  // rcv counter set 0
        DataStruct.U0HeadCnt = 0;
        // DataStruct.SendDeviceData=null;

        DataStruct.U0RcvFrameFlg = NO; // 有新接收到数据的标志
        DataStruct.U0SendFrameFlg = NO; // 有数据要发送的标志
        DataStruct.U0SynDataError = false; // 同步初始化数据是否出错
        DataStruct.PcConnectFlg = NO;
        DataStruct.PcConnectCnt = 0;
        bool_OpBT = false; //打开蓝牙完成，连接不成功
        DataStruct.U0HeadFlg = NO;  //设置包头无效

        //刷新界面连接按键
        Intent intentw = new Intent();
        intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
        intentw.putExtra("msg", Define.BoardCast_FlashUI_ConnectState);
        intentw.putExtra("state", false);
        mContext.sendBroadcast(intentw);
        if (DeviceVerErrorFlg) {
            //刷新界面连接
            Intent intentd = new Intent();
            intentd.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
            intentd.putExtra("msg", Define.BoardCast_FlashUI_DeviceVersionsErr);
            intentd.putExtra("state", false);
            mContext.sendBroadcast(intentd);
        }
        //延时后再执行关闭接口，保存正在发送的数据发送完成，否则会引起发送线程的崩溃
        //解放连接资源


        try {
            if (mSocketClient != null) {
                mSocketClient.close();
                mSocketClient = null;
            }

            if (mChatService != null) {
                mChatService.stop();
                mChatService = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void connectToDevice() {
        if (MacCfg.COMMUNICATION_MODE == Define.COMMUNICATION_WITH_UART) {
            //OpenSerialPort();
        }
    }

    private static void connectSet() {
        DataOptUtil.ComparedToSendData(false); //同步数据
        System.out.println("BUG 同步完成");

        DataStruct.U0SynDataSucessFlg = true; //同步数据完成标记
        DataStruct.isConnecting = true;  //蓝牙可正常连接
        bool_BT_ConTimeOut = false;
        BLE_DEVICE_STATUS = true;
        DataStruct.SEFF_USER_GROUP_OPT = 0;

        //刷新界面连接按键
        Intent intentwt = new Intent();
        intentwt.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
        intentwt.putExtra("msg", Define.BoardCast_FlashUI_ConnectState);
        intentwt.putExtra("state", true);
        mContext.sendBroadcast(intentwt);


        Intent intentm = new Intent();
        intentm.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
        intentm.putExtra("msg", Define.BoardCast_FlashUI_ShowSucessMsg);
        intentm.putExtra("state", true);
        mContext.sendBroadcast(intentm);
        //刷新界面连接
        Intent intentw = new Intent();
        intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
        intentw.putExtra("msg", Define.BoardCast_FlashUI_AllPage);
        intentw.putExtra("state", true);
        mContext.sendBroadcast(intentw);

    }

    // 接收数据，检包
    public static void ReceiveDataFromDevice(int data, int type) {

        if (DataStruct.RcvDeviceData == null) {
            return;
        }

        if (MacCfg.Mcu == 7|| MacCfg.Mcu==8) {
            MDOptUtil.RecMusicBoxDataFromDevice(mContext, data & 0xff, Define.COMMUNICATION_WITH_BLUETOOTH_SPP_TWO);
        }

        //判断包头，起始符，贞头

        if (DataStruct.U0HeadFlg == NO) {// DataStruct.U0HeadFlg=NO,rcv head data
            if ((data == MacCfg.HEAD_DATA) && (DataStruct.U0HeadCnt == 0)) {
                DataStruct.U0HeadCnt++;
            } else if ((data == MacCfg.HEAD_DATA) && (DataStruct.U0HeadCnt == 1)) {
                DataStruct.U0HeadCnt++;
            } else if ((data == MacCfg.HEAD_DATA) && (DataStruct.U0HeadCnt == 2)) {
                DataStruct.U0HeadCnt++;
            } else if (data == DataStruct.FRAME_STA && DataStruct.U0HeadCnt == 3) {// Have rcv // 0xff,0xff,0xff,0xb1
                DataStruct.U0HeadFlg = YES;
                DataStruct.U0HeadCnt = 0;
            } else {
                DataStruct.U0HeadCnt = 0;
            }
            DataStruct.U0DataCnt = 0; // Ready rcv data, rcv cnt set 0
        }
        //有效包
        else if (DataStruct.U0HeadFlg == YES) {
            DataStruct.U0HeadCnt = 0;

            //BUG
            if (DataStruct.U0DataCnt >= DataStruct.RcvDeviceData.DataBuf.length - 1) {
                DataStruct.U0HeadFlg = NO; // start rcv new head flag
                DataStruct.U0DataCnt = 0;  // rcv counter set 0
                BTS_Again = true;
                return;
            }

            DataStruct.RcvDeviceData.DataBuf[DataStruct.U0DataCnt] = data;
            DataStruct.U0DataCnt++;
            if (DataStruct.U0DataCnt >= (DataStruct.RcvDeviceData.DataBuf[8]
                    + DataStruct.RcvDeviceData.DataBuf[9] * 256 + DataStruct.CMD_LENGHT - 4)) // ²»°üº¬°üÍ·ºÍÆðÊ¼·û£¬¼õ4
            {
                DataStruct.RcvDeviceData.FrameType = DataStruct.RcvDeviceData.DataBuf[0];
                DataStruct.RcvDeviceData.DeviceID = DataStruct.RcvDeviceData.DataBuf[1];
                DataStruct.RcvDeviceData.UserID = DataStruct.RcvDeviceData.DataBuf[2];
                DataStruct.RcvDeviceData.DataType = DataStruct.RcvDeviceData.DataBuf[3];
                DataStruct.RcvDeviceData.ChannelID = DataStruct.RcvDeviceData.DataBuf[4];
                DataStruct.RcvDeviceData.DataID = DataStruct.RcvDeviceData.DataBuf[5];
                DataStruct.RcvDeviceData.PCFadeInFadeOutFlg = DataStruct.RcvDeviceData.DataBuf[6];
                DataStruct.RcvDeviceData.PcCustom = DataStruct.RcvDeviceData.DataBuf[7];
                DataStruct.RcvDeviceData.DataLen = DataStruct.RcvDeviceData.DataBuf[8]
                        + DataStruct.RcvDeviceData.DataBuf[9] * 256;
                DataStruct.RcvDeviceData.CheckSum = DataStruct.RcvDeviceData.DataBuf[DataStruct.RcvDeviceData.DataLen
                        + DataStruct.CMD_LENGHT - 6];
                DataStruct.RcvDeviceData.FrameEnd = DataStruct.RcvDeviceData.DataBuf[DataStruct.RcvDeviceData.DataLen
                        + DataStruct.CMD_LENGHT - 5];

                DataStruct.U0HeadFlg = NO; // start rcv new head flag
                DataStruct.U0DataCnt = 0;  // rcv counter set 0

                if (DataStruct.RcvDeviceData.FrameEnd == DataStruct.FRAME_END) { // 判断包尾是否正确
                    int sum = 0;
                    for (int i = 0; i < (DataStruct.RcvDeviceData.DataLen
                            + DataStruct.CMD_LENGHT - 6); i++) {
                        sum ^= DataStruct.RcvDeviceData.DataBuf[i];
                    }

                    if (sum == DataStruct.RcvDeviceData.CheckSum) { // 通过校验
                        DataStruct.PcConnectFlg = YES;
                        DataStruct.PcConnectCnt = 0;
                        DataStruct.ComType = type; // 通讯类型

                        DataStruct.comDSP = Define.COMT_DSP;
                        ProcessRcvData();
                    } else {
                        System.out.println("BUG XXXXXXXXCheckSum");
                    }
                } else {
                    System.out.println("BUG XXXXXXXXFRAME_END");
                }
            }
        }

    }

    // 处理检包后的数据，分类存储器起来 TODO
    public static void ProcessRcvData() {
//		System.out.println("BUG XXXXXXXXXXXXXXXXXXXXXX");
//
//		System.out.println("BUG ProcessRcvData FrameType:" + DataStruct.RcvDeviceData.FrameType);
//		System.out.println("BUG ProcessRcvData DataType:" + DataStruct.RcvDeviceData.DataType);
//		System.out.println("BUG ProcessRcvData ChannelID:" + DataStruct.RcvDeviceData.ChannelID);

        if (DataStruct.RcvDeviceData.FrameType == DataStruct.DATA_ACK) { // 数据回应帧
            if (DataStruct.RcvDeviceData.DataType == Define.MUSIC) {/*读取整MUSIC.2通道的整个数据*/
                if (DEBUG)
                    System.out.println("BUG ## Channel MUSIC DataLen:" + DataStruct.RcvDeviceData.DataLen);
                if (DataStruct.CurMacMode.BOOL_USE_INS) {
                    if (DataStruct.RcvDeviceData.DataLen == Define.INS_LEN) {
                        DataOptUtil.FillRecDataStruct(Define.MUSIC, DataStruct.RcvDeviceData.ChannelID, DataStruct.RcvDeviceData.DataBuf, true);
                        //保存整机数据
                    }
                } else {
                    if (DataStruct.RcvDeviceData.DataLen == Define.IN_LEN) {
                        DataOptUtil.FillRecDataStruct(Define.MUSIC, 0, DataStruct.RcvDeviceData.DataBuf, true);
                        //System.out.println("BUG ---Master Valume:" + DataStruct.RcvDeviceData.IN_CH[0].Valume);
                        //保存整机数据
                        if (MacCfg.bool_ReadMacGroup) {
                            for (int i = 0; i < Define.IN_LEN; i++) {
                                DataStruct.MAC_DataBuf.data[DataStruct.RcvDeviceData.UserID].music.music[0][i] = DataStruct.RcvDeviceData.DataBuf[10 + i];
                            }
                        }
                    }
                }

            } else if (DataStruct.RcvDeviceData.DataType == Define.OUTPUT) {
                System.out.println("BUG ## Channel OUTPUT ChannelID:" + DataStruct.RcvDeviceData.ChannelID);
                if (DataStruct.RcvDeviceData.DataLen == Define.OUT_LEN) {

                    if (MacCfg.bool_ReadMacGroup) {
                        System.out.println("BUG ## MAC UserID:" + DataStruct.RcvDeviceData.UserID
                                + "-ChannelID=" + DataStruct.RcvDeviceData.ChannelID
                                + ",DataStruct.OUT_LEN=" + DataStruct.RcvDeviceData.DataLen);
                        for (int i = 0; i < Define.OUT_LEN; i++) {
                            DataStruct.MAC_DataBuf.data[DataStruct.RcvDeviceData.UserID].output.output[DataStruct.RcvDeviceData.ChannelID][i] = DataStruct.RcvDeviceData.DataBuf[10 + i];
                        }

                        //保存够一组数据了
                        if (DataStruct.RcvDeviceData.ChannelID == (DataStruct.CurMacMode.Out.OUT_CH_MAX - 1)) {
                            //保存够整机数据了
                            if (DataStruct.RcvDeviceData.UserID == DataStruct.CurMacMode.MAX_USE_GROUP) {
                                //填充整机数据
                                DataStruct.MAC_DataBuf.Set_chs(DataOptUtil.getCHS(mContext));
                                DataStruct.MAC_DataBuf.Set_client(DataOptUtil.getClient());
                                DataStruct.MAC_DataBuf.Set_data_info(DataOptUtil.getData_info());
                                DataStruct.MAC_DataBuf.Set_SystemData(DataOptUtil.getSystemData());

                                if (DataStruct.bool_ShareOrSaveMacSEFF) {
                                    DataOptUtil.ShareMacEffData(mContext);
                                } else {
                                    DataOptUtil.SaveMACSEFF_JSON2Local(DataStruct.fileNameString, mContext);
                                }
                                MacCfg.bool_ReadMacGroup = false;
                            }
                        }
                    } else {//读取整机时不刷新数据
                        DataOptUtil.FillRecDataStruct(Define.OUTPUT, DataStruct.RcvDeviceData.ChannelID, DataStruct.RcvDeviceData.DataBuf, true);
                        //DataOptUtil.SaveEQTo_EQ_Buf(RcvDeviceData.ChannelID);

                        //检测数据是否出错
                        if (DataStruct.RcvDeviceData.ChannelID == (DataStruct.CurMacMode.Out.OUT_CH_MAX - 1)) {
                            if (DataStruct.U0SynDataError) {
                                //showResetMucDialog();
                            }
                        }
                    }
                }
            } else if (DataStruct.RcvDeviceData.DataType == Define.SYSTEM) {
                switch (DataStruct.RcvDeviceData.ChannelID) {
                    case Define.GROUP_NAME:
                        for (int i = 0; i < 16; i++) {
                            DataStruct.RcvDeviceData.SYS.UserGroup[DataStruct.RcvDeviceData.UserID][i] = (char) DataStruct.RcvDeviceData.DataBuf[10 + i]; // 接收用户名
                            DataStruct.MAC_DataBuf.data[DataStruct.RcvDeviceData.UserID].group_name[i] = (char) DataStruct.RcvDeviceData.DataBuf[10 + i];
                        }

                        break;

                    case Define.PC_SOURCE_SET:
                        DataStruct.RcvDeviceData.SYS.input_source = DataStruct.RcvDeviceData.DataBuf[10]; // 输入源(之前系统中的输入源)
                        // 高 1
                        // 低
                        // AUX 3
                        // 蓝牙  2
                        // 光纤
                        DataStruct.RcvDeviceData.SYS.aux_mode = DataStruct.RcvDeviceData.DataBuf[11]; // 低电平模式
                        // 有3种
                        // 0:4个AUX
                        // 1:..................
                        DataStruct.RcvDeviceData.SYS.device_mode = DataStruct.RcvDeviceData.DataBuf[12];  // 本字节第二位0x02
                        // 代表有数字音源输入，字节第一位0x01代表有蓝牙输入，否则没有该模块，PC不能切换至此音源
                        DataStruct.RcvDeviceData.SYS.hi_mode = DataStruct.RcvDeviceData.DataBuf[13];// 保留
                        DataStruct.RcvDeviceData.SYS.blue_gain = DataStruct.RcvDeviceData.DataBuf[14];// 保留
                        DataStruct.RcvDeviceData.SYS.aux_gain = DataStruct.RcvDeviceData.DataBuf[15];// 保留
                        DataStruct.RcvDeviceData.SYS.DigitMod = DataStruct.RcvDeviceData.DataBuf[16];// 保留
                        DataStruct.RcvDeviceData.SYS.none5 = DataStruct.RcvDeviceData.DataBuf[17];// 保留

                        break;

                    case Define.MCU_BUSY:
                        // U0BusyFlg = YES;
                        break;
                    case Define.SOFTWARE_VERSION:
                        char[] databuf = new char[12];
                        databuf[11] = 0;
                        for (int i = 0; i < 11; i++) {
                            databuf[i] = (char) DataStruct.RcvDeviceData.DataBuf[10 + i];
                        }
                        MacCfg.DeviceVerString = String.valueOf(databuf);

                        System.out.println("BUG 这个的值为"+MacCfg.DeviceVerString);

                        if (!(MacCfg.DeviceVerString.contains(MacCfg.MCU_Version))) {
                            MacCfg.DeviceVerString = "";
                            DeviceVerErrorFlg = true;
                            return;
                        } else {
                            DataStruct.B_InitLoad = true;
                        }


                        //当开机发送数据后，到一个正确的MCU_Versions后开始显示加载进度
                        if (DataStruct.B_InitLoad) {
                            DataStruct.B_InitLoad = false;

                            Message msgil = Message.obtain();
                            msgil.what = WHAT_IS_INIT_LOADING;
                            mHandler.sendMessage(msgil);
                        }

                        bool_OpBT = false;//打开蓝牙完成
                        break;

                    case Define.SYSTEM_DATA:
                        DataStruct.RcvDeviceData.SYS.main_vol = DataStruct.RcvDeviceData.DataBuf[10]
                                + DataStruct.RcvDeviceData.DataBuf[11] * 256;  // 输出总音量(之前输入结构中的总音量)
                        // -60~0dB
                        DataStruct.RcvDeviceData.SYS.alldelay = DataStruct.RcvDeviceData.DataBuf[12]; // DSP纯延时
                        // 0~100
                        // 0.01s~1s
                        DataStruct.RcvDeviceData.SYS.noisegate_t = DataStruct.RcvDeviceData.DataBuf[13];// 噪声门
                        // -120dbu~+10dbu,stp:1dbu,实际发送0~130
                        DataStruct.RcvDeviceData.SYS.AutoSource = DataStruct.RcvDeviceData.DataBuf[14];    // 自动音源开关
                        // 0关
                        // 1开
                        DataStruct.RcvDeviceData.SYS.AutoSourcedB = DataStruct.RcvDeviceData.DataBuf[15];  // 自动音源检测的信号阀值
                        // -120dB~0dB
                        DataStruct.RcvDeviceData.SYS.MainvolMuteFlg = DataStruct.RcvDeviceData.DataBuf[16];// 静音临时标志，这个标志关机不保存，注意特别处理
                        DataStruct.RcvDeviceData.SYS.none6 = DataStruct.RcvDeviceData.DataBuf[17];// 保留
                        break;
                    case Define.SYSTEM_SPK_TYPE:
//                        for(int i=0;i<8;i++){
//                            if(DataStruct.RcvDeviceData.DataBuf[10+i] > MacCfg.SPK_MAX){
//                                DataStruct.RcvDeviceData.DataBuf[10+i] = 0;
//                            }
//
//                            if(DataStruct.RcvDeviceData.DataBuf[10+i] < 0){
//                                DataStruct.RcvDeviceData.DataBuf[10+i]=0;
//                            }
//
//                        }
                        DataStruct.RcvDeviceData.SYS.out1_spk_type = DataStruct.RcvDeviceData.DataBuf[10];
                        DataStruct.RcvDeviceData.SYS.out2_spk_type = DataStruct.RcvDeviceData.DataBuf[11];
                        DataStruct.RcvDeviceData.SYS.out3_spk_type = DataStruct.RcvDeviceData.DataBuf[12];
                        DataStruct.RcvDeviceData.SYS.out4_spk_type = DataStruct.RcvDeviceData.DataBuf[13];
                        DataStruct.RcvDeviceData.SYS.out5_spk_type = DataStruct.RcvDeviceData.DataBuf[14];
                        DataStruct.RcvDeviceData.SYS.out6_spk_type = DataStruct.RcvDeviceData.DataBuf[15];
                        DataStruct.RcvDeviceData.SYS.out7_spk_type = DataStruct.RcvDeviceData.DataBuf[16];
                        DataStruct.RcvDeviceData.SYS.out8_spk_type = DataStruct.RcvDeviceData.DataBuf[17];
                        System.out.println("BUG 进入了直列了Proc_Amp_Mode吗"+DataStruct.RcvDeviceData.SYS.out8_spk_type);
                        break;
                    case Define.SYSTEM_SPK_TYPEB:
                        DataStruct.RcvDeviceData.SYS.out9_spk_type = DataStruct.RcvDeviceData.DataBuf[10];
                        DataStruct.RcvDeviceData.SYS.out10_spk_type = DataStruct.RcvDeviceData.DataBuf[11];
                        DataStruct.RcvDeviceData.SYS.out11_spk_type = DataStruct.RcvDeviceData.DataBuf[12];
                        DataStruct.RcvDeviceData.SYS.out12_spk_type = DataStruct.RcvDeviceData.DataBuf[13];
                        DataStruct.RcvDeviceData.SYS.out13_spk_type = DataStruct.RcvDeviceData.DataBuf[14];
                        DataStruct.RcvDeviceData.SYS.out14_spk_type = DataStruct.RcvDeviceData.DataBuf[15];
                        DataStruct.RcvDeviceData.SYS.out15_spk_type = DataStruct.RcvDeviceData.DataBuf[16];
                        DataStruct.RcvDeviceData.SYS.Proc_Amp_Mode = DataStruct.RcvDeviceData.DataBuf[17];
                        break;

                    case Define.SYSTEM_NINE:
//                        for (int i = 0; i <7 ; i++) {
//                            DataStruct.RcvDeviceData.SYS.none9[i] = DataStruct.RcvDeviceData.DataBuf[10+i];
//                        }
//                        DataStruct.RcvDeviceData.SYS.Hardware = DataStruct.RcvDeviceData.DataBuf[17];
                        break;

                    case Define.LED_DATA:
                        boolean isSend = false;
                            if (MacCfg.input_sourcetemp != DataStruct.RcvDeviceData.DataBuf[10]) {
                                MacCfg.input_sourcetemp = DataStruct.RcvDeviceData.DataBuf[10];
                                Message msg = Message.obtain();
                                msg.what = WHAT_IS_LEDUP_SOURCE;
                                msg.arg1 = MacCfg.input_sourcetemp;// 信号灯实时输入源
                                mHandler.sendMessage(msg);
                            }


                            if (DataStruct.SendDeviceData.SYS.input_source != DataStruct.RcvDeviceData.SYS.input_source) {
                                isSend = true;
                            }

                            if (MacCfg.SourceDefine_tmp != DataStruct.RcvDeviceData.DataBuf[11]) {
                                MacCfg.SourceDefine_tmp = DataStruct.RcvDeviceData.DataBuf[11];
                                if (MacCfg.SourceDefine_tmp != DataStruct.RcvDeviceData.SYS.aux_mode) {
                                    isSend = true;
                                    DataStruct.RcvDeviceData.SYS.aux_mode = MacCfg.SourceDefine_tmp;
                                    DataStruct.SendDeviceData.SYS.aux_mode = DataStruct.RcvDeviceData.SYS.aux_mode;
                                }
                            }
                            if (MacCfg.Opt_Vol_Vol_tmp != DataStruct.RcvDeviceData.DataBuf[12]) {
                                MacCfg.Opt_Vol_Vol_tmp = DataStruct.RcvDeviceData.DataBuf[12];
                                if (MacCfg.Opt_Vol_Vol_tmp != DataStruct.RcvDeviceData.SYS.device_mode) {
                                    isSend = true;
                                    DataStruct.RcvDeviceData.SYS.device_mode = MacCfg.Opt_Vol_Vol_tmp;
                                    DataStruct.SendDeviceData.SYS.device_mode = DataStruct.RcvDeviceData.SYS.device_mode;
                                }
                            }


                            if (MacCfg.High_Vol_tmp != DataStruct.RcvDeviceData.DataBuf[13]) {
                                MacCfg.High_Vol_tmp = DataStruct.RcvDeviceData.DataBuf[13];
                                if (MacCfg.High_Vol_tmp != DataStruct.RcvDeviceData.SYS.hi_mode) {
                                    isSend = true;
                                    DataStruct.RcvDeviceData.SYS.hi_mode = MacCfg.High_Vol_tmp;
                                    DataStruct.SendDeviceData.SYS.hi_mode = DataStruct.RcvDeviceData.SYS.hi_mode;
                                }
                            }

//                        System.out.println("BUG　接收到的值为\t"+DataStruct.RcvDeviceData.DataBuf[14]
//                                +"低电平"+ DataStruct.RcvDeviceData.DataBuf[15]+"高电平"+DataStruct.RcvDeviceData.DataBuf[13]);
                            if (MacCfg.Blue_Vol_tmp != DataStruct.RcvDeviceData.DataBuf[14]) {
                                MacCfg.Blue_Vol_tmp = DataStruct.RcvDeviceData.DataBuf[14];
                                if (MacCfg.Blue_Vol_tmp != DataStruct.RcvDeviceData.SYS.blue_gain) {
                                    isSend = true;
                                    DataStruct.RcvDeviceData.SYS.blue_gain = MacCfg.Blue_Vol_tmp;
                                    DataStruct.SendDeviceData.SYS.blue_gain = DataStruct.RcvDeviceData.SYS.blue_gain;
                                }
                            }
                            if (MacCfg.Aux_Vol_tmp != DataStruct.RcvDeviceData.DataBuf[15]) {
                                MacCfg.Aux_Vol_tmp = DataStruct.RcvDeviceData.DataBuf[15];
                                if (MacCfg.Aux_Vol_tmp != DataStruct.RcvDeviceData.SYS.aux_gain) {
                                    isSend = true;
                                    DataStruct.RcvDeviceData.SYS.aux_gain = MacCfg.Aux_Vol_tmp;
                                    DataStruct.SendDeviceData.SYS.aux_gain = DataStruct.RcvDeviceData.SYS.aux_gain;
                                }
                            }
                            if (MacCfg.Cox_Vol_tmp != DataStruct.RcvDeviceData.DataBuf[16]) {
                                MacCfg.Cox_Vol_tmp = DataStruct.RcvDeviceData.DataBuf[16];
                                if (MacCfg.Cox_Vol_tmp != DataStruct.RcvDeviceData.SYS.DigitMod) {
                                    isSend = true;
                                    DataStruct.RcvDeviceData.SYS.DigitMod = MacCfg.Cox_Vol_tmp;
                                    DataStruct.SendDeviceData.SYS.DigitMod = DataStruct.RcvDeviceData.SYS.DigitMod;
                                }
                            }


                            if (MacCfg.MainvolMuteFlg_tmp != DataStruct.RcvDeviceData.DataBuf[24]) {
                                MacCfg.MainvolMuteFlg_tmp = DataStruct.RcvDeviceData.DataBuf[24];
                                if (MacCfg.MainvolMuteFlg_tmp != DataStruct.RcvDeviceData.SYS.MainvolMuteFlg) {
                                    isSend = true;
                                    DataStruct.RcvDeviceData.SYS.MainvolMuteFlg = MacCfg.MainvolMuteFlg_tmp;
                                    DataStruct.SendDeviceData.SYS.MainvolMuteFlg = DataStruct.RcvDeviceData.SYS.MainvolMuteFlg;
                                    MacCfg.MainvolMuteFlg_tmp_click = DataStruct.SendDeviceData.SYS.MainvolMuteFlg;
                                }
                            }
                            if (isSend) {
                                isSend = false;
                                Intent intentw = new Intent();
                                intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
                                intentw.putExtra("msg", Define.BoardCast_FlashUI_InputSource_Val);
                                mContext.sendBroadcast(intentw);
                            }


                        break;
                    case Define.CUR_PROGRAM_INFO:
                        MacCfg.CurProID = DataStruct.RcvDeviceData.DataBuf[10];// 当前用户组ID
                        MacCfg.ReadGroup = MacCfg.CurProID;
                        break;
                    case Define.SOUND_FIELD_INFO:
                        //if(DEBUG) System.out.println("## Channel System-SOUND_FIELD_INFO:" + DataStruct.RcvDeviceData.DataLen);
                        DataOptUtil.FillDelayDataBySystemChannel(DataStruct.RcvDeviceData.DataBuf, DataStruct.RcvDeviceData.DataLen, true, true);
                        break;
                    case Define.SYSTEM_Group:
                        //DataStruct.BOOL_GroupREC_ACK = false;
                        DataStruct.RcvDeviceData.SYS.none1 = DataStruct.RcvDeviceData.DataBuf[10];
                        DataStruct.RcvDeviceData.SYS.Default_sound_source = DataStruct.RcvDeviceData.DataBuf[11];
                        DataStruct.RcvDeviceData.SYS.mode = DataStruct.RcvDeviceData.DataBuf[12];
                        DataStruct.RcvDeviceData.SYS.none[0] = DataStruct.RcvDeviceData.DataBuf[13];
                        DataStruct.RcvDeviceData.SYS.none[1] = DataStruct.RcvDeviceData.DataBuf[14];
                        DataStruct.RcvDeviceData.SYS.none[2] = DataStruct.RcvDeviceData.DataBuf[15];
                        DataStruct.RcvDeviceData.SYS.none[3] = DataStruct.RcvDeviceData.DataBuf[16];
                        DataStruct.RcvDeviceData.SYS.none[4] = DataStruct.RcvDeviceData.DataBuf[17];
                        System.out.println("BUG 进入了直列了吗"+DataStruct.RcvDeviceData.SYS.none[4]);
                        break;
                    default:
                        break;
                }
            } else {
                ;
            }

            DataStruct.U0RcvFrameFlg = YES; // rcv one frame end
            DataStruct.isConnecting = true;  // 蓝牙可正常连接
            DataStruct.isConnecting = true;
            if (DataStruct.isConnecting != DataStruct.isConnectingOld) {
                DataStruct.isConnectingOld = DataStruct.isConnecting;
                //setConnectStatus(DataStruct.isConnecting);
            }
        } else if (DataStruct.RcvDeviceData.FrameType == DataStruct.ERROR_ACK) { // 错误回应
            BTS_Again = true;
        } else if (DataStruct.RcvDeviceData.FrameType == DataStruct.RIGHT_ACK) { // 正确回应
            DataStruct.isConnecting = true;
            if (DataStruct.isConnecting != DataStruct.isConnectingOld) {
                DataStruct.isConnectingOld = DataStruct.isConnecting;
                //setConnectStatus(DataStruct.isConnecting);
            }

            HandTrue();


            DataStruct.U0RcvFrameFlg = YES; // rcv one frame end
            DataStruct.isConnecting = true;  // 蓝牙可正常连接
        } else {
            ;
        }
    }

    /**
     * 硬件手调开关回应正确
     * */
    private static void HandTrue(){
        if(MacCfg.Mcu==7&&DataStruct.RcvDeviceData.ChannelID == Define.SYSTEM_SPK_TYPEB){

        }
    }

    public void serviceOnDestroy() {
        DataStruct.isConnecting = false;
        if (rThread != null) {
            rThread.interrupt(); // 关闭接收线程
            rThread = null;
        }
        if (sThread != null) {
            sThread.interrupt(); // 关闭接收线程
            sThread = null;
        }
        if (aThread != null) {
            aThread.interrupt(); // 关闭接收线程
            aThread = null;
        }
        if (tThread != null) {
            tThread.interrupt(); // 关闭接收线程
            tThread = null;
        }
        if (CHS_Broad_Receiver != null) {
            unregisterReceiver(CHS_Broad_Receiver);
            CHS_Broad_Receiver = null;
        }
//        MacCfg.BOOL_CanLinkUART = false;

        DataStruct.U0SynDataSucessFlg = false;
        DataStruct.isConnecting = false;
        MacCfg.bool_ReadMacGroup = false;
        DataStruct.U0HeadFlg = NO; // start rcv new head flag
        DataStruct.U0DataCnt = 0;  // rcv counter set 0
        DataStruct.U0HeadCnt = 0;

        DataStruct.U0RcvFrameFlg = NO; // 有新接收到数据的标志
        DataStruct.U0SendFrameFlg = NO; // 有数据要发送的标志
        DataStruct.U0SynDataError = false; // 同步初始化数据是否出错
        DataStruct.PcConnectFlg = NO;
        DataStruct.PcConnectCnt = 0;

        bool_OpBT = false; //打开蓝牙完成，连接不成功
        DataStruct.U0HeadFlg = NO;  //设置包头无效
        MacCfg.UpdataAduanceData = false;

        for (int i = 0; i <= WHAT_IS_Max; i++) {
            mHandler.removeMessages(i);
        }
        mHandler.removeCallbacksAndMessages(null);
        switch (MacCfg.COMMUNICATION_MODE) {
            case Define.COMMUNICATION_WITH_WIFI:
                try {
                    if (mSocketClient != null) {
                        mSocketClient.close();
                        mSocketClient = null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case Define.COMMUNICATION_WITH_BLUETOOTH_SPP:

                break;

            case Define.COMMUNICATION_WITH_USB_HOST:

                break;
            case Define.COMMUNICATION_WITH_UART:

                break;
            default:
                break;
        }

        if (mChatService != null) {
            mChatService.stop();
            mChatService = null;
        }

        //closeSerialPort();

        DataOptUtil.ExitDatabases();


//        DataStruct.RcvDeviceData = null;
//        DataStruct.SendDeviceData = null;
//        DataStruct.DefaultDeviceData = null;
//        DataStruct.BufDeviceData = null;
//
//        DataStruct.MacModeAll = null;
//        DataStruct.CurMacMode = null;
//
//        DataStruct.user = null;
//        DataStruct.userSeffTemp = null;
//        DataStruct.venderOption = null;
//        DataStruct.HomeImageUrlList = null;
//
//        DataStruct.mDSP_MusicData = null;
//        DataStruct.mDSP_OutputData = null;
//        DataStruct.mDSP_DataMac = null;
//        DataStruct.MAC_DataBuf = null;

        super.onDestroy();
    }


}
