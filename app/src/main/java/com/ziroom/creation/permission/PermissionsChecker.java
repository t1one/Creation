package com.ziroom.creation.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.ziroom.creation.base.Constant;

/**
 * 监测权限（6.0系统以上使用）
 * Created by lmnrenbc on 2016/9/22.
 */

public class PermissionsChecker {

    // 判断权限集合
    public static boolean lacksPermissions(Context context, String... permissions) {
        for (String permission : permissions) {
            if (lacksPermission(context, permission)) {
                return true;
            }
        }
        return false;
    }

    // 判断是否缺少权限
    public static boolean lacksPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) ==
                PackageManager.PERMISSION_DENIED;
    }

    /**
     * 监察是否具备权限
     *
     * @param activity
     * @param permissions
     * @return
     */
    public static boolean checkPermission(Activity activity, String... permissions) {
        // 缺少权限时, 进入权限配置页面
        if (lacksPermissions(activity.getApplicationContext(), permissions)) {
            startPermissionsActivity(activity, permissions);
            return false;
        } else {
            return true;
        }
    }

    /**
     * 监察是否具备权限
     *
     * @param permissions
     * @return
     */
    public static boolean checkPermission(Fragment fragment, String... permissions) {
        // 缺少权限时, 进入权限配置页面
        if (lacksPermissions(fragment.getContext(), permissions)) {
            fragment.requestPermissions(permissions, Constant.PERMISSIONS_REQUEST_CODE);
            return false;
        } else {
            return true;
        }
    }

    public static void startPermissionsActivity(Activity activity, String... permissions) {
        PermissionsAc.startActivityForResult(activity, Constant.PERMISSIONS_REQUEST_CODE, permissions);
    }

    /**
     * 日历权限判断
     */
    public static boolean checkCalendarPermission(Activity activity) {
        return PermissionsChecker.checkPermission(activity, Manifest.permission.READ_CALENDAR);
    }

    /**
     * 相机权限判断
     */
    public static boolean checkCameraPermission(Activity activity) {
        return PermissionsChecker.checkPermission(activity, Manifest.permission.CAMERA);
    }

    /**
     * 电话权限判断
     */
    public static boolean checkPhonePermission(Activity activity) {
        return PermissionsChecker.checkPermission(activity, Manifest.permission.CALL_PHONE);
    }

    /**
     * 读写权限判断
     */
    public static boolean checkStoragePermission(Activity activity) {
        // 缺少读写权限时, 进入权限配置页面
        return PermissionsChecker.checkPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    /**
     * 定位权限判断
     */
    public static boolean checkLocationPermission(Activity activity) {
        // 缺少读写权限时, 进入权限配置页面
        return PermissionsChecker.checkPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
    }

    /**
     * 电话权限判断
     */
    public static boolean checkStatePermission(Activity activity) {
        // 缺少读写权限时, 进入权限配置页面
        return PermissionsChecker.checkPermission(activity, Manifest.permission.READ_PHONE_STATE);
    }

    /**
     * 电话权限判断
     */
    public static boolean checkStatePermission(Fragment fragment) {
        // 缺少读写权限时, 进入权限配置页面
        return PermissionsChecker.checkPermission(fragment, Manifest.permission.READ_PHONE_STATE);
    }

}