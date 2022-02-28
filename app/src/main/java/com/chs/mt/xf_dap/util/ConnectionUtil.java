package com.chs.mt.xf_dap.util;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;



public class ConnectionUtil {

    /*
     * 判断网络连接是否已开
     * 2012-08-20
     *true 已打开  false 未打开
     * */
    public static boolean isConn(Context context){
        boolean bisConnFlag=false;
        ConnectivityManager conManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = conManager.getActiveNetworkInfo();
        if(network!=null){
            bisConnFlag=conManager.getActiveNetworkInfo().isAvailable();
        }
      // System.out.println("BUG 网络转态"+network+"=-="+bisConnFlag);
        return bisConnFlag;
    }
    public static boolean isOpen(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        //判断GPS是否开启，没有开启，则开启
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return false;
        }
        return true;
    }



}
