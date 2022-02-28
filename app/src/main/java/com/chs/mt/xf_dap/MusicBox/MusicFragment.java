package com.chs.mt.xf_dap.MusicBox;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;



import com.chs.mt.xf_dap.R;
import com.chs.mt.xf_dap.datastruct.DataStruct;
import com.chs.mt.xf_dap.datastruct.Define;
import com.chs.mt.xf_dap.datastruct.MacCfg;
import com.chs.mt.xf_dap.fragment.dialogFragment.AlertDialogFragment;
import com.chs.mt.xf_dap.fragment.dialogFragment.DataLoadingDialogFragment;
import com.chs.mt.xf_dap.fragment.dialogFragment.LoadingMusicListDialogFragment;
import com.chs.mt.xf_dap.fragment.dialogFragment.UserGOPT_DialogFragment;
import com.chs.mt.xf_dap.fragment.dialogFragment.UserGOPT_Save_DialogFragment;
import com.chs.mt.xf_dap.operation.AnimationUtil;
import com.chs.mt.xf_dap.operation.DataOptUtil;
import com.chs.mt.xf_dap.tools.LongCickButton;
import com.chs.mt.xf_dap.tools.MHS_SeekBar;
import com.chs.mt.xf_dap.util.ToastUtil;
import com.chs.mt.xf_dap.util.ToolsUtil;
import com.tandong.bottomview.view.BottomView;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MusicFragment extends Fragment {
    private Toast mToast;
    private Context mContext;
    /********************主音量，音效操作*********************/
    private MHS_SeekBar SBMainVal,SBSubMainVal;
    private TextView TVMainVal,TVSubMainVal;
    private Button BtnMute;
    private int MainValBuf = 0;

    /******************** 音乐播放 *********************/
    private LinearLayout LYHideList;
    private RelativeLayout LYList;
    private Button BtnListopen, BtnFlashList;
    private TextView TVTotalFile;
    private boolean BOOLHideList = false;

    private V_MusicPlayItem mMusicItem;
    private ListView LV_File;
    private MFileItemAdapter MFileAdapter;
    private List<MFListInfo> FileListData = new ArrayList<MFListInfo>();
    private Boolean BOOL_HaveROOT = false;


    //底部
    private BottomView bottomView;
    private ListView LV_Music_BV;
    private MBoxItemAdapter MBoxAdapter_BV;
    private List<MMListInfo> ListData_BV = new ArrayList<MMListInfo>();
    private Button BtnModeB_BV;
    private TextView TVMode_BV;
    private LinearLayout LYPlayT_BV;
    private LinearLayout LYMusicNullMsg_BV;
    private boolean BOOL_ShowBList = false;
    private boolean BOOL_ClickList_BV = false;
    private int curPlayMode = 0, Ct;
    private LinearLayout LYMusicNullMsg;

    private View VCover1, VCover2, VCover3,VCover5;

    private View Vcoversource;

    /*********   调音界面首页     **********/
    private Button[] MACUSEREFF = new Button[Define.MAX_UI_GROUP + 1];//Define.MAX_UI_GROUP
    private Button[] SEFFView = new Button[Define.MAX_UI_GROUP + 1];//Define.MAX_UI_GROUP
    private LinearLayout[] SEFFLY = new LinearLayout[Define.MAX_UI_GROUP + 1];
    private int UserGroup = 1;

    private View UserViewLine;
    private RelativeLayout LYUser;
    /*********   音源   **********/
    private Button[] BtnInS = new Button[3];//Define.MAX_UI_GROUP
    private Button[] BtnInSView = new Button[3];//Define.MAX_UI_GROUP
    private LinearLayout[] LYIns = new LinearLayout[3];
    private static int inputSourceIndex = 0;
    /******************** 共用 *********************/
    private LoadingMusicListDialogFragment mLoadingMusicListDialogFragment = null;
    private CHS_Broad_FLASHUI_BroadcastReceiver CHS_Broad_Receiver = null;
    private LongCickButton BtnValInc,BtnSubValInc;/**  BtnValInc=输出音量按键+*/  /**  BtnSubValInc=超低音量按键+*/
    private LongCickButton BtnValSub,BtnSubValSub;/** BtnValSub输出音量按键-*/   /**  BtnSubValSub=超低音量按键+*/
    private LoadingItem mLoading;
    //对话框
    private UserGOPT_DialogFragment mUserGOPT_DialogFragment = null;
    private UserGOPT_Save_DialogFragment userGroupDialogFragment = null;
    private AlertDialogFragment alertDialogFragment = null;
    private DataLoadingDialogFragment mDataLoadingDialogFragment = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.chs_fragment_music, container, false);
        mContext = getActivity().getApplicationContext();
        //动态注册CHS_Broad_BroadcastReceiver
        CHS_Broad_Receiver = new CHS_Broad_FLASHUI_BroadcastReceiver();
        IntentFilter CHS_Broad_filter = new IntentFilter();
        CHS_Broad_filter.addAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
        mContext.registerReceiver(CHS_Broad_Receiver, CHS_Broad_filter);

        initView(view);
        FlashPageUI();
        return view;
    }

    //刷新页面UI
    public void FlashPageUI() {
        if (DataOptUtil.isDataNull()) {
            return;
        }
        showUserGroup(true);
        flashMainVal();
        flashSubVal();

        flashUserGroupName();
        flashInputSource();
        curPlayMode = DataStruct.CurMusic.PlayMode;
        flashMusicID3();
       flashMusicStatus();
        flashFileList();
    }

    /**
     * 刷新超低音量值
     * */
    private void flashSubVal() {
        TVSubMainVal.setText(String.valueOf(getSubVal()));
        SBSubMainVal.setProgress(getSubVal());
    }

    private void initView(View view) {
        initViewOfMainValOfSEFF(view);
        initViewOfMusic(view);
    }

    private void initViewOfMainValOfSEFF(View view) {
        UserViewLine = (View) view.findViewById(R.id.id_userline1);
        LYUser = (RelativeLayout) view.findViewById(R.id.id_user);
        BtnValInc = (LongCickButton) view.findViewById(R.id.id_button_val_inc);/*输出音量按键+*/
        BtnValSub = (LongCickButton) view.findViewById(R.id.id_button_val_sub);/*输出音量按键-*/
        mLoading = (LoadingItem) view.findViewById(R.id.id_load);
        //总音量
        TVMainVal = (TextView) view.findViewById(R.id.id_b_val);
        TVSubMainVal = (TextView) view.findViewById(R.id.id_b_sub_val);

        BtnMute = (Button) view.findViewById(R.id.id_b_mute);
        SBMainVal = (MHS_SeekBar) view.findViewById(R.id.id_sb_main_val);

        BtnSubValInc = (LongCickButton) view.findViewById(R.id.id_button_val_inc_sub);/*输出音量按键+*/
        BtnSubValSub = (LongCickButton) view.findViewById(R.id.id_button_val_sub_sub);/*输出音量按键-*/
        SBSubMainVal = (MHS_SeekBar) view.findViewById(R.id.id_sb_sub_main_val);

        SBMainVal.setProgressMax(DataStruct.CurMacMode.Master.MAX);    //设置主音量可拖动的最大值
        SBSubMainVal.setProgressMax(66);  //设置超低音量可拖动的最大值
        SBMainVal.setOnSeekBarChangeListener(new MHS_SeekBar.OnMSBSeekBarChangeListener() {
            @Override
            public void onProgressChanged(MHS_SeekBar mhs_SeekBar, int progress, boolean fromUser) {

                // DataStruct.RcvDeviceData.SYS.main_vol = progress;

                // voicemusic  获取总音量
                switch (DataStruct.RcvDeviceData.SYS.input_source) {
                    case 1:
                        DataStruct.RcvDeviceData.SYS.hi_mode = progress;
                        break;
                    case 2:
                        DataStruct.RcvDeviceData.SYS.blue_gain = progress;
                        break;
                    case 3:
                        DataStruct.RcvDeviceData.SYS.aux_gain= progress;
                        break;
                    case 5:
                        DataStruct.RcvDeviceData.SYS.none5 = progress;
                        break;
                    default:
                        break;
                }

                if (DataStruct.RcvDeviceData.SYS.MainvolMuteFlg == 0) {
                    DataStruct.RcvDeviceData.SYS.MainvolMuteFlg = 1;
                    BtnMute.setBackgroundResource(R.drawable.chs_mute_normal);
                }


                TVMainVal.setText(String.valueOf(progress));
            }
        });




        SBSubMainVal.setOnSeekBarChangeListener(new MHS_SeekBar.OnMSBSeekBarChangeListener() {
            @Override
            public void onProgressChanged(MHS_SeekBar mhs_SeekBar, int progress, boolean fromUser) {

                setSubVal(progress);
                TVSubMainVal.setText(String.valueOf(progress));
            }
        });


        BtnValInc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //MacCfg.inputChannelSel = (int)arg0.getTag();
                ValIncSub(true);
            }
        });
        BtnValInc.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View arg0) {
                //MacCfg.inputChannelSel = (int)arg0.getTag();
                BtnValInc.setStart();
                return false;
            }
        });
        BtnValInc.setOnLongTouchListener(new LongCickButton.LongTouchListener() {
            @Override
            public void onLongTouch() {
                ValIncSub(true);
            }
        }, MacCfg.LongClickEventTimeMax);

        //音量减
        BtnValSub.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //MacCfg.inputChannelSel = (int)arg0.getTag();
                ValIncSub(false);
            }
        });
        BtnValSub.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View arg0) {
                //MacCfg.inputChannelSel = (int)arg0.getTag();
                BtnValSub.setStart();
                return false;
            }
        });
        BtnValSub.setOnLongTouchListener(new LongCickButton.LongTouchListener() {
            @Override
            public void onLongTouch() {
                ValIncSub(false);
            }
        }, MacCfg.LongClickEventTimeMax);



        BtnSubValInc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ValSubIncSub(true);
            }
        });
        BtnSubValInc.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View arg0) {
                //MacCfg.inputChannelSel = (int)arg0.getTag();
                BtnSubValInc.setStart();
                return false;
            }
        });
        BtnSubValInc.setOnLongTouchListener(new LongCickButton.LongTouchListener() {
            @Override
            public void onLongTouch() {
                ValSubIncSub(true);
            }
        }, MacCfg.LongClickEventTimeMax);

        //音量减
        BtnSubValSub.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //MacCfg.inputChannelSel = (int)arg0.getTag();
                ValSubIncSub(false);
            }
        });
        BtnSubValSub.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View arg0) {
                //MacCfg.inputChannelSel = (int)arg0.getTag();
                BtnSubValSub.setStart();
                return false;
            }
        });
        BtnSubValSub.setOnLongTouchListener(new LongCickButton.LongTouchListener() {
            @Override
            public void onLongTouch() {
                ValSubIncSub(false);
            }
        }, MacCfg.LongClickEventTimeMax);


        BtnMute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //AnimationUtil.AnimScale(mContext,v);
                ScaleAnimation sa = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                sa.setDuration(500);
                sa.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (DataStruct.CurMacMode.Master.DATA_TRANSFER == Define.COM_TYPE_INPUT) {
                            if (DataStruct.RcvDeviceData.IN_CH[0].Valume == 0) {
                                DataStruct.RcvDeviceData.IN_CH[0].Valume = MainValBuf;
                                if (DataStruct.RcvDeviceData.IN_CH[0].Valume == 0) {
                                    BtnMute.setBackgroundResource(R.drawable.chs_mute_press);
                                } else {
                                    BtnMute.setBackgroundResource(R.drawable.chs_mute_normal);
                                }
                            } else {
                                MainValBuf = DataStruct.RcvDeviceData.IN_CH[0].Valume;
                                DataStruct.RcvDeviceData.IN_CH[0].Valume = 0;
                                BtnMute.setBackgroundResource(R.drawable.chs_mute_press);
                            }
                            TVMainVal.setText(String.valueOf(DataStruct.RcvDeviceData.IN_CH[0].Valume));
                            SBMainVal.setProgress(DataStruct.RcvDeviceData.IN_CH[0].Valume);
                        } else {


                            if (DataStruct.RcvDeviceData.SYS.MainvolMuteFlg == 0) {
                                DataStruct.RcvDeviceData.SYS.MainvolMuteFlg = 1;
                                BtnMute.setBackgroundResource(R.drawable.chs_mute_normal);
                            } else {
                                DataStruct.RcvDeviceData.SYS.MainvolMuteFlg = 0;
                                BtnMute.setBackgroundResource(R.drawable.chs_mute_press);
                            }
                        }
                        MacCfg.MainvolMuteFlg_tmp = DataStruct.RcvDeviceData.SYS.MainvolMuteFlg;
                        DataStruct.SendDeviceData.SYS.main_vol = DataStruct.RcvDeviceData.SYS.main_vol;
                        DataStruct.SendDeviceData.SYS.alldelay = DataStruct.RcvDeviceData.SYS.alldelay;
                        DataStruct.SendDeviceData.SYS.noisegate_t = DataStruct.RcvDeviceData.SYS.noisegate_t;
                        DataStruct.SendDeviceData.SYS.AutoSource = DataStruct.RcvDeviceData.SYS.AutoSource;
                        DataStruct.SendDeviceData.SYS.AutoSourcedB = DataStruct.RcvDeviceData.SYS.AutoSourcedB;
                        DataStruct.SendDeviceData.SYS.MainvolMuteFlg = DataStruct.RcvDeviceData.SYS.MainvolMuteFlg;
                        DataStruct.SendDeviceData.SYS.none6 = DataStruct.RcvDeviceData.SYS.none6;

                        DataStruct.SendDeviceData.FrameType = DataStruct.WRITE_CMD;
                        DataStruct.SendDeviceData.DeviceID = 0x01;
                        DataStruct.SendDeviceData.UserID = 0x00;
                        DataStruct.SendDeviceData.DataType = Define.SYSTEM;
                        DataStruct.SendDeviceData.ChannelID = Define.SYSTEM_DATA;
                        DataStruct.SendDeviceData.DataID = 0x00;
                        DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;
                        DataStruct.SendDeviceData.PcCustom = 0x00;
                        DataStruct.SendDeviceData.DataLen = 8;

                        DataStruct.U0SendFrameFlg = 1;
                        DataOptUtil.SendDataToDevice(false);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                v.startAnimation(sa);

            }
        });
        //音源
        LYIns[0] = (LinearLayout) view.findViewById(R.id.id_source0);
        LYIns[1] = (LinearLayout) view.findViewById(R.id.id_source1);
        LYIns[2] = (LinearLayout) view.findViewById(R.id.id_source2);
        // LYIns[3] = (LinearLayout) view.findViewById(R.id.id_source3);
        BtnInS[0] = (Button) LYIns[0].findViewById(R.id.id_b_home_mid_name0);
        BtnInS[1] = (Button) LYIns[1].findViewById(R.id.id_b_home_mid_name0);
        BtnInS[2] = (Button) LYIns[2].findViewById(R.id.id_b_home_mid_name0);
        // BtnInS[3] = (Button) LYIns[3].findViewById(R.id.id_b_home_mid_name0);
        BtnInSView[0] = (Button) LYIns[0].findViewById(R.id.id_v_home_mid_image0);
        BtnInSView[1] = (Button) LYIns[1].findViewById(R.id.id_v_home_mid_image0);
        BtnInSView[2] = (Button) LYIns[2].findViewById(R.id.id_v_home_mid_image0);
        //BtnInSView[3] = (Button) LYIns[3].findViewById(R.id.id_v_home_mid_image0);
        //音效
        SEFFLY[1] = (LinearLayout) view.findViewById(R.id.seff_0);
        SEFFLY[2] = (LinearLayout) view.findViewById(R.id.seff_1);
        SEFFLY[3] = (LinearLayout) view.findViewById(R.id.seff_2);
        SEFFLY[4] = (LinearLayout) view.findViewById(R.id.seff_3);
        SEFFLY[5] = (LinearLayout) view.findViewById(R.id.seff_4);
        SEFFLY[6] = (LinearLayout) view.findViewById(R.id.seff_5);


        MACUSEREFF[1] = (Button) SEFFLY[1].findViewById(R.id.id_b_home_mid_name0);
        MACUSEREFF[2] = (Button) SEFFLY[2].findViewById(R.id.id_b_home_mid_name0);
        MACUSEREFF[3] = (Button) SEFFLY[3].findViewById(R.id.id_b_home_mid_name0);
        MACUSEREFF[4] = (Button) SEFFLY[4].findViewById(R.id.id_b_home_mid_name0);
        MACUSEREFF[5] = (Button) SEFFLY[5].findViewById(R.id.id_b_home_mid_name0);
        MACUSEREFF[6] = (Button) SEFFLY[6].findViewById(R.id.id_b_home_mid_name0);




        SEFFView[1] = (Button) SEFFLY[1].findViewById(R.id.id_v_home_mid_image0);
        SEFFView[2] = (Button) SEFFLY[2].findViewById(R.id.id_v_home_mid_image0);
        SEFFView[3] = (Button) SEFFLY[3].findViewById(R.id.id_v_home_mid_image0);
        SEFFView[4] = (Button) SEFFLY[4].findViewById(R.id.id_v_home_mid_image0);
        SEFFView[5] = (Button) SEFFLY[5].findViewById(R.id.id_v_home_mid_image0);
        SEFFView[6] = (Button) SEFFLY[6].findViewById(R.id.id_v_home_mid_image0);




        for (int i = 1; i <= Define.MAX_UI_GROUP; i++) {
            setSEFFView(i, false);
            MACUSEREFF[i].setTag(i);
            SEFFView[i].setTag(i);
            SEFFLY[i].setTag(i);
//            SEFFLY[i].setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View view) {
//                    UserGroup=(Integer) view.getTag();
//                    if((DataStruct.isConnecting)&&(DataStruct.U0SynDataSucessFlg)){
//                       DealUserGroupSeff();
//                       // showCallDialog();
//                    }else {
//                        ToolsUtil.Toast(mContext,mContext.getResources().getString(R.string.off_line_mode));
//                    }
//
//                }
//            });
            SEFFLY[i].setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    UserGroup = (Integer) view.getTag();
                    System.out.println("BUG 当前的Tag值为" + UserGroup);
                    //AnimationUtil.AnimScale(mContext,view);
                    ScaleAnimation sa = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    sa.setDuration(500);
                    sa.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            if ((DataStruct.isConnecting) && (DataStruct.U0SynDataSucessFlg)) {
                                DealUserGroupSeff();
                                //showCallDialog();
                            } else {
                                ToolsUtil.Toast(mContext, mContext.getResources().getString(R.string.off_line_mode));
                            }
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    view.startAnimation(sa);
                }
            });

//            SEFFView[i].setOnTouchListener(new View.OnTouchListener() {
//
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    UserGroup=(Integer) v.getTag();
//                    switch (event.getAction()) {
//                        case MotionEvent.ACTION_DOWN://按住事件发生后执行代码的区域
//                            setSEFFView(UserGroup,true);
//                            break;
//                        case MotionEvent.ACTION_MOVE://移动事件发生后执行代码的区域
//                            setSEFFView(UserGroup,true);
//                            break;
//                        case MotionEvent.ACTION_UP://松开事件发生后执行代码的区域
//                            setSEFFView(UserGroup,false);
//                            if((DataStruct.isConnecting)&&(DataStruct.U0SynDataSucessFlg)){
//                                showCallDialog();
////                                DataOptUtil.ReadGroupData(UserGroup, mContext);
//                                //DealUserGroupSeff();
//                            }else {
//                                ToolsUtil.Toast(mContext,mContext.getResources().getString(R.string.off_line_mode));
//                            }
//                            break;
//                        default:
//                            setSEFFView(UserGroup,false);
//                            break;
//                    }
//                    return false;
//                }
//
//            });
        }

        BtnInS[0].setText(mContext.getResources().getString(R.string.moni));
        // BtnInS[1].setText(mContext.getResources().getString(R.string.Hi_Level));
        BtnInS[1].setText(mContext.getResources().getString(R.string.Player));
        BtnInS[2].setText(mContext.getResources().getString(R.string.bluetoothpush));
        flashInputSource();
        for (int i = 0; i < LYIns.length; i++) {
            LYIns[i].setTag(i);
            LYIns[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = (int) view.getTag();

                    if (index == 0) {
                        // DataStruct.RcvDeviceData.SYS.aux_mode = 1;
                        DataStruct.RcvDeviceData.SYS.input_source = 1;
                    } else if (index == 1) {
                        if (DataStruct.isConnecting) {
                            if (DataStruct.CurMusic.BoolHaveUHost) {
                                //DataStruct.RcvDeviceData.SYS.aux_mode = 1;
                                DataStruct.RcvDeviceData.SYS.input_source = 5;
                                MDOptUtil.setPUMode(MDef.PUMode_U, false);
                            } else {
                                ToastUtil.showShortToast(mContext, mContext.
                                        getResources().getString(R.string.ChangeUHostNullMsg));
                                return;
                            }
                        } else {
                            DataStruct.RcvDeviceData.SYS.input_source = 2;
                            MDOptUtil.setPUMode(MDef.PUMode_U, false);
                        }
                    } else if (index == 2) {
                        DataStruct.RcvDeviceData.SYS.input_source = 2;
                    }

                    setInputSource(index);
                    MacCfg.input_sourcetemp = DataStruct.RcvDeviceData.SYS.input_source;
                    DataStruct.SendDeviceData.SYS.input_source = DataStruct.RcvDeviceData.SYS.input_source;
                    DataStruct.SendDeviceData.SYS.aux_mode = DataStruct.RcvDeviceData.SYS.aux_mode;
                    DataStruct.SendDeviceData.SYS.device_mode = DataStruct.RcvDeviceData.SYS.device_mode;
                    DataStruct.SendDeviceData.SYS.hi_mode = DataStruct.RcvDeviceData.SYS.hi_mode;
                    DataStruct.SendDeviceData.SYS.blue_gain = DataStruct.RcvDeviceData.SYS.blue_gain;
                    DataStruct.SendDeviceData.SYS.aux_gain = DataStruct.RcvDeviceData.SYS.aux_gain;
                    DataStruct.SendDeviceData.SYS.DigitMod = DataStruct.RcvDeviceData.SYS.DigitMod;
                    DataStruct.SendDeviceData.SYS.none5 = DataStruct.RcvDeviceData.SYS.none5;

                    DataStruct.SendDeviceData.FrameType = DataStruct.WRITE_CMD;
                    DataStruct.SendDeviceData.DeviceID = 0x01;
                    DataStruct.SendDeviceData.UserID = 0x00;
                    DataStruct.SendDeviceData.DataType = Define.SYSTEM;
                    DataStruct.SendDeviceData.ChannelID = Define.PC_SOURCE_SET;
                    DataStruct.SendDeviceData.DataID = 0x00;
                    DataStruct.SendDeviceData.PCFadeInFadeOutFlg = 0x00;
                    DataStruct.SendDeviceData.PcCustom = 0x00;
                    DataStruct.SendDeviceData.DataLen = 8;

                    DataStruct.U0SendFrameFlg = 1;
                    DataOptUtil.SendDataToDevice(false);
                }
            });
        }
    }

    private int getInputVal() {
        int val = 0;
        switch (DataStruct.RcvDeviceData.SYS.input_source) {
            case 1:
                val = DataStruct.RcvDeviceData.SYS.hi_mode;
                break;
            case 2:
                val = DataStruct.RcvDeviceData.SYS.blue_gain;
                break;
            case 3:
                val = DataStruct.RcvDeviceData.SYS.aux_gain;
                break;
            case 5:
                val = DataStruct.RcvDeviceData.SYS.none5;
                break;
            default:
                break;
        }
        return val;
    }


    /**
     * 获取超低该显示那个通道的音量值
     * */
    private int getSubVal() {
        if(DataStruct.RcvDeviceData.OUT_CH[4].gain> DataStruct.RcvDeviceData.OUT_CH[5].gain){
            return DataStruct.RcvDeviceData.OUT_CH[5].gain/DataStruct.CurMacMode.Out.StepOutVol;
        }else {
            return DataStruct.RcvDeviceData.OUT_CH[4].gain/DataStruct.CurMacMode.Out.StepOutVol;
        }

    }

    /**
     * 设置超低的音量
     * */
    private void setSubVal(int val) {
            DataStruct.RcvDeviceData.OUT_CH[4].gain=val*DataStruct.CurMacMode.Out.StepOutVol;
            DataStruct.RcvDeviceData.OUT_CH[5].gain=val*DataStruct.CurMacMode.Out.StepOutVol;
    }

    private void setInputVal(int val) {
        switch (DataStruct.RcvDeviceData.SYS.input_source) {
            case 1:
                DataStruct.RcvDeviceData.SYS.hi_mode = val;
                break;
            case 2:
                DataStruct.RcvDeviceData.SYS.blue_gain = val;
                break;
            case 3:
                DataStruct.RcvDeviceData.SYS.aux_gain = val;
                break;
            case 5:
                DataStruct.RcvDeviceData.SYS.none5 = val;
                break;
            default:
                break;
        }
    }

    //设置音量 true:加
    private void ValIncSub(Boolean INC) {
        int val = getInputVal();
        if (INC) {
            val++;
            if (val > DataStruct.CurMacMode.Master.MAX) {
                val = DataStruct.CurMacMode.Master.MAX;
            }
        } else {
            val--;
            if (val <0) {
                val = 0;
            }
        }
        if (DataStruct.RcvDeviceData.SYS.MainvolMuteFlg == 0) {
            DataStruct.RcvDeviceData.SYS.MainvolMuteFlg = 1;
            BtnMute.setBackgroundResource(R.drawable.chs_mute_normal);
        }

        if (DataStruct.RcvDeviceData.SYS.main_vol == 0) {
            DataStruct.RcvDeviceData.SYS.MainvolMuteFlg = 0;
            BtnMute.setBackgroundResource(R.drawable.chs_mute_press);
        }


        setInputVal(val);
        SBMainVal.setProgress(val);
        TVMainVal.setText(String.valueOf(val));

    }


    //设置超低音量 true:加  false:减
    private void ValSubIncSub(Boolean INC) {
        int val = getSubVal();
        if (INC) {
            val++;
            if (val > DataStruct.CurMacMode.Master.MAX) {
                val = DataStruct.CurMacMode.Master.MAX;
            }
        } else {
            val--;
            if (val <0) {
                val = 0;
            }
        }
        setSubVal(val);
        SBSubMainVal.setProgress(val);
        TVSubMainVal.setText(String.valueOf(val));

    }


    private void changeMute() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                DataStruct.RcvDeviceData.SYS.MainvolMuteFlg = 1;
            }
        }, 500);

    }

    public void flashUHostPlay() {
        ToastUtil.showShortToast(mContext, mContext.getResources().getString(R.string.uhostplayCMsg));

        AnimationUtil.toHideAnim(mContext, LYIns[2]);
        AnimationUtil.toVisibleAnim(mContext, LYIns[2]);

    }

    public void flashInputSource() {

        BtnInS[0].setTextColor(mContext.getResources().getColor(R.color.user_group_set_text_color_normal));
        BtnInSView[0].setBackground(mContext.getResources().getDrawable(R.drawable.inputsource_analog_normal));

        BtnInS[1].setTextColor(mContext.getResources().getColor(R.color.user_group_set_text_color_normal));
        BtnInSView[1].setBackground(mContext.getResources().getDrawable(R.drawable.inputsource_uhoutplay_normal));

        BtnInS[2].setTextColor(mContext.getResources().getColor(R.color.user_group_set_text_color_normal));
        BtnInSView[2].setBackground(mContext.getResources().getDrawable(R.drawable.inputsource_bluetooth_normal));

        if (DataStruct.RcvDeviceData.SYS.input_source == 1) {
            BtnInS[0].setTextColor(mContext.getResources().getColor(R.color.inputsource_color_press));
            BtnInSView[0].setBackground(mContext.getResources().getDrawable(R.drawable.inputsource_analog_press));
        } else if (DataStruct.RcvDeviceData.SYS.input_source == 5) {
            if (DataStruct.CurMusic.BoolHaveUHost) {
                //有U盘
                if (DataStruct.CurMusic.PUMode == MDef.PUMode_U) {
                    BtnInS[1].setTextColor(mContext.getResources().getColor(R.color.inputsource_color_press));
                    BtnInSView[1].setBackground(mContext.getResources().getDrawable(R.drawable.inputsource_uhoutplay_press));
                } else if (DataStruct.CurMusic.PUMode == MDef.PUMode_P) {
                    BtnInS[2].setTextColor(mContext.getResources().getColor(R.color.inputsource_color_press));
                    BtnInSView[2].setBackground(mContext.getResources().getDrawable(R.drawable.inputsource_bluetooth_press));
                } else {
                    if (DataStruct.CurMusic.BoolPhoneBlueMusicPush) {
                        BtnInS[2].setTextColor(mContext.getResources().getColor(R.color.inputsource_color_press));
                        BtnInSView[2].setBackground(mContext.getResources().getDrawable(R.drawable.inputsource_bluetooth_press));
                        //MDOptUtil.setPUMode(MDef.PUMode_P,false);
                    } else {
                        BtnInS[1].setTextColor(mContext.getResources().getColor(R.color.inputsource_color_press));
                        BtnInSView[1].setBackground(mContext.getResources().getDrawable(R.drawable.inputsource_uhoutplay_press));
                        //MDOptUtil.setPUMode(MDef.PUMode_U,false);
                    }
                }
            } else {
                BtnInS[2].setTextColor(mContext.getResources().getColor(R.color.inputsource_color_press));
                BtnInSView[2].setBackground(mContext.getResources().getDrawable(R.drawable.inputsource_bluetooth_press));
            }
        } else if (DataStruct.RcvDeviceData.SYS.input_source == 2) {
            BtnInS[2].setTextColor(mContext.getResources().getColor(R.color.inputsource_color_press));
            BtnInSView[2].setBackground(mContext.getResources().getDrawable(R.drawable.inputsource_bluetooth_press));
        }
    }


    private void setInputSource(int index) {

        inputSourceIndex = index;
        BtnInS[0].setTextColor(mContext.getResources().getColor(R.color.user_group_set_text_color_normal));
        BtnInSView[0].setBackground(mContext.getResources().getDrawable(R.drawable.inputsource_analog_normal));

        BtnInS[1].setTextColor(mContext.getResources().getColor(R.color.user_group_set_text_color_normal));
        BtnInSView[1].setBackground(mContext.getResources().getDrawable(R.drawable.inputsource_uhoutplay_normal));

        BtnInS[2].setTextColor(mContext.getResources().getColor(R.color.user_group_set_text_color_normal));
        BtnInSView[2].setBackground(mContext.getResources().getDrawable(R.drawable.inputsource_bluetooth_normal));

        System.out.println("BUG euaeiawe"+index);
        if (index == 0) {
            BtnInS[0].setTextColor(mContext.getResources().getColor(R.color.inputsource_color_press));
            BtnInSView[0].setBackground(mContext.getResources().getDrawable(R.drawable.inputsource_analog_press));
        } else if (index == 1) {
            BtnInS[1].setTextColor(mContext.getResources().getColor(R.color.inputsource_color_press));
            BtnInSView[1].setBackground(mContext.getResources().getDrawable(R.drawable.inputsource_uhoutplay_press));
        } else if (index == 2) {
            BtnInS[2].setTextColor(mContext.getResources().getColor(R.color.inputsource_color_press));
            BtnInSView[2].setBackground(mContext.getResources().getDrawable(R.drawable.inputsource_bluetooth_press));
        }
        flashMainVal();

    }

    public void showUserGroup(boolean show) {
        if (show) {
//            UserViewLine.setVisibility(View.VISIBLE);
            //
        } else {
//            UserViewLine.setVisibility(View.GONE);
            //   LYUser.setVisibility(View.GONE);
        }
        //  LYUser.setVisibility(View.GONE);

    }

    public void showLoadingDspData(boolean show) {
        DataStruct.BOOL_ShowCheckHeadDialog = show;
        if (show) {
            mLoading.Start();
            mLoading.setVisibility(View.VISIBLE);
        } else {
            mLoading.Stop();
            mLoading.setVisibility(View.GONE);
        }
    }

    /**
     * 刷新主音量
     * */
    public void flashMainVal() {

        int progress = 0;
        if (DataStruct.CurMacMode.Master.DATA_TRANSFER == Define.COM_TYPE_INPUT) {
            DataStruct.CurMacMode.Master.MAX = 60;
            SBMainVal.setProgressMax(60);
            if (DataStruct.RcvDeviceData.IN_CH[0].Valume > 60) {
                DataStruct.RcvDeviceData.IN_CH[0].Valume = 60;
            }
            progress = DataStruct.RcvDeviceData.IN_CH[0].Valume;

            if (DataStruct.RcvDeviceData.IN_CH[0].Valume == 0) {
                BtnMute.setBackgroundResource(R.drawable.chs_mute_press);
            } else {
                BtnMute.setBackgroundResource(R.drawable.chs_mute_normal);
            }
        } else {
            SBMainVal.setProgressMax(DataStruct.CurMacMode.Master.MAX);
            switch (DataStruct.RcvDeviceData.SYS.input_source) {
                case 1:
                    progress = DataStruct.RcvDeviceData.SYS.hi_mode;
                    break;
                case 2:
                    progress = DataStruct.RcvDeviceData.SYS.blue_gain;
                    break;
                case 3:
                    progress = DataStruct.RcvDeviceData.SYS.aux_gain;
                    break;

                case 5:
                    progress = DataStruct.RcvDeviceData.SYS.none5;
                    break;
                default:
                    break;
            }
            if (progress > 66) {
                progress= 66;
            }else if(progress<0){
                progress=0;
            }

            // progress = DataStruct.RcvDeviceData.SYS.main_vol;
            if (DataStruct.RcvDeviceData.SYS.MainvolMuteFlg == 0) {
                BtnMute.setBackgroundResource(R.drawable.chs_mute_press);
            } else {
                BtnMute.setBackgroundResource(R.drawable.chs_mute_normal);
            }

//            if (DataStruct.RcvDeviceData.SYS.main_vol == 0) {
//                DataStruct.RcvDeviceData.SYS.MainvolMuteFlg = 0;
//                BtnMute.setBackgroundResource(R.drawable.chs_mute_press);
//            }
        }
        TVMainVal.setText(String.valueOf(progress));
        SBMainVal.setProgress(progress);
    }

    private void setSEFFView(int index, boolean sel) {
        if (!sel) {
            switch (index) {
                case 1:
                    SEFFView[index].setBackground(mContext.getResources().getDrawable(R.drawable.master_useg_normal_1));
                    MACUSEREFF[index].setTextColor(mContext.getResources().getColor(R.color.user_group_set_text_color_normal));
                    break;
                case 2:
                    SEFFView[index].setBackground(mContext.getResources().getDrawable(R.drawable.master_useg_normal_2));
                    MACUSEREFF[index].setTextColor(mContext.getResources().getColor(R.color.user_group_set_text_color_normal));
                    break;
                case 3:
                    SEFFView[index].setBackground(mContext.getResources().getDrawable(R.drawable.master_useg_normal_3));
                    MACUSEREFF[index].setTextColor(mContext.getResources().getColor(R.color.user_group_set_text_color_normal));
                    break;
                case 4:
                    SEFFView[index].setBackground(mContext.getResources().getDrawable(R.drawable.master_useg_normal_4));
                    MACUSEREFF[index].setTextColor(mContext.getResources().getColor(R.color.user_group_set_text_color_normal));
                    break;
                case 5:
                    SEFFView[index].setBackground(mContext.getResources().getDrawable(R.drawable.master_useg_normal_5));
                    MACUSEREFF[index].setTextColor(mContext.getResources().getColor(R.color.user_group_set_text_color_normal));
                    break;
                case 6:
                    SEFFView[index].setBackground(mContext.getResources().getDrawable(R.drawable.master_useg_normal_6));
                    MACUSEREFF[index].setTextColor(mContext.getResources().getColor(R.color.user_group_set_text_color_normal));
                    break;
            }
        } else {
            switch (index) {
                case 1:
                    SEFFView[index].setBackground(mContext.getResources().getDrawable(R.drawable.master_useg_press_1));
                    MACUSEREFF[index].setTextColor(mContext.getResources().getColor(R.color.user_group_set_text_color_press));
                    break;
                case 2:
                    SEFFView[index].setBackground(mContext.getResources().getDrawable(R.drawable.master_useg_press_2));
                    MACUSEREFF[index].setTextColor(mContext.getResources().getColor(R.color.user_group_set_text_color_press));
                    break;
                case 3:
                    SEFFView[index].setBackground(mContext.getResources().getDrawable(R.drawable.master_useg_press_3));
                    MACUSEREFF[index].setTextColor(mContext.getResources().getColor(R.color.user_group_set_text_color_press));
                    break;
                case 4:
                    SEFFView[index].setBackground(mContext.getResources().getDrawable(R.drawable.master_useg_press_4));
                    MACUSEREFF[index].setTextColor(mContext.getResources().getColor(R.color.user_group_set_text_color_press));
                    break;
                case 5:
                    SEFFView[index].setBackground(mContext.getResources().getDrawable(R.drawable.master_useg_press_5));
                    MACUSEREFF[index].setTextColor(mContext.getResources().getColor(R.color.user_group_set_text_color_press));
                    break;
                case 6:
                    SEFFView[index].setBackground(mContext.getResources().getDrawable(R.drawable.master_useg_press_6));
                    MACUSEREFF[index].setTextColor(mContext.getResources().getColor(R.color.user_group_set_text_color_press));
                    break;


            }
        }

    }


    public void DealUserGroupSeff() {
        if (mUserGOPT_DialogFragment == null) {
            mUserGOPT_DialogFragment = new UserGOPT_DialogFragment();
        }
        if (!mUserGOPT_DialogFragment.isAdded()) {
            mUserGOPT_DialogFragment.show(getActivity().getFragmentManager(), "mUserGOPT_DialogFragment");
        }
        mUserGOPT_DialogFragment.OnSetOnClickDialogListener(new UserGOPT_DialogFragment.SetOnClickDialogListener() {
            @Override
            public void onClickDialogListener(int type, boolean boolClick) {
                switch (type) {
                    case 0:
                        showSaveUserGroupDialog();
                        break;
                    case 1://调用
                        DataOptUtil.ReadGroupData( MacCfg.UserGroup , mContext);
                        break;
                    case 2://删除
                        showDelayUserGroupDialog();
                        break;
                }
            }
        });

    }

    private void showSaveUserGroupDialog() {
        Bundle bundle = new Bundle();
        bundle.putInt(UserGOPT_Save_DialogFragment.ST_UserGroup,  MacCfg.UserGroup );
        if (userGroupDialogFragment == null) {
            userGroupDialogFragment = new UserGOPT_Save_DialogFragment();
        }
        userGroupDialogFragment.setArguments(bundle);
        if (!userGroupDialogFragment.isAdded()) {
            userGroupDialogFragment.show(getActivity().getFragmentManager(), "EnterAdvanceDialog");
        }
        userGroupDialogFragment.OnSetUserGroupDialogFragmentChangeListener(new UserGOPT_Save_DialogFragment.OnUserGroupDialogFragmentClickListener() {
            //保存
            @Override
            public void onUserGroupSaveListener(int Index, boolean UserGroupflag) {
                // flashUserGroupName();
                DataOptUtil.SaveGroupData( MacCfg.UserGroup , mContext);
                FlashPageUI();
            }

        });
    }

    private void showDelayUserGroupDialog() {

    }

    /*设置调音主页用户组音效名字*/
    public void flashUserGroupName() {
        for (int i = 1; i <= Define.MAX_UI_GROUP; i++) {
            if (checkUserGroupByteNull(DataStruct.RcvDeviceData.SYS.UserGroup[i])) {
                MACUSEREFF[i].setText(getGBKString(DataStruct.RcvDeviceData.SYS.UserGroup[i]));
            } else {
                MACUSEREFF[i].setText(mContext.getResources().getString(R.string.Preset) + String.valueOf(i));
            }
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
            MACUSEREFF[i].setText(mContext.getResources().getString(R.string.Preset) + String.valueOf(i));
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
            e.printStackTrace();
        }

        return uNameString;
    }

    /*********************************************************************/
    /******************   Music  TODO  ********************/
    /*********************************************************************/
    private void initViewOfMusic(View view) {
        VCover1 = (View) view.findViewById(R.id.v_cover1);
        VCover2 = (View) view.findViewById(R.id.v_cover2);
        VCover3 = (View) view.findViewById(R.id.v_cover3);
        VCover5 = (View) view.findViewById(R.id.v_cover5);
        Vcoversource = (View) view.findViewById(R.id.v_coversource);
        VCover1.setVisibility(View.GONE);

        VCover2.setVisibility(View.GONE);
        VCover3.setVisibility(View.GONE);
        VCover5.setVisibility(View.GONE);
        Vcoversource.setVisibility(View.GONE);

        TVTotalFile = (TextView) view.findViewById(R.id.id_tv_myfiletotal);
        LYMusicNullMsg = (LinearLayout) view.findViewById(R.id.id_ly_msg);
        BtnFlashList = (Button) view.findViewById(R.id.id_b_flash);
        BtnListopen = (Button) view.findViewById(R.id.id_b_myfile);
        LYList = (RelativeLayout) view.findViewById(R.id.id_ly_listvew_of_file);
        LYHideList = (LinearLayout) view.findViewById(R.id.id_ly_myfile);
        LYHideList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!BOOLHideList) {
                    BOOLHideList = true;
                    BtnListopen.setBackgroundResource(R.drawable.music_index_filelist_press);
                    LYList.setVisibility(View.GONE);
                } else {
                    BOOLHideList = false;
                    BtnListopen.setBackgroundResource(R.drawable.music_index_filelist_normal);
                    LYList.setVisibility(View.VISIBLE);
                }
                AnimationUtil.toVisibleAnim(mContext, BtnListopen);
            }
        });

        BtnFlashList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationUtil.AnimScale(mContext, v);
                if (DataStruct.isConnecting) {
                    if (!DataStruct.CurMusic.BoolHaveUHost) {
                        ToastUtil.showShortToast(getContext(), mContext.getResources().getString(R.string.UHostOut));
                        return;
                    }
                    if (DataStruct.CurMusic.BoolPhoneBlueMusicPush) {
                        ToastUtil.showShortToast(getContext(), mContext.getResources().getString(R.string.bluetoothpush));
                        return;
                    }

                    if ((DataStruct.CurMusic.BoolHaveUPdateFileList)
                            && (DataStruct.CurMusic.BoolHaveUPdateList)
                            && (DataStruct.CurMusic.BoolHaveUpdateFileTotal)
                            ) {
                        MacCfg.BOOL_FlashMusicList = true;
                        ToolsUtil.Toast(mContext, mContext.getString(R.string.LoadMusicListFUMsg));
                        MDOptUtil.reloadMusicFileListSet();
                        DataStruct.dbMFileList_Table.ResetTable();
                        DataStruct.dbMMusicList_Table.ResetTable();
                        FileListData.clear();
                        flashFileList();
                    } else {
                        ToolsUtil.Toast(mContext, mContext.getString(R.string.UpdataMusicListNowMsg));
                    }
                } else {
                    ToolsUtil.Toast(mContext, mContext.getString(R.string.OffLineMsg));
                }
            }
        });

        //列表
        LV_File = (ListView) view.findViewById(R.id.id_listvew_of_file);
        MFileAdapter = new MFileItemAdapter(mContext, FileListData);
        LV_File.setAdapter(MFileAdapter);
        FileListViewListen();
        //底部
        mMusicItem = (V_MusicPlayItem) view.findViewById(R.id.id_music);
        mMusicItem.LYLine.setVisibility(View.GONE);
        mMusicItem.LYItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((DataStruct.comType == Define.COMT_OFF) || !DataStruct.isConnecting) {
                    ToastUtil.showShortToast(getContext(),
                            mContext.getResources().getString(R.string.OffLineMsg));
                    return;
                }
                if (DataStruct.comType >= Define.COMT_PLAY) {
                    if (!DataStruct.CurMusic.BoolPhoneBlueMusicPush) {
                        if (DataStruct.CurMusic.BoolHaveUHost) {
//                            Intent intent = new Intent();
//                            intent.setClass(mContext, MusicLrcActivity.class);
//                            startActivity(intent);
                        } else {
                            ToolsUtil.Toast(mContext, mContext.getString(R.string.UHostOut));
                        }
                    } else {
                        ToolsUtil.Toast(mContext, mContext.getString(R.string.bluetoothpush));
                    }
                }
            }
        });

        mMusicItem.BtnPhoneBlueMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((DataStruct.comType == Define.COMT_OFF) || !DataStruct.isConnecting) {
                    ToastUtil.showShortToast(getContext(), mContext.getResources().getString(R.string.OffLineMsg));
                    return;
                }
                if (!DataStruct.CurMusic.BoolPhoneBlueMusicPush) {
                    showBottomList();
                } else {
                    ToolsUtil.Toast(mContext, mContext.getString(R.string.bluetoothpush));
                }

            }
        });

        VCover1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DataStruct.isConnecting) {
                    ToastUtil.showShortToast(getContext(),
                            mContext.getResources().getString(R.string.offlineDSPMsg));
                } else {
                    ToastUtil.showShortToast(getContext(),
                            mContext.getResources().getString(R.string.off_line_mode));
                }
            }
        });
        VCover2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DataStruct.isConnecting) {
                    ToastUtil.showShortToast(getContext(),
                            mContext.getResources().getString(R.string.offlineDSPMsg));
                } else {
                    ToastUtil.showShortToast(getContext(),
                            mContext.getResources().getString(R.string.off_line_mode));
                }
            }
        });
        Vcoversource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!DataStruct.isConnecting) {
                    ToastUtil.showShortToast(getContext(),
                            mContext.getResources().getString(R.string.off_line_mode));
                }
            }
        });

        VCover3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DataStruct.isConnecting) {
                    ToastUtil.showShortToast(getContext(),
                            mContext.getResources().getString(R.string.offlineDSPPlayMsg));
                } else {
                    ToastUtil.showShortToast(getContext(),
                            mContext.getResources().getString(R.string.off_line_mode));
                }
            }
        });

        VCover5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DataStruct.isConnecting) {
                    ToastUtil.showShortToast(getContext(),
                            mContext.getResources().getString(R.string.offlineDSPMsg));
                } else {
                    ToastUtil.showShortToast(getContext(),
                            mContext.getResources().getString(R.string.off_line_mode));
                }
            }
        });
    }

    /*********************************************************************/
    /******************   底部弹出  TODO  ********************/
    /*********************************************************************/

    private void showBottomList() {
        BOOL_ShowBList = true;
        bottomView = new BottomView(getActivity(),
                R.style.BottomViewTheme_Defalut, R.layout.chs_musicbox_bv_list);
        bottomView.setAnimation(R.style.BottomToTopAnim);
        bottomView.showBottomView(true);
        LYMusicNullMsg_BV = (LinearLayout) bottomView.getView().findViewById(R.id.id_ly_msg);
        //顶部模式
        LYPlayT_BV = (LinearLayout) bottomView.getView().findViewById(R.id.id_ly_playtext);
        TVMode_BV = (TextView) bottomView.getView().findViewById(R.id.id_t_playtext);
        BtnModeB_BV = (Button) bottomView.getView().findViewById(R.id.id_playmode);
        //底部列表
        LV_Music_BV = (ListView) bottomView.getView().findViewById(R.id.id_listvew_of_music);
        //播放模式
        BtnModeB_BV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationUtil.AnimScaleI(mContext, v);
                byte playmode = 0;

                if (DataStruct.CurMusic.PlayMode == MDef.PLAY_MODE_SNGLE) {
                    playmode = MDef.PLAY_MODE_RANDOM;
                } else if (DataStruct.CurMusic.PlayMode == MDef.PLAY_MODE_RANDOM) {
                    playmode = MDef.PLAY_MODE_LOOP;
                } else if (DataStruct.CurMusic.PlayMode == MDef.PLAY_MODE_LOOP) {
                    playmode = MDef.PLAY_MODE_SNGLE;
                }
                MDOptUtil.setMusicPlayMode(playmode, false);
            }
        });

        LYPlayT_BV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AnimationUtil.AnimScaleI(mContext, v);
                MDOptUtil.setMusicNext(false);
            }
        });

        MBoxAdapter_BV = new MBoxItemAdapter(mContext, ListData_BV);
        LV_Music_BV.setAdapter(MBoxAdapter_BV);
        flashMusicList_BV(true);


        if (DataStruct.isConnecting) {
            if (DataStruct.comType >= Define.COMT_PLAY) {
                if (DataStruct.CurMusic.Total == 0) {
                    showMusicListButtonView(false);
                } else {
                    showMusicListButtonView(true);
                }
            }
        } else {
            showMusicListButtonView(true);
        }
    }

    private void showMusicListButtonView(boolean show) {
        if (BOOL_ShowBList) {
            if (show) {
                LYMusicNullMsg_BV.setVisibility(View.GONE);
                LV_Music_BV.setVisibility(View.VISIBLE);
            } else {
                LYMusicNullMsg_BV.setVisibility(View.VISIBLE);
                LV_Music_BV.setVisibility(View.GONE);
            }
        }
        if (show) {
            LYMusicNullMsg.setVisibility(View.GONE);
            LV_File.setVisibility(View.VISIBLE);
        } else {
            LYMusicNullMsg.setVisibility(View.VISIBLE);
            LV_File.setVisibility(View.GONE);
        }
    }

    private void flashMusicList_BV(boolean ft) {

        if ((DataStruct.CurMusic.UpdateListStart - 1) == DataStruct.CurMusic.Total) {
            DataStruct.CurMusic.BoolHaveUPdateList = true;
        }

        ListData_BV.clear();
        if (DataStruct.MusicList.size() > 0) {
            for (int i = 0; i < DataStruct.MusicList.size(); i++) {
                MMListInfo mM = DataStruct.MusicList.get(i);
                MMListInfo mdata = new MMListInfo();
                mdata.boolSel = false;
                mdata.ID = mM.ID;
                //System.out.println("BUG MboxList mdata.ID="+mdata.ID);
                mdata.FileName = mM.FileName;
                mdata.ID3_Title = mM.ID3_Title;
                mdata.ID3_Artist = mContext.getString(R.string.unknownArtist);//mM.ID3_Artist;
                mdata.ID3_Album = mM.ID3_Album;
                mdata.ID3_Genre = mM.ID3_Genre;
                ListData_BV.add(mdata);
            }
        } else {
            DataStruct.CurMusic.BoolHaveUPdateList = false;
            DataStruct.CurMusic.UpdateListStart = 1;
            DataStruct.CurMusic.UpdateListNum = 5;
        }

        //MBoxAdapter_BV.notifyDataSetChanged();
        MBoxAdapter_BV = new MBoxItemAdapter(mContext, ListData_BV);
        LV_Music_BV.setAdapter(MBoxAdapter_BV);
        listViewListen_BV();

        updateSongInListView_BV(DataStruct.CurMusic.CurID, ft);
    }

    private void updateSongInListView_BV(int index, boolean ft) {
        index--;
        if (ListData_BV.size() > index) {
            for (int i = 0; i < ListData_BV.size(); i++) {
                if (i == index) {
                    ListData_BV.get(i).boolSel = true;
                    ListData_BV.get(i).ID3_Title = DataStruct.CurMusic.ID3_Title;
                    ListData_BV.get(i).ID3_Artist = DataStruct.CurMusic.ID3_Artist;
                    ListData_BV.get(i).ID3_Album = DataStruct.CurMusic.ID3_Album;
                    ListData_BV.get(i).ID3_Genre = DataStruct.CurMusic.ID3_Genre;
                } else {
                    ListData_BV.get(i).boolSel = false;
                }
            }
            MBoxAdapter_BV.notifyDataSetChanged();
            if (BOOL_ClickList_BV) {
                BOOL_ClickList_BV = false;
                return;
            }
            if (ft) {
                LV_Music_BV.setSelectionFromTop(index, 4);
            }

        }
    }

    private void listViewListen_BV() {
        MBoxAdapter_BV.setOnMBoxAdpterOnItemClick(new MBoxItemAdapter.setMBoxAdpterOnItemClick() {
            @Override
            public void onAdpterClick(int which, int postion, View v) {
                final BaseAdapter adapter = (BaseAdapter) LV_Music_BV.getAdapter();
                //MusicListData item = (MusicListData) adapter.getItem(postion);
                MMListInfo item = ListData_BV.get(postion);
//                System.out.println("BUG MboxList postion="+postion+",item.ID==="+item.ID);
                if (which == R.id.id_ly) {
                    if (DataStruct.CurMusic.BoolPhoneBlueMusicPush) {
                        //ToastUtil.showShortToast(getContext(),mContext.getResources().getString(R.string.bluetoothpush));
                        ToastUtil.showShortToast(getContext(), mContext.getResources().getString(R.string.ChangeUhostMode));
                        DataStruct.RcvDeviceData.SYS.input_source=5;
                        changeUHostplay(item.ID);

                    } else {
                        DataStruct.CurMusic.resID3Info();
                        MDOptUtil.setMusicNumPlay(item.ID, false);
                        System.out.println("BUG MboxList LY");
                        for (int i = 0; i < ListData_BV.size(); i++) {
                            if (i == postion) {
                                ListData_BV.get(i).boolSel = true;
                            } else {
                                ListData_BV.get(i).boolSel = false;
                            }
                        }
                        MBoxAdapter_BV.notifyDataSetChanged();
                        BOOL_ClickList_BV = true;
                    }

                } else if (which == R.id.id_msg) {
                    System.out.println("BUG MboxList id_msg");
                    ToastUtil.showShortToast(mContext,
                            getString(R.string.music_info_title) + item.ID3_Title + "\n"
                                    + getString(R.string.music_info_artist) + item.ID3_Artist + "\n"
                                    + getString(R.string.music_info_ablum) + item.ID3_Album + "\n"
                                    + getString(R.string.music_info_styles) + item.ID3_Genre + "\n"
                    );

                }
            }
        });
    }

    /*********************************************************************/
    /************    Music  TODO        ****************************/
    /*********************************************************************/
    public void setVcover(int index, boolean show) {
//        switch (index){
//            case 1:
//                if(show){
//                    VCover1.setVisibility(View.VISIBLE);
//                }else {
//                    VCover1.setVisibility(View.GONE);
//                }
//                break;
//            case 2:
//                if(show){
//                    VCover2.setVisibility(View.VISIBLE);
//                }else {
//                    VCover2.setVisibility(View.GONE);
//                }
//                break;
//            case 3:
//                if(show){
//                    VCover3.setVisibility(View.VISIBLE);
//                }else {
//                    VCover3.setVisibility(View.GONE);
//                }
//                break;
//       }
    }

    private void flashMusicStatus() {
        if (DataStruct.isConnecting) {
            Ct = DataStruct.comType;
//            switch (Ct) {
//
//                case Define.COMT_OFF:
//                    VCover1.setVisibility(View.VISIBLE);
//                    VCover2.setVisibility(View.VISIBLE);
//                    VCover3.setVisibility(View.VISIBLE);
//                    VCover5.setVisibility(View.VISIBLE);
//                    Vcoversource.setVisibility(View.VISIBLE);
//
//                    VCover1.getBackground().setAlpha(170);
//                    VCover2.getBackground().setAlpha(170);
//                    VCover3.getBackground().setAlpha(170);
//                    VCover5.getBackground().setAlpha(170);
//                    Vcoversource.getBackground().setAlpha(170);
//                    break;
//                case Define.COMT_DSP:
//                    VCover1.setVisibility(View.GONE);
//                    VCover2.setVisibility(View.GONE);
//                    VCover5.setVisibility(View.GONE);
//                    Vcoversource.setVisibility(View.GONE);
//                    VCover3.setVisibility(View.VISIBLE);
//                    break;
//                case Define.COMT_DSPPLAY:
//                    VCover1.setVisibility(View.GONE);
//                    VCover2.setVisibility(View.GONE);
//                    VCover3.setVisibility(View.GONE);
//                    VCover5.setVisibility(View.GONE);
//                    Vcoversource.setVisibility(View.GONE);
//                    VCover3.getBackground().setAlpha(255);
//                    break;
//                case Define.COMT_PLAY:
//                    VCover1.setVisibility(View.VISIBLE);
//                    VCover2.setVisibility(View.VISIBLE);
//                    VCover3.setVisibility(View.GONE);
//                    VCover5.setVisibility(View.VISIBLE);
//                    Vcoversource.setVisibility(View.GONE);
//
//                    VCover1.getBackground().setAlpha(170);
//                    VCover2.getBackground().setAlpha(170);
//                    VCover5.getBackground().setAlpha(170);
//
//                    break;
//            }
        } else {
//            VCover1.setVisibility(View.VISIBLE);
//            VCover2.setVisibility(View.VISIBLE);
//            VCover3.setVisibility(View.VISIBLE);
//            VCover5.setVisibility(View.VISIBLE);
//            Vcoversource.setVisibility(View.VISIBLE);
//            VCover1.getBackground().setAlpha(170);
//            VCover2.getBackground().setAlpha(170);
//            VCover3.getBackground().setAlpha(170);
//            VCover5.getBackground().setAlpha(170);
//            Vcoversource.getBackground().setAlpha(170);
       }
        //播放状态
        mMusicItem.Update();
        TVTotalFile.setText("(" + String.valueOf(DataStruct.CurMusic.Total) + ")");

        if (!DataStruct.CurMusic.BoolHaveUHost) {
            if (FileListData.size() > 0) {
                FileListData.clear();
                MFileAdapter = new MFileItemAdapter(mContext, FileListData);
                LV_File.setAdapter(MFileAdapter);
                FileListViewListen();
            }

            showMusicListButtonView(false);
            TVTotalFile.setText("(" + String.valueOf(0) + ")");
        } else {
            if (DataStruct.CurMusic.Total > 0) {
                VCover3.setVisibility(View.GONE);
                showMusicListButtonView(true);
            } else {
                showMusicListButtonView(false);
            }

        }

        if (DataStruct.CurMusic.BoolPhoneBlueMusicPush) {
            VCover3.setVisibility(View.GONE);
        }


        if (curPlayMode != DataStruct.CurMusic.PlayMode) {
            curPlayMode = DataStruct.CurMusic.PlayMode;
            if (DataStruct.CurMusic.PlayMode == MDef.PLAY_MODE_SNGLE) {
                ToastUtil.showShortToast(mContext, mContext.getString(R.string.notice_playmode_repeatone));
            } else if (DataStruct.CurMusic.PlayMode == MDef.PLAY_MODE_RANDOM) {
                ToastUtil.showShortToast(mContext, mContext.getString(R.string.notice_playmode_toggle));
            } else if (DataStruct.CurMusic.PlayMode == MDef.PLAY_MODE_LOOP) {
                ToastUtil.showShortToast(mContext, mContext.getString(R.string.notice_playmode_repeat));
            }
        }
        if (BOOL_ShowBList) {
            if (DataStruct.CurMusic.PlayMode == MDef.PLAY_MODE_SNGLE) {
                BtnModeB_BV.setBackground(mContext.getResources().getDrawable(R.drawable.chs_music_bar_player_mode_co_press));
                TVMode_BV.setText(mContext.getString(R.string.notice_playmode_repeatone));
            } else if (DataStruct.CurMusic.PlayMode == MDef.PLAY_MODE_RANDOM) {
                BtnModeB_BV.setBackground(mContext.getResources().getDrawable(R.drawable.chs_music_player_mode_ran_press));
                TVMode_BV.setText(mContext.getString(R.string.notice_playmode_toggle));
            } else if (DataStruct.CurMusic.PlayMode == MDef.PLAY_MODE_LOOP) {
                BtnModeB_BV.setBackground(mContext.getResources().getDrawable(R.drawable.chs_music_bar_player_mode_ca_press));
                TVMode_BV.setText(mContext.getString(R.string.notice_playmode_repeat));
            }

            if (DataStruct.CurMusic.UPDATESONGSINList) {
                DataStruct.CurMusic.UPDATESONGSINList = false;
                //updateSongInListView_BV(DataStruct.CurMusic.CurID,true);
            }
        }

        if (!DataStruct.isConnecting) {
            TVTotalFile.setText("(" + String.valueOf(0) + ")");
        }


//        if (!DataStruct.CurMusic.BoolHaveUHost) {
//            if (inputSourceIndex == 2) {
//                setInputSource(3);
//            }
//        }
    }

    private void flashMusicID3() {

    }

    //刷新文件列表
    private void flashFileList() {
        TVTotalFile.setText("(" + String.valueOf(DataStruct.CurMusic.Total) + ")");
        FileListData.clear();
        if (DataStruct.FileList.size() > 0) {
            if ((DataStruct.FileList.get(0).FileName.contains("ROOT"))
                    && (DataStruct.FileList.get(0).CurDirID == 1)) {
                for (int i = 1; i < DataStruct.FileList.size(); i++) {
                    if (DataStruct.FileList.get(i).CurMusicList.size() > 0) {
                        DataStruct.FileList.get(i).ListType = MDef.ListType_File;
                        FileListData.add(DataStruct.FileList.get(i));
                    }
                }
                BOOL_HaveROOT = true;
            } else {
                for (int i = 0; i < DataStruct.FileList.size(); i++) {
                    if (DataStruct.FileList.get(i).CurMusicList.size() > 0) {
                        DataStruct.FileList.get(i).ListType = MDef.ListType_File;
                        FileListData.add(DataStruct.FileList.get(i));
                    }
                }
                BOOL_HaveROOT = false;
            }
        }


        if (DataStruct.FileList.size() > 0) {
            if (BOOL_HaveROOT) {
                if (DataStruct.FileList.get(0).CurMusicList.size() > 0) {
                    for (int i = 0; i < DataStruct.FileList.get(0).CurMusicList.size(); i++) {
                        MFListInfo item = new MFListInfo();
                        item.ListType = MDef.ListType_Music;
                        item.MName = DataStruct.FileList.get(0).CurMusicList.get(i).FileName;
                        item.MArtist = "";
                        item.PlayID = DataStruct.FileList.get(0).CurMusicList.get(i).ID;

                        FileListData.add(item);
                    }
                }

            }
        }

        MFileAdapter = new MFileItemAdapter(mContext, FileListData);
        LV_File.setAdapter(MFileAdapter);
        FileListViewListen();

        updateSongInFileList();
    }

    private void flashFileListMax() {
        FileListData.clear();
        if (DataStruct.FileList.size() > 0) {
            if ((DataStruct.FileList.get(0).FileName.contains("ROOT"))
                    && (DataStruct.FileList.get(0).CurDirID == 1)) {
                for (int i = 1; i < DataStruct.FileList.size(); i++) {
                    if (DataStruct.FileList.get(i).CurMusicList.size() > 0) {
                        DataStruct.FileList.get(i).ListType = MDef.ListType_File;
                        FileListData.add(DataStruct.FileList.get(i));
                    }
                }
                BOOL_HaveROOT = true;
            } else {
                for (int i = 0; i < DataStruct.FileList.size(); i++) {
                    if (DataStruct.FileList.get(i).CurMusicList.size() > 0) {
                        DataStruct.FileList.get(i).ListType = MDef.ListType_File;
                        FileListData.add(DataStruct.FileList.get(i));
                    }
                }
                BOOL_HaveROOT = false;
            }
        }

        if (DataStruct.FileList.size() > 0) {
            if (BOOL_HaveROOT) {
                if (DataStruct.FileList.get(0).CurMusicList.size() > 0) {
                    for (int i = 0; i < DataStruct.FileList.get(0).CurMusicList.size(); i++) {
                        MFListInfo item = new MFListInfo();
                        item.ListType = MDef.ListType_Music;
                        item.MName = DataStruct.FileList.get(0).CurMusicList.get(i).FileName;
                        item.MArtist = "";
                        item.PlayID = DataStruct.FileList.get(0).CurMusicList.get(i).ID;

                        FileListData.add(item);
                    }
                }

            }
        }

        MFileAdapter = new MFileItemAdapter(mContext, FileListData);
        LV_File.setAdapter(MFileAdapter);
        FileListViewListen();

        updateSongInFileList();
    }

    private void FileListViewListen() {
        MFileAdapter.setOnMFileAdpterOnItemClick(new MFileItemAdapter.setMFileAdpterOnItemClick() {
            @Override
            public void onAdpterClick(int which, int postion, View v) {
                if (FileListData == null) {
                    return;
                }
                if (FileListData.size() <= postion) {
                    return;
                }

                MFListInfo item = FileListData.get(postion);
                if (which == R.id.id_file_ly) {
                    if (item.ListType == MDef.ListType_Music) {
                        if (DataStruct.CurMusic.BoolPhoneBlueMusicPush) {
                            ToastUtil.showShortToast(getContext(), mContext.getResources().getString(R.string.ChangeUhostMode));
                            DataStruct.RcvDeviceData.SYS.input_source=5;
                            changeUHostplay(item.PlayID);
                        } else {
                            MDOptUtil.setMusicNumPlay(item.PlayID, false);
                            //刷新列表
                            if (FileListData.size() <= 0) {
                                return;
                            }
                            for (int j = 0; j < FileListData.size(); j++) {
                                if (postion == j) {
                                    FileListData.get(j).sel = true;
                                } else {
                                    FileListData.get(j).sel = false;
                                }
                            }
                            MFileAdapter.notifyDataSetChanged();
                        }

                    } else if (item.ListType == MDef.ListType_File) {
                        if (BOOL_HaveROOT) {
                            ++postion;
                        }
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putInt("FileIndex", postion);
                        intent.putExtras(bundle);
                        intent.setClass(mContext, MusicActivity.class);
                        startActivityForResult(intent, Define.ActivityResult_MusicLrcPage_Back);
                    }
                }
            }
        });
    }

    private void changeUHostplay(int index) {
        AudioUtil.stopOtherPlayer(mContext);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                MDOptUtil.setUHostPlayModeAPP(false);
            }
        }, 1000);
        changeUHostplaySongs(index);
    }

    private void changeUHostplaySongs(int index) {
        final int adex = index;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                MDOptUtil.setMusicNumPlay(adex, false);
            }
        }, 2000);
    }

    private void updateSongInFileList() {
        if (DataStruct.FileList.size() <= 0) {
            return;
        }
        for (int i = 0; i < FileListData.size(); i++) {
            FileListData.get(i).sel = false;
            if (FileListData.get(i).ListType == MDef.ListType_File) {
                if ((DataStruct.CurMusic.CurID >= FileListData.get(i).SongsStartID)
                        && (DataStruct.CurMusic.CurID <= FileListData.get(i).SongsEndID)) {

                    for (int j = 0; j < FileListData.size(); j++) {
                        if (i == j) {
                            FileListData.get(j).sel = true;
                        } else {
                            FileListData.get(j).sel = false;
                        }
                    }
                    break;
                }
            } else if (FileListData.get(i).ListType == MDef.ListType_Music) {
                FileListData.get(i).sel = false;
                if (FileListData.get(i).PlayID == DataStruct.CurMusic.CurID) {
                    FileListData.get(i).MArtist = DataStruct.CurMusic.ID3_Artist;
                    FileListData.get(i).sel = true;
                }
            }

        }
        MFileAdapter.notifyDataSetChanged();
    }


    private void setUIDefault() {

        //清除列表
        DataStruct.MusicList.clear();
        DataStruct.MusicList.removeAll(DataStruct.MusicList);

        FileListData.clear();
        FileListData.removeAll(FileListData);
        DataStruct.FileList.clear();
        DataStruct.FileList.removeAll(DataStruct.FileList);
        flashFileList();

    }

    //用接收广播刷新UI TODO
    public class CHS_Broad_FLASHUI_BroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getExtras().get("msg").toString();
            if (msg.equals(Define.BoardCast_FlashUI_MusicPage)) {
                final String type = intent.getExtras().get(MDef.MUSICMSG_MSGTYPE).toString();
                if (type.equals(MDef.MUSICPAGE_Status)) {
                   flashMusicStatus();
                } else if (type.equals(MDef.MUSICPAGE_ID3)) {
                    if (VCover3.getVisibility() == View.VISIBLE) {
                        VCover3.setVisibility(View.GONE);
                    }
                    //flashMusicID3();
                    updateSongInFileList();
                    if (BOOL_ShowBList) {
                        updateSongInListView_BV(DataStruct.CurMusic.CurID, false);
                    }
                } else if (type.equals(MDef.MUSICPAGE_List)) {

                    if (BOOL_ShowBList) {
                        flashMusicList_BV(true);
                    }
                    if (LV_File.getVisibility() == View.GONE) {
                        LV_File.setVisibility(View.VISIBLE);
                    }
                    VCover3.setVisibility(View.GONE);
                    ToastUtil.showShortToast(mContext, mContext.getString(R.string.UpdateFileListMsg));

                } else if (type.equals(MDef.MUSICPAGE_UpdateSongInList)) {
                    if (BOOL_ShowBList) {
                        //在ID3刷新
//                        updateSongInListView_BV(DataStruct.CurMusic.CurID,true);
                    }
                    if (LV_File.getVisibility() == View.GONE) {
                        LV_File.setVisibility(View.VISIBLE);
                    }
                } else if (type.equals(MDef.MUSICPAGE_FileList)) {
                    flashFileList();
                    if (LV_File.getVisibility() == View.GONE) {
                        LV_File.setVisibility(View.VISIBLE);
                    }
                } else if (type.equals(MDef.MUSICPAGE_FListShowLoading)) {
                    if (LV_File.getVisibility() == View.GONE) {
                        LV_File.setVisibility(View.VISIBLE);
                    }
                    if (DataStruct.CurMusic.BoolHaveUPdateFileList) {

                    } else {

                    }
                } else if (type.equals(MDef.MUSICPAGE_MListShowLoading)) {
                    if (DataStruct.CurMusic.BoolHaveUPdateList) {

                    } else {

                    }
                    if (LV_File.getVisibility() == View.GONE) {
                        LV_File.setVisibility(View.VISIBLE);
                    }
                    //更新文件显示
                    flashFileListMax();
                    if (BOOL_ShowBList) {
                        flashMusicList_BV(true);
                    }
                } else if (type.equals(MDef.MUSICPAGE_ShowConnectAgainMsg)) {
                    ToastUtil.showShortToast(mContext, mContext.getResources().getString(R.string.DSPPlayDisconnectedMsg));
                    DataStruct.CurMusic.resetData();
                    mMusicItem.Update();
                    FlashPageUI();
                }
            } else if (msg.equals(Define.BoardCast_FlashUI_ConnectState)) {
                boolean res = intent.getBooleanExtra("state", false);
                if (!res) {
                    DataStruct.CurMusic.resetData();
                    mMusicItem.Update();
                    LV_File.setVisibility(View.GONE);
                    DataStruct.FileList.clear();
                    DataStruct.MusicList.clear();

                    FlashPageUI();
                    for (int i = 1; i <= 6; i++) {
                        MACUSEREFF[i].setText(mContext.getResources().getString(R.string.Preset) + String.valueOf(i));
                    }
//                    VCover1.setVisibility(View.VISIBLE);
//                    VCover2.setVisibility(View.VISIBLE);
//                    VCover3.setVisibility(View.VISIBLE);
//                    VCover5.setVisibility(View.VISIBLE);
//
//                    VCover1.getBackground().setAlpha(170);
//                    VCover2.getBackground().setAlpha(170);
//                    VCover3.getBackground().setAlpha(170);
//                    VCover5.getBackground().setAlpha(170);

                    showMusicListButtonView(false);
                }
            } else if (msg.equals(Define.BoardCast_FlashUI_AllPage)) {
                FlashPageUI();
                if (DataOptUtil.isConnected()) {
                    if (DataStruct.comDSP == Define.COMT_DSP) {
                        VCover1.setVisibility(View.GONE);
                        VCover2.setVisibility(View.GONE);
                        VCover5.setVisibility(View.GONE);
                    }
                }
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("BUG onActivityResult requestCode=" + requestCode);
        switch (requestCode) {
            case Define.ActivityResult_MusicLrcPage_Back:
                updateSongInFileList();
                break;
        }
    }


}
