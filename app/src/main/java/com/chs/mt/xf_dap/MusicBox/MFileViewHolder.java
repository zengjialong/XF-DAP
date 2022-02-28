package com.chs.mt.xf_dap.MusicBox;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.chs.mt.xf_dap.R;

import java.io.Serializable;


@SuppressWarnings("serial")
public class MFileViewHolder implements Serializable {

	public View contentView;
    public ImageView IVHead;
    public Button Img;
    public Button BtnMsg;
    public TextView FileName;
    public TextView Total;
    public LinearLayout LY;

    public MFileViewHolder(View contentView) {
        this.contentView = contentView;
        IVHead   = (ImageView) contentView.findViewById(R.id.id_iv_file_head);
        BtnMsg   = (Button) contentView.findViewById(R.id.id_file_msg);
        Img      = (Button) contentView.findViewById(R.id.id_file_img);
        FileName = (TextView) contentView.findViewById(R.id.id_file_filename);
        Total    = (TextView) contentView.findViewById(R.id.id_file_art);
        LY       = (LinearLayout) contentView.findViewById(R.id.id_file_ly);
    }
    
    public View get_contentView(){
    	return this.contentView;
    }

}
