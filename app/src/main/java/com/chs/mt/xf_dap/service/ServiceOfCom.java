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
    /****************************   ????????????     ****************************/
    /*********************************************************************/
    public static final int NO = 0;  // ??????????????????
    public static final int YES = 1; // ??????????????????
    public static final int WHAT_IS_NULL = 0x00;            // ??????
    public static final int WHAT_IS_CONNECT_ERROR = 0x01;   // ????????????
    public static final int WHAT_IS_CONNECT_RIGHT = 0x02;   // ??????????????????
    public static final int WHAT_IS_SYNC_SUCESS = 0x03;     // ?????????????????????
    public static final int WHAT_IS_NEW_DATA = 0x04;        // ?????????????????????
    public static final int WHAT_IS_SEND_DATA = 0x05;       // ??????????????????
    public static final int WHAT_IS_PROGRESSDIALOG = 0x06;  // ??????progressDialog
    public static final int WHAT_IS_LEDUP_SOURCE = 0x07;    // ???????????????????????????
    public static final int WHAT_IS_OFF_LINE_INFO = 0x08;   // ?????????????????????
    public static final int WHAT_IS_SYNC_GROUP_NAME = 0x09;   // ?????????????????????
    public static final int WHAT_IS_LED_FLASH = 0x0A;         // ???????????????
    public static final int WHAT_IS_RESET_EQ_CHNAME = 0x0B;   // ??????EQ????????????
    public static final int WHAT_IS_LongPress_INC_SUB = 0x0d; // ????????????????????????????????????????????????
    public static final int WHAT_IS_GroupClick = 0x0e;        // ???????????????????????????????????????
    public static final int WHAT_IS_Wait = 0x0f;                //????????????????????????
    public static final int WHAT_IS_LOADING = 0x10;           //??????????????????????????????
    public static final int WHAT_IS_UPDATE_UI = 0x11;         // ??????????????????
    public static final int WHAT_IS_FLASH_BT_CONNECTED = 0x12;// ???????????????????????????????????????????????????
    public static final int WHAT_IS_INIT_LOADING = 0x13;      //??????????????????????????????????????????????????????????????????????????????
    public static final int WHAT_IS_CLOSE_BT = 0x14;          //??????????????????
    public static final int WHAT_IS_BT_TIME_OUT = 0x15;       //??????????????????
    public static final int WHAT_IS_MENU_LOCKED = 0x16;       //
    public static final int WHAT_IS_BLUETOOTH_SCAN = 0x17;    //
    public static final int WHAT_IS_RETURN_EXIT = 0x18;     // ???2??????????????????
    public static final int WHAT_IS_LOGOUT_SUCCESS = 0x19;
    public static final int WHAT_IS_LOGOUT_FAILED = 0x1a;
    public static final int WHAT_IS_FLASH_NETWORK = 0x1b;     //?????????????????????
    public static final int WHAT_IS_BT_SCAN = 0x1c;           //??????????????????
    public static final int WHAT_IS_FLASH_NET_HOME_UI = 0x1d;// ?????????
    public static final int WHAT_IS_FLASH_SYSTEM_DATA = 0x1e;// ?????????
    public static final int WHAT_IS_TRYS_TO_CON_DSPPLAYMSG = 0x1f;// ?????????
    public static final int WHAT_IS_ShowGetERRMasterVolMsg = 0x20;//
    public static final int WHAT_IS_Show_UnKnowMacType_Msg = 0x21;//
    public static final int WHAT_IS_Show_Timer = 0x22;//
    public static final int WHAT_IS_BOOT_Start = 0x23;//
    public static final int WHAT_IS_Max = 0x23;// ?????????
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


    /****************************   ?????????????????????????????????        ****************************/
    public static final String ACTION_MEDIA_SCANNER_SCAN_DIR = "android.intent.action.MEDIA_SCANNER_SCAN_DIR";

    private Toast mToast;
    /*********************************************************************/
    /*******************  USB Host  ??????    WIFI??????     **********************/
    /*********************************************************************/

    /****************************************************************************/
    /****************************     ????????????        ********************************/
    /****************************   ?????????????????????        ****************************/
    public static final int BT_SEND_DATA_PACK_SIZE = 20; //???????????????????????????????????????

    /*********************************************************************/
    /****************************    ??????SPP??????   ********************************/
    private static byte[] BTSendBuf20 = new byte[20];//????????????24??????
    private byte[] BTRecBuf = new byte[1024];

    private static boolean BT_COther = false;      //true:??????????????????????????????
    private static boolean bool_OpBT = false;      //false:????????????????????????????????????????????? ????????????
    private static boolean bool_CloseBT = false;   //false:????????????????????????????????????????????? ????????????
    /* true:??????????????????DSP HP-XXXX?????????8????????????????????????????????????????????????????????????????????????????????????????????????
     * ??????????????????????????????????????????????????????????????????*/
    private static boolean bool_BT_CTO_Send = false;
    private static boolean bool_BT_ConTimeOut = false;
    private static boolean bool_FristStart = false;
    /*?????????????????????????????????????????????????????????????????????????????????*/
    private static boolean BTS_Again = false;/*true:??????????????????1?????????*/
    /*************************************************************************/
    /****************************  ??????BLE??????   *******************************/
    public static boolean BLE_DEVICE_STATUS = false;
    private static byte[] BLESendBuf = new byte[Define.BLE_MaxT];
    @SuppressWarnings("unused")
    private String mDeviceName = null;
    private String mDeviceAddress = null;
    /***************************************************************************/
    /****************************     WFIF??????       *******************************/
    private static Socket mSocketClient = null;
    @SuppressWarnings("unused")
    private static BufferedReader mBufferedReaderClient = null;
    private String recvMessageClient = "";

    private static int WifiInfoTimerCnt = 0;


    private static boolean DeviceVerErrorFlg = false;// ??????????????????????????????
    private static boolean WIFI_CanConnect = false;
    /***************************************************************************/
    /****************************  USB Host??????    *******************************/
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private UsbManager USBManager; // USB?????????
    private static UsbDevice mUsbDevice;  // ?????????USB??????
    private UsbInterface mUsbInterface;
    private static UsbDeviceConnection mDeviceConnection;
    private static UsbEndpoint epOut;
    private UsbEndpoint epIn;
    private static byte[] USBSendBuf = new byte[Define.USB_MaxT];
    private byte[] Receiveytes = new byte[Define.USB_MaxT]; // ??????????????????
    /*********************************************************************/
    /*******************  ??????SPP-LE   ??????     **********************/
    /*********************************************************************/
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    // ?????????????????????
    private String mConnectedDeviceName = null;
    private StringBuffer mOutStringBuffer;
    // ?????????????????????
    private BluetoothAdapter mBluetoothAdapter = null;
    // ????????????????????????
    private static BluetoothChatService mChatService = null;
    private static BluetoothDevice deviceSPPBLE;
    /***************************************************************************/
    /****************************     ????????????       *******************************/
    /***************************************************************************/
    private static int progressDialogStep = 0; // progressDialog???????????????

    public static final String TAG = "BUG ###ServiceOfCom";

    private static Context mContext;
    private Thread sThread = null;
    private Thread rThread = null;
    private Thread tThread = null;
    private Thread aThread = null;

    private CHS_Broad_BroadcastReceiver CHS_Broad_Receiver;

    //???Activity??????
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
    /*****************************   ????????????       ****************************/
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

//		tThread.start(); // ?????????????????????
        int IdNum = (int) Thread.currentThread().getId();
        System.out.println(TAG + " initService currentThread Id:" + IdNum);
        initHandlerService();
        //????????????CHS_Broad_BroadcastReceiver
        CHS_Broad_Receiver = new CHS_Broad_BroadcastReceiver();
        IntentFilter CHS_Broad_filter = new IntentFilter();
        CHS_Broad_filter.addAction("android.intent.action.CHS_Broad_BroadcastReceiver");
        //??????receiver
        registerReceiver(CHS_Broad_Receiver, CHS_Broad_filter);
        DataStruct.jsonRWOpt = new JsonRWUtil();

        DataOptUtil.InitApp(mContext);
        //?????????????????????
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
         * HandlerThread???Android???????????????Handler???????????????????????????
         ??????HandlerThread?????????Hanlder??????????????????????????????
         * HandlerThread??????????????????,?????????handlerThread.getLooper()?????????????????????
         * HandlerThread???start?????????
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
     * ???????????????????????????true:????????????
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
            recvMessageClient = "IP????????????!";// ????????????
            Message msg = Message.obtain();
            msg.what = WHAT_IS_CONNECT_ERROR;
            mHandler.sendMessage(msg);

            System.out.println("BUG WHAT_IS_CONNECT_ERROR = " + "IP????????????!");
            return false;
        }
        int start = msgText.indexOf(":");
        if ((start == -1) || (start + 1 >= msgText.length())) {
            recvMessageClient = "IP???????????????!";// ????????????
            Message msg = Message.obtain();
            msg.what = WHAT_IS_CONNECT_ERROR;
            mHandler.sendMessage(msg);

            System.out.println("BUG WHAT_IS_CONNECT_ERROR = " + "IP???????????????!!");
            return false;
        }
        String sIP = msgText.substring(0, start);
        String sPort = msgText.substring(start + 1);
        int port = Integer.parseInt(sPort);

        System.out.println("BUG WIFI  " + "IP:" + sIP + ",port:" + port);
        try {
            if (mSocketClient == null) {
                // ???????????????
                mSocketClient = new Socket(sIP, port); // portnum
                if (mSocketClient != null) {
                    // ????????????????????????
                    mBufferedReaderClient = new BufferedReader(
                            new InputStreamReader(mSocketClient.getInputStream()));
                    recvMessageClient = "???????????????server!";// ????????????
                    // ??????????????????
                    Message msg = Message.obtain();
                    msg.what = WHAT_IS_CONNECT_RIGHT;
                    mHandler.sendMessage(msg);

                    System.out.println("BUG mSocketClient new Socket ???????????????server!");
                    return true;
                } else {
                    System.out.println("BUG mSocketClient new Socket ERROR!!!");
                    return false;
                }
            }
            return false;
        } catch (Exception e) {
            recvMessageClient = "??????IP??????:" + e.toString() + e.getMessage();// ????????????
            System.out.println("BUG WIFI error-leon");
            Message msg = Message.obtain();
            msg.what = WHAT_IS_CONNECT_ERROR;
            mHandler.sendMessage(msg);
            System.out.println("BUG WIFI  :" + recvMessageClient);
            return false;
        }
    }

    /**
     * ??????wifi????????????
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
        // ??????32?????????IP??????
        int ipAddress = wifiInfo.getIpAddress();
        String ipString = "";
        if (ipAddress != 0) {
            ipString = ((ipAddress & 0xff) + "." + (ipAddress >> 8 & 0xff) + "."
                    + (ipAddress >> 16 & 0xff) + "." + (ipAddress >> 24 & 0xff));
        }
        System.out.println("BUG WIFI  isWifiIpOfCHS A ipString:" + ipString);

        // ??????IP???????????????10.10.100.xxx
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

    //????????????true
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
        //??????????????????
        if ((DataStruct.U0SynDataSucessFlg == true) && (mSocketClient != null) && (DataStruct.isConnecting)) {
            DataStruct.U0SynDataSucessFlg = false;
            DataStruct.isConnecting = false;
            DataStruct.ManualConnecting = true; // ?????????????????????
            // ??????????????????
            Message msg = Message.obtain();
            msg.what = WHAT_IS_CONNECT_ERROR;
            mHandler.sendMessage(msg);

            System.out.println("BUG WHAT_IS_CONNECT_ERROR = " + "DataStruct.ManualConnecting = true; // ?????????????????????");
        } else {
            if (isWifiAvailable(mContext)) {//???????????????WIFI
                if (isWifiIpOfCHS()) {//??????????????????WIFI
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

            DataStruct.ManualConnecting = false; // ??????????????????
        }
    }


    /*********************************************************************/
    /*************************  ??????SPP???????????? TODO  *********************/
    /*********************************************************************/
    private void initCommunicationMode() {
        switch (MacCfg.COMMUNICATION_MODE) {
            case Define.COMMUNICATION_WITH_WIFI:

                break;
            case Define.COMMUNICATION_WITH_BLUETOOTH_LE:
                //????????????????????????
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
//                //????????????
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
            MacCfg.BTManualConnect = false;//true:??????????????????
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
    /********************  ??????BLE GATT????????????  TODO   ********************/
    /*********************************************************************/


    /**********************  Bluetooth SPP BLE????????????   TODO  **********************/
    private class ServerThread extends Thread {
        // ?????????UUID
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
//            Log.d(tag, "??????????????????...");
//            while (true) {
//                try {
//                    BluetoothSocket socket = serverSocket.accept();
//
//                    BluetoothDevice device = socket.getRemoteDevice();
//                    Log.d(tag, "?????????????????? , ??????????????????:" + device.getName() + " , ??????????????????:" + device.getAddress());
//
//                    if (socket.isConnected()) {
//                        Log.d(tag, "????????????????????????.");
//                    }
//
//                } catch (Exception e) {
//                    Log.d(tag, "??????????????????.Exception..");
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

    // ???Handler??????BluetoothChatService???????????????
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
                            //????????????
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
//                    System.out.println("BUG-COM-Service-??????N:"+cnt+st+">");


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
        //???0
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


    /**********************  USB Host????????????   TODO  **********************/
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
//            //????????????????????????????????????
//            if(UsbDevice!=null){
//                if (UsbDevice.getVendorId() == Define.USB_DSPHD_VID &&
//                        UsbDevice.getProductId() == Define.USB_DSPHD_PID) {
//                    mUsbDevice = UsbDevice;
//
//                    if (ACTION_USB_PERMISSION.equals(action)) {
//                        synchronized (this) {
//                            //UsbDevice = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
//                            //??????????????????
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
//            System.out.println("BUG USB USBManager.toString???" + String.valueOf(USBManager.toString()));
//            System.out.println("BUG USB deviceList.size???" + String.valueOf(deviceList.size()));
//            String size=String.valueOf(deviceList.size());
//            String vidString="VID=";
//            ////ToastMsg("deviceList.size()="+deviceList.size());
//
//            Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
//            ArrayList<String> USBDeviceList = new ArrayList<String>(); // ??????USB???????????????
//            while (deviceIterator.hasNext()) {
//                UsbDevice device = deviceIterator.next();
//
//                USBDeviceList.add(String.valueOf(device.getVendorId()));
//                USBDeviceList.add(String.valueOf(device.getProductId()));
//
//                vidString+=device.getVendorId()+",";
//                ////ToastMsg("device.getVendorId()="+device.getVendorId());
//                // ????????????????????????????????????
//                if (device.getVendorId() == Define.USB_DSPHD_VID &&
//                        device.getProductId() ==Define.USB_DSPHD_PID) {
//                    mUsbDevice = device;
//
//                    // ???????????????????????????
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

    // ???????????????????????????
//    private void findIntfAndEpt() {
//        if (mUsbDevice == null) {
//            System.out.println("BUG USB ?????? USB ??????!");
//            //ToastMsg(getResources().getString(R.string.DidNotFindUSB));
//            return;
//        }
//        for (int i = 0; i < mUsbDevice.getInterfaceCount();) {
//            // ???????????????????????????????????????????????????????????????getInterfaceCount()???????????????
//            // ???????????????????????????????????????????????????OUT ??? IN
//            UsbInterface intf = mUsbDevice.getInterface(i);
//            System.out.println("BUG usb "+ i + " " + intf);
//            mUsbInterface = intf;
//
//            break;
//        }
//
//        if (mUsbInterface != null) {
//            UsbDeviceConnection connection = null;
//            // ?????????????????????
//            if (USBManager.hasPermission(mUsbDevice)) {
//                // ????????????????????? UsbDeviceConnection ?????????????????????????????????????????????
//                connection = USBManager.openDevice(mUsbDevice);
//                if (connection == null) {
//                    return;
//                }
//                if (connection.claimInterface(mUsbInterface, true)) {
//                    System.out.println("BUG usb ????????????");
//                    //DisplayToast("????????????");
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
//                USBManager.requestPermission(mUsbDevice, pi); //???????????????????????????????????????????????????
//                System.out.println("BUG USB ????????????");
//                //ToastMsg(getResources().getString(R.string.HaveNotPermission));
//            }
//        } else {
//            System.out.println("BUG usb ??????????????????");
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
    /*************************    UART???????????? TODO  **********************/
    /*********************************************************************/


    /*********************************************************************/
    /*************************    ???????????? TODO  **********************/
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
        //???????????????
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
    /*************************   ???????????? TODO  **********************/
    /*********************************************************************/
    public static void APP_SendPack(byte[] btdata, int packsize) {
        if (MacCfg.COMMUNICATION_MODE == Define.COMMUNICATION_WITH_WIFI) {
            try {
                if (mSocketClient != null) {
                    OutputStream os = mSocketClient.getOutputStream();
                    os.write(btdata, 0, packsize); // ????????????????????????
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

            //DSP??????LED???
            // ?????????????????????????????????
            clearRecFlag();
            DataStruct.SendDeviceData.FrameType = 0xa2;
            DataStruct.SendDeviceData.DeviceID = 0x01;
            DataStruct.SendDeviceData.UserID = 0x00;
            DataStruct.SendDeviceData.DataType = Define.SYSTEM;
            DataStruct.SendDeviceData.ChannelID = Define.LED_DATA;
            // ???????????????
            DataStruct.SendDeviceData.DataID = 0x00;
            DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;
            DataStruct.SendDeviceData.PcCustom = 0x00;
            DataStruct.SendDeviceData.DataLen = 0x00;

            DataStruct.U0SendFrameFlg = NO;//??????????????????????????????????????????
            DataStruct.U0RcvFrameFlg = NO; //?????????????????????????????????????????????
            OutTimeCnt = 0;
            RetryCnt = 0;

            DataOptUtil.SendDataToDevice(true);//?????????????????????
            while (DataStruct.U0RcvFrameFlg == NO) {
                if (!DataStruct.isConnecting) {
                    break;
                }
                // ??????????????????
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    System.out.println("BUG sThread bt pack LED_DATA  InterruptedException");
                }
                if (BTS_Again == true) {
                    BTS_Again = false;
                    System.out.println("BUG ## Channel OutTimeCnt BTS_Again,LED_DATA Send again2");
                    // ?????????????????????????????????
                    clearRecFlag();
                    DataOptUtil.SendDataToDevice(true);
                }
                //????????????????????????????????????????????????????????????1ms????????????
                if (++OutTimeCnt > 1000) {
                    OutTimeCnt = 0;
                    // ?????????????????????????????????
                    clearRecFlag();
                    DataOptUtil.SendDataToDevice(true);
                    // 4??????????????????????????????????????????
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
     * ?????????????????????DSP??????
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

            DataStruct.U0SendFrameFlg = NO;//??????????????????????????????????????????
            DataStruct.U0RcvFrameFlg = NO; //?????????????????????????????????????????????
            OutTimeCnt = 0;
            OK = true;
            DataOptUtil.SendDataToDevice(true);//?????????????????????
            while (DataStruct.U0RcvFrameFlg == NO) {
                if (!DataStruct.isConnecting) {
                    OK = false;
                    break;
                }
                // ??????????????????
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                }
                //????????????????????????????????????????????????????????????1ms????????????
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
                //????????????????????????
                Intent intentw = new Intent();
                intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
                intentw.putExtra("msg", Define.BoardCast_FlashUI_CancelDSPDataLoading);
                mContext.sendBroadcast(intentw);
                //???????????????????????????????????????????????????????????????
                DataOptUtil.InitLoad();// ????????????????????????

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
                        //????????????????????????????????????????????????????????????1ms????????????
                        if (++mOutTimeCnt > 500) {
                            mOutTimeCnt = 0;
                            MDOptUtil.clearMusicRecFrameFlg();
                            MDOptUtil.getStatus(true);
                            // 4??????????????????????????????????????????
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
                        //????????????
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
     * ???????????????
     */
    private void DSPPlaySendCmd() {
        //DSP Play
        if (DataStruct.comPlay == Define.COMT_PLAY) {
            MDef.U0RecFrameFlg = false;
            //??????
            if ((DataStruct.MSendbufferList.size() > 0) && (!DataStruct.MSendbufferList.isEmpty())) {
                SPP_LESendPack(DataStruct.MSendbufferList.get(0), DataStruct.MSendbufferList.get(0).length);
                if (DataStruct.MSendbufferList.size() > 0) {//Dug:????????????????????????
                    DataStruct.MSendbufferList.remove(0);//??????????????????????????????????????????
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
            //??????DSP Play??????????????????????????????????????????????????????????????????
            if (!MDef.U0RecFrameFlg) {
                if (DataStruct.RcvDeviceData.SYS.input_source == 5) {
                   MDOptUtil.getStatus(true);//????????????????????????U???
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
                    //??????LED??????????????????????????? //0????????????????????????1?????????????????????2:????????????
                    checkClientHeadDataSendCmd();
                    checkDSPPlayStatuesSendCmd();//??????????????????(??????????????????)
//                getMasterVolSendCmd(); ??????????????????initLoad?????????
                    //??????????????????OK,?????????????????????DSPPlay ????????????
                    if ((!DataStruct.BOOL_CheckHEADOK) && (DataStruct.BOOLDSPHeadFlg != Define.Statues_YES)) {
                        //DSP Play
                        DSPPlaySendCmd();
                    }

                    //???????????????
                    DataStruct.comType = DataStruct.comDSP + DataStruct.comPlay;

                    //???????????????
                    if ((DataStruct.comType == Define.COMT_DSP) || (DataStruct.comType == Define.COMT_DSPPLAY)) {
                        if (!DataOptUtil.isListNull()) {
                            sendListPackage();
                            DataStruct.comType = DataStruct.comDSP + DataStruct.comPlay;
                            if ((DataStruct.comType == Define.COMT_DSP) || (DataStruct.comType == Define.COMT_DSPPLAY)) {
                                if (!DataOptUtil.isListNull()) {//Dug:????????????????????????
                                    DataStruct.SendbufferList.remove(0);//??????????????????????????????????????????
                                }

                                if (DataOptUtil.isListNull()) {
                                    if (!DataStruct.U0SynDataSucessFlg) {
                                        if (DataStruct.SEFF_USER_GROUP_OPT > 0) {//Bug:??????????????????????????????????????????
                                            DataStruct.SEFF_USER_GROUP_OPT = 0;
                                            Message msgss = Message.obtain();
                                            msgss.what = WHAT_IS_SYNC_SUCESS;
                                            mHandler.sendMessage(msgss);
                                        }
                                    }
                                }
                                // ?????????????????????????????????
                                if (progressDialogStep > 0) {
                                    Message msg = Message.obtain();
                                    msg.what = WHAT_IS_PROGRESSDIALOG;
                                    msg.arg1 = DataStruct.SendbufferList.size();
                                    mHandler.sendMessage(msg);
                                }
                            }
                        } else {
                            if ((DataStruct.comType == Define.COMT_DSP) || (DataStruct.comType == Define.COMT_DSPPLAY)) {
                                if (DataStruct.U0SynDataSucessFlg) {//???????????????????????????
                                    DataOptUtil.ComparedToSendData(true);//??????????????????????????????????????????????????????????????????????????????
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

                //?????????
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
        DataStruct.U0RcvFrameFlg = NO;//?????????????????????????????????????????????
        OutTimeCnt = 0;
        RetryCnt = 0;
        if (!DataOptUtil.isListNull()) {//Dug:????????????????????????
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
                // ?????????????????????????????????
                clearRecFlag();
                if (!DataOptUtil.isListNull()) {//Dug:????????????????????????
                    APP_SendPack(DataStruct.SendbufferList.get(0),
                            (DataStruct.SendbufferList.get(0).length));
                }
            }
            // ??????????????????????????????????????????????????????
            if (++OutTimeCnt > 1000) {
                OutTimeCnt = 0;
                System.out.println("BUG BT Send OutTimeCnt !!! =" + OutTimeCnt);
                if (!DataOptUtil.isListNull()) {//Dug:????????????????????????
                    // ?????????????????????????????????
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
     * ???????????????(?????????????????????????????????)   TODO
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
                        DataStruct.U0RcvFrameFlg = NO;//?????????????????????????????????????????????
                        OutTimeCnt = 0;
                        RetryCnt = 0;
                        if (!DataOptUtil.isListNull()) {//Dug:????????????????????????
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
                                // ?????????????????????????????????
                                clearRecFlag();
                                if (!DataOptUtil.isListNull()) {//Dug:????????????????????????
                                    APP_SendPack(DataStruct.SendbufferList.get(0),
                                            (DataStruct.SendbufferList.get(0).length));
                                }
                            }
                            // ??????????????????????????????????????????????????????
                            if (++OutTimeCnt > 800) {
                                OutTimeCnt = 0;
                                System.out.println("BUG BT Send OutTimeCnt !!! =" + OutTimeCnt);
                                if (!DataOptUtil.isListNull()) {//Dug:????????????????????????
                                    // ?????????????????????????????????
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
                            if (!DataOptUtil.isListNull()) {//Dug:????????????????????????
                                DataStruct.SendbufferList.remove(0);//??????????????????????????????????????????
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        if (DataOptUtil.isListNull()) {
                            if (!DataStruct.U0SynDataSucessFlg) {
                                if (DataStruct.SEFF_USER_GROUP_OPT > 0) {//Bug:??????????????????????????????????????????
                                    DataStruct.SEFF_USER_GROUP_OPT = 0;
                                    Message msgss = Message.obtain();
                                    msgss.what = WHAT_IS_SYNC_SUCESS;
                                    mHandler.sendMessage(msgss);
                                }
                            }
                        }
                        // ?????????????????????????????????
                        if (progressDialogStep > 0) {
                            Message msg = Message.obtain();
                            msg.what = WHAT_IS_PROGRESSDIALOG;
                            msg.arg1 = DataStruct.SendbufferList.size();
                            mHandler.sendMessage(msg);
                        }
                    } else {


                        if (DataStruct.U0SynDataSucessFlg) {//???????????????????????????
                            DataOptUtil.ComparedToSendData(true);//??????????????????????????????????????????????????????????????????????????????
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


                //?????????
                if (!DataStruct.isConnecting) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                    //??????????????????????????????
                    if (MacCfg.COMMUNICATION_MODE == Define.COMMUNICATION_WITH_WIFI) {
                        if ((mSocketClient == null) && (!DataStruct.isConnecting)) {
                            boolean wifiuse = CheckWifiStata();// ????????????????????????????????????socket
                            System.out.println("BUG WIFI tRunnable CheckWifiStata():" + wifiuse);
                            if (wifiuse == true) {
                                NewSocketClient(); // ??????socket????????????android4.0???????????????????????????socket????????????????????????
                            }
                        }
                    } else if (MacCfg.COMMUNICATION_MODE == Define.COMMUNICATION_WITH_BLUETOOTH_SPP) {
                        if (CheckBTStata() && (mSocketClient == null)) {
                            if (!DataStruct.ManualConnecting) {
                                Message msg = Message.obtain();
                                msg.what = WHAT_IS_FLASH_BT_CONNECTED;
                                msg.arg1 = 0;
                                mHandler.sendMessage(msg);// ??????????????????
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
//            mHandler.sendMessage(msg);// ??????????????????
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

    //?????????????????????UI TODO
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
                // ???????????????????????????
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                BluetoothDevice Dev = mBluetoothAdapter.getRemoteDevice(address);


                System.out.println("BUG ??????????????????");
                if (ConnectionUtil.isConn(mContext)) {
                    System.out.println("BUG ???????????????" + ConnectionUtil.isConn(mContext));
                    if (ConnectionUtil.isOpen(mContext)) {
                        // ?????????????????????
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
                    rThread.interrupt(); // ??????????????????
                    rThread = null;
                }
                if (sThread != null) {
                    sThread.interrupt(); // ??????????????????
                    sThread = null;
                }
                if (aThread != null) {
                    aThread.interrupt(); // ??????????????????
                    aThread = null;
                }

                if (tThread != null) {
                    tThread.interrupt(); // ??????????????????
                    tThread = null;
                }

                DataStruct.U0SynDataSucessFlg = false;
                DataStruct.isConnecting = false;
                MacCfg.bool_ReadMacGroup = false;
                DataStruct.U0HeadFlg = NO; // start rcv new head flag
                DataStruct.U0DataCnt = 0;  // rcv counter set 0
                DataStruct.U0HeadCnt = 0;

                DataStruct.BOOLDSPHeadFlg = Define.Statues_Null;//0????????????????????????1?????????????????????2:????????????
                DataStruct.BOOLPlayHeadFlg = Define.Statues_Null;//0????????????????????????1?????????????????????2:????????????
                DataStruct.BOOLHaveMasterVol = false;//false???????????????????????????true:??????????????????

                DataStruct.U0RcvFrameFlg = NO; // ??????????????????????????????
                DataStruct.U0SendFrameFlg = NO; // ???????????????????????????
                DataStruct.U0SynDataError = false; // ?????????????????????????????????
                DataStruct.PcConnectFlg = NO;
                DataStruct.PcConnectCnt = 0;


                bool_OpBT = false; //????????????????????????????????????
                DataStruct.U0HeadFlg = NO;  //??????????????????
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
//                        stopSelf();//????????????
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


    // TODO -------Client??????????????????????????????????????????????????????
    static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // int IdNum = (int)Thread.currentThread().getId();
            // System.out.println("mHandler thread:"+IdNum);
            super.handleMessage(msg);

            if (msg.what == WHAT_IS_NULL) {
                ;
            } else if (msg.what == WHAT_IS_CONNECT_ERROR) { // ?????????????????????????????????????????????

                disconnectSet();

            } else if (msg.what == WHAT_IS_CONNECT_RIGHT) { // ??????????????????

                System.out.println("BUG ??????????????????");
                if (ConnectionUtil.isConn(mContext)) {
                    System.out.println("BUG ???????????????" + ConnectionUtil.isConn(mContext));
                    if (ConnectionUtil.isOpen(mContext)) {
                        getCheckHeadFromBuf();
                        DataStruct.isConnecting = true;
                        DataOptUtil.InitLoad();// ????????????????????????
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


            } else if (msg.what == WHAT_IS_TRYS_TO_CON_DSPPLAYMSG) { // ??????????????????
                //ToolsUtil.Toast(mContext,mContext.getString(R.string.TriesToConnectTo)+" DSP Play");
            } else if (msg.what == WHAT_IS_ShowGetERRMasterVolMsg) { // ??????????????????
                ToolsUtil.Toast(mContext, mContext.getString(R.string.GetMasterVolMsg));
            } else if (msg.what == WHAT_IS_Show_UnKnowMacType_Msg) { // ??????????????????
                ToolsUtil.Toast(mContext, mContext.getString(R.string.UnknowMacType));
            } else if (msg.what == WHAT_IS_LongPress_INC_SUB) {  // ???????????????????????????????????????
                //FlashButtonLongPressUI();
            } else if (msg.what == WHAT_IS_Show_Timer) {  // ???????????????????????????????????????
                //FlashButtonLongPressUI();
                System.out.println("BUG ??????inlaid");
//                boolean status = DataOptUtil.getSoundStatus(mContext);
//                if(status != MacCfg.BOOL_SoundStatues){
//                    MacCfg.BOOL_SoundStatues = status;
//                    //????????????????????????
//                    Intent intentw=new Intent();
//                    intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
//                    intentw.putExtra("msg", Define.BoardCast_FlashUI_FlashSoundStatus);
//                    intentw.putExtra("state", status);
//                    mContext.sendBroadcast(intentw);
//                }
            } else if (msg.what == WHAT_IS_FLASH_BT_CONNECTED) {  // ???????????????????????????????????????????????????

            } else if (msg.what == WHAT_IS_Address) {
                Intent intentw = new Intent();
                intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
                intentw.putExtra("msg", Define.BoardCast_FlashUI_ADDRESS);
                intentw.putExtra("txtAddress", LocationUtils.getInstance().getLocations(mContext));
                mContext.sendBroadcast(intentw);
            } else if (msg.what == WHAT_IS_INIT_LOADING) {//??????????????????????????????????????????????????????????????????????????????
                //????????????????????????
                Intent intentw = new Intent();
                intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
                intentw.putExtra("msg", Define.BoardCast_FlashUI_ShowLoading);
                intentw.putExtra("state", false);
                mContext.sendBroadcast(intentw);
            } else if (msg.what == WHAT_IS_SYNC_SUCESS) {
                connectSet();
                DataOptUtil.saveBluetoothInfo(mContext);
                DataOptUtil.saveHEAD_DATA(mContext);
            } else if (msg.what == WHAT_IS_NEW_DATA) { // ??????????????????
            } else if (msg.what == WHAT_IS_SEND_DATA) { // ???????????????,?????????????????????
                if (++WifiInfoTimerCnt > 10) {
                    WifiInfoTimerCnt = 0;
                }
            } else if (msg.what == WHAT_IS_PROGRESSDIALOG) { // ??????progressDialog

            } else if (msg.what == WHAT_IS_FLASH_SYSTEM_DATA) {
                Intent intentw = new Intent();
                intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
                intentw.putExtra("msg", Define.BoardCast_FlashUI_SYSTEM_DATA);
                intentw.putExtra("state", false);
                mContext.sendBroadcast(intentw);
            } else if (msg.what == WHAT_IS_LEDUP_SOURCE) { // ???????????????????????????
                //if (DataStruct.CurMacMode.BOOL_INPUT_SOURCE) {
                DataStruct.RcvDeviceData.SYS.input_source = msg.arg1;
                DataStruct.SendDeviceData.SYS.input_source = msg.arg1;
                System.out.println("BUG ??????????????????");
                //??????????????????
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

            } else if (msg.what == WHAT_IS_BT_TIME_OUT) {//????????????
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

        DataStruct.U0RcvFrameFlg = NO; // ??????????????????????????????
        DataStruct.U0SendFrameFlg = NO; // ???????????????????????????
        DataStruct.U0SynDataError = false; // ?????????????????????????????????
        DataStruct.PcConnectFlg = NO;
        DataStruct.PcConnectCnt = 0;
        bool_OpBT = false; //????????????????????????????????????
        DataStruct.U0HeadFlg = NO;  //??????????????????

        //????????????????????????
        Intent intentw = new Intent();
        intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
        intentw.putExtra("msg", Define.BoardCast_FlashUI_ConnectState);
        intentw.putExtra("state", false);
        mContext.sendBroadcast(intentw);
        if (DeviceVerErrorFlg) {
            //??????????????????
            Intent intentd = new Intent();
            intentd.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
            intentd.putExtra("msg", Define.BoardCast_FlashUI_DeviceVersionsErr);
            intentd.putExtra("state", false);
            mContext.sendBroadcast(intentd);
        }
        //???????????????????????????????????????????????????????????????????????????????????????????????????????????????
        //??????????????????


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
        DataOptUtil.ComparedToSendData(false); //????????????
        System.out.println("BUG ????????????");

        DataStruct.U0SynDataSucessFlg = true; //????????????????????????
        DataStruct.isConnecting = true;  //?????????????????????
        bool_BT_ConTimeOut = false;
        BLE_DEVICE_STATUS = true;
        DataStruct.SEFF_USER_GROUP_OPT = 0;

        //????????????????????????
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
        //??????????????????
        Intent intentw = new Intent();
        intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
        intentw.putExtra("msg", Define.BoardCast_FlashUI_AllPage);
        intentw.putExtra("state", true);
        mContext.sendBroadcast(intentw);

    }

    // ?????????????????????
    public static void ReceiveDataFromDevice(int data, int type) {

        if (DataStruct.RcvDeviceData == null) {
            return;
        }

        if (MacCfg.Mcu == 7|| MacCfg.Mcu==8) {
            MDOptUtil.RecMusicBoxDataFromDevice(mContext, data & 0xff, Define.COMMUNICATION_WITH_BLUETOOTH_SPP_TWO);
        }

        //?????????????????????????????????

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
        //?????????
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
                    + DataStruct.RcvDeviceData.DataBuf[9] * 256 + DataStruct.CMD_LENGHT - 4)) // ????????????????????????????????????????????4
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

                if (DataStruct.RcvDeviceData.FrameEnd == DataStruct.FRAME_END) { // ????????????????????????
                    int sum = 0;
                    for (int i = 0; i < (DataStruct.RcvDeviceData.DataLen
                            + DataStruct.CMD_LENGHT - 6); i++) {
                        sum ^= DataStruct.RcvDeviceData.DataBuf[i];
                    }

                    if (sum == DataStruct.RcvDeviceData.CheckSum) { // ????????????
                        DataStruct.PcConnectFlg = YES;
                        DataStruct.PcConnectCnt = 0;
                        DataStruct.ComType = type; // ????????????

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

    // ???????????????????????????????????????????????? TODO
    public static void ProcessRcvData() {
//		System.out.println("BUG XXXXXXXXXXXXXXXXXXXXXX");
//
//		System.out.println("BUG ProcessRcvData FrameType:" + DataStruct.RcvDeviceData.FrameType);
//		System.out.println("BUG ProcessRcvData DataType:" + DataStruct.RcvDeviceData.DataType);
//		System.out.println("BUG ProcessRcvData ChannelID:" + DataStruct.RcvDeviceData.ChannelID);

        if (DataStruct.RcvDeviceData.FrameType == DataStruct.DATA_ACK) { // ???????????????
            if (DataStruct.RcvDeviceData.DataType == Define.MUSIC) {/*?????????MUSIC.2?????????????????????*/
                if (DEBUG)
                    System.out.println("BUG ## Channel MUSIC DataLen:" + DataStruct.RcvDeviceData.DataLen);
                if (DataStruct.CurMacMode.BOOL_USE_INS) {
                    if (DataStruct.RcvDeviceData.DataLen == Define.INS_LEN) {
                        DataOptUtil.FillRecDataStruct(Define.MUSIC, DataStruct.RcvDeviceData.ChannelID, DataStruct.RcvDeviceData.DataBuf, true);
                        //??????????????????
                    }
                } else {
                    if (DataStruct.RcvDeviceData.DataLen == Define.IN_LEN) {
                        DataOptUtil.FillRecDataStruct(Define.MUSIC, 0, DataStruct.RcvDeviceData.DataBuf, true);
                        //System.out.println("BUG ---Master Valume:" + DataStruct.RcvDeviceData.IN_CH[0].Valume);
                        //??????????????????
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

                        //????????????????????????
                        if (DataStruct.RcvDeviceData.ChannelID == (DataStruct.CurMacMode.Out.OUT_CH_MAX - 1)) {
                            //????????????????????????
                            if (DataStruct.RcvDeviceData.UserID == DataStruct.CurMacMode.MAX_USE_GROUP) {
                                //??????????????????
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
                    } else {//??????????????????????????????
                        DataOptUtil.FillRecDataStruct(Define.OUTPUT, DataStruct.RcvDeviceData.ChannelID, DataStruct.RcvDeviceData.DataBuf, true);
                        //DataOptUtil.SaveEQTo_EQ_Buf(RcvDeviceData.ChannelID);

                        //????????????????????????
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
                            DataStruct.RcvDeviceData.SYS.UserGroup[DataStruct.RcvDeviceData.UserID][i] = (char) DataStruct.RcvDeviceData.DataBuf[10 + i]; // ???????????????
                            DataStruct.MAC_DataBuf.data[DataStruct.RcvDeviceData.UserID].group_name[i] = (char) DataStruct.RcvDeviceData.DataBuf[10 + i];
                        }

                        break;

                    case Define.PC_SOURCE_SET:
                        DataStruct.RcvDeviceData.SYS.input_source = DataStruct.RcvDeviceData.DataBuf[10]; // ?????????(???????????????????????????)
                        // ??? 1
                        // ???
                        // AUX 3
                        // ??????  2
                        // ??????
                        DataStruct.RcvDeviceData.SYS.aux_mode = DataStruct.RcvDeviceData.DataBuf[11]; // ???????????????
                        // ???3???
                        // 0:4???AUX
                        // 1:..................
                        DataStruct.RcvDeviceData.SYS.device_mode = DataStruct.RcvDeviceData.DataBuf[12];  // ??????????????????0x02
                        // ?????????????????????????????????????????????0x01????????????????????????????????????????????????PC????????????????????????
                        DataStruct.RcvDeviceData.SYS.hi_mode = DataStruct.RcvDeviceData.DataBuf[13];// ??????
                        DataStruct.RcvDeviceData.SYS.blue_gain = DataStruct.RcvDeviceData.DataBuf[14];// ??????
                        DataStruct.RcvDeviceData.SYS.aux_gain = DataStruct.RcvDeviceData.DataBuf[15];// ??????
                        DataStruct.RcvDeviceData.SYS.DigitMod = DataStruct.RcvDeviceData.DataBuf[16];// ??????
                        DataStruct.RcvDeviceData.SYS.none5 = DataStruct.RcvDeviceData.DataBuf[17];// ??????

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

                        System.out.println("BUG ???????????????"+MacCfg.DeviceVerString);

                        if (!(MacCfg.DeviceVerString.contains(MacCfg.MCU_Version))) {
                            MacCfg.DeviceVerString = "";
                            DeviceVerErrorFlg = true;
                            return;
                        } else {
                            DataStruct.B_InitLoad = true;
                        }


                        //?????????????????????????????????????????????MCU_Versions???????????????????????????
                        if (DataStruct.B_InitLoad) {
                            DataStruct.B_InitLoad = false;

                            Message msgil = Message.obtain();
                            msgil.what = WHAT_IS_INIT_LOADING;
                            mHandler.sendMessage(msgil);
                        }

                        bool_OpBT = false;//??????????????????
                        break;

                    case Define.SYSTEM_DATA:
                        DataStruct.RcvDeviceData.SYS.main_vol = DataStruct.RcvDeviceData.DataBuf[10]
                                + DataStruct.RcvDeviceData.DataBuf[11] * 256;  // ???????????????(?????????????????????????????????)
                        // -60~0dB
                        DataStruct.RcvDeviceData.SYS.alldelay = DataStruct.RcvDeviceData.DataBuf[12]; // DSP?????????
                        // 0~100
                        // 0.01s~1s
                        DataStruct.RcvDeviceData.SYS.noisegate_t = DataStruct.RcvDeviceData.DataBuf[13];// ?????????
                        // -120dbu~+10dbu,stp:1dbu,????????????0~130
                        DataStruct.RcvDeviceData.SYS.AutoSource = DataStruct.RcvDeviceData.DataBuf[14];    // ??????????????????
                        // 0???
                        // 1???
                        DataStruct.RcvDeviceData.SYS.AutoSourcedB = DataStruct.RcvDeviceData.DataBuf[15];  // ?????????????????????????????????
                        // -120dB~0dB
                        DataStruct.RcvDeviceData.SYS.MainvolMuteFlg = DataStruct.RcvDeviceData.DataBuf[16];// ?????????????????????????????????????????????????????????????????????
                        DataStruct.RcvDeviceData.SYS.none6 = DataStruct.RcvDeviceData.DataBuf[17];// ??????
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
                        System.out.println("BUG ??????????????????Proc_Amp_Mode???"+DataStruct.RcvDeviceData.SYS.out8_spk_type);
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
                                msg.arg1 = MacCfg.input_sourcetemp;// ????????????????????????
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

//                        System.out.println("BUG?????????????????????\t"+DataStruct.RcvDeviceData.DataBuf[14]
//                                +"?????????"+ DataStruct.RcvDeviceData.DataBuf[15]+"?????????"+DataStruct.RcvDeviceData.DataBuf[13]);
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
                        MacCfg.CurProID = DataStruct.RcvDeviceData.DataBuf[10];// ???????????????ID
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
                        System.out.println("BUG ?????????????????????"+DataStruct.RcvDeviceData.SYS.none[4]);
                        break;
                    default:
                        break;
                }
            } else {
                ;
            }

            DataStruct.U0RcvFrameFlg = YES; // rcv one frame end
            DataStruct.isConnecting = true;  // ?????????????????????
            DataStruct.isConnecting = true;
            if (DataStruct.isConnecting != DataStruct.isConnectingOld) {
                DataStruct.isConnectingOld = DataStruct.isConnecting;
                //setConnectStatus(DataStruct.isConnecting);
            }
        } else if (DataStruct.RcvDeviceData.FrameType == DataStruct.ERROR_ACK) { // ????????????
            BTS_Again = true;
        } else if (DataStruct.RcvDeviceData.FrameType == DataStruct.RIGHT_ACK) { // ????????????
            DataStruct.isConnecting = true;
            if (DataStruct.isConnecting != DataStruct.isConnectingOld) {
                DataStruct.isConnectingOld = DataStruct.isConnecting;
                //setConnectStatus(DataStruct.isConnecting);
            }

            HandTrue();


            DataStruct.U0RcvFrameFlg = YES; // rcv one frame end
            DataStruct.isConnecting = true;  // ?????????????????????
        } else {
            ;
        }
    }

    /**
     * ??????????????????????????????
     * */
    private static void HandTrue(){
        if(MacCfg.Mcu==7&&DataStruct.RcvDeviceData.ChannelID == Define.SYSTEM_SPK_TYPEB){

        }
    }

    public void serviceOnDestroy() {
        DataStruct.isConnecting = false;
        if (rThread != null) {
            rThread.interrupt(); // ??????????????????
            rThread = null;
        }
        if (sThread != null) {
            sThread.interrupt(); // ??????????????????
            sThread = null;
        }
        if (aThread != null) {
            aThread.interrupt(); // ??????????????????
            aThread = null;
        }
        if (tThread != null) {
            tThread.interrupt(); // ??????????????????
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

        DataStruct.U0RcvFrameFlg = NO; // ??????????????????????????????
        DataStruct.U0SendFrameFlg = NO; // ???????????????????????????
        DataStruct.U0SynDataError = false; // ?????????????????????????????????
        DataStruct.PcConnectFlg = NO;
        DataStruct.PcConnectCnt = 0;

        bool_OpBT = false; //????????????????????????????????????
        DataStruct.U0HeadFlg = NO;  //??????????????????
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
