package com.chs.mt.xf_dap.fragment.dialogFragment;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chs.mt.xf_dap.R;
import com.chs.mt.xf_dap.datastruct.DataStruct;

import java.io.UnsupportedEncodingException;


@SuppressLint({ "NewApi", "ClickableViewAccessibility" })
public class UserGOPT_Save_DialogFragment extends DialogFragment {
	private Toast mToast;
	private static Context mContext;	

	private Button BtnCancel,BtnSave;
	private EditText ET_UGNane;

	private int UserGroup = 0;
	public static final String ST_UserGroup = "UserGroup";
	
 	private OnUserGroupDialogFragmentClickListener mUserGroupListener;
    public void OnSetUserGroupDialogFragmentChangeListener(OnUserGroupDialogFragmentClickListener listener) {
        this.mUserGroupListener = listener;
    }

    public interface OnUserGroupDialogFragmentClickListener{
        void onUserGroupSaveListener(int Index, boolean UserGroupflag);
//        void onUserGroupRecallListener(int Index, boolean UserGroupflag);
//        void onUserGroupDeleteListener(int Index, boolean UserGroupflag);
    }

	@Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
        Bundle savedInstanceState) {  
		mContext = getActivity().getApplicationContext();	
		UserGroup = getArguments().getInt(ST_UserGroup);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		//getDialog().setTitle(R.string.SoundOpt);
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.chs_user_gopt_save_dialog, container,false);
        initView(view);
		initData();
		initClick();
        return view;  
    }  
	private void initView(View V_UserGSel_Dialog) {
		BtnCancel = (Button) V_UserGSel_Dialog.findViewById(R.id.id_b_cancle);
		BtnSave = (Button) V_UserGSel_Dialog.findViewById(R.id.id_b_save);
        ET_UGNane = (EditText) V_UserGSel_Dialog.findViewById(R.id.id_et_username_edit);
//        if(MacCfg.Mcu!=7){
//			ET_UGNane.setInputType(InputType.TYPE_CLASS_TEXT);
//		}else{
//			final String regular = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
//			ET_UGNane.setKeyListener(new DigitsKeyListener() {
//				@Override
//				public int getInputType() {
//					return InputType.TYPE_TEXT_VARIATION_PASSWORD;
//				}
//
//				@Override
//				protected char[] getAcceptedChars() {
//					char[] ac = regular.toCharArray();
//					return ac;
//				}
//			});
//
//		}

        if(checkUserGroupByteNull(DataStruct.RcvDeviceData.SYS.UserGroup[UserGroup])){
            ET_UGNane.setText(getGBKString(DataStruct.RcvDeviceData.SYS.UserGroup[UserGroup]));
        }else{
            ET_UGNane.setText("");
        }
	}

	private void initData() {
		
	}

	private void initClick() {
		BtnSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(CheckGroupName(0)){
					SetUserGroupName(UserGroup);
					if(mUserGroupListener != null){
						mUserGroupListener.onUserGroupSaveListener(0, true);
					}
					getDialog().cancel();
				}else{
					ToastMsg(getResources().getString(R.string.SetGroupName));
				}
			}
		});

		BtnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getDialog().cancel();
			}
		});

	}
	//////////////////////////////////////////////
	/**
	* 消息提示
	*/
	private void ToastMsg(String Msg) {
		if (null != mToast) {
			mToast.setText(Msg);
		} else {
			mToast = Toast.makeText(mContext, Msg, Toast.LENGTH_SHORT);
		}
		mToast.show();
	}
	/**
     * 检测用户输入的名字，check 0:主界面，1：保存对话框
     */
    boolean CheckGroupName(int check){
    	boolean b_ret=false;
    	String gname="";
		if(check==0){
			gname=String.valueOf(ET_UGNane.getText());
		}else if(check==1){
			//gname=String.valueOf(ET_StoreSel.getText());
		} 
    	
		if(gname.length()>=1){
    		for(int i=0;i<gname.length();i++){
				if(gname.charAt(i)>=0x21){
					b_ret=true;
	    		}
	    	}
    	}else{
    		b_ret = false;
    	}
		return b_ret;
    }
    
    private String getGBKString(int[] nameC){
    	byte[] GBK=new byte[16];
    	for(int j=0;j<16;j++){
    		GBK[j]=0x00;				
		}
    	int n=0;
		String uNameString = null;
		for(int j=0;j<13;j++){
			if(nameC[j] != 0x00){
				GBK[j]=(byte) nameC[j];
				++n;
			}				
		}
		try {
			byte[] GBKN=new byte[n];
			for(int j=0;j<n;j++){
				GBKN[j]=GBK[j];
			}
			uNameString = new String(GBKN, "GBK");
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		}
    	
		return uNameString;   	
    }
    
    private boolean checkUserGroupByteNull(int[] ug){
    	
    	for(int i=0;i<15;i++){
    		if(ug[i]!=0x00){
    			return true;
    		}
    	}
    	return false;
    }
    
    void ShowEditGroupName(){
    	if(checkUserGroupByteNull(DataStruct.RcvDeviceData.SYS.UserGroup[UserGroup])){
    		ET_UGNane.setText(getGBKString(DataStruct.RcvDeviceData.SYS.UserGroup[UserGroup]));  
    	}else{
    		ET_UGNane.setText(""); 
    	}
    }
  
  	void SetUserGroupName(int UserID){
  		String gname="";

  		gname=String.valueOf(ET_UGNane.getText());
		byte[] strGBK = null;
		try {
			strGBK = gname.getBytes("GBK");
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		}
		for(int i=0;i<15;i++){
			DataStruct.RcvDeviceData.SYS.UserGroup[UserID][i]=0x00;
		}
		int bn=0;
		if(strGBK.length>DataStruct.MAX_Name_Size){
			for(int i=0;i<DataStruct.MAX_Name_Size;i++){
				DataStruct.RcvDeviceData.SYS.UserGroup[UserID][i]=(char) strGBK[i];
				if((strGBK[i]&0xFF) >= (0xa0)){
					++bn;
				}
			}				
			if(((bn%2)==1)&&((DataStruct.RcvDeviceData.SYS.UserGroup[UserID][12]) >= (0xa0))){
				DataStruct.RcvDeviceData.SYS.UserGroup[UserID][12]=0x00;
			}
		}else{
			for(int i=0;i<strGBK.length;i++){
				DataStruct.RcvDeviceData.SYS.UserGroup[UserID][i]=(char) strGBK[i];
			}	
		}	
	}
}
