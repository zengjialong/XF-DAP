package com.chs.mt.xf_dap.viewItem;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.chs.mt.xf_dap.R;


/**
 * Created by Administrator on 2017/8/7.
 */

public class V_XoverItem extends RelativeLayout {
    public Button btn_up,btn_type,btn_down;


    //监听函数
    private SetOnClickListener mSetOnClickListener = null;
    public void OnSetOnClickListener(SetOnClickListener listener) {
        this.mSetOnClickListener = listener;
    }

    public interface SetOnClickListener{
        void onClickListener(int val, int type, boolean boolClick);
    }

    //结构函数
    public V_XoverItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }
    public V_XoverItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public V_XoverItem(Context context) {
        this(context, null);
    }

    private void init(Context context) {
        //加载布局文件，与setContentView()效果一样
        LayoutInflater.from(context).inflate(R.layout.chs_viewitem_xoveritem, this);
//
        btn_up = (Button)findViewById(R.id.id_b_up);
        btn_type = (Button)findViewById(R.id.id_b_hp_freq);
        btn_down = (Button)findViewById(R.id.id_b_down);
//        HP_Freq = (Button)findViewById(R.id.id_b_hp_freq);
//        LP_Filter = (Button)findViewById(R.id.id_b_lp_filter);
//        LP_Level = (Button)findViewById(R.id.id_b_lp_level);
//        LP_Freq = (Button)findViewById(R.id.id_b_lp_freq);
        clickEvent();
    }

    /*
    响应事件
     */
    private void clickEvent(){

    }
}
