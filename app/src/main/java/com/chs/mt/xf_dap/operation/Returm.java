package com.chs.mt.xf_dap.operation;

import android.content.Context;
import android.widget.Toast;

import com.chs.mt.xf_dap.datastruct.DataStruct;

import java.io.UnsupportedEncodingException;

public class Returm {


    public static void ToastMsg(String Msg,Context mContext,Toast mToast) {
        if (null != mToast) {
            mToast.setText(Msg);
        } else {
            mToast = Toast.makeText(mContext, Msg, Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    public static  int lastOutchannel(int index){
        if(--index<0){
            index=0;
        }
        return index;
    }
    public static  int nextOutchannel(int index){
        if(++index>DataStruct.CurMacMode.Out.OUT_CH_MAX_USE-1){
            index=DataStruct.CurMacMode.Out.OUT_CH_MAX_USE-1;
        }
        return index;
    }



    /**
     * 用于检查接收到的音效组是否为空
     */
    public static boolean checkUserGroupByteNull(int[] ug) {
        for (int i = 0; i < 15; i++) {
            if (ug[i] != 0x00) {
                return true;
            }

        }
        return false;
    }

    public static String getGBKString(int[] nameC) {
        byte[] GBK = new byte[16];
        for (int j = 0; j < 16; j++) {
            GBK[j] = 0x00;
        }
        int n = 0;
        String uNameString = null;
        for (int j = 0; j < 16; j++) {
            if (nameC[j] != 0x00) {
                GBK[j] = (byte) nameC[j];
                ++n;
            }
        }
        try {
            byte[] GBKN = new byte[n];
            for (int j = 0; j < n; j++) {
                GBKN[j] = GBK[j];
            }
            uNameString = new String(GBKN, "GBK");


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return uNameString;
    }

}
