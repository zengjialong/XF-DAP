package com.chs.mt.xf_dap.application;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

public class CrashApp extends Application {

    public static final String TAG = "CrashApp";

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        new Handler(getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Looper.loop();//try-catch主线程的所有异常；Looper.loop()内部是一个死循环，出现异常时才会退出，所以这里使用while(true)。
                    } catch (Throwable e) {
                        Log.d(TAG, "Looper.loop(): " + e.getMessage());
                    }
                }
            }
        });

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {//try-catch子线程的所有异常。
                Log.d(TAG, "UncaughtExceptionHandler: " + e.getMessage());
            }
        });

    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
