package com.ziroom.creation.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;


import com.ziroom.creation.mvptest.TestMvpAc;
import com.ziroom.creation.loader.ImageLoader;
import com.ziroom.creation.utils.LogUtils;

import java.lang.ref.WeakReference;
import java.util.HashMap;


/**
 * 应用application
 * Created by lmnrenbc on 2017/11/25.
 */
public class CreationApplication extends Application implements Thread.UncaughtExceptionHandler {

    private static final String TAG = "CreationApplication";
    private HashMap<String, WeakReference<Activity>> mActivityList = new HashMap<String, WeakReference<Activity>>();
    private static CreationApplication instance = null;
    /**
     * 是否发布版，true为发布版
     */
    public static final boolean isRelease = false;
    public static final boolean analogData = false;

    /**
     * 发布版本类型，0：测试环境，1:AMI环境，2：准生产，3：生产
     */
    public static final int versionType = 1;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //设置Thread Exception Handler
        Thread.setDefaultUncaughtExceptionHandler(this);
        //初始化图片加载框架
        ImageLoader.getInstance().initImageLoader();
    }

    public static CreationApplication getInstance() {
        return instance;
    }

    /**
     * 创建Ac后，添加到集合中统一管理
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        if (null != activity) {
            LogUtils.e("add Activity ", activity.getClass().getName());
            mActivityList.put(activity.getClass().getName(), new WeakReference<>(activity));
        }
    }

    /**
     * 退出Ac后，从集合中移除
     *
     * @param activity
     */
    public void removeActivity(Activity activity) {
        if (null != activity) {
            LogUtils.e("remove Activity ", activity.getClass().getName());
            WeakReference<Activity> remove = mActivityList.remove(activity.getClass().getName());
            activity.finish();
        }
    }

    /**
     * 退出应用程序
     */
    public void AppExit() {
        try {
            for (String key : mActivityList.keySet()) {
                WeakReference<Activity> activity = mActivityList.get(key);
                if (activity != null && activity.get() != null) {
                    LogUtils.e("Exit", activity.get().getClass().getSimpleName());
                    activity.get().finish();
                }
            }
            mActivityList.clear();
            ActivityManager activityMgr = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.restartPackage(this.getPackageName());
            System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {
        }
    }

    @Override
    public void uncaughtException(final Thread t, Throwable e) {
        LogUtils.e("TAG", "uncaughtException", e);
        e.printStackTrace();
        Intent intent = new Intent();
        intent.setClass(CreationApplication.getInstance(), TestMvpAc.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CreationApplication.getInstance().startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}