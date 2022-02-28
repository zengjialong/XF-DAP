package com.chs.mt.xf_dap.mac;

import android.content.Context;

import com.chs.mt.xf_dap.R;
import com.chs.mt.xf_dap.datastruct.DataStruct;
import com.chs.mt.xf_dap.datastruct.Define;
import com.chs.mt.xf_dap.datastruct.MacCfg;
import com.chs.mt.xf_dap.mac.MacModeInitData.Mac_XF_50;
import com.chs.mt.xf_dap.mac.MacModeInitData.Mac_YBD_NDS460;
import com.chs.mt.xf_dap.operation.DataOptUtil;

/**
 * Created by Administrator on 2017/7/11.
 */

public class MacModeInit {


    static void initMacModeOf_YBD_NDS460(Context mContext) {
        DataStruct.MacModeAll.HEAD_DATA[Define.MAC_TYPE_YBD_NDS460] = 0x78;           //包头
        DataStruct.MacModeAll.MacMode[Define.MAC_TYPE_YBD_NDS460] = Define.MAC_TYPE_YBD_NDS460;      //机型编号
        DataStruct.MacModeAll.MacType[Define.MAC_TYPE_YBD_NDS460] = "DEQ-80ACH";           //机型，用于音效文件识别
        DataStruct.MacModeAll.MacTypeDisplay[Define.MAC_TYPE_YBD_NDS460] = "Sound Pro";    //机型，用于显示
        DataStruct.MacModeAll.MCU_Versions[Define.MAC_TYPE_YBD_NDS460] = "MPBD-AV1.00";//下位机版本号

        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].CurMacMode = Define.MAC_TYPE_YBD_NDS460;   //机型编号
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].MacType = "DEQ-80ACH";           //机型，用于音效文件识别
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].MacTypeDisplay = "Sound Pro";           //机型，用于显示
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].MCU_Versions = "MPBD-AV1.00";//下位机版本号
        /*****************************主功能***********************************/
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].BOOL_SYSTEM_SPK_TYPEB = false;//是否使用SYSTEM_SPK_TYPEB
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].BOOL_RESET_GROUP_DATA = false;//是否使用数据复位标志，当数据出错时提示用户是否恢复出厂设置
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].BOOL_TRANSMITTAL = false;//是否使用數據傳輸標誌
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].BOOL_USE_INS = false;/*输入功能*/
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].BOOL_ENCRYPTION = false;/*加密功能*/
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].BOOL_HIDEMODE = true;/*高频二分频*/
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].BOOL_MIXER = false;/*混音设置  false:无*/
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].BOOL_INPUT_SOURCE = true;/*音源输入*/
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].BOOL_INPUT_SOURCE_VOL = true;/*音源音量*/
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].BOOL_MIXER_SOURCE = false;/*混音音源输入*/
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].BOOL_MIXER_SOURCE_VOL = false;/*混音音源音量*/
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].BOOL_SUB_VOL = true;/*低音音量*/
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].BOOL_SET_SPK_TYPE = true;/*通道类型设置*/

    /*通道联调
    Define.LINKMODE_FRS           1.前声场，后声场，超低的联调，单独分开
    Define.LINKMODE_FRS_A         2.前声场，后声场，超低的联调，一起联调
    LINKMODE_FR            3.前声场，后声场，单独分开
    Define.LINKMODE_FR_A          4.前声场，后声场，中置超低的联调，一起联调
    Define.LINKMODE_SPKTYPE       5.设置通道输出类型后的联调
    Define.LINKMODE_SPKTYPE_S     6.设置通道输出类型后的联调，可联机保存
    Define.LINKMODE_AUTO          7.任意联调，每个通道可以单独联调，可联机保存
    Define.LINKMODE_LEFTRIGHT     8.固定两两通道联调
     */
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].LinkMOde = Define.LINKMODE_SPKTYPE;

        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].MAX_USE_GROUP = 6;//用户组数据，一般固定为6
        /*****************************主音量***********************************/
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Master.MAX = 66;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Master.DATA_TRANSFER = Define.COM_TYPE_SYSTEM;


//        //添加机型
//        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Master.Mcu_Type.add("80-ACH");
//        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Master.Mcu_Type.add("100-ACH");
//        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Master.Mcu_Type.add("300-ACH");
//        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Master.Mcu_Type.add("500-ACH");


        /*****************************延时***********************************/
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Delay.MAX = 350;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Delay.DATA_TRANSFER = Define.COM_TYPE_OUTPUT;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Delay.Type = Define.DELAY_FRS;
        /*****************************Output***********************************/
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Out.OUT_CH_MAX = 12;//最大通道
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Out.OUT_CH_MAX_USE = 12;//最大通道
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Out.MaxOutVol = 600;//最大值
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Out.StepOutVol = 10;//步进

        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Out.LinkMute = true;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Out.LinkPolor = true;
        //通道类型
        for (int i = 0; i < 16; i++) {
            DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Out.SPK_TYPE[i] = 0;
        }

        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Out.SPK_TYPE[0] = 6;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Out.SPK_TYPE[1] = 12;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Out.SPK_TYPE[2] = 15;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Out.SPK_TYPE[3] = 18;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Out.SPK_TYPE[4] = 24;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Out.SPK_TYPE[5] = 21;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Out.SPK_TYPE[6] = 22;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Out.SPK_TYPE[7] = 23;
        //通道名字
//        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Out.Name[0] =
//                mContext.getString(R.string.FrontLeft);
//        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Out.Name[1] =
//                mContext.getString(R.string.FrontRight);
//        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Out.Name[2] =
//                mContext.getString(R.string.RearLeft);
//        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Out.Name[3] =
//                mContext.getString(R.string.RearRight);
//        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Out.Name[4] =
//                mContext.getString(R.string.LeftSub);
//        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Out.Name[5] =
//                mContext.getString(R.string.RightSub);
//        for(int i=6;i<16;i++){
//            DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Out.Name[i] =
//                    mContext.getString(R.string.NULL);
//        }

        for (int i = 0; i < 16; i++) {
            DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Out.Name[i] = "CH" + String.valueOf(i + 1);
        }


        /*****************************滤波器 一般固定不用修改***********************************/
        //滤波器类型
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].XOver.BOOL_Hide_6DB_Fiter = true;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].XOver.Fiter.group = 1;//全部通道都用一组
        //组一配置
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].XOver.Fiter.start1 = 0;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].XOver.Fiter.end1 =
                DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Out.OUT_CH_MAX;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].XOver.Fiter.max1 = 3;//成员个数
        for (int i = 0; i < 16; i++) {
            DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].XOver.Fiter.member1[i] = i;
        }
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].XOver.Fiter.memberName1[0] = mContext.getString(R.string.FilterLR);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].XOver.Fiter.memberName1[1] = mContext.getString(R.string.FilterBW);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].XOver.Fiter.memberName1[2] = mContext.getString(R.string.FilterB);
        //组二配置
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].XOver.Fiter.start2 = 0;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].XOver.Fiter.end2 =
                DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Out.OUT_CH_MAX;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].XOver.Fiter.max2 = 3;//成员个数
        for (int i = 0; i < 16; i++) {
            DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].XOver.Fiter.member2[i] = i;
        }
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].XOver.Fiter.memberName2[0] = mContext.getString(R.string.FilterLR);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].XOver.Fiter.memberName2[1] = mContext.getString(R.string.FilterB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].XOver.Fiter.memberName2[2] = mContext.getString(R.string.FilterBW);

        //滤波器斜率
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].XOver.Level.group = 1;//全部通道都用一组
        //组一配置
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].XOver.Level.start1 = 0;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].XOver.Level.end1 =
                DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Out.OUT_CH_MAX;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].XOver.Level.max1 = 9;//成员个数
        for (int i = 0; i < 16; i++) {
            DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].XOver.Level.member1[i] = i;
        }
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].XOver.Level.memberName1[0] = mContext.getString(R.string.Otc6dB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].XOver.Level.memberName1[1] = mContext.getString(R.string.Otc12dB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].XOver.Level.memberName1[2] = mContext.getString(R.string.Otc18dB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].XOver.Level.memberName1[3] = mContext.getString(R.string.Otc24dB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].XOver.Level.memberName1[4] = mContext.getString(R.string.Otc30dB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].XOver.Level.memberName1[5] = mContext.getString(R.string.Otc36dB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].XOver.Level.memberName1[6] = mContext.getString(R.string.Otc42dB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].XOver.Level.memberName1[7] = mContext.getString(R.string.Otc48dB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].XOver.Level.memberName1[8] = mContext.getString(R.string.OtcOFF);

//        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].XOver.Level.memberName1[0] = mContext.getString(R.string.Otc12dB);
//        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].XOver.Level.memberName1[1] = mContext.getString(R.string.Otc18dB);
//        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].XOver.Level.memberName1[2] = mContext.getString(R.string.Otc24dB);
//        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].XOver.Level.memberName1[3] = mContext.getString(R.string.Otc30dB);
//        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].XOver.Level.memberName1[4] = mContext.getString(R.string.Otc36dB);
//        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].XOver.Level.memberName1[5] = mContext.getString(R.string.Otc42dB);
//        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].XOver.Level.memberName1[6] = mContext.getString(R.string.Otc48dB);
//        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].XOver.Level.memberName1[7] = mContext.getString(R.string.OtcOFF);
        //组二配置
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].XOver.Level.start2 = 0;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].XOver.Level.end2 =
                DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Out.OUT_CH_MAX;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].XOver.Level.max2 = 5;//成员个数
        for (int i = 0; i < 16; i++) {
            DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].XOver.Level.member2[i] = i;
        }
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].XOver.Level.memberName2[0] = mContext.getString(R.string.Otc12dB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].XOver.Level.memberName2[1] = mContext.getString(R.string.Otc18dB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].XOver.Level.memberName2[2] = mContext.getString(R.string.Otc24dB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].XOver.Level.memberName2[3] = mContext.getString(R.string.Otc30dB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].XOver.Level.memberName2[4] = mContext.getString(R.string.Otc36dB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].XOver.Level.memberName2[5] = mContext.getString(R.string.Otc42dB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].XOver.Level.memberName2[6] = mContext.getString(R.string.Otc48dB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].XOver.Level.memberName2[7] = mContext.getString(R.string.OtcOFF);
        /*****************************EQ***********************************/
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].EQ.group = 1;//有多少组
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].EQ.Max_EQ = 31;//使用的EQ,31,10
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].EQ.GPEQ_Mode = true;//图示均衡模式
        //EQ增益范围
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].EQ.EQ_LEVEL_MAX = 720;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].EQ.EQ_LEVEL_MIN = 480;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].EQ.EQ_LEVEL_ZERO = 600;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].EQ.EQ_Gain_MAX = 240;

        //组一
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].EQ.EQ_Max1 = 31;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].EQ.start1 = 0;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].EQ.end1 =
                DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Out.OUT_CH_MAX;
        //组二
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].EQ.EQ_Max2 = 31;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].EQ.start2 = 0;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].EQ.end2 =
                DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Out.OUT_CH_MAX;
        /****************************混音************************************/
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Mixer.MIXER_CH_MAX = 16;//最大通道
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Mixer.MIXER_CH_MAX_USE = 16;//最大通道
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Mixer.Max_Mixer_Vol = 100;//最大值
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Mixer.BOOL_Polar = false;//有无相反相
        //通道名字
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Mixer.Name[0] =
                mContext.getString(R.string.Mixer_IN1);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Mixer.Name[1] =
                mContext.getString(R.string.Mixer_IN2);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Mixer.Name[2] =
                mContext.getString(R.string.Mixer_IN3);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Mixer.Name[3] =
                mContext.getString(R.string.Mixer_IN4);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Mixer.Name[4] =
                mContext.getString(R.string.Mixer_IN5);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Mixer.Name[5] =
                mContext.getString(R.string.Mixer_IN6);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Mixer.Name[6] =
                mContext.getString(R.string.Mixer_IN15);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Mixer.Name[7] =
                mContext.getString(R.string.Mixer_IN16);


        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Mixer.Name2[0] =
                mContext.getString(R.string.Mixer_HI_1);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Mixer.Name2[1] =
                mContext.getString(R.string.Mixer_HI_2);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Mixer.Name2[2] =
                mContext.getString(R.string.Mixer_HI_3);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Mixer.Name2[3] =
                mContext.getString(R.string.Mixer_HI_4);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Mixer.Name2[4] =
                mContext.getString(R.string.Mixer_HI_5);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Mixer.Name2[5] =
                mContext.getString(R.string.Mixer_HI_6);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Mixer.Name2[6] =
                mContext.getString(R.string.Mixer_HI_7);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Mixer.Name2[7] =
                mContext.getString(R.string.Mixer_HI_8);

        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Mixer.Name2[8] =
                mContext.getString(R.string.Mixer_HI_9);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Mixer.Name2[9] =
                mContext.getString(R.string.Mixer_HI_10);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Mixer.Name2[10] =
                mContext.getString(R.string.Mixer_IN5);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Mixer.Name2[11] =
                mContext.getString(R.string.Mixer_IN6);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Mixer.Name2[12] =
                mContext.getString(R.string.Mixer_IN13);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Mixer.Name2[13] =
                mContext.getString(R.string.Mixer_IN14);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Mixer.Name2[14] =
                mContext.getString(R.string.Mixer_IN15);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Mixer.Name2[15] =
                mContext.getString(R.string.Mixer_IN16);


//        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Mixer.Name[8] =
//                mContext.getString(R.string.Mixer_IN16);
//        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Mixer.Name[9] =
//                mContext.getString(R.string.Mixer_IN10);
//        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Mixer.Name[10] =
//                mContext.getString(R.string.Mixer_IN11);
//        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Mixer.Name[11] =
//                mContext.getString(R.string.Mixer_IN12);
//        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Mixer.Name[12] =
//                mContext.getString(R.string.Mixer_IN13);
//        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Mixer.Name[13] =
//                mContext.getString(R.string.Mixer_IN14);
//        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Mixer.Name[14] =
//                mContext.getString(R.string.Mixer_IN15);
//        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Mixer.Name[15] =
//                mContext.getString(R.string.Mixer_IN16);
        /****************************音源************************************/
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].inputsource.MaxVol = 40;//音源音量
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].inputsource.Max = 4;//音源个数

        //音源名字
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].inputsource.Name[0] = mContext.getString(R.string.Hi_Level);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].inputsource.Name[1] = mContext.getString(R.string.AUX);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].inputsource.Name[2] = mContext.getString(R.string.Bluetooth);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].inputsource.Name[3] = mContext.getString(R.string.Digit);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].inputsource.Name[4] = mContext.getString(R.string.Optical);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].inputsource.Name[5] = mContext.getString(R.string.Coaxial);


        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].inputsource.Model_Name[0] = mContext.getString(R.string.mode_2);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].inputsource.Model_Name[1] = mContext.getString(R.string.mode_1);
        //与名字对应的音源值
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].inputsource.inputsource[0] = 1;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].inputsource.inputsource[1] = 3;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].inputsource.inputsource[2] = 2;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].inputsource.inputsource[3] = 0;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].inputsource.inputsource[4] = 5;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].inputsource.inputsource[5] = 6;
        /****************************混音音源************************************/
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Mixersource.MaxVol = 60;//音源音量
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Mixersource.Max = 4;//音源个数

        //混音音源名字
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Mixersource.Name[0] = mContext.getString(R.string.Hi_Level);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Mixersource.Name[1] = mContext.getString(R.string.AUX);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Mixersource.Name[2] = mContext.getString(R.string.Bluetooth);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Mixersource.Name[3] = mContext.getString(R.string.OFF);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Mixersource.Name[4] = mContext.getString(R.string.Optical);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Mixersource.Name[5] = mContext.getString(R.string.Coaxial);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Mixersource.Name[6] = mContext.getString(R.string.NULL);

        //与名字对应的混音音源值
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Mixersource.inputsource[0] = 1;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Mixersource.inputsource[1] = 3;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Mixersource.inputsource[2] = 2;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Mixersource.inputsource[3] = 255;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Mixersource.inputsource[4] = 5;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Mixersource.inputsource[5] = 6;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].Mixersource.inputsource[6] = 7;
        /*****************************输入***********************************/
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.INS_CH_MAX = 11;//最大通道
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.INS_CH_MAX_USE = 11;//最大通道
        for (int i = 0; i < 16; i++) {
            DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.Name[i] = String.valueOf(i);
        }

        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.INS_CH_MAX_EQ = 4;//最大通道
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.INS_CH_MAX_EQ_USE = 4;//最大通道

        //滤波器类型
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.XOver.BOOL_Hide_6DB_Fiter = false;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.XOver.Fiter.group = 1;//全部通道都用一组
        //组一配置
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.XOver.Fiter.start1 = 0;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.XOver.Fiter.end1 =
                DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.INS_CH_MAX;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.XOver.Fiter.max1 = 3;//成员个数
        for (int i = 0; i < 16; i++) {
            DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.XOver.Fiter.member1[i] = i;
        }
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.XOver.Fiter.memberName1[0] = mContext.getString(R.string.FilterLR);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.XOver.Fiter.memberName1[1] = mContext.getString(R.string.FilterB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.XOver.Fiter.memberName1[2] = mContext.getString(R.string.FilterBW);
        //组二配置
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.XOver.Fiter.start2 = 0;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.XOver.Fiter.end2 =
                DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.INS_CH_MAX;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.XOver.Fiter.max2 = 3;//成员个数
        for (int i = 0; i < 16; i++) {
            DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.XOver.Fiter.member2[i] = i;
        }
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.XOver.Fiter.memberName2[0] = mContext.getString(R.string.FilterLR);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.XOver.Fiter.memberName2[1] = mContext.getString(R.string.FilterB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.XOver.Fiter.memberName2[2] = mContext.getString(R.string.FilterBW);

        //滤波器斜率
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.XOver.Level.group = 1;//全部通道都用一组
        //组一配置
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.XOver.Level.start1 = 0;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.XOver.Level.end1 =
                DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.INS_CH_MAX;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.XOver.Level.max1 = 8;//成员个数
        for (int i = 0; i < 16; i++) {
            DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.XOver.Level.member1[i] = i;
        }
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.XOver.Level.memberName1[0] = mContext.getString(R.string.Otc12dB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.XOver.Level.memberName1[1] = mContext.getString(R.string.Otc18dB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.XOver.Level.memberName1[2] = mContext.getString(R.string.Otc24dB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.XOver.Level.memberName1[3] = mContext.getString(R.string.Otc30dB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.XOver.Level.memberName1[4] = mContext.getString(R.string.Otc36dB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.XOver.Level.memberName1[5] = mContext.getString(R.string.Otc42dB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.XOver.Level.memberName1[6] = mContext.getString(R.string.Otc48dB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.XOver.Level.memberName1[7] = mContext.getString(R.string.OtcOFF);

        //组二配置
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.XOver.Level.start2 = 0;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.XOver.Level.end2 =
                DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.INS_CH_MAX;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.XOver.Level.max2 = 8;//成员个数
        for (int i = 0; i < 16; i++) {
            DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.XOver.Level.member2[i] = i;
        }
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.XOver.Level.memberName2[0] = mContext.getString(R.string.Otc12dB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.XOver.Level.memberName2[1] = mContext.getString(R.string.Otc18dB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.XOver.Level.memberName2[2] = mContext.getString(R.string.Otc24dB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.XOver.Level.memberName2[3] = mContext.getString(R.string.Otc30dB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.XOver.Level.memberName2[4] = mContext.getString(R.string.Otc36dB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.XOver.Level.memberName2[5] = mContext.getString(R.string.Otc42dB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.XOver.Level.memberName2[6] = mContext.getString(R.string.Otc48dB);
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.XOver.Level.memberName2[7] = mContext.getString(R.string.OtcOFF);

        //EQ配置
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.EQ.group = 1;//有多少组
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.EQ.Max_EQ = 31;//使用的EQ,31,10
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.EQ.GPEQ_Mode = true;//图示均衡模式
        //EQ增益范围
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.EQ.EQ_LEVEL_MAX = 800;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.EQ.EQ_LEVEL_MIN = 400;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.EQ.EQ_LEVEL_ZERO = 600;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.EQ.EQ_Gain_MAX = 400;

        //组一
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.EQ.EQ_Max1 = 31;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.EQ.start1 = 0;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.EQ.end1 =
                DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.INS_CH_MAX;
        //组二
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.EQ.EQ_Max2 = 31;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.EQ.start2 = 0;
        DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.EQ.end2 =
                DataStruct.MacModeAll.MacModeArt[Define.MAC_TYPE_YBD_NDS460].INS.INS_CH_MAX;

    }

    public static void initCurMacModeArt(Context mContext) {
        //TODO
        MacCfg.bool_Encryption = false;//切换机型时把加密标志，联调标志清为默认
        DataStruct.CurMacMode = DataStruct.MacModeAll.MacModeArt[MacCfg.Define_MAC];
    }

    public static void initMacModeDefaultData(Context mContext) {
        initCurMacModeArt(mContext);
        DataOptUtil.FillRecDataStruct(Define.MUSIC, 0, Mac_YBD_NDS460.YBD_NDS460_inputs_init_data, false);
        DataOptUtil.FillRecDataStruct(Define.MUSIC, 0, Mac_YBD_NDS460.YBD_NDS460_Input_init_data, false);


            for (int i = 0; i < DataStruct.CurMacMode.Out.OUT_CH_MAX_USE; i++) {
                if (i < 4) {
                    DataOptUtil.FillRecDataStruct(Define.OUTPUT, i, Mac_XF_50.YBD_NDS460_Output1_init_data, false);
                } else {
                    DataOptUtil.FillRecDataStruct(Define.OUTPUT, i, Mac_XF_50.YBD_NDS460_Output5_init_data, false);
                }
            }


        DataOptUtil.InitDataStruct(mContext);
    }


    public static void initMacModeAllArt(Context mContext) {
        initMacModeOf_YBD_NDS460(mContext);//OK

        DataStruct.MacModeAll.MacModeMax = MacCfg.Define_MAC_MAX;
        initMacModeDefaultData(mContext);

        int[] spk_num = new int[16];//存储通道类型的数组
        String[] Name = new String[16]; //存储混音名字的数组
        String[] inputsourceName = new String[16]; //存储音源名字的数组
        int[] inputindex = new int[16];

        switch (MacCfg.Mcu) {//0=80ACH 1=100ACH 2=101ACH 3=300ACH 4=500ACH
            case 0:
                MacCfg.App_version = "APXF-CV1.00(DEQ-060ACH)(Beta)";  //APP版本
                MacCfg.MCU_Version = "MPXF-CV1.";  //80版本
                DataStruct.CurMacMode.MacTypeDisplay = "Pioneer DSP";//设备显示

                DataStruct.CurMacMode.Out.OUT_CH_MAX_USE = 6;
                MacCfg.OutMax = 6;
                DataStruct.CurMacMode.inputsource.Max = 3;

                inputsourceName[0] = mContext.getString(R.string.High);
                inputsourceName[1] = mContext.getString(R.string.AUX);
                inputsourceName[2] = mContext.getString(R.string.Bluetooth);
                inputindex[0] = 1;
                inputindex[1] = 3;
                inputindex[2] = 2;

                DataStruct.CurMacMode.Delay.MAX = 259;
                break;
            case 1: //100
                spk_num = new int[]{6, 12, 15, 18, 22, 23, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                MacCfg.App_version = "APXF-AV1.00(DEQ-200ACH)(Beta)";  //APP版本
                MacCfg.MCU_Version = "MPXF-AV1.";  //100版本
                DataStruct.CurMacMode.MacTypeDisplay = "Pioneer DSP";//设备显示
                MacCfg.ButtomItemMaxUse = 5;

                MacCfg.ModelMax=2;


                Name[0] = mContext.getString(R.string.Mixer_IN1);
                Name[1] = mContext.getString(R.string.Mixer_IN2);
                Name[2] = mContext.getString(R.string.Mixer_IN3);
                Name[3] = mContext.getString(R.string.Mixer_IN4);
                Name[4] = mContext.getString(R.string.Mixer_IN5);
                Name[5] = mContext.getString(R.string.Mixer_IN6);
                Name[6] = mContext.getString(R.string.Mixer_IN15);
                Name[7] = mContext.getString(R.string.Mixer_IN16);

                inputsourceName[0] = mContext.getString(R.string.High);
                inputsourceName[1] = mContext.getString(R.string.AUX);
                inputsourceName[2] = mContext.getString(R.string.Bluetooth);
                inputindex[0] = 1;
                inputindex[1] = 3;
                inputindex[2] = 2;

                DataStruct.CurMacMode.inputsource.Max = 3;
                DataStruct.CurMacMode.Mixer.MIXER_CH_MAX_USE = 8;
                DataStruct.CurMacMode.Delay.MAX = 350;
                DataStruct.CurMacMode.Out.OUT_CH_MAX_USE = 8;
                MacCfg.MixMax = 8;
                MacCfg.OutMax = 8;
                break;
            case 2://500
                spk_num = new int[]{6, 12, 15, 18, 0, 0, 0, 0, 0, 0, 22, 23, 0, 0, 0, 0};
                MacCfg.App_version = "APXF-BV1.00(DEQ-600ACH)(Beta)";  //APP版本
                MacCfg.MCU_Version = "MPXF-BV1.";  //500版本
                DataStruct.CurMacMode.MacTypeDisplay = "Pioneer DSP";//设备显示
                DataStruct.CurMacMode.Delay.MAX = 353;

                MacCfg.ButtomItemMaxUse = 5;


                Name[0] = mContext.getString(R.string.Mixer_HI_1);
                Name[1] = mContext.getString(R.string.Mixer_HI_2);
                Name[2] = mContext.getString(R.string.Mixer_HI_3);
                Name[3] = mContext.getString(R.string.Mixer_HI_4);
                Name[4] = mContext.getString(R.string.Mixer_HI_5);
                Name[5] = mContext.getString(R.string.Mixer_HI_6);
                Name[6] = mContext.getString(R.string.Mixer_HI_7);
                Name[7] = mContext.getString(R.string.Mixer_HI_8);

                Name[8] = mContext.getString(R.string.Mixer_HI_9);
                Name[9] = mContext.getString(R.string.Mixer_HI_10);
                Name[10] = mContext.getString(R.string.Mixer_IN5);
                Name[11] = mContext.getString(R.string.Mixer_IN6);
                Name[12] = mContext.getString(R.string.Mixer_IN13);
                Name[13] = mContext.getString(R.string.Mixer_IN14);
                Name[14] = mContext.getString(R.string.Mixer_IN15);
                Name[15] = mContext.getString(R.string.Mixer_IN16);
                inputsourceName[0] = mContext.getString(R.string.High);
                inputsourceName[1] = mContext.getString(R.string.AUX);
                inputsourceName[2] = mContext.getString(R.string.Bluetooth);
                inputsourceName[3] = mContext.getString(R.string.Digit);
                inputindex[0] = 1;
                inputindex[1] = 3;
                inputindex[2] = 2;
                inputindex[3] = 0;

                MacCfg.MixMax = 16;
                MacCfg.OutMax = 12;
                DataStruct.CurMacMode.inputsource.Max = 4;
                DataStruct.CurMacMode.Out.OUT_CH_MAX_USE = 12;
                DataStruct.CurMacMode.Mixer.MIXER_CH_MAX_USE = 16;
                break;

        }

        MacCfg.CurProID = 0;

        if (MacCfg.OutputChannelSel > MacCfg.OutMax) {
            MacCfg.OutputChannelSel = 0;
        }

        FillSPKType(spk_num);
        FillMixerName(Name);
        FillInputsource(inputsourceName, inputindex);

        DataOptUtil.initSystemDataStruct();


    }

    /**
     * 填充音源名字
     * index为对应的下标值
     */
    private static void FillInputsource(String[] Name, int[] index) {
        for (int i = 0; i < Name.length; i++) {
            DataStruct.CurMacMode.inputsource.Name[i] = Name[i];
        }
        for (int i = 0; i < index.length; i++) {
            DataStruct.CurMacMode.inputsource.inputsource[i] = index[i];

        }
    }

    /**
     * 填充通道类型
     */
    private static void FillSPKType(int[] spk_num) {
        for (int i = 0; i < spk_num.length; i++) {
            DataStruct.CurMacMode.Out.SPK_TYPE[i] = spk_num[i];
        }

    }

    /**
     * 填充混音名字
     */
    private static void FillMixerName(String[] Name) {
        for (int i = 0; i < Name.length; i++) {
            DataStruct.CurMacMode.Mixer.Name[i] = Name[i];
        }


    }


}
