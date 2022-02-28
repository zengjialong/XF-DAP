package com.chs.mt.xf_dap.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chs.mt.xf_dap.R;
import com.chs.mt.xf_dap.datastruct.DataStruct;
import com.chs.mt.xf_dap.datastruct.Define;
import com.chs.mt.xf_dap.datastruct.MacCfg;
import com.chs.mt.xf_dap.fragment.dialogFragment.AlertResetOutSPKDialogFragment;
import com.chs.mt.xf_dap.fragment.dialogFragment.LinkDataCoypLeftRight_DialogFragment;
import com.chs.mt.xf_dap.fragment.dialogFragment.SPKDialogFragment;
import com.chs.mt.xf_dap.fragment.dialogFragment.SetFreqDialogFragment;
import com.chs.mt.xf_dap.fragment.dialogFragment.filter_selectDialogFragment;
import com.chs.mt.xf_dap.fragment.dialogFragment.oct_selectDialogFragment;
import com.chs.mt.xf_dap.main.MainTURTActivity;
import com.chs.mt.xf_dap.operation.DataOptUtil;
import com.chs.mt.xf_dap.operation.ReturnFuncation;
import com.chs.mt.xf_dap.tools.HorizontalSelectedView;
import com.chs.mt.xf_dap.tools.LongCickButton;
import com.chs.mt.xf_dap.tools.MHS_SeekBar;
import com.chs.mt.xf_dap.tools.wheel.WheelView;
import com.chs.mt.xf_dap.viewItem.V_XoverItem;

import java.util.ArrayList;
import java.util.List;


public class OuputTypeFragment extends Fragment implements View.OnClickListener {
    private Button btn_spk_type, btnpolar, btn_opositipon, btn_mute;
    private MHS_SeekBar seekBar_outputvol;
    private TextView txt_vol;
    private Context mcontext;
    private LinearLayout ly_lock, ly_reset, ly_link, ly_mute;
    private Button btn_lock, btn_link, btn_reset;
    private Toast mToast;
    private LongCickButton btn_inc, btn_sub;
    private V_XoverItem[] v_xoverItems = new V_XoverItem[6];
    /*长按按键的操作：0-减操作，1-加操作*/
    private int SYNC_INCSUB = 0;
    private SPKDialogFragment mSPKDialogFragment = null;
    private List<String> Channellists;
    private WheelView WV_OutVa;
    private HorizontalSelectedView horizontalSelectedView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chs_fragment_output_xover, null);
        mcontext = getActivity().getApplication();
        initView(view);
        FlashPageUI();
        return view;
    }

    public void FlashPageUI() {


        ReturnFuncation.CurrentOutputChannel();

        // setNoClick();
        GetChannelNum();
        Channellists = new ArrayList<String>();
        for (int i = 0; i < MacCfg.OutMax; i++) {
            Channellists.add(DataStruct.CurMacMode.Out.Name[i]);
        }
        setLinkBtnState(MacCfg.bool_OutChLink);
        WV_OutVa.lists(Channellists);


        WV_OutVa.showCount(5).selectTip("").select(MacCfg.OutputChannelSel).listener(new WheelView.OnWheelViewItemSelectListener() {
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
        //  horizontalSelectedView.setData(Channellists);

        for (int i = 0; i < v_xoverItems.length; i++) {
            switch (i) {
                case 0:
//                    for(int j=0;j<DataStruct.CurMacMode.XOver.Fiter.max1;j++){
//                        if(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].h_freq
//                                == DataStruct.CurMacMode.XOver.Fiter.member1[i]){
                    v_xoverItems[i].btn_type.setText(String.valueOf(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].h_freq) + "Hz");
                    // break;
//                        }
//                    }
                    break;
                case 1:
                    if(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].h_level!=0) {
                        for (int j = 0; j < DataStruct.CurMacMode.XOver.Fiter.max1; j++) {
                            if (DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].h_filter
                                    == DataStruct.CurMacMode.XOver.Fiter.member1[j]) {
                                v_xoverItems[i].btn_type.setText(DataStruct.CurMacMode.XOver.Fiter.memberName1[j]);
                                break;
                            }
                        }
                    }else{
                        v_xoverItems[i].btn_type.setText(getResources().getString(R.string.NULL));
                    }

                    break;
                case 2:
                    for (int j = 0; j < DataStruct.CurMacMode.XOver.Level.max1; j++) {
                        if (DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].h_level
                                == DataStruct.CurMacMode.XOver.Level.member1[j]) {
                            v_xoverItems[i].btn_type.setText(DataStruct.CurMacMode.XOver.Level.memberName1[j]);
                            break;
                        }
                    }

                    break;
                case 3:
                    v_xoverItems[i].btn_type.setText(String.valueOf(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].l_freq) + "Hz");
                    break;
                case 4:
                    if(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].l_level!=0){
                        for (int j = 0; j < DataStruct.CurMacMode.XOver.Fiter.max1; j++) {
                            if (DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].l_filter
                                    == DataStruct.CurMacMode.XOver.Fiter.member1[j]) {
                                v_xoverItems[i].btn_type.setText(DataStruct.CurMacMode.XOver.Fiter.memberName1[j]);
                                break;
                            }
                        }

                    }else{
                        v_xoverItems[i].btn_type.setText(getResources().getString(R.string.NULL));
                    }
                    break;
                case 5:
                    for (int j = 0; j < DataStruct.CurMacMode.XOver.Level.max1; j++) {
                        if (DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].l_level
                                == DataStruct.CurMacMode.XOver.Level.member1[j]) {
                            v_xoverItems[i].btn_type.setText(DataStruct.CurMacMode.XOver.Level.memberName1[j]);
                            break;
                        }
                    }
                    break;
            }
        }

        setPolar(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].polar);
        setMute(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].mute);
        seekBar_outputvol.setProgress(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].gain / DataStruct.CurMacMode.Out.StepOutVol);
        txt_vol.setText(String.valueOf(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].gain / DataStruct.CurMacMode.Out.StepOutVol));
        setSpkTyoe(MacCfg.OutputChannelSel, DataOptUtil.GetChannelName(MacCfg.ChannelNumBuf[MacCfg.OutputChannelSel], mcontext));
    }

    private void setNoClick() {
        if (MacCfg.bool_OutChLock) {
            btn_lock.setText(getResources().getString(R.string.Unlock));
            btn_spk_type.setTextColor(getResources().getColor(R.color.gray));
            btn_spk_type.setEnabled(false);
        } else {
            btn_lock.setText(getResources().getString(R.string.Locked));
            btn_spk_type.setTextColor(getResources().getColor(R.color.white));
            btn_spk_type.setEnabled(true);
        }

    }

    private void setSpkTyoe(int index, String mString) {
        btn_spk_type.setText(mString);
    }

    private void initView(View view) {

        v_xoverItems[0] = view.findViewById(R.id.id_freq_1);
        v_xoverItems[1] = view.findViewById(R.id.id_filter_1);
        v_xoverItems[2] = view.findViewById(R.id.id_oct_1);
        v_xoverItems[3] = view.findViewById(R.id.id_freq_2);
        v_xoverItems[4] = view.findViewById(R.id.id_filter_2);
        v_xoverItems[5] = view.findViewById(R.id.id_oct_2);
        WV_OutVa = (WheelView) view.findViewById(R.id.id_output_va_wheelview);
        horizontalSelectedView = view.findViewById(R.id.id_output_va_howheelview);


        for (int i = 0; i < v_xoverItems.length; i++) {
            v_xoverItems[i].setTag(i);
            v_xoverItems[i].btn_up.setTag(i);
            v_xoverItems[i].btn_down.setTag(i);
            v_xoverItems[i].btn_type.setTag(i);
        }

//        btn_lock = view.findViewById(R.id.id_lock_txt);
        btn_spk_type = view.findViewById(R.id.id_b_spktype);
//
        btnpolar = view.findViewById(R.id.id_b_polar);
//
//        btn_opositipon = view.findViewById(R.id.id_btn_output_opposition);
        btn_mute = view.findViewById(R.id.id_b_mute);
        ly_mute = view.findViewById(R.id.id_b_ly_mute);
        txt_vol = view.findViewById(R.id.id_b_output_val);
//
        btn_inc = view.findViewById(R.id.id_button_val_inc);
        btn_sub = view.findViewById(R.id.id_button_val_sub);
        seekBar_outputvol = view.findViewById(R.id.id_sb_weight);
        btn_reset = view.findViewById(R.id.id_d_channel_reset_va);
//
//        ly_lock = view.findViewById(R.id.id_ly_lock_output);
//        ly_reset = view.findViewById(R.id.id_ly_reset_output);
//        ly_link = view.findViewById(R.id.id_ly_link_output);
        btn_link = view.findViewById(R.id.id_b_link);
        initClick();
    }


    private void showFreq(final int index) {//index==0 高通  index==1 低通
        Bundle Freq = new Bundle();
        Freq.putInt(SetFreqDialogFragment.ST_LP_Freq, DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].l_freq);
        Freq.putInt(SetFreqDialogFragment.ST_HP_Freq, DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].h_freq);
        if (index == 0) {
            Freq.putInt(SetFreqDialogFragment.ST_Freq, DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].h_freq);
            Freq.putBoolean(SetFreqDialogFragment.ST_BOOL_HP, true);
        } else {
            Freq.putInt(SetFreqDialogFragment.ST_Freq, DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].l_freq);
            Freq.putBoolean(SetFreqDialogFragment.ST_BOOL_HP, false);
        }
        SetFreqDialogFragment setFreqDialogFragment = new SetFreqDialogFragment();
        setFreqDialogFragment.setArguments(Freq);
        setFreqDialogFragment.show(getActivity().getFragmentManager(), "setFreqDialogFragment");
        setFreqDialogFragment.OnSetFreqDialogFragmentChangeListener(new SetFreqDialogFragment.OnFreqDialogFragmentChangeListener() {

            @Override
            public void onFreqSeekBarListener(int Freq, int type, boolean flag) {
                // TODO Auto-generated method stub
                if (index == 0) {
                    DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].h_freq = Freq;
                    MacCfg.UI_Type = Define.UI_HFreq;
                    DataOptUtil.syncLinkData();
                } else {
                    DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].l_freq = Freq;
                    MacCfg.UI_Type = Define.UI_LFreq;
                    DataOptUtil.syncLinkData();
                }
                FlashPageUI();
            }
        });
    }


    private void showFilter(final int index) {//index==0 高通  index==1 低通
        Bundle bundle = new Bundle();
        if (index == 0) {
            if(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].h_level==0){
                return;
            }
            bundle.putInt(filter_selectDialogFragment.ST_DataOPT, filter_selectDialogFragment.DataOPT_HP);
            bundle.putInt(filter_selectDialogFragment.ST_Data, DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].h_filter);
        } else {
            if(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].l_level==0){
                return;
            }
            bundle.putInt(filter_selectDialogFragment.ST_DataOPT, filter_selectDialogFragment.DataOPT_LP);
            bundle.putInt(filter_selectDialogFragment.ST_Data, DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].l_filter);
        }

        filter_selectDialogFragment filterDialog = new filter_selectDialogFragment();
        filterDialog.setArguments(bundle);
        filterDialog.show(getActivity().getFragmentManager(), "filterDialog");
        filterDialog.OnSetOnClickDialogListener(new filter_selectDialogFragment.SetOnClickDialogListener() {
            @Override
            public void onClickDialogListener(int type, boolean boolClick) {
                if (index == 0) {
                    DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].h_filter =
                            DataStruct.CurMacMode.XOver.Fiter.member1[type];
                    MacCfg.UI_Type = Define.UI_HFilter;
                    DataOptUtil.syncLinkData();
                } else {
                    DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].l_filter =
                            DataStruct.CurMacMode.XOver.Fiter.member1[type];
                    MacCfg.UI_Type = Define.UI_LFilter;
                    DataOptUtil.syncLinkData();
                }

                FlashPageUI();

            }
        });
    }


    private void showOct(final int index) {//index==0 高通  index==1 低通
        Bundle bundle = new Bundle();
        if (index == 0) {
            bundle.putInt(oct_selectDialogFragment.ST_DataOPT, oct_selectDialogFragment.DataOPT_HP);
            bundle.putInt(oct_selectDialogFragment.ST_Data, DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].h_level);
        } else {
            bundle.putInt(oct_selectDialogFragment.ST_DataOPT, oct_selectDialogFragment.DataOPT_LP);
            bundle.putInt(oct_selectDialogFragment.ST_Data, DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].l_level);
        }


        oct_selectDialogFragment otcDialog = new oct_selectDialogFragment();
        otcDialog.setArguments(bundle);
        otcDialog.show(getActivity().getFragmentManager(), "otcDialog");
        otcDialog.OnSetOnClickDialogListener(new oct_selectDialogFragment.SetOnClickDialogListener() {
            @Override
            public void onClickDialogListener(int type, boolean boolClick) {
                if (index == 0) {
                    DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].h_level =
                            DataStruct.CurMacMode.XOver.Level.member1[type];
                    MacCfg.UI_Type = Define.UI_HOct;
                    DataOptUtil.syncLinkData();
                } else {
                    DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].l_level =
                            DataStruct.CurMacMode.XOver.Level.member1[type];
                    MacCfg.UI_Type = Define.UI_LOct;
                    DataOptUtil.syncLinkData();
                }
                FlashPageUI();
            }
        });
    }


    private void Val_INC_SUB(boolean inc) {
        int val = 0;
        if (inc) {//递增

            val = DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].gain / DataStruct.CurMacMode.Out.StepOutVol;
            if (++val > 60) {
                val = 60;
            }
            DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].gain = val * DataStruct.CurMacMode.Out.StepOutVol;
        } else {
            val = DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].gain / DataStruct.CurMacMode.Out.StepOutVol;
            if (--val < 0) {
                val = 0;
            }
            DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].gain = val * DataStruct.CurMacMode.Out.StepOutVol;
        }
        MacCfg.UI_Type = Define.UI_OutVal;
        DataOptUtil.syncLinkData();
        FlashPageUI();
    }


    private void initClick() {
        btn_sub.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                SYNC_INCSUB = 0;
                Val_INC_SUB(false);
            }
        });
        btn_sub.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View arg0) {
                btn_sub.setStart();
                return false;
            }
        });
        btn_sub.setOnLongTouchListener(new LongCickButton.LongTouchListener() {
            @Override
            public void onLongTouch() {
                SYNC_INCSUB = 0;
                Val_INC_SUB(false);
            }
        }, MacCfg.LongClickEventTimeMax);

        btn_inc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                SYNC_INCSUB = 1;
                Val_INC_SUB(true);
            }
        });
        btn_inc.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View arg0) {
                btn_inc.setStart();
                return false;
            }
        });
        btn_inc.setOnLongTouchListener(new LongCickButton.LongTouchListener() {
            @Override
            public void onLongTouch() {
                SYNC_INCSUB = 1;
                Val_INC_SUB(true);
            }
        }, MacCfg.LongClickEventTimeMax);


        for (int i = 0; i < v_xoverItems.length; i++) {
            v_xoverItems[i].btn_type.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = (int) v.getTag();
                    switch (index) {
                        case 0:
                            showFreq(0);
                            break;
                        case 3:
                            showFreq(1);
                            break;
                        case 1:
                            showFilter(0);
                            break;
                        case 4:
                            showFilter(1);
                            break;
                        case 2:
                            showOct(0);
                            break;
                        case 5:
                            showOct(1);
                            break;
                    }

                }
            });

            v_xoverItems[i].btn_up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {   //+按钮
                    int index = (int) v.getTag();
                    switch (index) {
                        case 0:
                            int _hfreq= DataOptUtil.HFreq(true, DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].h_freq);
                            if(_hfreq>DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].l_freq){
                                _hfreq=DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].l_freq;
                            }
                            DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].h_freq =_hfreq;
                            MacCfg.UI_Type = Define.UI_HFreq;
                            DataOptUtil.syncLinkData();
                            break;
                        case 3:
                            int _lfreq= DataOptUtil.HFreq(true, DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].l_freq);
                            if(_lfreq<DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].h_freq){
                                _lfreq=DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].h_freq;
                            }

                            DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].l_freq =_lfreq;
                            MacCfg.UI_Type = Define.UI_LFreq;
                            DataOptUtil.syncLinkData();
                            break;


                        case 2:
                            DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].h_level = DataOptUtil.HFilter(true, DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].h_level, Define.H_Type);
                            MacCfg.UI_Type = Define.UI_HOct;
                            DataOptUtil.syncLinkData();
                            break;
                        case 5:
                            DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].l_level = DataOptUtil.HFilter(true, DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].l_level, Define.H_Type);
                            MacCfg.UI_Type = Define.UI_LOct;
                            DataOptUtil.syncLinkData();
                            break;
                        case 1:
                            if(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].h_level==0){
                                return;
                            }
                            DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].h_filter = DataOptUtil.HFilter(true, DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].h_filter, Define.L_Type);
                            MacCfg.UI_Type = Define.UI_HFilter;
                            DataOptUtil.syncLinkData();
                            break;
                        case 4:
                            if(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].l_level==0){
                                return;
                            }
                            DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].l_filter = DataOptUtil.HFilter(true, DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].l_filter, Define.L_Type);
                            MacCfg.UI_Type = Define.UI_LFilter;
                            DataOptUtil.syncLinkData();
                            break;
                    }
                    FlashPageUI();
                }
            });


            v_xoverItems[i].btn_down.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = (int) v.getTag();
                    switch (index) {

                        case 0:

                            int _hfreq= DataOptUtil.HFreq(false, DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].h_freq);
                            if(_hfreq>DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].l_freq){
                                _hfreq=DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].l_freq;
                            }

                            DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].h_freq = _hfreq;
                            MacCfg.UI_Type = Define.UI_HFreq;
                            DataOptUtil.syncLinkData();
                            break;
                        case 3:
                            int _lfreq=  DataOptUtil.HFreq(false, DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].l_freq);
                            if(_lfreq<DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].h_freq){
                                _lfreq=DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].h_freq;
                            }
                            DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].l_freq =_lfreq;
                            MacCfg.UI_Type = Define.UI_LFreq;
                            DataOptUtil.syncLinkData();
                            break;

                        case 2:
                            DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].h_level = DataOptUtil.HFilter(false, DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].h_level, Define.L_Type);
                            MacCfg.UI_Type = Define.UI_HOct;
                            DataOptUtil.syncLinkData();
                            break;
                        case 5:
                            DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].l_level = DataOptUtil.HFilter(false, DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].l_level, Define.L_Type);
                            MacCfg.UI_Type = Define.UI_LOct;
                            DataOptUtil.syncLinkData();
                            break;

                        case 1:
                            if(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].h_level==0){
                                return;
                            }
                            DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].h_filter = DataOptUtil.HFilter(false, DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].h_filter, Define.H_Type);
                            MacCfg.UI_Type = Define.UI_HFilter;
                            DataOptUtil.syncLinkData();
                            break;
                        case 4:
                            if(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].l_level==0){
                                return;
                            }
                            DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].l_filter = DataOptUtil.HFilter(false, DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].l_filter, Define.L_Type);
                            MacCfg.UI_Type = Define.UI_LFilter;
                            DataOptUtil.syncLinkData();
                            break;
                    }
                    FlashPageUI();
                }

            });


        }

        btn_spk_type.setOnClickListener(this);
        btnpolar.setOnClickListener(this);
//        btn_opositipon.setOnClickListener(this);
        btn_mute.setOnClickListener(this);
        ly_mute.setOnClickListener(this);
        btn_reset.setOnClickListener(this);
//
//        ly_lock.setOnClickListener(this);
//        btn_lock.setOnClickListener(this);
        btn_link.setOnClickListener(this);
//
//        btn_reset.setOnClickListener(this);
        seekBar_outputvol.setProgressMax(DataStruct.CurMacMode.Out.MaxOutVol / DataStruct.CurMacMode.Out.StepOutVol);
        seekBar_outputvol.setOnSeekBarChangeListener(new MHS_SeekBar.OnMSBSeekBarChangeListener() {
            @Override
            public void onProgressChanged(MHS_SeekBar mhs_SeekBar, int progress, boolean fromUser) {
                DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].gain = progress * DataStruct.CurMacMode.Out.StepOutVol;
                txt_vol.setText(String.valueOf(progress));
                MacCfg.UI_Type = Define.UI_OutVal;
                DataOptUtil.syncLinkData();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_b_spktype:
                showSetOutSpkNameDialog();
                break;
//            case R.id.id_btn_output_normal_phase:
//                DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].polar = 0;
//                setPolar(0);
//                MacCfg.UI_Type = Define.UI_OutPolar;
//                DataOptUtil.syncLinkData();
//                break;
            case R.id.id_b_polar:
                if (DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].polar == 0) {
                    DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].polar = 1;
                } else {
                    DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].polar = 0;
                }
                // DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].polar = 1;
                setPolar(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].polar);
                MacCfg.UI_Type = Define.UI_OutPolar;
                DataOptUtil.syncLinkData();
                break;
            case R.id.id_b_mute:
            case R.id.id_b_ly_mute:
                if (DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].mute == 0) {
                    DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].mute = 1;
                } else {
                    DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].mute = 0;
                }
                setMute(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].mute);

                MacCfg.UI_Type = Define.UI_OutMute;
                DataOptUtil.syncLinkData();

                break;
//            case R.id.id_ly_lock_output:
//                if (MacCfg.bool_OutChLock) {
//                    MacCfg.bool_OutChLock = false;
//                } else {
//                    MacCfg.bool_OutChLock = true;
//                }
//                setNoClick();
//                break;
//            case R.id.id_lock_txt:
//                if (MacCfg.bool_OutChLock) {
//                    MacCfg.bool_OutChLock = false;
//                } else {
//                    MacCfg.bool_OutChLock = true;
//                }
//                setNoClick();
//                break;
            case R.id.id_b_link:
                _LINKMODE_SPKTYPE_Dialog();
                break;
            case R.id.id_d_channel_reset_va:
                showOutputSpk();
                break;
        }
    }

    private void setOutputChannelSKP(int channel, int spk) {
        switch (channel) {
            case 0:
                DataStruct.RcvDeviceData.SYS.out1_spk_type = spk;
                break;
            case 1:
                DataStruct.RcvDeviceData.SYS.out2_spk_type = spk;
                break;
            case 2:
                DataStruct.RcvDeviceData.SYS.out3_spk_type = spk;
                break;
            case 3:
                DataStruct.RcvDeviceData.SYS.out4_spk_type = spk;
                break;
            case 4:
                DataStruct.RcvDeviceData.SYS.out5_spk_type = spk;
                break;
            case 5:
                DataStruct.RcvDeviceData.SYS.out6_spk_type = spk;
                break;
            case 6:
                DataStruct.RcvDeviceData.SYS.out7_spk_type = spk;
                break;
            case 7:
                DataStruct.RcvDeviceData.SYS.out8_spk_type = spk;
                break;
            case 8:
                DataStruct.RcvDeviceData.SYS.out9_spk_type = spk;
                break;
            case 9:
                DataStruct.RcvDeviceData.SYS.out10_spk_type = spk;
                break;
            case 10:
                DataStruct.RcvDeviceData.SYS.out11_spk_type = spk;
                break;
            case 11:
                DataStruct.RcvDeviceData.SYS.out12_spk_type = spk;
                break;
            case 12:
                DataStruct.RcvDeviceData.SYS.out13_spk_type = spk;
                break;
            case 13:
                DataStruct.RcvDeviceData.SYS.out14_spk_type = spk;
                break;
            case 14:
                DataStruct.RcvDeviceData.SYS.out15_spk_type = spk;
                break;
            case 15:
                DataStruct.RcvDeviceData.SYS.Proc_Amp_Mode = spk;
                break;
            default:
                break;
        }
    }


    private void showOutputSpk() {
        AlertResetOutSPKDialogFragment setResetOutSPKDialogFragment = new AlertResetOutSPKDialogFragment();
        setResetOutSPKDialogFragment.show(getActivity().getFragmentManager(), "setResetOutSPKDialogFragment");
        setResetOutSPKDialogFragment.OnSetOnClickDialogListener(new AlertResetOutSPKDialogFragment.SetOnClickDialogListener() {
            @Override
            public void onClickDialogListener(int type, boolean boolClick) {
                if (boolClick) {
                    if (type == 0) {
                        setOutputNameEmpty();
                    } else if (type == 1) {
                        setOutputNameDefault(true);
                    }
                    DataStruct.RcvDeviceData.SYS.mode = 0;
                    setLinkState_Unlink();
                    ((MainTURTActivity)getActivity()).setLinkText();
                    FlashPageUI();
                }
            }
        });
    }

    private void setOutputNameEmpty() {
        for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX_USE; i++) {
            setOutputChannelSKP(i, 0);
            DataStruct.RcvDeviceData.OUT_CH[i].h_filter = DataStruct.DefaultDeviceData.OUT_CH[i].h_filter;
            DataStruct.RcvDeviceData.OUT_CH[i].l_filter = DataStruct.DefaultDeviceData.OUT_CH[i].l_filter;
            DataStruct.RcvDeviceData.OUT_CH[i].h_level = DataStruct.DefaultDeviceData.OUT_CH[i].h_level;
            DataStruct.RcvDeviceData.OUT_CH[i].l_level = DataStruct.DefaultDeviceData.OUT_CH[i].l_level;
            DataStruct.RcvDeviceData.OUT_CH[i].h_freq = Define.NULLFreq_HPFreq;
            DataStruct.RcvDeviceData.OUT_CH[i].l_freq = Define.NULLFreq_LPFreq;
        }

        setSpkTyoe(MacCfg.OutputChannelSel, DataOptUtil.GetChannelName(GetChannelNum(MacCfg.OutputChannelSel), mcontext));
        //TODO 混音
        SetInputSourceMixerEmpty();
        //TODO 联调

    }


    void setOutputNameDefault(boolean setdef) {

        for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX_USE; i++) {
            setOutputChannelSKP(i, DataStruct.CurMacMode.Out.SPK_TYPE[i]);
        }


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

        for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX_USE; i++) {
            DataOptUtil.setXOverWithOutputSPKType(i);
        }

        //TODO
        for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX_USE; i++) {
            DataOptUtil.SetInputSourceMixerVol(i);
        }

    }


    //设置清空的混音值
    private void SetInputSourceMixerEmpty() {
        for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX_USE; i++) {
            DataStruct.RcvDeviceData.OUT_CH[i].IN1_Vol = 0;
            DataStruct.RcvDeviceData.OUT_CH[i].IN2_Vol = 0;
            DataStruct.RcvDeviceData.OUT_CH[i].IN3_Vol = 0;
            DataStruct.RcvDeviceData.OUT_CH[i].IN4_Vol = 0;

            DataStruct.RcvDeviceData.OUT_CH[i].IN5_Vol = 0;
            DataStruct.RcvDeviceData.OUT_CH[i].IN6_Vol = 0;
            DataStruct.RcvDeviceData.OUT_CH[i].IN7_Vol = 0;
            DataStruct.RcvDeviceData.OUT_CH[i].IN8_Vol = 0;

            DataStruct.RcvDeviceData.OUT_CH[i].IN9_Vol = 0;
            DataStruct.RcvDeviceData.OUT_CH[i].IN10_Vol = 0;
            DataStruct.RcvDeviceData.OUT_CH[i].IN11_Vol = 0;
            DataStruct.RcvDeviceData.OUT_CH[i].IN12_Vol = 0;

            DataStruct.RcvDeviceData.OUT_CH[i].IN13_Vol = 0;
            DataStruct.RcvDeviceData.OUT_CH[i].IN14_Vol = 0;
            DataStruct.RcvDeviceData.OUT_CH[i].IN15_Vol = 0;
            DataStruct.RcvDeviceData.OUT_CH[i].IN16_Vol = 0;

            //设置默认输出滤波器
            DataStruct.RcvDeviceData.OUT_CH[i].h_filter = 2;
            DataStruct.RcvDeviceData.OUT_CH[i].l_filter = 2;
            DataStruct.RcvDeviceData.OUT_CH[i].h_level = 1;
            DataStruct.RcvDeviceData.OUT_CH[i].l_level = 1;
            DataStruct.RcvDeviceData.OUT_CH[i].h_freq = Define.NULLFreq_HPFreq;
            DataStruct.RcvDeviceData.OUT_CH[i].l_freq = Define.NULLFreq_LPFreq;
        }
    }





    //设置通道输出类型后的联调 wang测试
    void _LINKMODE_SPKTYPE_Dialog() {
        if (!MacCfg.bool_OutChLink) {
            if (CheckChannelCanLink() > 0) {
                LinkDataCoypLeftRight_DialogFragment mLinkDataCoypLeftRightDialogFragment = new LinkDataCoypLeftRight_DialogFragment();
                mLinkDataCoypLeftRightDialogFragment.show(getActivity().getFragmentManager(), "mLinkDataCoypLeftRightDialogFragment");
                mLinkDataCoypLeftRightDialogFragment.OnSetOnClickDialogListener(new LinkDataCoypLeftRight_DialogFragment.SetOnClickDialogListener() {
                    @Override
                    public void onClickDialogListener(int type, boolean boolClick) {
                        MacCfg.bool_OutChLeftRight = boolClick;

                        //通道数据的复制
                        int Dfrom = 0, Dto = 0;
                        for (int i = 0; i < MacCfg.ChannelLinkCnt; i++) {
                            //true:从左复制到右，false:从右复制到左   */
                            if (MacCfg.bool_OutChLeftRight) {
                                Dfrom = MacCfg.ChannelLinkBuf[i][0];
                                Dto = MacCfg.ChannelLinkBuf[i][1];
                            } else {
                                Dfrom = MacCfg.ChannelLinkBuf[i][1];
                                Dto = MacCfg.ChannelLinkBuf[i][0];
                            }
                            DataOptUtil.channelDataCopy(Dfrom, Dto);
                        }
                        setLinkState_Link();
                        FlashPageUI();
                        ( (MainTURTActivity) getActivity()).setLinkText();
                        //刷新界面
                        Intent intentw = new Intent();
                        intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
                        intentw.putExtra("msg", Define.BoardCast_FlashUI_AllPage);
                        intentw.putExtra("state", true);


                    }
                });
            } else {
                ToastMsg(getString(R.string.NoLinkLR));
            }
        } else {
            //不联调
            setLinkState_Unlink();
            ( (MainTURTActivity) getActivity()).setLinkText();
        }
    }

    private void setLinkState_Unlink() {
        MacCfg.bool_OutChLink = false;
        setLinkBtnState(false);
    }


    //设置清空的混音值
//    private void SetInputSourceMixerEmpty(){
//        for(int i=0;i<DataStruct.CurMacMode.Out.OUT_CH_MAX_USE;i++){
//            DataStruct.RcvDeviceData.OUT_CH[i].IN1_Vol = 0;
//            DataStruct.RcvDeviceData.OUT_CH[i].IN2_Vol = 0;
//            DataStruct.RcvDeviceData.OUT_CH[i].IN3_Vol = 0;
//            DataStruct.RcvDeviceData.OUT_CH[i].IN4_Vol = 0;
//
//            DataStruct.RcvDeviceData.OUT_CH[i].IN5_Vol = 0;
//            DataStruct.RcvDeviceData.OUT_CH[i].IN6_Vol = 0;
//            DataStruct.RcvDeviceData.OUT_CH[i].IN7_Vol = 0;
//            DataStruct.RcvDeviceData.OUT_CH[i].IN8_Vol = 0;
//
//            DataStruct.RcvDeviceData.OUT_CH[i].IN9_Vol  = 0;
//            DataStruct.RcvDeviceData.OUT_CH[i].IN10_Vol = 0;
//            DataStruct.RcvDeviceData.OUT_CH[i].IN11_Vol = 0;
//            DataStruct.RcvDeviceData.OUT_CH[i].IN12_Vol = 0;
//
//            DataStruct.RcvDeviceData.OUT_CH[i].IN13_Vol = 0;
//            DataStruct.RcvDeviceData.OUT_CH[i].IN14_Vol = 0;
//            DataStruct.RcvDeviceData.OUT_CH[i].IN15_Vol = 0;
//            DataStruct.RcvDeviceData.OUT_CH[i].IN16_Vol = 0;
//
//            //设置默认输出滤波器
//            DataStruct.RcvDeviceData.OUT_CH[i].h_filter=0;
//            DataStruct.RcvDeviceData.OUT_CH[i].l_filter=0;
//            DataStruct.RcvDeviceData.OUT_CH[i].h_level=1;
//            DataStruct.RcvDeviceData.OUT_CH[i].l_level=1;
//            DataStruct.RcvDeviceData.OUT_CH[i].h_freq=Define.AllFreq_HPFreq;
//            DataStruct.RcvDeviceData.OUT_CH[i].l_freq=Define.AllFreq_LPFreq;
//        }
//    }


    /**
     * 消息提示
     */
    private void ToastMsg(String Msg) {
        if (null != mToast) {
            mToast.setText(Msg);
        } else {
            mToast = Toast.makeText(mcontext, Msg, Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    private void setLinkState_Link() {
        MacCfg.bool_OutChLink = true;
        setLinkBtnState(true);
    }

    private void setLinkBtnState(boolean Link) {
        if (Link) {

            // btn_link.setText(getString(R.string.cancleLink));
            //  btn_link.setTextColor(getResources().getColor(R.color.output_xover_reset_button_text_color_press));
            btn_link.setBackgroundResource(R.drawable.chs_output_link_reset_color_normal);
//            btn_link.setTextColor(getResources().getColor(R.color.white));
            btn_spk_type.setTextColor(getResources().getColor(R.color.gray));
        } else {

            // btn_link.setText(getString(R.string.Link));
//            btn_link.setTextColor(getResources().getColor(R.color.txt_press));
            btn_link.setBackgroundResource(R.drawable.chs_output_xover_reset_color_normal);
            btn_spk_type.setTextColor(getResources().getColor(R.color.white));
        }
    }


    //检查可设置联调的通道
    private int CheckChannelCanLink() {
        int channelNameNum = 0;
        int channelNameNumEls = 0;
        int res = 0;
        int inc = 0;
        int i = 0, j = 0;
        MacCfg.ChannelLinkCnt = 0;
        for (i = 0; i < MacCfg.MaxOupputNameLinkGroup; i++) {
            for (j = 0; j < 2; j++) {
                MacCfg.ChannelLinkBuf[i][j] = Define.EndFlag;
            }
        }
        for (i = 0; i < MacCfg.OutMax; i++) {
            channelNameNum = GetChannelNum(i);
            if (((channelNameNum >= 0) && (channelNameNum <= 18)) || (channelNameNum == 22) || (channelNameNum == 23)) {
                for (j = i + 1; j < MacCfg.OutMax; j++) {
                    channelNameNumEls = GetChannelNum(j);
//	    			channelNameNumEls=GetChannelNum(B_OutName[j].getText().toString());
                    if ((channelNameNum >= 1) && (channelNameNum <= 6) &&
                            (channelNameNumEls >= 7) && (channelNameNumEls <= 12)) {
                        res = channelNameNumEls - channelNameNum;
                        if (res == 6) {
                            MacCfg.ChannelLinkBuf[inc][0] = i;
                            MacCfg.ChannelLinkBuf[inc][1] = j;
                            ++inc;
                        }
                    } else if ((channelNameNum >= 7) && (channelNameNum <= 12) &&
                            (channelNameNumEls >= 1) && (channelNameNumEls <= 6)) {
                        res = channelNameNum - channelNameNumEls;
                        if (res == 6) {
                            MacCfg.ChannelLinkBuf[inc][0] = j;
                            MacCfg.ChannelLinkBuf[inc][1] = i;
                            ++inc;
                        }
                    } else if ((channelNameNum >= 13) && (channelNameNum <= 15) &&
                            (channelNameNumEls >= 16) && (channelNameNumEls <= 18)) {
                        res = channelNameNumEls - channelNameNum;
                        if (res == 3) {
                            MacCfg.ChannelLinkBuf[inc][0] = i;
                            MacCfg.ChannelLinkBuf[inc][1] = j;
                            ++inc;
                        }
                    } else if ((channelNameNum >= 16) && (channelNameNum <= 18) &&
                            (channelNameNumEls >= 13) && (channelNameNumEls <= 15)) {
                        res = channelNameNum - channelNameNumEls;
                        if (res == 3) {
                            MacCfg.ChannelLinkBuf[inc][0] = j;
                            MacCfg.ChannelLinkBuf[inc][1] = i;
                            ++inc;
                        }
                    } else if ((channelNameNum == 22) && (channelNameNumEls == 23)) {
                        MacCfg.ChannelLinkBuf[inc][0] = i;
                        MacCfg.ChannelLinkBuf[inc][1] = j;
                        ++inc;
                    } else if ((channelNameNum == 23) && (channelNameNumEls == 22)) {
                        MacCfg.ChannelLinkBuf[inc][0] = j;
                        MacCfg.ChannelLinkBuf[inc][1] = i;
                        ++inc;
                    }
                    //System.out.println("CH_NAME res:"+res);
                }
            }

        }
        MacCfg.ChannelLinkCnt = inc;

        return inc;
    }

    private int GetChannelNum(int channel) {
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

        for (int i = 0; i < 16; i++) {
            if (MacCfg.ChannelNumBuf[i] < 0) {
                MacCfg.ChannelNumBuf[i] = 0;
            }
        }

        return MacCfg.ChannelNumBuf[channel];
    }


    //设置正反相UI状态 true 静音
    private void setMute(int index) {
        if (index == 1) {
            btn_mute.setBackgroundResource(R.drawable.chs_output_mute_normal);
        } else {
            btn_mute.setBackgroundResource(R.drawable.chs_mute_press);
        }
    }


    //设置正反相UI状态 true:正相
    private void setPolar(int index) {
        if (index == 0) {
            btnpolar.setText(getResources().getString(R.string.Polar_P));
            btnpolar.setBackgroundResource(R.drawable.chs_btn_oppsition_normal);
        } else {
            //btnpolar.setTextColor(getResources().getColor(R.color.polar_normal));
            btnpolar.setText(getResources().getString(R.string.Polar_N));
            btnpolar.setBackgroundResource(R.drawable.chs_btn_oppsition_press);
            //  btn_opositipon.setBackgroundResource(R.drawable.chs_press_opposition_phase);
        }
    }


    private void showSetOutSpkNameDialog() {
        if (MacCfg.bool_OutChLink) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putInt(SPKDialogFragment.ST_DataOPT, GetChannelNum(MacCfg.OutputChannelSel));

        if (mSPKDialogFragment == null) {
            mSPKDialogFragment = new SPKDialogFragment();
        }
        if (!mSPKDialogFragment.isAdded()) {
            mSPKDialogFragment.setArguments(bundle);
            mSPKDialogFragment.show(getActivity().getFragmentManager(), "mSPKDialogFragment");
        }
        mSPKDialogFragment.OnSetOnClickDialogListener(new SPKDialogFragment.SetOnClickDialogListener() {

            @Override
            public void onClickDialogListener(int type, boolean boolClick) {
                if (boolClick) {
                    //setSpk(type);
                    //setXOverWithOutputSPKType(type);
                    setOutputChannelSKP(MacCfg.OutputChannelSel, type);
                    setSpkTyoe(MacCfg.OutputChannelSel, DataOptUtil.GetChannelName(type, mcontext));
                    DataOptUtil.SetInputSourceMixerVol(MacCfg.OutputChannelSel);
                    if(type==0) {
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                DataOptUtil.setXOverWithOutputSPKType(MacCfg.OutputChannelSel);
                                FlashPageUI();
                            }
                        }, 1000);

                    }else{
                        DataOptUtil.setXOverWithOutputSPKType(MacCfg.OutputChannelSel);
                        FlashPageUI();
                    }



                }
            }
        });

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


    private int getOutputTypeNum(String Name) {

        if (Name.equals(getResources().getString(R.string.NULL))) {
            return 0;
        } else if (Name.equals(getResources().getString(R.string.FL_Tweeter))) {
            return 1;
        } else if (Name.equals(getResources().getString(R.string.FL_Midrange))) {
            return 2;
        } else if (Name.equals(getResources().getString(R.string.FL_Woofer))) {
            return 3;
        } else if (Name.equals(getResources().getString(R.string.FL_M_T))) {
            return 4;
        } else if (Name.equals(getResources().getString(R.string.FL_M_WF))) {
            return 5;
        } else if (Name.equals(getResources().getString(R.string.FL_Full))) {
            return 6;
        } else if (Name.equals(getResources().getString(R.string.FR_Tweeter))) {
            return 7;
        } else if (Name.equals(getResources().getString(R.string.FR_Midrange))) {
            return 8;
        } else if (Name.equals(getResources().getString(R.string.FR_Woofer))) {
            return 9;
        } else if (Name.equals(getResources().getString(R.string.FR_M_T))) {
            return 10;
        } else if (Name.equals(getResources().getString(R.string.FR_M_WF))) {
            return 11;
        } else if (Name.equals(getResources().getString(R.string.FR_Full))) {
            return 12;
        } else if (Name.equals(getResources().getString(R.string.RL_Tweeter))) {
            return 13;
        } else if (Name.equals(getResources().getString(R.string.RL_Woofer))) {
            return 14;
        } else if (Name.equals(getResources().getString(R.string.RL_Full))) {
            return 15;
        } else if (Name.equals(getResources().getString(R.string.RR_Tweeter))) {
            return 16;
        } else if (Name.equals(getResources().getString(R.string.RR_Woofer))) {
            return 17;
        } else if (Name.equals(getResources().getString(R.string.RR_Full))) {
            return 18;
        } else if (Name.equals(getResources().getString(R.string.C_Tweeter))) {
            return 19;
        } else if (Name.equals(getResources().getString(R.string.C_Woofer))) {
            return 20;
        } else if (Name.equals(getResources().getString(R.string.C_Full))) {
            return 21;
        } else if (Name.equals(getResources().getString(R.string.L_Subweeter))) {
            return 22;
        } else if (Name.equals(getResources().getString(R.string.R_Subweeter))) {
            return 23;
        } else if (Name.equals(getResources().getString(R.string.Subweeter))) {
            return 24;
        }
        return 0;
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("进入");
        FlashPageUI();
    }


}
