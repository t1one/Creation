package com.ziroom.creation.permission;

import android.content.pm.PackageManager;

/**
 * 权限页面业务控制器
 * Created by lmnrenbc on 2017/11/25.
 */

public class PermissionsPresenter implements PermissionsContract.Presenter {

    //权限视图层
    private final PermissionsContract.View mView;

    private boolean isRequireCheck; // 是否需要系统权限检测

    public PermissionsPresenter(PermissionsContract.View permissionsView) {
        mView = permissionsView;
        mView.setPresenter(this);
    }

    @Override
    public boolean hasAllPermissionsGranted(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void setRequireCheck(boolean require) {
        isRequireCheck = require;
    }

    @Override
    public void checkPermissions() {
        if (isRequireCheck) {
            mView.checkPermissions();
        } else {
            isRequireCheck = true;
        }
    }

    @Override
    public void start() {
        isRequireCheck = true;
    }
}