package com.chs.mt.xf_dap.updateApp;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;

import com.chs.mt.xf_dap.R;

import java.io.File;

/**
 * Created by Administrator on 2017/12/8.
 */

public class UpdateUtil {
    /**
     * 下载文件
     * @param context
     */
    public static void downLoadApp(Context context,String downurl,boolean wifi){
        //downurl:下载app的后台地址
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downurl));
        //  下载时的网络状态，默认是wifi和移动网络都可以下载，如果选择一个，只能在选中的状态下进行下载
        if(wifi){
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        }else {
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE);
        }

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setTitle(context.getResources().getString(R.string.app_name)+
                context.getResources().getString(R.string.AppUpdate));
        request.setDescription(context.getResources().getString(R.string.app_name)+
                context.getResources().getString(R.string.AppDownloading));
        request.setAllowedOverRoaming(false);
        //设置文件存放目录
        //判断文件是否存在，保证其唯一性
        File file = context.getExternalFilesDir("Download/app");
        if(file.exists()){
            file.delete();
        }
        request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, "downapp");
        DownloadManager downManager = (DownloadManager)context.getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
        long id = downManager.enqueue(request);
        // 存储下载Key
        SharedPreferences sharedPreferences = context.getSharedPreferences("downloadapp", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("downloadid",id);
        editor.commit();
    }

    /**
     * 判断是否是wifi连接
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (null == cm) {
            return false;
        }

        NetworkInfo info = cm.getActiveNetworkInfo();
        if (null != info) {
            if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            }
        }
        return false;

    }

    /**
     * 判断网络情况
     * @param context 上下文
     * @return false 表示没有网络 true 表示有网络
     */
    public static boolean isNetworkAvalible(Context context) {
        // 获得网络状态管理器
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 建立网络数组
            NetworkInfo[] net_info = connectivityManager.getAllNetworkInfo();

            if (net_info != null) {
                for (int i = 0; i < net_info.length; i++) {
                    // 判断获得的网络状态是否是处于连接状态
                    if (net_info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
