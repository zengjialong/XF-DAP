package com.chs.mt.xf_dap.bean;

public class DSP_Data {
	private int[] group_name = new int[16];
	//DSP_MusicData
	private DSP_MusicData music=new DSP_MusicData();
	//DSP_OutputData
	private DSP_OutputData output=new DSP_OutputData();
	//system
	private DSP_SystemData system=new DSP_SystemData();
	
	public void Set_group_name(int[] group_name){
		this.group_name=group_name;
	}
	public int[] Get_group_name(){
		return this.group_name;
	}
	///////////////////////////////////////////////////////////////////////
	//DSP_MusicData
	public void Set_DSP_MusicData(DSP_MusicData st){
		this.music=st;
	}
	public DSP_MusicData Get_DSP_MusicData(){
		return music;
	}
	
	//DSP_OutputData
	public void Set_DSP_OutputData(DSP_OutputData st){
		this.output=st;
	}
	public DSP_OutputData Get_DSP_OutputData(){
		return output;
	}

	//system
	public void Set_SystemData(DSP_SystemData st){
		this.system=st;
	}
	public DSP_SystemData Get_SystemData(){
		return system;
	}
	
	public DSP_Data() {
		super();
		this.system=null;
		this.output=null;
		this.music=null;
		this.group_name=null;
	}
	
	public DSP_Data( DSP_OutputData output,DSP_MusicData music,DSP_SystemData system,int[] group_name) {
		super();
		this.system=system;
		this.output=output;
		this.music=music;
		this.group_name=group_name;
	}

 
}
