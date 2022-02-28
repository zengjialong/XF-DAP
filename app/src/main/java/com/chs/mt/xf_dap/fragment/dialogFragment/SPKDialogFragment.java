package com.chs.mt.xf_dap.fragment.dialogFragment;





import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.chs.mt.xf_dap.R;
import com.chs.mt.xf_dap.datastruct.DataStruct;
import com.chs.mt.xf_dap.datastruct.MacCfg;

public class SPKDialogFragment extends DialogFragment{
	//通道通道的互斥mutex 
	private int[] ChannelNumList = new int[32];
	private int[] ChannelNumBuf = new int[32];
	private int MaxSPK = 25;
	private int CH_Mutex[][]=new int[MaxSPK][32];
	private static final int EndFlag = 0xee;
    private int CH_Mutex0[]={0, EndFlag};//结束
    private int CH_Mutex1[]={1,4,6,10,12,EndFlag};//结束
	private int CH_Mutex2[]={2,4,5,6,10,11,12,EndFlag};
	private int CH_Mutex3[]={3,5,6,11,12,EndFlag};
	private int CH_Mutex4[]={1,2,4,5,6,7,8,11,12,EndFlag};
	private int CH_Mutex5[]={2,3,4,5,6,8,9,10,12,EndFlag};
	private int CH_Mutex6[]={1,2,3,4,5,6,7,8,9,10,11,EndFlag};
	private int CH_Mutex7[]={4,6,7,10,12,EndFlag};
	private int CH_Mutex8[]={4,5,6,8,10,11,12,EndFlag};
	private int CH_Mutex9[]={5,6,9,11,12,EndFlag};
	private int CH_Mutex10[]={1,2,5,6,7,8,10,11,12,EndFlag};
	private int CH_Mutex11[]={2,3,4,6,8,9,10,11,12,EndFlag};
	private int CH_Mutex12[]={1,2,3,4,5,7,8,9,10,11,12,EndFlag};
	private int CH_Mutex13[]={13,15,18,EndFlag};
	private int CH_Mutex14[]={14,15,18,EndFlag};
	private int CH_Mutex15[]={13,14,15,16,17,EndFlag};
	private int CH_Mutex16[]={15,16,18,EndFlag};
	private int CH_Mutex17[]={15,17,18,EndFlag};
	private int CH_Mutex18[]={13,14,16,17,18,EndFlag};
	private int CH_Mutex19[]={19,21,EndFlag};
	private int CH_Mutex20[]={20,21,EndFlag};
	private int CH_Mutex21[]={19,20,21,EndFlag};
	private int CH_Mutex22[]={22,24,EndFlag};
	private int CH_Mutex23[]={23,24,EndFlag};
	private int CH_Mutex24[]={22,23,24,EndFlag};

	private int DataOPT = 0;
	public static final String ST_DataOPT = "ST_DataOPT";
	private String SetMessage = "";
	public static final String ST_SetMessage = "ST_SetMessage";

	
	private TextView TVMsg;
	private Button   BtnOK,BtnCancel;
	private Button[] BtnSPK = new Button[MaxSPK];
	
	private SetOnClickDialogListener mSetOnClickDialogListener;
    public void OnSetOnClickDialogListener(SetOnClickDialogListener listener) {
        this.mSetOnClickDialogListener = listener;
    }
	public interface SetOnClickDialogListener{
		void onClickDialogListener(int type, boolean boolClick);
	}


	@Override
	public void onStart() {
		super.onStart();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

        DataOPT = getArguments().getInt(ST_DataOPT);
        //SetMessage = getArguments().getString(ST_SetMessage);

		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = inflater.inflate(R.layout.chs_dialog_spk, container,false);
		initData();
		initView(view);
		initStatus();

		return view;
	}

	private void initView(View view) {
		
		BtnSPK[0] = (Button)view.findViewById(R.id.id_spkb_0);
		BtnSPK[1] = (Button)view.findViewById(R.id.id_spkb_1);
		BtnSPK[2] = (Button)view.findViewById(R.id.id_spkb_2);
		BtnSPK[3] = (Button)view.findViewById(R.id.id_spkb_3);
		BtnSPK[4] = (Button)view.findViewById(R.id.id_spkb_4);
		BtnSPK[5] = (Button)view.findViewById(R.id.id_spkb_5);
		BtnSPK[6] = (Button)view.findViewById(R.id.id_spkb_6);
		BtnSPK[7] = (Button)view.findViewById(R.id.id_spkb_7);
		BtnSPK[8] = (Button)view.findViewById(R.id.id_spkb_8);
		BtnSPK[9] = (Button)view.findViewById(R.id.id_spkb_9);
		
		BtnSPK[10] = (Button)view.findViewById(R.id.id_spkb_10);
		BtnSPK[11] = (Button)view.findViewById(R.id.id_spkb_11);
		BtnSPK[12] = (Button)view.findViewById(R.id.id_spkb_12);
		BtnSPK[13] = (Button)view.findViewById(R.id.id_spkb_13);
		BtnSPK[14] = (Button)view.findViewById(R.id.id_spkb_14);
		BtnSPK[15] = (Button)view.findViewById(R.id.id_spkb_15);
		BtnSPK[16] = (Button)view.findViewById(R.id.id_spkb_16);
		BtnSPK[17] = (Button)view.findViewById(R.id.id_spkb_17);
		BtnSPK[18] = (Button)view.findViewById(R.id.id_spkb_18);
		BtnSPK[19] = (Button)view.findViewById(R.id.id_spkb_19);
		
		BtnSPK[20] = (Button)view.findViewById(R.id.id_spkb_20);
		BtnSPK[21] = (Button)view.findViewById(R.id.id_spkb_21);
		BtnSPK[22] = (Button)view.findViewById(R.id.id_spkb_22);
		BtnSPK[23] = (Button)view.findViewById(R.id.id_spkb_23);
		BtnSPK[24] = (Button)view.findViewById(R.id.id_spkb_24);

        BtnOK = (Button)view.findViewById(R.id.id_b_save);
        BtnCancel = (Button)view.findViewById(R.id.id_b_cancel);
		TVMsg = (TextView) view.findViewById(R.id.id_tv_msg);
        BtnOK.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				getDialog().cancel();
				if(mSetOnClickDialogListener != null){
					mSetOnClickDialogListener.onClickDialogListener(DataOPT, true);
				}

			}
		});

        BtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().cancel();
                if(mSetOnClickDialogListener != null){
                    mSetOnClickDialogListener.onClickDialogListener(DataOPT, false);
                }

            }
        });
        
        for(int i=0;i<MaxSPK;i++){
        	BtnSPK[i].setTag(i);
        	BtnSPK[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = (Integer)view.getTag();
                    if(ChannelNumList[index] ==EndFlag){
        				return;
        			}
                    initStatus();
                    setBtnPressWithIndex(index);
                    DataOPT = index;
                }
            });
        }
	}

	private void initStatus() {
		setBtnPress();
		for(int i=0;i<MaxSPK;i++){
			if(ChannelNumList[i] !=EndFlag){
				setBtnNornalWithIndex(ChannelNumList[i]);
			}
		}
	}
	private void setBtnDefault() {
		for(int i=0;i<MaxSPK;i++){
			BtnSPK[i].setBackgroundResource(R.drawable.spk_dialog_btn_normal);
			BtnSPK[i].setTextColor(getResources().getColor(R.color.spk_bg_btntext_normal));
		}
	}
	private void setBtnPress() {
		for(int i=0;i<MaxSPK;i++){
			BtnSPK[i].setBackgroundResource(R.drawable.spk_dialog_btn_press);
			BtnSPK[i].setTextColor(getResources().getColor(R.color.spk_bg_btntext_press));
		}
	}
	private void setBtnPressWithIndex(int index) {
		BtnSPK[index].setBackgroundResource(R.drawable.spk_dialog_btn_press);
		BtnSPK[index].setTextColor(getResources().getColor(R.color.spk_bg_btntext_press));
	}
	private void setBtnNornalWithIndex(int index) {
		BtnSPK[index].setBackgroundResource(R.drawable.spk_dialog_btn_normal);
		BtnSPK[index].setTextColor(getResources().getColor(R.color.spk_bg_btntext_normal));
	}
	
	
	
	/***
	 * 
	 * 
	 */
	private void initData(){
		for(int i=0;i<MaxSPK;i++){
	        for(int j=0;j<32;j++){
	            CH_Mutex[i][j]=EndFlag;
	        }
	    }
		
		int le=CH_Mutex0.length;
		for(int i=0;i<12;i++){
			if(i<le){
				CH_Mutex[0][i]=CH_Mutex0[i];
			}else{
				CH_Mutex[0][i]=EndFlag;
			}
			
		}
		le=CH_Mutex1.length;
		for(int i=0;i<12;i++){
			if(i<le){
				CH_Mutex[1][i]=CH_Mutex1[i];
			}else{
				CH_Mutex[1][i]=EndFlag;
			}
			
		}
		le=CH_Mutex2.length;
		for(int i=0;i<12;i++){
			if(i<le){
				CH_Mutex[2][i]=CH_Mutex2[i];
			}else{
				CH_Mutex[2][i]=EndFlag;
			}
			
		}
		le=CH_Mutex3.length;
		for(int i=0;i<12;i++){
			if(i<le){
				CH_Mutex[3][i]=CH_Mutex3[i];
			}else{
				CH_Mutex[3][i]=EndFlag;
			}
			
		}
		le=CH_Mutex4.length;
		for(int i=0;i<12;i++){
			if(i<le){
				CH_Mutex[4][i]=CH_Mutex4[i];
			}else{
				CH_Mutex[4][i]=EndFlag;
			}
			
		}
		le=CH_Mutex5.length;
		for(int i=0;i<12;i++){
			if(i<le){
				CH_Mutex[5][i]=CH_Mutex5[i];
			}else{
				CH_Mutex[5][i]=EndFlag;
			}
			
		}
		le=CH_Mutex6.length;
		for(int i=0;i<12;i++){
			if(i<le){
				CH_Mutex[6][i]=CH_Mutex6[i];
			}else{
				CH_Mutex[6][i]=EndFlag;
			}
			
		}
		le=CH_Mutex7.length;
		for(int i=0;i<12;i++){
			if(i<le){
				CH_Mutex[7][i]=CH_Mutex7[i];
			}else{
				CH_Mutex[7][i]=EndFlag;
			}
			
		}
		le=CH_Mutex8.length;
		for(int i=0;i<12;i++){
			if(i<le){
				CH_Mutex[8][i]=CH_Mutex8[i];
			}else{
				CH_Mutex[8][i]=EndFlag;
			}
			
		}
		le=CH_Mutex9.length;
		for(int i=0;i<12;i++){
			if(i<le){
				CH_Mutex[9][i]=CH_Mutex9[i];
			}else{
				CH_Mutex[9][i]=EndFlag;
			}
			
		}
		le=CH_Mutex10.length;
		for(int i=0;i<12;i++){
			if(i<le){
				CH_Mutex[10][i]=CH_Mutex10[i];
			}else{
				CH_Mutex[10][i]=EndFlag;
			}
			
		}

		le=CH_Mutex11.length;
		for(int i=0;i<12;i++){
			if(i<le){
				CH_Mutex[11][i]=CH_Mutex11[i];
			}else{
				CH_Mutex[11][i]=EndFlag;
			}
			
		}
		le=CH_Mutex12.length;
		for(int i=0;i<12;i++){
			if(i<le){
				CH_Mutex[12][i]=CH_Mutex12[i];
			}else{
				CH_Mutex[12][i]=EndFlag;
			}
			
		}
		le=CH_Mutex13.length;
		for(int i=0;i<12;i++){
			if(i<le){
				CH_Mutex[13][i]=CH_Mutex13[i];
			}else{
				CH_Mutex[13][i]=EndFlag;
			}
			
		}
		le=CH_Mutex14.length;
		for(int i=0;i<12;i++){
			if(i<le){
				CH_Mutex[14][i]=CH_Mutex14[i];
			}else{
				CH_Mutex[14][i]=EndFlag;
			}
			
		}
		le=CH_Mutex15.length;
		for(int i=0;i<12;i++){
			if(i<le){
				CH_Mutex[15][i]=CH_Mutex15[i];
			}else{
				CH_Mutex[15][i]=EndFlag;
			}
			
		}
		le=CH_Mutex16.length;
		for(int i=0;i<12;i++){
			if(i<le){
				CH_Mutex[16][i]=CH_Mutex16[i];
			}else{
				CH_Mutex[16][i]=EndFlag;
			}
			
		}
		le=CH_Mutex17.length;
		for(int i=0;i<12;i++){
			if(i<le){
				CH_Mutex[17][i]=CH_Mutex17[i];
			}else{
				CH_Mutex[17][i]=EndFlag;
			}
			
		}
		le=CH_Mutex18.length;
		for(int i=0;i<12;i++){
			if(i<le){
				CH_Mutex[18][i]=CH_Mutex18[i];
			}else{
				CH_Mutex[18][i]=EndFlag;
			}
			
		}
		le=CH_Mutex19.length;
		for(int i=0;i<12;i++){
			if(i<le){
				CH_Mutex[19][i]=CH_Mutex19[i];
			}else{
				CH_Mutex[19][i]=EndFlag;
			}
			
		}
		le=CH_Mutex20.length;
		for(int i=0;i<12;i++){
			if(i<le){
				CH_Mutex[20][i]=CH_Mutex20[i];
			}else{
				CH_Mutex[20][i]=EndFlag;
			}
			
		}
		le=CH_Mutex21.length;
		for(int i=0;i<12;i++){
			if(i<le){
				CH_Mutex[21][i]=CH_Mutex21[i];
			}else{
				CH_Mutex[21][i]=EndFlag;
			}
			
		}
		le=CH_Mutex22.length;
		for(int i=0;i<12;i++){
			if(i<le){
				CH_Mutex[22][i]=CH_Mutex22[i];
			}else{
				CH_Mutex[22][i]=EndFlag;
			}
			
		}
		le=CH_Mutex23.length;
		for(int i=0;i<12;i++){
			if(i<le){
				CH_Mutex[23][i]=CH_Mutex23[i];
			}else{
				CH_Mutex[23][i]=EndFlag;
			}			
		}
		le=CH_Mutex24.length;
		for(int i=0;i<12;i++){
			if(i<le){
				CH_Mutex[24][i]=CH_Mutex24[i];
			}else{
				CH_Mutex[24][i]=EndFlag;
			}			
		}
		CheckChannelNum();
	}
	
	
	//通过各通道名字编号检索可组成的新列表 MOTINLU TODO
	private void CheckChannelNum(){
	    for(int i=0;i<=MaxSPK;i++){
	        ChannelNumList[i]=i;
	    }
	    ChannelNumList[MaxSPK]=EndFlag;//结束 使用1-24
	    //NSLog(@"CheckChannelNum.ch=%d",output_channel_sel);
	    //更新列表
	    ChannelNumBuf[0]= DataStruct.RcvDeviceData.SYS.out1_spk_type;
	    ChannelNumBuf[1]= DataStruct.RcvDeviceData.SYS.out2_spk_type;
	    ChannelNumBuf[2]= DataStruct.RcvDeviceData.SYS.out3_spk_type;
	    ChannelNumBuf[3]= DataStruct.RcvDeviceData.SYS.out4_spk_type;
	    ChannelNumBuf[4]= DataStruct.RcvDeviceData.SYS.out5_spk_type;
	    ChannelNumBuf[5]= DataStruct.RcvDeviceData.SYS.out6_spk_type;
	    ChannelNumBuf[6]= DataStruct.RcvDeviceData.SYS.out7_spk_type;
	    ChannelNumBuf[7]= DataStruct.RcvDeviceData.SYS.out8_spk_type;
	    
	    ChannelNumBuf[8]  = DataStruct.RcvDeviceData.SYS.out9_spk_type;
	    ChannelNumBuf[9]  = DataStruct.RcvDeviceData.SYS.out10_spk_type;
	    ChannelNumBuf[10] = DataStruct.RcvDeviceData.SYS.out11_spk_type;
	    ChannelNumBuf[11] = DataStruct.RcvDeviceData.SYS.out12_spk_type;
	    ChannelNumBuf[12] = DataStruct.RcvDeviceData.SYS.out13_spk_type;
	    ChannelNumBuf[13] = DataStruct.RcvDeviceData.SYS.out14_spk_type;
	    ChannelNumBuf[14] = DataStruct.RcvDeviceData.SYS.out15_spk_type;
	    ChannelNumBuf[15] = DataStruct.RcvDeviceData.SYS.Proc_Amp_Mode;


		try {

			for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX; i++) {
				if ((i != MacCfg.OutputChannelSel) && (ChannelNumBuf[i] > 0)) {
					for (int j = 1; j < MaxSPK; j++) {
						for (int k = 0; k < MaxSPK; k++) {
							if (CH_Mutex[ChannelNumBuf[i]][k] == EndFlag) {
								continue;
							}
							if (ChannelNumList[j] == CH_Mutex[ChannelNumBuf[i]][k]) {
								ChannelNumList[j] = EndFlag;
							}
						}
					}
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	
	
	
	
	
	
	
	
	
	
}
