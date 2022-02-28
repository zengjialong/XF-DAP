package com.chs.mt.xf_dap.fragment.fifty;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import com.chs.mt.xf_dap.R;
import com.chs.mt.xf_dap.datastruct.DataStruct;
import com.chs.mt.xf_dap.datastruct.Define;
import com.chs.mt.xf_dap.datastruct.MacCfg;
import com.chs.mt.xf_dap.fragment.dialogFragment.AlertDialogFragment;
import com.chs.mt.xf_dap.fragment.dialogFragment.SetEQFreqBWGainDialogFragment;
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


public class fifty_eq_Fragment extends Fragment {
    private Toast mToast;
    private static Context mContext;
    private WheelView WV_OutVa;
    private AlertDialogFragment alertDialogFragment = null;
    //  EQ
    private EQ toneHomeEq;
    private V_EQ_Item[] Lyout_EQItem = new V_EQ_Item[31];
    private boolean bool_EQ_ModeFlag = false;
    private Button B_EQSetEQMode, B_EQSetDefault, B_EQSetRecover;
    private boolean bool_ByPass = false;
    private Button encryption;
    //  通道选择
    private static final int UI_OUT_CH_MAX = 6;
    //  通道选择
    private List<String> Channellists;
    //    private ChannelItem_Btn mChannel;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity().getApplicationContext();
        View view = inflater.inflate(R.layout.chs_fragment_fifty_eq, container, false);

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
        FlashPageUI();
        encryption.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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


    /*********************************************************************/
    /***************************    initChannelSel     ****************************/
    /*********************************************************************/
    private void initChannelSel(View view) {

        WV_OutVa = (WheelView) view.findViewById(R.id.id_eq_va_wheelview);
        /**/
        Channellists = new ArrayList<String>();
        for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX_USE; i++) {
            Channellists.add(DataStruct.CurMacMode.Out.Name[i]);
        }

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

    }


    public void FlashPageUI() {
        ((MainTURTActivity)getActivity()).setLinkText();
        if (MacCfg.bool_Encryption) {
            encryption.setVisibility(View.VISIBLE);
            for (int i = 0; i < Define.MAX_CHEQ; i++) {
                Lyout_EQItem[i].MVS_SeekBar.setProgress(DataStruct.DefaultDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].level - DataStruct.CurMacMode.EQ.EQ_LEVEL_MIN);
                Lyout_EQItem[i].B_Gain.setText(ChangeGainValume(DataStruct.DefaultDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].level - DataStruct.CurMacMode.EQ.EQ_LEVEL_MIN));
                Lyout_EQItem[i].B_BW.setText(ChangeBWValume(DataStruct.DefaultDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].bw));
                Lyout_EQItem[i].B_Freq.setText(String.valueOf(DataStruct.DefaultDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].freq));
            }
            toneHomeEq.SetEQData(DataStruct.DefaultDeviceData.OUT_CH[MacCfg.OutputChannelSel]);
        } else {
            encryption.setVisibility(View.GONE);
            FlashChannelUI();
        }
    }
    /**
     * EQ*/
    private void AddViewEqualizer_Pager(View viewEQPage) {
        encryption = (Button) viewEQPage.findViewById(R.id.id_b_encryption);
        toneHomeEq = (EQ) viewEQPage.findViewById(R.id.id_eq_eqfilter_page);
        toneHomeEq.SetEQData(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel]);
        B_EQSetRecover = (Button) viewEQPage.findViewById(R.id.id_b_eq_recover);
        B_EQSetDefault = (Button) viewEQPage.findViewById(R.id.id_b_eq_reset);
        B_EQSetEQMode = (Button) viewEQPage.findViewById(R.id.id_b_eq_mode);


        Lyout_EQItem[0] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_1);
        Lyout_EQItem[1] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_2);
        Lyout_EQItem[2] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_3);
        Lyout_EQItem[3] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_4);
        Lyout_EQItem[4] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_5);
        Lyout_EQItem[5] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_6);
        Lyout_EQItem[6] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_7);
        Lyout_EQItem[7] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_8);
        Lyout_EQItem[8] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_9);
        Lyout_EQItem[9] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_10);

        Lyout_EQItem[10] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_11);
        Lyout_EQItem[11] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_12);
        Lyout_EQItem[12] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_13);
        Lyout_EQItem[13] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_14);
        Lyout_EQItem[14] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_15);
        Lyout_EQItem[15] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_16);
        Lyout_EQItem[16] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_17);
        Lyout_EQItem[17] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_18);
        Lyout_EQItem[18] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_19);
        Lyout_EQItem[19] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_20);

        Lyout_EQItem[20] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_21);
        Lyout_EQItem[21] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_22);
        Lyout_EQItem[22] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_23);
        Lyout_EQItem[23] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_24);
        Lyout_EQItem[24] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_25);
        Lyout_EQItem[25] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_26);
        Lyout_EQItem[26] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_27);
        Lyout_EQItem[27] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_28);
        Lyout_EQItem[28] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_29);
        Lyout_EQItem[29] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_30);
        Lyout_EQItem[30] = (V_EQ_Item) viewEQPage.findViewById(R.id.id_llyout_eq_31);
        AddViewEqualizerListen_Pager();
    }

    private void AddViewEqualizerListen_Pager() {

//        toneHomeEq.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].eq_mode==0){
//                    ModeChange();
//                }else {
//
//                    Intent intent = new Intent();
//                    intent.setClass(getActivity(), EQ_GenterActivity.class);
//                    getActivity().startActivity(intent);
//                }
//
//            }
//        });

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

        B_EQSetEQMode.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                if (DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].eq_mode == 1) {
                    DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].eq_mode = 0;
                    B_EQSetEQMode.setText(R.string.PEQ_MODE);
                    bool_EQ_ModeFlag = false;
                    B_EQSetEQMode.setBackgroundResource(R.drawable.chs_btn_output_set_press);
                    B_EQSetEQMode.setTextColor(getResources().getColor(R.color.eq_page_text_eq_set_press));
                    //B_EQSetEQMode.setTextColor(getResources().getColor(R.color.eq_page_text_eq_set_press));
                    //LY_EQ_Mode.setVisibility(View.VISIBLE);
                } else if (DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].eq_mode == 0) {
                    DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].eq_mode = 1;
                    bool_EQ_ModeFlag = true;
                    B_EQSetEQMode.setText(R.string.GEQ_MODE);
                       B_EQSetEQMode.setBackgroundResource(R.drawable.chs_btn_output_set_normal);
                    B_EQSetEQMode.setTextColor(getResources().getColor(R.color.eq_page_text_eq_set_normal));
                   // B_EQSetEQMode.setTextColor(getResources().getColor(R.color.eq_page_text_eq_set_normal));
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

        for (int i = 0; i < DataStruct.CurMacMode.EQ.Max_EQ; i++) {
            Lyout_EQItem[i].MVS_SeekBar.setProgressMax(DataStruct.CurMacMode.EQ.EQ_Gain_MAX);
            //Lyout_EQItem[i].B_Gain.setClickable(false);
            //Lyout_EQItem[i].B_Freq.setClickable(false);
            //Lyout_EQItem[i].B_BW.setClickable(false);
            Lyout_EQItem[i].MVS_SeekBar.setTag(i);
            Lyout_EQItem[i].MVS_SeekBar.setOnSeekBarChangeListener(new EQ_SeekBar.OnMSBEQSeekBarChangeListener() {
                @Override
                public void onProgressChanged(EQ_SeekBar mvs_SeekBar, int progress, boolean fromUser) {
                    //根据fromUser解锁onTouchEvent(MotionEvent event)可以传到父控制，或者消费掉MotionEvent
                    //VP_CHS_Pager.setNoScrollOnIntercept(fromUser);
                    MacCfg.EQ_Num = (int) mvs_SeekBar.getTag();
                    FlashEQLevel(progress);
                    setEQColor(MacCfg.EQ_Num);
                }
            });

            Lyout_EQItem[i].MVS_SeekBar.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });

            //B_Gain
            Lyout_EQItem[i].B_Gain.setTag(i);
            Lyout_EQItem[i].B_Gain.setOnClickListener(new OnClickListener() {

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

                    SetEQFreqBWGainDialogFragment setEQFreqBWGainDialogFragment = new SetEQFreqBWGainDialogFragment();
                    setEQFreqBWGainDialogFragment.setArguments(bundle);
                    setEQFreqBWGainDialogFragment.show(getActivity().getFragmentManager(), "setEQFreqBWGainDialogFragment");

                    setEQFreqBWGainDialogFragment.OnSetEQFreqBWGainDialogFragmentChangeListener(new SetEQFreqBWGainDialogFragment.OnEQFreqBWGainDialogFragmentChangeListener() {

                        @Override
                        public void onGainSeekBarListener(int Gain, int type, boolean flag) {
                            //刷新图表
                            FlashEQLevel(Gain);
                            Lyout_EQItem[MacCfg.EQ_Num].MVS_SeekBar.setProgress(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[MacCfg.EQ_Num].level - DataStruct.CurMacMode.EQ.EQ_LEVEL_MIN);
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
            Lyout_EQItem[i].B_BW.setTag(i);
            Lyout_EQItem[i].B_BW.setOnClickListener(new OnClickListener() {

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

                    SetEQFreqBWGainDialogFragment setEQFreqBWGainDialogFragment = new SetEQFreqBWGainDialogFragment();
                    setEQFreqBWGainDialogFragment.setArguments(bundle);
                    setEQFreqBWGainDialogFragment.show(getActivity().getFragmentManager(), "setEQFreqBWGainDialogFragment");

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
                            Lyout_EQItem[MacCfg.EQ_Num].B_BW.setText(ChangeBWValume(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[MacCfg.EQ_Num].bw));
                            FlashEQPageUI();
                            //联调
                            flashLinkDataUI(Define.UI_EQ_BW);
                        }
                    });
                }
            });

            //FREQ
            Lyout_EQItem[i].B_Freq.setTag(i);
            Lyout_EQItem[i].B_Freq.setOnClickListener(new OnClickListener() {

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

                    SetEQFreqBWGainDialogFragment setEQFreqBWGainDialogFragment = new SetEQFreqBWGainDialogFragment();
                    setEQFreqBWGainDialogFragment.setArguments(bundle);
                    setEQFreqBWGainDialogFragment.show(getActivity().getFragmentManager(), "setEQFreqBWGainDialogFragment");

                    setEQFreqBWGainDialogFragment.OnSetEQFreqBWGainDialogFragmentChangeListener(new SetEQFreqBWGainDialogFragment.OnEQFreqBWGainDialogFragmentChangeListener() {

                        @Override
                        public void onGainSeekBarListener(int Gain, int type, boolean flag) {
                        }


                        @Override
                        public void onFreqSeekBarListener(int Freq, int type, boolean flag) {
                            DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[MacCfg.EQ_Num].freq = Freq;
                            Lyout_EQItem[MacCfg.EQ_Num].B_Freq.setText(String.valueOf(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[MacCfg.EQ_Num].freq));
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
            Lyout_EQItem[i].LY_ResetEQ.setTag(i);
            Lyout_EQItem[i].LY_ResetEQ.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    MacCfg.EQ_Num = (int) view.getTag();
                    if ((DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[MacCfg.EQ_Num].level == DataStruct.CurMacMode.EQ.EQ_LEVEL_ZERO) &&
                            (DataStruct.GainBuf[MacCfg.OutputChannelSel][MacCfg.EQ_Num] != DataStruct.CurMacMode.EQ.EQ_LEVEL_ZERO)) {
                        DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[MacCfg.EQ_Num].level = DataStruct.GainBuf[MacCfg.OutputChannelSel][MacCfg.EQ_Num];
                        DataStruct.GainBuf[MacCfg.OutputChannelSel][MacCfg.EQ_Num] = DataStruct.CurMacMode.EQ.EQ_LEVEL_ZERO;
                        Lyout_EQItem[MacCfg.EQ_Num].B_ResetEQ.setColorFilter(getResources().getColor(R.color.img_reset));
                    } else if ((DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[MacCfg.EQ_Num].level != DataStruct.CurMacMode.EQ.EQ_LEVEL_ZERO) &&
                            (DataStruct.GainBuf[MacCfg.OutputChannelSel][MacCfg.EQ_Num] == DataStruct.CurMacMode.EQ.EQ_LEVEL_ZERO)) {
                        Lyout_EQItem[MacCfg.EQ_Num].B_ResetEQ.setColorFilter(getResources().getColor(R.color.transparent));
                        DataStruct.GainBuf[MacCfg.OutputChannelSel][MacCfg.EQ_Num] = DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[MacCfg.EQ_Num].level;
                        DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[MacCfg.EQ_Num].level = DataStruct.CurMacMode.EQ.EQ_LEVEL_ZERO;
                    }
                    //刷新图表
                    Lyout_EQItem[MacCfg.EQ_Num].MVS_SeekBar.setProgress(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[MacCfg.EQ_Num].level - DataStruct.CurMacMode.EQ.EQ_LEVEL_MIN);
                    Lyout_EQItem[MacCfg.EQ_Num].B_Gain.setText(ChangeGainValume(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[MacCfg.EQ_Num].level - DataStruct.CurMacMode.EQ.EQ_LEVEL_MIN));
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
    }

    /**设置哪个不可点击*/
    private void setCanNotClick() {
        if (MacCfg.OutputChannelSel < 4) {
            for (int i = 0; i < 3; i++) {
                Lyout_EQItem[i].B_ID.setEnabled(false);
                Lyout_EQItem[i].B_Freq.setEnabled(false);
                Lyout_EQItem[i].B_Gain.setEnabled(false);
                Lyout_EQItem[i].B_BW.setEnabled(false);
                Lyout_EQItem[i].LY_ResetEQ.setEnabled(false);
                Lyout_EQItem[i].B_ResetEQ.setEnabled(false);
                Lyout_EQItem[i].B_ResetEQ.setImageResource(R.drawable.chs_eq_resetg_normal);
                Lyout_EQItem[i].B_Gain.setTextColor(getResources()
                        .getColor(R.color.grey));
                Lyout_EQItem[i].B_BW.setTextColor(getResources().getColor(R.color.grey));
                Lyout_EQItem[i].B_Freq.setTextColor(getResources()
                        .getColor(R.color.grey));
                Lyout_EQItem[i].B_ID.setTextColor(getResources().getColor(R.color.grey));
                Lyout_EQItem[i].MVS_SeekBar.setDrag(false);
                Lyout_EQItem[i].MVS_SeekBar.setProgressFrontColor(getResources()
                        .getColor(R.color.gray));
            }
        }else{
            for (int i = 0; i < 3; i++) {
                Lyout_EQItem[i].B_ID.setEnabled(true);
                Lyout_EQItem[i].B_Freq.setEnabled(true);
                Lyout_EQItem[i].B_Gain.setEnabled(true);
                Lyout_EQItem[i].B_BW.setEnabled(true);
                Lyout_EQItem[i].LY_ResetEQ.setEnabled(true);
                Lyout_EQItem[i].B_ResetEQ.setEnabled(true);
                Lyout_EQItem[i].MVS_SeekBar.setDrag(true);
                Lyout_EQItem[i].MVS_SeekBar.setProgressFrontColor(getResources()
                        .getColor(R.color.equalizer_eq_progress_color));
            }
        }
    }

    public void FlashChannelUI() {

        WV_OutVa.setIndex(MacCfg.OutputChannelSel);

        for (int i = 0; i < Define.MAX_CHEQ; i++) {
            Lyout_EQItem[i].setVisibility(View.GONE);
        }
        if (MacCfg.OutputChannelSel < 4) {
            for (int i = 0; i < 9; i++) {
                Lyout_EQItem[i].setVisibility(View.VISIBLE);

                if(i<3){
                    Lyout_EQItem[0].B_ID.setText(mContext.getString(R.string.BASS));
                    Lyout_EQItem[1].B_ID.setText(mContext.getString(R.string.MID));
                    Lyout_EQItem[2].B_ID.setText(mContext.getString(R.string.TRE));
                }else{
                    Lyout_EQItem[i].B_ID.setText(String.valueOf((i-3) + 1));
                }

                Lyout_EQItem[i].MVS_SeekBar.setProgressMax(DataStruct.CurMacMode.EQ.EQ_Gain_MAX);
            }
        } else if (MacCfg.OutputChannelSel >= 4) {
            for (int i = 0; i < 3; i++) {
                Lyout_EQItem[i].setVisibility(View.VISIBLE);
                Lyout_EQItem[i].B_ID.setText(String.valueOf(i + 1));
                Lyout_EQItem[i].MVS_SeekBar.setProgressMax(DataStruct.CurMacMode.EQ.EQ_Gain_MAX);
            }
        }


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

            Lyout_EQItem[i].MVS_SeekBar.setProgress(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].level - DataStruct.CurMacMode.EQ.EQ_LEVEL_MIN);
            Lyout_EQItem[i].B_Gain.setText(ChangeGainValume(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].level - DataStruct.CurMacMode.EQ.EQ_LEVEL_MIN));
            Lyout_EQItem[i].B_BW.setText(ChangeBWValume(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].bw));
            Lyout_EQItem[i].B_Freq.setText(String.valueOf(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].freq));
        }
        if (DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].eq_mode == 1) {
            bool_EQ_ModeFlag = true;
            B_EQSetEQMode.setText(R.string.GEQ_MODE);
            B_EQSetEQMode.setBackgroundResource(R.drawable.chs_btn_eq_set_normal);

            B_EQSetEQMode.setTextColor(getResources().getColor(R.color.eq_page_text_eq_set_normal));
        } else if (DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].eq_mode == 0) {
            bool_EQ_ModeFlag = false;
            B_EQSetEQMode.setText(R.string.PEQ_MODE);
            B_EQSetEQMode .setBackgroundResource(R.drawable.chs_btn_eq_set_press);
            B_EQSetEQMode.setTextColor(getResources().getColor(R.color.eq_page_text_eq_set_press));
        }
        for (int i = 0; i < DataStruct.CurMacMode.EQ.Max_EQ; i++) {
            DataStruct.GainBuf[MacCfg.OutputChannelSel][i] = DataStruct.CurMacMode.EQ.EQ_LEVEL_ZERO;
        }
//        B_Encryption.setVisibility(View.GONE);
        //设置eq显示数据
        toneHomeEq.SetEQData(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel]);
    }
    private void Set3BClick(boolean canclick) {
        if (!canclick) {
            for (int i = 0; i < Define.MAX_CHEQ; i++) {
                Lyout_EQItem[i].B_Freq.setClickable(true);
                Lyout_EQItem[i].B_BW.setClickable(true);
                Lyout_EQItem[i].B_Gain.setClickable(true);
            }
        } else {
            for (int i = 0; i < Define.MAX_CHEQ; i++) {
                Lyout_EQItem[i].B_Freq.setClickable(false);
                Lyout_EQItem[i].B_BW.setClickable(false);
               // Lyout_EQItem[i].B_Gain.setClickable(false);
            }
        }
    }

    private void ResetEQColor() {
        if (DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].eq_mode == 0) {
            for (int i = 0; i < Define.MAX_CHEQ; i++) {
                Lyout_EQItem[i].B_Freq.setTextColor(getResources().getColor(R.color.eq_page_item_color_normal));
                Lyout_EQItem[i].B_BW.setTextColor(getResources().getColor(R.color.eq_page_item_color_normal));
                Lyout_EQItem[i].B_Gain.setTextColor(getResources().getColor(R.color.eq_page_item_color_normal));
                Lyout_EQItem[i].B_ID.setTextColor(getResources().getColor(R.color.eq_page_item_color_normal));
            }
        } else {
            for (int i = 0; i < Define.MAX_CHEQ; i++) {
                Lyout_EQItem[i].B_Freq.setTextColor(getResources().getColor(R.color.eq_page_item_color_lock));
                Lyout_EQItem[i].B_BW.setTextColor(getResources().getColor(R.color.eq_page_item_color_lock));
                Lyout_EQItem[i].B_Gain.setTextColor(getResources().getColor(R.color.eq_page_item_color_normal));
                Lyout_EQItem[i].B_ID.setTextColor(getResources().getColor(R.color.eq_page_item_color_normal));
            }
        }
    }

    private void ResetEQColor(int num) {
        if (DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].eq_mode == 0) {
            //for(int i=0;i<Define.MAX_CHEQ;i++){
            Lyout_EQItem[num].B_Freq.setTextColor(getResources().getColor(R.color.eq_page_item_color_normal));
            Lyout_EQItem[num].B_BW.setTextColor(getResources().getColor(R.color.eq_page_item_color_normal));
            Lyout_EQItem[num].B_Gain.setTextColor(getResources().getColor(R.color.eq_page_item_color_normal));
            Lyout_EQItem[num].B_ID.setTextColor(getResources().getColor(R.color.eq_page_item_color_normal));
            //}
        } else {
            //for(int i=0;i<Define.MAX_CHEQ;i++){
            Lyout_EQItem[num].B_Freq.setTextColor(getResources().getColor(R.color.eq_page_item_color_lock));
            Lyout_EQItem[num].B_BW.setTextColor(getResources().getColor(R.color.eq_page_item_color_lock));
            Lyout_EQItem[num].B_Gain.setTextColor(getResources().getColor(R.color.eq_page_item_color_normal));
            Lyout_EQItem[num].B_ID.setTextColor(getResources().getColor(R.color.eq_page_item_color_normal));
            //}
        }
    }

    private void setEQColor(int num) {
        ResetEQColor();
        Lyout_EQItem[num].B_ID.setTextColor(getResources().getColor(R.color.eq_page_item_color_press));

        for (int i = 0; i <Lyout_EQItem.length ; i++) {
            Lyout_EQItem[i].MVS_SeekBar.setBackgroundResource(R.color.nullc);
        }
        if(num<3){
            Lyout_EQItem[num+3].MVS_SeekBar.setBackgroundResource(R.drawable.chs_eq_seekbar_normal);
        }else{
            Lyout_EQItem[num].MVS_SeekBar.setBackgroundResource(R.drawable.chs_eq_seekbar_normal);
        }



        if (DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].eq_mode == 0) {
            Lyout_EQItem[num].B_BW.setTextColor(getResources().getColor(R.color.eq_page_item_color_press));
            Lyout_EQItem[num].B_Gain.setTextColor(getResources().getColor(R.color.eq_page_item_color_press));
            Lyout_EQItem[num].B_Freq.setTextColor(getResources().getColor(R.color.eq_page_item_color_press));
        } else {
//			Lyout_EQItem[num].B_BW.setTextColor(getResources().getColor(R.color.eq_page_item_color_lock));
//			Lyout_EQItem[num].B_Gain.setTextColor(getResources().getColor(R.color.eq_page_item_color_lock));
//			Lyout_EQItem[num].B_Freq.setTextColor(getResources().getColor(R.color.eq_page_item_color_lock));

            Lyout_EQItem[num].B_BW.setTextColor(getResources().getColor(R.color.eq_page_item_color_lock));
            Lyout_EQItem[num].B_Gain.setTextColor(getResources().getColor(R.color.eq_page_item_color_press));
            Lyout_EQItem[num].B_Freq.setTextColor(getResources().getColor(R.color.eq_page_item_color_lock));
        }
        setCanNotClick();

    }

    private void RestoreEQTo_EQ_Buf_Form_EQ_Default() {
        //for(int i=0;i<MacCfg.OUT_CH_MAX;i++){
        for (int j = 0; j < Define.MAX_CHEQ; j++) {

            if(DataStruct.RcvDeviceData.SYS.none6==1) {
                if (j <= 2&&MacCfg.OutputChannelSel<4) {
                    continue;
                }
            }
                DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].freq = DataStruct.DefaultDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].freq;
                DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].level = DataStruct.DefaultDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].level;
                DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].bw = DataStruct.DefaultDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].bw;

            DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].shf_db = DataStruct.DefaultDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].shf_db;
            DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].type = DataStruct.DefaultDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].type;
            DataStruct.GainBuf[MacCfg.OutputChannelSel][j] =  DataStruct.CurMacMode.EQ.EQ_LEVEL_ZERO;
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
            if(DataStruct.RcvDeviceData.SYS.none6==1) {
                if (j  <= 2&&MacCfg.OutputChannelSel<4) {
                    continue;
                }
            }
//			BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].freq  = DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].freq;
            DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].level = DataStruct.DefaultDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].level;
//			BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].bw    = DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].bw;
//			BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].shf_db= DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].shf_db;
//			BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].type  = DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].type;
        }
    }

    private void EQ_StoreTo_Cur() {
        for (int j = 0; j < Define.MAX_CHEQ; j++) {
            if(DataStruct.RcvDeviceData.SYS.none6==1) {
                if (j  <= 2&&MacCfg.OutputChannelSel<4) {
                    continue;
                }
            }

            DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].level = DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[j].level;

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
                Lyout_EQItem[i].B_ResetEQ.setColorFilter(getResources().getColor(R.color.transparent));
            } else {
                Lyout_EQItem[i].B_ResetEQ.setColorFilter(getResources().getColor(R.color.img_reset));
            }
        }
    }

    private void CheckEQByPass() {
        bool_ByPass = ByEQPass();
        if (bool_ByPass) {//可以设置直通
            B_EQSetRecover.setText(R.string.Bypass_EQ);
            B_EQSetRecover.setTextColor(getResources().getColor(R.color.eq_page_text_eq_set_press));
            B_EQSetRecover.setBackgroundResource(R.drawable.chs_btn_eq_set_press);
        } else if (!bool_ByPass) {//可以设置恢复
            B_EQSetRecover.setText(R.string.Restore_EQ);
            B_EQSetRecover.setTextColor(getResources().getColor(R.color.eq_page_text_eq_set_normal));
            B_EQSetRecover.setBackgroundResource(R.drawable.chs_btn_eq_set_normal);
        }

    }

    private boolean ByEQPass() {
        boolean res = false;
        for (int i = 0; i < Define.MAX_CHEQ; i++) {
            if(DataStruct.RcvDeviceData.SYS.none6==1){
                if(i<= 2&&MacCfg.OutputChannelSel<4){
                    continue;
                }
                if (DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].level != DataStruct.CurMacMode.EQ.EQ_LEVEL_ZERO) {
                    res = true;
                }
            }else{
                if (DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].level != DataStruct.CurMacMode.EQ.EQ_LEVEL_ZERO) {
                    res = true;
                }
            }

        }
        return res;
    }

    private boolean ByEQPassStore() {
        boolean res = false;
        for (int i = 0; i < Define.MAX_CHEQ; i++) {
            if(DataStruct.RcvDeviceData.SYS.none6==1){
                if(i<= 2&&MacCfg.OutputChannelSel<4){
                    continue;
                }
                if (DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].level !=DataStruct.CurMacMode.EQ.EQ_LEVEL_ZERO) {
                    res = true;
                }
            }else{
                if (DataStruct.BufDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].level != DataStruct.CurMacMode.EQ.EQ_LEVEL_ZERO) {
                    res = true;
                }
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
                 if(DataStruct.RcvDeviceData.SYS.none6==1) {
                     if (i <= 2&&MacCfg.OutputChannelSel<4) {
                         continue;
                     }
                 }
                DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].level = DataStruct.CurMacMode.EQ.EQ_LEVEL_ZERO;
            }
            B_EQSetRecover.setText(R.string.Restore_EQ);
            // B_EQSetRecover.setTextColor(getResources().getColor(R.color.eq_page_text_eq_set_normal));
            B_EQSetRecover.setBackgroundResource(R.drawable.chs_btn_output_set_normal);
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
            //   B_EQSetRecover.setTextColor(getResources().getColor(R.color.eq_page_text_eq_set_press));
            B_EQSetRecover.setBackgroundResource(R.drawable.chs_btn_output_set_press);
        }
        FlashChannelUI();
        //联调
        flashLinkDataUI(Define.UI_EQ_ALL);
    }

    private void FlashEQLevel(int progress) {
        DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[MacCfg.EQ_Num].level = progress + DataStruct.CurMacMode.EQ.EQ_LEVEL_MIN;
        Lyout_EQItem[MacCfg.EQ_Num].B_Gain.setText(ChangeGainValume(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[MacCfg.EQ_Num].level - DataStruct.CurMacMode.EQ.EQ_LEVEL_MIN));

        DataStruct.GainBuf[MacCfg.OutputChannelSel][MacCfg.EQ_Num] = DataStruct.CurMacMode.EQ.EQ_LEVEL_ZERO;
        DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[MacCfg.EQ_Num].level = progress + DataStruct.CurMacMode.EQ.EQ_LEVEL_MIN;
        Lyout_EQItem[MacCfg.EQ_Num].B_Gain.setText(ChangeGainValume(progress));
        if (DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[MacCfg.EQ_Num].level == DataStruct.CurMacMode.EQ.EQ_LEVEL_ZERO) {
            Lyout_EQItem[MacCfg.EQ_Num].B_ResetEQ.setColorFilter(getResources().getColor(R.color.transparent));
        } else {
            Lyout_EQItem[MacCfg.EQ_Num].B_ResetEQ.setColorFilter(getResources().getColor(R.color.img_reset));
        }

        CheckEQByPass();//刷新直通和恢复
        FlashEQPageUI();
        //联调
        flashLinkDataUI(Define.UI_EQ_Level);
    }

}