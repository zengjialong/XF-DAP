package com.chs.mt.xf_dap.bean;

public class DSP_SystemData {

	//SYSTEM��Ϣ	
	//pc_source_set
	public int[] pc_source_set = new int[8];
	//system_data
	public int[] system_data = new int[8];
	//system_spk_type
	public int[] system_spk_type = new int[16];
	//out_led
	public int[] out_led = new int[16];
	//sound_delay_field
	public int[] sound_delay_field = new int[50];
	//system_group_name 
	public int[] system_group_name = new int[16];
	//eff_group_name
	public int[] eff_group_name = new int[16];
	//cur_password_data
	public int[] cur_password_data = new int[8];

	///////////////////////////////////////////////////////////////////////
	//pc_source_set
	public void Set_pc_source_set(int[] initData){
		for(int i=0;i<8;i++){
			this.pc_source_set[i]=initData[i];
		}
	}
	public int[] Get_pc_source_set(){
		return pc_source_set;
	}
	//system_data
	public void Set_system_data(int[] initData){
		for(int i=0;i<8;i++){
			this.system_data[i]=initData[i];
		}
	}
	public int[] Get_system_data(){
		return system_data;
	}
	//system_spk_type
	public void Set_system_spk_type(int[] initData){
		for(int i=0;i<16;i++){
			this.system_spk_type[i]=initData[i];
		}
	}
	public int[] Get_system_spk_type(){
		return system_spk_type;
	}
	//out_led
	public void Set_out_led(int[] initData){
		for(int i=0;i<16;i++){
			this.out_led[i]=initData[i];
		}
	}
	public int[] Get_out_led(){
		return out_led;
	}
	//sound_delay_field
	public void Set_sound_delay_field(int[] initData){
		for(int i=0;i<50;i++){
			this.sound_delay_field[i]=initData[i];
		}
	}
	public int[] Get_sound_delay_field(){
		return sound_delay_field;
	}
	//system_group_name
	public void Set_system_group_name(int[] initData){
		for(int i=0;i<16;i++){
			this.system_group_name[i]=initData[i];
		}
	}
	public int[] Get_system_group_name(){
		return system_group_name;
	}
	//eff_group_name
	public void Set_eff_group_name(int[] initData){
		for(int i=0;i<16;i++){
			this.eff_group_name[i]=initData[i];
		}
	}
	public int[] Get_eff_group_name(){
		return eff_group_name;
	}
	//cur_password_data
	public void Set_cur_password_data(int[] initData){
		for(int i=0;i<8;i++){
			this.cur_password_data[i]=initData[i];
		}
	}
	public int[] Get_cur_password_data(){
		return cur_password_data;
	}

	public DSP_SystemData() {
		super();
		for(int i=0;i<8;i++){
			this.pc_source_set[i]=0;
			this.system_data[i]=0;
			
			this.cur_password_data[i]=0;
		}
		for(int i=0;i<16;i++){
			this.out_led[i]=0;
			this.system_group_name[i]=0;
			this.eff_group_name[i]=0;
			this.system_spk_type[i]=0;
		}
		for(int i=0;i<0;i++){
			this.sound_delay_field[i]=0;
		}
	}

	public DSP_SystemData(
			int[] pc_source_set,
			int[] system_data,
			int[] system_spk_type,
			int[] out_led,
			int[] sound_delay_field,
			int[] system_group_name,
			int[] eff_group_name,
			int[] cur_password_data
			) {
		super();
		this.pc_source_set=pc_source_set;
		this.system_data=system_data;
		this.system_spk_type=system_spk_type;
		this.out_led=out_led;
		this.sound_delay_field=sound_delay_field;
		this.system_group_name=system_group_name;
		this.eff_group_name=eff_group_name;
		this.cur_password_data=cur_password_data;
	}
}
