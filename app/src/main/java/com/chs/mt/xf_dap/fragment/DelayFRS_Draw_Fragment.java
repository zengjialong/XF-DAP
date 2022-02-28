package com.chs.mt.xf_dap.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chs.mt.xf_dap.R;
import com.chs.mt.xf_dap.datastruct.DataStruct;
import com.chs.mt.xf_dap.datastruct.MacCfg;
import com.chs.mt.xf_dap.tools.DelaySettingFour;
import com.chs.mt.xf_dap.tools.EQ_SeekBar;

import java.util.ArrayList;
import java.util.Arrays;

import android.content.Context;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class DelayFRS_Draw_Fragment extends Fragment {
    private static String javast = "DelayAuto_Fragment";
    private int mIndex = 0;
    private LinearLayout LLyout_SetDelay_Unit;
    private Toast mToast;
    private static Context mContext;
    private int OUT_CH_MAX_CFG = 12;
    private int SPK_MAX = 6;
    private ArrayList<String> DelayUnit_list;
    private LinearLayout[] LLY_SetDelay_BG = new LinearLayout[OUT_CH_MAX_CFG];//延时通道选中
    private EQ_SeekBar[] SB_SetDelay_SeekBar = new EQ_SeekBar[OUT_CH_MAX_CFG];//延时滑动调节
    //private Button B_Inch;    //延时单位设置

    private Button[] B_SetDelay_speeker = new Button[SPK_MAX];//延时值显示

 //   private Button Btn_DelayTypeSpeaker, Btn_DelayTypeDelay;
    //Btn_CenterSet

    private DelaySettingFour ToningDelaySettingFour;
    private LinearLayout LY_DelayTypeBg;
    private Button btn_driver,btn_rear,btn_all;
    private ImageView Img_delay_src;
    /*延时单位切换，1：CM，2：MS，3：Inch*/
    private int DelayUnit = 2;

    //private Button Btn_LinkA, Btn_LinkLR;
    int LinkMode = 0;//不联调，1：左右，2整机除了超低
    int[] DelayVal = new int[16];
	private boolean delay_type=true;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity().getApplicationContext();
        //getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);// 设置默认键盘不弹出


        View view = inflater.inflate(R.layout.chs_fragment_delay_frs_draw, container, false);
//        HideKeyboard(view);
      initView(view);
//
//        initData();
      FlashPageUI();


        return view;
    }

    public static void HideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }


    public void hideIputKeyboard(final Context context) {
        final Activity activity = (Activity) context;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                InputMethodManager mInputKeyBoard = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (activity.getCurrentFocus() != null) {
                    mInputKeyBoard.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                }
            }
        });
    }


    private void initView(View view) {
        AddViewDelaySettingsPage_Auto(view);
    }

    private void initData() {
        //FlashPageUI();
    }


    //////////////////////////////////////////////

    /**
     * 消息提示
     */
    @SuppressWarnings("unused")
    private void ToastMsg(String Msg) {
        if (null != mToast) {
            mToast.setText(Msg);
        } else {
            mToast = Toast.makeText(mContext, Msg, Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    private void AddViewDelaySettingsPage_Auto(View viewDelaySettings) {



        ToningDelaySettingFour = (DelaySettingFour) viewDelaySettings.findViewById(R.id.id_setdelay_four_1);
     //   ToningDelaySettingFour.SeekTo();
        LY_DelayTypeBg = viewDelaySettings.findViewById(R.id.id_ly_delaytype);
        Img_delay_src = viewDelaySettings.findViewById(R.id.id_img_delay_src);



        btn_driver=viewDelaySettings.findViewById(R.id.id_driver);
        btn_rear=viewDelaySettings.findViewById(R.id.id_front);
        btn_all=viewDelaySettings.findViewById(R.id.id_all);


        AddViewDelaySettingsAutoPageListener();
    }




    /*************************************************************************/
    /***************************** 界面延时设置  *******************************/
    /**********************************TODO***********************************/
    /******* 刷新延时单位   *******/

    public void InitLoadPageUI() {

        AddViewDelaySettingsAutoPageListener();
    }


    public void FlashPageUI() {
//        ToningDelaySettingFour.setProgress();
//        spkTypeSet(delay_type);
        ToningDelaySettingFour.setProgress();
        setNormal();
        setPress();
    }


    /********************************  创建设置延时的SeekBar Dialog  *********************************/

    void AddViewDelaySettingsAutoPageListener() {


        btn_driver.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CountChannelDelay(1);
                ToningDelaySettingFour.setProgress();
                setNormal();
                setPress();
            }
        });

        btn_rear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CountChannelDelay(2);
                ToningDelaySettingFour.setProgress();
                setNormal();
                setPress();
            }
        });

        btn_all.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CountChannelDelay(3);
                ToningDelaySettingFour.setCenter();
                setNormal();
                setPress();

            }
        });


            ToningDelaySettingFour.setDelaySettingChangeListener(new DelaySettingFour.OnDelaySettingChangeListener() {
                @Override
                public void onProgressChanged(DelaySettingFour delaySettingFour, int[] progress, boolean fromUser) {
                    for (int i = 0; i < 4; i++) {
                        DataStruct.RcvDeviceData.OUT_CH[i].delay = progress[i];
                        if (DataStruct.RcvDeviceData.OUT_CH[i].delay > DataStruct.CurMacMode.Delay.MAX) {
                            DataStruct.RcvDeviceData.OUT_CH[i].delay = DataStruct.CurMacMode.Delay.MAX;
                        }
                    }

                    setNormal();
                  setPress();
                }
            });
    }

    private void setPress() {

        if(DataStruct.RcvDeviceData.OUT_CH[2].delay>DataStruct.RcvDeviceData.OUT_CH[0].delay||DataStruct.RcvDeviceData.OUT_CH[3].delay>DataStruct.RcvDeviceData.OUT_CH[1].delay){
            btn_rear.setTextColor(getResources().getColor(R.color.white));
            btn_rear.setBackgroundResource(R.drawable.chs_btn_cbm_press);
        }else if(DataStruct.RcvDeviceData.OUT_CH[0].delay>DataStruct.RcvDeviceData.OUT_CH[1].delay&&DataStruct.RcvDeviceData.OUT_CH[0].delay>DataStruct.RcvDeviceData.OUT_CH[2].delay){
            btn_driver.setTextColor(getResources().getColor(R.color.white));
            btn_driver.setBackgroundResource(R.drawable.chs_btn_cbm_press);
        }else if(DataStruct.RcvDeviceData.OUT_CH[0].delay==0&&
                DataStruct.RcvDeviceData.OUT_CH[1].delay==0&&
                DataStruct.RcvDeviceData.OUT_CH[2].delay==0&&
                DataStruct.RcvDeviceData.OUT_CH[3].delay==0){
            btn_all.setBackgroundResource(R.drawable.chs_btn_cbm_press);
            btn_all.setTextColor(getResources().getColor(R.color.white));
        }
    }

    private void setNormal() {
        btn_all.setBackgroundResource(R.drawable.chs_btn_center_normal);
        btn_driver.setBackgroundResource(R.drawable.chs_btn_center_normal);
        btn_rear.setBackgroundResource(R.drawable.chs_btn_center_normal);
    }


    /**
     * 填充数组中的数据用来判断最大值
     * */
    private int FillData(){

        int[] NumData=new int[4];
        for (int i = 0; i <NumData.length ; i++) {
            NumData[i]=DataStruct.RcvDeviceData.OUT_CH[i].delay;
        }
        Arrays.sort(NumData);
        int index=5;
        if(NumData[NumData.length-1]!=0) {
            for (int i = 0; i < NumData.length; i++) {
                if (NumData[NumData.length - 1] == DataStruct.RcvDeviceData.OUT_CH[i].delay) {
                    index = i;
                }
            }
        }
        return index;
    }

    /**
     * 设置延时座位选中
     * */
    private void setDelaySeatSelect(int cindex) {
        switch (cindex) {
            case 0:
                Img_delay_src.setImageDrawable(getResources().getDrawable(R.drawable.chs_delayset_scar_fl));
                break;
            case 1:
                Img_delay_src.setImageDrawable(getResources().getDrawable(R.drawable.chs_delayset_scar_fr));
                break;
            case 2:
                Img_delay_src.setImageDrawable(getResources().getDrawable(R.drawable.chs_delayset_scar_rl));
                break;
            case 3:
                Img_delay_src.setImageDrawable(getResources().getDrawable(R.drawable.chs_delayset_scar_rr));
                break;
            default:
                Img_delay_src.setImageDrawable(getResources().getDrawable(R.drawable.chs_delay_none_car));
                break;
        }
    }


    private void CountChannelDelay(int pos) {
        double PS=1/4;
        double LineOA=Math.sqrt(MacCfg.CCar[0]*MacCfg.CCar[0]
                +MacCfg.CCar[1]*MacCfg.CCar[1]);

        double Line0=LineOA*PS;
        double Line1=Math.sqrt(MacCfg.CCar[0]*(1-PS)*MacCfg.CCar[0]*(1-PS)
                +MacCfg.CCar[1]*PS*MacCfg.CCar[1]*PS);
        double Line2=Math.sqrt(MacCfg.CCar[0]*PS*MacCfg.CCar[0]*PS
                +MacCfg.CCar[1]*(1-PS)*MacCfg.CCar[1]*(1-PS));
        double Line3=LineOA*(1-PS);

        switch (pos) {
            case 1:
                DataStruct.RcvDeviceData.OUT_CH[0].delay=(int) (((Line3-Line0)/0.34)*48);
                DataStruct.RcvDeviceData.OUT_CH[1].delay=(int) (((Line2-Line0)/0.34)*48);
                DataStruct.RcvDeviceData.OUT_CH[2].delay=(int) (((Line1-Line0)/0.34)*48);
                DataStruct.RcvDeviceData.OUT_CH[3].delay=0;
                break;
            case 2:
                DataStruct.RcvDeviceData.OUT_CH[0].delay=0;
                DataStruct.RcvDeviceData.OUT_CH[1].delay=0;
                DataStruct.RcvDeviceData.OUT_CH[2].delay=(int) (((Line1-Line0)/0.34)*48);
                DataStruct.RcvDeviceData.OUT_CH[3].delay=(int) (((Line1-Line0)/0.34)*48);
                break;
            case 3:
                DataStruct.RcvDeviceData.OUT_CH[0].delay=0;
                DataStruct.RcvDeviceData.OUT_CH[1].delay=0;
                DataStruct.RcvDeviceData.OUT_CH[2].delay=0;
                DataStruct.RcvDeviceData.OUT_CH[3].delay=0;
                break;
            default:
                break;
        }
    }

}