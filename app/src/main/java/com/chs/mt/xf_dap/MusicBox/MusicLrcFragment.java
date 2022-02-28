package com.chs.mt.xf_dap.MusicBox;


import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;


import com.chs.mt.xf_dap.R;
import com.chs.mt.xf_dap.datastruct.DataStruct;
import com.chs.mt.xf_dap.datastruct.Define;
import com.chs.mt.xf_dap.util.img.MyAnimatorUpdateListener;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MusicLrcFragment extends Fragment {

    private Context mContext;
    private SimpleDraweeView CenterImg,DiePlay,DieStop;
    private ObjectAnimator anim;
    private MyAnimatorUpdateListener listener;
//    private ShadowImageView CenterImgSV;
    private int playState = 0;
    /*******************************************************************/
    private CHS_Broad_FLASHUI_BroadcastReceiver CHS_Broad_Receiver=null;
    /*******************************************************************/
    public MusicLrcFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.chs_fragment_music_lrc, container, false);
        mContext = getActivity().getApplicationContext();
        //动态注册CHS_Broad_BroadcastReceiver
        CHS_Broad_Receiver = new CHS_Broad_FLASHUI_BroadcastReceiver();
        IntentFilter CHS_Broad_filter=new IntentFilter();
        CHS_Broad_filter.addAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
        mContext.registerReceiver(CHS_Broad_Receiver, CHS_Broad_filter);

        initView(view);
        initClick();
        FlashPageUI();
        return view;
    }

    //刷新页面UI
    public void FlashPageUI(){
        flashMusicStatus();
    }

    private void initView(View view){
        DiePlay   = (SimpleDraweeView) view.findViewById(R.id.my_image_play);
        DieStop   = (SimpleDraweeView) view.findViewById(R.id.my_image_stop);
        CenterImg   = (SimpleDraweeView) view.findViewById(R.id.my_image);
//        CenterImgSV = (ShadowImageView) view.findViewById(R.id.my_image_sv);
//        CenterImgSV.startRotateAnimation();

        RoundingParams roundingParams = new RoundingParams();
        roundingParams.setCornersRadius((mContext.getResources().getDimensionPixelOffset(R.dimen.Music_LRCPage_ImgSize_radius)));
        GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(mContext.getResources());
        GenericDraweeHierarchy hierarchy = builder.build();
        hierarchy.setRoundingParams(roundingParams);
        CenterImg.setHierarchy(hierarchy);

        LinearInterpolator lin = new LinearInterpolator();
        anim = ObjectAnimator.ofFloat(CenterImg, "rotation", 0f, 360f);
        anim.setDuration(30000);
        anim.setInterpolator(lin);
        anim.setRepeatMode(ValueAnimator.RESTART);
        listener = new MyAnimatorUpdateListener(anim);
        anim.addUpdateListener(listener);
        anim.setRepeatCount(-1);
        anim.start();


    }

    private void initClick(){

    }
    private void flashMusicStatus(){
        //播放暂停
        if(playState != DataStruct.CurMusic.play_status){
            playState = DataStruct.CurMusic.play_status;
            if(DataStruct.CurMusic.play_status == MDef.PLAY_STATUS_PAUSE){
                listener.pause();
                DiePlay.setVisibility(View.GONE);
                DieStop.setVisibility(View.VISIBLE);
            }else if(DataStruct.CurMusic.play_status == MDef.PLAY_STATUS_PLAY){
                listener.play();
                DiePlay.setVisibility(View.VISIBLE);
                DieStop.setVisibility(View.GONE);
            }else {
                listener.pause();
                DiePlay.setVisibility(View.GONE);
                DieStop.setVisibility(View.VISIBLE);
            }
        }

    }

    //用接收广播刷新UI TODO
    public class CHS_Broad_FLASHUI_BroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getExtras().get("msg").toString();
            if (msg.equals(Define.BoardCast_FlashUI_MusicPage)) {
                final String type=intent.getExtras().get(MDef.MUSICMSG_MSGTYPE).toString();
                if(type.equals(MDef.MUSICPAGE_Status)){
                    flashMusicStatus();
                }
                else if(type.equals(MDef.MUSICPAGE_ID3)){

                }else if(type.equals(MDef.MUSICPAGE_List)){

                }else if(type.equals(MDef.MUSICPAGE_UpdateSongInList)){

                }else if(type.equals(MDef.MUSICPAGE_FileList)){

                }else if(type.equals(MDef.MUSICPAGE_FListShowLoading)){

                }else if(type.equals(MDef.MUSICPAGE_MListShowLoading)){

                }else if(type.equals(MDef.MUSICPAGE_ShowConnectAgainMsg)){

                }
            }else if (msg.equals(Define.BoardCast_FlashUI_ConnectState)) {
                boolean res=intent.getBooleanExtra("state",false);
                if(!res){

                }
            }else if (msg.equals(Define.BoardCast_FlashUI_AllPage)) {

            }
        }
    }

}
