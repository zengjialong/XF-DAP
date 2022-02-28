package com.chs.mt.xf_dap.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.chs.mt.xf_dap.MusicBox.MusicFragment;
import com.chs.mt.xf_dap.R;
import com.chs.mt.xf_dap.bluetooth.spp_ble.DeviceListActivity;
import com.chs.mt.xf_dap.datastruct.DataStruct;
import com.chs.mt.xf_dap.datastruct.Define;
import com.chs.mt.xf_dap.datastruct.MacCfg;
import com.chs.mt.xf_dap.fragment.DelayAuto_Fragment;
import com.chs.mt.xf_dap.fragment.DelayFRS_Draw_Fragment;
import com.chs.mt.xf_dap.fragment.EQ_Fragment;
import com.chs.mt.xf_dap.fragment.Home_Fragment;
import com.chs.mt.xf_dap.fragment.MixerFragment;
import com.chs.mt.xf_dap.fragment.OuputTypeFragment;
import com.chs.mt.xf_dap.fragment.dialogFragment.AboutDialogFragment;
import com.chs.mt.xf_dap.fragment.dialogFragment.AlertDialogFragment;
import com.chs.mt.xf_dap.fragment.dialogFragment.AlertDialogIFragment;
import com.chs.mt.xf_dap.fragment.dialogFragment.Alert_Dialog;
import com.chs.mt.xf_dap.fragment.dialogFragment.BootDefaultSourceDialogFrament;
import com.chs.mt.xf_dap.fragment.dialogFragment.InfomationFragment;
import com.chs.mt.xf_dap.fragment.dialogFragment.LoadingDialogFragment;
import com.chs.mt.xf_dap.fragment.eightyach.Home_Simple_Fragment;
import com.chs.mt.xf_dap.fragment.fifty.HandDialogFrament;
import com.chs.mt.xf_dap.fragment.fifty.fifty_eq_Fragment;
import com.chs.mt.xf_dap.fragment.twohundredandfiveach.OuputtwoFragment;
import com.chs.mt.xf_dap.operation.DataOptUtil;
import com.chs.mt.xf_dap.operation.mOKhttpUtil;
import com.chs.mt.xf_dap.service.ServiceOfCom;
import com.chs.mt.xf_dap.updateApp.DownLoadCompleteReceiver;
import com.chs.mt.xf_dap.util.ConnectionUtil;
import com.chs.mt.xf_dap.util.LocationUtils;
import com.chs.mt.xf_dap.util.ToastUtil;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainTURTActivity extends FragmentActivity {
    public static final int WHAT_IS_Address = 0x24;
    private static final int COMPLETED = 0x25;
    private static final int FAIL = 0x26;

    private ProgressDialog progressDialog;
    private String TAG = "MainTNTActivity";
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    //页面
    public static final int LOCATION_CODE = 301;
    private DelayFRS_Draw_Fragment mDelayFRS_Draw = null;
    private EQ_Fragment mEQ = null;
    //    private Input_Fragment mInput = null;
    private DelayAuto_Fragment mDelayAuto = null;
    private OuputTypeFragment mOutputFRS = null;
    private OuputtwoFragment ouputtwoFragment = null;
    private MusicFragment musicFragment = null;//播放器的连接度

    private fifty_eq_Fragment fifty_eq_fragment = null;//50ACH的EQ界面

    //    private OutputXover_Fragment mOutputXover = null;
    private Home_Fragment mHome = null;
    private Home_Simple_Fragment mHome_simple = null;
    private MixerFragment mixerFragment = null;
    private Alert_Dialog myDialog;
    //顶部状态栏
    // private TextView TV_ViewPageName,TV_Connect;
    private Button B_ConnectState, B_ConMenu;
    private LinearLayout LLyout_Back;
    private boolean bool_HighSettings = false;
    private PopupMenuActivity popuMenu;
    private android.app.Fragment fragment;
    //导航按键
    private LinearLayout LY_Buttom;
    private Button btn_not_inchina;
    private String provider;
    private int ButtomItemMax = 8;

    private ImageView[] IV_ButtomSel = new ImageView[ButtomItemMax];
    private ImageView[] IV_ButtomItem = new ImageView[ButtomItemMax];
    private TextView[] TV_ButtomItemName = new TextView[ButtomItemMax];
    private RelativeLayout[] RLyout_ButtomItem = new RelativeLayout[ButtomItemMax];
    private TextView img_main;
    private ImageView img_title;//80ACH 需要显示为标题
    //Service端的Messenger对象
    private Messenger mServiceMessenger;
    //Activity端的Messenger对象
    private Messenger mActivityMessenger;
    private static Context mContext;
    private LocationManager locationManager;
    private String locationProvider = null;
    private Toast mToast;
    private CHS_Broad_FLASHUI_BroadcastReceiver CHS_Broad_Receiver;
    private DownLoadCompleteReceiver mDownLoadCompleteReceiver;
    private boolean EXITFLAG = false;
    private boolean isFront = false;

    private static final int WRITE_COARSE_LOCATION_REQUEST_CODE = 0;

    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;

    private LinearLayout ly_link;
    private TextView txt_ch0, txt_ch1;


    /**
     * 设置界面显示的图片除50以外的按钮
     */
    private int[] image = new int[]{
            R.drawable.tab_home_normal,
            R.drawable.tab_delay_normal,
            R.drawable.tab_channel_normal,
            R.drawable.tab_weight_normal,
            R.drawable.tab_eq_normal
    };

    //50ACH的软件
    private int[] image_fifty = new int[]{
            R.drawable.tab_home_normal,
            R.drawable.tab_delay_normal,
            R.drawable.tab_channel_normal,
            R.drawable.tab_eq_normal,
    };

//    IV_ButtomItem[0].setBackgroundResource(R.drawable.tab_home_normal);
//    IV_ButtomItem[1].setBackgroundResource(R.drawable.tab_delay_normal);
//    IV_ButtomItem[2].setBackgroundResource(R.drawable.tab_channel_normal);
//    IV_ButtomItem[3].setBackgroundResource(R.drawable.tab_weight_normal);
//    IV_ButtomItem[4].setBackgroundResource(R.drawable.tab_eq_normal);


    /*********************************************************************/
    /*******************  蓝牙SPP-LE   通信     **********************/
    /*********************************************************************/
    private static final int REQUEST_FINE_LOCATION = 168;

    private LocationManager lm;
    private static final int REQUEST_ADDRESS = 0;
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private static final int REQUEST_EXTERNAL_STORAGE = 3;
    private static final int REQUEST_WirteSettings = 4;
    private static final int REQUEST_INTENT = 5;
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    public static String EXTRA_DEVICE_Name = "device_name";
    // 连接设备的名称
    private String mConnectedDeviceName = null;
    private StringBuffer mOutStringBuffer;
    // 本地蓝牙适配器
    private BluetoothAdapter mBluetoothAdapter = null;
    static final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
    public static BluetoothSocket btSocket;
    private LinearLayout ly_output;
    /**
     * 需要连接的蓝牙设备
     */
    private BluetoothDevice currentBluetoothDevice;
    private String latLongString;
    private LoadingDialogFragment mLoadingDialogFragment = null;
    private AlertDialogFragment alertDialogFragment = null;
    private AlertDialogIFragment alertDialogIFragment = null;
    private TextView txt_now;
    private EditText latTv, lngTv;
    private Button bn;
    private LinearLayout ly_delay_seting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);

        mContext = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.chs_activity_main);
        initData();
        initView();
        // 获取本地蓝牙适配器
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        setCheckPosition();
        // new WorkThread().start();
        //initLoadDialog();

        if (MacCfg.Mcu != 0) {
            setLinkText();
        } else {
            ly_link.setVisibility(View.GONE);
        }

    }

    /**
     * 检查安卓中的两个权限是否使用
     */
    private void setCheckPosition() {
        if (showCheckPermissions()) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //请求权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_CODE);
            } else {
                setPoistion();
            }
        } else {
            setPoistion();
        }
    }

    /**
     * 是否应该检查权限
     */
    public boolean showCheckPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 表示网络不佳时候
     */
    private void isChinaTimeOut() {
        btn_not_inchina.setText(String.valueOf(getResources().getString(R.string.nowyouaddressReplay)));
        if (DataStruct.RcvDeviceData.SYS.none6 == 0x83) {
            btn_not_inchina.setVisibility(View.VISIBLE);
        } else {
            btn_not_inchina.setVisibility(View.GONE);
        }

    }

    private void isChina(String addressname) {
        try {
            btn_not_inchina.setVisibility(View.GONE);
            String[] strarray = addressname.split("[,]");
            if (strarray.length >= 2 && !addressname.equals("")) {
                if ((!strarray[0].contains("中国")) && (!strarray[0].contains("CN")) || strarray[1].contains("台湾省")
                        || strarray[2].contains("香港特别行政区") ||
                        strarray[2].contains("澳门特别行政区")) {
                    btn_not_inchina.setText(String.valueOf(getResources().getString(R.string.nochinsa)));
                    if (DataStruct.RcvDeviceData.SYS.none6 == 0x83) {
                        btn_not_inchina.setVisibility(View.VISIBLE);
                    } else {
                        btn_not_inchina.setVisibility(View.GONE);
                    }
                } else {
                    btn_not_inchina.setVisibility(View.GONE);
                    DataStruct.RcvDeviceData.SYS.none6 = 1;
                }
            } else {
                if (DataStruct.RcvDeviceData.SYS.none6 == 0x83) {
                    btn_not_inchina.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * 打开设置网络界面
     * */
    public void setNetworkMethod(final Context context) {
        //提示对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("网络设置提示").setMessage("网络连接不可用,是否进行设置?").setPositiveButton("设置", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                Intent intent = null;
                //判断手机系统的版本  即API大于10 就是3.0或以上版本
                intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                startActivityForResult(intent, REQUEST_INTENT);
            }
        }).show();

    }


    /**
     * 打开GPS对话框
     */
    private void openGPSDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请打开GPS连接")
                .setIcon(R.drawable.newchs_logo)
                .setMessage("为了提高定位的准确度，更好的为您服务，请打开GPS")
                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //跳转到手机打开GPS页面
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        //设置完成后返回原来的界面
                        startActivityForResult(intent, REQUEST_ADDRESS);
                    }
                }).show();
    }

    /**
     * 开始加载
     */
    private void initLoadDialog() {
        CheckAndOpenBluetooth();
    }

    private void initData() {
        DataOptUtil.InitApp(mContext);
        //绑定Service
        Intent intent = new Intent(mContext, ServiceOfCom.class);
        mContext.bindService(intent, connection, Service.BIND_AUTO_CREATE);

        //动态注册CHS_Broad_BroadcastReceiver
        CHS_Broad_Receiver = new CHS_Broad_FLASHUI_BroadcastReceiver();
        IntentFilter CHS_Broad_filter = new IntentFilter();
        CHS_Broad_filter.addAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
        //注册receiver
        registerReceiver(CHS_Broad_Receiver, CHS_Broad_filter);
    }

    private void initView() {
        initFragment();
        initTopbar();
        initBottomBar();
        backToMain();
        titleShow();
    }


    private void initFragment() {
        mEQ = new EQ_Fragment();
        mDelayFRS_Draw = new DelayFRS_Draw_Fragment();
        mDelayAuto = new DelayAuto_Fragment();
        mOutputFRS = new OuputTypeFragment();
        mHome = new Home_Fragment();
        mixerFragment = new MixerFragment();
        mHome_simple = new Home_Simple_Fragment();
        ouputtwoFragment = new OuputtwoFragment();
        musicFragment = new MusicFragment();
        fifty_eq_fragment = new fifty_eq_Fragment();


        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        @SuppressLint("RestrictedApi") List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null && fragments.size() > 0) {
            for (Fragment fragment : fragments) {
                fragmentTransaction.remove(fragment);
            }
        }
        addFragment();
    }

    /**
     * 头部的按钮以及点击事件
     */
    private void initTopbar() {
        B_ConnectState = (Button) findViewById(R.id.id_b_Connect);

        LLyout_Back = (LinearLayout) findViewById(R.id.id_llyout_back);
        B_ConMenu = (Button) findViewById(R.id.id_b_menu);

        ly_link = (LinearLayout) findViewById(R.id.id_ly_link);
        txt_ch0 = (TextView) findViewById(R.id.id_txt_ch0);
        txt_ch1 = (TextView) findViewById(R.id.id_txt_ch1);

        myDialog = new Alert_Dialog(this).builder();
        popuMenu = new PopupMenuActivity(this);
        B_ConMenu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                popuMenu.showLocation(R.id.id_b_menu);// 设置弹出菜单弹出的位置
                popuMenu.setOnItemClickListener(new PopupMenuActivity.OnItemClickListener() {
                    @Override
                    public void onClick(int item, String str) {
                        switch (item) {
                            case 2:
                                showHandDialog();
                                break;


                            case 3:
                                AboutDialogFragment aboutDialog = new AboutDialogFragment();
                                aboutDialog.show(getFragmentManager(), "AboutDialogFragment");
                                aboutDialog.OnSetOnClickDialogListener(
                                        new AboutDialogFragment.SetOnClickDialogListener() {
                                            @Override
                                            public void onClickDialogListener(int type, boolean boolClick) {

                                            }
                                        }
                                );
                                break;
                            case 4:
                                Timer timer = new Timer();
                                timer.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        MenuExit();
                                    }
                                }, 388);
                                break;
                            case 5:
                                showBootSourceDialog();
                                break;
                            case 6:
                                myDialog.setGone().setTitle(getResources().getString(R.string.Prompting)).setMsg(getResources().getString(R.string.Factory_setting_bool)).setNegativeButton(getResources().getString(R.string.cancel), null).setPositiveButton(getResources().getString(R.string.Sure), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        DataStruct.SendDeviceData.FrameType = DataStruct.WRITE_CMD;
                                        DataStruct.SendDeviceData.DeviceID = 0x01;
                                        DataStruct.SendDeviceData.UserID = 0x00;
                                        DataStruct.SendDeviceData.DataType = Define.SYSTEM;
                                        DataStruct.SendDeviceData.ChannelID = Define.SYSTEM_RESET_MCU;
                                        DataStruct.SendDeviceData.DataID = 0x00;
                                        DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;
                                        DataStruct.SendDeviceData.PcCustom = 0x00;
                                        DataStruct.SendDeviceData.DataLen = 8;

                                        DataStruct.U0SendFrameFlg = 1;
                                        DataOptUtil.SendDataToDevice(true);

                                        ServiceOfCom.disconnectSet();

                                    }
                                }).show();
                                break;
                            case 7:
                                showInfomationDialog();
                                break;

                            default:
                                break;

                        }

                    }
                });

            }
        });

        B_ConnectState.setOnClickListener(new View.OnClickListener() {


            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {


                //if (new WorkThread() != null) {
                //  new WorkThread().interrupt(); // 关闭接收线程
                //new WorkThread() = null;
                //  }
                //new WorkThread().start();
                if (DataStruct.isConnecting) {//关闭蓝牙
                    DataStruct.isConnecting = false;
                    DataStruct.ManualConnecting = true;
                    ServiceOfCom.disconnectSet();

                    if (MacCfg.COMMUNICATION_MODE == Define.COMMUNICATION_WITH_WIFI) {

                    } else if (MacCfg.COMMUNICATION_MODE == Define.COMMUNICATION_WITH_BLUETOOTH_SPP) {
                        sentMstToService(ServiceOfCom.WHAT_IS_Opt_ConnectDevice, ServiceOfCom.Arg_ConnectStateBtn_OFF, 0);
                    } else if (MacCfg.COMMUNICATION_MODE == Define.COMMUNICATION_WITH_BLUETOOTH_SPP_TWO) {
                        sentMstToService(ServiceOfCom.WHAT_IS_Opt_DisconnectDeviceSPP_LE, ServiceOfCom.Arg_ConnectStateBtn_OFF, 0);
                    }
                    setConnectState(false);
                    ToastMsg(getString(R.string.offline_link_bt));
                } else {//打开蓝牙
                    if (ConnectionUtil.isConn(mContext)) {
                        System.out.println("BUG 连接上网络" + ConnectionUtil.isConn(mContext));
                        if (ConnectionUtil.isOpen(mContext)) {
                            if (mBluetoothAdapter != null) {
                                if (!mBluetoothAdapter.isEnabled()) {
                                    Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                                    startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                                    return;
                                }
                                setupChat();
                            }
                            // ToolsUtil.Toast(mContext,mContext.getString(R.string.TriesToConnectTo));
                        } else {
                            openGPS2();
                        }
                    } else {
                        showNoNetWorkDlg(mContext);
                    }
                }
            }
        });


        LLyout_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMain();
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == COMPLETED) {
                //System.out.println("BUG 完成后的值为" + MacCfg.addressName);
                btn_not_inchina.setEnabled(false);
                btn_not_inchina.setText(String.valueOf(getResources().getString(R.string.nowyouaddress)));
                isChina(MacCfg.addressName);
            } else if (msg.what == FAIL) {
                //  System.out.println("BUG 应该进入这里");
                btn_not_inchina.setEnabled(true);
                isChinaTimeOut();
            }
        }
    };

    /**
     * 显示开机默认音源
     */
    private void showBootSourceDialog() {
        BootDefaultSourceDialogFrament bootDefaultSourceDialogFrament = new BootDefaultSourceDialogFrament();
        bootDefaultSourceDialogFrament.show(getSupportFragmentManager(), "");
    }

    /**
     * 显示免责申明
     */
    private void showInfomationDialog() {
        InfomationFragment infomationFragment = new InfomationFragment();
        infomationFragment.show(getFragmentManager(), "");
    }


    /**
     * 显示手调开关
     */
    private void showHandDialog() {
        HandDialogFrament handDialogFrament = new HandDialogFrament();
        handDialogFrament.show(getSupportFragmentManager(), "");
        handDialogFrament.OnSetOnClickDialogListener(new HandDialogFrament.SetOnClickDialogListener() {
            @Override
            public void onClickDialogListener(boolean boolClick) {
                DataStruct.U0SynDataSucessFlg = false;
                DataStruct.SEFF_USER_GROUP_OPT = 1;

                DataOptUtil.ComparedToSendData(true);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (DataStruct.isConnecting) {
                            for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX_USE; i++) {
                                DataStruct.SendDeviceData.FrameType = DataStruct.READ_CMD;
                                DataStruct.SendDeviceData.DeviceID = 0x01;
                                DataStruct.SendDeviceData.UserID = 0x00;
                                DataStruct.SendDeviceData.DataType = Define.OUTPUT;
                                DataStruct.SendDeviceData.ChannelID = i;
                                DataStruct.SendDeviceData.DataID = 0x77;//读当前组的数据
                                DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;//
                                DataStruct.SendDeviceData.PcCustom = 0x00;// 自定义字符，发什么下去，返回什么
                                DataStruct.SendDeviceData.DataLen = 0x00;
                                DataOptUtil.SendDataToDevice(false);
                            }
                            Intent intentw = new Intent();
                            intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
                            intentw.putExtra("msg", Define.BoardCast_FlashUI_ShowLoading);
                            intentw.putExtra("state", false);
                            mContext.sendBroadcast(intentw);
                        }
                    }
                }).start();


            }
        });
    }


    //工作线程
    private class WorkThread extends Thread {
        @Override
        public void run() {
            // int i = 0;

            if (DataStruct.isConnecting) {
                //......处理比较耗时的操作
                for (int i = 0; i <= 4; i++) {
                    if (MacCfg.addressName.equals("")) {
                        mOKhttpUtil.registerErrorLogInformation(mContext, "");

                        if (i < 4) {
                            Message msg1 = new Message();
                            msg1.what = COMPLETED;
                            handler.sendMessage(msg1);
                        }
                        try {
                            Thread.sleep(3000);
                            if (i == 3) {
                                if (MacCfg.addressName.equals("")) {
                                    setAddress();
                                }

                            }


                            if (MacCfg.addressName.equals("")) {
                                if (i == 4) {
                                    MacCfg.DATAERROR = true;
                                    Message msg = new Message();
                                    msg.what = FAIL;
                                    handler.sendMessage(msg);
                                    break;
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }


    private void backToMain() {
        if (MacCfg.Mcu == 0) {
            if (mHome_simple == null) {
                mHome_simple = new Home_Simple_Fragment();
            }
            backtitleShow();
            mHome_simple.FlashPageUI();
            getSupportFragmentManager()
                    .beginTransaction()
                    .show(mHome_simple)
                    .hide(mDelayFRS_Draw)
                    .commit();
        } else {
            if (mHome == null) {
                mHome = new Home_Fragment();
            }
            img_main.setText(getResources().getString(R.string.Home));
            mHome.FlashPageUI();
            getSupportFragmentManager()
                    .beginTransaction()
                    .show(mHome)
                    .hide(mDelayAuto)
                    .hide(mOutputFRS)
                    .hide(mEQ)
                    .hide(mixerFragment)
                    .commit();
        }

        setMenuVisiable();
        MacCfg.CurPage = Define.UI_PAGE_HOME;
    }

    private void setMenuVisiable() {
        LLyout_Back.setVisibility(View.GONE);
        B_ConMenu.setVisibility(View.VISIBLE);

        if (MacCfg.Mcu == 0) {
            ly_delay_seting.setVisibility(View.VISIBLE);
        } else {
            ly_delay_seting.setVisibility(View.GONE);
        }

    }

    private void setBackVisiable() {
        LLyout_Back.setVisibility(View.VISIBLE);
        B_ConMenu.setVisibility(View.GONE);
        ly_delay_seting.setVisibility(View.GONE);

        System.out.println("BUG getVisibleFragment()=" + getVisibleFragment());
        img_main.setVisibility(View.VISIBLE);
        img_title.setVisibility(View.GONE);

    }

    private void MenuExit() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
        System.exit(0);
    }

    private void initBottomBar() {
        LY_Buttom = (LinearLayout) findViewById(R.id.id_rlyout_bottom);
        btn_not_inchina = findViewById(R.id.id_not_in_china);
        fragment = getFragmentManager().findFragmentById(R.id.id_framelayout);
        img_main = findViewById(R.id.id_img_main);
        img_title = findViewById(R.id.id_img_title);
        ly_delay_seting = findViewById(R.id.id_ly_delay);

        //导航按键
        RLyout_ButtomItem[0] = (RelativeLayout) findViewById(R.id.id_rlyout_0);
        RLyout_ButtomItem[1] = (RelativeLayout) findViewById(R.id.id_rlyout_1);
        RLyout_ButtomItem[2] = (RelativeLayout) findViewById(R.id.id_rlyout_2);
        RLyout_ButtomItem[3] = (RelativeLayout) findViewById(R.id.id_rlyout_3);
        RLyout_ButtomItem[4] = (RelativeLayout) findViewById(R.id.id_rlyout_4);
        RLyout_ButtomItem[5] = (RelativeLayout) findViewById(R.id.id_rlyout_5);
        RLyout_ButtomItem[6] = (RelativeLayout) findViewById(R.id.id_rlyout_6);
        RLyout_ButtomItem[7] = (RelativeLayout) findViewById(R.id.id_rlyout_7);
        //选中背景
        IV_ButtomSel[0] = (ImageView) findViewById(R.id.id_iv_item_bg_0);
        IV_ButtomSel[1] = (ImageView) findViewById(R.id.id_iv_item_bg_1);
        IV_ButtomSel[2] = (ImageView) findViewById(R.id.id_iv_item_bg_2);
        IV_ButtomSel[3] = (ImageView) findViewById(R.id.id_iv_item_bg_3);
        IV_ButtomSel[4] = (ImageView) findViewById(R.id.id_iv_item_bg_4);
        IV_ButtomSel[5] = (ImageView) findViewById(R.id.id_iv_item_bg_5);
        IV_ButtomSel[6] = (ImageView) findViewById(R.id.id_iv_item_bg_6);
        IV_ButtomSel[7] = (ImageView) findViewById(R.id.id_iv_item_bg_7);
        //项目图案
        IV_ButtomItem[0] = (ImageView) findViewById(R.id.id_iv_item_img_0);
        IV_ButtomItem[1] = (ImageView) findViewById(R.id.id_iv_item_img_1);
        IV_ButtomItem[2] = (ImageView) findViewById(R.id.id_iv_item_img_2);
        IV_ButtomItem[3] = (ImageView) findViewById(R.id.id_iv_item_img_3);
        IV_ButtomItem[4] = (ImageView) findViewById(R.id.id_iv_item_img_4);
        IV_ButtomItem[5] = (ImageView) findViewById(R.id.id_iv_item_img_5);
        IV_ButtomItem[6] = (ImageView) findViewById(R.id.id_iv_item_img_6);
        IV_ButtomItem[7] = (ImageView) findViewById(R.id.id_iv_item_img_7);
        //项目文字
        TV_ButtomItemName[0] = (TextView) findViewById(R.id.id_tv_item_name_0);
        TV_ButtomItemName[1] = (TextView) findViewById(R.id.id_tv_item_name_1);
        TV_ButtomItemName[2] = (TextView) findViewById(R.id.id_tv_item_name_2);
        TV_ButtomItemName[3] = (TextView) findViewById(R.id.id_tv_item_name_3);
        TV_ButtomItemName[4] = (TextView) findViewById(R.id.id_tv_item_name_4);
        TV_ButtomItemName[5] = (TextView) findViewById(R.id.id_tv_item_name_5);
        TV_ButtomItemName[6] = (TextView) findViewById(R.id.id_tv_item_name_6);
        TV_ButtomItemName[7] = (TextView) findViewById(R.id.id_tv_item_name_7);

        initBottomItemClick(0);

        for (int i = 0; i < MacCfg.ButtomItemMaxUse; i++) {
            RLyout_ButtomItem[i].setVisibility(View.VISIBLE);
            RLyout_ButtomItem[i].setTag(i);
            RLyout_ButtomItem[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int sel = (int) v.getTag();
                    BottomItemClick(sel);
                }
            });
        }
        btn_not_inchina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog(mContext, getResources().getString(R.string.toInfo));
            }
        });

        ly_delay_seting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomItemClick(5);
            }
        });
    }


    public void showProgressDialog(final Context mContext, String text) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }
        progressDialog.setMessage(text);    //设置内容
        progressDialog.setCancelable(false);//点击屏幕和按返回键都不能取消加载框
        progressDialog.show();

        //设置超时自动消失
        setDissDialog();
    }

    private void setProgressDialog() {
        dismissProgressDialog();
    }

    private void setDissDialog() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // isChina(LocationUtils.getInstance().getLocations(mContext));
                MacCfg.DATAERROR = false;
                //取消加载框
                if (dismissProgressDialog()) {
                    //超时处理
                    //Toast.makeText(mContext,"你的位置确实不在中国大陆",Toast.LENGTH_SHORT);
                    LocationUtils.getInstance().getLocations(mContext);

                    // progressDialog.setMessage("你现在可能因为网络问题不能定位不到当前位置");
                    //  }
                }
            }
        }, 10000);//超时时间60秒
    }

    public Boolean dismissProgressDialog() {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
                return true;//取消成功
            }
        }
        return false;//已经取消过了，不需要取消
    }

    /**
     * 除了50ACH其他的界面底部都是一样的
     */
    void initBottomItemClick(int sel) {

        for (int i = 0; i < image.length; i++) {
            IV_ButtomItem[i].setImageResource(image[i]);
        }

        for (int i = 0; i < image.length; i++) {
            IV_ButtomItem[i].setColorFilter(getResources().getColor(R.color.img_normal));
        }
        IV_ButtomItem[sel].setColorFilter(getResources().getColor(R.color.img_press));

        TV_ButtomItemName[0].setText(getResources().getString(R.string.Home));
        TV_ButtomItemName[1].setText(getResources().getString(R.string.SetDelay));
        TV_ButtomItemName[2].setText(getResources().getString(R.string.Output));
        TV_ButtomItemName[3].setText(getResources().getString(R.string.Weight));
        TV_ButtomItemName[4].setText(getResources().getString(R.string.EQ));

        //选择背景
        for (int i = 0; i < MacCfg.ButtomItemMaxUse; i++) {
            IV_ButtomSel[i].setVisibility(View.INVISIBLE);
            TV_ButtomItemName[i].setTextColor(getResources().getColor(R.color.text_color_Bottom_Bar_normal));
        }
        //IV_ButtomSel[sel].setVisibility(View.VISIBLE);
        TV_ButtomItemName[sel].setTextColor(getResources().getColor(R.color.text_color_Bottom_Bar_press));


        switch (sel) {
            case 0:
                img_main.setText(getResources().getString(R.string.Home));
                break;
            case 1:
                img_main.setText(getResources().getString(R.string.SetDelay));
                break;
            case 2:
                img_main.setText(getResources().getString(R.string.Output));
                break;
            case 3:
                img_main.setText(getResources().getString(R.string.Weight));
                break;
            case 4:
                img_main.setText(getResources().getString(R.string.EQ));
                break;
            case 5:
                img_main.setText(getResources().getString(R.string.Delay_select));
                break;
            case 7:
                img_main.setText(getResources().getString(R.string.Player));
                break;
            default:
                break;
        }
    }


    public void BottomItemClick(int sel) {
        initBottomItemClick(sel);
        isChina(LocationUtils.getInstance().getLocations(mContext));
        if (MacCfg.Mcu != 6 && MacCfg.Mcu != 7 && MacCfg.Mcu != 8) {
            switch (sel) {
                case 0:
                    MacCfg.CurPage = Define.UI_PAGE_HOME;
                    if (mHome == null) {
                        mHome = new Home_Fragment();
                    }
                    mHome.FlashPageUI();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .show(mHome)
                            .hide(mDelayAuto)
                            .hide(mixerFragment)
                            .hide(mOutputFRS)
                            .hide(mEQ)
                            .commit()
                    ;
                    break;
                case 1:
                    MacCfg.CurPage = Define.UI_PAGE_DELAY;
                    if (mDelayAuto == null) {
                        mDelayAuto = new DelayAuto_Fragment();
                    }
                    mDelayAuto.FlashPageUI();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .show(mDelayAuto)
                            .hide(mHome)
                            .hide(mOutputFRS)
                            .hide(mEQ)
                            .hide(mixerFragment)
                            .commit()
                    ;
                    break;
                case 2:
                    MacCfg.CurPage = Define.UI_PAGE_OUTPUT;
                    if (mOutputFRS == null) {
                        mOutputFRS = new OuputTypeFragment();
                    }
                    mOutputFRS.FlashPageUI();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .show(mOutputFRS)
                            .hide(mDelayAuto)
                            .hide(mHome)
                            .hide(mEQ)
                            .hide(mixerFragment)
                            .commit()
                    ;
                    break;

                case 3:
                    MacCfg.CurPage = Define.UI_PAGE_MIXER;
                    if (mixerFragment == null) {
                        mixerFragment = new MixerFragment();
                    }
                    mixerFragment.FlashPageUI();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .show(mixerFragment)
                            .hide(mDelayAuto)
                            .hide(mOutputFRS)
                            .hide(mEQ)
                            .hide(mHome)
                            .commit()
                    ;
                    break;


                case 4:

//                    MacCfg.CurPage = Define.UI_PAGE_EQ;
//                    if (musicFragment == null) {
//                        musicFragment = new MusicFragment();
//                    }
//                    musicFragment.FlashPageUI();
//                    getSupportFragmentManager()
//                            .beginTransaction()
//                            .show(musicFragment)
//                            .hide(mDelayAuto)
//                            .hide(mOutputFRS)
//                            .hide(mixerFragment)
//                            .hide(mHome)
//                            .hide(mEQ)
//                            .commit()
//                    ;

                    MacCfg.CurPage = Define.UI_PAGE_EQ;
                    if (mEQ == null) {
                        mEQ = new EQ_Fragment();
                    }
                    mEQ.FlashPageUI();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .show(mEQ)
                            .hide(mDelayAuto)
                            .hide(mOutputFRS)
                            .hide(mixerFragment)
                            .hide(mHome)
                            .commit()
                    ;
                    break;
                case 5:
                    MacCfg.CurPage = Define.UI_PAGE_Delay_DRAW;
                    if (mDelayFRS_Draw == null) {
                        mDelayFRS_Draw = new DelayFRS_Draw_Fragment();
                    }
                    mDelayFRS_Draw.FlashPageUI();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .show(mDelayFRS_Draw)
                            .hide(mHome_simple)
                            .commit();
                    setBackVisiable();
                    break;
                default:
                    break;
            }
        } else if (MacCfg.Mcu == 7) {
            if (MacCfg.Mcu == 7 && sel == 7) {
                LY_Buttom.setVisibility(View.GONE);
                LLyout_Back.setVisibility(View.VISIBLE);
                B_ConMenu.setVisibility(View.GONE);
            } else {
                LY_Buttom.setVisibility(View.VISIBLE);
                LLyout_Back.setVisibility(View.GONE);
                B_ConMenu.setVisibility(View.VISIBLE);
            }

            switch (sel) {
                case 0:
                    MacCfg.CurPage = Define.UI_PAGE_HOME;
                    if (mHome == null) {
                        mHome = new Home_Fragment();
                    }
                    mHome.FlashPageUI();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .show(mHome)
                            .hide(mDelayAuto)
                            .hide(musicFragment)
                            .hide(ouputtwoFragment)
                            .hide(fifty_eq_fragment)
                            .commit()
                    ;
                    break;
                case 1:
                    MacCfg.CurPage = Define.UI_PAGE_DELAY;
                    if (mDelayAuto == null) {
                        mDelayAuto = new DelayAuto_Fragment();
                    }
                    mDelayAuto.FlashPageUI();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .show(mDelayAuto)
                            .hide(mHome)
                            .hide(ouputtwoFragment)
                            .hide(fifty_eq_fragment)
                            .hide(musicFragment)
                            .commit()
                    ;
                    break;
                case 2:
                    MacCfg.CurPage = Define.UI_PAGE_OUTPUT;
                    if (ouputtwoFragment == null) {
                        ouputtwoFragment = new OuputtwoFragment();
                    }
                    ouputtwoFragment.FlashPageUI();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .show(ouputtwoFragment)
                            .hide(mDelayAuto)
                            .hide(mHome)
                            .hide(fifty_eq_fragment)
                            .hide(musicFragment)
                            .commit()
                    ;
                    break;
                case 3:
                    MacCfg.CurPage = Define.UI_PAGE_EQ;
                    if (fifty_eq_fragment == null) {
                        fifty_eq_fragment = new fifty_eq_Fragment();
                    }
                    fifty_eq_fragment.FlashPageUI();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .show(fifty_eq_fragment)
                            .hide(mDelayAuto)
                            .hide(ouputtwoFragment)
                            .hide(musicFragment)
                            .hide(mHome)
                            .commit()
                    ;
                    break;

                case 7:
                    MacCfg.CurPage = Define.UI_PAGE_MIXER;
                    if (musicFragment == null) {
                        musicFragment = new MusicFragment();
                    }

                    musicFragment.FlashPageUI();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .show(musicFragment)
                            .hide(mDelayAuto)
                            .hide(ouputtwoFragment)
                            .hide(fifty_eq_fragment)
                            .hide(mHome)
                            .commit()
                    ;
                    break;

            }
        } else if (MacCfg.Mcu == 8) {
            if (MacCfg.Mcu == 8 && sel == 7) {
                LY_Buttom.setVisibility(View.GONE);
                LLyout_Back.setVisibility(View.VISIBLE);
                B_ConMenu.setVisibility(View.GONE);
            } else {
                LY_Buttom.setVisibility(View.VISIBLE);
                LLyout_Back.setVisibility(View.GONE);
                B_ConMenu.setVisibility(View.VISIBLE);
            }

            switch (sel) {
                case 0:
                    MacCfg.CurPage = Define.UI_PAGE_HOME;
                    if (mHome == null) {
                        mHome = new Home_Fragment();
                    }
                    mHome.FlashPageUI();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .show(mHome)
                            .hide(mDelayAuto)
                            .hide(musicFragment)
                            .hide(mOutputFRS)
                            .hide(mEQ)
                            .hide(mixerFragment)
                            .commit()
                    ;
                    break;
                case 1:
                    MacCfg.CurPage = Define.UI_PAGE_DELAY;
                    if (mDelayAuto == null) {
                        mDelayAuto = new DelayAuto_Fragment();
                    }
                    mDelayAuto.FlashPageUI();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .show(mDelayAuto)
                            .hide(mHome)
                            .hide(mOutputFRS)
                            .hide(mEQ)
                            .hide(musicFragment)
                            .hide(mixerFragment)
                            .commit()
                    ;
                    break;
                case 2:
                    MacCfg.CurPage = Define.UI_PAGE_OUTPUT;
                    if (mOutputFRS == null) {
                        mOutputFRS = new OuputTypeFragment();
                    }
                    mOutputFRS.FlashPageUI();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .show(mOutputFRS)
                            .hide(mDelayAuto)
                            .hide(mHome)
                            .hide(mEQ)
                            .hide(musicFragment)
                            .hide(mixerFragment)
                            .commit()
                    ;
                    break;
                case 4:
                    MacCfg.CurPage = Define.UI_PAGE_EQ;
                    if (mEQ == null) {
                        mEQ = new EQ_Fragment();
                    }
                    mEQ.FlashPageUI();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .show(mEQ)
                            .hide(mDelayAuto)
                            .hide(mOutputFRS)
                            .hide(musicFragment)
                            .hide(mHome)
                            .hide(mixerFragment)
                            .commit()
                    ;
                    break;
                case 3:
                    MacCfg.CurPage = Define.UI_PAGE_MIXER;
                    if (mixerFragment == null) {
                        mixerFragment = new MixerFragment();
                    }
                    mixerFragment.FlashPageUI();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .show(mixerFragment)
                            .hide(mDelayAuto)
                            .hide(mOutputFRS)
                            .hide(musicFragment)
                            .hide(mHome)
                            .hide(mEQ)
                            .commit()
                    ;
                    break;


                case 7:
                    System.out.println("BUG 点击之后进入之类了呗");
                    MacCfg.CurPage = Define.UI_PAGE_MIXER;
                    if (musicFragment == null) {
                        musicFragment = new MusicFragment();
                    }
                    musicFragment.FlashPageUI();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .show(musicFragment)
                            .hide(mDelayAuto)
                            .hide(mOutputFRS)
                            .hide(mEQ)
                            .hide(mixerFragment)
                            .hide(mHome)
                            .commit()
                    ;
                    break;

            }
        } else {
            switch (sel) {
                case 0:
                    MacCfg.CurPage = Define.UI_PAGE_HOME;
                    if (mHome == null) {
                        mHome = new Home_Fragment();
                    }
                    mHome.FlashPageUI();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .show(mHome)
                            .hide(mDelayAuto)
                            .hide(mixerFragment)
                            .hide(ouputtwoFragment)
                            .hide(mEQ)
                            .commit()
                    ;
                    break;
                case 1:
                    MacCfg.CurPage = Define.UI_PAGE_DELAY;
                    if (mDelayAuto == null) {
                        mDelayAuto = new DelayAuto_Fragment();
                    }
                    mDelayAuto.FlashPageUI();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .show(mDelayAuto)
                            .hide(mHome)
                            .hide(ouputtwoFragment)
                            .hide(mEQ)
                            .hide(mixerFragment)
                            .commit()
                    ;
                    break;
                case 2:
                    MacCfg.CurPage = Define.UI_PAGE_OUTPUT;
                    if (ouputtwoFragment == null) {
                        ouputtwoFragment = new OuputtwoFragment();
                    }
                    ouputtwoFragment.FlashPageUI();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .show(ouputtwoFragment)
                            .hide(mDelayAuto)
                            .hide(mHome)
                            .hide(mEQ)
                            .hide(mixerFragment)
                            .commit()
                    ;
                    break;

                case 3:
                    MacCfg.CurPage = Define.UI_PAGE_MIXER;
                    if (mixerFragment == null) {
                        mixerFragment = new MixerFragment();
                    }
                    mixerFragment.FlashPageUI();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .show(mixerFragment)
                            .hide(mDelayAuto)
                            .hide(ouputtwoFragment)
                            .hide(mEQ)
                            .hide(mHome)
                            .commit()
                    ;
                    break;


                case 4:
                    MacCfg.CurPage = Define.UI_PAGE_EQ;
                    if (mEQ == null) {
                        mEQ = new EQ_Fragment();
                    }
                    mEQ.FlashPageUI();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .show(mEQ)
                            .hide(mDelayAuto)
                            .hide(ouputtwoFragment)
                            .hide(mixerFragment)
                            .hide(mHome)
                            .commit()
                    ;
                    break;
                default:
                    break;
            }
        }
        if (DataStruct.isConnecting) {
            isChina(MacCfg.addressName);
        }

    }

    /**
     * 刷新所有的页面数据
     */
    private void FlashFunsViewPage() {

        if (MacCfg.Mcu == 0) {
            if (mDelayFRS_Draw != null) {
                mDelayFRS_Draw.FlashPageUI();
            }
            if (mHome_simple != null) {
                mHome_simple.FlashPageUI();

            }
        } else if (MacCfg.Mcu == 6) {
            if (mDelayAuto != null) {
                mDelayAuto.FlashPageUI();
            }
            if (mEQ != null) {
                mEQ.FlashPageUI();
            }
            if (ouputtwoFragment != null) {
                ouputtwoFragment.FlashPageUI();
            }
            if (mHome != null) {
                mHome.FlashPageUI();
            }
            if (mixerFragment != null) {
                mixerFragment.FlashPageUI();
            }
        } else if (MacCfg.Mcu == 7) {
            if (mHome != null) {
                mHome.FlashPageUI();
            }

            if (mDelayAuto != null) {
                mDelayAuto.FlashPageUI();
            }
            if (fifty_eq_fragment != null) {
                fifty_eq_fragment.FlashPageUI();
            }
            if (ouputtwoFragment != null) {
                ouputtwoFragment.FlashPageUI();
            }
            if (musicFragment != null) {
                musicFragment.FlashPageUI();
            }

        } else if (MacCfg.Mcu == 8) {
            if (mHome != null) {
                mHome.FlashPageUI();
            }
            if (mDelayAuto != null) {
                mDelayAuto.FlashPageUI();
            }
            if (mEQ != null) {
                mEQ.FlashPageUI();
            }
            if (mOutputFRS != null) {
                mOutputFRS.FlashPageUI();
            }
            if (musicFragment != null) {
                musicFragment.FlashPageUI();
            }
            if (mixerFragment != null) {
                mixerFragment.FlashPageUI();
            }
        } else {
            if (mDelayAuto != null) {
                mDelayAuto.FlashPageUI();
            }
            if (mEQ != null) {
                mEQ.FlashPageUI();
            }
            if (mOutputFRS != null) {
                mOutputFRS.FlashPageUI();
            }
            if (mHome != null) {
                mHome.FlashPageUI();
            }
            if (mixerFragment != null) {
                mixerFragment.FlashPageUI();
            }
        }

        if (DataStruct.isConnecting) {
            if (MacCfg.addressName.equals("")) {
                new WorkThread().start();
            }
            isChina(MacCfg.addressName);
        }


    }

    private void FlashInputsource() {
        if (mHome != null) {
            mHome.flashInputsource();
        }
    }

    /**
     * 获取可见的Fragment
     */
    public Fragment getVisibleFragment() {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        @SuppressLint("RestrictedApi") List<Fragment> fragments = fragmentManager.getFragments();
        //System.out.println("BUG 这个的职位饿"+fragments.size());
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
    }


    //用接收广播刷新UI TODO
    public class CHS_Broad_FLASHUI_BroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getExtras().get("msg").toString();

            if (msg.equals(Define.BoardCast_FlashUI_ConnectState)) {


                boolean res = intent.getBooleanExtra("state", false);
                DataStruct.isConnecting = res;
                setConnectState(res);
            } else if (msg.equals(Define.BoardCast_FlashUI_AllPage)) {
                ToastMsg(getResources().getString(R.string.SynDataSucess));
                FlashFunsViewPage();
            } else if (msg.equals(Define.BoardCast_FlashUI_DeviceVersionsErr)) {
                ToastMsg(getResources().getString(
                        R.string.device)
                        + ":"
                        + DataStruct.DeviceVerString
                        + "\n"
                        + getResources().getString(R.string.apps)
                        + ":"
                        + MacCfg.App_versions/*getResources().getString(R.string.app_versions)*/
                        + "\n"
                        + getResources().getString(
                        R.string.version_error));
            } else if (msg.equals(Define.BoardCast_FlashUI_ShowLoading)) {
                // isFront=true;
                if (DataStruct.isConnecting) {
                    isFront = true;
                }
                showLoadingDialog();
            } else if ((msg.equals(Define.BoardCast_FlashUI_ADDRESS_ERRORDialog))) {
                setProgressDialog();
            } else if (msg.equals(Define.BoardCast_FlashUI_IninLoadUI)) {
                //InitLoadFunsViewPage();
            } else if (msg.equals(Define.BoardCast_FlashUI_InputSource)) {
                FlashInputsource();
            } else if (msg.equals(Define.BoardCast_FlashUI_ShowSucessMsg)) {
                ToastMsg(getResources().getString(R.string.SynDataSucess));
            } else if (msg.equals(Define.BoardCast_FlashUI_SYSTEM_DATA)) {

            } else if (msg.equals(Define.BoardCast_FlashUI_ConnectStateOFMsg)) {
                ToastMsg(getResources().getString(R.string.LinkOfMsg));
            } else if (msg.equals(Define.BoardCast_FlashUI_ADDRESS)) {
                String name = intent.getStringExtra("txtAddress");
                isChina(name);
            } else if (msg.equals(Define.BoardCast_FlashUI_InputSource_Val)) {
                System.out.println("BUG 因为近来了这里");
                if (MacCfg.Mcu != 0) {
                    if (mHome != null) {
                        mHome.flashInputsource();
                        mHome.flashMasterVol();
                    }
                } else {
                    if (mHome_simple != null) {
                        mHome_simple.flashInputSource();
                    }
                }
            } else if (msg.equals(Define.BoardCast_FlashUI_TOOPENINTENT)) {
                setNetworkMethod(mContext);
            } else if (msg.equals(Define.BoardCast_ReadGroupFlashHome)) {
                ToastMsg(getResources().getString(R.string.SynDataSucess));
                if (MacCfg.Mcu == 0) {
                    if (mHome_simple != null) {
                        mHome_simple.FlashPageUI();
                    }
                }

            } else if (msg.equals(Define.BoardCast_FlashUI_TOOPENGPS)) {
                openGPSDialog();
            } else if (msg.equals(Define.BoardCast_FlashUI_EQ)) {
                if (mEQ != null) {
                    mEQ.FlashPageUI();
                }
            }
        }

    }


    /**
     * 解决安卓6.0以上版本不能读取外部存储权限的问题
     *
     * @param activity
     * @return
     */
    public static boolean isGrantExternalRW(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            activity.requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, REQUEST_EXTERNAL_STORAGE);
            ActivityCompat.requestPermissions(activity, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE);
            return false;
        }

        return true;
    }

    private void sentMstToService(int what, int arg1, int arg2) {
        //创建消息
        Message message = Message.obtain();
        message.what = what;
        message.arg1 = arg1;
        message.arg2 = arg2;

        //设定消息要回应的Messenger
        message.replyTo = mActivityMessenger;
        //  handler.removeMessages(what);
        try {
            //通过ServiceMessenger将消息发送到Service中的Handler
            mServiceMessenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void setConnectState(boolean connect) {
        if (connect) {
            btn_not_inchina.setText(getResources().getString(R.string.nowyouaddress));
            btn_not_inchina.setVisibility(View.GONE);
            B_ConnectState.setBackgroundResource(R.drawable.chs_btn_connect_press_selector);
//            B_ConnectState.setText(getResources().getString(R.string.ST_BT_CONNECTED));
//            TV_Connect.setText(getResources().getString(R.string.ST_BT_CONNECTED));
//            TV_Connect.setTextColor(getResources().getColor(R.color.text_color_connected));
        } else {
            B_ConnectState.setBackgroundResource(R.drawable.chs_btn_connect_normal_selector);
            btn_not_inchina.setVisibility(View.GONE);
//            B_ConnectState.setText(getResources().getString(R.string.ST_BT_DISCONNECT));
//            TV_Connect.setText(getResources().getString(R.string.ST_BT_DISCONNECT));
//            TV_Connect.setTextColor(getResources().getColor(R.color.text_color_disconnected));
        }
    }

    private void showLoadingDialog() {
        if (mContext == null) {
            return;
        }
        if (!isFront) {
            return;
        }

        if (mLoadingDialogFragment == null) {
            mLoadingDialogFragment = new LoadingDialogFragment();
        }
        if (!mLoadingDialogFragment.isAdded()) {
            mLoadingDialogFragment.show(getFragmentManager(), "mLoadingDialogFragment");
        }
    }

    private void ToastMsg(String Msg) {
        if (null != mToast) {
            mToast.setText(Msg);
        } else {
            mToast = Toast.makeText(mContext, Msg, Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    /**
     * Service绑定状态的监听
     */
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //获取Service端的Messenger
            mServiceMessenger = new Messenger(service);
        }


        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    /*******************  蓝牙SPP-LE   通信     **********************/
    @SuppressLint("MissingPermission")
    private void CheckAndOpenBluetooth() {
        // 判断蓝牙是否可用
        if (mBluetoothAdapter == null) {
            ToastMsg("蓝牙是不可用的");
            return;
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        } else {
            setupChat();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*在这里，我们通过碎片管理器中的Tag，就是每个碎片的名称，来获取对应的fragment*/
        Fragment f=getSupportFragmentManager().findFragmentById(R.id.id_framelayout);
        /*然后在碎片中调用重写的onActivityResult方法*/
        f.onActivityResult(requestCode, resultCode, data);
        System.out.println("BUG onActivityResult requestCode=" + resultCode + "==" + requestCode);
        switch (requestCode) {
            case REQUEST_ADDRESS:
                // 当DeviceListActivity返回与设备连接的消息
                //if (resultCode == Activity.RESULT_OK) {
                System.out.println("BUG 进来");
                initLoadDialog();
                //}
                break;
            case REQUEST_CONNECT_DEVICE:
                // 当DeviceListActivity返回与设备连接的消息
                if (resultCode == Activity.RESULT_OK) {

                } else {

                }
                break;
            case REQUEST_ENABLE_BT:
                // 判断蓝牙是否启用
                if (resultCode == Activity.RESULT_OK) {
                    // setupChat();
                }
                break;
            case Define.ActivityResult_MusicPage_Back:

                break;

            case REQUEST_WirteSettings:
                // 当DeviceListActivity返回与设备连接的消息

                if (isGrantExternalRW((Activity) mContext)) {
                    initLoadDialog();
                }
                break;
            case REQUEST_INTENT:
                if (ConnectionUtil.isConn(mContext)) {
                    // initGPS();
                    System.out.println("BUG 现在打开了网络");
                    if (!ConnectionUtil.isOpen(mContext)) {
                        openGPSDialog();
                    } else {
                        // isChina(LocationUtils.getInstance().getLocations(mContext));
                    }
                }

                break;
        }


    }

    public LocationListener locationListener = new LocationListener() {
        // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            System.out.println("BUG 打开12这个函数");
        }

        // Provider被enable时触发此函数，比如GPS被打开
        @Override
        public void onProviderEnabled(String provider) {
            System.out.println("BUG 打开这个函数");
        }

        // Provider被disable时触发此函数，比如GPS被关闭
        @Override
        public void onProviderDisabled(String provider) {
            System.out.println("BUG 打开456这个函数");
        }

        //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                //不为空,显示地理位置经纬度
                updateShow(location);
                // Toast.makeText(mContext, location.getLongitude() + " " + location.getLatitude() + "", Toast.LENGTH_SHORT).show();
                // System.out.println("BUG 显示经纬度变化" + location.getLongitude() + " " + location.getLatitude() + "");
            } else {
                mOKhttpUtil.registerErrorLogInformation(mContext, "");
            }
        }
    };


    private void setupChat() {
        mayRequestLocation();
    }

    @SuppressLint("MissingPermission")
    private void mayRequestLocation() {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(mContext,
                    Manifest.permission.ACCESS_COARSE_LOCATION);

            int checkCallPhonePermissionfine = ContextCompat.checkSelfPermission(mContext,
                    Manifest.permission.ACCESS_FINE_LOCATION);

            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                //判断是否需要 向用户解释，为什么要申请该权限
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION))
                    Toast.makeText(mContext, "Android 6.0 Open Location", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_FINE_LOCATION);

            } else {
                if (mBluetoothAdapter != null) {
                    if (!mBluetoothAdapter.isEnabled()) {
                        Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                    } else {
                        Intent serverIntent = new Intent(mContext, DeviceListActivity.class);
                        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                    }
                }
            }
        } else {
            if (mBluetoothAdapter != null) {
                if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                } else {
                    Intent serverIntent = new Intent(mContext, DeviceListActivity.class);
                    startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                initLoadDialog();
            } else {
                ToastUtil.showShortToast(mContext, mContext.getResources().getString(R.string.quitLackPer));
                Exit();
            }
        } else if (requestCode == LOCATION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // 权限被用户同意。
                setPoistion();
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        || !ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    //提示用户前往设置界面自己打开权限
                    Toast.makeText(this, "请前往设置界面打开权限", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }

    }

    private Handler handlerMainVal = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    // 移除所有的msg.what为0等消息，保证只有一个循环消息队列再跑
                    handlerMainVal.removeMessages(0);
                    break;
                case 1:
                    // 直接移除，定时器停止
                    handlerMainVal.removeMessages(0);
                    break;
                default:
                    break;
            }
        }

        ;
    };

    private void Exittimer() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                EXITFLAG = false;
            }
        }, 2000);
    }

    private void Exit() {
        System.exit(0);
    }


    /**
     * 显示无网络的弹窗
     */
    public static void showNoNetWorkDlg(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.app_name)
                .setMessage(context.getResources().getString(R.string.newworkopen)).setPositiveButton(context.getResources().getString(R.string.setTing), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 跳转到系统的网络设置界面
                Intent intent = null;
                // 先判断当前系统版本
                if (android.os.Build.VERSION.SDK_INT > 10) {  // 3.0以上
                    intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                } else {
                    intent = new Intent();
                    intent.setClassName("com.android.settings", "com.android.settings.WirelessSettings");
                }
                context.startActivity(intent);

            }
        }).show();
    }

    private boolean isGpsAble(LocationManager lm) {
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ? true : false;
    }

    /**
     * 设置位置
     */
    private void setPoistion() {
        if (!ConnectionUtil.isConn(mContext)) {
            //Toast.makeText(this, "请打开网络~", Toast.LENGTH_SHORT).show();
            showNoNetWorkDlg(mContext);
            return;
        }
        if (!isGpsAble(lm)) {
            // Toast.makeText(this, "请打开GPS~", Toast.LENGTH_SHORT).show();
            openGPS2();
            return;
        }
        initLoadDialog();

    }

    @SuppressLint("MissingPermission")
    private boolean setAddress() {
        //从GPS获取最近的定位信息
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("BUG 获取最近的定位信息");
            return false;
        }
        try {

            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);//低精度，如果设置为高精度，依然获取不了location。
            criteria.setAltitudeRequired(false);//不要求海拔
            criteria.setBearingRequired(false);//不要求方位
            criteria.setCostAllowed(true);// 允许有花费
            criteria.setPowerRequirement(Criteria.POWER_LOW);//低功耗

            provider = lm.getBestProvider(criteria, true);
            if ("".equals(provider)) {
                Toast.makeText(mContext, "没有合适的提供者", Toast.LENGTH_SHORT).show();
            }
            Location lc = getLastKnownLocation();
            updateShow(lc);
            // System.out.println("BUG 更新23423信息吗" + provider+"他喵的"+lc);

            //  if(lc!=null){
            lm.requestLocationUpdates(provider, 2000, 0, locationListener);
            // }else{

            //  mOKhttpUtil.registerErrorLogInformation(mContext, "");
            //}

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    //打开设置页面让用户自己设置
    private void openGPS2() {
//        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//        startActivityForResult(intent, 0);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage((getResources().getString(R.string.newworkgpsss)))
                .setPositiveButton((getResources().getString(R.string.Sure)), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //跳转到手机打开GPS页面
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        //设置完成后返回原来的界面
                        startActivityForResult(intent, REQUEST_ADDRESS);
                    }
                }).show();
    }

    @SuppressLint("MissingPermission")
    private void updateShow(Location location) {
        //System.out.println("BUG location=" + location);
        if (location != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("当前的位置信息：\n");
            sb.append("经度：" + location.getLongitude() + "\n");
            sb.append("纬度：" + location.getLatitude() + "\n");
            sb.append("高度：" + location.getAltitude() + "\n");
            sb.append("速度：" + location.getSpeed() + "\n");
            sb.append("方向：" + location.getBearing() + "\n");
            sb.append("定位精度：" + location.getAccuracy() + "\n");
            System.out.println("BUG 这里获取到的值位" + sb.toString());
            lm.removeUpdates(locationListener);
            // MacCfg.addressName =  location.getLatitude()+","+location.getLongitude()+","+",";
            MacCfg.addressName = convertAddress(mContext, location.getLatitude(), location.getLongitude());
//            MacCfg.Latitude =  location.getLatitude();
//            MacCfg.Longitude =  location.getLongitude();


        }
        ;
    }

    /**
     * @param latitude  经度
     * @param longitude 纬度
     * @return 详细位置信息 GeoCoder是基于后台backend的服务，因此这个方法不是对每台设备都适用。
     * <p>
     * (这里非常消耗手机的内存)
     */
    public static String convertAddress(Context context, double latitude, double longitude) {
        Geocoder mGeocoder = new Geocoder(context, Locale.getDefault());
        System.out.println("BUG 进来了12312");
        StringBuilder mStringBuilder = new StringBuilder();
        try {
            List<Address> mAddresses = mGeocoder.getFromLocation(latitude, longitude, 1);
            if (!mAddresses.isEmpty()) {
                Address address = mAddresses.get(0);
                // MacCfg.addressName=address.getCountryCode() + address.getCountryName()+","+address.getAdminArea()+","+address.getLocality();
                mStringBuilder.append(address.getCountryCode() + address.getCountryName()).append(",")
                        .append(address.getAdminArea()).append(",").append(address.getLocality());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        MacCfg.addressName = mStringBuilder.toString();
        return mStringBuilder.toString();
    }

    /**
     * 80ACh显示的标题和图片跟其他型号的区别
     */
    private void titleShow() {
        img_main.setText(getResources().getString(R.string.Home));

    }

    /**
     * 80ACh显示的标题和图片跟其他型号的区别
     */
    private void backtitleShow() {
        img_main.setText(getResources().getString(R.string.Home));
        if (MacCfg.Mcu == 0) {
            img_main.setVisibility(View.VISIBLE);
            ly_delay_seting.setVisibility(View.VISIBLE);
            LY_Buttom.setVisibility(View.GONE);


        } else {
            img_main.setVisibility(View.VISIBLE);
            ly_delay_seting.setVisibility(View.GONE);
            LY_Buttom.setVisibility(View.VISIBLE);


        }
    }

    /**
     * 判断是要添加哪些Fragment
     */
    private void addFragment() {

        if (MacCfg.Mcu == 0) {
            fragmentTransaction
                    .add(R.id.id_framelayout, mDelayFRS_Draw)
                    .add(R.id.id_framelayout, mHome_simple)
                    .hide(mDelayFRS_Draw)
                    .commit();
        }  else {
            fragmentTransaction
                    .add(R.id.id_framelayout, mHome)
                    .add(R.id.id_framelayout, mDelayAuto)
                    .add(R.id.id_framelayout, mixerFragment)
                    .add(R.id.id_framelayout, mOutputFRS)
                    .add(R.id.id_framelayout, mEQ)
                    .hide(mDelayAuto)
                    .hide(mOutputFRS)
                    .hide(mixerFragment)
                    .hide(mEQ)
                    .commit();
        }


    }

    /**
     * 获取最后已知的位置
     */
    private Location getLastKnownLocation() {
        lm = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = lm.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            @SuppressLint("MissingPermission")
            Location l = lm.getLastKnownLocation(provider);
            //System.out.println("BUG 提供者=-=" + provider);
            if (l == null) {
                continue;
            }
            System.out.println("BUG 提供者" + provider);
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    /**
     * 界面重新加载进入这里
     */
    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 页面销毁进入之类
     */
    @Override
    public void onPause() {
        super.onPause();
    }


    /**
     * 移除所有的fragment
     */
    protected void removeAll() {
        if (CHS_Broad_Receiver != null) {
            unregisterReceiver(CHS_Broad_Receiver);
        }

        handlerMainVal.removeCallbacksAndMessages(null);
        //停止该服务
        Intent stopIntent = new Intent(mContext, ServiceOfCom.class);
        if (mContext != null) {
            if (connection != null) {
                mContext.stopService(stopIntent);
                mContext.unbindService(connection);
            }
        }

        @SuppressLint("RestrictedApi") List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null && fragments.size() > 0) {
            for (Fragment fragment : fragments) {
                fragmentTransaction.remove(fragment);
            }
        }
    }

    /**
     * 设置显示的联调文本
     */
    public void setLinkText() {
        int Dto = 0xff;
        if (MacCfg.bool_OutChLink) {
            ly_link.setVisibility(View.VISIBLE);
            for (int i = 0; i < MacCfg.ChannelLinkCnt; i++) {
                if (MacCfg.ChannelLinkBuf[i][0] == MacCfg.OutputChannelSel) {
                    Dto = MacCfg.ChannelLinkBuf[i][1];
                } else if (MacCfg.ChannelLinkBuf[i][1] == MacCfg.OutputChannelSel) {
                    Dto = MacCfg.ChannelLinkBuf[i][0];
                }
            }

            if (Dto < DataStruct.CurMacMode.Out.OUT_CH_MAX_USE) {
                if (MacCfg.OutputChannelSel > Dto) {
                    txt_ch0.setText(String.valueOf("CH" + (Dto + 1)));
                    txt_ch1.setText(String.valueOf("CH" + (MacCfg.OutputChannelSel + 1)));
                } else {
                    txt_ch0.setText(String.valueOf("CH" + (MacCfg.OutputChannelSel + 1)));
                    txt_ch1.setText(String.valueOf("CH" + (Dto + 1)));

                }
            } else {
                ly_link.setVisibility(View.GONE);
            }
        } else {
            ly_link.setVisibility(View.GONE);
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeAll();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (getVisibleFragment() == mDelayFRS_Draw || getVisibleFragment() == musicFragment) {
                backToMain();
            } else {
                finish();
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
