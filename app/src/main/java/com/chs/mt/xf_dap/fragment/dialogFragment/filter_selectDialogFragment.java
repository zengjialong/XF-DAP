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


@SuppressLint({ "NewApi", "ClickableViewAccessibility" })
public class filter_selectDialogFragment extends DialogFragment {

    private Button Exit;
    private TextView Msg;

    private int data = 0;
    public static final String ST_Data = "Data";
    private int DataOPT = 1;
    public static final String ST_DataOPT = "ST_DataOPT";
    public static final int DataOPT_HP   = 0;
    public static final int DataOPT_LP = 1;

    private TextView title_filter;

    private int maxOct=3;

    private Button[] btn_filter=new Button[maxOct];
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
        View view = inflater.inflate(R.layout.chs_dialog_filter_select, container,false);
        initView(view);
        FlashPageUI();
        return view;
    }



    private void initView(View view) {

        title_filter=view.findViewById(R.id.id_filter_title);
        btn_filter[0]=view.findViewById(R.id.id_filter_1);
        btn_filter[1]=view.findViewById(R.id.id_filter_2);
        btn_filter[2]=view.findViewById(R.id.id_filter_3);


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
        for (int i = 0; i <btn_filter.length ; i++) {
            btn_filter[i].setTag(i);
            btn_filter[i].setOnClickListener(new OnClickListener() {
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
            title_filter.setText(getResources().getString(R.string.HighPassFilter));
        }else{
            title_filter.setText(getResources().getString(R.string.LowPassFilter));
        }
        for (int i = 0; i <btn_filter.length ; i++) {
            btn_filter[i].setText(String.valueOf( DataStruct.CurMacMode.XOver.Fiter.memberName1[i]));
        }
    }
}
