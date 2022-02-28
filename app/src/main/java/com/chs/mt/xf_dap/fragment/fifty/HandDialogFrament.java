package com.chs.mt.xf_dap.fragment.fifty;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.chs.mt.xf_dap.R;
import com.chs.mt.xf_dap.datastruct.DataStruct;
import com.chs.mt.xf_dap.operation.DataOptUtil;

public class HandDialogFrament extends DialogFragment {
    private ImageView imageView;
    private Button btn_exit;
    private SetOnClickDialogListener mSetOnClickDialogListener;
    public void OnSetOnClickDialogListener(SetOnClickDialogListener listener) {
        this.mSetOnClickDialogListener = listener;
    }
    public interface SetOnClickDialogListener{
        void onClickDialogListener(boolean boolClick);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       // return super.onCreateView(inflater, container, savedInstanceState);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.user_chs_dialog_hand, container,false);
        initView(view);
        FlashPageUI();
        return view;
    }

    private void initView(View view) {
        imageView=view.findViewById(R.id.id_chs_dialog_btn1);
        btn_exit=view.findViewById(R.id.id_chs_dialog_exit);
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DataStruct.RcvDeviceData.SYS.Proc_Amp_Mode==0){
                    DataStruct.RcvDeviceData.SYS.Proc_Amp_Mode=1;
                }else{
                    DataStruct.RcvDeviceData.SYS.Proc_Amp_Mode=0;
                }
                DataOptUtil.ComparedToSendData(true);
                if (mSetOnClickDialogListener != null) {
                    mSetOnClickDialogListener.onClickDialogListener(true);
                }
                FlashPageUI();
            }
        });
    }

    public void FlashPageUI() {
        if(DataStruct.RcvDeviceData.SYS.Proc_Amp_Mode==0){
            imageView.setImageResource(R.drawable.chs_off);
        }else{
            imageView.setImageResource(R.drawable.chs_on);
        }

    }


}
