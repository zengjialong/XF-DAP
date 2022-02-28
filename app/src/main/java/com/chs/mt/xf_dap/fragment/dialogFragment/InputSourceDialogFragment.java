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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chs.mt.xf_dap.R;
import com.chs.mt.xf_dap.datastruct.DataStruct;

@SuppressLint({ "NewApi", "ClickableViewAccessibility" })
public class InputSourceDialogFragment extends DialogFragment {

    private Button Exit;
    private TextView Msg;
    private Button[] B_Item = new Button[6];
    private LinearLayout LY_BG;

    private int data = 0;
    public static final String ST_Data = "Data";
    private int DataOPT = 1;
    public static final String ST_DataOPT = "ST_DataOPT";
    public static final int DataOPT_INS   = 0;
    public static final int DataOPT_MIXER = 1;


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

        //data = getArguments().getInt(ST_Data);
        DataOPT = getArguments().getInt(ST_DataOPT);

		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.chs_dialog_inputsource, container,false);
        initView(view);
		initData();
		initClick();
        return view;  
    }


	private void initView(View view) {
        Exit = (Button) view.findViewById(R.id.id_chs_dialog_exit);
        B_Item[0] = (Button) view.findViewById(R.id.id_chs_dialog_btn0);
        B_Item[1] = (Button) view.findViewById(R.id.id_chs_dialog_btn1);
        B_Item[2] = (Button) view.findViewById(R.id.id_chs_dialog_btn2);
        B_Item[3] = (Button) view.findViewById(R.id.id_chs_dialog_btn3);
        B_Item[4] = (Button) view.findViewById(R.id.id_chs_dialog_btn4);
        B_Item[5] = (Button) view.findViewById(R.id.id_chs_dialog_btn5);
        LY_BG = (LinearLayout) view.findViewById(R.id.id_ly);
        Exit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();
            }
        });

        Msg = (TextView) view.findViewById(R.id.id_text_msg);
        System.out.println("BUG 大大大");
        if(DataOPT == DataOPT_MIXER){
            Msg.setText(getString(R.string.Ad_MixerSourceSet));
            showBg(DataStruct.CurMacMode.Mixersource.Max);
            for(int i=0;i<DataStruct.CurMacMode.Mixersource.Max;i++){
                B_Item[i].setVisibility(View.VISIBLE);
                if(i==DataStruct.CurMacMode.Mixersource.Max-1){
                    B_Item[i].setBackgroundResource(R.color.nullc);
                }else{
                    B_Item[i].setBackgroundResource(R.drawable.button_style);
                }
                B_Item[i].setText(DataStruct.CurMacMode.Mixersource.Name[i]);
            }
                for(int i=0;i<DataStruct.CurMacMode.Mixersource.Max;i++){
                    if(DataStruct.RcvDeviceData.SYS.none[1] ==
                            DataStruct.CurMacMode.Mixersource.inputsource[i]){
                        B_Item[i].setTextColor(getResources().getColor(R.color.color_dialogItemTextPress));
                        System.out.println("BUG i:" + i);
                        break;
                    }
                }

        }else{
            Msg.setText(getString(R.string.Ad_InputSourceSet));
            showBg(DataStruct.CurMacMode.inputsource.Max);
            for(int i=0;i<DataStruct.CurMacMode.inputsource.Max;i++){
                B_Item[i].setVisibility(View.VISIBLE);
                if(i!=DataStruct.CurMacMode.inputsource.Max-1){
                    B_Item[i].setBackgroundResource(R.drawable.button_style);
                }else{
                    B_Item[i].setBackgroundResource(R.color.nullc);
                }

                B_Item[i].setText(DataStruct.CurMacMode.inputsource.Name[i]);
            }

            for(int i=0;i<DataStruct.CurMacMode.inputsource.Max;i++){
                if(DataStruct.RcvDeviceData.SYS.input_source ==
                        DataStruct.CurMacMode.inputsource.inputsource[i]){
                    B_Item[i].setTextColor(getResources().getColor(R.color.color_dialogItemTextPress));
                    break;
                }
            }
        }


        for(int i=0;i<6;i++){
            B_Item[i].setTag(i);
            B_Item[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int newVal = (Integer) v.getTag();
                    System.out.println("BUG newVal:" + newVal);
                    if (mSetOnClickDialogListener != null) {
                        mSetOnClickDialogListener.onClickDialogListener(newVal, true);
                        getDialog().cancel();
                    }
                }
            });
        }

	}

	private void initData() {

	}

	private void initClick() {

    }

    private void showBg(int val){
//        switch (val){
//            case 2: LY_BG.setBackgroundResource(R.drawable.chs_dialog_list2_bg); break;
//            case 3: LY_BG.setBackgroundResource(R.drawable.chs_dialog_list3_bg); break;
//            case 4: LY_BG.setBackgroundResource(R.drawable.chs_dialog_list4_bg); break;
//            case 5: LY_BG.setBackgroundResource(R.drawable.chs_dialog_list5_bg); break;
//            //case 6: LY_BG.setBackgroundResource(R.drawable.chs_dialog_list6_bg); break;
//            default:LY_BG.setBackgroundResource(R.drawable.chs_dialog_list5_bg); break;
//        }
    }
}
