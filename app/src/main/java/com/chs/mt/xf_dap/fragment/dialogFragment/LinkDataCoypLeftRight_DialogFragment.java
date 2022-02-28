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

@SuppressLint({ "NewApi", "ClickableViewAccessibility" })
public class LinkDataCoypLeftRight_DialogFragment extends DialogFragment {

    private Button Exit;
    private TextView Msg;
    private Button[] B_Item = new Button[6];

    private int data = 0;
    public static final String ST_Data = "Data";
    private int DataOPT = 1;
    public static final String ST_DataOPT = "ST_DataOPT";
    public static final int DataOPT_HP   = 0;
    public static final int DataOPT_LP = 1;


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
        View view = inflater.inflate(R.layout.chs_dialog_link_copy_leftright, container,false);
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

        Msg = (TextView) view.findViewById(R.id.id_text_msg);


        B_Item[0] = (Button) view.findViewById(R.id.id_chs_dialog_btn0);
        B_Item[1] = (Button) view.findViewById(R.id.id_chs_dialog_btn1);
        B_Item[2] = (Button) view.findViewById(R.id.id_chs_dialog_btn2);

//        if(data == 1){
//            Msg.setText(getString(R.string.FrontCoupling));
//            B_Item[0].setText(R.string.LinkBaseCh1);
//            B_Item[1].setText(R.string.LinkBaseCh2);
//        }else if(data == 2){
//            Msg.setText(getString(R.string.RearCoupling));
//            B_Item[0].setText(R.string.LinkBaseCh3);
//            B_Item[1].setText(R.string.LinkBaseCh4);
//        }else if(data == 3){
//            Msg.setText(getString(R.string.SubCoupling));
//            B_Item[0].setText(R.string.LinkBaseCh5D);
//            B_Item[1].setText(R.string.LinkBaseCh5D);
//        }

//        B_Item[0].setTextColor(getResources().getColor(R.color.color_dialogItemTextPress));
//        B_Item[2].setTextColor(getResources().getColor(R.color.color_dialogItemTextCancle));
        for(int i=0;i<3;i++){
            B_Item[i].setTag(i);
            B_Item[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int newVal = (Integer) v.getTag();
                    switch (newVal){
                        case 0:
                            if (mSetOnClickDialogListener != null) {
                                mSetOnClickDialogListener.onClickDialogListener(newVal, true);
                            }
                            break;
                        case 1:
                            if (mSetOnClickDialogListener != null) {
                                mSetOnClickDialogListener.onClickDialogListener(newVal, false);
                            }
                            break;
                        case 2:

                            break;

                    }
                    getDialog().cancel();
                }
            });
        }

	}

	private void initData() {

	}

	private void initClick() {

    }



}
