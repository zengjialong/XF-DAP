package com.chs.mt.xf_dap.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chs.mt.xf_dap.R;
import com.chs.mt.xf_dap.datastruct.DataStruct;
import com.chs.mt.xf_dap.datastruct.MacCfg;
import com.chs.mt.xf_dap.main.MainTURTActivity;
import com.chs.mt.xf_dap.operation.ReturnFuncation;
import com.chs.mt.xf_dap.tools.LongCickButton;
import com.chs.mt.xf_dap.tools.MHS_SeekBar;
import com.chs.mt.xf_dap.tools.wheel.WheelView;
import com.chs.mt.xf_dap.viewItem.V_MixerItem;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MixerFragment extends Fragment {
    private Toast mToast;
    private Context mContext;

    private int MaxINPUT = 16;
    private V_MixerItem[] mMixerItem = new V_MixerItem[MaxINPUT];

    private LinearLayout LY_MIXERCH6, LY_MIXERCH8, LY_MIXERCH10, LY_MIXERCH12;
    private Button[] B_MIXERCH6_Ch = new Button[6];
    private Button[] B_MIXERCH8_Ch = new Button[8];
    private Button[] B_MIXERCH10_Ch = new Button[10];
    private Button[] B_MIXERCH12_Ch = new Button[12];
    private View LY_MIXERSel;
    private String[] mixname = new String[8];
    private String[] mixname_500 = new String[8];
    //通道选择
    private WheelView WV_OutVa;
    private List<String> Channellists;

    public MixerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.chs_fragment_mixer, container, false);
        mContext = getActivity().getApplicationContext();
        initChannelSel(view);
        initView(view);
        initClick();
        initConfigMac();

        initViewContext();
        FlashPageUI();


        return view;
    }

    //刷新页面UI
    public void FlashPageUI() {
        AddMixerPageListener();
        Channellists = new ArrayList<String>();
        for (int i = 0; i < MacCfg.OutMax; i++) {
            Channellists.add(DataStruct.CurMacMode.Out.Name[i]);
        }
        ReturnFuncation.CurrentOutputChannel();

        WV_OutVa.lists(Channellists).showCount(5).selectTip("").select(MacCfg.OutputChannelSel).listener(new WheelView.OnWheelViewItemSelectListener() {
            @Override
            public void onItemSelect(int index, boolean fromUser) {
                if (!fromUser) {
                    MacCfg.OutputChannelSel = index;
                    ReturnFuncation.CurrentOutputChannel();
                    MainTURTActivity parentActivity = (MainTURTActivity) getActivity();
                    parentActivity.setLinkText();
                    FlashPageUI();
                }
            }
        }).build();
        WV_OutVa.setIndex(MacCfg.OutputChannelSel);
        FlashChannelUI();
    }

    //刷新通道UI
    public void FlashChannelUI() {
        // WV_OutVa.setIndex(MacCfg.OutputChannelSel);
        flashMixerItemWithInputSource();
        FlashMixerInputChannelSel();

        FlashInputVal();
        FlashInputPolar();
        FlashMixerSeekbar();
        FlashMixerResetButtomState();


        for (int i = 0; i < MaxINPUT; i++) {
            mMixerItem[i].setVisibility(View.GONE);
        }

        for (int i = 0; i < MacCfg.MixMax; i++) {
            if (MacCfg.Mcu == 3) {  //表示选中300ACH  隐藏一些通道 方便使用其他数据
                if (i > 5 && i < 10) {
                    mMixerItem[i].setVisibility(View.GONE);
                } else {
                    mMixerItem[i].setVisibility(View.VISIBLE);
                }
            } else {
                mMixerItem[i].setVisibility(View.VISIBLE);
            }


            mMixerItem[i].TV_MixerChn.setText(DataStruct.CurMacMode.Mixer.Name[i]);

        }


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

    //加密
    private void initClick() {

    }

    private void initView(View view) {

        mMixerItem[0] = (V_MixerItem) view.findViewById(R.id.id_mixer_0);
        mMixerItem[1] = (V_MixerItem) view.findViewById(R.id.id_mixer_1);
        mMixerItem[2] = (V_MixerItem) view.findViewById(R.id.id_mixer_2);
        mMixerItem[3] = (V_MixerItem) view.findViewById(R.id.id_mixer_3);
        mMixerItem[4] = (V_MixerItem) view.findViewById(R.id.id_mixer_4);
        mMixerItem[5] = (V_MixerItem) view.findViewById(R.id.id_mixer_5);
        mMixerItem[6] = (V_MixerItem) view.findViewById(R.id.id_mixer_6);
        mMixerItem[7] = (V_MixerItem) view.findViewById(R.id.id_mixer_7);
        mMixerItem[8] = (V_MixerItem) view.findViewById(R.id.id_mixer_8);
        mMixerItem[9] = (V_MixerItem) view.findViewById(R.id.id_mixer_9);
        mMixerItem[10] = (V_MixerItem) view.findViewById(R.id.id_mixer_10);
        mMixerItem[11] = (V_MixerItem) view.findViewById(R.id.id_mixer_11);
        mMixerItem[12] = (V_MixerItem) view.findViewById(R.id.id_mixer_12);
        mMixerItem[13] = (V_MixerItem) view.findViewById(R.id.id_mixer_13);
        mMixerItem[14] = (V_MixerItem) view.findViewById(R.id.id_mixer_14);
        mMixerItem[15] = (V_MixerItem) view.findViewById(R.id.id_mixer_15);

        for (int i = 0; i < MaxINPUT; i++) {
            mMixerItem[i].TV_MixerChn.setTag(i);
            mMixerItem[i].LLY_Mixer.setTag(i);
            mMixerItem[i].LRY_Mixer.setTag(i);
            // mMixerItem[i].B_MixerPolar.setTag(i);
            mMixerItem[i].B_MixerValInc.setTag(i);
            mMixerItem[i].B_MixerValSub.setTag(i);

            mMixerItem[i].B_MixerVal.setTag(i);
            mMixerItem[i].SB_Mixer_SeekBar.setTag(i);
            mMixerItem[i].B_MixerHide.setTag(i);
            mMixerItem[i].B_MixerResetVal.setTag(i);
//
//            mMixerItem[i].LLY_Reset_Polar.setTag(i);
//           // mMixerItem[i].LLY_Reset.setTag(i);
//            mMixerItem[i].LLY_INS.setTag(i);
//            mMixerItem[i].LLY_SUB.setTag(i);
        }

        //b_OutChSelButton = (Button) view.findViewById(R.id.id_b_weight_ch_sel);
        LY_MIXERSel = view.findViewById(R.id.id_channel_sel_mixer);

        LY_MIXERCH6 = (LinearLayout) LY_MIXERSel.findViewById(R.id.id_ly_channel_6);
        LY_MIXERCH8 = (LinearLayout) LY_MIXERSel.findViewById(R.id.id_ly_channel_8);
        LY_MIXERCH10 = (LinearLayout) LY_MIXERSel.findViewById(R.id.id_ly_channel_10);
        LY_MIXERCH12 = (LinearLayout) LY_MIXERSel.findViewById(R.id.id_ly_channel_12);

        B_MIXERCH6_Ch[0] = (Button) LY_MIXERSel.findViewById(R.id.id_b_channel6_0);
        B_MIXERCH6_Ch[1] = (Button) LY_MIXERSel.findViewById(R.id.id_b_channel6_1);
        B_MIXERCH6_Ch[2] = (Button) LY_MIXERSel.findViewById(R.id.id_b_channel6_2);
        B_MIXERCH6_Ch[3] = (Button) LY_MIXERSel.findViewById(R.id.id_b_channel6_3);
        B_MIXERCH6_Ch[4] = (Button) LY_MIXERSel.findViewById(R.id.id_b_channel6_4);
        B_MIXERCH6_Ch[5] = (Button) LY_MIXERSel.findViewById(R.id.id_b_channel6_5);

        B_MIXERCH8_Ch[0] = (Button) LY_MIXERSel.findViewById(R.id.id_b_channel8_0);
        B_MIXERCH8_Ch[1] = (Button) LY_MIXERSel.findViewById(R.id.id_b_channel8_1);
        B_MIXERCH8_Ch[2] = (Button) LY_MIXERSel.findViewById(R.id.id_b_channel8_2);
        B_MIXERCH8_Ch[3] = (Button) LY_MIXERSel.findViewById(R.id.id_b_channel8_3);
        B_MIXERCH8_Ch[4] = (Button) LY_MIXERSel.findViewById(R.id.id_b_channel8_4);
        B_MIXERCH8_Ch[5] = (Button) LY_MIXERSel.findViewById(R.id.id_b_channel8_5);
        B_MIXERCH8_Ch[6] = (Button) LY_MIXERSel.findViewById(R.id.id_b_channel8_6);
        B_MIXERCH8_Ch[7] = (Button) LY_MIXERSel.findViewById(R.id.id_b_channel8_7);

        B_MIXERCH10_Ch[0] = (Button) LY_MIXERSel.findViewById(R.id.id_b_channel10_0);
        B_MIXERCH10_Ch[1] = (Button) LY_MIXERSel.findViewById(R.id.id_b_channel10_1);
        B_MIXERCH10_Ch[2] = (Button) LY_MIXERSel.findViewById(R.id.id_b_channel10_2);
        B_MIXERCH10_Ch[3] = (Button) LY_MIXERSel.findViewById(R.id.id_b_channel10_3);
        B_MIXERCH10_Ch[4] = (Button) LY_MIXERSel.findViewById(R.id.id_b_channel10_4);
        B_MIXERCH10_Ch[5] = (Button) LY_MIXERSel.findViewById(R.id.id_b_channel10_5);
        B_MIXERCH10_Ch[6] = (Button) LY_MIXERSel.findViewById(R.id.id_b_channel10_6);
        B_MIXERCH10_Ch[7] = (Button) LY_MIXERSel.findViewById(R.id.id_b_channel10_7);
        B_MIXERCH10_Ch[8] = (Button) LY_MIXERSel.findViewById(R.id.id_b_channel10_8);
        B_MIXERCH10_Ch[9] = (Button) LY_MIXERSel.findViewById(R.id.id_b_channel10_9);

        B_MIXERCH12_Ch[0] = (Button) LY_MIXERSel.findViewById(R.id.id_b_channel12_0);
        B_MIXERCH12_Ch[1] = (Button) LY_MIXERSel.findViewById(R.id.id_b_channel12_1);
        B_MIXERCH12_Ch[2] = (Button) LY_MIXERSel.findViewById(R.id.id_b_channel12_2);
        B_MIXERCH12_Ch[3] = (Button) LY_MIXERSel.findViewById(R.id.id_b_channel12_3);
        B_MIXERCH12_Ch[4] = (Button) LY_MIXERSel.findViewById(R.id.id_b_channel12_4);
        B_MIXERCH12_Ch[5] = (Button) LY_MIXERSel.findViewById(R.id.id_b_channel12_5);
        B_MIXERCH12_Ch[6] = (Button) LY_MIXERSel.findViewById(R.id.id_b_channel12_6);
        B_MIXERCH12_Ch[7] = (Button) LY_MIXERSel.findViewById(R.id.id_b_channel12_7);
        B_MIXERCH12_Ch[8] = (Button) LY_MIXERSel.findViewById(R.id.id_b_channel12_8);
        B_MIXERCH12_Ch[9] = (Button) LY_MIXERSel.findViewById(R.id.id_b_channel12_9);
        B_MIXERCH12_Ch[10] = (Button) LY_MIXERSel.findViewById(R.id.id_b_channel12_10);
        B_MIXERCH12_Ch[11] = (Button) LY_MIXERSel.findViewById(R.id.id_b_channel12_11);
    }
    /*********************************************************************/
    /***************************    initChannelSel     ****************************/
    /*********************************************************************/
    private void initChannelSel(View view) {

//        Channellists = new ArrayList<String>();
//        for(int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX_USE; i++){
//            Channellists.add(DataStruct.CurMacMode.Out.Name[i]);
//        }

        WV_OutVa = (WheelView) view.findViewById(R.id.id_output_va_wheelview);
//        WV_OutVa.select(MacCfg.OutputChannelSel);
//        WV_OutVa.lists(Channellists).showCount(5).selectTip("").select(0).listener(new WheelView.OnWheelViewItemSelectListener() {
//            @Override
//            public void onItemSelect(int index,boolean fromUser) {
//                if(!fromUser){
//                    MacCfg.OutputChannelSel = index;
//                    FlashChannelUI();
//                }
//            }
//        }).build();
    }

    private void initViewContext() {

    }


    private void initConfigMac() {

//        mixname= new String[]{mContext.getString(R.string.Mixer_IN1),mContext.getString(R.string.Mixer_IN1),
//                mContext.getString(R.string.Mixer_IN1),mContext.getString(R.string.Mixer_IN1),
//                mContext.getString(R.string.Mixer_IN1),mContext.getString(R.string.Mixer_IN1),};


        for (int i = 0; i < 16; i++) {
            mMixerItem[i].setVisibility(View.GONE);
        }

//        mMixerItem[6].setVisibility(View.GONE);
//        mMixerItem[7].setVisibility(View.GONE);


    }


    /*********************************************************************/
    /*****************************     混音  TODO        ****************************/
    /*********************************************************************/
    void FlashInputPolar() {

    }

    private void setMixerVolumeByIndex(int index, int val) {
        if (MacCfg.Mcu != 6&&MacCfg.Mcu!=8) {
            switch (index) {
                case 0:
                    DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN1_Vol = val;
                    break;
                case 1:
                    DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN2_Vol = val;
                    break;
                case 2:
                    DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN3_Vol = val;
                    break;
                case 3:
                    DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN4_Vol = val;
                    break;

                case 4:
                    DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN5_Vol = val;
                    break;
                case 5:
                    DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN6_Vol = val;
                    break;
                case 6:
                    DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN7_Vol = val;
                    break;
                case 7:
                    DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN8_Vol = val;
                    break;

                case 8:
                    DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN9_Vol = val;
                    break;
                case 9:
                    DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN10_Vol = val;
                    break;
                case 10:
                    DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN11_Vol = val;
                    break;
                case 11:
                    DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN12_Vol = val;
                    break;

                case 12:
                    DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN13_Vol = val;
                    break;
                case 13:
                    DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN14_Vol = val;
                    break;
                case 14:
                    DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN15_Vol = val;
                    break;
                case 15:
                    DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN16_Vol = val;
                    break;

                default:
                    break;
            }
        } else if (MacCfg.Mcu==8) {
            switch (index) {
                case 0:
                    DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN1_Vol = val;
                    break;
                case 1:
                    DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN2_Vol = val;
                    break;
                case 2:
                    DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN3_Vol = val;
                    break;
                case 3:
                    DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN4_Vol = val;
                    break;

                case 4:
                    DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN5_Vol = val;
                    break;
                case 5:
                    DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN6_Vol = val;
                    break;
                case 6:
                    DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN7_Vol = val;
                    break;
                case 7:
                    DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN8_Vol = val;
                    break;

                case 8:
                    DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN15_Vol = val;
                    break;
                case 9:
                    DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN16_Vol = val;
                    break;
                default:
                    break;
            }
        } else {
            switch (index) {
                case 0:
                    DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN1_Vol = val;
                    break;
                case 1:
                    DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN2_Vol = val;
                    break;
                case 2:
                    DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN3_Vol = val;
                    break;
                case 3:
                    DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN4_Vol = val;
                    break;

                case 4:
                    DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN9_Vol = val;
                    break;
                case 5:
                    DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN10_Vol = val;
                    break;
                case 6:
                    DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN7_Vol = val;
                    break;
                case 7:
                    DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN8_Vol = val;
                    break;
            }
        }

    }

    private int getMixerVolumeByIndex(int index) {
        if (MacCfg.Mcu != 6 &&MacCfg.Mcu!=8) {
            switch (index) {
                case 0:
                    return DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN1_Vol;
                case 1:
                    return DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN2_Vol;
                case 2:
                    return DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN3_Vol;
                case 3:
                    return DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN4_Vol;

                case 4:
                    return DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN5_Vol;
                case 5:
                    return DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN6_Vol;
                case 6:
                    return DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN7_Vol;
                case 7:
                    return DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN8_Vol;

                case 8:
                    return DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN9_Vol;
                case 9:
                    return DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN10_Vol;
                case 10:
                    return DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN11_Vol;
                case 11:
                    return DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN12_Vol;

                case 12:
                    return DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN13_Vol;
                case 13:
                    return DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN14_Vol;
                case 14:
                    return DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN15_Vol;
                case 15:
                    return DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN16_Vol;

                default:
                    return 0;
            }
        } else if (MacCfg.Mcu == 8) {
            switch (index) {
                case 0:
                    return DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN1_Vol;
                case 1:
                    return DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN2_Vol;
                case 2:
                    return DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN3_Vol;
                case 3:
                    return DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN4_Vol;

                case 4:
                    return DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN5_Vol;
                case 5:
                    return DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN6_Vol;
                case 6:
                    return DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN7_Vol;
                case 7:
                    return DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN8_Vol;

                case 8:
                    return DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN15_Vol;
                case 9:
                    return DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN16_Vol;

                default:
                    return 0;
            }
        } else {
            switch (index) {
                case 0:
                    return DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN1_Vol;
                case 1:
                    return DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN2_Vol;
                case 2:
                    return DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN3_Vol;
                case 3:
                    return DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN4_Vol;

                case 4:
                    return DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN9_Vol;
                case 5:
                    return DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN10_Vol;
                case 6:
                    return DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN7_Vol;
                case 7:
                    return DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN8_Vol;
                default:
                    return 0;
            }
        }

    }

    private void setMixerBufVolumeByIndex(int index, int val) {
        if (MacCfg.Mcu != 6&&MacCfg.Mcu!=8) {
            switch (index) {
                case 0:
                    DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN1_Vol = val;
                    break;
                case 1:
                    DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN2_Vol = val;
                    break;
                case 2:
                    DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN3_Vol = val;
                    break;
                case 3:
                    DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN4_Vol = val;
                    break;

                case 4:
                    DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN5_Vol = val;
                    break;
                case 5:
                    DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN6_Vol = val;
                    break;
                case 6:
                    DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN7_Vol = val;
                    break;
                case 7:
                    DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN8_Vol = val;
                    break;

                case 8:
                    DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN9_Vol = val;
                    break;
                case 9:
                    DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN10_Vol = val;
                    break;
                case 10:
                    DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN11_Vol = val;
                    break;
                case 11:
                    DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN12_Vol = val;
                    break;

                case 12:
                    DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN13_Vol = val;
                    break;
                case 13:
                    DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN14_Vol = val;
                    break;
                case 14:
                    DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN15_Vol = val;
                    break;
                case 15:
                    DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN16_Vol = val;
                    break;

                default:
                    break;
            }
        } else if (MacCfg.Mcu == 8) {
            switch (index) {
                case 0:
                    DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN1_Vol = val;
                    break;
                case 1:
                    DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN2_Vol = val;
                    break;
                case 2:
                    DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN3_Vol = val;
                    break;
                case 3:
                    DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN4_Vol = val;
                    break;
                case 4:
                    DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN5_Vol = val;
                    break;
                case 5:
                    DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN6_Vol = val;
                    break;
                case 6:
                    DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN7_Vol = val;
                    break;
                case 7:
                    DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN8_Vol = val;
                    break;
                case 8:
                    DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN15_Vol = val;
                    break;
                case 9:
                    DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN16_Vol = val;
                    break;
                default:
                    break;
            }
        } else {
            switch (index) {
                case 0:
                    DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN1_Vol = val;
                    break;
                case 1:
                    DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN2_Vol = val;
                    break;
                case 2:
                    DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN3_Vol = val;
                    break;
                case 3:
                    DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN4_Vol = val;
                    break;
                case 4:
                    DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN9_Vol = val;
                    break;
                case 5:
                    DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN10_Vol = val;
                    break;
                case 6:
                    DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN7_Vol = val;
                    break;
                case 7:
                    DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN8_Vol = val;
                    break;
                default:
                    break;
            }
        }

    }

    private int getMixerBufVolumeByIndex(int index) {
        if (MacCfg.Mcu != 6&&MacCfg.Mcu!=8) {
            switch (index) {
                case 0:
                    return DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN1_Vol;
                case 1:
                    return DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN2_Vol;
                case 2:
                    return DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN3_Vol;
                case 3:
                    return DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN4_Vol;

                case 4:
                    return DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN5_Vol;
                case 5:
                    return DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN6_Vol;
                case 6:
                    return DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN7_Vol;
                case 7:
                    return DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN8_Vol;

                case 8:
                    return DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN9_Vol;
                case 9:
                    return DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN10_Vol;
                case 10:
                    return DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN11_Vol;
                case 11:
                    return DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN12_Vol;

                case 12:
                    return DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN13_Vol;
                case 13:
                    return DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN14_Vol;
                case 14:
                    return DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN15_Vol;
                case 15:
                    return DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN16_Vol;

                default:
                    return 0;
            }
        } else if (MacCfg.Mcu == 8) {
            switch (index) {
                case 0:
                    return DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN1_Vol;
                case 1:
                    return DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN2_Vol;
                case 2:
                    return DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN3_Vol;
                case 3:
                    return DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN4_Vol;

                case 4:
                    return DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN5_Vol;
                case 5:
                    return DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN6_Vol;
                case 6:
                    return DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN7_Vol;
                case 7:
                    return DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN8_Vol;
                case 8:
                    return DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN15_Vol;
                case 9:
                    return DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN16_Vol;
                default:
                    return 0;
            }
        } else {
            switch (index) {
                case 0:
                    return DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN1_Vol;
                case 1:
                    return DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN2_Vol;
                case 2:
                    return DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN3_Vol;
                case 3:
                    return DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN4_Vol;

                case 4:
                    return DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN9_Vol;
                case 5:
                    return DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN10_Vol;
                case 6:
                    return DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN7_Vol;
                case 7:
                    return DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN8_Vol;
                default:
                    return 0;
            }
        }

    }

    private void setMixerVolumeState(boolean off) {
        if (off) {
            setMixerVolumeByIndex(MacCfg.inputChannelSel, 0);
        } else {
            setMixerVolumeByIndex(MacCfg.inputChannelSel, 100);
        }

    }

    void FlashInputVal() {
        //DataStruct.CurMacMode.Mixer.MIXER_CH_MAX
        for (int i = 0; i < 16; i++) {
            if ((getMixerVolumeByIndex(i) > DataStruct.CurMacMode.Mixer.Max_Mixer_Vol)
                    || (getMixerVolumeByIndex(i) < 0)) {
                setMixerVolumeByIndex(i, 100);
            }
        }

        for (int i = 0; i < DataStruct.CurMacMode.Mixer.MIXER_CH_MAX_USE; i++) {
            mMixerItem[i].B_MixerVal.setText(String.valueOf(getMixerVolumeByIndex(i)));
        }

        FlashMixerResetButtomState();
        //
        if ((DataStruct.CurMacMode.BOOL_ENCRYPTION) && (MacCfg.bool_Encryption == true)) {
            for (int i = 0; i < DataStruct.CurMacMode.Mixer.MIXER_CH_MAX_USE; i++) {
                mMixerItem[i].B_MixerVal.setText("0");
            }
        }

    }

    void FlashMixerSeekbar() {
        for (int i = 0; i < DataStruct.CurMacMode.Mixer.MIXER_CH_MAX_USE; i++) {
            mMixerItem[i].SB_Mixer_SeekBar.setProgress(getMixerVolumeByIndex(i));
//            if(i == 8){
//                mMixerItem[i].SB_Mixer_SeekBar.setProgress(getMixerVolumeByIndex(15));
//            }
        }

        if ((DataStruct.CurMacMode.BOOL_ENCRYPTION) && (MacCfg.bool_Encryption == true)) {
            for (int i = 0; i < DataStruct.CurMacMode.Mixer.MIXER_CH_MAX_USE; i++) {
                mMixerItem[i].SB_Mixer_SeekBar.setProgress(0);
            }
        }

        FlashMixerResetButtomState();
    }

    void FlashMixerInputChannelSel() {
        for (int i = 0; i < DataStruct.CurMacMode.Mixer.MIXER_CH_MAX_USE; i++) {
            mMixerItem[i].LLY_Mixer.setBackgroundResource(R.drawable.chs_layoutc_normal);
            //TV_MixerChn[i].setTextColor(getResources().getColor(R.color.text_color_delaychannelsel_normal));
        }
//        mMixerItem[MacCfg.inputChannelSel].LLY_Mixer.setBackgroundResource(R.drawable.chs_layoutc_press);
        //TV_MixerChn[MacCfg.inputChannelSel].setTextColor(getResources().getColor(R.color.text_color_delaychannelsel_press));
    }

    private void flashMixerItemWithInputSource() {
//        for (int i = 0; i < MaxINPUT; i++) {
//
//            mMixerItem[i].B_MixerHide.setVisibility(View.VISIBLE);
//            mMixerItem[i].B_MixerHide.getBackground().setAlpha(170);
//        }
        switch (MacCfg.Mcu) {
            case 1://100
            case 2://300
            case 6://250LAP
                switch (DataStruct.RcvDeviceData.SYS.input_source) {
                    case 1://高电平
                        for (int i = 0; i < 4; i++) {
                            mMixerItem[i].B_MixerHide.setVisibility(View.GONE);
                        }
                        break;
                    case 3://AUX
                        for (int i = 4; i < 6; i++) {
                            mMixerItem[i].B_MixerHide.setVisibility(View.GONE);
                        }
                        break;

                    case 2://高电平
                        for (int i = 6; i < 8; i++) {
                            mMixerItem[i].B_MixerHide.setVisibility(View.GONE);
                        }
                        break;

                    default:
                        break;
                }
                break;

            case 3: //300
            case 4:  //500
            case 5://501
                switch (DataStruct.RcvDeviceData.SYS.input_source) {
                    case 1://高电平
                        for (int i = 0; i < 10; i++) {
                            mMixerItem[i].B_MixerHide.setVisibility(View.GONE);
                        }
                        break;
                    case 3://AUX
                        for (int i = 10; i < 12; i++) {
                            mMixerItem[i].B_MixerHide.setVisibility(View.GONE);
                        }
                        break;
                    case 0://高电平
                        for (int i = 12; i < 14; i++) {
                            mMixerItem[i].B_MixerHide.setVisibility(View.GONE);
                        }
                        break;
                    case 2://高电平
                        for (int i = 14; i < 16; i++) {
                            mMixerItem[i].B_MixerHide.setVisibility(View.GONE);
                        }
                        break;

                    default:
                        break;

                }
                break;
            case 8:
                switch (DataStruct.RcvDeviceData.SYS.input_source) {
                    case 1://高电平
                        for (int i = 0; i < 6; i++) {
                            mMixerItem[i].B_MixerHide.setVisibility(View.GONE);
                        }
                        break;
                    case 3://AUX
                        for (int i = 6; i < 8; i++) {
                            mMixerItem[i].B_MixerHide.setVisibility(View.GONE);
                        }
                        break;
                    case 2:
                    case 5:
                        for (int i = 8; i < 10; i++) {
                            mMixerItem[i].B_MixerHide.setVisibility(View.GONE);
                        }
                        break;
                }
                break;
        }


    }

    void syncMixerVolume() {
        DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN1_Vol = DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN1_Vol;
        DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN2_Vol = DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN2_Vol;
        DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN3_Vol = DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN3_Vol;
        DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN4_Vol = DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN4_Vol;

        DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN5_Vol = DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN5_Vol;
        DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN6_Vol = DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN6_Vol;
        DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN7_Vol = DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN7_Vol;
        DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN8_Vol = DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN8_Vol;

        DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN9_Vol = DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN9_Vol;
        DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN10_Vol = DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN10_Vol;
        DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN11_Vol = DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN11_Vol;
        DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN12_Vol = DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN12_Vol;

        DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN13_Vol = DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN13_Vol;
        DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN14_Vol = DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN14_Vol;
        DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN15_Vol = DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN15_Vol;
        DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN16_Vol = DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].IN16_Vol;
    }

    void FlashMixerResetButtomState() {
        for (int i = 0; i < DataStruct.CurMacMode.Mixer.MIXER_CH_MAX_USE; i++) {
            if (getMixerVolumeByIndex(i) == 0) {
                mMixerItem[i].B_MixerResetVal.setBackgroundResource(R.drawable.chs_weight_off);
            } else {
                mMixerItem[i].B_MixerResetVal.setBackgroundResource(R.drawable.chs_weight_on);
            }

//            if(i == 8){
//                if(getMixerVolumeByIndex(15) == 0){
//                    mMixerItem[i].B_MixerResetVal.setBackgroundResource(R.drawable.chs_switch_normal);
//                }else{
//                    mMixerItem[i].B_MixerResetVal.setBackgroundResource(R.drawable.chs_switch_press);
//                }
//            }
        }
    }

    private void FlashMixerVolumeData() {

        if ((getMixerVolumeByIndex(MacCfg.inputChannelSel) == 0) &&
                (getMixerBufVolumeByIndex(MacCfg.inputChannelSel) != 0)) {
            mMixerItem[MacCfg.inputChannelSel].B_MixerResetVal.setBackgroundResource(R.drawable.chs_switch_press);
            setMixerVolumeByIndex(MacCfg.inputChannelSel, getMixerBufVolumeByIndex(MacCfg.inputChannelSel));
        } else if (getMixerVolumeByIndex(MacCfg.inputChannelSel) != 0) {
            setMixerBufVolumeByIndex(MacCfg.inputChannelSel, getMixerVolumeByIndex(MacCfg.inputChannelSel));
            setMixerVolumeByIndex(MacCfg.inputChannelSel, 0);
            mMixerItem[MacCfg.inputChannelSel].B_MixerResetVal.setBackgroundResource(R.drawable.chs_switch_normal);
        }


//        if(MacCfg.inputChannelSel != 8){
//            if((getMixerVolumeByIndex(MacCfg.inputChannelSel) == 0)&&
//                    (getMixerBufVolumeByIndex(MacCfg.inputChannelSel) != 0)){
//                mMixerItem[MacCfg.inputChannelSel].B_MixerResetVal.setBackgroundResource(R.drawable.chs_switch_press);
//                setMixerVolumeByIndex(MacCfg.inputChannelSel, getMixerBufVolumeByIndex(MacCfg.inputChannelSel));
//            }else if(getMixerVolumeByIndex(MacCfg.inputChannelSel) != 0){
//                setMixerBufVolumeByIndex(MacCfg.inputChannelSel, getMixerVolumeByIndex(MacCfg.inputChannelSel));
//                setMixerVolumeByIndex(MacCfg.inputChannelSel, 0);
//                mMixerItem[MacCfg.inputChannelSel].B_MixerResetVal.setBackgroundResource(R.drawable.chs_switch_normal);
//            }
//        }else{
//            if((getMixerVolumeByIndex(14) == 0)&&
//                    (getMixerBufVolumeByIndex(14) != 0)){
//                mMixerItem[MacCfg.inputChannelSel].B_MixerResetVal.setBackgroundResource(R.drawable.chs_switch_press);
//                setMixerVolumeByIndex(14, getMixerBufVolumeByIndex(14));
//                setMixerVolumeByIndex(15, getMixerBufVolumeByIndex(14));
//            }else if(getMixerVolumeByIndex(14) != 0){
//                setMixerBufVolumeByIndex(14, getMixerVolumeByIndex(14));
//                setMixerVolumeByIndex(14, 0);
//                setMixerVolumeByIndex(15, 0);
//                mMixerItem[MacCfg.inputChannelSel].B_MixerResetVal.setBackgroundResource(R.drawable.chs_switch_normal);
//            }
//        }

    }


    void MixerIN_ValInc() {
        int val = 0;

        val = getMixerVolumeByIndex(MacCfg.inputChannelSel);
        if (++val > DataStruct.CurMacMode.Mixer.Max_Mixer_Vol) {
            val = DataStruct.CurMacMode.Mixer.Max_Mixer_Vol;
        }
        setMixerVolumeByIndex(MacCfg.inputChannelSel, val);
        mMixerItem[MacCfg.inputChannelSel].SB_Mixer_SeekBar.setProgress(val);
        mMixerItem[MacCfg.inputChannelSel].B_MixerVal.setText(String.valueOf(val));


//        if(MacCfg.inputChannelSel != 8){
//            val = getMixerVolumeByIndex(MacCfg.inputChannelSel);
//            if(++val>DataStruct.CurMacMode.Mixer.Max_Mixer_Vol){
//                val=DataStruct.CurMacMode.Mixer.Max_Mixer_Vol;
//            }
//            setMixerVolumeByIndex(MacCfg.inputChannelSel, val);
//            mMixerItem[MacCfg.inputChannelSel].SB_Mixer_SeekBar.setProgress(val);
//            mMixerItem[MacCfg.inputChannelSel].B_MixerVal.setText(String.valueOf(val));
//        }else{
//            val = getMixerVolumeByIndex(14);
//            if(++val>DataStruct.CurMacMode.Mixer.Max_Mixer_Vol){
//                val=DataStruct.CurMacMode.Mixer.Max_Mixer_Vol;
//            }
//            setMixerVolumeByIndex(14, val);
//            setMixerVolumeByIndex(15, val);
//            mMixerItem[MacCfg.inputChannelSel].SB_Mixer_SeekBar.setProgress(val);
//            mMixerItem[MacCfg.inputChannelSel].B_MixerVal.setText(String.valueOf(val));
//        }

        syncMixerVolume();
        FlashMixerResetButtomState();


    }

    void MixerIN_ValSub() {

        int val = 0;

        val = getMixerVolumeByIndex(MacCfg.inputChannelSel);
        if (--val < 0) {
            val = 0;
        }
        setMixerVolumeByIndex(MacCfg.inputChannelSel, val);
        mMixerItem[MacCfg.inputChannelSel].SB_Mixer_SeekBar.setProgress(val);
        mMixerItem[MacCfg.inputChannelSel].B_MixerVal.setText(String.valueOf(val));

        syncMixerVolume();
        FlashMixerResetButtomState();


    }

    void MixerIN_ValShow(int val) {
        setMixerVolumeByIndex(MacCfg.inputChannelSel, val);
        mMixerItem[MacCfg.inputChannelSel].B_MixerVal.setText(String.valueOf(val));
    }

    public void AddMixerPageListener() {
        for (int i = 0; i < DataStruct.CurMacMode.Mixer.MIXER_CH_MAX_USE; i++) {
            mMixerItem[i].B_MixerValInc.setTag(i);
            mMixerItem[i].B_MixerValSub.setTag(i);
            //音量加
            mMixerItem[i].B_MixerValInc.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    MacCfg.inputChannelSel = (int) arg0.getTag();
                    MixerIN_ValInc();
                }
            });
            mMixerItem[i].B_MixerValInc.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View arg0) {
                    MacCfg.inputChannelSel = (int) arg0.getTag();
                    mMixerItem[MacCfg.inputChannelSel].B_MixerValInc.setStart();
                    return false;
                }
            });
            mMixerItem[i].B_MixerValInc.setOnLongTouchListener(new LongCickButton.LongTouchListener() {
                @Override
                public void onLongTouch() {
                    MixerIN_ValInc();
                }
            }, MacCfg.LongClickEventTimeMax);

            //音量减
            mMixerItem[i].B_MixerValSub.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    MacCfg.inputChannelSel = (int) arg0.getTag();
                    MixerIN_ValSub();
                }
            });
            mMixerItem[i].B_MixerValSub.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View arg0) {
                    MacCfg.inputChannelSel = (int) arg0.getTag();
                    mMixerItem[MacCfg.inputChannelSel].B_MixerValSub.setStart();
                    return false;
                }
            });
            mMixerItem[i].B_MixerValSub.setOnLongTouchListener(new LongCickButton.LongTouchListener() {
                @Override
                public void onLongTouch() {
                    MixerIN_ValSub();
                }
            }, MacCfg.LongClickEventTimeMax);

        }

        for (int j = 0; j < DataStruct.CurMacMode.Mixer.MIXER_CH_MAX_USE; j++) {
            mMixerItem[j].SB_Mixer_SeekBar.setProgressMax(DataStruct.CurMacMode.Mixer.Max_Mixer_Vol);
            mMixerItem[j].SB_Mixer_SeekBar.setOnSeekBarChangeListener(new MHS_SeekBar.OnMSBSeekBarChangeListener() {

                @Override
                public void onProgressChanged(MHS_SeekBar mhs_SeekBar, int progress,
                                              boolean fromUser) {
                    //根据fromUser解锁onTouchEvent(MotionEvent event)可以传到父控制，或者消费掉MotionEvent
                    //VP_CHS_Pager.setNoScrollOnIntercept(fromUser);
                    MacCfg.inputChannelSel = (int) mhs_SeekBar.getTag();

                    MixerIN_ValShow(progress);
                    FlashMixerInputChannelSel();

                    syncMixerVolume();
                    FlashMixerResetButtomState();


                }
            });

            mMixerItem[j].B_MixerResetVal.setTag(j);
            mMixerItem[j].B_MixerResetVal.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    MacCfg.inputChannelSel = (Integer) view.getTag();

                    FlashMixerInputChannelSel();
                    FlashMixerVolumeData();
                    FlashInputVal();
                    FlashMixerSeekbar();
                }
            });

        }


        for (int j = 0; j < 6; j++) {
            B_MIXERCH6_Ch[j].setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    int temp = 0;
                    for (int i = 0; i < 6; i++) {
                        if (view.getId() == B_MIXERCH6_Ch[i].getId()) {
                            temp = i;
                            break;
                        }
                    }
                    MacCfg.OutputChannelSel = temp;
                    FlashPageUI();
                }
            });
        }

        for (int j = 0; j < 8; j++) {
            B_MIXERCH8_Ch[j].setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    int temp = 0;
                    for (int i = 0; i < 8; i++) {
                        if (view.getId() == B_MIXERCH8_Ch[i].getId()) {
                            temp = i;
                            break;
                        }
                    }
                    MacCfg.OutputChannelSel = temp;
                    FlashPageUI();
                }
            });
        }

        for (int j = 0; j < 10; j++) {
            B_MIXERCH10_Ch[j].setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    int temp = 0;
                    for (int i = 0; i < 10; i++) {
                        if (view.getId() == B_MIXERCH10_Ch[i].getId()) {
                            temp = i;
                            break;
                        }
                    }
                    MacCfg.OutputChannelSel = temp;
                    FlashPageUI();
                }
            });
        }

        for (int j = 0; j < 12; j++) {
            B_MIXERCH12_Ch[j].setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    int temp = 0;
                    for (int i = 0; i < 12; i++) {
                        if (view.getId() == B_MIXERCH12_Ch[i].getId()) {
                            temp = i;
                            break;
                        }
                    }
                    MacCfg.OutputChannelSel = temp;
                    FlashPageUI();
                }
            });
        }
    }
}
