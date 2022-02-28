package com.chs.mt.xf_dap.datastruct;

public class Define {
	public static final int DEF_APP    = 1;
	public static final int DEF_LIB    = 2;
	public static int       DEFALIB    = DEF_APP;

	public static final int COMT_OFF     = 0;
	public static final int COMT_DSP     = 1;
	public static final int COMT_PLAY    = 2;
	public static final int COMT_DSPPLAY = COMT_DSP+COMT_PLAY;

	public static final int Statues_Null   = 0;
	public static final int Statues_NO     = 1;
	public static final int Statues_YES    = 2;
	//数据的通信通道
	public static final int COM_TYPE_SYSTEM   = 1;
	public static final int COM_TYPE_INPUT    = 2;
	public static final int COM_TYPE_OUTPUT   = 3;
	//机型定义
	public static final int MAC_TYPE_H812		= 0x00;
	public static final int MAC_TYPE_BW8S       = 0x01;
	public static final int MAC_TYPE_H680		= 0x02;
	public static final int MAC_TYPE_X1         = 0x03;
	public static final int MAC_TYPE_X2		    = 0x04;
	public static final int MAC_TYPE_Y1         = 0x05;
	public static final int MAC_TYPE_Y2         = 0x06;
	public static final int MAC_TYPE_D1         = 0x07;
	public static final int MAC_TYPE_D2         = 0x08;
	public static final int MAC_TYPE_YBD_NDS460 = 0x09;
	public static final int MAC_TYPE_USE = 0x0a;

	public static final int USB_Type		= 0;//与PC通信类型定义
	public static final int UART_Type		= 1;//与PC通信类型定义
	public static final int ZK_Type			= 2;//中控通信类型定义
	public static final int BT_Apple_Type	= 3;//CSR8670 与苹果蓝牙通信类型定义
	public static final int BT_Android_Type	= 4;//CSR8670 与安卓蓝牙通信类型定义
	public static final int BT_Conrol_Type	= 5;//DX-BT12 与安卓&苹果蓝牙通信类型定义
	public static final int BT_ATS2825_Type	= 6;//ATS2825 与安卓&苹果蓝牙通信类型定义



//	public  static  String BT_Paired_Name_DSP_HDS ="DSP HDs====";//匹配的蓝牙名称
//	public  static  String BT_Paired_Name_DSP_CCS ="DSP CCS";//匹配的蓝牙名称
//	public  static  String BT_Paired_Name_DSP_Play="DSP Play";//匹配的蓝牙名称

    public  static  String BT_Paired_Name_DSP_HD_ ="DSP HD-";//匹配的蓝牙名称
	public  static  String BT_Paired_Name_DSP_HDx ="DSP HDx";//匹配的蓝牙名称 6901蓝牙 下发ID必须变为6
	public  static  String BT_Paired_Name_DEQ_80CH ="DEQ-";//匹配的蓝牙名称 6901蓝牙 下发ID必须变为6   原本为DEQ-80ACH
	public  static  String BT_Paired_Name_Pioneer ="Pioneer";//匹配的蓝牙名称  下发ID必须变为6
	public  static  String BT_Paired_Name_DSP_Play ="DSP Play";//匹配的蓝牙名称
	public  static  String BT_Paired_Name_DSP_NAKAMICHI="DSP BT";//匹配的蓝牙名称
	public  static  String BT_Paired_Name_DSP_ZD ="DSP HD";//匹配的蓝牙名称

	public  static  String BT_Paired_Name_DSP_BD_NAKAMICHI ="Nakamichi";//匹配的蓝牙名称

	//联调
	public static final int LINKMODE_FRS        = 1; //前声场，后声场，超低的联调，单独分开
	public static final int LINKMODE_FRS_A      = 2; //前声场，后声场，超低的联调，一起联调
	public static final int LINKMODE_FR         = 3; //前声场，后声场，单独分开
	public static final int LINKMODE_FR_A       = 4; //前声场，后声场，中置超低的联调，全部一起联调
	public static final int LINKMODE_SPKTYPE    = 5; //设置通道输出类型后的联调
	public static final int LINKMODE_SPKTYPE_S  = 6; //设置通道输出类型后的联调，可联机保存
	public static final int LINKMODE_AUTO       = 7; //任意联调，每个通道可以单独联调，可联机保存
	public static final int LINKMODE_LEFTRIGHT  = 8; //固定两两通道联调
	public static final int LINKMODE_FR2A       = 9; //前声场，后声场，一起两两联调
	//延时
	public static final int DELAY_FRS        = 1; //前声场，后声场，超低
	public static final int DELAY_SPKTYPE    = 2; //设置通道输出类型
	public static final int DELAY_FIX        = 3; //固定



	public static final int H_Type=0;
	public static final int L_Type=1;

	public static final int MAC_MAX = 100;
    public static final int MAX_CH = 12;
	public static final int MAX_CHEQ = 31;
    public static final int MAX_GROUP = 15;
    public static final int MAX_UI_GROUP = 6;//UI 界面显示的用户组个数
    public static final int MAX_IN_EQ = 9;
	//联机通信方式	
	public static final int COMMUNICATION_WITH_WIFI = 0;//通信方式为wifi
	public static final int COMMUNICATION_WITH_BLUETOOTH_SPP = 1;//通信方式为蓝牙用SPP协议
	public static final int COMMUNICATION_WITH_BLUETOOTH_LE = 2;//通信方式为蓝牙用BLE协议
	public static final int COMMUNICATION_WITH_USB_HOST = 3;//通信方式为USB Host
	public static final int COMMUNICATION_WITH_UART = 4;//通信方式为串口
	public static final int COMMUNICATION_WITH_BLUETOOTH_SPP_TWO = 5;//通信方式为蓝牙用SPP协议,另一模块，BLE-SPP

	public static final int BLE_MaxT=20;

	//从BluetoothChatService发送处理程序的消息类型
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;

	public static final int UART_MaxT=32;
	//USB Host 配置
	public static final int USB_MaxT=64;
	public static final int USB_DSPHD_VID=1155; //0x0483
	public static final int USB_DSPHD_PID=22352;//0x5750
	public static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
	//WIFI IP
	public static String WIFI_IP_PORT ="10.10.100.254:8899";
	public static String WIFI_IP ="10.10.100.254";
	public static String WIFI_PORT =":8899";
	/*加密异或数值*/
	public static  int Encrypt_DATA = 0x83;
	public static  int EncryptionFlag = 0x21;
	public static  int DecipheringFlag = 0x20;

    //通道通道的互斥mutex
    public static int CH_Mutex[][]=new int[26][];
    public static final int EndFlag = 0xee;
    public static int CH_Mutex0[]={0, EndFlag};//结束
    public static int CH_Mutex1[]={1,4,6,10,12,EndFlag};//结束
    public static int CH_Mutex2[]={2,4,5,6,10,11,12,EndFlag};
    public static int CH_Mutex3[]={3,5,6,11,12,EndFlag};
    public static int CH_Mutex4[]={1,2,4,5,6,7,8,11,12,EndFlag};
    public static int CH_Mutex5[]={2,3,4,5,6,8,9,10,12,EndFlag};
    public static int CH_Mutex6[]={1,2,3,4,5,6,7,8,9,10,11,EndFlag};
    public static int CH_Mutex7[]={4,6,7,10,12,EndFlag};
    public static int CH_Mutex8[]={4,5,6,8,10,11,12,EndFlag};
    public static int CH_Mutex9[]={5,6,9,11,12,EndFlag};
    public static int CH_Mutex10[]={1,2,5,6,7,8,10,11,12,EndFlag};
    public static int CH_Mutex11[]={2,3,4,6,8,9,10,11,12,EndFlag};
    public static int CH_Mutex12[]={1,2,3,4,5,7,8,9,10,11,12,EndFlag};
    public static int CH_Mutex13[]={13,15,18,EndFlag};
    public static int CH_Mutex14[]={14,15,18,EndFlag};
    public static int CH_Mutex15[]={13,14,15,16,17,EndFlag};
    public static int CH_Mutex16[]={15,16,18,EndFlag};
    public static int CH_Mutex17[]={15,17,18,EndFlag};
    public static int CH_Mutex18[]={13,14,16,17,18,EndFlag};
    public static int CH_Mutex19[]={19,21,EndFlag};
    public static int CH_Mutex20[]={20,21,EndFlag};
    public static int CH_Mutex21[]={19,20,21,EndFlag};
    public static int CH_Mutex22[]={22,24,EndFlag};
    public static int CH_Mutex23[]={23,24,EndFlag};
    public static int CH_Mutex24[]={22,23,24,EndFlag};

	//---------------------------------机型Xover通道定义------------------------
	//高频
	public static final int HighFreq_HPFreq = 3400;
	public static final int HighFreq_LPFreq = 20000;

	public static int HighFreq[]={1,7,13,16,19,EndFlag};
	//中频
	public static final int MidFreq_HPFreq = 600;
	public static final int MidFreq_LPFreq = 1900;
	public static int MidFreq[]={2,8,EndFlag};
	//低频
	public static final int LowFreq_HPFreq = 70;
	public static final int LowFreq_LPFreq = 320;
	public static int LowFreq[]={3,9,14,17,20,EndFlag};
	//中高
	public static final int MidHighFreq_HPFreq = 600;
	public static final int MidHighFreq_LPFreq = 20000;
	public static int MidHighFreq[]={4,10,EndFlag};
	//中低
	public static final int MidLowFreq_HPFreq = 70;
	public static final int MidLowFreq_LPFreq = 1900;
	public static int MidLowFreq[]={5,11,EndFlag};
	//超低
	public static final int SupperLowFreq_HPFreq = 27;
	public static final int SupperLowFreq_LPFreq = 55;
	public static int SupperLowFreq[]={22,23,24,EndFlag};
	//前、中全频
	public static final int AllFreq_HPFreq = 70;
	public static final int AllFreq_LPFreq = 20000;
	public static int AllFreq[]={6,12,21,EndFlag};
	//全频
	public static final int R_AllFreq_HPFreq = 70;
	public static final int R_AllFreq_LPFreq = 20000;
	public static int R_AllFreq[]={15,18,EndFlag};
	//空
	public static final int NULLFreq_HPFreq = 20;
	public static final int NULLFreq_LPFreq = 20000;
	public static int NULLFreq[]={0,EndFlag};

	public static int ACH_Vol[]=new int[]{100,0,0,0,100,0,100,0,0,0,0,0,0,0,0,0};




	public  static final int EQ_FREQ[]={
		20, 25, 32, 40, 50, 63, 80, 100, 125, 160,
		200, 250, 315, 400, 500, 630, 800, 1000, 1250, 1600,
		2000, 2500, 3150, 4000, 5000, 6300, 8000, 10000, 12500, 16000, 20000};	
	public  static final int XOver_FREQ[]={//0-50
		20,23,27,32,37,42,49,57,66,76,
		88,102,118,140,162,187,216,250,289,334,
		375,420,486,561,648,749,866,1000,1123,1297,
		1498,1731,2000,2245,2594,2997,3462,4000,4757,5496,
		6350,7127,8000,9243,10679,12338,13849,15102,16000,17959,20000};	
	public  static final int EQ_Freq_MAX = 240;
	public  static final float FREQ241[]={//19.7
		20,(float)20.3,(float)20.9,(float)21.5,(float)22.1,(float)22.7,(float)23.4,(float)24.1,  
		(float)24.8,(float)25.5,(float)26.3,(float)27.0,(float)27.8,(float)28.7,(float)29.5,(float)30.4,  
		(float)31.3,(float)32.2,(float)33.1,(float)34.1,(float)35.1,(float)36.1,(float)37.2,(float)38.3,  
		(float)39.4,(float)40.5,(float)41.7,(float)42.9,(float)44.2,(float)45.5,(float)46.8,(float)48.2,  
		(float)49.6,(float)51.1,(float)52.6,(float)54.1,(float)55.7,(float)57.3,(float)59.0,(float)60.7,  
		(float)62.5,(float)64.3,(float)66.2,(float)68.2,(float)70.2,(float)72.2,(float)74.3,(float)76.5,  
		(float)78.7,(float)81.1,(float)83.4,(float)85.9,(float)88.4,(float)91.0,(float)93.6,(float)96.4,  
			(float)99.2,    102,    105,    108,    111,    115,    118,    121,  
			125,    129,    132,    136,    140,    144,    149,    153,  
			158,    162,    167,    172,    179,    182,    187,    193,  
			198,    204,    210,    216,    223,    229,    236,    243,  
			250,    257,    265,    273,    281,    289,    297,    306,  
			315,    324,    334,    344,    354,    364,    375,    386,  
			397,    409,    420,    433,    445,    459,    472,    486,  
			500,    515,    530,    545,    561,    578,    595,    612,  
			630,    648,    667,    687,    707,    728,    749,    771,  
			794,    817,    841,    866,    891,    917,    944,    972,  
			1000,   1030,   1060,   1090,   1123,   1155,   1190,   1224,  
			1260,   1297,   1335,   1374,   1414,   1456,   1498,   1542,  
			1587,   1634,   1682,   1731,   1782,   1834,   1888,   1943,  
			2000,   2059,   2119,   2181,   2245,   2311,   2378,   2448,  
			2520,   2594,   2670,   2748,   2828,   2911,   2997,   3084,  
			3175,   3268,   3364,   3462,   3564,   3668,   3776,   3886,  
			4000,   4117,   4238,   4362,   4490,   4621,   4757,   4896,  
			5000,   5187,   5339,   5496,   5657,   5823,   5993,   6169,  
			6350,   6536,   6727,   6924,   7127,   7336,   7551,   7772,  
			8000,   8234,   8476,   8724,   8980,   9243,   9514,   9792,  
			10079,  10374,  10679,  10992,  11314,  11645,  11987,  12338,  
			12699,  13071,  13454,  13849,  14254,  14672,  15102,  15545,  
			16000,  16469,  16951,  17448,  17959,  18486,  19027,  19585,
			20000
	};
	/*EQ MAX BW*/
	public  static final int EQ_BW_MAX = 295;
	public  static final float  EQ_BW[] ={//296
		    (float) 28.852,(float) 24.043,(float) 20.608,(float) 18.031,(float) 16.027,(float) 14.424,//6
			(float) 13.112,(float) 12.019,(float) 11.094,(float) 10.301,(float) 9.614,(float) 9.012,(float) 8.482,(float) 8.010,(float) 7.588,(float) 7.208,(float) 6.864,(float) 6.551,//12
			(float) 6.266,(float) 6.004,(float) 5.764,(float) 5.541,(float) 5.336,(float) 5.144,(float) 4.966,(float) 4.800,(float) 4.645,(float) 4.499,(float) 4.362,(float) 4.233,(float) 4.112,//13
			(float) 3.997,(float) 3.889,(float) 3.786,(float) 3.688,(float) 3.595,(float) 3.507,(float) 3.423,(float) 3.343,(float) 3.266,(float) 3.193,(float) 3.123,(float) 3.056,(float) 2.992,//13
			(float) 2.930,(float) 2.871,(float) 2.814,(float) 2.759,(float) 2.707,(float) 2.656,(float) 2.607,(float) 2.560,(float) 2.515,(float) 2.471,(float) 2.428,(float) 2.387,(float) 2.348,//13
			(float) 2.309,(float) 2.272,(float) 2.236,(float) 2.201,(float) 2.167,(float) 2.134,(float) 2.102,(float) 2.071,(float) 2.041,(float) 2.012,(float) 1.983,(float) 1.955,(float) 1.928,
			(float) 1.902,(float) 1.877,(float) 1.852,(float) 1.827,(float) 1.804,(float) 1.780,(float) 1.758,(float) 1.736,(float) 1.714,(float) 1.693,(float) 1.673,(float) 1.653,(float) 1.633,
			(float) 1.614,(float) 1.596,(float) 1.577,(float) 1.559,(float) 1.542,(float) 1.525,(float) 1.508,(float) 1.492,(float) 1.475,(float) 1.460,(float) 1.444,(float) 1.429,(float) 1.414,
			(float) 1.400,(float) 1.385,(float) 1.371,(float) 1.358,(float) 1.344,(float) 1.331,(float) 1.318,(float) 1.305,(float) 1.293,(float) 1.280,(float) 1.268,(float) 1.256,(float) 1.245,
			(float) 1.233,(float) 1.222,(float) 1.211,(float) 1.200,(float) 1.189,(float) 1.179,(float) 1.168,(float) 1.158,(float) 1.148,(float) 1.138,(float) 1.128,(float) 1.119,(float) 1.109,
			(float) 1.100,(float) 1.091,(float) 1.082,(float) 1.073,(float) 1.064,(float) 1.056,(float) 1.047,(float) 1.039,(float) 1.031,(float) 1.023,(float) 1.015,(float) 1.007,(float) 0.999,
			(float) 0.991,(float) 0.984,(float) 0.976,(float) 0.969,(float) 0.961,(float) 0.954,(float) 0.947,(float) 0.940,(float) 0.933,(float) 0.927,(float) 0.920,(float) 0.913,(float) 0.907,
			(float) 0.900,(float) 0.894,(float) 0.887,(float) 0.881,(float) 0.875,(float) 0.869,(float) 0.863,(float) 0.857,(float) 0.851,(float) 0.845,(float) 0.840,(float) 0.834,(float) 0.828,
			(float) 0.823,(float) 0.817,(float) 0.812,(float) 0.807,(float) 0.801,(float) 0.796,(float) 0.791,(float) 0.786,(float) 0.781,(float) 0.776,(float) 0.771,(float) 0.766,(float) 0.761,
			(float) 0.757,(float) 0.752,(float) 0.747,(float) 0.742,(float) 0.738,(float) 0.733,(float) 0.729,(float) 0.724,(float) 0.720,(float) 0.716,(float) 0.711,(float) 0.707,(float) 0.703,
			(float) 0.699,(float) 0.695,(float) 0.690,(float) 0.686,(float) 0.682,(float) 0.678,(float) 0.674,(float) 0.671,(float) 0.667,(float) 0.663,(float) 0.659,(float) 0.655,(float) 0.652,
			(float) 0.648,(float) 0.644,(float) 0.641,(float) 0.637,(float) 0.633,(float) 0.630,(float) 0.626,(float) 0.623,(float) 0.620,(float) 0.616,(float) 0.613,(float) 0.609,(float) 0.606,
			(float) 0.603,(float) 0.600,(float) 0.596,(float) 0.593,(float) 0.590,(float) 0.587,(float) 0.584,(float) 0.581,(float) 0.577,(float) 0.574,(float) 0.571,(float) 0.568,(float) 0.565,
			(float) 0.563,(float) 0.560,(float) 0.557,(float) 0.554,(float) 0.551,(float) 0.548,(float) 0.545,(float) 0.543,(float) 0.540,(float) 0.537,(float) 0.534,(float) 0.532,(float) 0.529,
			(float) 0.526,(float) 0.524,(float) 0.521,(float) 0.518,(float) 0.516,(float) 0.513,(float) 0.511,(float) 0.508,(float) 0.506,(float) 0.503,(float) 0.501,(float) 0.498,(float) 0.496,
			(float) 0.493,(float) 0.491,(float) 0.489,(float) 0.486,(float) 0.484,(float) 0.482,(float) 0.479,(float) 0.477,(float) 0.475,(float) 0.473,(float) 0.470,(float) 0.468,(float) 0.466,
			(float) 0.464,(float) 0.461,(float) 0.459,(float) 0.457,(float) 0.455,(float) 0.453,(float) 0.451,(float) 0.449,(float) 0.447,(float) 0.445,(float) 0.442,(float) 0.440,(float) 0.438,
			(float) 0.436,(float) 0.434,(float) 0.432,(float) 0.430,(float) 0.428,(float) 0.427,(float) 0.425,(float) 0.423,(float) 0.421,(float) 0.419,(float) 0.417,(float) 0.415,(float) 0.413,
			(float) 0.411,(float) 0.410,(float) 0.408,(float) 0.406,(float) 0.404
	};
	//------------------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------系统类型中的ID号定义-----------------------------------------------------------
	//------------------------------------------------------------------------------------------------------------------------------
	public static final int UI_PAGE_Master			= 0;
	public static final int UI_PAGE_HOME			= 1;
	public static final int UI_PAGE_DELAY			= 2;
	public static final int UI_PAGE_XOVER			= 3;
	public static final int UI_PAGE_OUTPUT			= 4;
	public static final int UI_PAGE_EQ				= 5;
	public static final int UI_PAGE_MUSICBOX		= 6;
	public static final int UI_PAGE_MIXER			= 7;
	public static final int UI_PAGE_Delay_DRAW				= 8;

	public static final int UI_HFilter			= 1;
	public static final int UI_HOct				= 2;
	public static final int UI_HFreq			= 3;
	public static final int UI_LFilter			= 4;
	public static final int UI_LOct				= 5;
	public static final int UI_LFreq			= 6;
	public static final int UI_OutVal			= 7;
	public static final int UI_OutMute	        = 8;
	public static final int UI_OutPolar			= 9;
	public static final int UI_OutEQ100			= 0x0a;
	public static final int UI_OutEQ1K			= 0x0b;
	public static final int UI_OutEQ10K			= 0x0c;
	public static final int UI_EQ_BW			= 0x0d;
	public static final int UI_EQ_Freq			= 0x0e;
	public static final int UI_EQ_Level		    = 0x0f;
	public static final int UI_EQ_G_P_MODE_EQ	= 0x10;
	public static final int UI_EQ_ALL	        = 0x11;
	public static final int UI_Mixer	        = 0x12;
	public static final int UI_Delay	        = 0x13;
	//-------------------------------------------自定义按键的状态-----------------------------------------------------------
	//在联调时使用时的状态  0：原始状态，1：准备加入联调组，2：本次操作已经加入联调组，3：本次操作前已经加入联调组
	public static final int Button_Link_Normal		 = 0;//0：原始状态
	public static final int Button_Link_WillLink	 = 1;//1：准备加入联调组
	public static final int Button_Link_Linking		 = 2;//2：本次操作已经加入联调组
	public static final int Button_Link_Linked		 = 3;//3：本次操作前已经加入联调组
	public static final int Button_Link_GNULL		 = 0xff;//3：本次操作前已经加入联调组
	
	
	//-------------------------------------------系统广播消息-----------------------------------------------------------
	public static final String BoardCast_Load_LocalJson="BoardCast_Load_LocalJson";
	public static final String BoardCast_SAVE_LOCAL_SEFF="BoardCast_SAVE_LOCAL_SEFF";
	public static final String BoardCast_EXIT="BoardCast_EXIT";
	public static final String BoardCast_INIT_BLUETOOTH_LE="BoardCast_INIT_BLUETOOTH_LE";
	public static final String BoardCast_SHARE_SEFF="BoardCast_SHARE_SEFF";
	public static final String BoardCast_SHARE_MAC_SEFF="BoardCast_SHARE_MAC_SEFF";
	public static final String BoardCast_FlashWebLoadPage="BoardCast_FlashWebLoadPage";
	public static final String BoardCast_EXIT_SEFFUploadPage="BoardCast_EXIT_SEFFUploadPage";
	public static final String BoardCast_LOAD_SEFF_FROM_OTHER="BoardCast_LOAD_SEFF_FROM_OTHER";
	public static final String BoardCast_BTConnectStatus="BoardCast_BTConnectStatus";
	public static final String BoardCast_BTConnect_SPP_LE="BoardCast_BTConnect_SPP_LE";
	public static final String BoardCast_BTConnect_SPP_OLD="BoardCast_BTConnect_SPP_OLD";
	public static final String BoardCast_FlashUI_ConnectStateOFMsg="BoardCast_FlashUI_ConnectStateOFMsg";
    public static final String BoardCast_FlashUI_ConnectState="BoardCast_FlashUI_ConnectState";
    public static final String BoardCast_FlashUI_AllPage="BoardCast_FlashUI_AllPage";
    public static final String BoardCast_FlashUI_DeviceVersionsErr="BoardCast_FlashUI_DeviceVersionsErr";
    public static final String BoardCast_FlashUI_InputSource="BoardCast_FlashUI_InputSource";
	public static final String BoardCast_FlashUI_ShowLoading="BoardCast_FlashUI_ShowLoading";
	public static final String BoardCast_FlashUI_IninLoadUI="BoardCast_FlashUI_IninLoadUI";
    public static final String BoardCast_FlashUI_ShowSucessMsg="BoardCast_FlashUI_ShowSucessMsg";
	public static final String BoardCast_FlashUI_SYSTEM_DATA="BoardCast_FlashUI_SYSTEM_DATA";
	public static final String BoardCast_OPT_DisonnectDeviceBT="BoardCast_OPT_DisonnectDeviceBT";
	public static final String BoardCast_FlashUI_MusicPage="BoardCast_FlashUI_MusicPage";
	public static final String BoardCast_Show_HeadCheadLoading="BoardCast_Show_HeadCheadLoading";
    public static final String BoardCast_FlashUI_CancelDSPDataLoading="BoardCast_FlashUI_CancelDSPDataLoading";
	public static final String BoardCast_ConnectToSomeoneDevice="BoardCast_ConnectToSomeoneDevice";
	public static final String BoardCast_FlashUI_FlashSoundStatus="BoardCast_FlashUI_FlashSoundStatus";
	public static final String BoardCast_FlashUI_TOOPENINTENT="BoardCast_FlashUI_TOOPENINTENT";
	public static final String BoardCast_FlashUI_TOOPENGPS="BoardCast_FlashUI_TOOPENGPS";
	public static final String BoardCast_FlashUI_ADDRESS="BoardCast_FlashUI_ADDRESS";

	public static final String BoardCast_FlashUI_ADDRESS_ERROR="BoardCast_FlashUI_ADDRESS_ERROR";
	public static final String BoardCast_FlashUI_ADDRESS_ERRORDialog="BoardCast_FlashUI_ADDRESS_ERRORDialog";

	public static final String BoardCast_FlashUI_EQ="BoardCast_FlashUI_EQ";
    public static final String BoardCast_FlashUI_ShowMsg="BoardCast_FlashUI_ShowMsg";
	public static final String BoardCast_FlashUI_CloseActivity="BoardCast_FlashUI_CloseActivity";

	public static final String BoardCast_FlashUI_InputSource_Val="BoardCast_FlashUI_InputSource_Val";
	public static final String BoardCast_StopService="BoardCast_StopService";
	public static final String BoardCast_ReadGroupFlashHome="BoardCast_ReadGroupFlashHome";

	public static final String BoardCast_FlashUI_ChangeUHostPlayIndex="BoardCast_FlashUI_ChangeUHostPlayIndex";
	//------------------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------系统类型中的ID号定义-----------------------------------------------------------
	//------------------------------------------------------------------------------------------------------------------------------
	public   static   final   int MICRO				= 1;
	public   static   final   int EFF				= 2;
	public   static   final   int MUSIC				= 3;
	public   static   final   int OUTPUT			= 4;
	public   static   final   int MICSET			= 5;
	public   static   final   int MUSICSET			= 6;
	public   static   final   int EFFSET			= 7;
	public   static   final   int MIC_MUSIC_EFF_VOL	= 8;
	public   static   final   int SYSTEM			= 9;
	public   static   final   int FEEDBACK			= 10;
	
	public   static   final   int  		GROUP_NAME	         = 0x00;	// 20组用户数据组名称
	public   static   final   int  		EFF_GROUP_NAME		 = 0x01;	// 单独的10组效果数据组名称
	public   static   final   int  		PC_SOURCE_SET		 = 0x02;	//输入源选择
	
	public   static   final   int  		LED_DATA             = 0x03;
	public   static   final   int  		SOFTWARE_VERSION	 = 0x04;	//设备版本号
	public   static   final   int  		SYSTEM_DATA	         = 0x05;
	public   static   final   int  		SYSTEM_SPK_TYPE	     = 0x06;
	public   static   final   int  		SYSTEM_SPK_TYPEB     = 0x07;
    public   static   final   int  		SYSTEM_Group         = 0x08;

	public   static   final   int  		SYSTEM_NINE         = 0x09;

	public   static   final   int  		LOGO_INFO	         = 0x10;
	public   static   final   int  		SYSTEM_INFO	         = 0x11;
	public   static   final   int  		CUR_PASSWORD_DATA	 = 0x12;
	public   static   final   int  		SUPER_PASSWORD_DATA	 = 0X13;
	public   static   final   int  		BACKLIGHT_INFO	     = 0x14;
	public   static   final   int  		DEVICE_ID_INFO	     = 0x15;
	public   static   final   int  		TEMPERATURE_INFO     = 0x16;
	//public   static   final   int  		CUR_PROGRAM_INFO = 0x17;

	public   static   final   int  		OUT_MAX_VAL_INFO	 = 0x20;
	public   static   final   int  		OUT_MIN_VAL_INFO	 = 0x21;
	public   static   final   int  		MIC_MAX_VAL_INFO	 = 0x22;
	public   static   final   int  		MIC_MIN_VAL_INFO	 = 0x23;
	public   static   final   int  		EFF_MAX_VAL_INFO	 = 0x24;
	public   static   final   int  		EFF_MIN_VAL_INFO	 = 0x25;
	public   static   final   int  		MUS_MAX_VAL_INFO	 = 0x26;
	public   static   final   int  		MUS_MIN_VAL_INFO	 = 0x27;

	public   static   final   int  		OUT_MIN_EN_INFO		 = 0x28;
	public   static   final   int  		MIC_MIN_EN_INFO	     = 0x29;
	public   static   final   int  		EFF_MIN_EN_INFO		 = 0x30;
	public   static   final   int  		MUS_MIN_EN_INFO	     = 0x31;
	public   static   final   int  	    CH6_LPF_HZ_INFO		 = 0x32;
	public   static   final   int  		Ch6_LPF_F_INFO	     = 0x33;

	public   static   final   int  		CUR_PROGRAM_INFO     = 0x34;
	public   static   final   int  		STARTUP_DATA_INFO    = 0x35;

	public   static   final   int  	    IAP_REQUEST_INFO     = 0x36; //APP端--申请进入IAP程序
	public   static   final   int  		IAP_START_INFO       = 0x37; //IAP端--升级开始(擦除FLASH)
	public   static   final   int  		IAP_PROGAM_INFO      = 0x38; //IAP端--FLASH数据包
	public   static   final   int  		IAP_END_INFO         = 0x39; //IAP端--升级结束包
	
	public   static   final   int  		IAP_DSP_REQUEST_INFO = 0x3a; //申请进入DSP的IAP程序
	public   static   final   int  		IAP_DSP_PROGAM_INFO  = 0x3b; //DSP的FLASH数据包
	public   static   final   int  		IAP_DSP_END_INFO     = 0x3c; //DSP的IAP升级结束包
	
	public   static   final   int  		SOUND_FIELD_INFO     = 0x40; //声场信息，代替现有数据中的延时(2字节)，等于是在系统中做4组6通道的延时，再加一个4组选择标志2字节，共50字节

	public   static   final   int  	    WIFI_RESET_INFO	     = 0x41;
	public   static   final   int  		WIFI_SETSSID_INFO	 = 0x42; //设置wifiSSID默认为最大为9个字符，输入字符不够PC在后面加零即可 2013-04-09
	
	public   static   final   int  	    SYSTEM_RESET_MCU     = 0x60;
	public   static   final   int  	    SYSTEM_TRANSMITTAL   = 0x61;
	public   static   final   int  	    SYSTEM_RESET_GROUP_DATA   = 0x62;
	
	public   static   final   int  	    MCU_BUSY	         = 0xCC;


	//-------------------------------------------   DataStruct_Iutput     -----------------------------------------------------------
	/*DataStruct_Iutput*/
	/* 音乐输入 每组8Byte EQ8X9+40=112*/                        /*0~8组EQ：2B-freq，2B-level，bw，shf_db，type*/
	public   static   final   int  	    INS_MISC_ID		= 0; /*杂项:feedback-反馈抑制 0~4,polar,mode,nute,delay,valume*/
	public   static   final   int  	    INS_XOVER_ID	= 1;/*高低通:h_freq,h_filter,h_level,l_freq,l_filter,l_level*/
	public   static   final   int  	    INS_ID_MAX	= 1;
	
	public   static   final   int  	    IN_MISC_ID		= 9; /*杂项:feedback-反馈抑制 0~4,polar,mode,nute,delay,valume*/
	public   static   final   int  	    IN_XOVER_ID		= 10;/*高低通:h_freq,h_filter,h_level,l_freq,l_filter,l_level*/
	public   static   final   int  	    IN_NOISEGATE_ID	= 11;/*噪声门:noisegate_t(akr),noise_config*/
	public   static   final   int  	    IN_LIMIT_ID		= 12;/*压限*/
	public   static   final   int  	    IN_NAME_ID		= 13;/*name[8]*/
	public   static   final   int  	    IN_ID_MAX		= 13;/**/  
	//-------------------------------------------   DataStruct_Output     -----------------------------------------------------------
	/*DataStruct_Output，每组8Byte EQ8X31+48=296*/                          /*0~31组EQ：2B-freq，2B-level，bw，shf_db，type*/
	public   static   final   int  	    OUT_MISC_ID		= 31;/*杂项：*/
	public   static   final   int  	    OUT_XOVER_ID	= 32;/*高低通(xover限MIC)*/
	public   static   final   int  	    OUT_Valume_ID	= 33;/*混合比例:IN1_Vol1 X 4,Source*/
	public   static   final   int  	    OUT_MIX_ID		= 34;/*保留*/
	public   static   final   int  	    OUT_LIMIT_ID	= 35;/*压限*/
	public   static   final   int  	    OUT_NAME_ID		= 36;/*name[8]*/
	public   static   final   int  	    OUT_ID_MAX		= 36;/**/
	//-------------------------------------------   DataStruct_EFFect     -----------------------------------------------------------
	/*DataStruct_EFFect 每组8Byte EQ8X8+32=96 */                           /*0~7组EQ：2B-freq，2B-level，bw，shf_db，type 左回声EQ*/
	public   static   final   int  	    EFF_Echo_ID		= 9; /*回声*/
	public   static   final   int  	    EFF_REV_ID		= 10;/*混响*/
	public   static   final   int  	    EFF_NAME_ID		= 11;/*name[8]*/
	public   static   final   int  	    EFF_ID_MAX		= 11;/**/
	
	/*名数据块的长度*/
    public   static   final   int  	    INS_LEN	    = (4*8+16)*11;
	public   static   final   int  	    INS_S_LEN	= 4*8+16;
	public   static   final   int  	    IN_LEN		= 9*8+40;    //  112; //0x70  DataStruct_Iutput:EQ8X9+40=112
	public   static   final   int  	    OUT_LEN		= 31*8+48;   //  296; //0xA8  DataStruct_Output:EQ8X31+48=296
	public   static   final   int  	    EFF_LEN		= MacCfg.EFF_CH_EQ_MAX*8+32;   //  96;  //0x60  DataStruct_EFFect:EQ8X8+32=96 
	public   static   final   int  	    SYSTEM_LEN	= 32;
	
	public   static   final   int  	    DATAID0x77	= 0x77;
	//------------------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------边界定义----------------------------------------------------------------
	//------------------------------------------------------------------------------------------------------------------------------
	public   static  	final   int       SENDBUFMAX = 100;
		
	//------------------------------------------------------------------------------------------------------------------------------	
	public static  final int ActivityResult_SET_CARMAC_TYPE        =  1;  
	public static  final int ActivityResult_SET_SEFF_IMG           =  2; 
	public static  final int ActivityResult_SET_SEFF_IMG_FROMCAM   =  3; 
	public static  final int ActivityResult_SET_SEFF_IMG_SAVE      =  4; 
	public static  final int ActivityResult_GET_LOGIN_USER         =  5;
	public static  final int ActivityResult_MusicPage_Back         =  6;
	public static  final int ActivityResult_MusicLrcPage_Back      =  7;
	//------------------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------软件授权使用商ID-------------------------------------------------------------
	//------------------------------------------------------------------------------------------------------------------------------	
	public static  final int AgentHead_BAF    = 0x6d;  //佰芙
	public static  final int AgentHead_AP     = 0x7c;  //阿尔派
	public static  final int AgentHead_HD     = 3;  //合德
	public static  final int AgentHead_HZHY   = 0x8e;  //惠州惠诺
	public static  final int AgentHead_YY     = 0x75;  //御音
	public static  final int AgentHead_RG     = 0x5a;  //锐高
	public static  final int AgentHead_DS     = 0x5A;  //迪声
	public static  final int AgentHead_SX     = 0x71;  //声鑫
	public static  final int AgentHead_PH     = 0x77;  //鹏辉
	public static  final int AgentHead_FL     = 0x72;  //芬朗
	public static  final int AgentHead_HL     = 0x73;  //汇隆
	public static  final int AgentHead_KL     = 0x6e;  //卡莱
	public static  final int AgentHead_YJ     = 0x5e;  //云晶
	public static  final int AgentHead_JB     = 0x6a;  //江波
	public static  final int AgentHead_JH     = 0x70;  //俊宏
	public static  final int AgentHead_KP     = 0x6b;  //酷派
	public static  final int AgentHead_YBD    = 0x78;  //盈必达
	public static  final int AgentHead_CHS    = 0x68;  //车厘子
	public static  final int AgentHead_RD     = 0x5c;  //荣鼎
	public static  final int AgentHead_YB     = 0x5E;  //译宝
	public static  final int AgentHead_LYL    = 0x6a;  //乐与路
	public static  final int AgentHead_GEM    = 0x83;  //歌尔玛
	public static  final int AgentHead_LM     = 0x7f;  //蓝脉
	public static  final int AgentHead_YG     = 0x7d;  //鹰歌
	public static  final int AgentHead_DR     = 0x81;  //东荣音响(公司)-东风（品牌）
	public static  final int AgentHead_TH     = 0x82;  //泰华电子
	public static  final int AgentHead_ZYZ    = 0x7e;  //古登
	public static  final int AgentHead_YYWC   = 0x84;  //音乐王朝
	public static  final int AgentHead_OF     = 0x8d;  //欧凡
	public static  final int AgentHead_LY     = 0x8f;  //欧运(公司)-路翼（品牌）-
	public static  final int AgentHead_XG     = 0x89;  //先歌
	public static  final int AgentHead_YDW    = 0x80;  //雅迪威
	public static  final int AgentHead_TJJ    = 0x90;  //铁将军
	public static  final int AgentHead_YYLM   = 0x85;  //音乐联盟
	public static  final int AgentHead_CL     = 0x6c;  //(宸临兄弟.潘)
	public static  final int AgentHead_HM     = 0x5b;  //
	public static  final int AgentHead_JKC    = 0x69;  //精科创
	public static  final int AgentHead_MAX    = 0x5d;  //
	public static  final int AgentHead_RS     = 0x70;  //睿勝
	public static  final int AgentHead_YX     = 0x6f;  //云响
	public static  final int AgentHead_LS     = 0x7a;  //
	public static  final int AgentHead_XD     = 0x86;  //向达
	public static  final int AgentHead_OM     = 0x7b;  //
	public static  final int AgentHead_HS     = 0x87;  //慧声
	public static  final int AgentHead_ZC     = 0x8a;  //卓成
	public static  final int AgentHead_CZL    = 0x8c;  //车智联
	public static  final int AgentHead_HW     = 0x91;  //惠威
	public static  final int AgentHead_CHY    = 0x40;  //车爵士

	public static  final int[]  DefHead={
			AgentHead_CHS     ,  //车厘子-12
			AgentHead_KP      ,  //酷派-
			AgentHead_YB      ,  //译宝-
            AgentHead_AP      ,  //阿尔派-
			AgentHead_YBD     ,  //盈必达-
			AgentHead_HL      ,  //汇隆-
			AgentHead_JH      ,  //俊宏-
			AgentHead_HZHY    ,  //惠州惠诺
			AgentHead_YDW     ,  //雅迪威
			AgentHead_TJJ     ,  //铁将军
			AgentHead_KL      ,  //卡莱
			AgentHead_FL      ,  //芬朗-
            AgentHead_CHY     ,  //汇友-
			AgentHead_YJ      ,  //云晶
			AgentHead_JB      ,  //江波-
			AgentHead_PH      ,  //鹏辉-
			AgentHead_LYL     ,  //乐与路
			AgentHead_ZYZ     ,  //古登
			AgentHead_BAF     ,  //佰芙
			AgentHead_XG      ,  //先歌
            AgentHead_GEM     ,  //歌尔玛
            AgentHead_LM      ,  //蓝脉
            AgentHead_YG      ,  //鹰歌
            AgentHead_DR      ,  //东荣音响(公司)-东风（品牌）
            AgentHead_TH      ,  //泰华电子
            AgentHead_YYWC    ,  //音乐王朝
            AgentHead_OF      ,  //欧凡
            AgentHead_LY      ,  //欧运(公司)-路翼（品牌）
            AgentHead_YYLM    ,  //音乐联盟
            AgentHead_CL      ,  //(宸临兄弟.潘)
            AgentHead_HM      ,  //
            AgentHead_JKC     ,  //精科创
            AgentHead_MAX     ,  //
            AgentHead_RS      ,  //睿勝
            AgentHead_YX      ,  //云响
            AgentHead_LS      ,  //
            AgentHead_XD      ,  //向达
            AgentHead_OM      ,  //
            AgentHead_HS      ,  //慧声
            AgentHead_ZC      ,  //卓成
            AgentHead_CZL     ,  //车智联
			AgentHead_HD      ,  //合德
			AgentHead_YY      ,  //御音
			AgentHead_RG      ,  //锐高
			AgentHead_DS      ,  //迪声
			AgentHead_SX      ,  //声鑫
			AgentHead_RD      ,  //荣鼎
			AgentHead_HW      ,  //惠威
			0x92,  //
			0x93,  //
			0x94,  //
			0x95,  //
			0x96,  //
			0x97,  //
			0x98,  //
			0x99,  //
			0x9a,  //
			0x9b,  //
			0x9c,  //
			0x9d,  //
			0x9e,  //
			0x9f,  //
			0xa0,  //
			0xa1,  //
			0xa2,  //
	};



	
	public static  final String AgentID_BAF    = "1";  //佰芙
	public static  final String AgentID_AP     = "2";  //阿尔派
	public static  final String AgentID_HD     = "3";  //合德
	public static  final String AgentID_HZHY   = "4";  //惠州惠诺
	public static  final String AgentID_YY     = "5";  //御音
	public static  final String AgentID_RG     = "6";  //锐高
	public static  final String AgentID_DS     = "7";  //迪声
	public static  final String AgentID_SX     = "8";  //声鑫
	public static  final String AgentID_PH     = "9";  //鹏辉
	public static  final String AgentID_FL     = "10";  //芬朗
	public static  final String AgentID_HL     = "11";  //汇隆
	public static  final String AgentID_KL     = "12";  //卡莱
	public static  final String AgentID_YJ     = "13";  //云晶
	public static  final String AgentID_JB     = "14";  //江波
	public static  final String AgentID_JH     = "15";  //俊宏
	public static  final String AgentID_KP     = "16";  //酷派
	public static  final String AgentID_YBD    = "53";  //盈必达
	public static  final String AgentID_CHS    = "18";  //车厘子
	public static  final String AgentID_RD     = "19";  //荣鼎
	public static  final String AgentID_YB     = "20";  //译宝
	public static  final String AgentID_LYL    = "21";  //乐与路
	public static  final String AgentID_GEM    = "22";  //歌尔玛
	public static  final String AgentID_LM     = "23";  //蓝脉
	public static  final String AgentID_YG     = "24";  //鹰歌
	public static  final String AgentID_DR     = "25";  //东荣音响
	public static  final String AgentID_TH     = "26";  //泰华电子
	public static  final String AgentID_ZYZ    = "27";  //古登
	public static  final String AgentID_YYWC   = "28";  //音乐王朝
	public static  final String AgentID_OF     = "29";  //欧凡
	public static  final String AgentID_DF     = "30";  //东风
	public static  final String AgentID_LY     = "31";  //路翼
	public static  final String AgentID_XG     = "32";  //先歌
	public static  final String AgentID_YDW    = "33";  //雅迪威
	public static  final String AgentID_TJJ    = "34";  //铁将军
	public static  final String AgentID_YYLM   = "35";  //音乐联盟


	public static  final byte SEFFFILE_EncryptByte=(byte) 0xee;//不使用
	public static  final boolean SEFFFILE_Encrypt=true;//使用加密
    public static  final String CIPHER_TEXT_SUFFIX = ".tmp";//解密后时时文件
	
	public static  final String ShareDefaultName="SEFF_share";
	public static  final String ShareDefaultGruopName="MacEFF_share";
	public static  final String USER_HeadImg="LoginHeadPic.jpeg";
	public static  final String USER_ForumImg="ForumPic.jpeg";
	public static  final String USER_SEFFImg="SEffUploadPic.jpeg";
	public static  final String USER_SEffModifyImg="SEffModifyPic.jpeg";
	public static  final String QRCodeImg="QRCodeImg.jpeg";
	//网络用户信息
	public static  final String CHS_URLVAR="http://121.41.9.33/";//公司服务器
	//public static  final String CHS_URLVAR="http://192.168.188.199/";//测试服务器
	public static  final String AddsessionId = "&sessionId=";
	public static  final String AddAgentIDsofttype = "&AgentID="+MacCfg.AgentID+"&softtype=2";
	public static  final String AddAgentIDsofttypeSessionId = "&AgentID="+MacCfg.AgentID+"&softtype=2"+AddsessionId;	
	public static  final String SeffFile_URLVAR = CHS_URLVAR+"chs_index.php?m=Home&a=getVenderList&AgentID="+MacCfg.AgentID+"&softtype=2";
	public static  final String CHS_MACCFG = ".jmac";//
	public static  final String CHS_SEff_TYPE = ".jssh";//
	public static  final String CHS_SEff_MAC_TYPE = ".jsah";//
	public static  final String CHS_SEff_SEARCH = ".jssh";//
	public static  final String CHS_SEff_MAC_SEARCH = ".jsah";//
	public static  final int    CHS_SEff_TYPE_L = 5;//
	public static  final String VerderOptionVersion =  "VerderOptionV9";
	public static  final String URL_VenderOption = "chs_index.php?m=Index&a=getVenderOption";
	public static  final String URL_ImageList = "chs_index.php?m=Index&a=GetHomeDatas";
	public static  final String URL_Forum=CHS_URLVAR+"chs_index.php?m=Forum&a=chs_index"+"&AgentID="+MacCfg.AgentID+"&softtype=2";
	
	public static  final String URL_UserInfoEdit = CHS_URLVAR+"chs_index.php?m=Home&a=editUser&AgentID="+MacCfg.AgentID+"&softtype=2";
	public static  final String URL_Register = CHS_URLVAR+"chs_index.php?m=Home&a=reg&AgentID="+MacCfg.AgentID+"&softtype=2";
	public static  final String URL_LoginForget = CHS_URLVAR+"chs_index.php?m=Home&a=forgetpwd&AgentID="+MacCfg.AgentID+"&softtype=2";
	public static  final String URL_Login = "chs_index.php?m=Public&a=doLogin";
	public static  final String URL_Logout = "chs_index.php?m=Public&a=logout";
	public static  final String URL_UpLoadImage = "chs_index.php?m=Public&a=uploadAvater";
	public static  final String URL_UpLoadSEFF = "chs_index.php?m=Index&a=postVendor";
	public static  final String URL_UpLoadSEFFA = CHS_URLVAR+"chs_index.php?m=Index&a=postVendor";
	public static  final String URL_Subscriptions = CHS_URLVAR+"chs_index.php?m=Home&a=getSubscribeList";
	public static  final String URL_Comment = CHS_URLVAR+"chs_index.php?m=Member&a=userManagementReview";
	public static  final String URL_BBS = CHS_URLVAR+"chs_index.php?m=Forum&a=myforum";
	public static  final String URL_SEFF = CHS_URLVAR+"chs_index.php?m=Member&a=myvender";
	public static  final String URL_Feedback = CHS_URLVAR+"chs_index.php?m=Member&a=AddMessage";
	public static  final String URL_Get_SEFF = CHS_URLVAR+"chs_index.php?m=Index&a=getVender";
	public static  final String OkHttpClient_URL_Get_SEFF="OkHttpClient_URL_Get_SEFF";
}
