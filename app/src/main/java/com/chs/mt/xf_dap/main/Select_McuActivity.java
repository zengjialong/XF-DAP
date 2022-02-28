package com.chs.mt.xf_dap.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.chs.mt.xf_dap.R;
import com.chs.mt.xf_dap.adapter.MyListAdapter;

import java.util.ArrayList;
import java.util.List;

public class Select_McuActivity extends Activity {
    private Boolean isDown=false;//判断弹窗是否显示
    private Button btn_300ch;
    private Button btn_sure;
    private ListView listView;
    List<String> list = new ArrayList<String>() {//这个大括号 就相当于我们  new 接口
        {//这个大括号 就是 构造代码块 会在构造函数前 调用
            add("80-ACH");//this 可以省略  这里加上 只是为了让读者 更容易理解
            add("100-ACH");
            add("300-ACH");
            add("500-ACH");
        }
    };



    private Context mContext;
    private PopupWindow popWin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_select);
        mContext=this;
        btn_300ch= (Button) findViewById(R.id.id_300ch);
        btn_sure=findViewById(R.id.id_sure);
        initListView();
        
        //val num="11";

        btn_300ch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //定义一个popupWindow
                popWin=new PopupWindow(mContext);
                popWin.setWidth(btn_300ch.getWidth());//设置宽度 和编辑框的宽度相同
                popWin.setHeight(800); //设置高度
                //为popWin填充内容
                popWin.setContentView(listView);
                //点击popWin区域之外 自动关闭popWin
                popWin.setOutsideTouchable(true);
                if (popWin != null) {//要先让popupwindow获得焦点，才能正确获取popupwindow的状态
                    popWin.setFocusable(true);
                }
                if(!popWin.isShowing()){
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            btn_300ch.setText(list.get(position));
                            popWin.dismiss();
                        }
                    });
                    /**
                     * 设置弹出窗口显示的位置
                     * 参数一:相对于参数的位置进行显示 即在编辑框的下面显示
                     * 参数二 三:x y轴的偏移量
                     */
                    popWin.showAsDropDown(btn_300ch, 0, 0);
                }else{
                    popWin.dismiss();
                }
            }
        });
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mContext, MainTURTActivity.class);
                mContext.startActivity(intent);
            }
        });
    }

    ///*初始化*/
    private void initListView() {
        listView = new ListView(this);
        //设置listView的背景
        listView.setBackgroundResource(R.drawable.mcu_select_bg);
        //设置条目之间的分割线及滚动条不可见
        listView.setDivider(null);
        listView.setVerticalScrollBarEnabled(false);
        //设置适配器
        listView.setAdapter(new MyListAdapter(mContext, list));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //返回true，以防止此事件被进一步传播。
            //返回false，表示还没有处理完这个事件，它应该继续传播到其他监听。
             finish();

            return true;
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }
}