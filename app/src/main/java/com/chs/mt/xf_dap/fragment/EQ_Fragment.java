package com.chs.mt.xf_dap.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chs.mt.xf_dap.R;
import com.chs.mt.xf_dap.datastruct.DataStruct;
import com.chs.mt.xf_dap.datastruct.Define;
import com.chs.mt.xf_dap.datastruct.MacCfg;
import com.chs.mt.xf_dap.fragment.dialogFragment.AlertDialogFragment;
import com.chs.mt.xf_dap.fragment.dialogFragment.LoadingDialogFragment;
import com.chs.mt.xf_dap.fragment.dialogFragment.SetEQFreqBWGainDialogFragment;
import com.chs.mt.xf_dap.main.EQ_GenterActivity;
import com.chs.mt.xf_dap.main.MainTURTActivity;
import com.chs.mt.xf_dap.operation.DataOptUtil;
import com.chs.mt.xf_dap.operation.ReturnFuncation;
import com.chs.mt.xf_dap.tools.EQ;
import com.chs.mt.xf_dap.tools.EQ_SeekBar;
import com.chs.mt.xf_dap.tools.wheel.WheelView;
import com.chs.mt.xf_dap.viewItem.V_EQ_Item;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class EQ_Fragment extends Fragment {
    private Toast mToast;
    private static Context mContext;
    //EQ
    private V_EQ_Item[] EQItem = new V_EQ_Item[31];
    private EQ toneHomeEq;
    private TextView TV_EQGainMax, TV_EQGainMin;//TV_EQ_Num
    private List<String> Channellists;
    private WheelView WV_OutVa;

    private boolean bool_EQ_ModeFlag = false;
    private Button B_EQSetEQMode, B_EQSetDefault, B_EQSetRecover;
    private boolean bool_ByPass = false;

    //道选择
    private static final int UI_OUT_CH_MAX = 6;

    private LinearLayout LY_EQCH6, LY_EQCH8, LY_EQCH10, LY_EQCH12;
    private Button[] B_EQCH6_Ch = new Button[6];
    private Button[] B_EQCH8_Ch = new Button[8];
    private Button[] B_EQCH10_Ch = new Button[10];
    private Button[] B_EQCH12_Ch = new Button[12];
    private View LY_EQSel;
    //通道选择
//    private Button[] B_Channel = new Button[UI_OUT_CH_MAX];
//    private View[] B_ChannelView = new View[UI_OUT_CH_MAX];

    //对话框
    private LoadingDialogFragment mLoadingDialogFragment = null;
    private AlertDialogFragment alertDialogFragment = null;
    private SetEQFreqBWGainDialogFragment setEQFreqBWGainDialogFragment = null;
//    private SetEQFreqBWGainMVNSBDialogFragment setEQFreqBWGainMVNSBDialogFragment=null;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity().getApplicationContext();
        View view = inflater.inflate(R.layout.chs_fragment_eq, container, false);
        initData();
        initView(view);
        initClick();
        return view;
    }

    private void initView(View view) {
        initChannelSel(view);
        AddViewEqualizer_Pager(view);
    }

    private void initData() {

    }

    private void initClick() {

    }

    /**
     * 消息提示
     */
    private void ToastMsg(String Msg) {
        if (null != mToast) {
            mToast.setText(Msg);
        } else {
            mToast = Toast.makeText(mContext, Msg, Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    private void flashLinkDataUI(int Tpe) {
        MacCfg.UI_Type = Tpe;
        DataOptUtil.syncLinkData();

    }



    private void showLoadingDialog() {
        if (mLoadingDialogFragment == null) {
            mLoadingDialogFragment = new LoadingDialogFragment();
        }
        if (!mLoadingDialogFragment.isAdded()) {
            mLoadingDialogFragment.show(getActivity().getFragmentManager(), "mLoadingDialogFragment");
        }

    }
    /*********************************************************************/
    /***************************    initChannelSel     ****************************/
    /*********************************************************************/
    private void initChannelSel(View view) {



        WV_OutVa = (WheelView) view.findViewById(R.id.id_eq_va_wheelview);



//        LY_EQSel = view.findViewById(R.id.id_channel_sel_eq);
//
//        LY_EQCH6 = (LinearLayout) LY_EQSel.findViewById(R.id.id_ly_channel_6);
//        LY_EQCH8 = (LinearLayout) LY_EQSel.findViewById(R.id.id_ly_channel_8);
//        LY_EQCH10 = (LinearLayout) LY_EQSel.findViewById(R.id.id_ly_channel_10);
//        LY_EQCH12 = (LinearLayout) LY_EQSel.findViewById(R.id.id_ly_channel_12);
//
//        B_EQCH6_Ch[0] = (Button) LY_EQSel.findViewById(R.id.id_b_channel6_0);
//        B_EQCH6_Ch[1] = (Button) LY_EQSel.findViewById(R.id.id_b_channel6_1);
//        B_EQCH6_Ch[2] = (Button) LY_EQSel.findViewById(R.id.id_b_channel6_2);
//        B_EQCH6_Ch[3] = (Button) LY_EQSel.findViewById(R.id.id_b_channel6_3);
//        B_EQCH6_Ch[4] = (Button) LY_EQSel.findViewById(R.id.id_b_channel6_4);
//        B_EQCH6_Ch[5] = (Button) LY_EQSel.findViewById(R.id.id_b_channel6_5);
//
//        B_EQCH8_Ch[0] = (Button) LY_EQSel.findViewById(R.id.id_b_channel8_0);
//        B_EQCH8_Ch[1] = (Button) LY_EQSel.findViewById(R.id.id_b_channel8_1);
//        B_EQCH8_Ch[2] = (Button) LY_EQSel.findViewById(R.id.id_b_channel8_2);
//        B_EQCH8_Ch[3] = (Button) LY_EQSel.findViewById(R.id.id_b_channel8_3);
//        B_EQCH8_Ch[4] = (Button) LY_EQSel.findViewById(R.id.id_b_channel8_4);
//        B_EQCH8_Ch[5] = (Button) LY_EQSel.findViewById(R.id.id_b_channel8_5);
//        B_EQCH8_Ch[6] = (Button) LY_EQSel.findViewById(R.id.id_b_channel8_6);
//        B_EQCH8_Ch[7] = (Button) LY_EQSel.findViewById(R.id.id_b_channel8_7);
//
//        B_EQCH10_Ch[0] = (Button) LY_EQSel.findViewById(R.id.id_b_channel10_0);
//        B_EQCH10_Ch[1] = (Button) LY_EQSel.findViewById(R.id.id_b_channel10_1);
//        B_EQCH10_Ch[2] = (Button) LY_EQSel.findViewById(R.id.id_b_channel10_2);
//        B_EQCH10_Ch[3] = (Button) LY_EQSel.findViewById(R.id.id_b_channel10_3);
//        B_EQCH10_Ch[4] = (Button) LY_EQSel.findViewById(R.id.id_b_channel10_4);
//        B_EQCH10_Ch[5] = (Button) LY_EQSel.findViewById(R.id.id_b_channel10_5);
//        B_EQCH10_Ch[6] = (Button) LY_EQSel.findViewById(R.id.id_b_channel10_6);
//        B_EQCH10_Ch[7] = (Button) LY_EQSel.findViewById(R.id.id_b_channel10_7);
//        B_EQCH10_Ch[8] = (Button) LY_EQSel.findViewById(R.id.id_b_channel10_8);
//        B_EQCH10_Ch[9] = (Button) LY_EQSel.findViewById(R.id.id_b_channel10_9);
//
//        B_EQCH12_Ch[0] = (Button) LY_EQSel.findViewById(R.id.id_b_channel12_0);
//        B_EQCH12_Ch[1] = (Button) LY_EQSel.findViewById(R.id.id_b_channel12_1);
//        B_EQCH12_Ch[2] = (Button) LY_EQSel.findViewById(R.id.id_b_channel12_2);
//        B_EQCH12_Ch[3] = (Button) LY_EQSel.findViewById(R.id.id_b_channel12_3);
//        B_EQCH12_Ch[4] = (Button) LY_EQSel.findViewById(R.id.id_b_channel12_4);
//        B_EQCH12_Ch[5] = (Button) LY_EQSel.findViewById(R.id.id_b_channel12_5);
//        B_EQCH12_Ch[6] = (Button) LY_EQSel.findViewById(R.id.id_b_channel12_6);
//        B_EQCH12_Ch[7] = (Button) LY_EQSel.findViewById(R.id.id_b_channel12_7);
//        B_EQCH12_Ch[8] = (Button) LY_EQSel.findViewById(R.id.id_b_channel12_8);
//        B_EQCH12_Ch[9] = (Button) LY_EQSel.findViewById(R.id.id_b_channel12_9);
//        B_EQCH12_Ch[10] = (Button) LY_EQSel.findViewById(R.id.id_b_channel12_10);
//        B_EQCH12_Ch[11] = (Button) LY_EQSel.findViewById(R.id.id_b_channel12_11);
//
//        LY_EQCH6.setVisibility(View.GONE);
//        LY_EQCH8.setVisibility(View.VISIBLE);
//        LY_EQCH10.setVisibility(View.GONE);
//        LY_EQCH12.setVisibility(View.GONE);
//
//        for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX_USE; i++) {
//            B_EQCH8_Ch[i].setText(DataStruct.CurMacMode.Out.Name[i]);
//        }


        //TODO
        /*
        B_Channel[0] = (Button)view.findViewById(R.id.id_b_chh0);
        B_Channel[1] = (Button)view.findViewById(R.id.id_b_chh1);
        B_Channel[2] = (Button)view.findViewById(R.id.id_b_chh2);
        B_Channel[3] = (Button)view.findViewById(R.id.id_b_chh3);
        B_Channel[4] = (Button)view.findViewById(R.id.id_b_chh4);
        B_Channel[5] = (Button)view.findViewById(R.id.id_b_chh5);

        B_ChannelView[0] = (View)view.findViewById(R.id.id_v0);
        B_ChannelView[1] = (View)view.findViewById(R.id.id_v1);
        B_ChannelView[2] = (View)view.findViewById(R.id.id_v2);
        B_ChannelView[3] = (View)view.findViewById(R.id.id_v3);
        B_ChannelView[4] = (View)view.findViewById(R.id.id_v4);
        B_ChannelView[5] = (View)view.findViewById(R.id.id_v5);


        for(int i=0;i<UI_OUT_CH_MAX;i++){
            B_Channel[i].setTag(i);
            B_Channel[i].setText(DataStruct.CurMacMode.Out.Name[i]);
            B_Channel[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    MacCfg.OutputChannelSel = (int)v.getTag();
                    FlashChannelUI();
                }
            });
        }
        */
    }

    private void flashChannelSel() {
//        if (DataStruct.CurMacMode.Out.OUT_CH_MAX_USE == 6) {
//            for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX_USE; i++) {
//                B_EQCH6_Ch[i].setBackgroundResource(R.drawable.chs_btn_channel_sel_normal);
//                B_EQCH6_Ch[i].setTextColor(getResources().getColor(R.color.output_channel_set_text_color_normal));
//            }
//            B_EQCH6_Ch[MacCfg.OutputChannelSel].setBackgroundResource(R.drawable.chs_btn_channel_sel_press);
//            B_EQCH6_Ch[MacCfg.OutputChannelSel].setTextColor(getResources().getColor(R.color.output_channel_set_text_color_press));
//        } else if (DataStruct.CurMacMode.Out.OUT_CH_MAX_USE == 8) {
//            for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX_USE; i++) {
//                B_EQCH8_Ch[i].setBackgroundResource(R.drawable.chs_btn_channel_sel_normal);
//                B_EQCH8_Ch[i].setTextColor(getResources().getColor(R.color.output_channel_set_text_color_normal));
//            }
//            B_EQCH8_Ch[MacCfg.OutputChannelSel].setBackgroundResource(R.drawable.chs_btn_channel_sel_press);
//            B_EQCH8_Ch[MacCfg.OutputChannelSel].setTextColor(getResources().getColor(R.color.output_channel_set_text_color_press));
//        } else if (DataStruct.CurMacMode.Out.OUT_CH_MAX_USE == 10) {
//            for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX_USE; i++) {
//                B_EQCH10_Ch[i].setBackgroundResource(R.drawable.chs_btn_channel_sel_normal);
//                B_EQCH10_Ch[i].setTextColor(getResources().getColor(R.color.output_channel_set_text_color_normal));
//            }
//            B_EQCH10_Ch[MacCfg.OutputChannelSel].setBackgroundResource(R.drawable.chs_btn_channel_sel_press);
//            B_EQCH10_Ch[MacCfg.OutputChannelSel].setTextColor(getResources().getColor(R.color.output_channel_set_text_color_press));
//        } else if (DataStruct.CurMacMode.Out.OUT_CH_MAX_USE == 12) {
//            for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX_USE; i++) {
//                B_EQCH12_Ch[i].setBackgroundResource(R.drawable.chs_btn_channel_sel_normal);
//                B_EQCH12_Ch[i].setTextColor(getResources().getColor(R.color.output_channel_set_text_color_normal));
//            }
//            B_EQCH12_Ch[MacCfg.OutputChannelSel].setBackgroundResource(R.drawable.chs_btn_channel_sel_press);
//            B_EQCH12_Ch[MacCfg.OutputChannelSel].setTextColor(getResources().getColor(R.color.output_channel_set_text_color_press));
//        }
//
//        for (int i = 0; i < 6; i++) {
//            B_EQCH6_Ch[i].setTag(i);
//            B_EQCH6_Ch[i].setOnClickListener(new OnClickListener() {
//
//                @Override
//                public void onClick(View view) {
//                    MacCfg.OutputChannelSel = (int) view.getTag();
//                    FlashChannelUI();
//                }
//            });
//        }
//
//        for (int i = 0; i < 8; i++) {
//            B_EQCH8_Ch[i].setTag(i);
//            B_EQCH8_Ch[i].setOnClickListener(new OnClickListener() {
//
//                @Override
//                public void onClick(View view) {
//                    MacCfg.OutputChannelSel = (int) view.getTag();
//                    FlashChannelUI();
//                }
//            });
//        }
//
//        for (int i = 0; i < 10; i++) {
//            B_EQCH10_Ch[i].setTag(i);
//            B_EQCH10_Ch[i].setOnClickListener(new OnClickListener() {
//
//                @Override
//                public void onClick(View view) {
//                    MacCfg.OutputChannelSel = (int) view.getTag();
//                    FlashChannelUI();
//                }
//            });
//        }
//
//        for (int i = 0; i < 12; i++) {
//            B_EQCH12_Ch[i].setTag(i);
//            B_EQCH12_Ch[i].setOnClickListener(new OnClickListener() {
//
//                @Override
//                public void onClick(View view) {
//                    MacCfg.OutputChannelSel = (int) view.getTag();
//                    FlashChannelUI();
//                }
//            });
//        }
        /*
        for(int i=0;i<UI_OUT_CH_MAX;i++){
            B_Channel[i].setTextColor(getResources().getColor(R.color.outputCH_normal_text_color));
            B_ChannelView[i].setVisibility(View.GONE);
        }
        B_Channel[MacCfg.OutputChannelSel].setTextColor(getResources().getColor(R.color.outputCH_press_text_color));
        B_ChannelView[MacCfg.OutputChannelSel].setVisibility(View.VISIBLE);
        */
    }

    public void InitLoadPageUI() {
        AddViewEqualizerListen_Pager();
        FlashPageUI();
    }

    public void FlashPageUI() {

        Channellists = new ArrayList<String>();
        for(int i = 0; i < MacCfg.OutMax; i++){
            Channellists.add(DataStruct.CurMacMode.Out.Name[i]);
        }
      //  System.out.println("BUG 进来啊");
     //   try {
        ReturnFuncation.CurrentOutputChannel();

        WV_OutVa.lists(Channellists).showCount(5).selectTip("").select(MacCfg.OutputChannelSel).listener(new WheelView.OnWheelViewItemSelectListener() {
            @Override
            public void onItemSelect(int index,boolean fromUser) {
                if(!fromUser){
                    MacCfg.OutputChannelSel = index;
                    ReturnFuncation.CurrentOutputChannel();
                    MainTURTActivity parentActivity = (MainTURTActivity) getActivity();
                    parentActivity.setLinkText();
                    FlashPageUI();
                }
            }
        }).build();
        WV_OutVa.setIndex(MacCfg.OutputChannelSel);
//        }catch (Exception e){
//e.printStackTrace();
//        }


//        if (DataStruct.CurMacMode.BOOL_ENCRYPTION) {
//            if (MacCfg.bool_Encryption) {
//                encryption.setVisibility(View.VISIBLE);
//                for (int i = 0; i < Define.MAX_CHEQ; i++) {
//                    EQItem[i].MVS_SeekBar.setProgress(DataStruct.DefaultDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].level - DataStruct.CurMacMode.EQ.EQ_LEVEL_MIN);
//                    EQItem[i].B_Gain.setText(ChangeGainValume(DataStruct.DefaultDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].level - DataStruct.CurMacMode.EQ.EQ_LEVEL_MIN));
//                    EQItem[i].B_BW.setText(ChangeBWValume(DataStruct.DefaultDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].bw));
//                    EQItem[i].B_Freq.setText(String.valueOf(DataStruct.DefaultDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].freq) + "Hz");
//                }
//                toneHomeEq.SetEQData(DataStruct.DefaultDeviceData.OUT_CH[MacCfg.OutputChannelSel]);
//            } else {
//                encryption.setVisibility(View.GONE);
//                FlashChannelUI();
//            }
//        } else {
          //  encryption.setVisibility(View.GONE);
            FlashChannelUI();
      //  }
    }
    /*********************************************************************/
    /***************************    EQ     ****************************/
    /*********************************************************************/
    private void AddViewEqualizer_Pager(View viewEQPage) {
        toneHomeEq = (EQ) viewEQPage.findViewById(R.id.id_eq_eqfilter_page);
        toneHomeEq.SetEQData(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel]);
        B_EQSetRecover = (Button) viewEQPage.findViewById(R.id.id_b_eq_recover);
        B_EQSetDefault = (Button) viewEQPage.findViewById(R.id.id_b_eq_reset);
        B_EQSetEQMode = (Button) viewEQPage.findViewById(R.id.id_b_eq_mode);

        TV_EQGainMax = (TextView) viewEQPage.findViewById(R.id.id_tv_equalizer_eq_gainmax);
        TV_EQGainMin = (TextView) viewEQPage.findViewById(R.id.id_tv_equalizer_eq_gainmin);
        TV_EQGainMax.setText("+" + String.valueOf(DataStruct.CurMacMode.EQ.EQ_Gain_MAX / 20) + "dB");
        TV_EQGainMin.setText("-" + String.valueOf(DataStruct.CurMacMode.EQ.EQ_Gain_MAX / 20) + "dB");

        EQItem[0] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_1);
        EQItem[1] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_2);
        EQItem[2] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_3);
        EQItem[3] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_4);
        EQItem[4] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_5);
        EQItem[5] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_6);
        EQItem[6] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_7);
        EQItem[7] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_8);
        EQItem[8] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_9);
        EQItem[9] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_10);

        EQItem[10] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_11);
        EQItem[11] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_12);
        EQItem[12] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_13);
        EQItem[13] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_14);
        EQItem[14] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_15);
        EQItem[15] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_16);
        EQItem[16] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_17);
        EQItem[17] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_18);
        EQItem[18] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_19);
        EQItem[19] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_20);

        EQItem[20] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_21);
        EQItem[21] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_22);
        EQItem[22] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_23);
        EQItem[23] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_24);
        EQItem[24] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_25);
        EQItem[25] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_26);
        EQItem[26] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_27);
        EQItem[27] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_28);
        EQItem[28] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_29);
        EQItem[29] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_30);
        EQItem[30] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_31);


        AddViewEqualizerListen_Pager();
    }

    private void ModeChange(){
        Bundle bundle = new Bundle();
        bundle.putInt(AlertDialogFragment.ST_DataOPT, 0);
        bundle.putString(AlertDialogFragment.ST_SetMessage, getResources().getString(R.string.ModeChange));

        if (alertDialogFragment == null) {
            alertDialogFragment = new AlertDialogFragment();
        }
        if (!alertDialogFragment.isAdded()) {
            alertDialogFragment.setArguments(bundle);
            alertDialogFragment.show(getActivity().getFragmentManager(), "alertDialogFragment");
        }
        alertDialogFragment.OnSetOnClickDialogListener(new AlertDialogFragment.SetOnClickDialogListener() {
            @Override
            public void onClickDialogListener(int type, boolean boolClick) {
                if (boolClick) {
                    DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].eq_mode=1;
                    Set3BClick(true);
                    RestoreEQTo_EQ_Buf_Form_EQ_Default();
                    FlashChannelUI();
                    //联调
                    flashLinkDataUI(Define.UI_EQ_G_P_MODE_EQ);
                    setEQColor(MacCfg.EQ_Num);
                    ResetEQColor(MacCfg.EQ_Num);
                    //ResetEQColor();
                    flashLinkDataUI(Define.UI_EQ_ALL);
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), EQ_GenterActivity.class);
                    getActivity().startActivity(intent);
                }
            }
        });
    }

    private void AddViewEqualizerListen_Pager() {
        //初始化
        for (int i = 0; i < Define.MAX_CHEQ; i++) {
            EQItem[i].setVisibility(View.GONE);
            EQItem[i].MVS_SeekBar.setDrag(true);
        }
        for (int j = 0; j < DataStruct.CurMacMode.Out.OUT_CH_MAX_USE; j++) {
                for (int i = 0; i < DataStruct.CurMacMode.EQ.Max_EQ; i++) {
                    EQItem[i].setVisibility(View.VISIBLE);
                    EQItem[i].B_ID.setText(String.valueOf(i + 1));
                    EQItem[i].MVS_SeekBar.setProgressMax(Define.EQ_BW_MAX);//DataStruct.CurMacMode.EQ.EQ_Gain_MAX
                }
        }
        FlashChannelUI();
        toneHomeEq.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].eq_mode==0){
                    ModeChange();
                }else {

                    Intent intent = new Intent();
                    intent.setClass(getActivity(), EQ_GenterActivity.class);
                    getActivity().startActivity(intent);
                }

            }
        });

        /**/
        B_EQSetRecover.setOnClickListener(new OnClickListener() {
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
                    alertDialogFragment.show(getActivity().getFragmentManager(), "alertDialogFragment");
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
//        B_EQSetRecover.setOnTouchListener(new OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN://按住事件发生后执行代码的区域
//                        B_EQSetRecover.setTextColor(getResources().getColor(R.color.eq_page_text_eq_set_press));
//                        B_EQSetRecover.setBackgroundResource(R.drawable.chs_btn_eq_set_press);
//                        break;
//                    case MotionEvent.ACTION_MOVE://移动事件发生后执行代码的区域
//                        B_EQSetRecover.setTextColor(getResources().getColor(R.color.eq_page_text_eq_set_press));
//                        B_EQSetRecover.setBackgroundResource(R.drawable.chs_btn_eq_set_press);
//                        break;
//                    case MotionEvent.ACTION_UP://松开事件发生后执行代码的区域
//                        B_EQSetRecover.setTextColor(getResources().getColor(R.color.eq_page_text_eq_set_normal));
//                        B_EQSetRecover.setBackgroundResource(R.drawable.chs_btn_eq_set_normal);
////
////                        if((!ByEQPass())&&(ByEQPassStore())){
////
////                        }else{
//
////                        }
//
//                        break;
//                    default:
//                        B_EQSetRecover.setBackgroundResource(R.drawable.chs_btn_eq_set_normal);
//                        B_EQSetRecover.setTextColor(getResources().getColor(R.color.eq_page_text_eq_set_normal));
//                        break;
//                }
//                return false;
//            }
//        });
        B_EQSetDefault.setOnClickListener(new OnClickListener() {
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
                    alertDialogFragment.show(getActivity().getFragmentManager(), "alertDialogFragment");
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

//        B_EQSetDefault.setOnTouchListener(new OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN://按住事件发生后执行代码的区域
//                        B_EQSetDefault.setTextColor(getResources().getColor(R.color.eq_page_text_eq_set_press));
//                        B_EQSetDefault.setBackgroundResource(R.drawable.chs_btn_eq_set_press);
//                        break;
//                    case MotionEvent.ACTION_MOVE://移动事件发生后执行代码的区域
//                        B_EQSetDefault.setTextColor(getResources().getColor(R.color.eq_page_text_eq_set_press));
//                        B_EQSetDefault.setBackgroundResource(R.drawable.chs_btn_eq_set_press);
//                        break;
//                    case MotionEvent.ACTION_UP://松开事件发生后执行代码的区域
//                        B_EQSetDefault.setTextColor(getResources().getColor(R.color.eq_page_text_eq_set_normal));
//                        B_EQSetDefault.setBackgroundResource(R.drawable.chs_btn_eq_set_normal);
//
//                        Bundle bundle = new Bundle();
//                        bundle.putInt(AlertDialogFragment.ST_DataOPT, 0);
//                        bundle.putString(AlertDialogFragment.ST_SetMessage, getResources().getString(R.string.ResetEQ));
//
//                        if(alertDialogFragment == null){
//                            alertDialogFragment= new AlertDialogFragment();
//                        }
//                        if (!alertDialogFragment.isAdded()) {
//                            alertDialogFragment.setArguments(bundle);
//                            alertDialogFragment.show(getActivity().getFragmentManager(), "alertDialogFragment");
//                        }
//
//                        alertDialogFragment.OnSetOnClickDialogListener(new AlertDialogFragment.SetOnClickDialogListener() {
//
//                            @Override
//                            public void onClickDialogListener(int type, boolean boolClick) {
//                                if(boolClick){
//                                    Set_Default();
//                                }
//                            }
//                        });
//                        break;
//                    default:
//                        B_EQSetDefault.setBackgroundResource(R.drawable.chs_btn_eq_set_normal);
//                        B_EQSetDefault.setTextColor(getResources().getColor(R.color.eq_page_text_eq_set_normal));
//                        break;
//                }
//                return false;
//            }
//        });

        B_EQSetEQMode.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                if (DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].eq_mode == 1) {
                    DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].eq_mode = 0;
                    B_EQSetEQMode.setText(R.string.PEQ_MODE);
                    bool_EQ_ModeFlag = false;
                    B_EQSetEQMode.setBackgroundResource(R.drawable.chs_btn_eq_set_press);
//                    B_EQSetEQMode.setTextColor(getResources().getColor(R.color.eq_page_text_eq_set_press));
                    //LY_EQ_Mode.setVisibility(View.VISIBLE);
                } else if (DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].eq_mode == 0) {
                    DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].eq_mode = 1;
                    bool_EQ_ModeFlag = true;
                    B_EQSetEQMode.setText(R.string.GEQ_MODE);
                    B_EQSetEQMode.setBackgroundResource(R.drawable.chs_btn_eq_set_normal);
//                    B_EQSetEQMode.setTextColor(getResources().getColor(R.color.eq_page_text_eq_set_normal));
                    //LY_EQ_Mode.setVisibility(View.INVISIBLE);
                }
                Set3BClick(bool_EQ_ModeFlag);
                RestoreEQTo_EQ_Buf_Form_EQ_Default();
                FlashChannelUI();
                //联调
                flashLinkDataUI(Define.UI_EQ_G_P_MODE_EQ);
                setEQColor(MacCfg.EQ_Num);
                ResetEQColor(MacCfg.EQ_Num);
                //ResetEQColor();
                flashLinkDataUI(Define.UI_EQ_ALL);
            }
        });

        for (int i = 0; i <31; i++) {
            EQItem[i].MVS_SeekBar.setProgressMax(260);//DataStruct.CurMacMode.EQ.EQ_Gain_MAX
            //EQItem[i].B_Gain.setClickable(false);
            //EQItem[i].B_Freq.setClickable(false);
            //EQItem[i].B_BW.setClickable(false);
            EQItem[i].MVS_SeekBar.setTag(i);
            EQItem[i].MVS_SeekBar.setOnSeekBarChangeListener(new EQ_SeekBar.OnMSBEQSeekBarChangeListener() {
                @Override
                public void onProgressChanged(EQ_SeekBar mvs_SeekBar, int progress, boolean fromUser) {
                    //根据fromUser解锁onTouchEvent(MotionEvent event)可以传到父控制，或者消费掉MotionEvent
                    //VP_CHS_Pager.setNoScrollOnIntercept(fromUser);
                    MacCfg.EQ_Num = (int) mvs_SeekBar.getTag();
                    FlashEQLevel(progress);
                    setEQColor(MacCfg.EQ_Num);
                }
            });
            EQItem[i].MVS_SeekBar.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });


            //B_Gain
            EQItem[i].B_Gain.setTag(i);
            EQItem[i].B_Gain.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {


                    MacCfg.EQ_Num = (int) view.getTag();
                    setEQColor(MacCfg.EQ_Num);
                    Bundle bundle = new Bundle();
                    bundle.putInt(SetEQFreqBWGainDialogFragment.ST_DataOPT, SetEQFreqBWGainDialogFragment.DataOPT_Gain);
                    bundle.putInt(SetEQFreqBWGainDialogFragment.ST_Data,
                            DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel]
                                    .EQ[MacCfg.EQ_Num].level - DataStruct.CurMacMode.EQ.EQ_LEVEL_MIN);
                    bundle.putInt(SetEQFreqBWGainDialogFragment.ST_DataNUM, MacCfg.EQ_Num);

                    if (setEQFreqBWGainDialogFragment == null) {
                        setEQFreqBWGainDialogFragment = new SetEQFreqBWGainDialogFragment();
                    }
                    if (!setEQFreqBWGainDialogFragment.isAdded()) {
                        setEQFreqBWGainDialogFragment.setArguments(bundle);
                        setEQFreqBWGainDialogFragment.show(getActivity().getFragmentManager(), "setEQFreqBWGainDialogFragment");
                    }


                    setEQFreqBWGainDialogFragment.OnSetEQFreqBWGainDialogFragmentChangeListener(new SetEQFreqBWGainDialogFragment.OnEQFreqBWGainDialogFragmentChangeListener() {

                        @Override
                        public void onGainSeekBarListener(int Gain, int type, boolean flag) {
                            //刷新图表
                            FlashEQLevel(Gain);
                            EQItem[MacCfg.EQ_Num].MVS_SeekBar.setProgress(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[MacCfg.EQ_Num].level - DataStruct.CurMacMode.EQ.EQ_LEVEL_MIN);
                            //联调
                            flashLinkDataUI(Define.UI_EQ_Level);
                        }

                        @Override
                        public void onFreqSeekBarListener(int Freq, int type, boolean flag) {
                        }

                        @Override
                        public void onBWSeekBarListener(int BW, int type, boolean flag) {
                        }
                    });
                }
            });

            //BW
            EQItem[i].B_BW.setTag(i);
            EQItem[i].B_BW.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    if (DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].eq_mode == 1) {
                        return;
                    }
                    MacCfg.EQ_Num = (int) view.getTag();
                    setEQColor(MacCfg.EQ_Num);


                    Bundle bundle = new Bundle();
                    bundle.putInt(SetEQFreqBWGainDialogFragment.ST_DataOPT, SetEQFreqBWGainDialogFragment.DataOPT_BW);
                    bundle.putInt(SetEQFreqBWGainDialogFragment.ST_Data, DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[MacCfg.EQ_Num].bw);
                    bundle.putInt(SetEQFreqBWGainDialogFragment.ST_DataNUM, MacCfg.EQ_Num);

                    if (setEQFreqBWGainDialogFragment == null) {
                        setEQFreqBWGainDialogFragment = new SetEQFreqBWGainDialogFragment();
                    }
                    if (!setEQFreqBWGainDialogFragment.isAdded()) {
                        setEQFreqBWGainDialogFragment.setArguments(bundle);
                        setEQFreqBWGainDialogFragment.show(getActivity().getFragmentManager(), "setEQFreqBWGainDialogFragment");
                    }

                    setEQFreqBWGainDialogFragment.OnSetEQFreqBWGainDialogFragmentChangeListener(new SetEQFreqBWGainDialogFragment.OnEQFreqBWGainDialogFragmentChangeListener() {

                        @Override
                        public void onGainSeekBarListener(int Gain, int type, boolean flag) {
                        }

                        @Override
                        public void onFreqSeekBarListener(int Freq, int type, boolean flag) {
                        }

                        @Override
                        public void onBWSeekBarListener(int BW, int type, boolean flag) {
                            DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[MacCfg.EQ_Num].bw = BW;
                            EQItem[MacCfg.EQ_Num].B_BW.setText(ChangeBWValume(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[MacCfg.EQ_Num].bw));
                            FlashEQPageUI();
                            //联调
                            flashLinkDataUI(Define.UI_EQ_BW);
                        }
                    });
                }
            });

            //FREQ
            EQItem[i].B_Freq.setTag(i);
            EQItem[i].B_Freq.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    if (DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].eq_mode == 1) {
                        return;
                    }
                    MacCfg.EQ_Num = (int) view.getTag();
                    setEQColor(MacCfg.EQ_Num);


                    Bundle bundle = new Bundle();
                    bundle.putInt(SetEQFreqBWGainDialogFragment.ST_DataOPT, SetEQFreqBWGainDialogFragment.DataOPT_Freq);
                    bundle.putInt(SetEQFreqBWGainDialogFragment.ST_Data, DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[MacCfg.EQ_Num].freq);
                    bundle.putInt(SetEQFreqBWGainDialogFragment.ST_DataNUM, MacCfg.EQ_Num);

                    if (setEQFreqBWGainDialogFragment == null) {
                        setEQFreqBWGainDialogFragment = new SetEQFreqBWGainDialogFragment();
                    }
                    if (!setEQFreqBWGainDialogFragment.isAdded()) {
                        setEQFreqBWGainDialogFragment.setArguments(bundle);
                        setEQFreqBWGainDialogFragment.show(getActivity().getFragmentManager(), "setEQFreqBWGainDialogFragment");
                    }

                    setEQFreqBWGainDialogFragment.OnSetEQFreqBWGainDialogFragmentChangeListener(new SetEQFreqBWGainDialogFragment.OnEQFreqBWGainDialogFragmentChangeListener() {

                        @Override
                        public void onGainSeekBarListener(int Gain, int type, boolean flag) {
                        }

                        @Override
                        public void onFreqSeekBarListener(int Freq, int type, boolean flag) {
                            DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[MacCfg.EQ_Num].freq = Freq;
                            EQItem[MacCfg.EQ_Num].B_Freq.setText(String.valueOf(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[MacCfg.EQ_Num].freq) + "Hz");
                            //刷新图表
                            FlashEQPageUI();
                            flashLinkDataUI(Define.UI_EQ_Freq);
                        }

                        @Override
                        public void onBWSeekBarListener(int BW, int type, boolean flag) {
                        }
                    });
                }
            });
            EQItem[i].LY_ResetEQ.setTag(i);
            EQItem[i].LY_ResetEQ.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    MacCfg.EQ_Num = (int) view.getTag();

                    if ((DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[MacCfg.EQ_Num].level == DataStruct.CurMacMode.EQ.EQ_LEVEL_ZERO) &&
                            (DataStruct.GainBuf[MacCfg.OutputChannelSel][MacCfg.EQ_Num] != DataStruct.CurMacMode.EQ.EQ_LEVEL_ZERO)) {
                        DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[MacCfg.EQ_Num].level = DataStruct.GainBuf[MacCfg.OutputChannelSel][MacCfg.EQ_Num];
                        DataStruct.GainBuf[MacCfg.OutputChannelSel][MacCfg.EQ_Num] = DataStruct.CurMacMode.EQ.EQ_LEVEL_ZERO;
                        EQItem[MacCfg.EQ_Num].B_ResetEQ.setColorFilter(getResources().getColor(R.color.img_reset));


                    } else if ((DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[MacCfg.EQ_Num].level != DataStruct.CurMacMode.EQ.EQ_LEVEL_ZERO) &&
                            (DataStruct.GainBuf[MacCfg.OutputChannelSel][MacCfg.EQ_Num] == DataStruct.CurMacMode.EQ.EQ_LEVEL_ZERO)) {
                        EQItem[MacCfg.EQ_Num].B_ResetEQ.setColorFilter(getResources().getColor(R.color.transparent));
                        DataStruct.GainBuf[MacCfg.OutputChannelSel][MacCfg.EQ_Num] = DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[MacCfg.EQ_Num].level;
                        DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[MacCfg.EQ_Num].level = DataStruct.CurMacMode.EQ.EQ_LEVEL_ZERO;

                    }

                    //刷新图表
                    EQItem[MacCfg.EQ_Num].MVS_SeekBar.setProgress(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[MacCfg.EQ_Num].level - DataStruct.CurMacMode.EQ.EQ_LEVEL_MIN);
                    EQItem[MacCfg.EQ_Num].B_Gain.setText(ChangeGainValume(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[MacCfg.EQ_Num].level - DataStruct.CurMacMode.EQ.EQ_LEVEL_MIN));
                    FlashEQPageUI();
                    CheckEQByPass();
                    //联调
                    flashLinkDataUI(Define.UI_EQ_Level);
                }
            });
        }
    }


    /*
     * 根据频率值更新
	 */
    int GetFreqDialogSeekBarIndex(int fP) {
        int i;
        int index = 0;
        for (i = 0; i < 240; i++) {
            if ((fP >= Define.FREQ241[i]) && (fP <= Define.FREQ241[i + 1])) {
                index = i + 1;
            }
        }
        return index;
    }

    //------------------------------------
    //获取Equalizer 的EQ的增益数据显示
    private String ChangeGainValume(int num) {
        //System.out.println("ChangeValume:"+num);
        String show = null;
        DecimalFormat decimalFormat = new DecimalFormat("0.0");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        show = decimalFormat.format(0.0 - (DataStruct.CurMacMode.EQ.EQ_Gain_MAX / 2 - num) / 10.0);//format 返回的是字符串
        show = show + "dB";
        return show;
    }

    //获取Equalizer 的EQ的Q值数据显示
    private String ChangeBWValume(int num) {
        if (num > Define.EQ_BW_MAX) {
            num = Define.EQ_BW_MAX;
        }
        String show = null;
        DecimalFormat decimalFormat = new DecimalFormat("0.000");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        show = decimalFormat.format(Define.EQ_BW[num]);//format 返回的是字符串
        return show;
    }

    private void FlashEQPageUI() {
        toneHomeEq.SetEQData(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel]);
    }

    private void FlashEQPageChannelSel() {
        /*
        if(MacCfg.OUT_CH_MAX==6){
            for(int i=0;i<MacCfg.OUT_CH_MAX;i++){
                B_EQCH6_Ch[i].setBackgroundResource(R.drawable.chs_btn_channel_sel_normal);
                B_EQCH6_Ch[i].setTextColor(getResources().getColor(R.color.channelsel_text_color_normal));
            }
            B_EQCH6_Ch[MacCfg.OutputChannelSel].setBackgroundResource(R.drawable.chs_btn_channel_sel_press);
            B_EQCH6_Ch[MacCfg.OutputChannelSel].setTextColor(getResources().getColor(R.color.channelsel_text_color_press));
        }else if(MacCfg.OUT_CH_MAX==8){
            for(int i=0;i<MacCfg.OUT_CH_MAX;i++){
                B_EQCH8_Ch[i].setBackgroundResource(R.drawable.chs_btn_channel_sel_normal);
                B_EQCH8_Ch[i].setTextColor(getResources().getColor(R.color.channelsel_text_color_normal));
            }
            B_EQCH8_Ch[MacCfg.OutputChannelSel].setBackgroundResource(R.drawable.chs_btn_channel_sel_press);
            B_EQCH8_Ch[MacCfg.OutputChannelSel].setTextColor(getResources().getColor(R.color.channelsel_text_color_press));
        }else if(MacCfg.OUT_CH_MAX==10){
            for(int i=0;i<MacCfg.OUT_CH_MAX;i++){
                B_EQCH10_Ch[i].setBackgroundResource(R.drawable.chs_btn_channel_sel_normal);
                B_EQCH10_Ch[i].setTextColor(getResources().getColor(R.color.channelsel_text_color_normal));
            }
            B_EQCH10_Ch[MacCfg.OutputChannelSel].setBackgroundResource(R.drawable.chs_btn_channel_sel_press);
            B_EQCH10_Ch[MacCfg.OutputChannelSel].setTextColor(getResources().getColor(R.color.channelsel_text_color_press));
        }else if(MacCfg.OUT_CH_MAX==12){
            for(int i=0;i<MacCfg.OUT_CH_MAX;i++){
                B_EQCH12_Ch[i].setBackgroundResource(R.drawable.chs_btn_channel_sel_normal);
                B_EQCH12_Ch[i].setTextColor(getResources().getColor(R.color.channelsel_text_color_normal));
            }
            B_EQCH12_Ch[MacCfg.OutputChannelSel].setBackgroundResource(R.drawable.chs_btn_channel_sel_press);
            B_EQCH12_Ch[MacCfg.OutputChannelSel].setTextColor(getResources().getColor(R.color.channelsel_text_color_press));
        }
        */
    }

    public void FlashChannelUI() {
        flashChannelSel();

        //初始化
        /**/
        for (int i = 0; i < Define.MAX_CHEQ; i++) {
            EQItem[i].Lyout_EQItem.setVisibility(View.GONE);
            EQItem[i].setVisibility(View.GONE);
        }


                for (int i = 0; i < DataStruct.CurMacMode.EQ.Max_EQ; i++) {
                    EQItem[i].setVisibility(View.VISIBLE);
                    EQItem[i].Lyout_EQItem.setVisibility(View.VISIBLE);
                    EQItem[i].B_ID.setText(String.valueOf(i + 1));
                    EQItem[i].MVS_SeekBar.setProgressMax(DataStruct.CurMacMode.EQ.EQ_Gain_MAX);//DataStruct.CurMacMode.EQ.EQ_Gain_MAX
//                    if(MacCfg.EQ_Num==i){
//                        EQItem[i].MVS_SeekBar.setBackgroundResource(R.drawable.chs_eq_seekbar_normal);
//                    }else{
//                        EQItem[i].MVS_SeekBar.setBackgroundResource(R.color.nullc);
//                    }

        }





        FlashEQPageChannelSel();

        for (int i = 0; i < Define.MAX_CHEQ; i++) {
            if ((DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].level < DataStruct.CurMacMode.EQ.EQ_LEVEL_MIN)
                    || (DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].level > DataStruct.CurMacMode.EQ.EQ_LEVEL_MAX)) {
                DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].level = DataStruct.CurMacMode.EQ.EQ_LEVEL_ZERO;
            }

            if ((DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].bw < 0)
                    || (DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].bw > Define.EQ_BW_MAX)) {
                DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].bw = 0;
            }


            if ((DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].freq < 20)
                    || (DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].freq > 20000)) {
                DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].freq = 20;
            }
        }
        CheckEQOneByOneLevel();//刷新E单点直通
        CheckEQByPass();//刷新直通和恢复
        setEQColor(MacCfg.EQ_Num);
//        TV_EQ_Num.setText("EQ"+(MacCfg.EQ_Num+1));

        for (int i = 0; i < Define.MAX_CHEQ; i++) {

            EQItem[i].MVS_SeekBar.setProgress(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].level - DataStruct.CurMacMode.EQ.EQ_LEVEL_MIN);
            EQItem[i].B_Gain.setText(ChangeGainValume(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].level - DataStruct.CurMacMode.EQ.EQ_LEVEL_MIN));
            EQItem[i].B_BW.setText(ChangeBWValume(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].bw));
            EQItem[i].B_Freq.setText(String.valueOf(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].freq) + "Hz");
        }
        if (DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].eq_mode == 1) {
            bool_EQ_ModeFlag = true;
            B_EQSetEQMode.setText(R.string.GEQ_MODE);
            B_EQSetEQMode.setBackgroundResource(R.drawable.chs_btn_eq_set_normal);
//            B_EQSetEQMode.setTextColor(getResources().getColor(R.color.eq_page_text_eq_set_normal));
        } else if (DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].eq_mode == 0) {
            bool_EQ_ModeFlag = false;
            B_EQSetEQMode.setText(R.string.PEQ_MODE);
            B_EQSetEQMode.setBackgroundResource(R.drawable.chs_btn_eq_set_press);
//            B_EQSetEQMode.setTextColor(getResources().getColor(R.color.eq_page_text_eq_set_press));
        }
        for (int i = 0; i < DataStruct.CurMacMode.EQ.Max_EQ; i++) {
            DataStruct.GainBuf[MacCfg.OutputChannelSel][i] = DataStruct.CurMacMode.EQ.EQ_LEVEL_ZERO;
        }
//        B_Encryption.setVisibility(View.GONE);
        toneHomeEq.SetEQData(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel]);
    }

    private void Set3BClick(boolean canclick) {
        if (!canclick) {
            for (int i = 0; i < Define.MAX_CHEQ; i++) {
                EQItem[i].B_Freq.setClickable(true);
                EQItem[i].B_BW.setClickable(true);
                EQItem[i].B_Gain.setClickable(true);
            }
        } else {
            for (int i = 0; i < Define.MAX_CHEQ; i++) {
//                EQItem[i].B_Freq.setClickable(false);
//                EQItem[i].B_BW.setClickable(false);
//                EQItem[i].B_Gain.setClickable(false);
            }
        }
    }

    private void ResetEQColor() {
        if (DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].eq_mode == 0) {
            for (int i = 0; i < Define.MAX_CHEQ; i++) {
                EQItem[i].B_Freq.setTextColor(getResources().getColor(R.color.eq_page_item_color_normal));
                EQItem[i].B_BW.setTextColor(getResources().getColor(R.color.eq_page_item_color_normal));
                EQItem[i].B_Gain.setTextColor(getResources().getColor(R.color.eq_page_item_color_normal));
                EQItem[i].B_ID.setTextColor(getResources().getColor(R.color.eq_page_item_color_normal));
            }
        } else {
            for (int i = 0; i < Define.MAX_CHEQ; i++) {
                EQItem[i].B_Freq.setTextColor(getResources().getColor(R.color.eq_page_item_color_lock));
                EQItem[i].B_BW.setTextColor(getResources().getColor(R.color.eq_page_item_color_lock));
                EQItem[i].B_Gain.setTextColor(getResources().getColor(R.color.eq_page_item_color_normal));
                EQItem[i].B_ID.setTextColor(getResources().getColor(R.color.eq_page_item_color_normal));
            }
        }
    }

    private void ResetEQColor(int num) {
        if (DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].eq_mode == 0) {
            //for(int i=0;i<Define.MAX_CHEQ;i++){
            EQItem[num].B_Freq.setTextColor(getResources().getColor(R.color.eq_page_item_color_normal));
            EQItem[num].B_BW.setTextColor(getResources().getColor(R.color.eq_page_item_color_normal));
            EQItem[num].B_Gain.setTextColor(getResources().getColor(R.color.eq_page_item_color_normal));
            EQItem[num].B_ID.setTextColor(getResources().getColor(R.color.eq_page_item_color_normal));
            //}
        } else {
            //for(int i=0;i<Define.MAX_CHEQ;i++){
            EQItem[num].B_Freq.setTextColor(getResources().getColor(R.color.eq_page_item_color_lock));
            EQItem[num].B_BW.setTextColor(getResources().getColor(R.color.eq_page_item_color_lock));
            EQItem[num].B_Gain.setTextColor(getResources().getColor(R.color.eq_page_item_color_normal));
            EQItem[num].B_ID.setTextColor(getResources().getColor(R.color.eq_page_item_color_normal));
            //}
        }
    }

    private void setEQColor(int num) {
        ResetEQColor();
        EQItem[num].B_ID.setTextColor(getResources().getColor(R.color.eq_page_item_color_press));

        for (int i = 0; i <EQItem.length ; i++) {
            EQItem[i].MVS_SeekBar.setBackgroundResource(R.color.nullc);
        }
        
        EQItem[num].MVS_SeekBar.setBackgroundResource(R.drawable.chs_eq_seekbar_normal);
        if (DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].eq_mode == 0) {
            EQItem[num].B_BW.setTextColor(getResources().getColor(R.color.eq_page_item_color_press));
            EQItem[num].B_Gain.setTextColor(getResources().getColor(R.color.eq_page_item_color_press));
            EQItem[num].B_Freq.setTextColor(getResources().getColor(R.color.eq_page_item_color_press));

            //if(MacCfg.EQ_Num==i){
               // EQItem[num].MVS_SeekBar.setBackgroundResource(R.drawable.chs_eq_seekbar_normal);
          //  }else{
               // EQItem[i].MVS_SeekBar.setBackgroundResource(R.color.nullc);
          //  }

        } else {
//			EQItem[num].B_BW.setTextColor(getResources().getColor(R.color.eq_page_item_color_lock));
//			EQItem[num].B_Gain.setTextColor(getResources().getColor(R.color.eq_page_item_color_lock));
//			EQItem[num].B_Freq.setTextColor(getResources().getColor(R.color.eq_page_item_color_lock));

            EQItem[num].B_BW.setTextColor(getResources().getColor(R.color.eq_page_item_color_lock));
            EQItem[num].B_Gain.setTextColor(getResources().getColor(R.color.eq_page_item_color_press));
            EQItem[num].B_Freq.setTextColor(getResources().getColor(R.color.eq_page_item_color_lock));
          //  EQItem[num].MVS_SeekBar.setBackgroundResource(R.color.nullc);
        }

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

    private void SaveEQTo_EQ_Store() {
        for (int j = 0; j < Define.MAX_CHEQ; j++) {
//			BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].freq  = DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].freq;
            DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].level = DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].level;
//			BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].bw    = DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].bw;
//			BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].shf_db= DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].shf_db;
//			BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].type  = DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].type;
        }
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

    private void EQ_StoreTo_Cur() {
        for (int j = 0; j < Define.MAX_CHEQ; j++) {
//			DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].freq  = BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].freq;
            DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].level = DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].level;
//			DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].bw    = BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].bw;
//			DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].shf_db= BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].shf_db;
//			DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].type  = BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].type;
        }
    }

    //恢复出厂数据的界面
    private void Set_Default() {
        RestoreEQTo_EQ_Buf_Form_EQ_Default();
        //联调
        flashLinkDataUI(Define.UI_EQ_ALL);
        //刷新图表
        FlashChannelUI();
        B_EQSetRecover.setText(R.string.Restore_EQ);
    }

    private void CheckEQOneByOneLevel() {
        for (int i = 0; i < Define.MAX_CHEQ; i++) {
            if (DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].level == DataStruct.CurMacMode.EQ.EQ_LEVEL_ZERO) {
                EQItem[i].B_ResetEQ.setColorFilter(getResources().getColor(R.color.transparent));
            } else {
                EQItem[i].B_ResetEQ.setColorFilter(getResources().getColor(R.color.img_reset));
            }
        }
    }

    private void CheckEQByPass() {
        bool_ByPass = ByEQPass();
        if (bool_ByPass) {//可以设置直通
            B_EQSetRecover.setText(R.string.Bypass_EQ);
//            B_EQSetRecover.setTextColor(getResources().getColor(R.color.eq_page_text_eq_set_press));
            B_EQSetRecover.setBackgroundResource(R.drawable.chs_btn_eq_set_press);
        } else if (!bool_ByPass) {//可以设置恢复
            B_EQSetRecover.setText(R.string.Restore_EQ);
//            B_EQSetRecover.setTextColor(getResources().getColor(R.color.eq_page_text_eq_set_normal));
            B_EQSetRecover.setBackgroundResource(R.drawable.chs_btn_eq_set_normal);
        }
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

    private boolean ByEQPassStore() {
        boolean res = false;
        for (int i = 0; i < Define.MAX_CHEQ; i++) {
            if (DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].level != DataStruct.CurMacMode.EQ.EQ_LEVEL_ZERO) {
                res = true;
            }
        }
        return res;
    }

    private void Set_Recover() {
        System.out.println("FUCK Set_Recover");
        if (bool_ByPass) {//设置直通    --  恢复状态
            //保存数据用于恢复
            bool_ByPass = false;
//            for(int j=0;j<Define.MAX_CHEQ;j++){
//                System.out.println("FUCK --ff EQ["+j+"].level="+DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].level);
//            }
            SaveEQTo_EQ_Store();
            for (int i = 0; i < Define.MAX_CHEQ; i++) {
                DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].level = DataStruct.CurMacMode.EQ.EQ_LEVEL_ZERO;
            }
            B_EQSetRecover.setText(R.string.Restore_EQ);
//            for(int j=0;j<Define.MAX_CHEQ;j++){
//                System.out.println("FUCK --buf EQ["+j+"].level="+DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].level);
//            }
        } else if (!bool_ByPass) {//设置恢复
//            for(int j=0;j<Define.MAX_CHEQ;j++){
//                System.out.println("FUCK ##buf EQ["+j+"].level="+DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].level);
//            }
            bool_ByPass = true;
            EQ_StoreTo_Cur();
//            for(int j=0;j<Define.MAX_CHEQ;j++){
//                System.out.println("FUCK ##tt EQ["+j+"].level="+DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].level);
//            }
            B_EQSetRecover.setText(R.string.Bypass_EQ);
        }
        FlashChannelUI();
        //联调
        flashLinkDataUI(Define.UI_EQ_ALL);
    }

    private void FlashEQLevel(int progress) {
        DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[MacCfg.EQ_Num].level = progress + DataStruct.CurMacMode.EQ.EQ_LEVEL_MIN;
        EQItem[MacCfg.EQ_Num].B_Gain.setText(ChangeGainValume(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[MacCfg.EQ_Num].level - DataStruct.CurMacMode.EQ.EQ_LEVEL_MIN));

        DataStruct.GainBuf[MacCfg.OutputChannelSel][MacCfg.EQ_Num] = DataStruct.CurMacMode.EQ.EQ_LEVEL_ZERO;
        DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[MacCfg.EQ_Num].level = progress + DataStruct.CurMacMode.EQ.EQ_LEVEL_MIN;
        EQItem[MacCfg.EQ_Num].B_Gain.setText(ChangeGainValume(progress));
        if (DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[MacCfg.EQ_Num].level == DataStruct.CurMacMode.EQ.EQ_LEVEL_ZERO) {
            EQItem[MacCfg.EQ_Num].B_ResetEQ.setColorFilter(getResources().getColor(R.color.img_reset));
        } else {
            EQItem[MacCfg.EQ_Num].B_ResetEQ.setColorFilter(getResources().getColor(R.color.img_reset));
        }

        CheckEQByPass();//刷新直通和恢复
        FlashEQPageUI();
        //联调
        flashLinkDataUI(Define.UI_EQ_Level);
    }

    @Override
    public void onResume() {
        super.onResume();

      //  FlashChannelUI();
    }
}