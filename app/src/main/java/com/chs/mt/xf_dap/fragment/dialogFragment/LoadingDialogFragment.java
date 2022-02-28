package com.chs.mt.xf_dap.fragment.dialogFragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.chs.mt.xf_dap.R;
import com.chs.mt.xf_dap.datastruct.DataStruct;
import com.chs.mt.xf_dap.datastruct.Define;
import com.chs.mt.xf_dap.datastruct.MacCfg;

@SuppressLint({ "NewApi", "ClickableViewAccessibility" })
public class LoadingDialogFragment extends DialogFragment {
    private static int MSG_What_Show_Val = 1;
    private static int MSG_What_Cancel   = 2;

    private static int MSG_What_FlashMain   = 3;
    /*加载对话框f*/
    private Dialog LoadingDialog,Loading;
    private TextView TV_LoadShow,TV_loadMessage;
    private View V_LoadingDialog;

    int toal=0;
    int step=0;


    private SetOnClickDialogListener mSetOnClickDialogListener;
    public void OnSetOnClickDialogListener(SetOnClickDialogListener listener) {
        this.mSetOnClickDialogListener = listener;
    }
    public interface SetOnClickDialogListener{
        void onClickDialogListener(int type, boolean boolClick);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(MacCfg.BOOL_DialogHideBG){
            Window window = getDialog().getWindow();
            WindowManager.LayoutParams windowParams = window.getAttributes();
            windowParams.dimAmount = 0.0f;
            windowParams.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(windowParams);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
        Bundle savedInstanceState) {  
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCanceledOnTouchOutside(false);
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.chs_layout_loading_dialog, container,false);
        initView(view);
		initData();
		initClick();
        return view;  
    }  
	private void initView(View V_LoadingDialog) {
        ImageView spaceshipImage = (ImageView) V_LoadingDialog.findViewById(R.id.id_loading_dialog_img);
        TV_LoadShow = (TextView) V_LoadingDialog.findViewById(R.id.id_tv_progress);
        TV_loadMessage = (TextView) V_LoadingDialog.findViewById(R.id.id_tv_loading_message);
        Animation animation = AnimationUtils.loadAnimation(
                getActivity(), R.anim.chs_cirle); // 加载动画
        animation.setInterpolator(new LinearInterpolator());
        spaceshipImage.startAnimation(animation); // 使用ImageView显示动画

        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return false;
            }
        });

        if(DataStruct.SEFF_USER_GROUP_OPT == 2){
            TV_loadMessage.setText(getString(R.string.Saving));
        }else if(DataStruct.SEFF_USER_GROUP_OPT==3){
            TV_loadMessage.setText(getString(R.string.delete_user));
        }
        else {
            TV_loadMessage.setText(getString(R.string.Loading));
        }
        toal = DataStruct.SendbufferList.size();
        System.out.println("BUG toal:" + toal);
        step=toal;
        new Thread(new Runnable() {

            @Override
            public void run() {

                while (step > 0){
                    float val = (((float)toal - step)/toal)*100;
                    String progresString = String.valueOf((int)(val))+"%";
                    //需要数据传递，用下面方法；
                    Message msg =new Message();
                    msg.what = MSG_What_Show_Val;
                    msg.obj = progresString;//可以是基本类型，可以是对象，可以是List、map等；
                    mHandler.sendMessage(msg);

                    //System.out.println("BUG val的职位"+val+"=====step====="+step);
                    if((val <0)||(val > 100)||(!DataStruct.isConnecting)){
                        Message msgc =new Message();
                        msgc.what = MSG_What_Cancel;
                        mHandler.sendMessage(msgc);

                        Message msg2 =new Message();
                        msg2.what = MSG_What_FlashMain;
                        mHandler.sendMessage(msg2);
                    }

                    if (step == 0) {
                        //需要数据传递，用下面方法；
                        Message msg1 =new Message();
                        msg1.what = MSG_What_Show_Val;
                        msg1.obj = "100%";//可以是基本类型，可以是对象，可以是List、map等；
                        mHandler.sendMessage(msg1);


                        Message msg2 =new Message();
                        msg2.what = MSG_What_FlashMain;
                        mHandler.sendMessage(msg2);
                    }

                    try {
                        Thread.sleep(700);
                    } catch (InterruptedException e) {

                    }
                    step = DataStruct.SendbufferList.size();
                }

                Message msgc =new Message();
                msgc.what = MSG_What_Cancel;
                mHandler.sendMessage(msgc);

            }


        }).start();

	}

	private void initData() {

	}

	private void initClick() {

	}

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == MSG_What_Show_Val){
                TV_LoadShow.setText(msg.obj.toString());
            }else if(msg.what == MSG_What_Cancel){
                if(getDialog() != null){
                    getDialog().cancel();
                    Intent intentt=new Intent();
                    intentt.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
                    intentt.putExtra("msg", Define.BoardCast_ReadGroupFlashHome);
                    intentt.putExtra("state", false);
                    getActivity().sendBroadcast(intentt);

                    Intent intentr=new Intent();
                    intentr.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
                    intentr.putExtra("msg", Define.BoardCast_FlashUI_AllPage);
                    intentr.putExtra("state", false);
                    getActivity().sendBroadcast(intentr);
                }
            }else if(msg.what == MSG_What_FlashMain){

            }
        }

    };

}
