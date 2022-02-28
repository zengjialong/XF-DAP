package com.chs.mt.xf_dap.fragment.dialogFragment;

import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.chs.mt.xf_dap.R;

public class AlertResetOutSPKDialogFragment extends DialogFragment {

    private int DataOPT = 1;
    public static final String ST_DataOPT = "ST_DataOPT";
    private String SetMessage = "";
    public static final String ST_SetMessage = "ST_SetMessage";
    private Button btn_empty, btn_default;


    private SetOnClickDialogListener mSetOnClickDialogListener;

    public void OnSetOnClickDialogListener(SetOnClickDialogListener listener) {
        this.mSetOnClickDialogListener = listener;
    }

    public interface SetOnClickDialogListener {
        void onClickDialogListener(int type, boolean boolClick);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //DataOPT = getArguments().getInt(ST_DataOPT);
        //SetMessage = getArguments().getString(ST_SetMessage,null);


        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
      //  getDialog().setCanceledOnTouchOutside(false);
        View view = inflater.inflate(R.layout.chs_dialog_reset, container, false);
        initView(view);

        return view;
    }

    private void initView(View view) {
        btn_empty = view.findViewById(R.id.id_b_cancel);
        btn_default = view.findViewById(R.id.id_b_save);
        btn_empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSetOnClickDialogListener != null) {
                    getDialog().cancel();
                    mSetOnClickDialogListener.onClickDialogListener(0, true);
                }
            }
        });
        btn_default.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSetOnClickDialogListener != null) {
                    getDialog().cancel();
                    mSetOnClickDialogListener.onClickDialogListener(1, true);
                }
            }
        });
    }


//	public Dialog onCreateDialog(Bundle savedInstanceState){
//		//DataOPT = getArguments().getInt(ST_DataOPT);
//		//SetMessage = getArguments().getString(ST_SetMessage);
//		// Use the Builder class for convenient dialog construction
//		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//		builder.setMessage(getString(R.string.Opt_Channel_Set))
//			.setPositiveButton(getResources().getString(R.string.Emptied),
//				new DialogInterface.OnClickListener(){
//					@Override
//					public void onClick(DialogInterface dialog, int id){
//						if(mSetOnClickDialogListener != null){
//							mSetOnClickDialogListener.onClickDialogListener(0, true);
//						}
//					}
//				})
//			.setNegativeButton(getResources().getString(R.string.Default),
//				new DialogInterface.OnClickListener(){
//					@Override
//					public void onClick(DialogInterface dialog, int id){

//					}
//				});
//		// Create the AlertDialog object and return it
//		return builder.create();
//	}
}
