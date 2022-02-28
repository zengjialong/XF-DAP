package com.chs.mt.xf_dap.main;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chs.mt.xf_dap.R;
import com.chs.mt.xf_dap.datastruct.DataStruct;
import com.chs.mt.xf_dap.datastruct.Define;
import com.chs.mt.xf_dap.datastruct.MacCfg;
import com.chs.mt.xf_dap.fragment.EQ_Fragment;
import com.chs.mt.xf_dap.fragment.dialogFragment.AlertDialogFragment;
import com.chs.mt.xf_dap.operation.DataOptUtil;
import com.chs.mt.xf_dap.tools.EQ_Genter;

import java.text.DecimalFormat;

public class EQ_GenterActivity extends Activity implements View.OnTouchListener {
    /** 触摸时按下的点 **/
    PointF downP = new PointF();
    /** 触摸时当前的点 **/
    PointF curP = new PointF();
    private EQ_Genter eq_genter;
    private Button back_eq,btn_donott;
    private EQ_Fragment eq_fragment;
    private Context mContext;
    private TextView[] btn_channelselect=new TextView[12];
    private Button btn_eq_genter,btn_reset_eq,btn_recover_eq,lock_eq,btn_output_select;
    private boolean bool_EQ_ModeFlag = false;
    private ImageView img_src;
    private boolean bool_ByPass = false;
    private AlertDialogFragment alertDialogFragment = null;
    private TextView txt_freq,txt_level;
   // private
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.eq_genter);
        initView();
        FlashPageUI();
    }

    private void FlashPageUI() {
        FlashChannelUI();
      //  eq_genter.SetEQData(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel]);
    }

    private void initView() {
        back_eq=findViewById(R.id.back_eq);
        eq_genter=findViewById(R.id.id_eq_eqfilter_page);
        btn_donott=findViewById(R.id.id_button_donott);
        btn_recover_eq=findViewById(R.id.recover_eq);
        btn_reset_eq=findViewById(R.id.reset_eq);
        lock_eq=findViewById(R.id.lock_eq);
        txt_freq=findViewById(R.id.id_freq);
        txt_level=findViewById(R.id.id_level);

        btn_output_select=findViewById(R.id.output_select);
        img_src=findViewById(R.id.id_select);
        mContext=this;
        initClick();
    }

    private void initClick() {
        back_eq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentw = new Intent();
                intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
                intentw.putExtra("msg", Define.BoardCast_FlashUI_EQ);
                mContext.sendBroadcast(intentw);
                finish();
            }
        });
        eq_genter.setOnEQ_GenterChangeListener(new EQ_Genter.EQ_GenterChangeListener() {
            @Override
            public void onProgressChanged(EQ_Genter eq_genter, int progress,int current, boolean fromUser) {

                MacCfg.EQ_Num=current;
                DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[current].level=progress;


                txt_freq.setText(String.valueOf(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[current].freq)+"Hz");
                txt_level.setText(String.valueOf(ChangeGainValume(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[current].level - DataStruct.CurMacMode.EQ.EQ_LEVEL_MIN)));


                CheckEQByPass();
                MacCfg.UI_Type=Define.UI_EQ_ALL;
                DataOptUtil.syncLinkData();
            }
        });
        btn_output_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomDialog();
            }
        });
        img_src.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomDialog();
            }
        });

        lock_eq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                btn_reset_eq.setEnabled(false);
//                btn_recover_eq.setEnabled(false);
//                btn_donott.setVisibility(View.VISIBLE);
                System.out.println("BUG =-=值为"+MacCfg.Lock_EQ[MacCfg.OutputChannelSel]+"TT"+MacCfg.OutputChannelSel);
                if(MacCfg.Lock_EQ[MacCfg.OutputChannelSel]){
                    MacCfg.Lock_EQ[MacCfg.OutputChannelSel]=false;
                }else{
                    MacCfg.Lock_EQ[MacCfg.OutputChannelSel]=true;
                }
                System.out.println("BUG ---值为"+MacCfg.Lock_EQ[MacCfg.OutputChannelSel]);

                FlashChannelUI();

            }
        });
        btn_reset_eq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(AlertDialogFragment.ST_DataOPT, 0);
                bundle.putString(AlertDialogFragment.ST_SetMessage, getResources().getString(R.string.ResetEQ));

                if (alertDialogFragment == null) {
                    alertDialogFragment = new AlertDialogFragment();
                }
                if (!alertDialogFragment.isAdded()) {
                    alertDialogFragment.setArguments(bundle);
                    alertDialogFragment.show(getFragmentManager(), "alertDialogFragment");
                }

                alertDialogFragment.OnSetOnClickDialogListener(new AlertDialogFragment.SetOnClickDialogListener() {

                    @Override
                    public void onClickDialogListener(int type, boolean boolClick) {
                        if (boolClick) {
                            Set_Default();
                        }
                    }
                });

            }
        });
        btn_recover_eq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(AlertDialogFragment.ST_DataOPT, 0);

                if (bool_ByPass) {//设置直通
                    bundle.putString(AlertDialogFragment.ST_SetMessage, getResources().getString(R.string.ByPassEQ));
                } else if (!bool_ByPass) {////设置恢复
                    bundle.putString(AlertDialogFragment.ST_SetMessage, getResources().getString(R.string.RecoverEQ));
                }

                if (alertDialogFragment == null) {
                    alertDialogFragment = new AlertDialogFragment();
                }
                if (!alertDialogFragment.isAdded()) {
                    alertDialogFragment.setArguments(bundle);
                    alertDialogFragment.show(getFragmentManager(), "alertDialogFragment");
                }
                alertDialogFragment.OnSetOnClickDialogListener(new AlertDialogFragment.SetOnClickDialogListener() {

                    @Override
                    public void onClickDialogListener(int type, boolean boolClick) {
                        if (boolClick) {
                            Set_Recover();
                        }
                    }
                });
            }
        });


    }

    private void showBottomDialog(){
        //1、使用Dialog、设置style
        final Dialog dialog = new Dialog(this,R.style.ActionSheetDialogStyle);
        //2、设置布局
        View view = View.inflate(this,R.layout.view_actionsheet,null);
        dialog.setContentView(view);

        Window window = dialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        window.setWindowAnimations(R.style.ActionSheetDialogAnimation);
        //设置对话框大小
        window.setLayout((int) getResources().getDimension(R.dimen.space_160),ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
        dialog.findViewById(R.id.tv_take_photo);
        btn_channelselect[0]= dialog.findViewById(R.id.tv_take_photo);
        btn_channelselect[1]= dialog.findViewById(R.id.tv_take_pic1);
        btn_channelselect[2]= dialog.findViewById(R.id.tv_take_pic2);
        btn_channelselect[3]= dialog.findViewById(R.id.tv_take_pic3);
        btn_channelselect[4]= dialog.findViewById(R.id.tv_take_pic4);
        btn_channelselect[5]= dialog.findViewById(R.id.tv_take_pic5);
        btn_channelselect[6]= dialog.findViewById(R.id.tv_take_pic6);
        btn_channelselect[7]= dialog.findViewById(R.id.tv_take_pic7);
        btn_channelselect[8]= dialog.findViewById(R.id.tv_take_pic8);
        btn_channelselect[9]= dialog.findViewById(R.id.tv_take_pic9);
        btn_channelselect[10]= dialog.findViewById(R.id.tv_take_pic10);
        btn_channelselect[11]= dialog.findViewById(R.id.tv_take_pic11);

        if(MacCfg.Mcu==3&&DataStruct.RcvDeviceData.SYS.mode == 1){
            btn_channelselect[1].setEnabled(false);
            btn_channelselect[1].setTextColor(getResources().getColor(R.color.gray));
        }else{
            btn_channelselect[1].setEnabled(true);
            btn_channelselect[1].setTextColor(getResources().getColor(R.color.background_dark));
        }


        for (int i = 0; i < btn_channelselect.length; i++) {
            btn_channelselect[i].setVisibility(View.GONE);
        }
        for (int i = 0; i < MacCfg.OutMax; i++) {
            btn_channelselect[i].setVisibility(View.VISIBLE);
        }


        for (int i = 0; i <btn_channelselect.length ; i++) {
            btn_channelselect[i].setTag(i);
            btn_channelselect[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MacCfg.OutputChannelSel=(int)v.getTag();
                    dialog.dismiss();
                    System.out.println("BUG 当前通道为"+DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].eq_mode);
                    if(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].eq_mode==0){
                        ModeChange();
                    }else{
                        FlashChannelUI();
                    }


                }
            });
        }
        dialog.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    private void ModeChange(){
        Bundle bundle = new Bundle();
        bundle.putInt(AlertDialogFragment.ST_DataOPT, 0);
        bundle.putString(AlertDialogFragment.ST_SetMessage, getResources().getString(R.string.ModeNotChange));

        if (alertDialogFragment == null) {
            alertDialogFragment = new AlertDialogFragment();
        }
        if (!alertDialogFragment.isAdded()) {
            alertDialogFragment.setArguments(bundle);
            alertDialogFragment.show(getFragmentManager(), "alertDialogFragment");
        }

        alertDialogFragment.OnSetOnClickDialogListener(new AlertDialogFragment.SetOnClickDialogListener() {

            @Override
            public void onClickDialogListener(int type, boolean boolClick) {
                if (boolClick) {
                    DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].eq_mode=1;

                    RestoreEQTo_EQ_Buf_Form_EQ_Default();
                    FlashChannelUI();
                    //联调
                    flashLinkDataUI(Define.UI_EQ_G_P_MODE_EQ);
                    flashLinkDataUI(Define.UI_EQ_ALL);

                    FlashChannelUI();
                }else{
                    MacCfg.OutputChannelSel=1;
                    Intent intentw = new Intent();
                    intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
                    intentw.putExtra("msg", Define.BoardCast_FlashUI_EQ);
                    mContext.sendBroadcast(intentw);
                    finish();
                }
            }
        });
    }



    private void RestoreEQTo_EQ_Buf_Form_EQ_Default() {
        //for(int i=0;i<MacCfg.OUT_CH_MAX;i++){
        for (int j = 0; j < Define.MAX_CHEQ; j++) {
            DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].freq = DataStruct.DefaultDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].freq;
            DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].level = DataStruct.DefaultDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].level;
            DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].bw = DataStruct.DefaultDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].bw;
            DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].shf_db = DataStruct.DefaultDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].shf_db;
            DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].type = DataStruct.DefaultDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].type;

            DataStruct.GainBuf[MacCfg.OutputChannelSel][j] = DataStruct.CurMacMode.EQ.EQ_LEVEL_ZERO;
        }
        SetEQStoreToDefault();
        //}
    }

    private void SetEQStoreToDefault() {
        for (int j = 0; j < Define.MAX_CHEQ; j++) {
//			BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].freq  = DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].freq;
            DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].level = DataStruct.DefaultDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].level;
//			BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].bw    = DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].bw;
//			BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].shf_db= DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].shf_db;
//			BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].type  = DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].type;
        }
    }

    //恢复出厂数据的界面
    private void Set_Default() {
        bool_ByPass=false;
        RestoreEQTo_EQ_Buf_Form_EQ_Default();
        //联调
        flashLinkDataUI(Define.UI_EQ_ALL);
        //刷新图表
        FlashChannelUI();
        btn_recover_eq.setText(R.string.Restore_EQ);
    }




    private void Set_Recover() {
        System.out.println("FUCK Set_Recover");
        if (bool_ByPass) {//设置直通    --  恢复状态
            //保存数据用于恢复
            bool_ByPass = false;

            SaveEQTo_EQ_Store();
            for (int i = 0; i < Define.MAX_CHEQ; i++) {
                DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].level = DataStruct.CurMacMode.EQ.EQ_LEVEL_ZERO;
            }
            btn_recover_eq.setText(R.string.Restore_EQ);
            System.out.println("BUG 取消-==进来设置了啊");
        } else if (!bool_ByPass) {//设置恢复

            bool_ByPass = true;
            EQ_StoreTo_Cur();
            btn_recover_eq.setText(R.string.Bypass_EQ);
            System.out.println("BUG 进来=-=-=-=设置了啊");
        }
        FlashChannelUI();
        //联调
        flashLinkDataUI(Define.UI_EQ_ALL);
    }


    private void SaveEQTo_EQ_Store() {
        for (int j = 0; j < Define.MAX_CHEQ; j++) {
            DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].level = DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].level;
        }
    }
    private void EQ_StoreTo_Cur() {
        for (int j = 0; j < Define.MAX_CHEQ; j++) {
            DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].level = DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].level;
        }
    }


    private void CheckEQByPass() {
        bool_ByPass = ByEQPass();
        if (bool_ByPass) {//可以设置直通
            btn_recover_eq.setText(R.string.Bypass_EQ);
            btn_recover_eq.setTextColor(getResources().getColor(R.color.eq_page_text_eq_set_press));
            btn_recover_eq.setBackgroundResource(R.drawable.chs_btn_eq_set_press);
        } else if (!bool_ByPass) {//可以设置恢复
            btn_recover_eq.setText(R.string.Restore_EQ);
            btn_recover_eq.setTextColor(getResources().getColor(R.color.eq_page_text_eq_set_normal));
            btn_recover_eq.setBackgroundResource(R.drawable.chs_btn_eq_set_normal);
        }
    }


    private void FlashChannelUI(){
        txt_freq.setText(String.valueOf(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[MacCfg.EQ_Num].freq)+"Hz");
        txt_level.setText(String.valueOf(ChangeGainValume(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[MacCfg.EQ_Num].level - DataStruct.CurMacMode.EQ.EQ_LEVEL_MIN)));

        CheckEQByPass();
        eq_genter.SetEQData(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel]);
        btn_output_select.setText(String.valueOf("CH"+(MacCfg.OutputChannelSel+1)));
        if(MacCfg.Lock_EQ[MacCfg.OutputChannelSel]==true){
            btn_reset_eq.setEnabled(false);
            btn_recover_eq.setEnabled(false);
            btn_recover_eq.setBackgroundResource(R.drawable.eqgenter_bg);
            btn_recover_eq.setTextColor(getResources().getColor(R.color.stroke_line_normal));
            btn_reset_eq.setBackgroundResource(R.drawable.eqgenter_bg);
            btn_reset_eq.setTextColor(getResources().getColor(R.color.stroke_line_normal));
            lock_eq.setBackgroundResource(R.drawable.chs_lock);
            btn_donott.setVisibility(View.VISIBLE);
        }else{
            btn_reset_eq.setEnabled(true);
            btn_recover_eq.setEnabled(true);
            lock_eq.setBackgroundResource(R.drawable.chs_unlock);

            btn_recover_eq.setBackgroundResource(R.drawable.chs_output_xover_reset_color_normal);
            btn_recover_eq.setTextColor(getResources().getColor(R.color.txt_press));
            btn_reset_eq.setBackgroundResource(R.drawable.chs_output_xover_reset_color_normal);
            btn_reset_eq.setTextColor(getResources().getColor(R.color.txt_press));

            btn_donott.setVisibility(View.GONE);
        }
    }

    private void flashLinkDataUI(int Tpe) {
        MacCfg.UI_Type = Tpe;
        DataOptUtil.syncLinkData();

    }
    private boolean ByEQPass() {
        boolean res = false;
        for (int i = 0; i < Define.MAX_CHEQ; i++) {
            if (DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].level != DataStruct.CurMacMode.EQ.EQ_LEVEL_ZERO) {
                res = true;
            }
        }
        return res;
    }

    private String ChangeGainValume(int num) {
        //System.out.println("ChangeValume:"+num);
        String show = null;
        DecimalFormat decimalFormat = new DecimalFormat("0.0");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        show = decimalFormat.format(0.0 - (DataStruct.CurMacMode.EQ.EQ_Gain_MAX / 2 - num) / 10.0);//format 返回的是字符串
        show = show + "dB";
        return show;
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        curP.x = event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                downP.x = event.getX();

                break;
            case MotionEvent.ACTION_MOVE:
                if (curP.x- downP.x > 500) {

                    Intent intentw = new Intent();
                    intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
                    intentw.putExtra("msg", Define.BoardCast_FlashUI_EQ);
                    mContext.sendBroadcast(intentw);

                    finish();
                }

                break;
            case MotionEvent.ACTION_UP:

                break;

            default:
                break;
        }

        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
