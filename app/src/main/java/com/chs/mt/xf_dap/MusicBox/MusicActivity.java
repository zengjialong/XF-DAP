package com.chs.mt.xf_dap.MusicBox;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
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
import com.chs.mt.xf_dap.datastruct.MacCfg;
import com.chs.mt.xf_dap.fragment.dialogFragment.LoadingMusicListDialogFragment;
import com.chs.mt.xf_dap.operation.AnimationUtil;
import com.chs.mt.xf_dap.operation.DataOptUtil;
import com.chs.mt.xf_dap.operation.ReturnFuncation;
import com.chs.mt.xf_dap.tools.MHS_SeekBar;
import com.chs.mt.xf_dap.util.ToastUtil;
import com.chs.mt.xf_dap.util.ToolsUtil;
import com.tandong.bottomview.view.BottomView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MusicActivity extends Activity {
    private Toast mToast;
    private Context mContext;
    /*******************************************************************/
    private LinearLayout LYUHost,LYUHostChid;
    private TextView TVCurFile,TVTitle;
    private boolean boolGoToMusicList = false;
    private int titleStatus = -1;

    private V_MusicPlayItem mMusicItem;
    private Button BtnMode,BtnUHost;
    private TextView TVMode;
    private int curPlayMode = 0;
    private Boolean BoolPlayPause = false;
    private LinearLayout LYBack,LYPlayT;
    private RelativeLayout LYPlayMode;
    private ListView LV_Music;
    private MBoxItemAdapter MBoxAdapter;
    private List<MMListInfo> ListData = new ArrayList<MMListInfo>();
    private LinearLayout LYMainVal;
    private MHS_SeekBar SBMainVal;
    private Button BtnStatusMsg;
    private boolean BOOL_ClickList=false;

    private ListView LV_File;
    private MFileItemAdapter MFileAdapter;
    private List<MFListInfo> FileListData = new ArrayList<MFListInfo>();


    //文件更新信息
    private LinearLayout LYLoadMsg;
    private TextView TVLoadOpt,TVLoadMsg;

    private LoadingMusicListDialogFragment mLoadingMusicListDialogFragment=null;
    private CHS_Broad_FLASHUI_BroadcastReceiver CHS_Broad_Receiver;
    private int fileIndex = 0;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.chs_activity_music);

        mContext = this;
        //动态注册CHS_Broad_BroadcastReceiver
        CHS_Broad_Receiver = new CHS_Broad_FLASHUI_BroadcastReceiver();
        IntentFilter CHS_Broad_filter=new IntentFilter();
        CHS_Broad_filter.addAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
        //注册receiver
        registerReceiver(CHS_Broad_Receiver, CHS_Broad_filter);
        Bundle bundle=this.getIntent().getExtras();
        fileIndex = bundle.getInt("FileIndex");

        initView();
        initClick();

        MacCfg.CurPage = Define.UI_PAGE_MUSICBOX;
        FileListData.clear();
        if(DataStruct.FileList.size()<=0){
            finish();
            return;
        }
        for(int i=0;i<DataStruct.FileList.size();i++){
            FileListData.add(DataStruct.FileList.get(i));
        }

        flashChildFileMusicList(fileIndex,true);
        curPlayMode = DataStruct.CurMusic.PlayMode;
        FlashPageUI();
    }

    //刷新页面UI
    public void FlashPageUI(){
        flashMusicStatus();
        flashMusicID3();
        //flashMusicList();
        //flashFileList();
    }

    private void initView(){
        TVTitle = (TextView) findViewById(R.id.di_tv_viewpage_name);
        //文件更新信息
        LYLoadMsg = (LinearLayout) findViewById(R.id.id_ly_msg);
        TVLoadOpt = (TextView) findViewById(R.id.id_t_opt);
        TVLoadMsg = (TextView) findViewById(R.id.id_t_optmsg);


        BtnStatusMsg = (Button) findViewById(R.id.id_b_msg);

        LYMainVal = (LinearLayout) findViewById(R.id.id_ly_sb_main_val);
        SBMainVal = (MHS_SeekBar) findViewById(R.id.id_sb_main_val);
        SBMainVal.setProgressMax(DataStruct.CurMacMode.Master.MAX);
        SBMainVal.setOnSeekBarChangeListener(new MHS_SeekBar.OnMSBSeekBarChangeListener() {
            @Override
            public void onProgressChanged(MHS_SeekBar mhs_SeekBar, int progress, boolean fromUser) {

                ReturnFuncation.setProcessVal(progress);


                handlerMainVal.removeMessages(0);
                handlerMainVal.sendEmptyMessageDelayed(0, 3000);
            }
        });
        //顶部
        LYBack  = (LinearLayout) findViewById(R.id.id_llyout_back);
        BtnUHost = (Button) findViewById(R.id.id_uhost);

        //顶部模式
        LYPlayMode = (RelativeLayout) findViewById(R.id.id_ly_playmode);
        LYPlayT = (LinearLayout) findViewById(R.id.id_ly_playtext);
        TVMode = (TextView) findViewById(R.id.id_t_playtext);
        BtnMode = (Button) findViewById(R.id.id_playmode);
        //底部
        mMusicItem = (V_MusicPlayItem) findViewById(R.id.id_music);
        mMusicItem.LYLine.setVisibility(View.GONE);
        //列表
//        for(int i=0;i<100;i++){
//            MusicListData mdata = new MusicListData();
//            mdata.ID = i;
//            mdata.FileName = String.valueOf(mdata.ID)+"-mM.FileName";
//            mdata.ID3_Title = "mM.ID3_Title";
//            mdata.ID3_Artist = "mM.ID3_Artist";
//            mdata.ID3_Album = "mM.ID3_Album";
//            mdata.ID3_Genre = "mM.ID3_Genre";
//            ListData.add(mdata);
//        }
        LV_Music = (ListView) findViewById(R.id.id_listvew_of_music);
        MBoxAdapter = new MBoxItemAdapter(mContext, ListData);
        LV_Music.setAdapter(MBoxAdapter);


        //FileList
        LYUHost = (LinearLayout) findViewById(R.id.id_ly_uhost);
        LYUHostChid = (LinearLayout) findViewById(R.id.id_ly_file);
        TVCurFile = (TextView) findViewById(R.id.id_t_file);
        LV_File = (ListView) findViewById(R.id.id_listvew_of_file);
        MFileAdapter = new MFileItemAdapter(mContext, FileListData);
        LV_File.setAdapter(MFileAdapter);
    }

    private void initClick(){

        LYBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(boolGoToMusicList){
//                    backToFileListPage();
//                }else {
                AnimationUtil.toVisibleAnim(mContext,v);
                    MacCfg.CurPage = Define.UI_PAGE_HOME;
                    finish();
//                }

            }
        });

        LYPlayT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationUtil.AnimScaleI(mContext,v);
                MDOptUtil.setMusicNext(false);
            }
        });

        mMusicItem.LYItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((DataStruct.comType == Define.COMT_OFF)||!DataStruct.isConnecting){
                    ToastUtil.showShortToast(mContext,
                            mContext.getResources().getString(R.string.OffLineMsg));
                    return;
                }
                if(DataStruct.comType >= Define.COMT_PLAY){
                    if(!DataStruct.CurMusic.BoolPhoneBlueMusicPush){
                        if(DataStruct.CurMusic.BoolHaveUHost){
//                            Intent intent = new Intent();
//                            intent.setClass(mContext, MusicLrcActivity.class);
//                            startActivity(intent);
                        }else {
                            ToolsUtil.Toast(mContext,mContext.getString(R.string.UHostOut));
                        }

                    }else {
                        ToolsUtil.Toast(mContext,mContext.getString(R.string.bluetoothpush));
                    }
                }

            }
        });
        mMusicItem.BtnPhoneBlueMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomList();
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

        BtnStatusMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationUtil.AnimScaleI(mContext,v);
                if(DataStruct.CurMusic.BoolPhoneBlueMusicPush){
//                    ToastUtil.showShortToast(mContext,getString(R.string.Changemode));
                    ToastUtil.showShortToast(mContext, getResources().getString(R.string.ChangeUhostMode));
                    MDOptUtil.setUHostPlayModeAPP(false);
                }
            }
        });


        LYUHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToFileListPage();
            }
        });

        listViewListen();
    }

    /*********************************************************************/
    /*****************************     Music  TODO        ****************************/
    /*********************************************************************/
    private void backToFileListPage(){
        if(boolGoToMusicList){
            boolGoToMusicList = false;
            LYUHostChid.setVisibility(View.INVISIBLE);
            LV_Music.setVisibility(View.GONE);
            LV_File.setVisibility(View.VISIBLE);
            LYPlayMode.setVisibility(View.GONE);

            updateSongInFileList();
        }
    }


    private void listViewListen(){
        MBoxAdapter.setOnMBoxAdpterOnItemClick(new MBoxItemAdapter.setMBoxAdpterOnItemClick() {
            @Override
            public void onAdpterClick(int which, int postion, View v) {
            final BaseAdapter adapter = (BaseAdapter) LV_Music.getAdapter();
            //MusicListData item = (MusicListData) adapter.getItem(postion);
                MMListInfo item = ListData.get(postion);
//                System.out.println("BUG MboxList postion="+postion+",item.ID==="+item.ID);
            if(which == R.id.id_ly) {
                if(DataStruct.CurMusic.BoolPhoneBlueMusicPush) {
                    //ToastUtil.showShortToast(getContext(),getResources().getString(R.string.bluetoothpush));
                    ToastUtil.showShortToast(mContext,getResources().getString(R.string.ChangeUhostMode));
                    changeUHostplay(item.ID);

                }else {
                    MDOptUtil.setMusicNumPlay(item.ID,false);

                    MacCfg.CurrentID=item.ID;
                    MacCfg.isBOOL_MusicList=true;

                    System.out.println("BUG MboxList LY");
                    for(int i=0;i<ListData.size();i++){
                        if(i == postion ){
                            ListData.get(i).boolSel=true;
                            ListData.get(i).ID3_Artist = getString(R.string.unknownArtist);
                        }else {
                            ListData.get(i).boolSel=false;
                        }
                    }
                    BOOL_ClickList = true;
                    MBoxAdapter.notifyDataSetChanged();
                }


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

    private void flashMusicStatus(){
//        if(titleStatus != DataStruct.CurMusic.config_flag){
//            titleStatus = DataStruct.CurMusic.config_flag;
//            if(1 == DataStruct.CurMusic.config_flag){
//                TVTitle.setVisibility(View.GONE);
//            }else {
//                TVTitle.setVisibility(View.VISIBLE);
//                TVTitle.setText(R.string.music_preparation_message);
//                if(DataStruct.CurMusic.BoolHaveUHost){
//                    if(DataStruct.CurMusic.BoolPhoneBlueMusicPush){
//                        TVTitle.setVisibility(View.GONE);
//                        TVTitle.setText(R.string.bluetoothpush);
//                    }
//                }else {
//                    TVTitle.setVisibility(View.GONE);
//                    TVTitle.setText(R.string.UHostOut);
//                }
//
//                if((!DataStruct.U0SynDataSucessFlg)&&(!DataStruct.isConnecting)){
//                    TVTitle.setVisibility(View.VISIBLE);
//                    TVTitle.setText(R.string.dialog_show_disconnect);
//                }
//            }
//        }

        //UHost
//        if(DataStruct.CurMusic.BoolHaveUHost){
//            BtnUHost.setVisibility(View.VISIBLE);
//            BtnUHost.setBackgroundResource(R.drawable.chs_music_uhost_press);
//        }else {
////            BtnUHost.setVisibility(View.INVISIBLE);
//            finish();
//        }

        if(DataStruct.CurMusic.BoolPhoneBlueMusicPush){
            BtnUHost.setBackgroundResource(R.drawable.chs_music_phonepush_normal);
            if(BOOL_ShowBList_BV){
                flashMusicList_BV(false);
            }
        }else {
            BtnUHost.setBackgroundResource(R.drawable.chs_music_uhostpush_normal);
        }

        if(!DataStruct.CurMusic.BoolHaveUHost){
            finish();
        }

//        if(DataStruct.CurMusic.BoolPhoneBlueMusicPush){
//            finish();
//        }
        //播放模式
        if(DataStruct.CurMusic.PlayMode == MDef.PLAY_MODE_SNGLE){
            BtnMode.setBackgroundResource(R.drawable.chs_music_bar_player_mode_co_press);
            TVMode.setText(R.string.notice_playmode_repeatone);
        }else if(DataStruct.CurMusic.PlayMode == MDef.PLAY_MODE_RANDOM){
            BtnMode.setBackgroundResource(R.drawable.chs_music_player_mode_ran_press);
            TVMode.setText(R.string.notice_playmode_toggle);
        }else if(DataStruct.CurMusic.PlayMode == MDef.PLAY_MODE_LOOP){
            BtnMode.setBackgroundResource(R.drawable.chs_music_bar_player_mode_ca_press);
            TVMode.setText(R.string.notice_playmode_repeat);
        }

        if(curPlayMode != DataStruct.CurMusic.PlayMode){
            curPlayMode = DataStruct.CurMusic.PlayMode;
            if(DataStruct.CurMusic.BoolHaveUHost){
                if(DataStruct.CurMusic.PlayMode == MDef.PLAY_MODE_SNGLE){
                    ToastUtil.showShortToast(mContext,getString(R.string.notice_playmode_repeatone));
                }else if(DataStruct.CurMusic.PlayMode == MDef.PLAY_MODE_RANDOM){
                    ToastUtil.showShortToast(mContext,getString(R.string.notice_playmode_toggle));
                }else if(DataStruct.CurMusic.PlayMode == MDef.PLAY_MODE_LOOP){
                    ToastUtil.showShortToast(mContext,getString(R.string.notice_playmode_repeat));
                }
            }
        }

        if(DataStruct.CurMusic.UPDATESONGSINList){
            DataStruct.CurMusic.UPDATESONGSINList = false;
//            updateSongInListViewWithIndex(DataStruct.CurMusic.CurID);
//            updateSongInListView();
        }

        flashBtnStatus();

        //播放状态
        mMusicItem.Update();
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

//            if(DataStruct.CurMusic.UPDATESONGSINList){
//                DataStruct.CurMusic.UPDATESONGSINList = false;
//                updateSongInListView_BV(DataStruct.CurMusic.CurID);
//            }
        }
    }

    private void flashMusicID3(){
        SBMainVal.setProgress(DataOptUtil.getInputVal());
    }

    //刷新文件列表
    private void flashFileList(){
        if(DataStruct.CurMusic.BoolHaveUPdateFileList){
//            ToastUtil.showShortToast(mContext,getString(R.string.UpdateFileListMsg));
        }else {
            return;
        }

        FileListData.clear();
        for(int i=0;i<DataStruct.FileList.size();i++){
            FileListData.add(DataStruct.FileList.get(i));
        }

        MFileAdapter = new MFileItemAdapter(mContext, FileListData);
        LV_File.setAdapter(MFileAdapter);
        FileListViewListen();

        updateSongInFileList();
    }

    private void flashFileListMax(){
        FileListData.clear();
        for(int i=0;i<DataStruct.CurMusic.UpdateFileOfMusicListNum;i++){
            FileListData.add(DataStruct.FileList.get(i));
        }

        MFileAdapter = new MFileItemAdapter(mContext, FileListData);
        LV_File.setAdapter(MFileAdapter);
        FileListViewListen();

        updateSongInFileList();
    }

    private void FileListViewListen(){
        MFileAdapter.setOnMFileAdpterOnItemClick(new MFileItemAdapter.setMFileAdpterOnItemClick() {
            @Override
            public void onAdpterClick(int which, int postion, View v) {
                if(which == R.id.id_file_ly) {
                    flashChildFileMusicList(postion,false);
                }
            }
        });
    }

    private void flashChildFileMusicList(int index,boolean ft){

        MFListInfo item = FileListData.get(index);
        boolGoToMusicList = true;
        LYUHostChid.setVisibility(View.GONE);
        TVCurFile.setText(item.FileName);
        LV_Music.setVisibility(View.VISIBLE);
        LV_File.setVisibility(View.GONE);
        LYPlayMode.setVisibility(View.VISIBLE);
        TVTitle.setVisibility(View.VISIBLE);
        TVTitle.setText(item.FileName);

        ListData.clear();
        ListData.removeAll(ListData);
        for(int i=0;i<item.CurMusicList.size();i++){
            ListData.add(item.CurMusicList.get(i));
        }
        DataStruct.CurMusic.resID3Info();
        updateSongInListView(ft);
    }


    private void updateSongInFileList(){
        for(int i=0;i<FileListData.size();i++){
            if((DataStruct.CurMusic.CurID>=FileListData.get(i).SongsStartID)
                    &&(DataStruct.CurMusic.CurID<=FileListData.get(i).SongsEndID)){

                for(int j=0;j<FileListData.size();j++){
                    if(i == j){
                        FileListData.get(j).sel = true;
                    }else {
                        FileListData.get(j).sel = false;
                    }
                }
                MFileAdapter.notifyDataSetChanged();
                break;
            }
        }
    }


    private void flashMusicList(){
        if(DataStruct.CurMusic.BoolHaveUPdateList){
//            ToastUtil.showShortToast(mContext,getString(R.string.UpdateMusicListMsg));
        }

        ListData.clear();
        for(int i=0;i<DataStruct.MusicList.size();i++){
            MMListInfo mM = DataStruct.MusicList.get(i);
            MMListInfo mdata = new MMListInfo();
            mdata.boolSel = false;
            mdata.ID = mM.ID;
            //System.out.println("BUG MboxList mdata.ID="+mdata.ID);
            mdata.FileName   = mM.FileName;
            mdata.ID3_Title  = mM.ID3_Title;
            mdata.ID3_Artist = getString(R.string.unknown);//mM.ID3_Artist;
            mdata.ID3_Album  = mM.ID3_Album;
            mdata.ID3_Genre  = mM.ID3_Genre;
            ListData.add(mdata);
        }
        //MBoxAdapter.notifyDataSetChanged();
        MBoxAdapter = new MBoxItemAdapter(mContext, ListData);
        LV_Music.setAdapter(MBoxAdapter);
        listViewListen();

        updateSongInListViewWithIndex(DataStruct.CurMusic.CurID);
    }

    private void updateSongInListViewWithIndex(int index){
        index--;
        if(ListData.size() > index){
            for(int i=0;i<ListData.size();i++){
                if(i == index ){
                    ListData.get(i).boolSel=true;
                    ListData.get(i).ID3_Title = DataStruct.CurMusic.ID3_Title;
                    ListData.get(i).ID3_Artist = DataStruct.CurMusic.ID3_Artist;
                    ListData.get(i).ID3_Album = DataStruct.CurMusic.ID3_Album;
                    ListData.get(i).ID3_Genre = DataStruct.CurMusic.ID3_Genre;
                }else {
                    ListData.get(i).boolSel=false;
                }
            }
            MBoxAdapter.notifyDataSetChanged();
            if(BOOL_ClickList){
                BOOL_ClickList = false;
                return;
            }
            LV_Music.setSelectionFromTop(index, 4);
        }
    }

    private void updateSongInListView(boolean ft){
        int index = 0;
        for(int i=0;i<ListData.size();i++){
            if(DataStruct.CurMusic.CurID == ListData.get(i).ID){
                ListData.get(i).boolSel=true;
                ListData.get(i).ID3_Title  = DataStruct.CurMusic.ID3_Title;
                ListData.get(i).ID3_Artist = DataStruct.CurMusic.ID3_Artist;
                ListData.get(i).ID3_Album  = DataStruct.CurMusic.ID3_Album;
                ListData.get(i).ID3_Genre  = DataStruct.CurMusic.ID3_Genre;
                index = i;
            }else {
                ListData.get(i).boolSel=false;
            }

        }
        System.out.println("BUG 就这样吧");
        if(MacCfg.isBOOL_MusicList==true){//用户手动点击了并且数据错误了
            MacCfg.isBOOL_MusicList=false;

            if(MacCfg.CurrentID!=DataStruct.CurMusic.CurID){
                Toast.makeText(mContext,getResources().getString(R.string.musicerror), Toast.LENGTH_LONG).show();
            }
            MacCfg.CurrentID=DataStruct.CurMusic.CurID;
        }

        MBoxAdapter.notifyDataSetChanged();
        if(BOOL_ClickList){
            BOOL_ClickList = false;
            return;
        }
        MBoxAdapter = new MBoxItemAdapter(mContext, ListData);
        LV_Music.setAdapter(MBoxAdapter);
        listViewListen();
        if(ft){
            LV_Music.setSelectionFromTop(index, 0);
        }
    }

    private void setStatuesBtn(boolean show, String msg){
        if(show){
            BtnStatusMsg.setVisibility(View.VISIBLE);
            BtnStatusMsg.setText(msg);
        }else {
            BtnStatusMsg.setVisibility(View.GONE);
        }
    }

    private void flashBtnStatus(){

        if(DataStruct.CurMusic.BoolHaveUHost){
            setStatuesBtn(false,"");

//            if(DataStruct.CurMusic.BoolPhoneBlueMusicPush){
//                setStatuesBtn(true,getString(R.string.bluetoothpush)+
//                ","+getString(R.string.UHostPlayMsg));
//            }else {
//                setStatuesBtn(false,"");
//            }

        }else {
            setStatuesBtn(true,getString(R.string.UHostOut));
            setUIDefault();
        }

        if((!DataStruct.U0SynDataSucessFlg)&&(!DataStruct.isConnecting)){
            setStatuesBtn(true,getString(R.string.dialog_show_disconnect));
            setUIDefault();
        }

    }

    private void setUIDefault(){
        backToFileListPage();

        //清除列表
        ListData.clear();
        ListData.removeAll(ListData);
        DataStruct.MusicList.clear();
        DataStruct.MusicList.removeAll(DataStruct.MusicList);
        updateSongInListView(false);

        FileListData.clear();
        FileListData.removeAll(FileListData);
        DataStruct.FileList.clear();
        DataStruct.FileList.removeAll(DataStruct.FileList);
        flashFileList();

    }

    private void changeUHostplay(int index){
//        AudioUtil.stopOtherPlayer(mContext);
//
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask(){
//            @Override
//            public void run(){
//                MDOptUtil.setUHostPlayModeAPP(false);
//            }
//        }, 1000);
//        changeUHostplaySongs(index);
    }
    private void changeUHostplaySongs(int index){
        final int adex = index;
        Timer timer = new Timer();
        timer.schedule(new TimerTask(){
            @Override
            public void run(){
                MDOptUtil.setMusicNumPlay(adex,false);
            }
        }, 2000);
    }
    /*********************************************************************/
    /******************   底部弹出  TODO  ********************/
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
        listViewListen_BV();
        flashMusicList_BV(true);

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

    private void flashMusicList_BV(boolean ft){
        if(BOOL_ClickList_BV){
            BOOL_ClickList_BV = false;
            return;
        }
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

        //MBoxAdapter_BV.notifyDataSetChanged();
        MBoxAdapter_BV = new MBoxItemAdapter(mContext, ListData_BV);
        LV_Music_BV.setAdapter(MBoxAdapter_BV);
        listViewListen_BV();

        updateSongInListView_BV(DataStruct.CurMusic.CurID,ft);
    }

    private void updateSongInListView_BV(int index,boolean ft){
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
            MBoxAdapter_BV.notifyDataSetChanged();
            if(BOOL_ClickList_BV){
                BOOL_ClickList_BV = false;
                return;
            }
            if(ft){
                LV_Music_BV.setSelectionFromTop(index, 4);
            }

        }
    }
    private void listViewListen_BV(){
        MBoxAdapter_BV.setOnMBoxAdpterOnItemClick(new MBoxItemAdapter.setMBoxAdpterOnItemClick() {
            @Override
            public void onAdpterClick(int which, int postion, View v) {
                final BaseAdapter adapter = (BaseAdapter) LV_Music_BV.getAdapter();
                //MusicListData item = (MusicListData) adapter.getItem(postion);
                MMListInfo item = ListData_BV.get(postion);
//                System.out.println("BUG MboxList postion="+postion+",item.ID==="+item.ID);
                if(which == R.id.id_ly) {
                    if(DataStruct.CurMusic.BoolPhoneBlueMusicPush) {
                        //ToastUtil.showShortToast(getContext(),mContext.getResources().getString(R.string.bluetoothpush));
                        ToastUtil.showShortToast(mContext,mContext.getResources().getString(R.string.ChangeUhostMode));
                        changeUHostplay(item.ID);

                    }else {
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
                        BOOL_ClickList_BV = true;
                    }

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



    /*********************************************************************/
    /******************   SYSTEM  TODO  ********************/
    /*********************************************************************/
    //用接收广播刷新UI TODO
    public class CHS_Broad_FLASHUI_BroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getExtras().get("msg").toString();
            if (msg.equals(Define.BoardCast_FlashUI_MusicPage)) {
                final String type=intent.getExtras().get(MDef.MUSICMSG_MSGTYPE).toString();
                if(type.equals(MDef.MUSICPAGE_Status)){
                    flashMusicStatus();
                }else if(type.equals(MDef.MUSICPAGE_ID3)){
                    flashMusicID3();

                    updateSongInListView(true);
//                    updateSongInFileList();
                    if(BOOL_ShowBList_BV){
                        updateSongInListView_BV(DataStruct.CurMusic.CurID,false);
                    }
                }else if(type.equals(MDef.MUSICPAGE_List)){
//                    updateSongInListView();
//                    flashMusicList();
                    flashChildFileMusicList(fileIndex,true);
                }else if(type.equals(MDef.MUSICPAGE_FileList)){
                    flashFileList();
                }else if(type.equals(MDef.MUSICPAGE_UpdateSongInList)){
                    if(BOOL_ShowBList_BV){
                        //在ID3刷新
                        //updateSongInListView_BV(DataStruct.CurMusic.CurID,true);
                    }
                }
                else if(type.equals(MDef.MUSICPAGE_FListShowLoading)){
                    if(DataStruct.CurMusic.BoolHaveUPdateFileList){
                        if(LYLoadMsg.getVisibility() == View.VISIBLE){
                            LYLoadMsg.setVisibility(View.GONE);
                        }
                        ToastUtil.showShortToast(mContext,getString(R.string.UpdateFileListMsg));
                    }else {
                        if(LYLoadMsg.getVisibility() == View.GONE){
                            LYLoadMsg.setVisibility(View.VISIBLE);
                        }
                        TVLoadOpt.setText(R.string.ScanFile);
                        TVLoadMsg.setText(String.valueOf(DataStruct.CurMusic.UpdateFileListStart-1)+"/"
                                +String.valueOf(DataStruct.CurMusic.FileTotal));
                    }
//                    showLoadingDialog(2);
                }else if(type.equals(MDef.MUSICPAGE_MListShowLoading)){
                    if(DataStruct.CurMusic.BoolHaveUPdateList){
                        if(LYLoadMsg.getVisibility() == View.VISIBLE){
                            LYLoadMsg.setVisibility(View.GONE);
                        }
                        ToastUtil.showShortToast(mContext,getString(R.string.UpdateMusicListMsg));
                    }else {
                        if(LYLoadMsg.getVisibility() == View.GONE){
                            LYLoadMsg.setVisibility(View.VISIBLE);
                        }
                        TVLoadOpt.setText(R.string.UpdateList);
                        TVLoadMsg.setText(String.valueOf(DataStruct.CurMusic.UpdateListStart-1)+"/"
                                +String.valueOf(DataStruct.CurMusic.file_total));
                    }


                    //更新文件显示
                    flashFileListMax();
//                    showLoadingDialog(1);
                }else if(type.equals(MDef.MUSICPAGE_ShowConnectAgainMsg)){
                    ToastUtil.showShortToast(mContext, mContext.getResources().getString(R.string.DSPPlayDisconnectedMsg));
                    DataStruct.CurMusic.resetData();
                    mMusicItem.Update();
                    FlashPageUI();
                }
            }else if (msg.equals(Define.BoardCast_FlashUI_ConnectState)) {
                boolean res=intent.getBooleanExtra("state",false);
                if(!res){
//                    DataStruct.CurMusic.resetData();
//                    mMusicItem.Update();
//                    FlashPageUI();
                    finish();
                }
            }
        }
    }

    private void showLoadingDialog(int type){
        DataStruct.BOOL_MFList_OPT = type;
        if(mLoadingMusicListDialogFragment == null){
            mLoadingMusicListDialogFragment = new LoadingMusicListDialogFragment();
        }
        if (!mLoadingMusicListDialogFragment.isAdded()) {
            mLoadingMusicListDialogFragment.show(getFragmentManager(), "mLoadingMusicListDialogFragment");
        }


    }

    private Handler handlerMainVal = new Handler() {
        public void handleMessage(android.os.Message msg) {
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

                default:
                    break;
            }
        };
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        System.out.println("BUG onActivityResult requestCode="+requestCode);
        switch (requestCode) {
            case Define.ActivityResult_MusicLrcPage_Back:
                if(boolGoToMusicList){
                    updateSongInListView(true);
                }else {
                    updateSongInFileList();
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //if (keyCode == KeyEvent.KEYCODE_BACK) {
        //if (keyCode == KeyEvent.KEYCODE_MENU) {
//        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
//            int val = 0;
//            if(DataStruct.CurMacMode.Master.DATA_TRANSFER == Define.COM_TYPE_INPUT){
//                val = DataStruct.RcvDeviceData.IN_CH[0].Valume;
//            }else {
//                val = DataOptUtil.getInputVal();
//            }
//            if(--val < 0){
//                val = 0;
//            }
//            DataOptUtil.setInputVal(val);
//
//            LYMainVal.setVisibility(View.VISIBLE);
//            SBMainVal.setProgress(val);
//
//            handlerMainVal.removeMessages(0);
//            handlerMainVal.sendEmptyMessageDelayed(0, 3000);
//            return true;
//
//        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
//            int val = 0;
//            if(DataStruct.CurMacMode.Master.DATA_TRANSFER == Define.COM_TYPE_INPUT){
//                val = DataStruct.RcvDeviceData.IN_CH[0].Valume;
//            }else {
//                val = DataOptUtil.getInputVal();
//            }
//
//            if(++val >66){
//                val = 66;
//            }
//
//
//            DataOptUtil.setInputVal(val);
//
//            LYMainVal.setVisibility(View.VISIBLE);
//            SBMainVal.setProgress(val);
//
//            handlerMainVal.removeMessages(0);
//            handlerMainVal.sendEmptyMessageDelayed(0, 3000);
//            return true;
//
//        }else if (keyCode == KeyEvent.KEYCODE_BACK) {
////            if(boolGoToMusicList){
////                backToFileListPage();
////            }else {
//                //MacCfg.CurPage = Define.UI_PAGE_HOME;
//                finish();
////            }
//            return true;
//        }else {
            return super.onKeyDown(keyCode, event);
      //  }
    }

    /**
     * 监听Back键按下事件,方法1:
     * 注意:
     * super.onBackPressed()会自动调用finish()方法,关闭
     * 当前Activity.
     * 若要屏蔽Back键盘,注释该行代码即可

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MacCfg.CurPage = Define.UI_PAGE_HOME;
        DataOptUtil.send_get_SYSTEM_DATA_CMD();

        Intent intent = new Intent();
        setResult(Define.ActivityResult_MusicPage_Back, intent);
    }
     */


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(CHS_Broad_Receiver!=null){
            unregisterReceiver(CHS_Broad_Receiver);
        }
    }

}
