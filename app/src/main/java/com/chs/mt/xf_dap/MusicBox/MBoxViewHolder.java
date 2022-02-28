package com.chs.mt.xf_dap.MusicBox;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chs.mt.xf_dap.R;

import java.io.Serializable;


@SuppressWarnings("serial")
public class MBoxViewHolder implements Serializable {
	
	public View contentView;
    public ImageView IVHead;
    public Button Img;
    public Button BtnMsg;
    public TextView FileName;
    public TextView Artist;
    public LinearLayout LY;
       
    public MBoxViewHolder(View contentView) {
        this.contentView = contentView;
        IVHead   = (ImageView) contentView.findViewById(R.id.id_iv_head);
        BtnMsg   = (Button) contentView.findViewById(R.id.id_msg);
        Img      = (Button) contentView.findViewById(R.id.id_img);
        FileName = (TextView) contentView.findViewById(R.id.id_filename);
        Artist   = (TextView) contentView.findViewById(R.id.id_art);
        LY       = (LinearLayout) contentView.findViewById(R.id.id_ly);
    }
    
    public View get_contentView(){
    	return this.contentView;
    }

}
