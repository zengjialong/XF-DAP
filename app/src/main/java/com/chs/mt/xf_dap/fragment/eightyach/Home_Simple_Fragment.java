package com.chs.mt.xf_dap.fragment.eightyach;

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
import com.chs.mt.xf_dap.cache.util.LogUtil;
import com.chs.mt.xf_dap.datastruct.DataStruct;
import com.chs.mt.xf_dap.datastruct.Define;
import com.chs.mt.xf_dap.datastruct.MacCfg;
import com.chs.mt.xf_dap.fragment.dialogFragment.LoadingDialogFragment;
import com.chs.mt.xf_dap.fragment.dialogFragment.UserGOPT_DialogFragment;
import com.chs.mt.xf_dap.fragment.dialogFragment.UserGOPT_Save_DialogFragment;
import com.chs.mt.xf_dap.main.MainInputSourceActivity;
import com.chs.mt.xf_dap.operation.DataOptUtil;

import com.chs.mt.xf_dap.operation.ReturnFuncation;
import com.chs.mt.xf_dap.tools.KnobViewLine_OutThumb;
import com.chs.mt.xf_dap.tools.LongCickButton;
import com.chs.mt.xf_dap.tools.MHS_thumb_half_SeekBar;
import com.chs.mt.xf_dap.tools.MainSeekbar;

import java.io.UnsupportedEncodingException;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;


public class Home_Simple_Fragment extends Fragment {
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
    private ImageView img_SimpleMute;
    private int masterValBuf = 0;
    private TextView  txt_simple_val;
    private LinearLayout ly_mute;

    private MHS_thumb_half_SeekBar sub_val_seekbar, sub_freq_seekbar;
    private int Val_Type = 0;
    private LongCickButton[] BtnValInc = new LongCickButton[3];
    private LongCickButton[] BtnValSub = new LongCickButton[3];
    //private MHS_SeekBar SB_Val;

    private Button btn_inputsouce;



    private TextView txt_subfreq_val, txt_sub_val;


    private KnobViewLine_OutThumb VSB_Master_simple;
    //private Button BtnMaster,BtnSub;
    //private TextView TV_Volume;
    private RelativeLayout rl_inputsource, rl_model;

    // private LongCickButton BtnValInc,BtnValSub;
    //private MHS_SeekBar SB_Val;



    //////////////////////////////////////////////////////////////
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity().getApplicationContext();
        View view = inflater.inflate(R.layout.chs_fragment_home_simple, container, false);
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

    public void FlashPageUI() {
        if (MACUSEREFF[1] == null) {
            return;
        }
        SetToningHomeUserGroupName();
        flashSimpleMasterVol();
        if (DataStruct.RcvDeviceData.SYS.MainvolMuteFlg == 0) {
            setMute(true);
        } else {
            setMute(false);
        }
        flashOutputValFreq();
        FlashFreqDialogSeekBarProgress();
        flashInputSource();
    }

    /*********************************************************************/
    /***************************    主音量     ****************************/
    /*********************************************************************/
    private void addMasterVal(View view) {
        txt_subfreq_val = view.findViewById(R.id.id_subfreq_val);
        txt_sub_val = view.findViewById(R.id.id_sub_val);
        rl_inputsource= view.findViewById(R.id.id_rl_inputsource);
        ly_mute=(LinearLayout) view.findViewById(R.id.id_ly_mute);
        img_SimpleMute = (ImageView) view.findViewById(R.id.id_b_mute);
        VSB_Master_simple = (KnobViewLine_OutThumb) view.findViewById(R.id.id_sbi_0);
        txt_simple_val = view.findViewById(R.id.id_main_val_simple);


        btn_inputsouce = view.findViewById(R.id.id_btn_inputsource);

        BtnValSub[0] = view.findViewById(R.id.id_button_val_sub);
        BtnValSub[1] = view.findViewById(R.id.id_btn_subval_sub);
        BtnValSub[2] = view.findViewById(R.id.id_btn_subfreq_sub);

        BtnValInc[0] = view.findViewById(R.id.id_button_val_inc);
        BtnValInc[1] = view.findViewById(R.id.id_btn_subval_inc);
        BtnValInc[2] = view.findViewById(R.id.id_btn_subfreq_inc);


        sub_val_seekbar = view.findViewById(R.id.id_sub_val_seekbar);
        sub_freq_seekbar = view.findViewById(R.id.id_sub_freq_seekbar);
        for (int i = 0; i < BtnValInc.length; i++) {
            BtnValInc[i].setTag(i);
            BtnValSub[i].setTag(i);
        }

        VSB_Master_simple.setMax(DataStruct.CurMacMode.Master.MAX);
        sub_val_seekbar.setProgressMax(600);
        sub_freq_seekbar.setProgressMax(112);

       addMasterValListener();
    }

    private void addMasterValListener() {

        //  VSB_Master.setMax(66);
        VSB_Master_simple.setProgressChangeListener(new KnobViewLine_OutThumb.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(KnobViewLine_OutThumb view, boolean fromUser, int val) {
                switch (DataStruct.RcvDeviceData.SYS.input_source) {
                    case 1:
                        DataStruct.RcvDeviceData.SYS.hi_mode = val;
                        break;
                    case 2:
                        DataStruct.RcvDeviceData.SYS.blue_gain = val;
                        break;
                    case 0:
                        DataStruct.RcvDeviceData.SYS.device_mode = val;
                        break;
                    case 3:
                        DataStruct.RcvDeviceData.SYS.aux_gain = val;
                        break;
                    default:
                        break;
                }
                if (DataStruct.RcvDeviceData.SYS.MainvolMuteFlg == 0) {
                    DataStruct.RcvDeviceData.SYS.MainvolMuteFlg = 1;
                    setMute(false);
                }
                txt_simple_val.setText(String.valueOf(val));
            }

            @Override
            public void onStartTrackingTouch(KnobViewLine_OutThumb view, int progress) {

            }

            @Override
            public void onStopTrackingTouch(KnobViewLine_OutThumb view, int progress) {

            }

        });
        sub_val_seekbar.setOnSeekBarChangeListener(new MHS_thumb_half_SeekBar.OnMSBSeekBarChangeListener() {
            @Override
            public void onProgressChanged(MHS_thumb_half_SeekBar mhs_SeekBar, int progress, boolean fromUser) {
                setSubVal(progress);
                flashOutputValFreq();
            }
        });



        sub_freq_seekbar.setOnSeekBarChangeListener(new MHS_thumb_half_SeekBar.OnMSBSeekBarChangeListener() {
            @Override
            public void onProgressChanged(MHS_thumb_half_SeekBar mhs_SeekBar, int progress, boolean fromUser) {
                setSubFreq((int) Define.FREQ241[progress]);
                FlashFreqDialogSeekBarProgress();
            }
        });


        for (int i = 0; i < BtnValSub.length; i++) {
            BtnValSub[i].setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    SYNC_INCSUB = 0;
                    Val_Type = (int) arg0.getTag();
                    MasterVol_INC_SUB(false, Val_Type);
                }
            });
            BtnValSub[i].setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View arg0) {
                    Val_Type = (int) arg0.getTag();
                    BtnValSub[(int) arg0.getTag()].setStart();
                    return false;
                }
            });
            BtnValSub[i].setOnLongTouchListener(new LongCickButton.LongTouchListener() {
                @Override
                public void onLongTouch() {
                    SYNC_INCSUB = 0;

                    MasterVol_INC_SUB(false, Val_Type);
                }
            }, MacCfg.LongClickEventTimeMax);


            BtnValInc[i].setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    SYNC_INCSUB = 1;
                    Val_Type = (int) arg0.getTag();
                    MasterVol_INC_SUB(true, Val_Type);
                }
            });
            BtnValInc[i].setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View arg0) {
                    Val_Type = (int) arg0.getTag();
                    BtnValInc[(int) arg0.getTag()].setStart();
                    return false;
                }
            });
            BtnValInc[i].setOnLongTouchListener(new LongCickButton.LongTouchListener() {
                @Override
                public void onLongTouch() {
                    SYNC_INCSUB = 1;
                    MasterVol_INC_SUB(true, Val_Type);
                }
            }, MacCfg.LongClickEventTimeMax);
        }

        /*
         * 音量静音
         */
        img_SimpleMute.setOnClickListener(new OnClickListener() {
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

        btn_inputsouce.setOnClickListener(new OnClickListener() {
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



    }


    /**
     * 刷新通道音量
     */
    public void flashOutputValFreq() {
        sub_val_seekbar.setProgress(getSubVal());
        float a = (float)(getSubVal()-600);
        float b = (float)DataStruct.CurMacMode.Out.StepOutVol;
        float c =(float)(Math.round(a/b*10))/10;
        double num=getSubVal()/(double)DataStruct.CurMacMode.Out.StepOutVol-60;
        txt_sub_val.setText(String.valueOf(c+"dB"));
    }

    /**
     * 刷新通道频率
     */
    public void FlashFreqDialogSeekBarProgress() {
        int fP =  DataOptUtil.getSubFreq();
        txt_subfreq_val.setText(String.valueOf( DataOptUtil.getSubFreq() + "Hz"));
        for (int i = 0; i < 240; i++) {
            if ((fP >= Define.FREQ241[i]) && (fP <= Define.FREQ241[i + 1])) {
                sub_freq_seekbar.setProgress(i + 1);
                return;
            }
        }
    }


    /**
     * 刷新主音量
     */
    public void flashSimpleMasterVol() {
        int val = ReturnFuncation.getMainVol();
        txt_simple_val.setText(String.valueOf(val));
        VSB_Master_simple.setProgress(val);
    }



    /**
     * Type=1  主音量加、减  Type=2  超低音量  Type=3 低音频率
     */
    private void MasterVol_INC_SUB(boolean inc, int Type) {

        int val;
        if (Type == 0) {
            val = ReturnFuncation.getMainVol();
            if (inc) {
                if (++val > DataStruct.CurMacMode.Master.MAX) {
                    val = DataStruct.CurMacMode.Master.MAX;
                }
            } else {
                if (--val < 0) {
                    val = 0;
                }
            }
            // DataStruct.RcvDeviceData.SYS.main_vol = val;
            setMainVol(val);

            if (DataStruct.RcvDeviceData.SYS.MainvolMuteFlg == 0) {
                DataStruct.RcvDeviceData.SYS.MainvolMuteFlg = 1;
                setMute(false);
            }

        } else if (Type == 1) {
            val = getSubVal();
            if (inc) {
                val += 1;
                if (val > DataStruct.CurMacMode.Out.MaxOutVol) {
                    val = DataStruct.CurMacMode.Out.MaxOutVol;
                }
            } else {
                val -= 1;
                if (val < 0) {
                    val = 0;
                }
            }
            setSubVal(val);
        } else {
            val = DataOptUtil.getSubFreq();
            if (inc) {
                val += 1;
                if (val > 500) {
                    val = 500;
                }
            } else {
                val -= 1;
                if (val < DataOptUtil.getMINXSubFreq()) {
                    val = DataOptUtil.getMINXSubFreq();
                }
            }
            setSubFreq(val);
        }
        FlashPageUI();
    }


    private void setMute(boolean mute) {
        if (mute) {
            img_SimpleMute.setImageResource(R.drawable.chs_mute_press);
            ly_mute.setBackgroundResource(R.drawable.chs_btn_mute_press);
        } else {
            img_SimpleMute.setImageResource(R.drawable.chs_mute_normal);
            ly_mute.setBackgroundResource(R.drawable.chs_btn_mute_normal);
        }

    }






    /***************************    音效调用     ****************************/
    private void addToningHomeView(View view) {
        MACUSEREFF[1] = (Button) view.findViewById(R.id.btn_useg_1);
        MACUSEREFF[2] = (Button) view.findViewById(R.id.btn_useg_2);
        MACUSEREFF[3] = (Button) view.findViewById(R.id.btn_useg_3);
        MACUSEREFF[4] = (Button) view.findViewById(R.id.btn_useg_4);
        MACUSEREFF[5] = (Button) view.findViewById(R.id.btn_useg_5);
        MACUSEREFF[6] = (Button) view.findViewById(R.id.btn_useg_6);
        // MACUSEREFF[7] = (Button) view.findViewById(R.id.btn_useg_7);

        ly_user[1] = (LinearLayout) view.findViewById(R.id.ly_useg_1);
        ly_user[2] = (LinearLayout) view.findViewById(R.id.ly_useg_2);
        ly_user[3] = (LinearLayout) view.findViewById(R.id.ly_useg_3);
        ly_user[4] = (LinearLayout) view.findViewById(R.id.ly_useg_4);
        ly_user[5] = (LinearLayout) view.findViewById(R.id.ly_useg_5);
        ly_user[6] = (LinearLayout) view.findViewById(R.id.ly_useg_6);


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


    private void showLoadingDialog() {

        LoadingDialogFragment mLoadingDialog = new LoadingDialogFragment();
        mLoadingDialog.show(getActivity().getFragmentManager(), "mLoadingDialog");
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
                        MacCfg.CurProID = MacCfg.UserGroup;
                        MacCfg.ReadGroup=MacCfg.UserGroup;
                        DataOptUtil.ReadGroupData(MacCfg.UserGroup, mContext);
                        setUserGroupBg();
                        break;
                    case 2://删除
                        String filepathone = "";
                        String filename = "";
                        switch (MacCfg.UserGroup) {
                            case 1:
                                filepathone = MacCfg.Assets_path1;
                                filename = mContext.getResources().getString(R.string.standard);
                                break;
                            case 2:
                                filepathone = MacCfg.Assets_path2;
                                filename = mContext.getResources().getString(R.string.bass);
                                break;
                            case 3:
                                filepathone = MacCfg.Assets_path3;
                                filename = mContext.getResources().getString(R.string.classical);
                                break;
                            case 4:
                                filepathone = MacCfg.Assets_path4;
                                filename = mContext.getResources().getString(R.string.Jazz);
                                break;
                            case 5:
                                filepathone = MacCfg.Assets_path5;
                                filename = mContext.getResources().getString(R.string.popular);
                                break;
                            case 6:
                                filepathone = MacCfg.Assets_path6;
                                filename = mContext.getResources().getString(R.string.Rock);
                                break;
                        }

                        for(int i=0;i<15;i++){
                            DataStruct.RcvDeviceData.SYS.UserGroup[MacCfg.UserGroup][i]=0x00;
                        }

                        byte[] strGBK = null;
                        try {
                            strGBK = filename.getBytes("GBK");
                        } catch (UnsupportedEncodingException e) {

                            e.printStackTrace();
                        }
                        for(int i=0;i<strGBK.length;i++){
                            DataStruct.RcvDeviceData.SYS.UserGroup[MacCfg.UserGroup][i]=(char) strGBK[i];
                        }
                        DataOptUtil.UpdateJsonSingleData(filepathone, mContext);
                        SetUserGroupName(MacCfg.UserGroup, filename);
                        DataOptUtil.setMCUData();
                        DataStruct.SEFF_USER_GROUP_OPT=3;//表示正在删除中
                        showLoadingDialog();
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


    void SetUserGroupName(int UserID, String value) {
        String gname = "";
        gname = String.valueOf(value);
        byte[] strGBK = null;
        try {
            strGBK = gname.getBytes("GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 15; i++) {
            DataStruct.RcvDeviceData.SYS.UserGroup[UserID][i] = 0x00;
        }
        int bn = 0;
        if (strGBK.length > DataStruct.MAX_Name_Size) {
            for (int i = 0; i < DataStruct.MAX_Name_Size; i++) {
                DataStruct.RcvDeviceData.SYS.UserGroup[UserID][i] = (char) strGBK[i];
                if ((strGBK[i] & 0xFF) >= (0xa0)) {
                    ++bn;
                }
            }
            if (((bn % 2) == 1) && ((DataStruct.RcvDeviceData.SYS.UserGroup[UserID][12]) >= (0xa0))) {
                DataStruct.RcvDeviceData.SYS.UserGroup[UserID][12] = 0x00;
            }
        } else {
            for (int i = 0; i < strGBK.length; i++) {
                DataStruct.RcvDeviceData.SYS.UserGroup[UserID][i] = (char) strGBK[i];
            }
        }
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

    private boolean isZh() {
        Locale locale = getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (locale.getLanguage().equals(new Locale("zh", "", "").getLanguage()))
            return true;
        else
            return false;
    }

    /*设置调音主页用户组音效名字*/
    public void SetToningHomeUserGroupName() {
            for (int i = 1; i <= Define.MAX_UI_GROUP; i++) {
                ly_user[i].setBackgroundResource(R.color.nullc);
                if (checkUserGroupByteNull(DataStruct.RcvDeviceData.SYS.UserGroup[i]) && !getGBKString(DataStruct.RcvDeviceData.SYS.UserGroup[i]).equals("")) {
                    if(!isZh()){
                        System.out.println("BUG 这里的值为"+i+"======="+getGBKString(DataStruct.RcvDeviceData.SYS.UserGroup[i]));
                        switch (getGBKString(DataStruct.RcvDeviceData.SYS.UserGroup[i])){
                            case "标准":
                                MACUSEREFF[i].setText(mContext.getString(R.string.standard));
                                break;
                            case "低音":
                                MACUSEREFF[i].setText(mContext.getString(R.string.bass));
                                break;
                            case "古典":
                                MACUSEREFF[i].setText(mContext.getString(R.string.classical));
                                break;
                            case "爵士":
                                MACUSEREFF[i].setText(mContext.getString(R.string.Jazz));
                                break;
                            case "流行":
                                MACUSEREFF[i].setText(mContext.getString(R.string.popular));
                                break;
                            case "摇滚":
                                MACUSEREFF[i].setText(mContext.getString(R.string.Rock));
                                break;
                        }
                    }else{
                        MACUSEREFF[i].setText(getGBKString(DataStruct.RcvDeviceData.SYS.UserGroup[i]));
                    }
                } else {
                      switch (i){
                          case 1:
                             MACUSEREFF[i].setText(getResources().getString(R.string.standard));
                             break;
                          case 2:
                              MACUSEREFF[i].setText(getResources().getString(R.string.bass));
                              break;
                          case 3:
                              MACUSEREFF[i].setText(getResources().getString(R.string.classical));
                              break;
                          case 4:
                              MACUSEREFF[i].setText(getResources().getString(R.string.Jazz));
                              break;
                          case 5:
                              MACUSEREFF[i].setText(getResources().getString(R.string.popular));
                              break;
                          case 6:
                              MACUSEREFF[i].setText(getResources().getString(R.string.Rock));
                              break;
                      }

                }
            }
            setUserGroupBg();
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







    /**
     * 刷新音源选中
     */
    public void flashInputSource() {
        boolean haveS=false;
        for (int i = 0; i < DataStruct.CurMacMode.inputsource.Max; i++) {
            if (DataStruct.RcvDeviceData.SYS.input_source ==
                    DataStruct.CurMacMode.inputsource.inputsource[i]) {
                btn_inputsouce.setText(DataStruct.CurMacMode.inputsource.Name[i]);
                haveS = true;
                break;
            }
        }
        if (!haveS) {
            btn_inputsouce.setText(DataStruct.CurMacMode.inputsource.Name[0]);
        }
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