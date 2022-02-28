package com.chs.mt.xf_dap.main;

import java.text.DecimalFormat;

public class TextMain {
    public static void main(String[] args) {
        float str=Float.valueOf("3.000");
        double num=((int)(str *1000))/1000.0;
        DecimalFormat df=new DecimalFormat("###.00");
        String val=df.format(num);
       System.out.println(num+"=="+val);
    }

}
