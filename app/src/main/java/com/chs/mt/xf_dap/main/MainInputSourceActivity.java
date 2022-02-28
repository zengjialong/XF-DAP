package com.chs.mt.xf_dap.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.chs.mt.xf_dap.R;
import com.chs.mt.xf_dap.datastruct.DataStruct;
import com.chs.mt.xf_dap.viewItem.Back_Title_ItemView;

/**
 *主音源界面
 * */
public class MainInputSourceActivity extends BaseActivity {
    private Context mcontext;
    private String name;
    private int inputsourceIndex;
    private Back_Title_ItemView back_title_itemView;
    private ImageView img_inputsource;
    private TextView txt_band;
    private LinearLayout linearLayout[]=new LinearLayout[DataStruct.CurMacMode.inputsource.Max]   ;
    private ImageView imageView[]=new ImageView[DataStruct.CurMacMode.inputsource.Max];
    private int sourceNum=0;
    private InputSourceReceiver dataReceiver;
    private LinearLayout ly_inputsource;

    /**设置界面显示的图片*/
    private int[] image=new int[]{
            R.drawable.chs_back,
            R.drawable.chs_back,
            R.drawable.chs_back,
            R.drawable.chs_back,
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.chs_main_inputsource);
        mcontext = this;
        // 首先获取到意图对象
        Intent intent = getIntent();
        // 获取到传递过来的姓名
        name = intent.getStringExtra("name");
        inputsourceIndex = intent.getExtras().getInt("index",0);
        initView();
        FlashPageUI();

    }

    private void FlashPageUI() {
        for (int i = 0; i <DataStruct.CurMacMode.inputsource.Max ; i++) {
            imageView[i].setVisibility(View.INVISIBLE);
        }
        imageView[getInputSource(inputsourceIndex)].setVisibility(View.VISIBLE);
    }

    private void initView() {
        back_title_itemView=findViewById(R.id.id_title_back);
        back_title_itemView.setTextfeatures(name);
        back_title_itemView.BackClick(this);
        ly_inputsource=findViewById(R.id.ly_inputsource);



        for (int i = 0; i <DataStruct.CurMacMode.inputsource.Max ; i++) {
            ly_inputsource.addView( addView1(i));
        }
        initClick();
    }

    private void initClick() {
        for (int i = 0; i <DataStruct.CurMacMode.inputsource.Max ; i++) {
            linearLayout[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inputsourceIndex=setInputSource((int)v.getTag());
                    FlashPageUI();
                }
            });
        }
        back_title_itemView.btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataStruct.RcvDeviceData.SYS.input_source=inputsourceIndex;
                finish();
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
            }
        });


    }
    @Override
    protected void onStart() {//重写onStart方法
        dataReceiver = new InputSourceReceiver();
        IntentFilter filter = new IntentFilter();//创建IntentFilter对象
        filter.addAction("com.maininputsource.activity");
        registerReceiver(dataReceiver, filter);//注册Broadcast Receiver
        super.onStart();
    }


    ///广播刷新UI
    public class InputSourceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getExtras().get("inputSourceSelect").toString();
            if(msg.equals("inputSourceSelectTrue")){  //刷新通用界面的主音源和混音音源
                FlashPageUI();
            }
        }
    }


    /**添加一个组合控件*/
    private View addView1(int index) {
        // TODO 动态添加布局(xml方式)
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);  //LayoutInflater inflater1=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LayoutInflater inflater3 = LayoutInflater.from(mcontext);
        View view = inflater3.inflate(R.layout.check_text_item, null);
        view.setLayoutParams(lp);

        txt_band=view.findViewById(R.id.id_text);
        imageView[sourceNum]=view.findViewById(R.id.id_img_check);
        img_inputsource=view.findViewById(R.id.id_img_inputsource);

        img_inputsource.setImageResource(image[index]);

        linearLayout[sourceNum]=view.findViewById(R.id.id_ly_check);
        imageView[sourceNum].setImageResource(R.drawable.chs_yes);
        txt_band.setText(DataStruct.CurMacMode.inputsource.Name[index]);

        linearLayout[sourceNum].addView(addView2());
        linearLayout[sourceNum].setTag(index);
        sourceNum++;
        return view;
    }


    /**添加一个组合控件*/
    private View addView2() {
        // TODO 动态添加布局(xml方式)
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LayoutInflater inflater3 = LayoutInflater.from(mcontext);
        View view = inflater3.inflate(R.layout.chs_line, null);
        view.setLayoutParams(lp);
        return view;
    }


    /***
     * 获取音源显示为哪个
     * */
    private int getInputSource(int index){
        int val=0;
        switch (index){
            case 1:
                val= 0;
                break;
            case 3:
                val= 1;
                break;
            case 2:
                val= 2;
                break;

            default:
                val= 1;
                break;
        }
        return val;
    }

    /***
     * 设置音源下发哪个
     * */
    private int setInputSource(int index){
        int val=0;
        switch (index){
            case 0:
                val= 1;
                break;
            case 1:
                val= 3;
                break;
            case 2:
                val= 2;
                break;

        }
        return val;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(dataReceiver);
    }



}
