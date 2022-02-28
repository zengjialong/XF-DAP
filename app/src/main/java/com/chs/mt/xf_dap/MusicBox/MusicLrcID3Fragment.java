package com.chs.mt.xf_dap.MusicBox;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chs.mt.xf_dap.R;
import com.chs.mt.xf_dap.datastruct.DataStruct;


/**
 * A simple {@link Fragment} subclass.
 */
public class MusicLrcID3Fragment extends Fragment {

    private Context mContext;
    /*******************************************************************/
    private TextView TVArtist,TVAlbum,TVYear;
    private ImageView IVArtist,IVAlbum,IVYear;
    /*******************************************************************/
    public MusicLrcID3Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.chs_fragment_music_id3, container, false);
        mContext = getActivity().getApplicationContext();
        initView(view);
        initClick();
        FlashPageUI();
        return view;
    }

    //刷新页面UI
    public void FlashPageUI(){
        flashID3Info();
    }

    private void initView(View view){
        TVArtist = (TextView)  view.findViewById(R.id.id_tv_artist);
        TVAlbum  = (TextView)  view.findViewById(R.id.id_tv_album);
        TVYear   = (TextView)  view.findViewById(R.id.id_tv_data);
        IVArtist = (ImageView) view.findViewById(R.id.id_b_art);
        IVAlbum  = (ImageView) view.findViewById(R.id.id_b_album);
        IVYear   = (ImageView) view.findViewById(R.id.id_b_data);
    }

    private void initClick(){

    }

    public void flashID3Info(){
        if(DataStruct.CurMusic.ID3_Artist.equals("")){
            TVArtist.setText(getString(R.string.unknown));
        }else {
            TVArtist.setText(DataStruct.CurMusic.ID3_Artist);
        }

        if(DataStruct.CurMusic.ID3_Album.equals("")){
            TVAlbum.setText(getString(R.string.unknown));
        }else {
            TVAlbum.setText(DataStruct.CurMusic.ID3_Album);
        }

        if(DataStruct.CurMusic.ID3_Year.equals("")){
            TVYear.setText(getString(R.string.unknown));
        }else {
            TVYear.setText(DataStruct.CurMusic.ID3_Year);
        }
    }
}
