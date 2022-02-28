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
public class oct_selectDialogFragment extends DialogFragment {

    private Button Exit;
    private TextView Msg;

    private int data = 0;
    public static final String ST_Data = "Data";
    private int DataOPT = 1;
    public static final String ST_DataOPT = "ST_DataOPT";
    public static final int DataOPT_HP   = 0;
    public static final int DataOPT_LP = 1;

    private TextView title_oct;

    private int maxOct=9;

    private Button[] btn_oct=new Button[maxOct];
    private Button btn_exit;

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

        data = getArguments().getInt(ST_Data);
        DataOPT = getArguments().getInt(ST_DataOPT);

		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.chs_dialog_oct_select, container,false);
        initView(view);
        FlashPageUI();
        return view;
    }



    private void initView(View view) {

        title_oct=view.findViewById(R.id.id_oct_title);
        btn_oct[0]=view.findViewById(R.id.id_oct_1);
        btn_oct[1]=view.findViewById(R.id.id_oct_2);
        btn_oct[2]=view.findViewById(R.id.id_oct_3);
        btn_oct[3]=view.findViewById(R.id.id_oct_4);

        btn_oct[4]=view.findViewById(R.id.id_oct_5);
        btn_oct[5]=view.findViewById(R.id.id_oct_6);
        btn_oct[6]=view.findViewById(R.id.id_oct_7);
        btn_oct[7]=view.findViewById(R.id.id_oct_8);
        btn_oct[8]=view.findViewById(R.id.id_oct_9);
//        btn_oct[4]=view.findViewById(R.id.id_oct_5);
//        btn_oct[5]=view.findViewById(R.id.id_oct_6);
//        btn_oct[6]=view.findViewById(R.id.id_oct_7);
//        btn_oct[7]=view.findViewById(R.id.id_oct_8);
//        btn_oct[7]=view.findViewById(R.id.id_oct_8);
        btn_exit=view.findViewById(R.id.id_oct_exit);
        initclick();
    }

    private void initclick() {
        btn_exit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();
            }
        });
        for (int i = 0; i <btn_oct.length ; i++) {
            btn_oct[i].setTag(i);
            btn_oct[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mSetOnClickDialogListener!=null){
                        mSetOnClickDialogListener.onClickDialogListener( (int)v.getTag(), true);
                    }
                    getDialog().cancel();
                }
            });
        }
    }

    private void FlashPageUI() {
        if(DataOPT==0){
            title_oct.setText(getResources().getString(R.string.HighPassFilter));
        }else{
            title_oct.setText(getResources().getString(R.string.LowPassFilter));
        }
        for (int i = 0; i <btn_oct.length ; i++) {
            btn_oct[i].setVisibility(View.GONE);
        }

        if(MacCfg.Mcu!=7){

            for (int i = 0; i <btn_oct.length ; i++) {
                btn_oct[i].setVisibility(View.VISIBLE);
                btn_oct[i].setText(String.valueOf( DataStruct.CurMacMode.XOver.Level.memberName1[i]));
            }
        }else{
            if(MacCfg.OutputChannelSel>3){
                for (int i = 0; i <4 ; i++) {
                    btn_oct[i].setVisibility(View.VISIBLE);
                    btn_oct[i].setText(String.valueOf( DataStruct.CurMacMode.XOver.Level.memberName1[i]));
                }
                btn_oct[8].setText(String.valueOf( DataStruct.CurMacMode.XOver.Level.memberName1[8]));

                btn_oct[8].setVisibility(View.VISIBLE);
            }else{
                for (int i = 0; i < 2 ; i++) {
                    btn_oct[i].setVisibility(View.VISIBLE);
                    btn_oct[i].setText(String.valueOf( DataStruct.CurMacMode.XOver.Level.memberName1[i]));
                }
                btn_oct[8].setText(String.valueOf( DataStruct.CurMacMode.XOver.Level.memberName1[8]));
                btn_oct[8].setVisibility(View.VISIBLE);
            }
        }


    }
}
