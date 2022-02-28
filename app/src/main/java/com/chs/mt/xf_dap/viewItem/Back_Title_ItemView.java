package com.chs.mt.xf_dap.viewItem;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chs.mt.xf_dap.R;
import com.chs.mt.xf_dap.datastruct.DataStruct;


public class Back_Title_ItemView extends LinearLayout {

    public ImageView img_back;
    public TextView txt_features;
    public LinearLayout ly_back;
    private Context mcontext;
    public Button btn_sure;


    public Back_Title_ItemView(Context context) {
        super(context);
        initview();
    }

    public Back_Title_ItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initview();
    }

    public Back_Title_ItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initview();
    }

    private void initview() {
        View view = View.inflate(getContext(), R.layout.back_title_item, this);
        mcontext = getContext();
        img_back = view.findViewById(R.id.id_back);
        txt_features = view.findViewById(R.id.id_features_text);
        btn_sure = view.findViewById(R.id.id_back_sure);
        ly_back = view.findViewById(R.id.id_ly_back);
    }

    /**
     * 设置标题名称
     */
    public void setTextfeatures(String name) {
        txt_features.setText(name);
    }

    /**
     * 返回按钮的点击事件
     */
    public void BackClick(final Activity activity) {
        img_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
                ;
                activity.overridePendingTransition(R.anim.left_in, R.anim.right_out);

            }
        });
        ly_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
                ;
                activity.overridePendingTransition(R.anim.left_in, R.anim.right_out);
            }
        });



    }
}
