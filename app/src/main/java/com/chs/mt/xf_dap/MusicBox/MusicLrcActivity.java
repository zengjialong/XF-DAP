package com.chs.mt.xf_dap.MusicBox;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
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
import com.chs.mt.xf_dap.fragment.dialogFragment.LoadingMusicListDialogFragment;
import com.chs.mt.xf_dap.operation.AnimationUtil;
import com.chs.mt.xf_dap.tools.MHS_SeekBar;
import com.chs.mt.xf_dap.tools.MHS_SeekBarProgress;
import com.chs.mt.xf_dap.tools.ZoomOutPageTransformer;
import com.chs.mt.xf_dap.util.ToastUtil;
import com.chs.mt.xf_dap.util.img.FastBulr;
import com.facebook.drawee.view.SimpleDraweeView;
import com.tandong.bottomview.view.BottomView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MusicLrcActivity extends FragmentActivity {
    private Toast mToast;
    private Context mContext;
    /*******************************************************************/
    private Button BtnMode,BtnPre,BtnNext,BtnPlay,BtnMenu;
    private RelativeLayout LYPre,LYNext,LYPlay;
    private MHS_SeekBarProgress SBPlay;
    private TextView TV_TimeAll,TV_TimePlay, TVName;
    private TextView TVArtist;

    private LinearLayout LYBack;
    private SimpleDraweeView IVArtBg;

    private LinearLayout LYMainVal;
    private MHS_SeekBar SBMainVal;

    private int curColorMode = 0;
    private int curPlayMode = 0;

    private ViewPager viewpager;
    private MusicLrcAdapter adapter;
    private List<Fragment> list;
    private Button index0,index1;

    private MusicLrcFragment mMusicLrcFragment;
    private MusicLrcID3Fragment mMusicLrcID3Fragment;
    //底部
    private BottomView bottomView;
    private ListView LV_Music_BV;
    private MBoxItemAdapter MBoxAdapter_BV;
    private List<MMListInfo> ListData_BV = new ArrayList<MMListInfo>();
    private Button BtnModeB_BV;
    private TextView TVMode_BV;
    private LinearLayout LYPlayT_BV;
    private boolean BOOL_ShowBList_BV=false;
    private LinearLayout LYMusicNullMsg_BV;
    private boolean BOOL_ClickList_BV=false;

    private LoadingMusicListDialogFragment mLoadingMusicListDialogFragment=null;
    private CHS_Broad_FLASHUI_BroadcastReceiver CHS_Broad_Receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.chs_activity_musiclrc);

        mContext = this;
        initView();
        initClick();

        //动态注册CHS_Broad_BroadcastReceiver
        CHS_Broad_Receiver = new CHS_Broad_FLASHUI_BroadcastReceiver();
        IntentFilter CHS_Broad_filter=new IntentFilter();
        CHS_Broad_filter.addAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
        //注册receiver
        registerReceiver(CHS_Broad_Receiver, CHS_Broad_filter);

        curPlayMode = DataStruct.CurMusic.PlayMode;
        FlashPageUI();
        timerColorMode.schedule(taskColorMode, 2000, 20000);

//        if((DataStruct.U0SynDataSucessFlg)
//                &&(DataStruct.isConnecting)
//                ){
//            if(DataStruct.CurMusic.BoolMLDialogCanShow){
//                showLoadingDialog(1);
//            }else if(DataStruct.CurMusic.BoolFLDialogCanShow){
//                showLoadingDialog(2);
//            }
//        }
    }

    //刷新页面UI
    public void FlashPageUI(){
        flashMusicStatus();
        flashMusicLrcImg();
        flashMusicLrc();
        flashMusicID3();
    }



    Timer timerColorMode = new Timer();
    TimerTask taskColorMode = new TimerTask() {

        @Override
        public void run() {

            if(DataStruct.CurMusic.play_status == MDef.PLAY_STATUS_PLAY){
                Message msgil = Message.obtain();
                msgil.what = 3;
                handlerMainVal.sendMessage(msgil);
            }
        }
    };
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


    private void initView(){
        LYMainVal = (LinearLayout) findViewById(R.id.id_ly_sb_main_val);
        SBMainVal = (MHS_SeekBar) findViewById(R.id.id_sb_main_val);
        SBMainVal.setProgressMax(DataStruct.CurMacMode.Master.MAX);
        SBMainVal.setOnSeekBarChangeListener(new MHS_SeekBar.OnMSBSeekBarChangeListener() {
            @Override
            public void onProgressChanged(MHS_SeekBar mhs_SeekBar, int progress, boolean fromUser) {
                if(DataStruct.CurMacMode.Master.DATA_TRANSFER == Define.COM_TYPE_INPUT){
                    DataStruct.RcvDeviceData.IN_CH[0].Valume = progress;
                }else {
                    DataStruct.RcvDeviceData.SYS.main_vol = progress;
                }
                handlerMainVal.removeMessages(0);
                handlerMainVal.sendEmptyMessageDelayed(0, 3000);
            }
        });

        LYBack = (LinearLayout) findViewById(R.id.id_llyout_back);

        LYPre  = (RelativeLayout) findViewById(R.id.id_ly_music_previous);
        LYNext = (RelativeLayout) findViewById(R.id.id_ly_music_next);
        LYPlay = (RelativeLayout) findViewById(R.id.id_ly_music_play);

        BtnMode = (Button) findViewById(R.id.id_b_music_play_mode);
        BtnPre  = (Button) findViewById(R.id.id_b_music_previous);
        BtnNext = (Button) findViewById(R.id.id_b_music_next);
        BtnPlay = (Button) findViewById(R.id.id_b_music_play);
        BtnMenu = (Button) findViewById(R.id.id_b_music_play_cur_msg);

        SBPlay      = (MHS_SeekBarProgress) findViewById(R.id.id_sb_play);
        TV_TimeAll  = (TextView) findViewById(R.id.id_tx_time_all);
        TV_TimePlay = (TextView) findViewById(R.id.id_tx_time);
        TVName      = (TextView) findViewById(R.id.di_tv_viewpage_name);
        IVArtBg     = (SimpleDraweeView) findViewById(R.id.id_iv);

        TVArtist    = (TextView) findViewById(R.id.id_tv_artist);

        viewpager = (ViewPager) findViewById(R.id.viewpager);
        mMusicLrcFragment = new MusicLrcFragment();
        mMusicLrcID3Fragment = new MusicLrcID3Fragment();
        list = new ArrayList<Fragment>();
        list.add(mMusicLrcFragment);
        list.add(mMusicLrcID3Fragment);

        adapter = new MusicLrcAdapter(getSupportFragmentManager(), list);
        viewpager.setAdapter(adapter);
        viewpager.setPageTransformer(true, new ZoomOutPageTransformer());
        viewpager.setCurrentItem(0);

        index0 = (Button) findViewById(R.id.id_b_index0);
        index1 = (Button) findViewById(R.id.id_b_index1);



    }

    private void initClick(){

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                int val = (int) (positionOffset*5);
//                if(position == 1){
//                    val = 5-val;
//                }
//                setBgAp(val);
            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0){
                    index0.setBackgroundResource(R.drawable.chs_musiclrc_pindex_press);
                    index1.setBackgroundResource(R.drawable.chs_musiclrc_pindex_normal);
//                    setBgAp(0);
                }else {
                    index0.setBackgroundResource(R.drawable.chs_musiclrc_pindex_normal);
                    index1.setBackgroundResource(R.drawable.chs_musiclrc_pindex_press);
//                    setBgAp(5);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        LYBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AnimationUtil.toVisibleAnim(mContext,v);
                finish();
            }
        });

        //播放暂停
        LYPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationUtil.AnimScaleI(mContext,v);
                if(DataStruct.CurMusic.play_status == MDef.PLAY_STATUS_PAUSE){
                    //播放
                    MDOptUtil.setMusicPlay(false);
                    //BtnPlay.setBackgroundResource(R.drawable.chs_music_stop);
                    ToastUtil.showShortToast(mContext, mContext.getResources().getString(R.string.MusicOptPlay));
                }else {
                    MDOptUtil.setMusicPause(false);
                    ToastUtil.showShortToast(mContext, mContext.getResources().getString(R.string.MusicOptPause));
                    //BtnPlay.setBackgroundResource(R.drawable.chs_music_play);

                }

            }
        });
        //下一首
        LYNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationUtil.AnimScaleI(mContext,v);
                MDOptUtil.setMusicNext(false);
                ToastUtil.showShortToast(mContext, mContext.getResources().getString(R.string.MusicOptNext));

                ++curColorMode;
                Message msgil = Message.obtain();
                msgil.what = 3;
                handlerMainVal.sendMessage(msgil);
            }
        });
        //前一首
        LYPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationUtil.AnimScaleI(mContext,v);
                MDOptUtil.setMusicPrev(false);
                ToastUtil.showShortToast(mContext, mContext.getResources().getString(R.string.MusicOptPre));

                curColorMode -=2;
                Message msgil = Message.obtain();
                msgil.what = 3;
                handlerMainVal.sendMessage(msgil);
            }
        });
        //播放模式
        BtnMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationUtil.AnimScaleI(mContext,v);
                byte playmode = 0;

                if(DataStruct.CurMusic.PlayMode == MDef.PLAY_MODE_SNGLE){
                    playmode = MDef.PLAY_MODE_RANDOM;
                }else if(DataStruct.CurMusic.PlayMode == MDef.PLAY_MODE_RANDOM){
                    playmode = MDef.PLAY_MODE_LOOP;
                }else if(DataStruct.CurMusic.PlayMode == MDef.PLAY_MODE_LOOP){
                    playmode = MDef.PLAY_MODE_SNGLE;
                }

                MDOptUtil.setMusicPlayMode(playmode,false);
            }
        });
        //播放Menu
        BtnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationUtil.AnimScaleI(mContext,v);
                showBottomList();
//                if(DataStruct.CurMusic.FileName.length()>0){
//                    ToastMsg(getString(R.string.music_info_title)+DataStruct.CurMusic.ID3_Title+"\n"
//                            +getString(R.string.music_info_artist)+DataStruct.CurMusic.ID3_Artist+"\n"
//                            +getString(R.string.music_info_ablum)+DataStruct.CurMusic.ID3_Album+"\n"
//                            +getString(R.string.music_info_styles)+DataStruct.CurMusic.ID3_Genre+"\n"
//                            +getString(R.string.music_info_mime)+MDOptUtil.getMusicType(DataStruct.CurMusic.Form)+"\n"
//                    );
//                }else {
//                    ToastMsg(getString(R.string.notice_device_cannot_connect));
//                }

            }
        });
    }

    /*********************************************************************/
    /*****************************     Music  TODO        ****************************/
    /*********************************************************************/

    private void showBottomList(){
        BOOL_ShowBList_BV = true;
        bottomView = new BottomView(mContext,
                R.style.BottomViewTheme_Defalut, R.layout.chs_musicbox_bv_list);
        bottomView.setAnimation(R.style.BottomToTopAnim);
        bottomView.showBottomView(true);
        //顶部模式
        LYMusicNullMsg_BV = (LinearLayout) bottomView.getView().findViewById(R.id.id_ly_msg);
        LYPlayT_BV = (LinearLayout) bottomView.getView().findViewById(R.id.id_ly_playtext);
        TVMode_BV = (TextView) bottomView.getView().findViewById(R.id.id_t_playtext);
        BtnModeB_BV = (Button) bottomView.getView().findViewById(R.id.id_playmode);
        //底部列表
        LV_Music_BV = (ListView) bottomView.getView().findViewById(R.id.id_listvew_of_music);
        //播放模式
        BtnModeB_BV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationUtil.AnimScaleI(mContext,v);
                byte playmode = 0;

                if(DataStruct.CurMusic.PlayMode == MDef.PLAY_MODE_SNGLE){
                    playmode = MDef.PLAY_MODE_RANDOM;
                }else if(DataStruct.CurMusic.PlayMode == MDef.PLAY_MODE_RANDOM){
                    playmode = MDef.PLAY_MODE_LOOP;
                }else if(DataStruct.CurMusic.PlayMode == MDef.PLAY_MODE_LOOP){
                    playmode = MDef.PLAY_MODE_SNGLE;
                }
                MDOptUtil.setMusicPlayMode(playmode,false);
            }
        });

        LYPlayT_BV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AnimationUtil.AnimScaleI(mContext,v);
                MDOptUtil.setMusicNext(false);
            }
        });

        MBoxAdapter_BV = new MBoxItemAdapter(mContext, ListData_BV);
        LV_Music_BV.setAdapter(MBoxAdapter_BV);
        flashMusicList(true);

        if(DataStruct.isConnecting){
            if(DataStruct.comType >= Define.COMT_PLAY){
                if(DataStruct.CurMusic.Total == 0){
                    LYMusicNullMsg_BV.setVisibility(View.VISIBLE);
                    LV_Music_BV.setVisibility(View.GONE);
                }else {
                    LYMusicNullMsg_BV.setVisibility(View.GONE);
                    LV_Music_BV.setVisibility(View.VISIBLE);
                }
            }
        }else {
            LYMusicNullMsg_BV.setVisibility(View.VISIBLE);
            LV_Music_BV.setVisibility(View.GONE);
        }
    }

    private void flashMusicList(boolean flashlist){
        if((DataStruct.CurMusic.UpdateListStart-1)== DataStruct.CurMusic.Total){
            DataStruct.CurMusic.BoolHaveUPdateList = true;
        }

        ListData_BV.clear();
        if(DataStruct.MusicList.size() > 0){
            for(int i=0;i<DataStruct.MusicList.size();i++){
                MMListInfo mM = DataStruct.MusicList.get(i);
                MMListInfo mdata = new MMListInfo();
                mdata.boolSel = false;
                mdata.ID = mM.ID;
                //System.out.println("BUG MboxList mdata.ID="+mdata.ID);
                mdata.FileName   = mM.FileName;
                mdata.ID3_Title  = mM.ID3_Title;
                mdata.ID3_Artist = getString(R.string.unknownArtist);//mM.ID3_Artist;
                mdata.ID3_Album  = mM.ID3_Album;
                mdata.ID3_Genre  = mM.ID3_Genre;
                ListData_BV.add(mdata);
            }
        }else {
            DataStruct.CurMusic.BoolHaveUPdateList = false;
            DataStruct.CurMusic.UpdateListStart = 1;
            DataStruct.CurMusic.UpdateListNum = 5;
        }
        if(BOOL_ClickList_BV){
            BOOL_ClickList_BV = false;
            return;
        }
        //MBoxAdapter_BV.notifyDataSetChanged();
        MBoxAdapter_BV = new MBoxItemAdapter(mContext, ListData_BV);
        LV_Music_BV.setAdapter(MBoxAdapter_BV);
        listViewListen();

        updateSongInListView(DataStruct.CurMusic.CurID , flashlist);
    }

    private void updateSongInListView(int index,boolean flashlist){
        index--;
        if(ListData_BV.size() > index){
            for(int i=0;i<ListData_BV.size();i++){
                if(i == index ){
                    ListData_BV.get(i).boolSel=true;
                    ListData_BV.get(i).ID3_Title = DataStruct.CurMusic.ID3_Title;
                    ListData_BV.get(i).ID3_Artist = DataStruct.CurMusic.ID3_Artist;
                    ListData_BV.get(i).ID3_Album = DataStruct.CurMusic.ID3_Album;
                    ListData_BV.get(i).ID3_Genre = DataStruct.CurMusic.ID3_Genre;
                }else {
                    ListData_BV.get(i).boolSel=false;
                }
            }
            if(BOOL_ClickList_BV){
                BOOL_ClickList_BV = false;
                return;
            }
            MBoxAdapter_BV.notifyDataSetChanged();
            if(flashlist){
                LV_Music_BV.setSelectionFromTop(index, 4);
            }
        }
    }
    private void listViewListen(){
        MBoxAdapter_BV.setOnMBoxAdpterOnItemClick(new MBoxItemAdapter.setMBoxAdpterOnItemClick() {
            @Override
            public void onAdpterClick(int which, int postion, View v) {
                final BaseAdapter adapter = (BaseAdapter) LV_Music_BV.getAdapter();
                //MusicListData_BV item = (MusicListData_BV) adapter.getItem(postion);
                MMListInfo item = ListData_BV.get(postion);
//                System.out.println("BUG MboxList postion="+postion+",item.ID==="+item.ID);
                if(which == R.id.id_ly) {
                    DataStruct.CurMusic.resID3Info();
                    MDOptUtil.setMusicNumPlay(item.ID,false);
                    System.out.println("BUG MboxList LY");
                    for(int i=0;i<ListData_BV.size();i++){
                        if(i == postion ){
                            ListData_BV.get(i).boolSel=true;
                        }else {
                            ListData_BV.get(i).boolSel=false;
                        }
                    }
                    MBoxAdapter_BV.notifyDataSetChanged();
                    BOOL_ClickList_BV=true;
                }else if(which == R.id.id_msg) {
                    System.out.println("BUG MboxList id_msg");
                    ToastUtil.showShortToast(mContext,
                            getString(R.string.music_info_title)+item.ID3_Title+"\n"
                                    +getString(R.string.music_info_artist)+item.ID3_Artist+"\n"
                                    +getString(R.string.music_info_ablum)+item.ID3_Album+"\n"
                                    +getString(R.string.music_info_styles)+item.ID3_Genre+"\n"
                    );

                }
            }
        });
    }

    private void flashPlayBarColor(){
        if(++curColorMode > 10){
            curColorMode=1;
        }
        if(curColorMode < 0){
            curColorMode = 10;
        }
        switch (curColorMode){
            case 1:
                LYPlay.setBackgroundResource(R.drawable.chs_music_lrc_play_color_1);
                LYPre.setBackgroundResource(R.drawable.chs_music_lrc_pnext_color_1);
                LYNext.setBackgroundResource(R.drawable.chs_music_lrc_pnext_color_1);
                break;
            case 2:
                LYPlay.setBackgroundResource(R.drawable.chs_music_lrc_play_color_2);
                LYPre.setBackgroundResource(R.drawable.chs_music_lrc_pnext_color_2);
                LYNext.setBackgroundResource(R.drawable.chs_music_lrc_pnext_color_2);
                break;
            case 3:
                LYPlay.setBackgroundResource(R.drawable.chs_music_lrc_play_color_3);
                LYPre.setBackgroundResource(R.drawable.chs_music_lrc_pnext_color_3);
                LYNext.setBackgroundResource(R.drawable.chs_music_lrc_pnext_color_3);
                break;
            case 4:
                LYPlay.setBackgroundResource(R.drawable.chs_music_lrc_play_color_4);
                LYPre.setBackgroundResource(R.drawable.chs_music_lrc_pnext_color_4);
                LYNext.setBackgroundResource(R.drawable.chs_music_lrc_pnext_color_4);
                break;
            case 5:
                LYPlay.setBackgroundResource(R.drawable.chs_music_lrc_play_color_5);
                LYPre.setBackgroundResource(R.drawable.chs_music_lrc_pnext_color_5);
                LYNext.setBackgroundResource(R.drawable.chs_music_lrc_pnext_color_5);
                break;
            case 6:
                LYPlay.setBackgroundResource(R.drawable.chs_music_lrc_play_color_6);
                LYPre.setBackgroundResource(R.drawable.chs_music_lrc_pnext_color_6);
                LYNext.setBackgroundResource(R.drawable.chs_music_lrc_pnext_color_6);
                break;
            case 7:
                LYPlay.setBackgroundResource(R.drawable.chs_music_lrc_play_color_7);
                LYPre.setBackgroundResource(R.drawable.chs_music_lrc_pnext_color_7);
                LYNext.setBackgroundResource(R.drawable.chs_music_lrc_pnext_color_7);
                break;
            case 8:
                LYPlay.setBackgroundResource(R.drawable.chs_music_lrc_play_color_8);
                LYPre.setBackgroundResource(R.drawable.chs_music_lrc_pnext_color_8);
                LYNext.setBackgroundResource(R.drawable.chs_music_lrc_pnext_color_8);
                break;
            case 9:
                LYPlay.setBackgroundResource(R.drawable.chs_music_lrc_play_color_9);
                LYPre.setBackgroundResource(R.drawable.chs_music_lrc_pnext_color_9);
                LYNext.setBackgroundResource(R.drawable.chs_music_lrc_pnext_color_9);
                break;
            case 10:
                LYPlay.setBackgroundResource(R.drawable.chs_music_lrc_play_color_10);
                LYPre.setBackgroundResource(R.drawable.chs_music_lrc_pnext_color_10);
                LYNext.setBackgroundResource(R.drawable.chs_music_lrc_pnext_color_10);
                break;
        }
    }

    private void flashMusicStatus(){

        //播放模式
        if(DataStruct.CurMusic.PlayMode == MDef.PLAY_MODE_SNGLE){
            BtnMode.setBackgroundResource(R.drawable.chs_music_bar_player_mode_co_press);
        }else if(DataStruct.CurMusic.PlayMode == MDef.PLAY_MODE_RANDOM){
            BtnMode.setBackgroundResource(R.drawable.chs_music_player_mode_ran_press);
        }else if(DataStruct.CurMusic.PlayMode == MDef.PLAY_MODE_LOOP){
            BtnMode.setBackgroundResource(R.drawable.chs_music_bar_player_mode_ca_press);
        }
        if(curPlayMode != DataStruct.CurMusic.PlayMode){
            curPlayMode = DataStruct.CurMusic.PlayMode;
            if(DataStruct.CurMusic.PlayMode == MDef.PLAY_MODE_SNGLE){
                ToastUtil.showShortToast(mContext,getString(R.string.notice_playmode_repeatone));
            }else if(DataStruct.CurMusic.PlayMode == MDef.PLAY_MODE_RANDOM){
                ToastUtil.showShortToast(mContext,getString(R.string.notice_playmode_toggle));
            }else if(DataStruct.CurMusic.PlayMode == MDef.PLAY_MODE_LOOP){
                ToastUtil.showShortToast(mContext,getString(R.string.notice_playmode_repeat));
            }
        }
        //播放暂停
        if(DataStruct.CurMusic.play_status == MDef.PLAY_STATUS_PAUSE){
            BtnPlay.setBackgroundResource(R.drawable.chs_music_lrc_stop);
        }else if(DataStruct.CurMusic.play_status == MDef.PLAY_STATUS_PLAY){
            BtnPlay.setBackgroundResource(R.drawable.chs_music_lrc_play);
        }else {
            BtnPlay.setBackgroundResource(R.drawable.chs_music_lrc_stop);
        }
        //播放时间
        TV_TimeAll.setText(DataStruct.CurMusic.TotalTimeSt);
        TV_TimePlay.setText(DataStruct.CurMusic.PlayTimeSt);


        //进度
        SBPlay.setProgressMax((int) DataStruct.CurMusic.TotalTime/1000);
        SBPlay.setProgress((int)DataStruct.CurMusic.PlayTime/1000);

        //BottomVieew
        //播放模式
        if(BOOL_ShowBList_BV){
            if(DataStruct.CurMusic.PlayMode == MDef.PLAY_MODE_SNGLE){
                BtnModeB_BV.setBackgroundResource(R.drawable.chs_music_bar_player_mode_co_press);
                TVMode_BV.setText(R.string.notice_playmode_repeatone);
            }else if(DataStruct.CurMusic.PlayMode == MDef.PLAY_MODE_RANDOM){
                BtnModeB_BV.setBackgroundResource(R.drawable.chs_music_player_mode_ran_press);
                TVMode_BV.setText(R.string.notice_playmode_toggle);
            }else if(DataStruct.CurMusic.PlayMode == MDef.PLAY_MODE_LOOP){
                BtnModeB_BV.setBackgroundResource(R.drawable.chs_music_bar_player_mode_ca_press);
                TVMode_BV.setText(R.string.notice_playmode_repeat);
            }

            if(DataStruct.CurMusic.UPDATESONGSINList){
                DataStruct.CurMusic.UPDATESONGSINList = false;
                updateSongInListView(DataStruct.CurMusic.CurID,false);
            }

            if(DataStruct.CurMusic.Total > 0){
                if(LYMusicNullMsg_BV.getVisibility() == View.VISIBLE){
                    LYMusicNullMsg_BV.setVisibility(View.GONE);
                }
            }

            if(!DataStruct.CurMusic.BoolHaveUHost){
                if(LYMusicNullMsg_BV.getVisibility() == View.GONE){
                    LYMusicNullMsg_BV.setVisibility(View.VISIBLE);
                    LV_Music_BV.setVisibility(View.GONE);
                }
            }else {
                if(LYMusicNullMsg_BV.getVisibility() == View.VISIBLE){
                    LYMusicNullMsg_BV.setVisibility(View.GONE);
                    LV_Music_BV.setVisibility(View.VISIBLE);
                }
            }
        }



    }

    private void flashMusicID3(){
        //当前音乐
        if(DataStruct.CurMusic.FileName.equals("")){
            TVName.setText(getString(R.string.unknownSong));
        }else {
            TVName.setText(DataStruct.CurMusic.FileName);
        }

        if(DataStruct.CurMusic.ID3_Artist.equals("")){
            TVArtist.setText(getString(R.string.unknownArtist));
        }else {
            TVArtist.setText(DataStruct.CurMusic.ID3_Artist);
        }

    }

    private void flashMusicLrc(){

    }

    private void flashMusicLrcImg(){

    }


    private void setBgAp(int val){
        if(val == 0){
            IVArtBg.setBackgroundResource(R.drawable.chs_main_bg);
        }else {
            Bitmap bit = BitmapFactory.decodeResource(getResources(),
                    R.drawable.chs_main_bg);
            Matrix matrix = new Matrix();
            matrix.postScale(0.1f, 0.1f);
            Bitmap overlay = Bitmap.createBitmap(bit, 0, 0, bit.getWidth(),
                    bit.getHeight(), matrix, true);
            Bitmap bitmap = FastBulr.doBlur(overlay, val, true);

            Drawable d = new BitmapDrawable(bitmap);
            IVArtBg.setBackgroundDrawable(d);

        }
    }

    //用接收广播刷新UI TODO
    public class CHS_Broad_FLASHUI_BroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getExtras().get("msg").toString();
            if (msg.equals(Define.BoardCast_FlashUI_MusicPage)) {
                final String type=intent.getExtras().get(MDef.MUSICMSG_MSGTYPE).toString();

//                System.out.println("BUG MUSIC LRC-type="+type);

                if(type.equals(MDef.MUSICPAGE_Status)){
                    flashMusicStatus();
                    if((!DataStruct.CurMusic.BoolHaveUHost)||DataStruct.CurMusic.BoolPhoneBlueMusicPush ||
                            (!DataStruct.isConnecting)){
                        finish();
                    }
                }else if(type.equals(MDef.MUSICPAGE_ID3)){
                    flashMusicID3();
                    if(BOOL_ShowBList_BV){
                        updateSongInListView(DataStruct.CurMusic.CurID,false);
                    }
                    mMusicLrcID3Fragment.flashID3Info();
                }else if(type.equals(MDef.MUSICPAGE_LRC)){
//                    flashMusicLrc();
                }else if(type.equals(MDef.MUSICPAGE_LRCIMG)){
//                    flashMusicLrcImg();
                }else if(type.equals(MDef.MUSICPAGE_List)){
                    if(BOOL_ShowBList_BV){
                        flashMusicList(false);
                    }
                }else if(type.equals(MDef.MUSICPAGE_UpdateSongInList)){
                    if(BOOL_ShowBList_BV){
                        updateSongInListView(DataStruct.CurMusic.CurID,true);
                    }
                }else if(type.equals(MDef.MUSICPAGE_FListShowLoading)){
                    showLoadingDialog(2);
                }else if(type.equals(MDef.MUSICPAGE_MListShowLoading)){
                    showLoadingDialog(1);
                }else if(type.equals(MDef.MUSICPAGE_ShowConnectAgainMsg)){
                    finish();
                }
            }else if (msg.equals(Define.BoardCast_FlashUI_ConnectState)) {
                boolean res=intent.getBooleanExtra("state",false);
                if(!res){
                    finish();
                }
            }
        }
    }
    private void showLoadingDialog(int type){
//        DataStruct.BOOL_MFList_OPT = type;
//        if(mLoadingMusicListDialogFragment == null){
//            mLoadingMusicListDialogFragment = new LoadingMusicListDialogFragment();
//        }
//        if (!mLoadingMusicListDialogFragment.isAdded()) {
//            mLoadingMusicListDialogFragment.show(getFragmentManager(), "mLoadingMusicListDialogFragment");
//        }
    }
    @SuppressLint("HandlerLeak")
    private Handler handlerMainVal = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    // 移除所有的msg.what为0等消息，保证只有一个循环消息队列再跑
                    handlerMainVal.removeMessages(0);
                    LYMainVal.setVisibility(View.GONE);
                    break;
                case 1:
                    // 直接移除，定时器停止
                    handlerMainVal.removeMessages(0);
                    break;
                case 3:
                    // 直接移除，定时器停止
                    handlerMainVal.removeMessages(3);
                    flashPlayBarColor();
                    break;

                default:
                    break;
            }
        };
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        setResult(Define.ActivityResult_MusicLrcPage_Back, intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //if (keyCode == KeyEvent.KEYCODE_BACK) {
        //if (keyCode == KeyEvent.KEYCODE_MENU) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            int val = 0;
            if(DataStruct.CurMacMode.Master.DATA_TRANSFER == Define.COM_TYPE_INPUT){
                val = DataStruct.RcvDeviceData.IN_CH[0].Valume;
            }else {
                val = DataStruct.RcvDeviceData.SYS.main_vol;
            }
            if(--val < 0){
                val = 0;
            }

            if(DataStruct.CurMacMode.Master.DATA_TRANSFER == Define.COM_TYPE_INPUT){
                DataStruct.RcvDeviceData.IN_CH[0].Valume = val;
            }else {
                DataStruct.RcvDeviceData.SYS.main_vol = val;
            }

            LYMainVal.setVisibility(View.VISIBLE);
            SBMainVal.setProgress(val);

            handlerMainVal.removeMessages(0);
            handlerMainVal.sendEmptyMessageDelayed(0, 3000);
            return true;

        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            int val = 0;
            if(DataStruct.CurMacMode.Master.DATA_TRANSFER == Define.COM_TYPE_INPUT){
                val = DataStruct.RcvDeviceData.IN_CH[0].Valume;
            }else {
                val = DataStruct.RcvDeviceData.SYS.main_vol;
            }

            if(++val > DataStruct.CurMacMode.Master.MAX){
                val = DataStruct.CurMacMode.Master.MAX;
            }

            if(DataStruct.CurMacMode.Master.DATA_TRANSFER == Define.COM_TYPE_INPUT){
                DataStruct.RcvDeviceData.IN_CH[0].Valume = val;
            }else {
                DataStruct.RcvDeviceData.SYS.main_vol = val;
            }

            LYMainVal.setVisibility(View.VISIBLE);
            SBMainVal.setProgress(val);


            handlerMainVal.removeMessages(0);
            handlerMainVal.sendEmptyMessageDelayed(0, 3000);
            return true;

        }else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(CHS_Broad_Receiver!=null){
            unregisterReceiver(CHS_Broad_Receiver);
        }
        timerColorMode.cancel();
        taskColorMode.cancel();
    }

}
