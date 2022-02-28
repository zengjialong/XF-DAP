package com.chs.mt.xf_dap.operation;

import com.chs.mt.xf_dap.datastruct.DataStruct;
import com.chs.mt.xf_dap.datastruct.MacCfg;

/**使用的一些公用函数*/
public class ReturnFuncation {


    /**
     * 设置进度条的音量
     * */
    public static void setProcessVal(int val){
        switch (DataStruct.RcvDeviceData.SYS.input_source) {
            case 0:
                DataStruct.RcvDeviceData.SYS.device_mode = val;
                break;
            case 1:
                DataStruct.RcvDeviceData.SYS.hi_mode = val;
                break;
            case 2:
                DataStruct.RcvDeviceData.SYS.blue_gain = val;
                break;
            case 3:
                DataStruct.RcvDeviceData.SYS.aux_gain = val;
                break;
            case 5:
                DataStruct.RcvDeviceData.SYS.none5 = val;
                break;
            default:
                break;
        }
    }

    /**获取主音量的值*/
    public static  int getMainVol() {
        int val = 0;
        switch (DataStruct.RcvDeviceData.SYS.input_source) {
            case 0:
                val = DataStruct.RcvDeviceData.SYS.device_mode;
                break;
            case 1:
                val = DataStruct.RcvDeviceData.SYS.hi_mode;
                break;
            case 2:
                val = DataStruct.RcvDeviceData.SYS.blue_gain;
                break;
            case 3:
                val = DataStruct.RcvDeviceData.SYS.aux_gain;
                break;
            case 5:
                val = DataStruct.RcvDeviceData.SYS.none5;
                break;
        }
        return val;
    }

    /**得到当前的通道*/
    public static  int CurrentOutputChannel(){
        if(MacCfg.Mcu==3){
            if (DataStruct.RcvDeviceData.SYS.mode == 1 && MacCfg.OutputChannelSel == 1) {
                MacCfg.OutputChannelSel = 0;
            }
        }
        return MacCfg.OutputChannelSel;
    }

    //限制斜率  flag==true 高通  ==false 低通
    public static void setFreq(boolean flag,int h_freq){
        if(flag) {
            if (DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].LP_OFF_Flg == 1) {
                if (h_freq > DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].LP_OFF_Freq) {
                    DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].h_freq = DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].LP_OFF_Freq;
                } else {
                    if(h_freq>DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].l_freq){
                        DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].h_freq =DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].l_freq;
                    }else{
                        DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].h_freq = h_freq;
                    }

                }
            } else {
                if(h_freq>DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].l_freq){
                    DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].h_freq =DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].l_freq;
                }else{
                    DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].h_freq = h_freq;
                }
            }
        }else{
            if(DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].HP_OFF_Flg==1){
                if(h_freq<DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].HP_OFF_Freq){
                    DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].l_freq=DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].HP_OFF_Freq;
                }else{
                    if(h_freq<DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].h_freq){
                        DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].l_freq =DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].h_freq;
                    }else{
                        DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].l_freq = h_freq;
                    }
                }
            }else{
                if(h_freq<DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].h_freq){
                    DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].l_freq =DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].h_freq;
                }else{
                    DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].l_freq = h_freq;
                }
            }
        }
    }


}
