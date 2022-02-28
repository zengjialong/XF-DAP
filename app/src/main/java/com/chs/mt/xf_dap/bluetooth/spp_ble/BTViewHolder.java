package com.chs.mt.xf_dap.bluetooth.spp_ble;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chs.mt.xf_dap.R;

import java.io.Serializable;


@SuppressWarnings("serial")
public class BTViewHolder implements Serializable {

	public View contentView;
    public TextView ID,Name;
    public LinearLayout LY,LYAutoC;
    public TextView TVT;


    public BTViewHolder(View contentView) {
        this.contentView = contentView;
        Name = (TextView) contentView.findViewById(R.id.id_name);
        ID   = (TextView) contentView.findViewById(R.id.id_id);
        LY   = (LinearLayout) contentView.findViewById(R.id.id_ly);
        LYAutoC   = (LinearLayout) contentView.findViewById(R.id.id_ly_tvt);
        TVT   = (TextView) contentView.findViewById(R.id.id_tvt);
    }
    
    public View get_contentView(){
    	return this.contentView;
    }

}
