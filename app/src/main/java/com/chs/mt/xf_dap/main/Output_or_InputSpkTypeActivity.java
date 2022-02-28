package com.chs.mt.xf_dap.main;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;


import com.chs.mt.xf_dap.R;
import com.chs.mt.xf_dap.datastruct.DataStruct;
import com.chs.mt.xf_dap.datastruct.MacCfg;
import com.chs.mt.xf_dap.operation.DataOptUtil;
import com.chs.mt.xf_dap.tools.MyScrollView;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class Output_or_InputSpkTypeActivity extends Activity implements MyScrollView.ScrollViewListener {
    private TextView title0, title1, title2, title3, title4, title, title5;
    private long topDistance0, topDistance1, topDistance2, topDistance3, topDistance4, topDistance5, height;
    private Button btn_exit;
    private MyScrollView scrollView;
    private int distance;
    private int DataOP = 2;

    //

    private TextView text_exit;

    private int MAXINPUT = 16;

    private TextView[] txt_output_spk = new TextView[25];

    private Output_or_InputSpkTypeActivity.SetOnClickDialogListener mSetOnClickDialogListener;

    public void OnSetOnClickDialogListener(Output_or_InputSpkTypeActivity.SetOnClickDialogListener listener) {
        this.mSetOnClickDialogListener = listener;
    }

    public interface SetOnClickDialogListener {
        void onClickDialogListener(int type, boolean boolClick);
    }


    private List<Integer> aList = new ArrayList<>();

    private Context mcontet;
    private Boolean boolSetTw = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.chs_dialog_output_input_spk_type);
        mcontet = this;
        initView();
        initClick();
        FlashPageUI();

    }

    /*刷新界面*/
    private void FlashPageUI() {
            setOutput();
            setOutputNumberOverTwo();
            text_exit.setText(getResources().getString(R.string.settings) + "\t" + getResources().getString(R.string.out_link) + String.valueOf(MacCfg.OutputChannelSel + 1) + "\t" + getResources().getString(R.string.output_input_type));


    }

    private void setOutput() {

    }

    private void setInput() {

    }


    /**
     * 设置当前通道数大于二的时候不可以选该通道(输出)
     */
    private void setOutputNumberOverTwo() {
        aList.clear();
        for (int i = 0; i < DataStruct.CurMacMode.Out.SPK_TYPE.length; i++) {
            int count = 0;
            for (int j = DataStruct.CurMacMode.Out.SPK_TYPE.length - 1; j >= 0; j--) { //循环后续所有元素

            }
        }
    }




    private void initView() {
        btn_exit = findViewById(R.id.id_exit);
        text_exit = findViewById(R.id.id_txt_out_name_msg);
        title0 = (TextView) findViewById(R.id.title0);
        title1 = (TextView) findViewById(R.id.title1);
        title2 = (TextView) findViewById(R.id.title2);
        title3 = (TextView) findViewById(R.id.title3);

        title5 = (TextView) findViewById(R.id.title5);
        title = (TextView) findViewById(R.id.id_title);
        scrollView = (MyScrollView) findViewById(R.id.scv);
        scrollView.setScrollViewListener(this);

        txt_output_spk[0] = findViewById(R.id.id_txt_0);
        txt_output_spk[1] = findViewById(R.id.id_txt_1);
        txt_output_spk[2] = findViewById(R.id.id_txt_2);
        txt_output_spk[3] = findViewById(R.id.id_txt_3);
        txt_output_spk[4] = findViewById(R.id.id_txt_4);
        txt_output_spk[5] = findViewById(R.id.id_txt_5);
        txt_output_spk[6] = findViewById(R.id.id_txt_6);
        txt_output_spk[7] = findViewById(R.id.id_txt_7);
        txt_output_spk[8] = findViewById(R.id.id_txt_8);
        txt_output_spk[9] = findViewById(R.id.id_txt_9);
        txt_output_spk[10] = findViewById(R.id.id_txt_10);
        txt_output_spk[11] = findViewById(R.id.id_txt_11);
        txt_output_spk[12] = findViewById(R.id.id_txt_12);
        txt_output_spk[13] = findViewById(R.id.id_txt_13);
        txt_output_spk[14] = findViewById(R.id.id_txt_14);
        txt_output_spk[15] = findViewById(R.id.id_txt_15);
        txt_output_spk[16] = findViewById(R.id.id_txt_16);
        txt_output_spk[17] = findViewById(R.id.id_txt_17);
        txt_output_spk[18] = findViewById(R.id.id_txt_18);
        txt_output_spk[19] = findViewById(R.id.id_txt_19);
        txt_output_spk[20] = findViewById(R.id.id_txt_20);
        txt_output_spk[21] = findViewById(R.id.id_txt_21);
        txt_output_spk[22] = findViewById(R.id.id_txt_22);
        txt_output_spk[23] = findViewById(R.id.id_txt_23);
        txt_output_spk[24] = findViewById(R.id.id_txt_24);

        for (int i = 0; i < txt_output_spk.length; i++) {
            txt_output_spk[i].setTag(i);
        }
    }

    private void initClick() {
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Output_or_InputSpkTypeActivity.this.finish();
            }
        });

        for (int i = 0; i < txt_output_spk.length; i++) {
            txt_output_spk[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        int outputspk = (int) v.getTag();
                        DataOptUtil.setOutputChannelSKP(MacCfg.OutputChannelSel, outputspk);
                        Output_or_InputSpkTypeActivity.this.finish();
                    } catch (Exception e) {
                        StringWriter sw = new StringWriter();
                        PrintWriter pw = new PrintWriter(sw);
                        e.printStackTrace(pw);
                        String msg = sw.toString();
                        System.out.println(msg);

                    }
                }
            });
        }
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            topDistance5 = title5.getTop();
            topDistance0 = title0.getTop();
            topDistance1 = title1.getTop();
            topDistance2 = title2.getTop();
            topDistance3 = title3.getTop();//按钮左上角相对于父view（LinerLayout）的y坐标

            height = title.getMeasuredHeight();

        }
    }

    @Override
    public void onScrollChanged(int x, int y, int oldx, int oldy) {
        distance = y;
        distancePlace(topDistance5, topDistance0);
        distancePlaceLayout(topDistance0, topDistance5);

        distancePlace(topDistance0, topDistance1);
        distancePlaceLayout(topDistance1, topDistance0);


        distancePlace(topDistance1, topDistance2);
        distancePlaceLayout(topDistance2, topDistance1);

        distancePlace(topDistance2, topDistance3);
        distancePlaceLayout(topDistance3, topDistance2);

        distancePlace(topDistance3, topDistance4);
        distancePlaceLayout(topDistance4, topDistance3);

        if (distance > topDistance4) {
            title.layout(0, 0, title.getRight(), (int) height);
            showText(topDistance4);
        }
    }

    private void distancePlaceLayout(long topDistance1, long topDistance0) {
        if (distance > topDistance1 - height && distance < topDistance1) {
            title.layout(0, (int) (topDistance1 - distance - height), title.getRight(), (int) (topDistance1 - distance));
            showText(topDistance0);
        }
    }

    private void distancePlace(long topDistance0, long topDistance1) {
        if (distance >= topDistance0 && distance < topDistance1 - height) {
            title.layout(0, 0, title.getRight(), (int) height);
            showText(topDistance0);
        }
    }

    private void showText(long topDistance) {

        if (topDistance == topDistance5) {
            title.setText(getResources().getString(R.string.NULL));
        }
        if (topDistance == topDistance0) {
            title.setText(getResources().getString(R.string.setpreposition));
        }
        if (topDistance == topDistance1) {
            title.setText(getResources().getString(R.string.setpostposition));
        }
        if (topDistance == topDistance2) {
            title.setText(getResources().getString(R.string.setcenter));
        }
        if (topDistance == topDistance3) {
            title.setText(getResources().getString(R.string.setSub));

        }
        if (topDistance == topDistance4) {
            title.setText(getResources().getString(R.string.setrearout));
        }
    }
}
