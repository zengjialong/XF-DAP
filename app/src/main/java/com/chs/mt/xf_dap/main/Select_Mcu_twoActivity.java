package com.chs.mt.xf_dap.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.chs.mt.xf_dap.R;
import com.chs.mt.xf_dap.datastruct.MacCfg;

import java.util.ArrayList;
import java.util.List;

public class Select_Mcu_twoActivity extends Activity {
    private Boolean isDown = false;//判断弹窗是否显示
    private Button btn_300ch;
    private ListView listView;
    private LinearLayout ly_mcu_type;
    List<String> list = new ArrayList<String>() {//这个大括号 就相当于我们  new 接口
        {//这个大括号 就是 构造代码块 会在构造函数前 调用

            add("DEQ-060ACH");   //0   //this 可以省略  这里加上 只是为了让读者 更容易理解
            add("DEQ-200ACH"); //1
            add("DEQ-600ACH");//2
        }
    };
    /**
     * 用来保存每个的Tag值
     * */
    List<Integer> listTag = new ArrayList<Integer>() {//存储所有的Tag值
        {
            add(0);   //0   //this 可以省略  这里加上 只是为了让读者 更容易理解
            add(1); //1
            add(2); //2


        }
    };

    //定义一个初始长度为0的数组，用来缓存数据
    private Button[] btn_mcu_type = new Button[0];

    private int mcu_index = 0;

    private Context mContext;
    private PopupWindow popWin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_select_two);
        mContext = this;
        ly_mcu_type = (LinearLayout) findViewById(R.id.id_ly_mcu_type);
        btn_mcu_type = new Button[list.size() + 1];

//        btn_mcu_type[0]=findViewById(R.id.id_80ch);
//        btn_mcu_type[1]=findViewById(R.id.id_100ch);
//        btn_mcu_type[2]=findViewById(R.id.id_101ch);
//        btn_mcu_type[3]=findViewById(R.id.id_300ch);
//        btn_mcu_type[4]=findViewById(R.id.id_500ch);
//        btn_mcu_type[5]=findViewById(R.id.id_501ch);
//        btn_mcu_type[6]=findViewById(R.id.id_250ch);

        for (int i = 0; i < list.size(); i++) {
            ly_mcu_type.addView(addView(i, listTag.get(i)));
        }

        //val num="11";
//        for (int i = 0; i <btn_mcu_type.length ; i++) {
//            btn_mcu_type[i].setTag(i);
//            btn_mcu_type[i].setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                int index=(int)v.getTag();
//                MacCfg.Mcu=index;
//                Intent intent = new Intent();
//                intent.setClass(mContext, MainTURTActivity.class);//MainTURTActivity
//                mContext.startActivity(intent);
//                }
//            });
//        }
    }

    private View addView(final int index, int Tag) {
        View mGridView = View.inflate(this, R.layout.mcu_type_item, null);
        btn_mcu_type[index] = mGridView.findViewById(R.id.id_300ch);
        btn_mcu_type[index].setTag(Tag);

        btn_mcu_type[index].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = (int) v.getTag();
                MacCfg.bool_OutChLink = false;
                MacCfg.Mcu = index;
                Intent intent = new Intent();
                intent.setClass(mContext, MainTURTActivity.class);//MainTURTActivity
                mContext.startActivity(intent);
            }
        });
        btn_mcu_type[mcu_index].setText(String.valueOf(list.get(index)));
        mcu_index++;


        return mGridView;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //返回true，以防止此事件被进一步传播。
            //返回false，表示还没有处理完这个事件，它应该继续传播到其他监听。
            finish();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}