package com.chs.mt.xf_dap.MusicBox;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.chs.mt.xf_dap.R;

import java.util.ArrayList;
import java.util.List;

public class MBoxItemAdapter extends BaseAdapter {
    private int oldid = -1;
    private setMBoxAdpterOnItemClick myAdpterOnclick;
    private final List<MMListInfo> dataSource = new ArrayList<MMListInfo>();
    private LayoutInflater mLayoutInflater = null;
    private Context mContext;
    public MBoxItemAdapter(Context cxt, List<MMListInfo> dataSource) {
        this.dataSource.addAll(dataSource);
        mLayoutInflater = LayoutInflater.from(cxt);
        mContext = cxt;
    }
    @Override
    public int getCount() {

        return dataSource.size();
    }

    @Override
    public Object getItem(int position) {
        return dataSource.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }
    public void setOnMBoxAdpterOnItemClick(setMBoxAdpterOnItemClick l) {
        myAdpterOnclick = l;
    }
    public interface setMBoxAdpterOnItemClick{
        public void onAdpterClick(int which, int postion, View v);
    }

    @SuppressLint("NewApi") @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MBoxViewHolder mBoxViewHolder = null;

        if (convertView == null
                || !MBoxViewHolder.class.isInstance(convertView.getTag())) {
            convertView = mLayoutInflater.inflate(R.layout.chs_listview_musicbox_item,
                    null);
            mBoxViewHolder = new MBoxViewHolder(convertView);
            convertView.setTag(mBoxViewHolder);
        } else {
            mBoxViewHolder = (MBoxViewHolder) convertView.getTag();
        }
        MMListInfo lisdata = dataSource.get(position);
        mBoxViewHolder.FileName.setText(lisdata.FileName);
        mBoxViewHolder.Artist.setText(lisdata.ID3_Artist);
        int cheight  = mContext.getResources().getDimensionPixelSize(R.dimen.Music_Musicitem_ly_sel_height);
        int oheight  = mContext.getResources().getDimensionPixelSize(R.dimen.Music_Musicitem_ly_height);

        mBoxViewHolder.Img.setVisibility(View.GONE);
        mBoxViewHolder.IVHead.setVisibility(View.GONE);

        if(lisdata.boolSel){
            mBoxViewHolder.Img.setVisibility(View.GONE);
            mBoxViewHolder.IVHead.setVisibility(View.VISIBLE);
//            mBoxViewHolder.Img.setBackgroundResource(R.drawable.chs_music_list_player_press);
            mBoxViewHolder.LY.setBackgroundResource(R.drawable.chs_musiclistitem_press);
            //改变高度
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, cheight);
            mBoxViewHolder.LY.setLayoutParams(params);
            //改变图片大小
//            LinearLayout.LayoutParams paramsIV = new LinearLayout.LayoutParams(cheight, cheight);
//            mBoxViewHolder.IVHead.setLayoutParams(paramsIV);

            //列表没有选中是一行，选中是两行，选中后FileName字体大些
//            mBoxViewHolder.FileName.setTextSize(18);
            mBoxViewHolder.Artist.setVisibility(View.VISIBLE);
//            if(lisdata.ID != oldid){
//                oldid = lisdata.ID;
//                AnimationUtil.AnimScaleI(mContext,convertView);//convertView mBoxViewHolder.IVHead
//            }

        }else {
            //改变高度
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, oheight);
            mBoxViewHolder.LY.setLayoutParams(params);
            //改变图片大小
//            LinearLayout.LayoutParams paramsIV = new LinearLayout.LayoutParams(oheight, oheight);
//            mBoxViewHolder.IVHead.setLayoutParams(paramsIV);

            mBoxViewHolder.Img.setVisibility(View.VISIBLE);
            mBoxViewHolder.IVHead.setVisibility(View.GONE);
//            mBoxViewHolder.Img.setBackgroundResource(R.drawable.chs_music_list_player_normal);
            mBoxViewHolder.LY.setBackgroundResource(R.drawable.chs_musiclistitem_normal);
            //列表没有选中是一行，选中是两行，选中后FileName字体大些
            mBoxViewHolder.FileName.setTextSize(15);
            mBoxViewHolder.Artist.setVisibility(View.GONE);
        }
    //    mBoxViewHolder.LY.setBackgroundResource(R.color.red);

        final int fpostion = position;
        mBoxViewHolder.BtnMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myAdpterOnclick != null) {

                    int which = v.getId();
                    myAdpterOnclick.onAdpterClick(which, fpostion,v);
                }
            }
        });
        mBoxViewHolder.LY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myAdpterOnclick != null) {

                    int which = v.getId();
                    myAdpterOnclick.onAdpterClick(which, fpostion,v);
                }
            }
        });

        return convertView;
    }

}
