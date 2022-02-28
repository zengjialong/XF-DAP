package com.chs.mt.xf_dap.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;

import com.chs.mt.xf_dap.R;
import com.chs.mt.xf_dap.tools.MHS_SeekBar;

import java.util.ArrayList;
import java.util.List;


public class OutputViewPageFragment extends Fragment {
    private Context mcontext;
    private int mCurrentPosition = 0;
    // 滚动条初始偏移量
    private int offset = 0;
    // 当前页编号
    private int currIndex = 0;
    // 滚动条宽度
    private int bmpW;
    //一倍滚动量
    private int one;
    private Spinner spinner;
    private ArrayAdapter adapter;
    private Button btn_output_select, btn_output_name;
    private Button btn_polar, btn_output_val;
    private Button btn_spk_type, btn_hp_fiter, btn_hp_freq, btn_hp_oct, btn_lp_fiter, btn_lp_freq, btn_lp_oct, btn_reset_va, btn_b_link, btn_mute;
    private LinearLayout ly_mute;
    private MHS_SeekBar output_val;
    private ListView mListView;
    private PopupWindow popLeft;

    private List<String> aList=new ArrayList<String>(){
        {
           add("CH1");
            add("CH2");
            add("CH3");
            add("CH4");
            add("CH5");
            add("CH6");
            add("CH7");
            add("CH8");
            add("CH9");
            add("CH10");
            add("CH11");
            add("CH12");
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mcontext = getActivity().getApplicationContext();
        View view = inflater.inflate(R.layout.chs_fragment_output_xover, container, false);

        return view;
    }


}
