package com.chs.mt.xf_dap.fragment.dialogFragment;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.chs.mt.xf_dap.R;
import com.chs.mt.xf_dap.datastruct.DataStruct;
import com.chs.mt.xf_dap.datastruct.MacCfg;

@SuppressLint({ "NewApi", "ClickableViewAccessibility" })
public class AboutDialogFragment extends DialogFragment {
    //关于
    private TextView TV_MCU_Version_Menu,TV_SoftVersion_Menu,TV_CopyRight_Menu,TV_DeviceMac;//TV_DeviceVersion
    private Button AboutSure;



    private SetOnClickDialogListener mSetOnClickDialogListener;
    public void OnSetOnClickDialogListener(SetOnClickDialogListener listener) {
        this.mSetOnClickDialogListener = listener;
    }
    public interface SetOnClickDialogListener{
        void onClickDialogListener(int type, boolean boolClick);
    }
	@Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
        Bundle savedInstanceState) {  
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.chs_about_dialog, container,false);
        initView(view);
		initData();
		initClick();
        return view;  
    }  
	private void initView(View V_AboutDialog) {

        AboutSure = (Button)V_AboutDialog.findViewById(R.id.id_b_about_ok);

        TV_MCU_Version_Menu = (TextView) V_AboutDialog.findViewById(R.id.id_tv_device_version);
        TV_SoftVersion_Menu = (TextView) V_AboutDialog.findViewById(R.id.id_tv_soft_version);
        TV_CopyRight_Menu = (TextView) V_AboutDialog.findViewById(R.id.id_tv_copyright);
        TV_DeviceMac = (TextView) V_AboutDialog.findViewById(R.id.id_tv_device_mac);
        TV_MCU_Version_Menu.setText(getResources().getString(R.string.device_version)+MacCfg.DeviceVerString);
        //MacCfg.App_versions="Beta 1.01";



       MacCfg.App_versions=String.valueOf(MacCfg.App_version);

        TV_SoftVersion_Menu.setText(getResources().getString(R.string.Software_version)+MacCfg.App_versions);
        TV_CopyRight_Menu.setText(MacCfg.Copyright);


        TV_DeviceMac.setText(getResources().getString(R.string.MacDevice)+ DataStruct.CurMacMode.MacTypeDisplay);
        AboutSure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().cancel();
                if(mSetOnClickDialogListener != null){
                    mSetOnClickDialogListener.onClickDialogListener(0, true);
                }

            }
        });
	}

	private void initData() {

	}

	private void initClick() {

	}

}
