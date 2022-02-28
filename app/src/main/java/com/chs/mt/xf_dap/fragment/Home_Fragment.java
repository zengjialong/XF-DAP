package com.chs.mt.xf_dap.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chs.mt.xf_dap.R;
import com.chs.mt.xf_dap.datastruct.DataStruct;
import com.chs.mt.xf_dap.datastruct.Define;
import com.chs.mt.xf_dap.datastruct.MacCfg;
import com.chs.mt.xf_dap.fragment.dialogFragment.InputSourceDialogFragment;
import com.chs.mt.xf_dap.fragment.dialogFragment.UserGOPT_DialogFragment;
import com.chs.mt.xf_dap.fragment.dialogFragment.UserGOPT_Save_DialogFragment;
import com.chs.mt.xf_dap.main.MainInputSourceActivity;
import com.chs.mt.xf_dap.main.MainTURTActivity;
import com.chs.mt.xf_dap.main.ModelChangeActivity;
import com.chs.mt.xf_dap.operation.DataOptUtil;

import com.chs.mt.xf_dap.operation.ReturnFuncation;
import com.chs.mt.xf_dap.tools.KnobViewLine_OutThumb;
import com.chs.mt.xf_dap.tools.LongCickButton;
import com.chs.mt.xf_dap.tools.MainSeekbar;

import java.io.UnsupportedEncodingException;

import static android.app.Activity.RESULT_OK;


public class Home_Fragment extends Fragment {
    private Toast mToast;
    private static Context mContext;

    /*按键长按：true-长按，false-非长按*/
    /****************************   调音界面首页     ****************************/
    private Button[] MACUSEREFF = new Button[Define.MAX_UI_GROUP + 1];//Define.MAX_UI_GROUP
    private LinearLayout[] ly_user = new LinearLayout[Define.MAX_UI_GROUP + 1];//Define.MAX_UI_GROUP
    private int UserGroup = 1;
    private int SYNC_INCSUB = 0;
    //true: select Master
    private Boolean Bool_MasterSub = true;
    private ImageView imgMute;
    private int masterValBuf = 0;
    private TextView txt_main_val;

    private LinearLayout ly_mute;

    private int Val_Type = 0;
    //private MHS_SeekBar SB_Val;


    private KnobViewLine_OutThumb VSB_Master;
    //private Button BtnMaster,BtnSub;
    //private TextView TV_Volume;


    private LongCickButton BtnValInc, BtnValSub;
    //private MHS_SeekBar SB_Val;

    private Button  BtnType,BtnInputsource;

    private RelativeLayout rl_inputsource, rl_model;
    private LinearLayout ly_all_page;

    //////////////////////////////////////////////////////////////
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity().getApplicationContext();
        View view = inflater.inflate(R.layout.chs_fragment_home, container, false);
        initView(view);
        initData();
        initClick();
        FlashPageUI();
        return view;
    }

    private void initView(View view) {
        addToningHomeView(view);
        addMasterVal(view);
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

//    public void InitLoadPageUI(){
//        if(MACUSEREFF[1]==null){
//            return;
//        }
//        addMasterValListener();
//        FlashPageUI();
//    }

    public void FlashPageUI() {
        if (MACUSEREFF[1] == null) {
            return;
        }

        flashModle();
        SetToningHomeUserGroupName();
        flashMasterVol();
        flashInputsource();
        if (DataStruct.RcvDeviceData.SYS.MainvolMuteFlg == 0) {
            setMute(true);
        } else {
            setMute(false);
        }
        flashOutputValFreq();
    }

    /*********************************************************************/
    /***************************    主音量     ****************************/
    /*********************************************************************/
    private void addMasterVal(View view) {
        //BtnHiMode  = (Button) view.findViewById(R.id.id_b_hidemode);


        rl_inputsource = (RelativeLayout) view.findViewById(R.id.id_rl_inputsource);

        rl_model = (RelativeLayout) view.findViewById(R.id.id_rl_model);


        BtnType = (Button) view.findViewById(R.id.id_btn_sound);

        /////////////
        VSB_Master = (KnobViewLine_OutThumb) view.findViewById(R.id.id_sbi_0);
        txt_main_val = view.findViewById(R.id.id_main_val);
        VSB_Master.setMax(DataStruct.CurMacMode.Master.MAX);

        ly_all_page = view.findViewById(R.id.ly_100_ch);


        ly_mute=view.findViewById(R.id.id_ly_mute);

        imgMute = (ImageView) view.findViewById(R.id.id_b_mute);

        BtnInputsource= (Button) view.findViewById(R.id.id_btn_inputsource);

        addMasterValListener();
    }

    private void addMasterValListener() {

        BtnValSub.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                SYNC_INCSUB = 0;
                Val_INC_SUB(false);
            }
        });
        BtnValSub.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View arg0) {
                BtnValSub.setStart();
                return false;
            }
        });
        BtnValSub.setOnLongTouchListener(new LongCickButton.LongTouchListener() {
            @Override
            public void onLongTouch() {
                SYNC_INCSUB = 0;
                Val_INC_SUB(false);
            }
        }, MacCfg.LongClickEventTimeMax);

        BtnValInc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                SYNC_INCSUB = 1;
                Val_INC_SUB(true);
            }
        });
        BtnValInc.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View arg0) {
                BtnValInc.setStart();
                return false;
            }
        });
        BtnValInc.setOnLongTouchListener(new LongCickButton.LongTouchListener() {
            @Override
            public void onLongTouch() {
                SYNC_INCSUB = 1;
                Val_INC_SUB(true);
            }
        }, MacCfg.LongClickEventTimeMax);


        VSB_Master.setMax(66);
        VSB_Master.setProgressChangeListener(new KnobViewLine_OutThumb.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(KnobViewLine_OutThumb view, boolean fromUser, int progress) {
                ReturnFuncation.setProcessVal(progress);
                if (DataStruct.RcvDeviceData.SYS.MainvolMuteFlg == 0) {
                    DataStruct.RcvDeviceData.SYS.MainvolMuteFlg = 1;
                    setMute(false);
                }
                txt_main_val.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(KnobViewLine_OutThumb view, int progress) {

            }

            @Override
            public void onStopTrackingTouch(KnobViewLine_OutThumb view, int progress) {

            }
        });

        rl_inputsource.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(mContext, MainInputSourceActivity.class);
                intent.putExtra("name", getResources().getString(R.string.Ad_InputSourceMain));
                intent.putExtra("index", DataStruct.RcvDeviceData.SYS.input_source);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivityForResult(intent, 1);
            }
        });

        BtnType.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mContext, ModelChangeActivity.class);
                intent.putExtra("name", getResources().getString(R.string.Changemode));
                intent.putExtra("index", DataStruct.RcvDeviceData.SYS.Proc_Amp_Mode);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivityForResult(intent, 1);
            }
        });
        rl_model.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(mContext, ModelChangeActivity.class);
                intent.putExtra("name", getResources().getString(R.string.Changemode));
                intent.putExtra("index", DataStruct.RcvDeviceData.SYS.Proc_Amp_Mode);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivityForResult(intent, 1);
            }
        });



        /*
         * 音量静音
         */
        imgMute.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DataStruct.RcvDeviceData.SYS.MainvolMuteFlg == 0) {
                    DataStruct.RcvDeviceData.SYS.MainvolMuteFlg = 1;
                    MacCfg.MainvolMuteFlg_tmp_click = 1;
                    setMute(false);
                } else {
                    DataStruct.RcvDeviceData.SYS.MainvolMuteFlg = 0;
                    MacCfg.MainvolMuteFlg_tmp_click = 0;
                    setMute(true);
                }
            }
        });

        ly_mute.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DataStruct.RcvDeviceData.SYS.MainvolMuteFlg == 0) {
                    DataStruct.RcvDeviceData.SYS.MainvolMuteFlg = 1;
                    MacCfg.MainvolMuteFlg_tmp_click = 1;
                    setMute(false);
                } else {
                    DataStruct.RcvDeviceData.SYS.MainvolMuteFlg = 0;
                    MacCfg.MainvolMuteFlg_tmp_click = 0;
                    setMute(true);
                }
            }
        });




        ///////
        //  if(DataStruct.CurMacMode.BOOL_INPUT_SOURCE){
        rl_inputsource.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mContext, MainInputSourceActivity.class);
                intent.putExtra("name", getResources().getString(R.string.Ad_InputSourceMain));
                intent.putExtra("index", DataStruct.RcvDeviceData.SYS.input_source);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivityForResult(intent, 1);
            }
        });
        BtnInputsource.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mContext, MainInputSourceActivity.class);
                intent.putExtra("name", getResources().getString(R.string.Ad_InputSourceMain));
                intent.putExtra("index", DataStruct.RcvDeviceData.SYS.input_source);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivityForResult(intent, 1);
            }
        });

    }


    private void Val_INC_SUB(boolean inc) {
        int val = 0;
        val = ReturnFuncation.getMainVol();
        if (inc) {//递增


            if (++val > DataStruct.CurMacMode.Master.MAX) {
                val = DataStruct.CurMacMode.Master.MAX;
            }
            ReturnFuncation.setProcessVal(val);
        } else {
            if (--val < 0) {
                val = 0;
            }
            ReturnFuncation.setProcessVal(val);
        }

        FlashPageUI();
    }


    private void flashModle() {


        if (DataStruct.RcvDeviceData.SYS.Proc_Amp_Mode == 0) {
            BtnType.setText(getResources().getString(R.string.mode_2));
        } else {
            BtnType.setText(getResources().getString(R.string.mode_1));
        }

    }

    /**
     * 刷新通道音量
     */
    public void flashOutputValFreq() {
        float a = (float) (getSubVal() - 600);
        float b = (float) DataStruct.CurMacMode.Out.StepOutVol;
        float c = (float) (Math.round(a / b * 10)) / 10;
        double num = getSubVal() / (double) DataStruct.CurMacMode.Out.StepOutVol - 60;
    }


    private void InputsourceEventDialog() {
        Bundle bundle = new Bundle();
        bundle.putInt(InputSourceDialogFragment.ST_DataOPT, InputSourceDialogFragment.DataOPT_INS);
        InputSourceDialogFragment mInputSourceDialog = new InputSourceDialogFragment();
        mInputSourceDialog.setArguments(bundle);
        mInputSourceDialog.show(getActivity().getFragmentManager(), "mInputSourceDialog");
        mInputSourceDialog.OnSetOnClickDialogListener(new InputSourceDialogFragment.SetOnClickDialogListener() {
            @Override
            public void onClickDialogListener(int type, boolean boolClick) {
                DataStruct.RcvDeviceData.SYS.input_source = DataStruct.CurMacMode.inputsource.inputsource[type];
                BtnInputsource.setText(DataStruct.CurMacMode.inputsource.Name[type]);
                flashMasterVol();
            }
        });
    }



    public void flashInputsource() {
        if (!DataStruct.CurMacMode.BOOL_INPUT_SOURCE) {
            return;
        }
        boolean haveS = false;
        for (int i = 0; i < DataStruct.CurMacMode.inputsource.Max; i++) {
            if (DataStruct.RcvDeviceData.SYS.input_source ==
                    DataStruct.CurMacMode.inputsource.inputsource[i]) {
                BtnInputsource.setText(DataStruct.CurMacMode.inputsource.Name[i]);
                haveS = true;
                break;
            }
        }
        if (!haveS) {
            BtnInputsource.setText(R.string.NULL);
        }
    }





    private void setMute(boolean mute) {
        if (mute) {
            imgMute.setImageResource(R.drawable.chs_mute_press);
            ly_mute.setBackgroundResource(R.drawable.chs_btn_mute_press);
        } else {
            imgMute.setImageResource(R.drawable.chs_mute_normal);
            ly_mute.setBackgroundResource(R.drawable.chs_btn_mute_normal);
        }

    }


    public void flashMasterVol() {
        int val = ReturnFuncation.getMainVol();
        if (DataStruct.RcvDeviceData.SYS.MainvolMuteFlg == 0) {
            imgMute.setImageResource(R.drawable.chs_mute_press);
            ly_mute.setBackgroundResource(R.drawable.chs_btn_mute_press);
        } else {
            imgMute.setImageResource(R.drawable.chs_mute_normal);
            ly_mute.setBackgroundResource(R.drawable.chs_btn_mute_normal);
        }
        txt_main_val.setText(String.valueOf(val));
        VSB_Master.setProgress(val);


    }


    /***************************    音效调用     ****************************/
    private void addToningHomeView(View view) {
        MACUSEREFF[1] = (Button) view.findViewById(R.id.btn_useg_1);
        MACUSEREFF[2] = (Button) view.findViewById(R.id.btn_useg_2);
        MACUSEREFF[3] = (Button) view.findViewById(R.id.btn_useg_3);
        MACUSEREFF[4] = (Button) view.findViewById(R.id.btn_useg_4);
        MACUSEREFF[5] = (Button) view.findViewById(R.id.btn_useg_5);
        MACUSEREFF[6] = (Button) view.findViewById(R.id.btn_useg_6);
        ly_user[1] = (LinearLayout) view.findViewById(R.id.ly_useg_1);
        ly_user[2] = (LinearLayout) view.findViewById(R.id.ly_useg_2);
        ly_user[3] = (LinearLayout) view.findViewById(R.id.ly_useg_3);
        ly_user[4] = (LinearLayout) view.findViewById(R.id.ly_useg_4);
        ly_user[5] = (LinearLayout) view.findViewById(R.id.ly_useg_5);
        ly_user[6] = (LinearLayout) view.findViewById(R.id.ly_useg_6);


        BtnValInc = view.findViewById(R.id.id_button_val_inc);

        BtnValSub = view.findViewById(R.id.id_button_val_sub);


        for (int i = 1; i <= Define.MAX_UI_GROUP; i++) {
            MACUSEREFF[i].setTag(i);
            MACUSEREFF[i].setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    MacCfg.UserGroup = (Integer) view.getTag();

                    if ((DataStruct.isConnecting) && (DataStruct.U0SynDataSucessFlg)) {
                        DealUserGroupSeff();
                    } else {
                        ToastMsg(mContext.getResources().getString(R.string.off_line_mode));
                    }

                }
            });
        }
    }

    private void DealUserGroupSeff() {
        UserGOPT_DialogFragment mUserGOPT_DialogFragment = new UserGOPT_DialogFragment();
        mUserGOPT_DialogFragment.show(getActivity().getFragmentManager(), "mUserGOPT_DialogFragment");
        mUserGOPT_DialogFragment.OnSetOnClickDialogListener(new UserGOPT_DialogFragment.SetOnClickDialogListener() {
            @Override
            public void onClickDialogListener(int type, boolean boolClick) {
                switch (type) {
                    case 0:
                        MacCfg.CurProID = MacCfg.UserGroup;
                        showSaveUserGroupDialog();
                        break;
                    case 1://调用
                        MacCfg.ReadGroup = MacCfg.UserGroup;
                        MacCfg.CurProID = MacCfg.UserGroup;
                        DataOptUtil.ReadGroupData(MacCfg.UserGroup, mContext);
                        ((MainTURTActivity) getActivity()).setLinkText();
                        setUserGroupBg();
                        break;
                    case 2://删除
                        DataOptUtil.DeleteGroup(MacCfg.UserGroup);

                        DataOptUtil.ReadCurID();
                        SetToningHomeUserGroupName();
                        break;

                    case 4:
                        ComponentName componentName = new ComponentName(
                                "cn.madeapps.android.yibaomusic",   //要去启动的App的包名
                                "cn.madeapps.android.yibaomusic.activity.ChooseSoundEffectActivity_");//要去启动的App中的Activity的类名
                        Intent intent = new Intent();
                        intent.setComponent(componentName);
                        startActivityForResult(intent, 101);


                        break;

                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("BUG 碎片化----------->"+requestCode);
        if(requestCode==1){
            //刷新界面
            FlashPageUI();
        }
    }

    private void showSaveUserGroupDialog() {

        Bundle bundle = new Bundle();
        bundle.putInt(UserGOPT_Save_DialogFragment.ST_UserGroup, MacCfg.UserGroup);

        UserGOPT_Save_DialogFragment userGroupDialogFragment = new UserGOPT_Save_DialogFragment();
        userGroupDialogFragment.setArguments(bundle);
        userGroupDialogFragment.show(getActivity().getFragmentManager(), "userGroupDialogFragment");
        userGroupDialogFragment.OnSetUserGroupDialogFragmentChangeListener(new UserGOPT_Save_DialogFragment.OnUserGroupDialogFragmentClickListener() {
            //保存
            @Override
            public void onUserGroupSaveListener(int Index, boolean UserGroupflag) {
                SetToningHomeUserGroupName();
                DataOptUtil.SaveGroupData(MacCfg.UserGroup, mContext);
                //showLoadingDialog();
            }

        });
    }

    /*设置调音主页用户组音效名字*/
    public void SetToningHomeUserGroupName() {
        for (int i = 1; i <= Define.MAX_UI_GROUP; i++) {
            ly_user[i].setBackgroundResource(R.color.nullc);
            if (checkUserGroupByteNull(DataStruct.RcvDeviceData.SYS.UserGroup[i]) && !getGBKString(DataStruct.RcvDeviceData.SYS.UserGroup[i]).equals("")) {

                MACUSEREFF[i].setText(getGBKString(DataStruct.RcvDeviceData.SYS.UserGroup[i]));

            } else {
                MACUSEREFF[i].setText(getResources().getString(R.string.Preset) + String.valueOf(i));
            }
            setUserGroupBg();
        }
    }

    private void setUserGroupBg() {
        for (int i = 1; i <= Define.MAX_UI_GROUP; i++) {
            ly_user[i].setBackgroundResource(R.color.nullc);
        }
        if (MacCfg.CurProID != 0) {
            ly_user[MacCfg.CurProID].setBackgroundResource(R.drawable.chs_ly_user);
        }

    }

    private boolean checkUserGroupByteNull(int[] ug) {
        for (int i = 0; i < 15; i++) {
            if (ug[i] != 0x00) {
                return true;
            }
        }
        return false;
    }

    private void SetUserGroupNameDefault() {
        for (int i = 1; i <= Define.MAX_UI_GROUP; i++) {
            MACUSEREFF[i].setText(getResources().getString(R.string.Preset) + String.valueOf(i));
        }
    }

    private String getGBKString(int[] nameC) {
        byte[] GBK = new byte[16];
        for (int j = 0; j < 16; j++) {
            GBK[j] = 0x00;
        }
        int n = 0;
        String uNameString = null;
        for (int j = 0; j < 13; j++) {
            if (nameC[j] != 0x00) {
                GBK[j] = (byte) nameC[j];
                ++n;
            }
        }
        try {
            byte[] GBKN = new byte[n];
            for (int j = 0; j < n; j++) {
                GBKN[j] = GBK[j];
            }
            uNameString = new String(GBKN, "GBK");
        } catch (UnsupportedEncodingException e) {
            //  Auto-generated catch block
            e.printStackTrace();
        }

        return uNameString;
    }


    /**
     * 设置超低音量
     */
    private void setSubVal(int val) {
        DataStruct.RcvDeviceData.OUT_CH[4].gain = val;
        DataStruct.RcvDeviceData.OUT_CH[5].gain = val;
    }


    /**
     * 设置低音频率
     */
    private void setSubFreq(int val) {
        DataStruct.RcvDeviceData.OUT_CH[4].l_freq = val;
        DataStruct.RcvDeviceData.OUT_CH[5].l_freq = val;
    }

    /**
     * 获取超低音量
     */
    private int getSubVal() {
        return DataStruct.RcvDeviceData.OUT_CH[4].gain >
                DataStruct.RcvDeviceData.OUT_CH[5].gain ?
                DataStruct.RcvDeviceData.OUT_CH[4].gain : DataStruct.RcvDeviceData.OUT_CH[5].gain;
    }


    private void setMainVol(int vol) {
        switch (DataStruct.RcvDeviceData.SYS.input_source) {
            case 1:
                DataStruct.RcvDeviceData.SYS.hi_mode = vol;
                break;
            case 2:
                DataStruct.RcvDeviceData.SYS.blue_gain = vol;
                break;
            case 3:
                DataStruct.RcvDeviceData.SYS.aux_gain = vol;
                break;
        }
    }
}