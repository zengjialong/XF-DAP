package com.chs.mt.xf_dap.operation;

import android.app.Activity;
import android.app.Application;

import java.util.ArrayList;
import java.util.List;

public class SysApplication extends Application {
    public static List<Activity> activities = new ArrayList<>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
        activities.clear();
    }

//关闭应用
    public static void exitApp(){
        finishAll();
        android.os.Process.killProcess(android.os.Process.myPid());  ///杀死所有的进程
    }
}
