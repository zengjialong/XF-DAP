package com.chs.mt.xf_dap.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chs.mt.xf_dap.R;

import java.util.List;

public class MyListAdapter extends BaseAdapter implements View.OnClickListener {
    private List<String> numList;
    private Context mContext;
    private InnerItemOnclickListener mListener;

    //初始化的时候必须赋值并且设置为哪个Activity
    public MyListAdapter(Context context, List<String> umList) {
        numList = umList;
        mContext = context;
    }

    @Override
    public int getCount() {
        return numList == null ? 0 : numList.size();
    }

    @Override
    public Object getItem(int position) {
        return numList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
           ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.list_item, null);
            holder = new ViewHolder();
            holder.tvNum = (TextView) convertView.findViewById(R.id.tv_list_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvNum.setText(numList.get(position));
        return convertView;
    }

    //ViewHolder通常出现在适配器里，
    // 为的是listview滚动的时候快速设置值，而不必每次都重新创建很多对象
    private class ViewHolder {
        TextView tvNum;
    }

    interface InnerItemOnclickListener {//定义一个接口 用于主页的点击
        void itemClick(View v);
    }
    public void setOnInnerItemOnClickListener(InnerItemOnclickListener listener){
        this.mListener=listener;
    }

    @Override
    public void onClick(View v) {
        mListener.itemClick(v);
    }

}
