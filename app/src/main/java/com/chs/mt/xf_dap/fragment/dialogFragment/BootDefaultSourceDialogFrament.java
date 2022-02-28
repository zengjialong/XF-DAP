package com.chs.mt.xf_dap.fragment.dialogFragment;

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
import android.widget.LinearLayout;

import com.chs.mt.xf_dap.R;
import com.chs.mt.xf_dap.datastruct.DataStruct;

/**
 * 开机默认音源
 * */
public class BootDefaultSourceDialogFrament extends DialogFragment {
    private LinearLayout ly_moni,ly_player,ly_aux;
    private Button btn_exit;
    private Button btn_moni,btn_player,btn_aux;

    private SetOnClickDialogListener setOnClickDialogListener;
    public void OnSetOnClickDialogListener(SetOnClickDialogListener listener) {
        this.setOnClickDialogListener = listener;
    }

    private interface SetOnClickDialogListener{
        void onClickDialogListener(boolean boolClick);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       // return super.onCreateView(inflater, container, savedInstanceState);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.chs_dialog_boot_source, container,false);
        initView(view);
        FlashDefaultSource();
        return view;
    }

    private void FlashDefaultSource() {
        btn_moni.setTextColor(getResources().getColor(R.color.white));
        btn_player.setTextColor(getResources().getColor(R.color.white));
        btn_aux.setTextColor(getResources().getColor(R.color.white));
        switch (DataStruct.RcvDeviceData.SYS.Default_sound_source){
            case 1:
                btn_moni.setTextColor(getResources().getColor(R.color.oct_select_num));
                break;
            case 5:
                btn_player.setTextColor(getResources().getColor(R.color.oct_select_num));
                break;
            case 3:
                btn_aux.setTextColor(getResources().getColor(R.color.oct_select_num));
                break;
            default:
                btn_moni.setTextColor(getResources().getColor(R.color.oct_select_num));
                break;
        }
    }

    private void initView(View view) {
        ly_moni=view.findViewById(R.id.id_source1);
        ly_player=view.findViewById(R.id.id_source2);
        ly_aux=view.findViewById(R.id.id_source3);
        btn_exit=view.findViewById(R.id.id_chs_dialog_exit);
        btn_moni=view.findViewById(R.id.id_v_image1);
        btn_player=view.findViewById(R.id.id_v_image2);
        btn_aux=view.findViewById(R.id.id_v_image3);

        ly_moni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataStruct.RcvDeviceData.SYS.Default_sound_source=1;
                getDialog().cancel();
                if(setOnClickDialogListener != null){
                    setOnClickDialogListener.onClickDialogListener( true);
                }
            }
        });
        ly_aux.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataStruct.RcvDeviceData.SYS.Default_sound_source=3;
                getDialog().cancel();
                if(setOnClickDialogListener != null){
                    setOnClickDialogListener.onClickDialogListener( true);
                }
            }
        });

        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();
            }
        });

        ly_player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataStruct.RcvDeviceData.SYS.Default_sound_source=5;
                getDialog().cancel();
                if(setOnClickDialogListener != null){
                    setOnClickDialogListener.onClickDialogListener( true);
                }
            }
        });

    }


}
