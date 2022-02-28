package com.chs.mt.xf_dap.MusicBox;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chs.mt.xf_dap.R;
import com.chs.mt.xf_dap.datastruct.DataStruct;
import com.chs.mt.xf_dap.datastruct.Define;
import com.chs.mt.xf_dap.operation.AnimationUtil;
import com.chs.mt.xf_dap.tools.MHS_SeekBarProgress;
import com.chs.mt.xf_dap.util.ToastUtil;
import com.chs.mt.xf_dap.util.img.MyAnimatorUpdateListener;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Administrator on 2017/8/7.
 */

public class V_MusicPlayItem extends RelativeLayout {
    public TextView TV_Title,TV_Artist;
    public Button BtnPre,BtnNext,BtnPlay;
    public MHS_SeekBarProgress SBPlay;
    public TextView TV_TimeAll,TV_TimePlay;
    public Button BtnPhoneBlueMusic;
    public SimpleDraweeView BtnImg;

    public Button Img_type,BtnMode;

    public RelativeLayout LY;
    private ObjectAnimator anim;//Animator ObjectAnimator
    private MyAnimatorUpdateListener listener;
    public LinearLayout LYLine,LYItem;
    private Context mContext;
    private int curMusicType = 0xff;
    //监听函数
    private SetOnClickListener mSetOnClickListener = null;
    public void OnSetOnClickListener(SetOnClickListener listener) {
        this.mSetOnClickListener = listener;
    }

    public interface SetOnClickListener{
        void onClickListener(int val, int type, boolean boolClick);
    }

    //结构函数
    public V_MusicPlayItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }
    public V_MusicPlayItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public V_MusicPlayItem(Context context) {
        this(context, null);
    }

    private void init(Context context) {
        mContext = context;
        //加载布局文件，与setContentView()效果一样
        LayoutInflater.from(context).inflate(R.layout.chs_viewitem_musicplayitem, this);
        LYItem = (LinearLayout) findViewById(R.id.id_ly_item);
        LY = (RelativeLayout) findViewById(R.id.id_ly);
        LYLine = (LinearLayout) findViewById(R.id.id_ly_line);
        TV_Title    = (TextView) findViewById(R.id.id_music_info_title);
        TV_Artist   = (TextView) findViewById(R.id.id_music_info_artist);
        TV_TimeAll  = (TextView) findViewById(R.id.id_tx_time_all);
        TV_TimePlay = (TextView) findViewById(R.id.id_tx_time);

        BtnPre  = (Button) findViewById(R.id.id_b_music_previous);
        BtnNext = (Button) findViewById(R.id.id_b_music_next);
        BtnPlay = (Button) findViewById(R.id.id_b_music_play);
        BtnPhoneBlueMusic = (Button) findViewById(R.id.id_b_phoneblue_music);
        SBPlay  = (MHS_SeekBarProgress) findViewById(R.id.id_sb_play);


        BtnImg = (SimpleDraweeView)findViewById(R.id.my_image);
        Img_type=findViewById(R.id.id_music_type);
        BtnMode=findViewById(R.id.id_b_music_play_mode);

        RoundingParams roundingParams = new RoundingParams();
        roundingParams.setCornersRadius((context.getResources().getDimensionPixelOffset(R.dimen.Music_Control_Bar_ImgSize_radius)));
        GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(context.getResources());
        GenericDraweeHierarchy hierarchy = builder.build();
        hierarchy.setRoundingParams(roundingParams);
        BtnImg.setHierarchy(hierarchy);

        LinearInterpolator lin = new LinearInterpolator();
        anim = ObjectAnimator.ofFloat(BtnImg, "rotation", 0f, 360f);
        anim.setDuration(30000);
        anim.setInterpolator(lin);
        anim.setRepeatMode(ValueAnimator.RESTART);
        anim.setRepeatCount(-1);
        listener = new MyAnimatorUpdateListener(anim);
        anim.addUpdateListener(listener);
        anim.start();

        clickEvent();
        flasMusic();
    }

    private void flasMusic() {
        if(DataStruct.CurMusic.PlayMode == MDef.PLAY_MODE_SNGLE){
            BtnMode.setBackgroundResource(R.drawable.chs_music_bar_player_mode_co_press);
        }else if(DataStruct.CurMusic.PlayMode == MDef.PLAY_MODE_RANDOM){
            BtnMode.setBackgroundResource(R.drawable.chs_music_player_mode_ran_press);
        }else if(DataStruct.CurMusic.PlayMode == MDef.PLAY_MODE_LOOP){
            BtnMode.setBackgroundResource(R.drawable.chs_music_bar_player_mode_ca_press);
        }
    }

    /*
    响应事件
     */
    private void clickEvent(){
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



        //播放暂停
        BtnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationUtil.AnimScaleBig(mContext,v);

                if((DataStruct.comType == Define.COMT_OFF)||!DataStruct.isConnecting){
                    ToastUtil.showShortToast(getContext(), getResources().getString(R.string.OffLineMsg));
                    return;
                }

                System.out.println("BUG 播放暂停"+DataStruct.CurMusic.PUMode );
                if(DataStruct.CurMusic.PUMode == MDef.PUMode_P){
                    //刷新界面连接进度
                    Intent intentw=new Intent();
                    intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
                    intentw.putExtra("msg", Define.BoardCast_FlashUI_ChangeUHostPlayIndex);
                    mContext.sendBroadcast(intentw);
                    return;
                }
//                if(!MDef.BOOL_BluetoothBusy){
                if((DataStruct.CurMusic.BoolHaveUPdateList)&&(!DataStruct.CurMusic.BoolPhoneBlueMusicPush)){
                    if(DataStruct.CurMusic.BoolPhoneBlueMusicPush){
                        if(DataStruct.CurMusic.PUMode == MDef.PUMode_P){
                            //刷新界面连接进度
                            Intent intentw=new Intent();
                            intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
                            intentw.putExtra("msg", Define.BoardCast_FlashUI_ChangeUHostPlayIndex);
                            mContext.sendBroadcast(intentw);
                            return;
                        }
                        //ToastUtil.showShortToast(getContext(),getResources().getString(R.string.bluetoothpush));
                        if(DataStruct.CurMusic.BoolHaveUHost){
                            changeUHostplay();
                        }else {
                            ToastUtil.showShortToast(getContext(),getResources().getString(R.string.ChangeUHostNullMsg));
                        }

                    }else {
                        if(DataStruct.CurMusic.play_status == MDef.PLAY_STATUS_PAUSE){
                            //播放
                            ToastUtil.showShortToast(getContext(), getResources().getString(R.string.MusicOptPlay));
                            MDOptUtil.setMusicPlay(false);
                        }else {
                            ToastUtil.showShortToast(getContext(), getResources().getString(R.string.MusicOptPause));
                            MDOptUtil.setMusicPause(false);
                        }
                    }
                }else {
                    if(DataStruct.CurMusic.PUMode == MDef.PUMode_P){
                        //刷新界面连接进度
                        Intent intentw=new Intent();
                        intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
                        intentw.putExtra("msg", Define.BoardCast_FlashUI_ChangeUHostPlayIndex);
                        mContext.sendBroadcast(intentw);
                        return;
                    }
                    if(DataStruct.CurMusic.BoolPhoneBlueMusicPush){
                        if(DataStruct.CurMusic.BoolHaveUHost){
                            ToastUtil.showShortToast(getContext(),getResources().getString(R.string.ChangeUhostMode));
                            DataStruct.RcvDeviceData.SYS.input_source=5;
                            changeUHostplay();
                        }else {
                            ToastUtil.showShortToast(getContext(),getResources().getString(R.string.ChangeUHostNullMsg));
                        }

                    }else {
//                            ToastUtil.showShortToast(getContext(), getResources().getString(R.string.WaitListMsg));
                    }
                }
//                }else {
//                    ToastUtil.showShortToast(getContext(),  getResources().getString(R.string.BusyMsg));
//                }
            }
        });
        //下一首
        BtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationUtil.AnimScaleBig(mContext,v);
                if((DataStruct.comType == Define.COMT_OFF)||!DataStruct.isConnecting){
                    ToastUtil.showShortToast(getContext(), getResources().getString(R.string.OffLineMsg));
                    return;
                }
//                if(!MDef.BOOL_BluetoothBusy){
                if((DataStruct.CurMusic.BoolHaveUPdateList)&&(!DataStruct.CurMusic.BoolPhoneBlueMusicPush)){
                    if(DataStruct.CurMusic.BoolPhoneBlueMusicPush){
                        if(DataStruct.CurMusic.PUMode == MDef.PUMode_P){
                            //刷新界面连接进度
                            Intent intentw=new Intent();
                            intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
                            intentw.putExtra("msg", Define.BoardCast_FlashUI_ChangeUHostPlayIndex);
                            mContext.sendBroadcast(intentw);
                            return;
                        }
                        //ToastUtil.showShortToast(getContext(),getResources().getString(R.string.bluetoothpush));
                        if(DataStruct.CurMusic.BoolHaveUHost){
                            changeUHostplay();
                        }else {
                            ToastUtil.showShortToast(getContext(),getResources().getString(R.string.ChangeUHostNullMsg));
                        }

                    }else {
                        DataStruct.CurMusic.resID3Info();
                        ToastUtil.showShortToast(getContext(), getResources().getString(R.string.MusicOptNext));
                        MDOptUtil.setMusicNext(false);
                    }
                }else {
                    if(DataStruct.CurMusic.BoolPhoneBlueMusicPush){
                        if(DataStruct.CurMusic.PUMode == MDef.PUMode_P){
                            //刷新界面连接进度
                            Intent intentw=new Intent();
                            intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
                            intentw.putExtra("msg", Define.BoardCast_FlashUI_ChangeUHostPlayIndex);
                            mContext.sendBroadcast(intentw);
                            return;
                        }
                        if(DataStruct.CurMusic.BoolHaveUHost){
                            ToastUtil.showShortToast(getContext(),getResources().getString(R.string.ChangeUhostMode));
                            DataStruct.RcvDeviceData.SYS.input_source=5;
                            changeUHostplay();
                        }else {
                            ToastUtil.showShortToast(getContext(),getResources().getString(R.string.ChangeUHostNullMsg));
                        }

                    }else {
//                            ToastUtil.showShortToast(getContext(), getResources().getString(R.string.WaitListMsg));
                    }
                }
//                }else {
//                    ToastUtil.showShortToast(getContext(),  getResources().getString(R.string.BusyMsg));
//                }
            }
        });
        //前一首
        BtnPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationUtil.AnimScaleI(mContext,v);
                MDOptUtil.setMusicPrev(false);
                ToastUtil.showShortToast(mContext, mContext.getResources().getString(R.string.MusicOptPre));
            }
        });

//        BtnPhoneBlueMusic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(!DataOptUtil.isConnected()){
//                    ToastUtil.showShortToast(getContext(), getResources().getString(R.string.off_line_mode));
//                    return;
//                }
//                if(!MDef.BOOL_BluetoothBusy){
//                    //蓝牙推送
//                    if(DataStruct.CurMusic.BoolPhoneBlueMusicPush){
//                        ToastUtil.showShortToast(getContext(), getResources().getString(R.string.ChangeUhostMode));
//                        MDOptUtil.setUHostPlayModeAPP(false);
//
//
////                        if(DataStruct.CurMusic.play_status == MDef.PLAY_STATUS_PAUSE){
////                            MDOptUtil.setMusicPlay(false);
////                        }
//
//                    }else {
//                        if(DataStruct.CurMusic.BoolHaveUPdateList){
//                            ToastUtil.showShortToast(getContext(), getResources().getString(R.string.ChangeBluetoothMode));
//                            MDOptUtil.setMusicPlayModeAPP(false);
//                        }else {
//                            ToastUtil.showShortToast(getContext(), getResources().getString(R.string.WaitListMsg));
//                        }
//
////                        if(DataStruct.CurMusic.play_status == MDef.PLAY_STATUS_PAUSE){
////                            MDOptUtil.setMusicPause(false);
////                        }
//                    }
//                }else {
//                    ToastUtil.showShortToast(getContext(),  getResources().getString(R.string.BusyMsg));
//                }
//            }
//        });

    }

    private void changeUHostplay(){
        AudioUtil.stopOtherPlayer(mContext);

        Timer timer = new Timer();
        timer.schedule(new TimerTask(){
            @Override
            public void run(){
                MDOptUtil.setUHostPlayModeAPP(false);
            }
        }, 1000);
    }

    public void Update(){
        //播放模式
        flasMusic();
        //播放暂停
        if(DataStruct.CurMusic.play_status == MDef.PLAY_STATUS_PAUSE){
            BtnPlay.setBackgroundResource(R.drawable.chs_music_bar_stop_normal);
            listener.pause();
//            anim.pause();
        }else if(DataStruct.CurMusic.play_status == MDef.PLAY_STATUS_PLAY){
            BtnPlay.setBackgroundResource(R.drawable.chs_music_bar_play_normal);
            listener.play();
//            anim.start();
        }else {
            BtnPlay.setBackgroundResource(R.drawable.chs_music_bar_stop_normal);
            listener.pause();
//            anim.pause();
        }

        //播放时间
        TV_TimeAll.setText(DataStruct.CurMusic.TotalTimeSt);
        TV_TimePlay.setText(DataStruct.CurMusic.PlayTimeSt);

        //音量
        //进度
        SBPlay.setProgressMax((int) DataStruct.CurMusic.TotalTime/1000);
        SBPlay.setProgress((int)DataStruct.CurMusic.PlayTime/1000);

        if(DataStruct.CurMusic.FileName.equals("")){
            TV_Title.setText(getResources().getString(R.string.unknownSong));
        }else {
            TV_Title.setText(DataStruct.CurMusic.FileName);
        }

        if(DataStruct.CurMusic.ID3_Artist.equals("")){
            TV_Artist.setText(getResources().getString(R.string.unknownArtist));
        }else {
            TV_Artist.setText(DataStruct.CurMusic.ID3_Artist);
        }

        //蓝牙推送
        if(DataStruct.CurMusic.BoolPhoneBlueMusicPush){
//            BtnPhoneBlueMusic.setBackgroundResource(R.drawable.chs_music_phonepush_selector);
            // TV_Title.setText(getResources().getString(R.string.bluetoothpush));
            TV_Artist.setText("");
            BtnPlay.setBackgroundResource(R.drawable.chs_music_bar_stop_normal);
            listener.pause();
//            anim.pause();
        }else {
//            BtnPhoneBlueMusic.setBackgroundResource(R.drawable.chs_music_uhostpush_selector);
        }

        flashCurMusicType(DataStruct.CurMusic.Form);
    }

    public void flashCurMusicType(int type){

        if(curMusicType != type){
            curMusicType = type;
            switch (curMusicType){
                case 0:
                    Img_type.setText("MP3");
                    //   BtnImg.setBackgroundResource(R.drawable.music_type_mp3);
                    break;
                case 1:
                    Img_type.setText("WMA");
                    BtnImg.setBackgroundResource(R.drawable.music_type_wma);
                    break;
                case 2:
                    Img_type.setText("WAV");
                    BtnImg.setBackgroundResource(R.drawable.music_type_wav);
                    break;
                case 3:
                    Img_type.setText("FLAC");
                    BtnImg.setBackgroundResource(R.drawable.music_type_flac);
                    break;
                case 4:
                    Img_type.setText("APE");
                    BtnImg.setBackgroundResource(R.drawable.music_type_ape);
                    break;
                case 5:
                    Img_type.setText("MP3");
                    BtnImg.setBackgroundResource(R.drawable.music_type_mp3);
                    break;
                case 6:
                    Img_type.setText("MP3");
                    BtnImg.setBackgroundResource(R.drawable.music_type_mp3);
                    break;
                default:
                    Img_type.setText("MP3");
                    // Img_type.setImageDrawable(getResources().getDrawable(R.drawable.music_type_mp3));
                    //BtnImg.setBackgroundResource(R.drawable.music_type_mp3);
                    break;
            }
        }


    }











}
