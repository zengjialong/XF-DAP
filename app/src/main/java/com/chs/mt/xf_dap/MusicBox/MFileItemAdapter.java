package com.chs.mt.xf_dap.MusicBox;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import com.chs.mt.xf_dap.R;

import java.util.ArrayList;
import java.util.List;


public class MFileItemAdapter extends BaseAdapter {
	private setMFileAdpterOnItemClick myAdpterOnclick;
    private final List<MFListInfo> dataSource = new ArrayList<MFListInfo>();
    private LayoutInflater mLayoutInflater = null;
    private Context mContext;
    public MFileItemAdapter(Context cxt, List<MFListInfo> dataSource) {
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
    public void setOnMFileAdpterOnItemClick(setMFileAdpterOnItemClick l) {
    	myAdpterOnclick = l;
    }
    public interface setMFileAdpterOnItemClick{
    	public void onAdpterClick(int which, int postion, View v);
    }

    @SuppressLint("NewApi") @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MFileViewHolder mMFileViewHolder = null;

        if (convertView == null
                || !MFileViewHolder.class.isInstance(convertView.getTag())) {
            convertView = mLayoutInflater.inflate(R.layout.chs_listview_musicfile_item,
                    null);
            mMFileViewHolder = new MFileViewHolder(convertView);
            convertView.setTag(mMFileViewHolder);
        } else {
            mMFileViewHolder = (MFileViewHolder) convertView.getTag();
        }
        MFListInfo lisdata = dataSource.get(position);


        mMFileViewHolder.LY.setBackgroundResource(R.drawable.chs_musiclistitem_normal);


        if(lisdata.ListType == MDef.ListType_File){//显示文件信息
            mMFileViewHolder.FileName.setText(lisdata.FileName);
            mMFileViewHolder.Total.setText(String.valueOf(lisdata.TotalSongs)+" "+mContext.getString(R.string.Songs));
            if(lisdata.sel){
                mMFileViewHolder.LY.setBackgroundResource(R.drawable.chs_musiclistitem_press);
            }else {
                mMFileViewHolder.LY.setBackgroundResource(R.drawable.chs_musiclistitem_normal);
            }
            mMFileViewHolder.IVHead.setBackgroundResource(R.drawable.icon_file);
        }else if(lisdata.ListType == MDef.ListType_Music){//显示歌曲信息
            String title = lisdata.MName;
            if((title == null)||(title.equals(""))){
                title = mContext.getString(R.string.unknownSong);
            }
            String Artist = lisdata.MArtist;
            if((Artist == null)||(Artist.equals(""))){
                Artist = mContext.getString(R.string.unknownArtist);
            }
            mMFileViewHolder.FileName.setText(title);
            mMFileViewHolder.Total.setText(Artist);


            if(lisdata.sel){
                mMFileViewHolder.LY.setBackgroundResource(R.drawable.chs_musiclistitem_press);
                mMFileViewHolder.IVHead.setBackgroundResource(R.drawable.chs_music_list_player_default);
            }else {
                mMFileViewHolder.LY.setBackgroundResource(R.drawable.chs_musiclistitem_normal);
                mMFileViewHolder.IVHead.setBackgroundResource(R.drawable.chs_music_list_player_normal);
            }
        }


        final int fpostion = position;
        mMFileViewHolder.BtnMsg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (myAdpterOnclick != null) {
					int which = v.getId();
					myAdpterOnclick.onAdpterClick(which, fpostion,v);
				}
			}
		});
        mMFileViewHolder.LY.setOnClickListener(new View.OnClickListener() {
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
