package com.chs.mt.xf_dap.MusicBox;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


import com.chs.mt.xf_dap.R;
import com.chs.mt.xf_dap.datastruct.DataStruct;
import com.chs.mt.xf_dap.datastruct.Define;
import com.chs.mt.xf_dap.datastruct.MacCfg;
import com.chs.mt.xf_dap.operation.DataOptUtil;
import com.chs.mt.xf_dap.service.ServiceOfCom;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * Created by Administrator on 2017/10/25.
 */

public class MDOptUtil {


    public static final int NO = 0;  // 定义静态变量
    public static final int YES = 1; // 定义静态变量
    public static boolean DEBUG = false;
    private static Toast mToast;

    /**
     * 显示相关信息
     * @param Msg
     * @param mContext
     */
    public static void ToastMsg(String Msg, Context mContext) {
        if (null != mToast) {
            mToast.setText(Msg);
        } else {
            mToast = Toast.makeText(mContext,Msg, Toast.LENGTH_LONG);
        }
        mToast.show();
    }

    /**
     * 切换到下一首
     */
    public static void setMusicNext(boolean send){
        DataStruct.SendMBoxData.CHAN       = MDef.CHAN_SPP;
        DataStruct.SendMBoxData.SEG        = MDef.SEG_NULL;
        DataStruct.SendMBoxData.ACK        = MDef.ACK_NULL;
        DataStruct.SendMBoxData.CMD_TYPE   = MDef.CMDTYPE_C;
        DataStruct.SendMBoxData.CMD_ID     = MDef.CMDID_C_Next;
        DataStruct.SendMBoxData.CMD_LENGTH = 0x10;
        DataStruct.SendMBoxData.PARAM1     = 0x00;
        DataStruct.SendMBoxData.PARAM2     = 0x00;

        MSendDataToDevice(send);
    }
    /**
     * 切换到上一首
     */
    public static void setMusicPrev(boolean send){
        DataStruct.SendMBoxData.CHAN       = MDef.CHAN_SPP;
        DataStruct.SendMBoxData.SEG        = MDef.SEG_NULL;
        DataStruct.SendMBoxData.ACK        = MDef.ACK_NULL;
        DataStruct.SendMBoxData.CMD_TYPE   = MDef.CMDTYPE_C;
        DataStruct.SendMBoxData.CMD_ID     = MDef.CMDID_C_pre;
        DataStruct.SendMBoxData.CMD_LENGTH = 0x10;
        DataStruct.SendMBoxData.PARAM1     = 0x00;
        DataStruct.SendMBoxData.PARAM2     = 0x00;

        MSendDataToDevice(send);
    }
    /**
     * 暂停
     */
    public static void setMusicPause(boolean send){
        DataStruct.SendMBoxData.CHAN       = MDef.CHAN_SPP;
        DataStruct.SendMBoxData.SEG        = MDef.SEG_NULL;
        DataStruct.SendMBoxData.ACK        = MDef.ACK_NULL;
        DataStruct.SendMBoxData.CMD_TYPE   = MDef.CMDTYPE_C;
        DataStruct.SendMBoxData.CMD_ID     = MDef.CMDID_C_Stop;
        DataStruct.SendMBoxData.CMD_LENGTH = 0x10;
        DataStruct.SendMBoxData.PARAM1     = 0x00;
        DataStruct.SendMBoxData.PARAM2     = 0x00;
        MDef.BoolHaveManualPause = true;
        MSendDataToDevice(send);
    }
    /**
     * 播放
     */
    public static void setMusicPlay(boolean send){
        DataStruct.SendMBoxData.CHAN       = MDef.CHAN_SPP;
        DataStruct.SendMBoxData.SEG        = MDef.SEG_NULL;
        DataStruct.SendMBoxData.ACK        = MDef.ACK_NULL;
        DataStruct.SendMBoxData.CMD_TYPE   = MDef.CMDTYPE_C;
        DataStruct.SendMBoxData.CMD_ID     = MDef.CMDID_C_Play;
        DataStruct.SendMBoxData.CMD_LENGTH = 0x10;
        DataStruct.SendMBoxData.PARAM1     = 0x00;
        DataStruct.SendMBoxData.PARAM2     = 0x00;
        MDef.BoolHaveManualPause = false;
        MSendDataToDevice(send);
    }
    /**
     * 设置音量 <01fe0000 53001000 01000000 1f000000>
     */
    public static void setMusicVolume(int volume,boolean send){
        DataStruct.SendMBoxData.CHAN       = MDef.CHAN_SPP;
        DataStruct.SendMBoxData.SEG        = MDef.SEG_NULL;
        DataStruct.SendMBoxData.ACK        = MDef.ACK_NULL;
        DataStruct.SendMBoxData.CMD_TYPE   = MDef.CMDTYPE_S;
        DataStruct.SendMBoxData.CMD_ID     = MDef.CMDID_S_SystemData;
        DataStruct.SendMBoxData.CMD_LENGTH = 0x10;
        DataStruct.SendMBoxData.PARAM1     = 0x01;
        DataStruct.SendMBoxData.PARAM2     = volume & 0xff;

        MSendDataToDevice(send);
    }
    /**
     * 播放模式
     */
    public static void setMusicPlayMode(int Mode,boolean send){
        DataStruct.SendMBoxData.CHAN       = MDef.CHAN_SPP;
        DataStruct.SendMBoxData.SEG        = MDef.SEG_NULL;
        DataStruct.SendMBoxData.ACK        = MDef.ACK_NULL;
        DataStruct.SendMBoxData.CMD_TYPE   = MDef.CMDTYPE_S;
        DataStruct.SendMBoxData.CMD_ID     = MDef.CMDID_S_MusicData;
        DataStruct.SendMBoxData.CMD_LENGTH = 0x10;
        DataStruct.SendMBoxData.PARAM1     = 0x00;
        DataStruct.SendMBoxData.PARAM2     = Mode;

        MSendDataToDevice(send);
    }

    /**
     * 切换UHost播放
     */
    public static void setUHostPlayModeAPP(boolean send){
        DataStruct.SendMBoxData.CHAN       = MDef.CHAN_SPP;
        DataStruct.SendMBoxData.SEG        = MDef.SEG_NULL;
        DataStruct.SendMBoxData.ACK        = MDef.ACK_NULL;
        DataStruct.SendMBoxData.CMD_TYPE   = MDef.CMDTYPE_P;
        DataStruct.SendMBoxData.CMD_ID     = MDef.CMDID_P_UHost;
        DataStruct.SendMBoxData.CMD_LENGTH = 0x10;
        DataStruct.SendMBoxData.PARAM1     = 0x00;
        DataStruct.SendMBoxData.PARAM2     = 0x00;

        MSendDataToDevice(send);
    }

    /**
     * 切换推歌播放
     */
    public static void setMusicPlayModeAPP(boolean send){
        DataStruct.SendMBoxData.CHAN       = MDef.CHAN_SPP;
        DataStruct.SendMBoxData.SEG        = MDef.SEG_NULL;
        DataStruct.SendMBoxData.ACK        = MDef.ACK_NULL;
        DataStruct.SendMBoxData.CMD_TYPE   = MDef.CMDTYPE_P;
        DataStruct.SendMBoxData.CMD_ID     = MDef.CMDID_P_PushSong;
        DataStruct.SendMBoxData.CMD_LENGTH = 0x10;
        DataStruct.SendMBoxData.PARAM1     = 0x00;
        DataStruct.SendMBoxData.PARAM2     = 0x00;

        MSendDataToDevice(send);
    }

    /**
     * 切换推歌播放
     */
    public static void setMusicNumPlay(int numPlay,boolean send){
        DataStruct.SendMBoxData.CHAN       = MDef.CHAN_SPP;
        DataStruct.SendMBoxData.SEG        = MDef.SEG_NULL;
        DataStruct.SendMBoxData.ACK        = MDef.ACK_NULL;
        DataStruct.SendMBoxData.CMD_TYPE   = MDef.CMDTYPE_S;
        DataStruct.SendMBoxData.CMD_ID     = MDef.CMDID_S_MusicNumPlay;
        DataStruct.SendMBoxData.CMD_LENGTH = 0x10;
        DataStruct.SendMBoxData.PARAM1     = numPlay;
        DataStruct.SendMBoxData.PARAM2     = 0x00;
        MSendDataToDevice(send);
    }
    //设置推送和U盘播放控制状态
    public static void setPUMode(int mode,boolean send){
        DataStruct.SendMBoxData.CHAN       = MDef.CHAN_SPP;
        DataStruct.SendMBoxData.SEG        = MDef.SEG_NULL;
        DataStruct.SendMBoxData.ACK        = MDef.ACK_NULL;
        DataStruct.SendMBoxData.CMD_TYPE   = MDef.CMDTYPE_C;
        DataStruct.SendMBoxData.CMD_ID     = MDef.CMDID_C_PUMode;
        DataStruct.SendMBoxData.CMD_LENGTH = 0x10;
        DataStruct.SendMBoxData.PARAM1     = mode;
        DataStruct.SendMBoxData.PARAM2     = 0x00;

        MSendDataToDevice(send);

    }


    /**
     * 获取文件列表
     */
    public static void getPlayList(boolean send){

        if(DataStruct.CurMusic.Total < 5){
            DataStruct.CurMusic.UpdateListNum = DataStruct.CurMusic.Total;
        }else {
            if((DataStruct.CurMusic.Total
                    - DataStruct.CurMusic.UpdateListStart)<5){

                DataStruct.CurMusic.UpdateListNum = DataStruct.CurMusic.Total
                        - DataStruct.CurMusic.UpdateListStart+1;
            }else {
                DataStruct.CurMusic.UpdateListNum = 5;
            }
        }
        if(DataStruct.CurMusic.UpdateListNum > 0){
            DataStruct.SendMBoxData.CHAN       = MDef.CHAN_SPP;
            DataStruct.SendMBoxData.SEG        = MDef.SEG_NULL;
            DataStruct.SendMBoxData.ACK        = MDef.ACK_NULL;
            DataStruct.SendMBoxData.CMD_TYPE   = MDef.CMDTYPE_Q;
            DataStruct.SendMBoxData.CMD_ID     = MDef.CMDID_Q_MusicListInfo;
            DataStruct.SendMBoxData.CMD_LENGTH = 0x10;
            DataStruct.SendMBoxData.PARAM1     = DataStruct.CurMusic.UpdateListStart;
            DataStruct.SendMBoxData.PARAM2     = DataStruct.CurMusic.UpdateListNum;
            MSendDataToDevice(send);
        }
    }

    /**
     * 获取文件总数
     */
    public static void getFileListTotal(boolean send){
        DataStruct.SendMBoxData.CHAN       = MDef.CHAN_SPP;
        DataStruct.SendMBoxData.SEG        = MDef.SEG_NULL;
        DataStruct.SendMBoxData.ACK        = MDef.ACK_NULL;
        DataStruct.SendMBoxData.CMD_TYPE   = MDef.CMDTYPE_Q;
        DataStruct.SendMBoxData.CMD_ID     = MDef.CMDID_Q_FileTotalInfo;
        DataStruct.SendMBoxData.CMD_LENGTH = 0x10;
        DataStruct.SendMBoxData.PARAM1     = 0;
        DataStruct.SendMBoxData.PARAM2     = 0;
        MSendDataToDevice(send);

    }
    public static void getFileList(boolean send){
        if(DataStruct.CurMusic.FileTotal < 5){
            DataStruct.CurMusic.UpdateFileListNum = DataStruct.CurMusic.FileTotal;
        }else {
            if((DataStruct.CurMusic.FileTotal
                    - DataStruct.CurMusic.UpdateFileListStart)<5){

                DataStruct.CurMusic.UpdateFileListNum = DataStruct.CurMusic.FileTotal
                        - DataStruct.CurMusic.UpdateFileListStart+1;
            }else {
                DataStruct.CurMusic.UpdateFileListNum = 5;
            }
        }
        if(DataStruct.CurMusic.UpdateFileListNum > 0){
            DataStruct.SendMBoxData.CHAN       = MDef.CHAN_SPP;
            DataStruct.SendMBoxData.SEG        = MDef.SEG_NULL;
            DataStruct.SendMBoxData.ACK        = MDef.ACK_NULL;
            DataStruct.SendMBoxData.CMD_TYPE   = MDef.CMDTYPE_Q;
            DataStruct.SendMBoxData.CMD_ID     = MDef.CMDID_Q_FileListInfo;
            DataStruct.SendMBoxData.CMD_LENGTH = 0x10;
            DataStruct.SendMBoxData.PARAM1     = DataStruct.CurMusic.UpdateFileListStart;
            DataStruct.SendMBoxData.PARAM2     = DataStruct.CurMusic.UpdateFileListNum;
            MSendDataToDevice(send);
        }
    }

    public static void getFileListStEnd(boolean send,int start,int num){
        DataStruct.SendMBoxData.CHAN       = MDef.CHAN_SPP;
        DataStruct.SendMBoxData.SEG        = MDef.SEG_NULL;
        DataStruct.SendMBoxData.ACK        = MDef.ACK_NULL;
        DataStruct.SendMBoxData.CMD_TYPE   = MDef.CMDTYPE_Q;
        DataStruct.SendMBoxData.CMD_ID     = MDef.CMDID_Q_FileListInfo;
        DataStruct.SendMBoxData.CMD_LENGTH = 0x10;
        DataStruct.SendMBoxData.PARAM1     = start;
        DataStruct.SendMBoxData.PARAM2     = num;
        MSendDataToDevice(send);
    }

    /**
     * 获取播放状态
     */
    public static void getStatus(boolean send){
        MacCfg.delayms = 200;
        DataStruct.SendMBoxData.CHAN       = MDef.CHAN_SPP;
        DataStruct.SendMBoxData.SEG        = MDef.SEG_NULL;
        DataStruct.SendMBoxData.ACK        = MDef.ACK_NULL;
        DataStruct.SendMBoxData.CMD_TYPE   = MDef.CMDTYPE_Q;
        DataStruct.SendMBoxData.CMD_ID     = MDef.CMDID_Q_StatusMsg;
        DataStruct.SendMBoxData.CMD_LENGTH = 0x10;
        DataStruct.SendMBoxData.PARAM1     = 0x00;
        DataStruct.SendMBoxData.PARAM2     = 0x00;
        MSendDataToDevice(send);
    }

    /**
     * 获取播放状态
     */
    public static void getCurMusicAndID3(boolean send){
        DataStruct.SendMBoxData.CHAN       = MDef.CHAN_SPP;
        DataStruct.SendMBoxData.SEG        = MDef.SEG_NULL;
        DataStruct.SendMBoxData.ACK        = MDef.ACK_NULL;
        DataStruct.SendMBoxData.CMD_TYPE   = MDef.CMDTYPE_Q;
        DataStruct.SendMBoxData.CMD_ID     = MDef.CMDID_Q_CurSongNameInfo;
        DataStruct.SendMBoxData.CMD_LENGTH = 0x10;
        DataStruct.SendMBoxData.PARAM1     = 0x00;
        DataStruct.SendMBoxData.PARAM2     = 0x00;

        MSendDataToDevice(send);
    }


    public static void MusicBox_CMD(boolean send) {

        if(DataStruct.CurMusic.config_flag == 1){  //获取状态，直到完成配置
//            System.out.println("BUG DataStruct.CurMusic.BoolHaveUHost==="+DataStruct.CurMusic.BoolHaveUHost
//                    +"是否更新"+DataStruct.CurMusic.BoolHaveUpdateFileTotal+"是否ID3"+DataStruct.CurMusic.BoolHaveUPdateID3
//            +"是否更新文件列表"+DataStruct.CurMusic.BoolHaveUPdateFileList+"是否更新当前列表"+DataStruct.CurMusic.BoolHaveUPdateList
//            );
            if(DataStruct.CurMusic.BoolHaveUHost) {  //是否有U盘
                if(DataStruct.CurMusic.BoolHaveUpdateFileTotal) {//是否更新文件总数
                    if(DataStruct.CurMusic.BoolHaveUPdateID3) {//是否更新ID3信息
                        if(DataStruct.CurMusic.BoolHaveUPdateFileList) {//是否更新文件列表
                            if(DataStruct.CurMusic.BoolHaveUPdateList){  //是否获取当前列表
                                getStatus(send);
                            }else {
                                getPlayList(send);
                            }
                        }else {
                            getFileList(send);
                        }
                    }else {
                        getCurMusicAndID3(send);
                    }
                }else {
                    getFileListTotal(send);
                }
            }else {
                getStatus(send);
            }
        }else {
            getStatus(send);
        }
    }


//        if(DataStruct.CurMusic.config_flag == 1){//获取状态，直到完成配置
//            if(DataStruct.CurMusic.BoolHaveUHost) {//是否有U盘
//                if(DataStruct.CurMusic.BoolHaveUpdateFileTotal) {//是否更新文件总数
//                    if(DataStruct.CurMusic.BoolHaveUPdateID3) {//是否更新ID3信息
//                        if(DataStruct.CurMusic.BoolHaveUPdateFileList) {//是否更新文件列表
//                            if(DataStruct.CurMusic.BoolHaveUPdateList){//是否获取当前列表
//                                getStatus(send);
//                            }else {
//                                getPlayList(send);
//                            }
//                        }else {
//                            getFileList(send);
//                        }
//                    }else {
//                        getCurMusicAndID3(send);
//                    }
//                }else {
//                    getFileListTotal(send);
//                }
//            }else {
//                getStatus(send);
//            }
//        }else {
//            getStatus(send);
//        }
  //  }

    public static String byteArCSOfLittleEndianUnicodeToString(int[] mBytes){
        StringBuffer unicode = new StringBuffer();
        byte[] mByte = new byte[mBytes.length];
        for(int i=0;i<mByte.length;i++){
            mByte[i]=(byte)(mBytes[i]&0xff);
        }

        byte temp = 0;
        for(int i=0;i<mByte.length-2;i+=2){
            temp = mByte[i];
            mByte[i] = mByte[i+1];
            mByte[i+1] = temp;
        }

        String tempSt = "";
        try {
            tempSt = new String(mByte, "Unicode");
        } catch (UnsupportedEncodingException e) {
            //  Auto-generated catch block
            e.printStackTrace();
        }
        return tempSt;

    }

    public static String byteArCSOfBigEndianUnicodeToString(int[] mBytes){
        StringBuffer unicode = new StringBuffer();
        byte[] mByte = new byte[mBytes.length];
        for(int i=0;i<mByte.length;i++){
            mByte[i]=(byte)(mBytes[i]&0xff);
        }

//        byte temp = 0;
//        for(int i=0;i<mByte.length-2;i+=2){
//            temp = mByte[i];
//            mByte[i] = mByte[i+1];
//            mByte[i+1] = temp;
//        }

        String tempSt = "";
        try {
            tempSt = new String(mByte, "Unicode");
        } catch (UnsupportedEncodingException e) {
            //  Auto-generated catch block
            e.printStackTrace();
        }
        return tempSt;

    }


    public static String byteArCSOfANSIToString(int[] mBytes){
        byte[] mByte = new byte[mBytes.length];
        for(int i=0;i<mBytes.length;i++){
            mByte[i]=(byte)(mBytes[i]&0xff);
        }

        String tempSt = "";
        try {
            tempSt = new String(mByte, "GB2312");
        } catch (UnsupportedEncodingException e) {
            //  Auto-generated catch block
            e.printStackTrace();
        }

        return tempSt;
    }

    public static String byteArCSOfUTF_8ToString(int[] mBytes){
        StringBuffer unicode = new StringBuffer();
        byte[] mByte = new byte[mBytes.length];
        for(int i=0;i<mBytes.length;i++){
            mByte[i]=(byte)(mBytes[i]&0xff);
        }

        String tempSt = "";
        try {
            tempSt = new String(mByte, "utf8");
        } catch (UnsupportedEncodingException e) {
            //  Auto-generated catch block
            e.printStackTrace();
        }
        return tempSt;

    }
    //音乐类型 0-MusicType 0-mp3,1-wma,2-wav,3-flac,4-ape,5-mp3,6-mp3
    private static String getFromSt(int From){
        switch (From){
            case 0:return MDef.MUSIC_TYPE_MP3;
            case 1:return MDef.MUSIC_TYPE_WMA;
            case 2:return MDef.MUSIC_TYPE_WAV;
            case 3:return MDef.MUSIC_TYPE_FLAC;
            case 4:return MDef.MUSIC_TYPE_APE;
            case 5:return MDef.MUSIC_TYPE_MP3;
            case 6:return MDef.MUSIC_TYPE_MP3;

        }
        return "mp3";
    }
    private static String getFileWithSongId(int id){
        for(int i=0;i<DataStruct.FileList.size();i++){
            if((id>=DataStruct.FileList.get(i).SongsStartID)
                    &&(id<=DataStruct.FileList.get(i).SongsEndID)){
                return DataStruct.FileList.get(i).FileName;
            }
        }
        return "";
    }



    private static String getStringbyCode(int code, int[] mBytes){
        switch (code){
            case MDef.CODE_ANSI: return byteArCSOfANSIToString(mBytes);
            case MDef.CODE_UnicodeLittleEndian: return byteArCSOfLittleEndianUnicodeToString(mBytes);
            case MDef.CODE_UnicodeBigEndian: return byteArCSOfBigEndianUnicodeToString(mBytes);
            case MDef.CODE_UTF_8: return byteArCSOfUTF_8ToString(mBytes);
            default: return byteArCSOfANSIToString(mBytes);
        }
    }

    public static void MSendDataToDevice(boolean sendorinsert) {
        for(int i = 0; i<(MData.MDataMax); i++){
            DataStruct.FDMBoxBuf[i]=(byte)(0x00);
        }

        DataStruct.FDMBoxBuf[0] = (byte)0x01;
        DataStruct.FDMBoxBuf[1] = (byte)0xfe;
        DataStruct.FDMBoxBuf[2] = (byte)(DataStruct.SendMBoxData.CHAN+
                (DataStruct.SendMBoxData.SEG<<2)+
                (DataStruct.SendMBoxData.ACK<<4));
        DataStruct.FDMBoxBuf[3] = (byte)0x00;

        DataStruct.FDMBoxBuf[4] = (byte) (DataStruct.SendMBoxData.CMD_TYPE & 0xff);   // 命令类型
        DataStruct.FDMBoxBuf[5] = (byte) (DataStruct.SendMBoxData.CMD_ID & 0xff);     // 命令号
        DataStruct.FDMBoxBuf[6] = (byte) (DataStruct.SendMBoxData.CMD_LENGTH & 0xff); // 命令长度
        DataStruct.FDMBoxBuf[7] = (byte) ((DataStruct.SendMBoxData.CMD_LENGTH >> 8) & 0xff);
        DataStruct.FDMBoxBuf[8] = (byte)   (DataStruct.SendMBoxData.PARAM1 & 0xff); // 命令第一个参数
        DataStruct.FDMBoxBuf[9] = (byte)  ((DataStruct.SendMBoxData.PARAM1 >> 8) & 0xff);
        DataStruct.FDMBoxBuf[10] = (byte) ((DataStruct.SendMBoxData.PARAM1 >> 16) & 0xff);
        DataStruct.FDMBoxBuf[11] = (byte) ((DataStruct.SendMBoxData.PARAM1 >> 24) & 0xff);
        DataStruct.FDMBoxBuf[12] = (byte)  (DataStruct.SendMBoxData.PARAM2 & 0xff); // 命令第二个参数
        DataStruct.FDMBoxBuf[13] = (byte) ((DataStruct.SendMBoxData.PARAM2 >> 8) & 0xff);
        DataStruct.FDMBoxBuf[14] = (byte) ((DataStruct.SendMBoxData.PARAM2 >> 16) & 0xff);
        DataStruct.FDMBoxBuf[15] = (byte) ((DataStruct.SendMBoxData.PARAM2 >> 24) & 0xff);

        if(DataStruct.SendMBoxData.CMD_LENGTH > 16){
            for(int i=0;i<DataStruct.SendMBoxData.CMD_LENGTH;i++){
                DataStruct.FDMBoxBuf[i+16] = (byte)DataStruct.SendMBoxData.BUF[i];
            }
        }

        if(sendorinsert){
            if(MacCfg.COMMUNICATION_MODE== Define.COMMUNICATION_WITH_BLUETOOTH_SPP_TWO){
                ServiceOfCom.SPP_LESendPack(DataStruct.FDMBoxBuf, DataStruct.SendMBoxData.CMD_LENGTH);
            }
        }else {//加载列表
            byte[] tempbuffer = new byte[DataStruct.SendMBoxData.CMD_LENGTH];
            for (int i = 0; i < DataStruct.SendMBoxData.CMD_LENGTH; i++) {
                tempbuffer[i] = DataStruct.FDMBoxBuf[i];
            }
//            if(DataStruct.MSendbufferList.size() > 3){
//                DataStruct.MSendbufferList.clear();
//                DataStruct.MSendbufferList.removeAll(DataStruct.MSendbufferList);
//            }
            DataStruct.MSendbufferList.add(tempbuffer);
        }

    }


    //////////////////////////////////////////////////////////////
    /////////////////////// MusicBox /////////////////////////////
    //////////////////////////////////////////////////////////////
    public static void clearMusicRecFrameFlg(){
        MDef.U0HeadFlag = false;
        MDef.U0HeadCnt = 0;
        MDef.U0DataCnt = 0;
    }

    // 接收数据，检包
    public static void RecMusicBoxDataFromDevice(Context mContext, int data, int type) {

        if(!MDef.U0HeadFlag){
            if((data == 0x01)&&(MDef.U0HeadCnt == 0)){
                MDef.U0HeadCnt = 1;
            }else if((data == 0xfe)&&(MDef.U0HeadCnt == 1)){
                MDef.U0HeadCnt = 2;
                MDef.U0DataCnt = 2;//FLAGS开始
                MDef.U0HeadFlag = true;
            }
        }else {
            MDef.U0HeadCnt = 0;

            if(MDef.U0DataCnt >= (MData.MDataMax - 1)){
                clearMusicRecFrameFlg();
                return;
            }
            DataStruct.RcvMBoxData.BUF[MDef.U0DataCnt] = (data);
            MDef.U0DataCnt += 1;

            if (MDef.U0DataCnt >= (15)) {
                DataStruct.RcvMBoxData.FLAGS_Temp = (DataStruct.RcvMBoxData.BUF[2]
                        +((DataStruct.RcvMBoxData.BUF[3]&0xff)<<8));

                DataStruct.RcvMBoxData.CHAN = DataStruct.RcvMBoxData.FLAGS_Temp&0x03;
                DataStruct.RcvMBoxData.SEG = (DataStruct.RcvMBoxData.FLAGS_Temp>>2)&0x03;
                DataStruct.RcvMBoxData.ACK = (DataStruct.RcvMBoxData.FLAGS_Temp>>4)&0x01;

                DataStruct.RcvMBoxData.CMD_TYPE = DataStruct.RcvMBoxData.BUF[4];
                DataStruct.RcvMBoxData.CMD_ID   = DataStruct.RcvMBoxData.BUF[5];
                DataStruct.RcvMBoxData.CMD_LENGTH = DataStruct.RcvMBoxData.BUF[6]+
                        ((DataStruct.RcvMBoxData.BUF[7])*256);

                DataStruct.RcvMBoxData.PARAMLENGTH = DataStruct.RcvMBoxData.CMD_LENGTH-16;

                DataStruct.RcvMBoxData.PARAM1 =

                        DataStruct.RcvMBoxData.BUF[8]
                        +((DataStruct.RcvMBoxData.BUF[9])<<8)
                        +((DataStruct.RcvMBoxData.BUF[10])<<16)
                        +((DataStruct.RcvMBoxData.BUF[11])<<24);

                DataStruct.RcvMBoxData.PARAM2 = DataStruct.RcvMBoxData.BUF[12]
                        +((DataStruct.RcvMBoxData.BUF[13])<<8)
                        +((DataStruct.RcvMBoxData.BUF[14])<<16)
                        +((DataStruct.RcvMBoxData.BUF[15])<<24);



                if(DataStruct.RcvMBoxData.CMD_LENGTH>0){
                    if(MDef.U0DataCnt >= DataStruct.RcvMBoxData.CMD_LENGTH){
                        for(int i=0;i<DataStruct.RcvMBoxData.PARAMLENGTH;i++){
                            DataStruct.RcvMBoxData.DataBUF[i]=(byte)(DataStruct.RcvMBoxData.BUF[16+i]);
                        }
                        clearMusicRecFrameFlg();

                        ProcessMusicBoxData(mContext);
                    }
                }else {
                    clearMusicRecFrameFlg();
                    ProcessMusicBoxData(mContext);
                }
            }
        }
    }

    /***接受到的音乐一些消息*/
    public static void ProcessMusicBoxData(Context mContext) {
        //printMDataFrameMsg();
        if((DataStruct.RcvMBoxData.CMD_TYPE == MDef.CMDTYPE_P)
                ||(DataStruct.RcvMBoxData.CMD_TYPE == MDef.CMDTYPE_C)
                ||(DataStruct.RcvMBoxData.CMD_TYPE == MDef.CMDTYPE_K)
                ||(DataStruct.RcvMBoxData.CMD_TYPE == MDef.CMDTYPE_Q)
                ||(DataStruct.RcvMBoxData.CMD_TYPE == MDef.CMDTYPE_A)
                ||(DataStruct.RcvMBoxData.CMD_TYPE == MDef.CMDTYPE_R)
                ||(DataStruct.RcvMBoxData.CMD_TYPE == MDef.CMDTYPE_S)
                ||(DataStruct.RcvMBoxData.CMD_TYPE == MDef.CMDTYPE_B)
                ) {

            printMDataFrameMsg();


            if(DataStruct.RcvMBoxData.CMD_TYPE == MDef.CMDTYPE_P){

            }else if(DataStruct.RcvMBoxData.CMD_TYPE == MDef.CMDTYPE_C){

            }else if(DataStruct.RcvMBoxData.CMD_TYPE == MDef.CMDTYPE_K){

            }else if(DataStruct.RcvMBoxData.CMD_TYPE == MDef.CMDTYPE_Q){

            }else if(DataStruct.RcvMBoxData.CMD_TYPE == MDef.CMDTYPE_A){

                if(DataStruct.RcvMBoxData.CMD_ID == MDef.CMDID_A_StatusMsg){//获取状态信息
//                    System.out.println("BUG ===================== Status!!!===============================");
                    if((DataStruct.RcvMBoxData.CMD_LENGTH == 0x28)
                            ||(DataStruct.RcvMBoxData.CMD_LENGTH == 0x40)){


                        if(DataStruct.CurMusic.ap_type != DataStruct.RcvMBoxData.DataBUF[0]){
                            DataOptUtil.send_get_SYSTEM_DATA_CMD();
                        }

                        DataStruct.CurMusic.ap_type=DataStruct.RcvMBoxData.DataBUF[0];//当前应用类型
                        DataStruct.CurMusic.max_volume=DataStruct.RcvMBoxData.DataBUF[1];//最大音量
                        DataStruct.CurMusic.min_volume=DataStruct.RcvMBoxData.DataBUF[2];//最小音量
                        DataStruct.CurMusic.cur_volume=DataStruct.RcvMBoxData.DataBUF[3];//当前音量
                        DataStruct.CurMusic.cur_eqtype=DataStruct.RcvMBoxData.DataBUF[4];//当前EQ模式
                        DataStruct.CurMusic.mute_flag=DataStruct.RcvMBoxData.DataBUF[5];//是否静音
                        DataStruct.CurMusic.battery_state=DataStruct.RcvMBoxData.DataBUF[6];//当前电池状态
                        DataStruct.CurMusic.battery_value=DataStruct.RcvMBoxData.DataBUF[7];//当前电池电量0-4
                        DataStruct.CurMusic.sdcard_in=DataStruct.RcvMBoxData.DataBUF[8];//当前SD卡状态，无插卡，插卡，卡播放
                        DataStruct.CurMusic.uhost_in=DataStruct.RcvMBoxData.DataBUF[9];//当前UHost状态，无UHost，插UHost，UHost播放
                        DataStruct.CurMusic.usb_in=DataStruct.RcvMBoxData.DataBUF[10];//当前UHost状态，无usb，连接usb，连接充电适配线
                        DataStruct.CurMusic.linein_in=DataStruct.RcvMBoxData.DataBUF[11];//是否连接LINEIN线
                        DataStruct.CurMusic.hp_in=DataStruct.RcvMBoxData.DataBUF[12];//是否连接耳机
                        DataStruct.CurMusic.config_flag=DataStruct.RcvMBoxData.DataBUF[13];//是否完成配置
                        DataStruct.CurMusic.alarm_clock=DataStruct.RcvMBoxData.DataBUF[14];//是否正在闹钟
                        DataStruct.CurMusic.app_argv=DataStruct.RcvMBoxData.DataBUF[15];//当前应用完成配置时，传递给APK的应用相关参数。
                        DataStruct.CurMusic.dialog_type=DataStruct.RcvMBoxData.DataBUF[16];//对话框类型
                        DataStruct.CurMusic.dialog_button_type=DataStruct.RcvMBoxData.DataBUF[17];//对话框按键组合类型
                        DataStruct.CurMusic.dialog_desc_id=DataStruct.RcvMBoxData.DataBUF[18];//对话框描述信息ID
                        DataStruct.CurMusic.dialog_active_default=DataStruct.RcvMBoxData.DataBUF[19];//默认激活按键项
                        DataStruct.CurMusic.dae_mode=DataStruct.RcvMBoxData.DataBUF[20];//当前数字音效模式0：无，1：EQ音效，2：DAE音效
                        DataStruct.CurMusic.dae_option=DataStruct.RcvMBoxData.DataBUF[21];//bit0-VBASS开 关，bit1-Treble开 关，bit2-虚拟环绕开 关
                        DataStruct.CurMusic.reserve_bytes=DataStruct.RcvMBoxData.DataBUF[22]&0xff;//RESERVE
                        DataStruct.CurMusic.reserve_bytes += ((DataStruct.RcvMBoxData.DataBUF[23]&0xff)<<8);

                        DataStruct.CurMusic.PUMode = DataStruct.RcvMBoxData.DataBUF[22]&0xff;

                        //蓝牙推送
                        if(DataStruct.CurMusic.ap_type == MDef.UHOST_PUSH){
                            DataStruct.CurMusic.BoolPhoneBlueMusicPush = false;
                        }else if(DataStruct.CurMusic.ap_type == MDef.BLUE_PUSH){
                            DataStruct.CurMusic.BoolPhoneBlueMusicPush = true;
                        }
                        //Have UHost
                        if(DataStruct.CurMusic.uhost_in == MDef.UHOST_IN){   //表示有U盘
                            if(!DataStruct.CurMusic.BoolHaveUHost){
                                reloadMusicFileListSet();
                                DataStruct.dbMFileList_Table.ResetTable();
                                DataStruct.dbMMusicList_Table.ResetTable();
                            }
                            DataStruct.CurMusic.BoolHaveUHost = true;
                        }else if(DataStruct.CurMusic.uhost_in == MDef.UHOST_OUT){  //表示U盘拔出
                            DataStruct.CurMusic.BoolHaveUHost = false;
                            DataStruct.CurMusic.BoolHaveUPdateList = false;

                        }

                        MDef.U0RecFrameFlg = true;
                        if(DataStruct.RcvMBoxData.CMD_LENGTH == 0x40){//获取状态信息

                            DataStruct.CurMusic.repeat_mode = DataStruct.RcvMBoxData.DataBUF[24];//循环模式
                            DataStruct.CurMusic.play_status = DataStruct.RcvMBoxData.DataBUF[25];//播放状态
                            DataStruct.CurMusic.ab_status = DataStruct.RcvMBoxData.DataBUF[26];//AB复读状态
                            DataStruct.CurMusic.fast_status = DataStruct.RcvMBoxData.DataBUF[27];//快进状态
                            DataStruct.CurMusic.err_status = DataStruct.RcvMBoxData.DataBUF[28];//错误状态
                            DataStruct.CurMusic.lyric = DataStruct.RcvMBoxData.DataBUF[29];//歌词存在标志
                            DataStruct.CurMusic.file_switch = DataStruct.RcvMBoxData.DataBUF[30];//文件切换标志
                            DataStruct.CurMusic.reserve = DataStruct.RcvMBoxData.DataBUF[31];//
                            //当前播放时间
                            DataStruct.CurMusic.cur_time = DataStruct.RcvMBoxData.DataBUF[32]&0xff;
                            DataStruct.CurMusic.cur_time += ((DataStruct.RcvMBoxData.DataBUF[33]&0xff)<<8);
                            DataStruct.CurMusic.cur_time += ((DataStruct.RcvMBoxData.DataBUF[34]&0xff)<<16);
                            DataStruct.CurMusic.cur_time += ((DataStruct.RcvMBoxData.DataBUF[35]&0xff)<<24);
                            //歌曲总时间
                            DataStruct.CurMusic.total_time = DataStruct.RcvMBoxData.DataBUF[36]&0xff;
                            DataStruct.CurMusic.total_time += ((DataStruct.RcvMBoxData.DataBUF[37]&0xff)<<8);
                            DataStruct.CurMusic.total_time += ((DataStruct.RcvMBoxData.DataBUF[38]&0xff)<<16);
                            DataStruct.CurMusic.total_time += ((DataStruct.RcvMBoxData.DataBUF[39]&0xff)<<24);
                            //当前歌曲序号
                            DataStruct.CurMusic.file_seq = DataStruct.RcvMBoxData.DataBUF[40]&0xff;
                            DataStruct.CurMusic.file_seq += ((DataStruct.RcvMBoxData.DataBUF[41]&0xff)<<8);
                            DataStruct.CurMusic.file_seq += ((DataStruct.RcvMBoxData.DataBUF[42]&0xff)<<16);
                            DataStruct.CurMusic.file_seq += ((DataStruct.RcvMBoxData.DataBUF[43]&0xff)<<24);
                            //歌曲总数

                            DataStruct.CurMusic.file_total = DataStruct.RcvMBoxData.DataBUF[44]&0xff;
                            DataStruct.CurMusic.file_total += ((DataStruct.RcvMBoxData.DataBUF[45]&0xff)<<8);
                            DataStruct.CurMusic.file_total += ((DataStruct.RcvMBoxData.DataBUF[46]&0xff)<<16);
                            DataStruct.CurMusic.file_total += ((DataStruct.RcvMBoxData.DataBUF[47]&0xff)<<24);
                            ///////////////
                            if((DataStruct.CurMusic.file_seq == 0)
                                    ||(DataStruct.CurMusic.file_seq > DataStruct.CurMusic.file_total)
                                    ){
                                return;
                            }

                            DataStruct.CurMusic.Volume = DataStruct.RcvMBoxData.DataBUF[3]&0xff;
                            DataStruct.CurMusic.PlayMode = DataStruct.RcvMBoxData.DataBUF[24]&0xff;
                            DataStruct.CurMusic.play_status = DataStruct.RcvMBoxData.DataBUF[25]&0xff;
                            DataStruct.CurMusic.PlayTime = DataStruct.CurMusic.cur_time;
                            DataStruct.CurMusic.TotalTime = DataStruct.CurMusic.total_time;

                            long pt = DataStruct.CurMusic.PlayTime/1000;
                            long tt = DataStruct.CurMusic.TotalTime/1000;
                            String ptm =String.valueOf(pt/60);
                            String pts =String.valueOf(pt%60);
                            String ttm =String.valueOf(tt/60);
                            String tts =String.valueOf(tt%60);
                            if(ptm.length()==1){
                                ptm = "0"+ptm;
                            }
                            if(pts.length()==1){
                                pts = "0"+pts;
                            }
                            if(ttm.length()==1){
                                ttm = "0"+ttm;
                            }
                            if(tts.length()==1){
                                tts = "0"+tts;
                            }

                            DataStruct.CurMusic.PlayTimeSt = ptm+":"+pts;
                            DataStruct.CurMusic.TotalTimeSt = ttm+":"+tts;

                            //蓝牙推送
//                            if(DataStruct.CurMusic.ap_type == MDef.UHOST_PUSH){
//                                DataStruct.CurMusic.BoolPhoneBlueMusicPush = false;
//                                if(MDef.BoolHaveManualPause&&(DataStruct.CurMusic.play_status == MDef.PLAY_STATUS_PLAY)){
//                                    MDOptUtil.setMusicPause(false);
//                                }
//                            }else if(DataStruct.CurMusic.ap_type == MDef.BLUE_PUSH){
//                                DataStruct.CurMusic.BoolPhoneBlueMusicPush = true;
////                            DataStruct.CurMusic.BoolHaveUPdateList = false;
//                            }
                            //Total
                            DataStruct.CurMusic.Total  = DataStruct.CurMusic.file_total;
                            //CurID
                            DataStruct.CurMusic.BoolHaveUPdateStates = true;
                            if(DataStruct.CurMusic.file_seq != DataStruct.CurMusic.CurID){
                                DataStruct.CurMusic.CurID = DataStruct.CurMusic.file_seq;
                                DataStruct.CurMusic.BoolHaveUPdateID3 = false;
                                DataStruct.CurMusic.UPDATESONGSINList = true;

                                //刷新界面
                                Intent intentl=new Intent();
                                intentl.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
                                intentl.putExtra("msg", Define.BoardCast_FlashUI_MusicPage);
                                intentl.putExtra(MDef.MUSICMSG_MSGTYPE, MDef.MUSICPAGE_UpdateSongInList);
                                mContext.sendBroadcast(intentl);

                                //getCurMusicAndID3(false);
                            }

                        }

                        if(DataStruct.CurMusic.config_flag == 1){
                            DataStruct.CurMusic.UPDATE = true;
                        }else{
                        }
                    }

                    //刷新界面
                    if(DataStruct.U0SynDataSucessFlg) {
                        Intent intentw = new Intent();
                        intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
                        intentw.putExtra("msg", Define.BoardCast_FlashUI_MusicPage);
                        intentw.putExtra(MDef.MUSICMSG_MSGTYPE, MDef.MUSICPAGE_Status);
                        mContext.sendBroadcast(intentw);
                    }

                }else if(DataStruct.RcvMBoxData.CMD_ID == MDef.CMDID_A_CurSongNameInfo){ //获取本地音乐当前歌曲文件名和ID3信息
                    if(DataStruct.RcvMBoxData.CMD_LENGTH == 0x14e) {
                        if((DataStruct.RcvMBoxData.PARAM1 == 0)
                                ||(DataStruct.RcvMBoxData.PARAM1 > DataStruct.CurMusic.file_total)
                                ){
                            System.out.println("BUG ====Music Index Error============PARAM1="+DataStruct.RcvMBoxData.PARAM1);
                            return;
                        }

                        //64=92=52=52=52
                        //音乐格式
                        DataStruct.CurMusic.Form = DataStruct.RcvMBoxData.DataBUF[0];

                        int code = 0;
                        boolean emt = true;
                        //FileName
                        code = DataStruct.RcvMBoxData.DataBUF[1]&0xff;
                        int[] bFN = Arrays.copyOfRange(DataStruct.RcvMBoxData.DataBUF,2,66);
                        for(int i=0;i<bFN.length;i++){
                            if(bFN[i]!=0){
                                emt = false;
                            }
                        }
                        if(emt){
                            //MDOptUtil.getCurMusicAndID3(false);
                            DataStruct.CurMusic.FileName = mContext.getString(R.string.unknownSong);
                        }else {
                            DataStruct.CurMusic.FileName = MDOptUtil.getStringbyCode(code,bFN);
                            DataStruct.CurMusic.BoolHaveUPdateID3 = true;
                        }

                        //ID3_Title
                        code = DataStruct.RcvMBoxData.DataBUF[66]&0xff;
                        bFN = Arrays.copyOfRange(DataStruct.RcvMBoxData.DataBUF,67,159);
                        emt = true;
                        for(int i=0;i<bFN.length;i++){
                            if(bFN[i]!=0){
                                emt = false;
                            }
                        }
                        if(emt){
                            DataStruct.CurMusic.ID3_Title = mContext.getString(R.string.unknownSong);
                        }else {
                            DataStruct.CurMusic.ID3_Title = MDOptUtil.getStringbyCode(code,bFN);
                        }
                        //ID3_Artist
                        code = DataStruct.RcvMBoxData.DataBUF[159]&0xff;
                        bFN = Arrays.copyOfRange(DataStruct.RcvMBoxData.DataBUF,160,212);
                        emt = true;
                        for(int i=0;i<bFN.length;i++){
                            if(bFN[i]!=0){
                                emt = false;
                            }
                        }
                        if(emt){
                            DataStruct.CurMusic.ID3_Artist = mContext.getString(R.string.unknownArtist);
                        }else {
                            DataStruct.CurMusic.ID3_Artist = MDOptUtil.getStringbyCode(code,bFN);
                        }
                        //ID3_Album
                        code = DataStruct.RcvMBoxData.DataBUF[212]&0xff;
                        bFN = Arrays.copyOfRange(DataStruct.RcvMBoxData.DataBUF,213,265);
                        emt = true;
                        for(int i=0;i<bFN.length;i++){
                            if(bFN[i]!=0){
                                emt = false;
                            }
                        }
                        if(emt){
                            DataStruct.CurMusic.ID3_Album = mContext.getString(R.string.unknown);
                        }else {
                            DataStruct.CurMusic.ID3_Album = MDOptUtil.getStringbyCode(code,bFN);
                        }
                        //ID3_Genre
                        code = DataStruct.RcvMBoxData.DataBUF[265]&0xff;
                        bFN = Arrays.copyOfRange(DataStruct.RcvMBoxData.DataBUF,266,318);
                        emt = true;
                        for(int i=0;i<bFN.length;i++){
                            if(bFN[i]!=0){
                                emt = false;
                            }
                        }
                        if(emt){
                            DataStruct.CurMusic.ID3_Genre = mContext.getString(R.string.unknown);
                        }else {
                            DataStruct.CurMusic.ID3_Genre = MDOptUtil.getStringbyCode(code,bFN);
                        }


                        DataStruct.CurMusic.UPDATE = true;
                        MDef.U0RecFrameFlg = true;



                        //刷新界面
                        Intent intentw=new Intent();
                        intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
                        intentw.putExtra("msg", Define.BoardCast_FlashUI_MusicPage);
                        intentw.putExtra(MDef.MUSICMSG_MSGTYPE, MDef.MUSICPAGE_ID3);
                        mContext.sendBroadcast(intentw);

                        //if(DataStruct.RcvMBoxData.PARAM1 != DataStruct.CurMusic.CurID){
                         //   DataStruct.CurMusic.BoolHaveUPdateID3 = false;
                        //}
                    }
                }else if(DataStruct.RcvMBoxData.CMD_ID == MDef.CMDID_A_MusicListInfo){

                    if(DataStruct.RcvMBoxData.CMD_LENGTH <= 16){
                        return;
                    }

//                    if(!DataStruct.CurMusic.BoolMLDialogCanShow){
//                        DataStruct.CurMusic.BoolMLDialogCanShow = true;
//                        //显示加载框
//                        Intent intentw=new Intent();
//                        intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
//                        intentw.putExtra("msg", Define.BoardCast_FlashUI_MusicPage);
//                        intentw.putExtra(MDef.MUSICMSG_MSGTYPE, MDef.MUSICPAGE_MListShowLoading);
//                        mContext.sendBroadcast(intentw);
//                    }

                    int[] bufN;
                    int[] bufC;
                    int[] bufCc = new int[64];
                    int NO=0;
                    boolean haveNull = false;

                    for(int i=0;i<64;i++){
                        bufCc[i] = 0;
                    }
                    //int part = DataStruct.RcvMBoxData.PARAMLENGTH/70;
                    //System.out.println("BUG MUSICBOX Music DataStruct.RcvMBoxData.PARAM1:"+DataStruct.RcvMBoxData.PARAM1);
                    for(int i=0;i<DataStruct.RcvMBoxData.PARAM1;i++){
                        bufN = Arrays.copyOfRange(DataStruct.RcvMBoxData.DataBUF,i*70,i*70+70);

                        MMListInfo mM = new MMListInfo();
                        mM.USBIDST = DataStruct.CurMusic.USBIDST;
                        mM.ID  = byteArray2int(Arrays.copyOfRange(bufN, 0, 4),4);
                        mM.Form= bufN[4&0xff];
                        mM.FormSt = getFromSt(mM.Form);
//                        mM.FileName = getFileWithSongId(mM.ID);
                        mM.Total = DataStruct.CurMusic.Total;
                        int code = bufN[5]&0xff;
                        bufC = Arrays.copyOfRange(bufN, 6, 70);
                        mM.FileName = MDOptUtil.getStringbyCode(code, bufC);
                        System.out.println("BUG MUSICBOX mM.ID="+mM.ID+",mM.name:"+mM.FileName);
                        //修正列表
                        if(DataStruct.MusicList.size() > (mM.ID-1)){
                            while (DataStruct.MusicList.size() != (mM.ID-1)){
                                DataStruct.MusicList.remove(DataStruct.MusicList.size()-1);
                            }
                        }

                        if(Arrays.equals(bufC,bufCc)){
                            haveNull = true;
                            break;
                        }

                        DataStruct.MusicList.add(mM);

                        NO = mM.ID;
                    }
                    if(!haveNull){
                        DataStruct.CurMusic.UpdateListStart = NO+1;
                    }

                    if(DataStruct.MusicList.size() > 0){
                        DataStruct.CurMusic.UPDATE = true;
                        DataStruct.CurMusic.UPDATELIST = true;
                    }

                    MDef.U0RecFrameFlg = true;

                    //Music列表划分
                    if(DataStruct.CurMusic.UpdateFileOfMusicListNum >= DataStruct.FileList.size()){
                        return;
                    }
                    if(NO >= DataStruct.FileList.
                                    get(DataStruct.CurMusic.UpdateFileOfMusicListNum).SongsEndID){
                        //清除列表
                        DataStruct.FileList.
                                get(DataStruct.CurMusic.UpdateFileOfMusicListNum).CurMusicList.clear();

                        int total = DataStruct.FileList.get(DataStruct.CurMusic.UpdateFileOfMusicListNum).TotalSongs;
                        int add = DataStruct.FileList.get(DataStruct.CurMusic.UpdateFileOfMusicListNum).SongsStartID-1;
                        int index = 0;
                        for(int j=0;j<total;j++){
                            index = add+j;
                            if(DataStruct.MusicList.size() >= (index+1)){
                                DataStruct
                                        .FileList
                                        .get(DataStruct.CurMusic.UpdateFileOfMusicListNum)
                                        .CurMusicList
                                        .add(DataStruct.MusicList.get(index));
                            }else {
                                if((DataStruct.MusicList.size() > 0)&& DataStruct.CurMusic.BoolHaveUPdateFileList){
                                    //加载出错了重新加载
                                    reloadMusicFileListSet();
                                }
                            }

                        }
                        DataStruct.CurMusic.UpdateFileOfMusicListNum++;

                        Intent intentw=new Intent();
                        intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
                        intentw.putExtra("msg", Define.BoardCast_FlashUI_MusicPage);
                        intentw.putExtra(MDef.MUSICMSG_MSGTYPE, MDef.MUSICPAGE_MListShowLoading);
                        mContext.sendBroadcast(intentw);
                    }

                    //列表读取完成
                    if(NO == DataStruct.CurMusic.Total){
                        DataStruct.CurMusic.BoolHaveUPdateList = true;
                        DataStruct.CurMusic.BoolMLDialogCanShow = false;
                        addMusicListTodB();

                        for(int i=0;i<DataStruct.FileList.size();i++){
                            loadMusicInFile(i);
                        }

                        System.out.println("BUG--BDataStruct.FileList.size():"+DataStruct.FileList.size());
                        //刷新音乐列表
                        Intent intentw=new Intent();
                        intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
                        intentw.putExtra("msg", Define.BoardCast_FlashUI_MusicPage);
                        intentw.putExtra(MDef.MUSICMSG_MSGTYPE, MDef.MUSICPAGE_List);
                        mContext.sendBroadcast(intentw);
                    }
                }else if(DataStruct.RcvMBoxData.CMD_ID == MDef.CMDID_A_FileTotalInfo){//获取文件夹总数
                    String st="ID>";
                    String ss="";

                    DataStruct.CurMusic.FileTotal = (DataStruct.RcvMBoxData.PARAM1>>16)&0xff;
                    DataStruct.CurMusic.BoolHaveUpdateFileTotal = true;
                    DataStruct.CurMusic.USBIDST="";
                    for(int i=0;i<6;i++){//10
                        DataStruct.CurMusic.USBID[i]=(byte) DataStruct.RcvMBoxData.DataBUF[i];
                        ss=Integer.toHexString(DataStruct.CurMusic.USBID[i]&0xff).toUpperCase();
                        if(ss.length()==1){
                            ss="0"+ss;
                        }
                        st+=ss;
                        DataStruct.CurMusic.USBIDST += ss;
                    }

                    MDef.U0RecFrameFlg = true;
                    System.out.println("BUG--BDBDBD#-USB-ID:"+st+",USBIDST="+DataStruct.CurMusic.USBIDST);

                    //判断是否加载U盘列表
                    DataStruct.MusicList = DataStruct.dbMMusicList_Table.getTableList();
                    DataStruct.FileList = DataStruct.dbMFileList_Table.getTableList();
                    if(DataStruct.MusicList == null){
                        System.out.println("BUG--BDBDBD DataStruct.MusicList=null");
                    }else {
                        System.out.println("BUG--BDBDBD DataStruct.MusicList.size()="+DataStruct.MusicList.size());
                    }
                    if(DataStruct.FileList == null){
                        System.out.println("BUG--BDBDBD DataStruct.FileList=null");
                    }else {
                        System.out.println("BUG--BDBDBD DataStruct.FileList.size()="+DataStruct.FileList.size());
                    }
                    if((DataStruct.MusicList.size() <=0)
                            ||(DataStruct.MusicList.size() <=0)
                            ||(DataStruct.MusicList == null)){
                        //列表为空重新加载
                        DataStruct.CurMusic.BoolHaveUPdateFileList = false;
                        DataStruct.CurMusic.BoolHaveUPdateList = false;
                    }else {
                        //获取U盘ID
                        String UID = "";
                        MMListInfo mM = DataStruct.dbMMusicList_Table.find("id",String.valueOf(1));
                        if(mM != null){
                            if((DataStruct.CurMusic.Total == DataStruct.MusicList.size())
                                    ||(DataStruct.CurMusic.USBIDST.equals(mM.USBIDST))
                                    ){
                                //
                                DataStruct.CurMusic.BoolHaveUPdateFileList = true;
                                DataStruct.CurMusic.BoolHaveUPdateList = true;

                                for(int i=0;i<DataStruct.FileList.size();i++){
                                    System.out.println("BUG--FUCK ########################");
                                    if((DataStruct.FileList.get(i).SongsEndID>=
                                            DataStruct.FileList.get(i).SongsStartID)
                                            &&(DataStruct.MusicList.size()>=DataStruct.FileList.get(i).SongsEndID)){
                                        if(DataStruct.FileList.get(i).TotalSongs==1){
                                            DataStruct.FileList.get(i).CurMusicList.add(DataStruct.MusicList.get(DataStruct.FileList.get(i).SongsStartID-1));
                                            System.out.println("BUG--FUCK ##->"+DataStruct.MusicList.get(DataStruct.FileList.get(i).SongsStartID-1).FileName);
                                        }else {
                                            for(int j=0;j<=(DataStruct.FileList.get(i).SongsEndID-DataStruct.FileList.get(i).SongsStartID);j++){
                                                DataStruct.FileList.get(i).CurMusicList.add(DataStruct.MusicList.get(DataStruct.FileList.get(i).SongsStartID+j-1));
                                                System.out.println("BUG--FUCK ##->"+DataStruct.MusicList.get(DataStruct.FileList.get(i).SongsStartID+j-1).FileName);
                                            }
                                        }

                                    }

                                }

                                //刷新文件列表
                                Intent intentf=new Intent();
                                intentf.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
                                intentf.putExtra("msg", Define.BoardCast_FlashUI_MusicPage);
                                intentf.putExtra(MDef.MUSICMSG_MSGTYPE, MDef.MUSICPAGE_FileList);
                                mContext.sendBroadcast(intentf);

                                //刷新音乐列表
                                Intent intentw=new Intent();
                                intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
                                intentw.putExtra("msg", Define.BoardCast_FlashUI_MusicPage);
                                intentw.putExtra(MDef.MUSICMSG_MSGTYPE, MDef.MUSICPAGE_List);
                                mContext.sendBroadcast(intentw);
                            }else {
                                //列表为空重新加载
                                DataStruct.CurMusic.BoolHaveUPdateFileList = false;
                                DataStruct.CurMusic.BoolHaveUPdateList = false;
                                System.out.println("BUG--BDBDBD DataStruct.dbMMusicList_Table.find=null");
                            }
                        }else {
                            //列表为空重新加载
                            DataStruct.CurMusic.BoolHaveUPdateFileList = false;
                            DataStruct.CurMusic.BoolHaveUPdateList = false;
                        }

                    }

                }else if(DataStruct.RcvMBoxData.CMD_ID == MDef.CMDID_A_FileListInfo) {//获取文件列表

//                    if(!DataStruct.CurMusic.BoolFLDialogCanShow){
//                        DataStruct.CurMusic.BoolFLDialogCanShow = true;
//                        //显示加载框
//                        Intent intentw=new Intent();
//                        intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
//                        intentw.putExtra("msg", Define.BoardCast_FlashUI_MusicPage);
//                        intentw.putExtra(MDef.MUSICMSG_MSGTYPE, MDef.MUSICPAGE_FListShowLoading);
//                        mContext.sendBroadcast(intentw);
//                    }

                    int[] bufN;
                    int[] bufC;
                    int[] bufCc = new int[64];
                    int NO=0;
                    boolean haveNull = false;

                    for(int i=0;i<64;i++){
                        bufCc[i] = 0;
                    }

                    //int part = DataStruct.RcvMBoxData.PARAMLENGTH/70;
                    for(int i=0;i<DataStruct.RcvMBoxData.PARAM2;i++){
                        bufN = Arrays.copyOfRange(DataStruct.RcvMBoxData.DataBUF,i*70,i*70+70);

                        MFListInfo mM = new MFListInfo();
                        mM.USBIDST = DataStruct.CurMusic.USBIDST;
                        mM.SongsStartID= (bufN[0]&0xff)+((bufN[1]&0xff)<<8);
                        mM.SongsEndID  = (bufN[2]&0xff)+((bufN[3]&0xff)<<8);
                        mM.TotalSongs = mM.SongsEndID-mM.SongsStartID+1;
                        mM.TotalFiles = DataStruct.CurMusic.FileTotal;
                        int code = bufN[4]&0xff;

                        if((mM.SongsStartID<0)||(mM.SongsEndID<0)){
                            System.out.println("BUG File Start=Fuck");
                        }
                        bufC = Arrays.copyOfRange(bufN, 5, 67);
                        mM.FileName = MDOptUtil.getStringbyCode(code, bufC);
                        System.out.println("BUG File Start="+mM.SongsStartID+",End="+mM.SongsEndID+",name="+mM.FileName);
                        //修正列表1
                        NO = DataStruct.RcvMBoxData.PARAM1+i;
                        if(DataStruct.FileList.size() >= NO){
                            while (DataStruct.FileList.size() != (NO-1)){
                                DataStruct.FileList.remove(DataStruct.FileList.size()-1);
                            }
                        }
                        //修正列表2
                        if(Arrays.equals(bufC,bufCc)){
                            haveNull = true;
                            break;
                        }

                        //Music列表划分
//                        if((mM.SongsEndID>mM.SongsStartID)&&(DataStruct.MusicList.size()>=mM.SongsEndID)){
//                            for(int j=0;j<=(mM.SongsEndID-mM.SongsStartID);j++){
//                                mM.CurMusicList.add(DataStruct.MusicList.get(mM.SongsStartID+j-1));
//                            }
//                        }else {
//                            System.out.println("BUG File 获取文件列表出错！！！！");
//                        }
                        mM.CurDirID = NO;
                        DataStruct.FileList.add(mM);
                    }
                    if(!haveNull){
                        DataStruct.CurMusic.UpdateFileListStart = DataStruct.RcvMBoxData.PARAM1+DataStruct.RcvMBoxData.PARAM2;
                    }

                    //列表读取完成
                    if((DataStruct.CurMusic.UpdateFileListStart-1) == DataStruct.CurMusic.FileTotal){
                        DataStruct.CurMusic.BoolHaveUPdateFileList = true;
                        DataStruct.CurMusic.BoolFLDialogCanShow = false;
                        addFileListTodB();

                        Intent intentw=new Intent();
                        intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
                        intentw.putExtra("msg", Define.BoardCast_FlashUI_MusicPage);
                        intentw.putExtra(MDef.MUSICMSG_MSGTYPE, MDef.MUSICPAGE_FileList);
                        mContext.sendBroadcast(intentw);
                    }

                    MDef.U0RecFrameFlg = true;

                    //刷新文件列表


                    //显示加载框
                    Intent intentw=new Intent();
                    intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
                    intentw.putExtra("msg", Define.BoardCast_FlashUI_MusicPage);
                    intentw.putExtra(MDef.MUSICMSG_MSGTYPE, MDef.MUSICPAGE_FListShowLoading);
                    mContext.sendBroadcast(intentw);

                }






            }else if(DataStruct.RcvMBoxData.CMD_TYPE == MDef.CMDTYPE_R){

            }else if(DataStruct.RcvMBoxData.CMD_TYPE == MDef.CMDTYPE_S){

            }


        }



    }



    /**
     * 表示向列表中添加数据(仅仅第一层的音乐数据)
     * */
    private static void addMusicListTodB(){
        System.out.println("BUG---------->>>>>>>>>>>>>>>>>>>>addMusicListTodB");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DataStruct.dbMMusicList_Table.ResetTable();
                    for(int i=0;i<DataStruct.MusicList.size();i++){
                        DataStruct.dbMMusicList_Table.insert(DataStruct.MusicList.get(i));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
    *表示向列表中添加数据(仅仅第一层的文件数据)
    */
    private static void addFileListTodB(){
        System.out.println("BUG---------->>>>>>>>>>>>>>>>>>>>addFileListTodB");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DataStruct.dbMFileList_Table.ResetTable();
                    for(int i=0;i<DataStruct.FileList.size();i++){
                        DataStruct.dbMFileList_Table.insert(DataStruct.FileList.get(i));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private static void loadMusicInFile(int indexFile){
        //清除列表
        DataStruct.FileList.get(indexFile).CurMusicList.clear();

        int total = DataStruct.FileList.get(indexFile).TotalSongs;
        int start = DataStruct.FileList.get(indexFile).SongsStartID-1;
        for(int j=0;j<total;j++){
            DataStruct
                    .FileList
                    .get(indexFile)
                    .CurMusicList
                    .add(DataStruct.MusicList.get(start+j));
        }
    }

    /**重新加载文件*/
    public static void reloadMusicFileListSet(){
        DataStruct.CurMusic.resetData();
        DataStruct.CurMusic.BoolHaveUPdateFileList = false;
        DataStruct.FileList.clear();
        DataStruct.MusicList.clear();
    }

    public static String getMusicType(int type){
        switch (type){
            case 0: return MDef.MUSIC_TYPE_MP3;
            case 1: return MDef.MUSIC_TYPE_WMA;
            case 2: return MDef.MUSIC_TYPE_WAV;
            case 3: return MDef.MUSIC_TYPE_FLAC;
            case 4: return MDef.MUSIC_TYPE_APE;

            default:return MDef.MUSIC_TYPE_MP3;
        }
    }

    private static int byteArray2int(int[] buf,int size){
        int re = 0;

        for(int i=0;i<size;i++){
            re += (buf[i]&0xff)<<(8*i);
        }

        return re;
    }

    private static void printMDataFrameMsg(){
//        System.out.println("BUG MUSICBOX XXXXXXXXXXXXXXXXXXXXXX");
//        System.out.println("BUG MUSICBOX DataStruct.RcvMBoxData.FLAGS:" + DataStruct.RcvMBoxData.FLAGS_Temp);
//        System.out.println("BUG MUSICBOX DataStruct.RcvMBoxData.CMD_TYPE:" + String.valueOf(DataStruct.RcvMBoxData.CMD_TYPE));
//        System.out.println("BUG MUSICBOX DataStruct.RcvMBoxData.CMD_ID:" + DataStruct.RcvMBoxData.CMD_ID);
//        System.out.println("BUG MUSICBOX DataStruct.RcvMBoxData.CMD_LENGTH:" + DataStruct.RcvMBoxData.CMD_LENGTH);
//        System.out.println("BUG MUSICBOX DataStruct.RcvMBoxData.PARAM1:" + DataStruct.RcvMBoxData.PARAM1);
//        System.out.println("BUG MUSICBOX DataStruct.RcvMBoxData.PARAM2:" + DataStruct.RcvMBoxData.PARAM2);
    }


}
