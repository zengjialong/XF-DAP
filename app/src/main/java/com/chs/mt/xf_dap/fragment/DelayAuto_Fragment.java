package com.chs.mt.xf_dap.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;

import android.content.Context;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chs.mt.xf_dap.R;
import com.chs.mt.xf_dap.datastruct.DataStruct;
import com.chs.mt.xf_dap.datastruct.MacCfg;
import com.chs.mt.xf_dap.fragment.dialogFragment.SetDelayDialogFragment;
import com.chs.mt.xf_dap.main.MainTURTActivity;
import com.chs.mt.xf_dap.operation.DataOptUtil;
import com.chs.mt.xf_dap.tools.EQ_SeekBar;


public class DelayAuto_Fragment extends Fragment {
    private static String javast = "DelayAuto_Fragment";
    private int mIndex = 0;
    private Toast mToast;
    private static Context mContext;
    private int OUT_CH_MAX_CFG = 12;
    private int SPK_MAX = 24;
    private ArrayList<String> DelayUnit_list;
    private LinearLayout[] LLY_SetDelay_BG = new LinearLayout[OUT_CH_MAX_CFG];//延时通道选中
    private EQ_SeekBar[] SB_SetDelay_SeekBar = new EQ_SeekBar[OUT_CH_MAX_CFG];//延时滑动调节
    private Button B_CM, B_MS, B_Inch;    //延时单位设置

    private LinearLayout ly_unit;    //延时单位设置
    private Button[] B_SetDelay_Show = new Button[SPK_MAX];//延时值显示
    private ImageView[] imageViews_show = new ImageView[SPK_MAX];
    //private Button[] B_SetDelay_speeker = new Button[SPK_MAX];//延时值显示
    private LinearLayout[] LLyout_SetDelay = new LinearLayout[SPK_MAX];//延时值显示

    //private Button encryption;
    /*延时单位切换，1：CM，2：MS，3：Inch*/
    private int DelayUnit = 2;

    int LinkMode = 0;//不联调，1：左右，2整机除了超低
    int[] DelayVal = new int[16];

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity().getApplicationContext();
        View view = inflater.inflate(R.layout.chs_fragment_delay_auto, container, false);
        initView(view);

        return view;
    }

    private void initView(View view) {
        AddViewDelaySettingsPage_Auto(view);
    }

    //////////////////////////////////////////////

    /**
     * 消息提示
     */
    @SuppressWarnings("unused")
    private void ToastMsg(String Msg) {
        if (null != mToast) {
            mToast.setText(Msg);
        } else {
            mToast = Toast.makeText(mContext, Msg, Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    private void AddViewDelaySettingsPage_Auto(View viewDelaySettings) {


        imageViews_show[0] = (ImageView) viewDelaySettings.findViewById(R.id.img_delay_1);
        imageViews_show[1] = (ImageView) viewDelaySettings.findViewById(R.id.img_delay_2);
        imageViews_show[2] = (ImageView) viewDelaySettings.findViewById(R.id.img_delay_3);
        imageViews_show[3] = (ImageView) viewDelaySettings.findViewById(R.id.img_delay_4);
        imageViews_show[4] = (ImageView) viewDelaySettings.findViewById(R.id.img_delay_5);
        imageViews_show[5] = (ImageView) viewDelaySettings.findViewById(R.id.img_delay_6);
        imageViews_show[6] = (ImageView) viewDelaySettings.findViewById(R.id.img_delay_7);
        imageViews_show[7] = (ImageView) viewDelaySettings.findViewById(R.id.img_delay_8);
        imageViews_show[8] = (ImageView) viewDelaySettings.findViewById(R.id.img_delay_9);
        imageViews_show[9] = (ImageView) viewDelaySettings.findViewById(R.id.img_delay_10);
        imageViews_show[10] = (ImageView) viewDelaySettings.findViewById(R.id.img_delay_11);
        imageViews_show[11] = (ImageView) viewDelaySettings.findViewById(R.id.img_delay_12);
        imageViews_show[12] = (ImageView) viewDelaySettings.findViewById(R.id.img_delay_13);
        imageViews_show[13] = (ImageView) viewDelaySettings.findViewById(R.id.img_delay_14);
        imageViews_show[14] = (ImageView) viewDelaySettings.findViewById(R.id.img_delay_15);
        imageViews_show[15] = (ImageView) viewDelaySettings.findViewById(R.id.img_delay_16);
        imageViews_show[16] = (ImageView) viewDelaySettings.findViewById(R.id.img_delay_17);
        imageViews_show[17] = (ImageView) viewDelaySettings.findViewById(R.id.img_delay_18);
        imageViews_show[18] = (ImageView) viewDelaySettings.findViewById(R.id.img_delay_19);
        imageViews_show[19] = (ImageView) viewDelaySettings.findViewById(R.id.img_delay_20);
        imageViews_show[20] = (ImageView) viewDelaySettings.findViewById(R.id.img_delay_21);
        imageViews_show[21] = (ImageView) viewDelaySettings.findViewById(R.id.img_delay_22);
        imageViews_show[22] = (ImageView) viewDelaySettings.findViewById(R.id.img_delay_23);
        imageViews_show[23] = (ImageView) viewDelaySettings.findViewById(R.id.img_delay_24);


        //encryption = (Button) viewDelaySettings.findViewById(R.id.id_b_encryption);
        /*延时值显示*/
        B_SetDelay_Show[0] = (Button) viewDelaySettings.findViewById(R.id.txt_delay_1);
        B_SetDelay_Show[1] = (Button) viewDelaySettings.findViewById(R.id.txt_delay_2);
        B_SetDelay_Show[2] = (Button) viewDelaySettings.findViewById(R.id.txt_delay_3);
        B_SetDelay_Show[3] = (Button) viewDelaySettings.findViewById(R.id.txt_delay_4);
        B_SetDelay_Show[4] = (Button) viewDelaySettings.findViewById(R.id.txt_delay_5);
        B_SetDelay_Show[5] = (Button) viewDelaySettings.findViewById(R.id.txt_delay_6);
        B_SetDelay_Show[6] = (Button) viewDelaySettings.findViewById(R.id.txt_delay_7);
        B_SetDelay_Show[7] = (Button) viewDelaySettings.findViewById(R.id.txt_delay_8);
        B_SetDelay_Show[8] = (Button) viewDelaySettings.findViewById(R.id.txt_delay_9);
        B_SetDelay_Show[9] = (Button) viewDelaySettings.findViewById(R.id.txt_delay_10);
        B_SetDelay_Show[10] = (Button) viewDelaySettings.findViewById(R.id.txt_delay_11);
        B_SetDelay_Show[11] = (Button) viewDelaySettings.findViewById(R.id.txt_delay_12);
        B_SetDelay_Show[12] = (Button) viewDelaySettings.findViewById(R.id.txt_delay_13);
        B_SetDelay_Show[13] = (Button) viewDelaySettings.findViewById(R.id.txt_delay_14);
        B_SetDelay_Show[14] = (Button) viewDelaySettings.findViewById(R.id.txt_delay_15);
        B_SetDelay_Show[15] = (Button) viewDelaySettings.findViewById(R.id.txt_delay_16);
        B_SetDelay_Show[16] = (Button) viewDelaySettings.findViewById(R.id.txt_delay_17);
        B_SetDelay_Show[17] = (Button) viewDelaySettings.findViewById(R.id.txt_delay_18);
        B_SetDelay_Show[18] = (Button) viewDelaySettings.findViewById(R.id.txt_delay_19);
        B_SetDelay_Show[19] = (Button) viewDelaySettings.findViewById(R.id.txt_delay_20);
        B_SetDelay_Show[20] = (Button) viewDelaySettings.findViewById(R.id.txt_delay_21);
        B_SetDelay_Show[21] = (Button) viewDelaySettings.findViewById(R.id.txt_delay_22);
        B_SetDelay_Show[22] = (Button) viewDelaySettings.findViewById(R.id.txt_delay_23);
        B_SetDelay_Show[23] = (Button) viewDelaySettings.findViewById(R.id.txt_delay_24);



        /*延时值显示*/
        LLyout_SetDelay[0] = (LinearLayout) viewDelaySettings.findViewById(R.id.ly_id_1);
        LLyout_SetDelay[1] = (LinearLayout) viewDelaySettings.findViewById(R.id.ly_id_2);
        LLyout_SetDelay[2] = (LinearLayout) viewDelaySettings.findViewById(R.id.ly_id_3);
        LLyout_SetDelay[3] = (LinearLayout) viewDelaySettings.findViewById(R.id.ly_id_4);
        LLyout_SetDelay[4] = (LinearLayout) viewDelaySettings.findViewById(R.id.ly_id_5);
        LLyout_SetDelay[5] = (LinearLayout) viewDelaySettings.findViewById(R.id.ly_id_6);
        LLyout_SetDelay[6] = (LinearLayout) viewDelaySettings.findViewById(R.id.ly_id_7);
        LLyout_SetDelay[7] = (LinearLayout) viewDelaySettings.findViewById(R.id.ly_id_8);
        LLyout_SetDelay[8] = (LinearLayout) viewDelaySettings.findViewById(R.id.ly_id_9);
        LLyout_SetDelay[9] = (LinearLayout) viewDelaySettings.findViewById(R.id.ly_id_10);
        LLyout_SetDelay[10] = (LinearLayout) viewDelaySettings.findViewById(R.id.ly_id_11);
        LLyout_SetDelay[11] = (LinearLayout) viewDelaySettings.findViewById(R.id.ly_id_12);
        LLyout_SetDelay[12] = (LinearLayout) viewDelaySettings.findViewById(R.id.ly_id_13);
        LLyout_SetDelay[13] = (LinearLayout) viewDelaySettings.findViewById(R.id.ly_id_14);
        LLyout_SetDelay[14] = (LinearLayout) viewDelaySettings.findViewById(R.id.ly_id_15);
        LLyout_SetDelay[15] = (LinearLayout) viewDelaySettings.findViewById(R.id.ly_id_16);
        LLyout_SetDelay[16] = (LinearLayout) viewDelaySettings.findViewById(R.id.ly_id_17);
        LLyout_SetDelay[17] = (LinearLayout) viewDelaySettings.findViewById(R.id.ly_id_18);
        LLyout_SetDelay[18] = (LinearLayout) viewDelaySettings.findViewById(R.id.ly_id_19);
        LLyout_SetDelay[19] = (LinearLayout) viewDelaySettings.findViewById(R.id.ly_id_20);
        LLyout_SetDelay[20] = (LinearLayout) viewDelaySettings.findViewById(R.id.ly_id_21);
        LLyout_SetDelay[21] = (LinearLayout) viewDelaySettings.findViewById(R.id.ly_id_22);
        LLyout_SetDelay[22] = (LinearLayout) viewDelaySettings.findViewById(R.id.ly_id_23);
        LLyout_SetDelay[23] = (LinearLayout) viewDelaySettings.findViewById(R.id.ly_id_24);


        //延时单个切换
        B_CM = (Button) viewDelaySettings.findViewById(R.id.id_delay_cm);
        B_MS = (Button) viewDelaySettings.findViewById(R.id.id_delay_ms);
        B_Inch = (Button) viewDelaySettings.findViewById(R.id.id_delay_inch);

        ly_unit = (LinearLayout) viewDelaySettings.findViewById(R.id.id_ly_unit_bg);
//                ly_ms= (LinearLayout) viewDelaySettings.findViewById(R.id.id_ms);
//        ly_inch = (LinearLayout) viewDelaySettings.findViewById(R.id.id_inch);

        AddViewDelaySettingsAutoPageListener();
    }


    /*************************************************************************/
    /***************************** 界面延时设置  *******************************/
    /**********************************TODO***********************************/
    /******* 刷新延时单位   *******/

    public void InitLoadPageUI() {
//        if(B_SetDelay_Show[0] == null){
//            return;
//        }
        AddViewDelaySettingsAutoPageListener();
    }


    public void FlashPageUI() {
        GetChannelNum();

        ((MainTURTActivity)getActivity()).setLinkText();
        setselect();
        UnitDelayTimeShow();
//
        SetDelayUnit();
//
//        if(DataStruct.CurMacMode.CurMacMode == Define.MAC_TYPE_H680){
//            Btn_LinkA.setVisibility(View.VISIBLE);
//            Btn_LinkLR.setVisibility(View.VISIBLE);
//        }else{
//            Btn_LinkA.setVisibility(View.GONE);
//            Btn_LinkLR.setVisibility(View.GONE);
//            LinkMode = 0;
//        }
    }

    private void GetChannelNum() {
        MacCfg.ChannelNumBuf[0] = DataStruct.RcvDeviceData.SYS.out1_spk_type;
        MacCfg.ChannelNumBuf[1] = DataStruct.RcvDeviceData.SYS.out2_spk_type;
        MacCfg.ChannelNumBuf[2] = DataStruct.RcvDeviceData.SYS.out3_spk_type;
        MacCfg.ChannelNumBuf[3] = DataStruct.RcvDeviceData.SYS.out4_spk_type;
        MacCfg.ChannelNumBuf[4] = DataStruct.RcvDeviceData.SYS.out5_spk_type;
        MacCfg.ChannelNumBuf[5] = DataStruct.RcvDeviceData.SYS.out6_spk_type;
        MacCfg.ChannelNumBuf[6] = DataStruct.RcvDeviceData.SYS.out7_spk_type;
        MacCfg.ChannelNumBuf[7] = DataStruct.RcvDeviceData.SYS.out8_spk_type;

        MacCfg.ChannelNumBuf[8] = DataStruct.RcvDeviceData.SYS.out9_spk_type;
        MacCfg.ChannelNumBuf[9] = DataStruct.RcvDeviceData.SYS.out10_spk_type;
        MacCfg.ChannelNumBuf[10] = DataStruct.RcvDeviceData.SYS.out11_spk_type;
        MacCfg.ChannelNumBuf[11] = DataStruct.RcvDeviceData.SYS.out12_spk_type;
        MacCfg.ChannelNumBuf[12] = DataStruct.RcvDeviceData.SYS.out13_spk_type;
        MacCfg.ChannelNumBuf[13] = DataStruct.RcvDeviceData.SYS.out14_spk_type;
        MacCfg.ChannelNumBuf[14] = DataStruct.RcvDeviceData.SYS.out15_spk_type;
        MacCfg.ChannelNumBuf[15] = DataStruct.RcvDeviceData.SYS.Proc_Amp_Mode;

    }

    void UnitDelayTimeShow() {
        FlashDelaySpeaker();
        if (DataStruct.CurMacMode.BOOL_ENCRYPTION) {
            if (MacCfg.bool_Encryption) {
                //encryption.setVisibility(View.VISIBLE);
                for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX_USE; i++) {
                    B_SetDelay_Show[MacCfg.ChannelNumBuf[i] - 1].setText(String.valueOf(ChannelShowDelay(0)));
                }
            } else {
                //  encryption.setVisibility(View.GONE);
                for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX_USE; i++) {
                    if (MacCfg.ChannelNumBuf[i] != 0) {
                        B_SetDelay_Show[MacCfg.ChannelNumBuf[i] - 1].
                                setText(String.valueOf(ChannelShowDelay(DataStruct.RcvDeviceData.OUT_CH[i].delay)));
                    }
                }
            }
        } else {
            //encryption.setVisibility(View.GONE);
            for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX_USE; i++) {
                try {
                    if (MacCfg.ChannelNumBuf[i] != 0) {

                            B_SetDelay_Show[MacCfg.ChannelNumBuf[i] - 1].
                                    setText(String.valueOf(ChannelShowDelay(DataStruct.RcvDeviceData.OUT_CH[i].delay)));

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
    }

    /******* 延时时间转换  *******/
    private String ChannelShowDelay(int timedelay) {
        String delaytimes = new String();
        switch (DelayUnit) {
            case 1:
                delaytimes = DataOptUtil.CountDelayCM(timedelay);
                break;
            case 2:
                delaytimes = DataOptUtil.CountDelayMs(timedelay);
                break;
            case 3:
                delaytimes = DataOptUtil.CountDelayInch(timedelay);
                break;
            default:
                break;
        }
        return delaytimes;
    }




    /*
     * 根据通道输入类型获取延时的显示位置
     */
//    private int GetDelayId(int channelNameId){
//        int id=0;
//        switch (channelNameId) {
//            case 1:
//                id=0;
//                break;
//            //case 2:
//            case 4:
//            case 5:
//            case 6:
//                id=1;
//                break;
//            case 3:
//                id=2;
//                break;
//            case 7:
//                id=3;
//                break;
//            //case 8:
//            case 10:
//            case 11:
//            case 12:
//                id=4;
//                break;
//            case 9:
//                id=5;
//                break;
//            case 13:
//                id=6;
//                break;
//            case 14:
//            case 15:
//                id=7;
//                break;
//            case 16:
//                id=8;
//                break;
//            case 17:
//            case 18:
//                id=9;
//                break;
//            case 19:
//                id=10;
//                break;
//            case 20:
//            case 21:
//                id=11;
//                break;
//            case 22:
//                id=12;
//                break;
//            case 23:
//                id=13;
//                break;
//            case 24:
//                id=14;
//                break;
//            case 25:
//                id=15;
//                break;
//            case 26:
//                id=16;
//                break;
//            case 27:
//                id=17;
//                break;
//            case 28:
//                id=18;
//                break;
//            ///--------------
//            case 2:
//                id=19;
//                break;
//            case 8:
//                id=20;
//                break;
//            default:
//                id=0;
//                break;
//        }
//        return id;
//    }

    void setselect(){
        for (int i = 0; i < MacCfg.OutMax; i++) {
            if(MacCfg.ChannelNumBuf[i] != 0) {
                if (i != MacCfg.OutputChannelSel) {
                    imageViews_show[(MacCfg.ChannelNumBuf[i]) - 1].setSelected(false);
                } else {
                    imageViews_show[(MacCfg.ChannelNumBuf[i]) - 1].setSelected(true);
                }
            }
        }
    }

    void FlashDelaySpeaker() {

        DataOptUtil.getSpkTypeBassVolume();
        HideAllDelaySpeaker();
        try {
            for (int i = 0; i < MacCfg.OutMax; i++) {
                if (MacCfg.ChannelNumBuf[i] != 0) {
                    LLyout_SetDelay[(MacCfg.ChannelNumBuf[i]) - 1].setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void HideAllDelaySpeaker() {
        for (int i = 0; i < SPK_MAX; i++) {
            LLyout_SetDelay[i].setVisibility(View.INVISIBLE);
        }
    }

    /********************************  创建设置延时的SeekBar Dialog  *********************************/

    void SetDelayUnit() {
        B_MS.setTextColor(getResources().getColor(R.color.txt_press));
        B_CM.setTextColor(getResources().getColor(R.color.txt_press));
        B_Inch.setTextColor(getResources().getColor(R.color.txt_press));
        switch (DelayUnit) {
            case 1:
                B_CM.setTextColor(getResources().getColor(R.color.white));
                ly_unit.setBackgroundResource(R.drawable.chs_delay_unit_cm);
                break;
            case 2:
                B_MS.setTextColor(getResources().getColor(R.color.white));
                ly_unit.setBackgroundResource(R.drawable.chs_delay_unit_ms);
                break;
            case 3:
                B_Inch.setTextColor(getResources().getColor(R.color.white));
                ly_unit.setBackgroundResource(R.drawable.chs_delay_unit_inch);
                break;
            default:
                break;
        }
    }

    void AddViewDelaySettingsAutoPageListener() {
        HideAllDelaySpeaker();
        /*点击显示延时数值时弹出SeekBar对话框*/
        for (int i = 0; i < SPK_MAX; i++) {
            LLyout_SetDelay[i].setTag(i);
            B_SetDelay_Show[i].setTag(i);
            imageViews_show[i].setTag(i);


            imageViews_show[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    int sd_num = 0;
                    sd_num = (int) view.getTag();

                    //获取通信编号
                    for (int j = 0; j < DataStruct.CurMacMode.Out.OUT_CH_MAX_USE; j++) {
                        if (MacCfg.ChannelNumBuf[j] - 1 == sd_num) {
                            MacCfg.OutputChannelSel = j;
                            break;
                        }
                    }
                    FlashPageUI();
                    setselect();
                    Bundle Freq = new Bundle();
                    Freq.putInt(SetDelayDialogFragment.ST_Data, DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].delay);
                    Freq.putInt(SetDelayDialogFragment.ST_DelayUnit, DelayUnit);

                    SetDelayDialogFragment setDelayDialogFragment = new SetDelayDialogFragment();
                    setDelayDialogFragment.setArguments(Freq);
                    setDelayDialogFragment.show(getActivity().getFragmentManager(), "SetDelayDialogFragment");
                    setDelayDialogFragment.OnSetDelayDialogFragmentChangeListener(new SetDelayDialogFragment.OnDelayDialogFragmentChangeListener() {

                        @Override
                        public void onDelayVolChangeListener(int delay, int type, boolean flag) {
                            // TODO Auto-generated method stub
                            DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].delay = delay;

                                B_SetDelay_Show[MacCfg.ChannelNumBuf[MacCfg.OutputChannelSel] - 1].setText(String.valueOf(ChannelShowDelay(delay)));

                        }
                    });
                }
            });


            B_SetDelay_Show[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    int sd_num = 0;
                    sd_num = (int) view.getTag();

                    //获取通信编号
                    for (int j = 0; j < DataStruct.CurMacMode.Out.OUT_CH_MAX_USE; j++) {
                        if (MacCfg.ChannelNumBuf[j] - 1 == sd_num) {
                            MacCfg.OutputChannelSel = j;
                            break;
                        }
                    }
                    FlashPageUI();
                    setselect();
                    Bundle Freq = new Bundle();
                    Freq.putInt(SetDelayDialogFragment.ST_Data, DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].delay);
                    Freq.putInt(SetDelayDialogFragment.ST_DelayUnit, DelayUnit);

                    SetDelayDialogFragment setDelayDialogFragment = new SetDelayDialogFragment();
                    setDelayDialogFragment.setArguments(Freq);
                    setDelayDialogFragment.show(getActivity().getFragmentManager(), "SetDelayDialogFragment");
                    setDelayDialogFragment.OnSetDelayDialogFragmentChangeListener(new SetDelayDialogFragment.OnDelayDialogFragmentChangeListener() {

                        @Override
                        public void onDelayVolChangeListener(int delay, int type, boolean flag) {
                            // TODO Auto-generated method stub
                            DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].delay = delay;

                                B_SetDelay_Show[MacCfg.ChannelNumBuf[MacCfg.OutputChannelSel] - 1].setText(String.valueOf(ChannelShowDelay(delay)));
                        }
                    });
                }
            });

            LLyout_SetDelay[i].setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    int sd_num = 0;
                    sd_num = (int) view.getTag();

                    //获取通信编号
                    for (int j = 0; j < DataStruct.CurMacMode.Out.OUT_CH_MAX_USE; j++) {
                        if (MacCfg.ChannelNumBuf[j] - 1 == sd_num) {
                            MacCfg.OutputChannelSel = j;
                            break;
                        }
                    }
                    setselect();
                    FlashPageUI();
                    Bundle Freq = new Bundle();
                    Freq.putInt(SetDelayDialogFragment.ST_Data, DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].delay);
                    Freq.putInt(SetDelayDialogFragment.ST_DelayUnit, DelayUnit);

                    SetDelayDialogFragment setDelayDialogFragment = new SetDelayDialogFragment();
                    setDelayDialogFragment.setArguments(Freq);
                    setDelayDialogFragment.show(getActivity().getFragmentManager(), "SetDelayDialogFragment");
                    setDelayDialogFragment.OnSetDelayDialogFragmentChangeListener(new SetDelayDialogFragment.OnDelayDialogFragmentChangeListener() {

                        @Override
                        public void onDelayVolChangeListener(int delay, int type, boolean flag) {
                            // TODO Auto-generated method stub
                            DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].delay = delay;

                                B_SetDelay_Show[MacCfg.ChannelNumBuf[MacCfg.OutputChannelSel] - 1].setText(String.valueOf(ChannelShowDelay(delay)));
                        }
                    });
                }
            });
            /*********************    设置延时单位         **********************/
            B_CM.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    DelayUnit = 1;
                    SetDelayUnit();
                    UnitDelayTimeShow();
                }
            });
            B_MS.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    DelayUnit = 2;
                    SetDelayUnit();
                    UnitDelayTimeShow();
                }
            });
            B_Inch.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    DelayUnit = 3;
                    SetDelayUnit();
                    UnitDelayTimeShow();
                }
            });
            DelayUnit_list = new ArrayList<String>();
            DelayUnit_list.add(getResources().getString(R.string.CM));
            DelayUnit_list.add(getResources().getString(R.string.MS));
            DelayUnit_list.add(getResources().getString(R.string.Inch));
        }
    }
}