package com.chs.mt.xf_dap.datastruct;

public class DataStruct_System {	
	//PC_SOURCE_SET           
		public  int    input_source = 3;   //输入源(之前系统中的输入源)  0:高, 1:低, 2:AUX, 3:蓝牙, 4:光纤
		public  int    aux_mode =0 ;       //低电平模式 有3种 0:4个AUX   1:..................
		public  int    device_mode = 0x03; //本字节第二位0x02 代表有数字音源输入，字节第一位0x01代表有蓝牙输入，否则没有该模块，PC不能切换至此音源
		public  int    hi_mode     = 0x00;       //高电平音源等级20-60（40）
		public  int    blue_gain   = 0x00;       //蓝牙音源等级20-60（40）
		public  int    aux_gain    = 0x00;       //低电平音源等级20-60（40）
		public  int    DigitMod = 0x00;       //保留
		public  int    none5 = 0x00;       //保留
			
		//SYSTEM_DATA	
		public  int    main_vol = 60;      //输出总音量(之前输入结构中的总音量)  -60~0dB
		public  int    alldelay = 20;      //DSP纯延时 0~100 0.01s~1s
		public  int    noisegate_t = 0;    //噪声门  -120dbu~+10dbu,stp:1dbu,实际发送0~130   
		public  int    AutoSource = 0;     //自动音源开关  0关  1开
		public  int    AutoSourcedB = 0;   //自动音源检测的信号阀值 -120dB~0dB
		public  int    MainvolMuteFlg = 1; //静音临时标志，这个标志关机不保存，注意特别处理	
		public  int    none6 = 0x00;       //保留
		
		//SYSTEM_SPK_TYPEA
		public  int    out1_spk_type;	  //所接喇叭类型 共25种 0~24表示   顺序按 无连接、前(左右)、中置、后(左右)、低音(左右)
		public  int    out2_spk_type;	  //所接喇叭类型 共25种 0~24表示   顺序按 无连接、前(左右)、中置、后(左右)、低音(左右)
		public  int    out3_spk_type;	  //所接喇叭类型 共25种 0~24表示   顺序按 无连接、前(左右)、中置、后(左右)、低音(左右)
		public  int    out4_spk_type;	  //所接喇叭类型 共25种 0~24表示   顺序按 无连接、前(左右)、中置、后(左右)、低音(左右)
		public  int    out5_spk_type;	  //所接喇叭类型 共25种 0~24表示   顺序按 无连接、前(左右)、中置、后(左右)、低音(左右)
		public  int    out6_spk_type;	  //所接喇叭类型 共25种 0~24表示   顺序按 无连接、前(左右)、中置、后(左右)、低音(左右)
		public  int    out7_spk_type;	  //所接喇叭类型 共25种 0~24表示   顺序按 无连接、前(左右)、中置、后(左右)、低音(左右)
		public  int    out8_spk_type;	  //所接喇叭类型 共25种 0~24表示   顺序按 无连接、前(左右)、中置、后(左右)、低音(左右)

		//SYSTEM_SPK_TYPEB
		public  int    out9_spk_type;	  //所接喇叭类型 共25种 0~24表示   顺序按 无连接、前(左右)、中置、后(左右)、低音(左右)
		public  int    out10_spk_type;	  //所接喇叭类型 共25种 0~24表示   顺序按 无连接、前(左右)、中置、后(左右)、低音(左右)
		public  int    out11_spk_type;	  //所接喇叭类型 共25种 0~24表示   顺序按 无连接、前(左右)、中置、后(左右)、低音(左右)
		public  int    out12_spk_type;	  //所接喇叭类型 共25种 0~24表示   顺序按 无连接、前(左右)、中置、后(左右)、低音(左右)
		public  int    out13_spk_type;	  //所接喇叭类型 共25种 0~24表示   顺序按 无连接、前(左右)、中置、后(左右)、低音(左右)
		public  int    out14_spk_type;	  //所接喇叭类型 共25种 0~24表示   顺序按 无连接、前(左右)、中置、后(左右)、低音(左右)
		public  int    out15_spk_type;	  //所接喇叭类型 共25种 0~24表示   顺序按 无连接、前(左右)、中置、后(左右)、低音(左右)
		public  int    Proc_Amp_Mode;	 //0为功放+处理器模式,1为处理器模式

		//SYSTEM_Group
		public  int    none1;
		public  int    Default_sound_source;
		public  int    mode;	   //1--桥接模式  0-立体模式
		public  int[]    none=new int[5];	  //所接喇叭类型 共25种 0~24表示   顺序按 无连接、前(左右)、中置、后(左右)、低音(左右)
		/**
		 * 0x09*/
		public  int[]    none9=new int[5];	  //模拟喇叭类型
		public  int Hardware;//硬件手调开关


	//led 0-13
		public  int[]    out_led = new int[15];
		//复位MUC, 恢复出厂设置，随机数街产生
		public  static byte[] RESET_MCU = new byte[8];
		//读取出错数据，请求数据恢复
		public  static byte[] RESET_GROUP_DATA = new byte[8];
		//读取出错数据，请求数据恢复
		public  static byte[] GROUP = new byte[8];
		/*
		数据第0个字节为数据操作标志
		数据第1个字节为要操作的组ID 现在没用上（备用）
		其余补0即可
		1是标志正在操作 
		0为操作完成
		*/


		public  static byte[] TRANSMITTAL = new byte[8];
		//组名称        
		public  int[][]   UserGroup = new int[Define.MAX_GROUP+1][16];
		public  int SoundDelayField[] = new int[50];
}
