package com.chs.mt.xf_dap.datastruct;
//每个ID里包含有8个字节
public class DataStruct_Output {	
	//public	int	  EQ[][] = new int[31][5]; 
	public  DataStruct_EQ[] EQ = new DataStruct_EQ[Define.MAX_CHEQ];
	//id = 31		杂项
	public  int  mute;  //静音，0－－静音，1－－非静音
	public  int  polar; //极极，0－－同相，1－－反相
	public  int	 gain;  //输出总音量，连调，调试一个其他的通道都改变，只发送一个通道的数据//	增益植0~100%,stp:0.1,实际发送值：0~100
	public  int	 delay; //延时, 0~60Ms (0~475  stp:0.021ms   476~526 str:1ms) 发送数据范围 0~526
	public  int  eq_mode;		//EQ 模式 PEQ/GEQ
	public  int  spk_type;	  //所接喇叭类型 共25种 0~24表示   顺序按 无连接、前(左右)、中置、后(左右)、低音(左右)  注意：此值用系统结构中的喇叭类型代替，不随用户组数据改变
	//public  int  reduce_gain;	   //保留 低音通道的增益自动衰减或关闭此功能(只有OUTPUT6有此功能)，0~80dB，步进1dB
	//public  int  reduce_threshold; //保留 低音通道的增益自动衰减的启动门限，-20~-80dB，步进1dB  

	//高低通 ,ID = 32	(xover限MIC)
	public  int  h_freq;   //高通频率，20~20K，stp:1hz,发送实际频率值，如201HZ就发201
	public  int  h_filter; //高通类型值，0－－LR,1－－BESSEL,2－－BUTTERWORTH
	public  int  h_level;  //高通斜率值，0－－6db,1－－18db,2－－24db
	public  int  l_freq;   //低通频率	
	public  int  l_filter; //低通类型
	public  int  l_level;  //低通斜率值
	 
	// id = 33		混合比例
	public  int  IN1_Vol;  //音量  0%~100%
	public  int  IN2_Vol;  //音量
	public  int  IN3_Vol;  //音量
	public  int  IN4_Vol;  //音量
	public  int  IN5_Vol;   //保留
	public  int  IN6_Vol;    //保留
	public  int  IN7_Vol;    //保留
	public  int  IN8_Vol;    //保留
	
	// id = 34	        保留  
	public  int  IN9_Vol;	   //
	public  int	 IN10_Vol;	   //
	public  int	 IN11_Vol;	   //
	public  int  IN12_Vol;	   //
	public  int  IN13_Vol;	   //
	public  int  IN14_Vol;	   //	
	public  int  IN15_Vol;	   //
	public  int  IN16_Vol;	   //	
	//public  int  IN_polar; //极极，0－－同相，1－－反相，16位，15.14..3.2.1.0 对应IN4_Vol，IN3_Vol，IN2_Vol，IN1_Vol

	// id = 35		压限
	public  int  HP_OFF_Freq;     //高通斜率OFF,记忆高通频率
	public  int  LP_OFF_Freq;		//低通频率OFF,记忆低通频率
	public  int  Music22_Vol;	//保留
	public  int  Music23_Vol;	//保留
	public  int  HP_OFF_Flg;		//高通频率标记,0为0FF,1为高通斜率OFF,同时记忆高通频率
	public  int  LP_OFF_Flg;	//低通频率标记,0为0FF,1为高通斜率OFF,同时记忆高通频率

	//, ID = 36
	public  byte[] name = new byte[8];
	
	public DataStruct_Output(){
		for(int j=0;j<Define.MAX_CHEQ;j++){
			EQ[j]= new DataStruct_EQ();
		}
	}
}
