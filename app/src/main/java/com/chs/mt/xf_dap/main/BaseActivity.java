package com.chs.mt.xf_dap.main;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chs.mt.xf_dap.R;
import com.chs.mt.xf_dap.datastruct.DataStruct;
import com.chs.mt.xf_dap.operation.SysApplication;
import com.chs.mt.xf_dap.service.ServiceOfCom;
import com.chs.mt.xf_dap.tools.EQ_SeekBar;


public class BaseActivity extends FragmentActivity {
    private Context mContext;
    //自定义弹出框设置音量
    private PopupWindow popupWindow;
    private SeekBar seekBar ;
    private EQ_SeekBar eq_seekBar ;
    private TextView txt_gain;
    public interface setData {
        void setBack(String name);
    }

    /**
     * 手势监听
     */
    GestureDetector mGestureDetector;
    /**
     * 是否需要监听手势关闭功能
     */
    private boolean mNeedBackGesture = true;

    // 再点一次退出程序时间设置
    private long TOUCH_TIME = 0L;

    //当前时间与上次点击返回键的时间差
    private static final long WAIT_TIME = 2000L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
        SysApplication.addActivity(this);//添加
        initGestureDetector();
        mContext = this;
//        audioManager= (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//        MacCfg.System_Vol_Max= audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);//音乐音量的最大值
//        MacCfg.System_Vol_Current = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);  //当前的音乐音量
    }

    private void initGestureDetector() {
//        if (mGestureDetector == null) {
//            mGestureDetector = new GestureDetector(getApplicationContext(),
//                    new BackGestureListener(this));
//        }
    }


    /* (non-Javadoc)
     * touch手势分发
     * @see android.app.Activity#dispatchTouchEvent(android.view.MotionEvent)
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
//        if (mNeedBackGesture) {
//            return mGestureDetector.onTouchEvent(ev)
//                    || super.dispatchTouchEvent(ev);
//        }
        return super.dispatchTouchEvent(ev);
    }

    /*
     * 设置是否进行手势监听
     */
    public final void setNeedBackGesture(boolean mNeedBackGesture) {
        this.mNeedBackGesture = mNeedBackGesture;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SysApplication.removeActivity(this);//清除
    }






}
