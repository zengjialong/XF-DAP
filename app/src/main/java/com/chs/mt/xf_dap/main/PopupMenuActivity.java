package com.chs.mt.xf_dap.main;

/**
 * Created by Administrator on 2017/6/14.
 */

        import android.app.Activity;
        import android.content.Context;
        import android.graphics.drawable.ColorDrawable;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.widget.LinearLayout.LayoutParams;
        import android.widget.PopupWindow;

        import com.chs.mt.xf_dap.R;
        import com.chs.mt.xf_dap.datastruct.MacCfg;

public class PopupMenuActivity extends PopupWindow implements OnClickListener {

    private Activity activity;
    private View popView;
    private int MaxItem=8;
    private View[] VItem  = new View[MaxItem];
    private OnItemClickListener onItemClickListener;
    private View v_line;

    public PopupMenuActivity(Activity activity) {
        super(activity);
        this.activity = activity;
        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popView = inflater.inflate(R.layout.chs_layout_popupmenu, null);// 加载菜单布局文件
        this.setContentView(popView);// 把布局文件添加到popupwindow中
        this.setWidth(dip2px(activity, 130));// 设置菜单的宽度（需要和菜单于右边距的距离搭配，可以自己调到合适的位置）
        this.setHeight(LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);// 获取焦点
        this.setTouchable(true); // 设置PopupWindow可触摸
        this.setOutsideTouchable(true); // 设置非PopupWindow区
        // 域可触摸
        ColorDrawable dw = new ColorDrawable(0x00000000);
        this.setBackgroundDrawable(dw);

        // 获取选项卡
        VItem[0] = popView.findViewById(R.id.ly_item1);
        VItem[1]  = popView.findViewById(R.id.ly_item2);
        VItem[2]  = popView.findViewById(R.id.ly_item6);
        VItem[3]  = popView.findViewById(R.id.ly_item4);
        VItem[4]  = popView.findViewById(R.id.ly_item5);
        VItem[5]  = popView.findViewById(R.id.ly_item3);
        VItem[6]  = popView.findViewById(R.id.ly_item7);
        VItem[7]  = popView.findViewById(R.id.ly_item8);

        v_line=popView.findViewById(R.id.fifity_line);

        if(MacCfg.Mcu==7){
            VItem[2].setVisibility(View.VISIBLE);
            VItem[5].setVisibility(View.GONE);
        }else if(MacCfg.Mcu==8){
            VItem[2].setVisibility(View.GONE);
            VItem[5].setVisibility(View.GONE);
        }else{
            VItem[2].setVisibility(View.GONE);
            VItem[5].setVisibility(View.GONE);
        }

        // 添加监听
        for(int i=0;i<MaxItem;i++){
            VItem[i].setTag(i);
            VItem[i].setOnClickListener(this);
        }
    }


    /**
     * 设置显示的位置
     *
     * @param resourId
     *            这里的x,y值自己调整可以
     */
    public void showLocation(int resourId) {
        showAsDropDown(activity.findViewById(resourId), dip2px(activity, 0),
                dip2px(activity, -8));
    }

    @Override
    public void onClick(View v) {
        int item = (Integer) v.getTag();

        if (onItemClickListener != null) {
            onItemClickListener.onClick(item, "");
        }
        dismiss();
    }

    // dip转换为px
    public int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    // 点击监听接口
    public interface OnItemClickListener {
        public void onClick(int item, String str);
    }

    // 设置监听
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}