package com.chs.mt.xf_dap.fragment.dialogFragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.chs.mt.xf_dap.R;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

@SuppressLint({ "NewApi", "ClickableViewAccessibility" })
public class InfomationFragment extends DialogFragment {
    //关于
    private ImageView AboutSure;
    private TextView textView;


    private SetOnClickDialogListener mSetOnClickDialogListener;
    public void OnSetOnClickDialogListener(SetOnClickDialogListener listener) {
        this.mSetOnClickDialogListener = listener;
    }

    @Override
    public void onStart() {
        super.onStart();
        //计算宽高大小，只显示90%
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout((int) (dm.widthPixels * 0.9), (int) (dm.heightPixels * 0.8));
        }
    }


    public interface SetOnClickDialogListener{
        void onClickDialogListener(int type, boolean boolClick);
    }
	@Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
        Bundle savedInstanceState) {  
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.chs_infomation_dialog, container,false);
        initView(view);
		initData();
		initClick();
        return view;  
    }  
	private void initView(View V_AboutDialog) {

        AboutSure = (ImageView)V_AboutDialog.findViewById(R.id.exit);
        textView= V_AboutDialog.findViewById(R.id.id_txt_num);
        doRaw();
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
    private void doRaw(){
        InputStream is = this.getResources().openRawResource(R.raw.chs);
        try{
            doRead(is);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    private void doRead(InputStream is) throws IOException{
        DataInputStream dis = new DataInputStream(is);
        byte[]buffer = new byte[is.available()];
        dis.readFully(buffer);
        textView.setText(new String(buffer));
        dis.close();
        is.close();
    }

}
