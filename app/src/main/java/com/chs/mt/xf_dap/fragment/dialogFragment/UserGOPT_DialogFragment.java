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
import com.chs.mt.xf_dap.datastruct.MacCfg;
import com.chs.mt.xf_dap.R;

@SuppressLint({ "NewApi", "ClickableViewAccessibility" })
public class UserGOPT_DialogFragment extends DialogFragment {

    private Button Exit;
    private TextView Msg;
    private Button[] B_Item = new Button[5];

    private int data = 0;
    public static final String ST_Data = "Data";
    private int DataOPT = 1;
    public static final String ST_DataOPT = "ST_DataOPT";
    public static final int DataOPT_HP   = 0;
    public static final int DataOPT_LP = 1;

    private LinearLayout ly_bg;

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

//        data = getArguments().getInt(ST_Data);
//        DataOPT = getArguments().getInt(ST_DataOPT);

		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.chs_dialog_user_gopt, container,false);
        initView(view);
		initData();
		initClick();
        return view;  
    }


	private void initView(View view) {
        Exit = (Button) view.findViewById(R.id.id_chs_dialog_exit);
        Exit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();
            }
        });
        ly_bg= view.findViewById(R.id.id_ly_bg);
//        Msg = (TextView) view.findViewById(R.id.id_text_msg);
//        Msg.setText(getString(R.string.Toning_CarType));
        B_Item[0] = (Button) view.findViewById(R.id.id_chs_dialog_btn0);
        B_Item[1] = (Button) view.findViewById(R.id.id_chs_dialog_btn1);
        B_Item[2] = (Button) view.findViewById(R.id.id_chs_dialog_btn2);
        B_Item[3] = (Button) view.findViewById(R.id.id_chs_dialog_btn3);
        B_Item[4] = (Button) view.findViewById(R.id.id_chs_dialog_btn4);


        if(MacCfg.Mcu==0){
            B_Item[0].setVisibility(View.GONE);
            ly_bg.setBackgroundResource(R.drawable.chs_dialog_list2_bg);
        }else{
            B_Item[0].setVisibility(View.VISIBLE);
            ly_bg.setBackgroundResource(R.drawable.chs_dialog_list3_bg);
        }




        for(int i=0;i<5;i++){
            B_Item[i].setTag(i);
            B_Item[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int newVal = (Integer) v.getTag();
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

}
