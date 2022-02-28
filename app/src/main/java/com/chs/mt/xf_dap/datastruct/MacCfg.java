package com.chs.mt.xf_dap.datastruct;

import android.bluetooth.BluetoothDevice;

import java.util.ArrayList;

public class MacCfg {
	/**
	 */
	public static  int HEAD_DATA = 0x78;	            // 包头
	public static  String MCU_Versions="MPXF-AV1.0";//下位机版本号    460  AV1.0
	public static  String MCU_Version="MPXF-BV1.0";//下位机版本号   680
	public static  String MCU_Version_8="MPXF-CV1.0";//下位机版本号   80-ACH

	public static  String BRAND = "XF";

	public static   String App_version="APXF-AV1.05(Release)";//定义本软件版本
	public static   String App_versions="APXF-BV1.05(Release)";//定义本软件版本
	public static   String App_version_8="APXF-CV1.09(DEQ-80ACH)(beta)";//定义本软件版本
	public static  final String Copyright="Copyright © 2020 Pioneer Corporation.All rights reserved.";//定义本版权
	public static  final String welcome_logo="APBD-AV1.00";
	public static  final String Mac="Sound Pro";


	public static int ButtomItemMaxUse = 5;

	//选择不同的整机文件加载
	public static  final String Assets_path1 = "/storage/emulated/0/Android/data/com.chs.mt.xf_dap460/files/standard.80ACH_SINGLE";
	public static  final String Assets_path2 = "/storage/emulated/0/Android/data/com.chs.mt.xf_dap460/files/diyin.80ACH_SINGLE";
	public static  final String Assets_path3 = "/storage/emulated/0/Android/data/com.chs.mt.xf_dap460/files/gudian.80ACH_SINGLE";
	public static  final String Assets_path4 = "/storage/emulated/0/Android/data/com.chs.mt.xf_dap460/files/jueshi.80ACH_SINGLE";
	public static  final String Assets_path5 = "/storage/emulated/0/Android/data/com.chs.mt.xf_dap460/files/liuxing.80ACH_SINGLE";
	public static  final String Assets_path6 = "/storage/emulated/0/Android/data/com.chs.mt.xf_dap460/files/yaogun.80ACH_SINGLE";

	public static int CurrentID=0;
	public static boolean isBOOL_MusicList=false;

	public static  final String AgentNAME = BRAND;  //译宝
	public static  final String Json_versions="CHS-JSON_V1.00";//定义本软件版本
	public static  final String Json_versions_V0_00="CHS-JSON_V0.00";//定义本软件版本 
	public static  final String Json_MacCfgVersions="CHS-JSONMacCfg_V0.00d";//定义本软件版本
	public static  final boolean Lock_EQ[]=new boolean[12];
	public static int OutMax=12;
	public static int Mcu=2;   //1为100CH 2 为500ACH   3为80ACH
	public static int UserGroup=16;

	public static int ModelMax=2;//模式切换有几种模式

	public static int ReadGroup=16;


	public static int MixMax=16;
	public static  final String AgentID        = Define.AgentID_YBD;
	public static  final int    Agent_ID       = 17;
	public static        String PhoneMAC = "";
    public static        String PhoneOS = "";
	public static        String PhoneOS_Mode = "";
    public static        String PhoneName = "";
	public static  final   int Define_MAC_MAX = 10;
	public static int Define_MAC = Define.MAC_TYPE_YBD_NDS460;
	//联机通信方式	
	public static       boolean BOOL_ANYCON = false;
	public static         int COMMUNICATION_MODE = Define.COMMUNICATION_WITH_BLUETOOTH_SPP;
	public static         Boolean BOOL_USE_MusicBox=false;
	//USB Host
	public static boolean USBConnected=false; 
	//变量
	public static boolean DATAERROR=false;
	public static  int HEAD_DATA_Index = 0;
    public static boolean bool_Encryption=false;//加密标志
	public static byte Encryption_PasswordBuf[]={0,0,0,0,0,0};//密码保存
	public static boolean bool_HaveSEFFUpdate = false;//false:没有数据更新，ture:有数据更新。
	public static String LOAD_SEFF_FROM_OTHER_PathName = null;//false:没有数据更新，ture:有数据更新。
	public static boolean first_con = false;
	public static final float[] SCar = {(float) 1.3, (float) 1.15 }; //(float) 1.5, (float) 1.35
	public static final float[] MCar = {(float) 1.6, (float) 1.58};
	public static final float[] LCar = {(float) 1.75,(float) 1.75};
	public static       float[] CCar = SCar;
	public  static  boolean bool_OutChLink = false;

	public  static  boolean bool_AllTwoLinkCopyType=false;//false:left to right,true:right to left
	public  static  boolean bool_AllTwoLinkStatus = false;
	public static String addressName="";
	public static Double Latitude=0.0,Longitude=0.0;
	
	/* 按键长按时间  */	
	public static  int LongClickEventTimeMax = 100;
	/* 设置输出通道联调标志  */
    public  static int ChannelConOPT=0;
	public  static int ChannelConFLR=0;
	public  static int ChannelConRLR=0;
	public  static int ChannelConSLR=0;
	public  static int ChannelConCS=0;
	public  static int LinkChannleBase=0;
	public  static int ChannelConClick=0;
	public  static int MaxOupputNameLinkGroup=16;
	public  static  int ChannelLinkCnt = 0;
	public  static  int ChannelLinkBuf[][]=new int [16][2];
    public  static  int[] ChannelNumList = new int[26];
	public  static  int ChannelNumBuf[]={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
	public  static  boolean bool_OutChLeftRight=true;//true:从左复制到右，false:从右复制到左
	public  static  boolean bool_OutChLock = false;//当联调的时候  锁定
	public  static  boolean bool_LinkFlag = false;//联调标志


	public 	static     int  input_source_tmp =100;
	public 	static     int  SourceDefine_tmp = 100;
	public 	static     int  High_Vol_tmp = 100;
	public 	static     int  Opt_Vol_Vol_tmp = 100;
	public 	static     int  Blue_Vol_tmp = 100;
	public 	static     int  Aux_Vol_tmp = 100;
	public 	static     int  Cox_Vol_tmp = 100;
	public 	static     int  Player_vol_tmp = 100;


	public 	static     int  main_vol_tmp =1;
	public 	static     int  alldelay_tmp = 1;
	public 	static     int  noisegate_t_tmp = 1;
	public 	static     int   AutoSource_tmp = 1;
	public 	static     int  AutoSourcedB_tmp = 1;
	public 	static     int  MainvolMuteFlg_tmp = 1;
	public 	static     int  none6_tmp = 1;
	public 	static     int  MainvolMuteFlg_tmp_click=0;

	public static int setDelay_Val[]=new int[6];

	public static int AutoLinkGMax = 6; //自定义联调的通道数目
	public static int[][] LinkGroupMem = new int[AutoLinkGMax + 1][AutoLinkGMax]; //当前操作组的当前通道
	public static int OCLHadLinkBuf[] = new int[AutoLinkGMax];//获取联调通道


	public static int [] OutputVal = new int[16];

	/*EFF*/
	public  static final int EFF_CH_EQ_MAX=8;
	//TOING SUB
	public  static final int ToningBW =295-72;
	public 	static     int  input_sourcetemp = 3; //信号灯包接收到的音源
	public 	static     int  CurProID = 0; //当前程序ID

	public  static  String BT_CUR_ConnectedName="DSP";//匹配的蓝牙名称
	public  static  String BT_CUR_ConnectedID = "NULL";//匹配的蓝牙名称
    public  static  String BT_OLD_ConnectedName="DSP HD";//匹配的蓝牙名称
	public  static  String BT_OLD_ConnectedID="DSP HD";//匹配的蓝牙名称
	public  static  String BT_ConnectedID = "NULL";//匹配的蓝牙名称
	public  static  String BT_ConnectedName = "NULL";//匹配的蓝牙名称
	public  static  String BT_GetName = "NULL";//匹配的蓝牙名称
	public  static  String BT_GetID = "NULL";//匹配的蓝牙名称

	public 	static  int  BluetoothDeviceID = Define.BT_Android_Type; //当前蓝牙ID
    //已经连接的蓝牙列表
	public  static ArrayList<BluetoothDevice> LCBT = new ArrayList<BluetoothDevice>();
	public  static  boolean CHS_BT_CONNECTED = false; //已经连接上DSH HD的蓝牙设备
    public  static  boolean BTManualConnect = false;//是否手动关闭
	public  static  boolean BOOL_Login=true;
	public  static     int  UI_Type	    = 0;
	public 	static     int  IMM_Hight = 1;
	
	public  static  boolean bool_ReadMacGroup = false; 
	public  static  final int EndFlag = 0xee;
	public  static  int OutputChannelSel = 0;
	public  static  int inputChannelSel = 0;
	public  static  int EQ_Num = 0;
	
	public  static  final int SPK_MAX = 28;

	public  static  final String DEVICE_NAME = "device_name";
	public  static  final String TOAST = "toast";
	public  static String DeviceVerString = null;     // 设备版本信息
	public  static  int CurPage = 0;
    public  static  int LedPackageCnt = 0;
	public  static  boolean UpdataAduanceData = false;
	public  static  int delayms = 200;
	public  static boolean BOOL_DialogHideBG=true;
	public  static  boolean BOOL_LoadSeffMute = false;
	public  static  boolean BOOL_OPTClose = false;
	public  static  boolean BOOL_OPTOpen = false;
	public  static  int HEAD[]={0,0,0};
	public  static  boolean BOOL_SoundStatues = false;
	public  static int cntDSP=0;
	public  static boolean BOOL_FlashMusicList=true;
	public  static boolean BOOL_FirstStart=false;
}
