package com.ziroom.creation.permission;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.ziroom.creation.R;

import static com.ziroom.creation.base.Constant.PERMISSIONS_EXTRA;
import static com.ziroom.creation.base.Constant.PERMISSIONS_DENIED;
import static com.ziroom.creation.base.Constant.PERMISSIONS_GRANTED;

/**
 * 权限监测、申请页面
 * Created by lmnrenbc on 2017/11/25.
 */

public class PermissionsAc extends AppCompatActivity implements PermissionsContract.View {

    private PermissionsContract.Presenter mPresenter;

    private static final int PERMISSION_REQUEST_CODE = 0; // 系统权限管理页面的参数
    private static final String PACKAGE_URL_SCHEME = "package:"; // 方案

    private String[] permissions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() == null || !getIntent().hasExtra(PERMISSIONS_EXTRA)) {
            throw new RuntimeException("PermissionsActivity需要使用静态startActivityForResult方法启动!");
        }

        new PermissionsPresenter(this);
        setContentView(R.layout.ac_permissions);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.start();
        mPresenter.checkPermissions();
    }

    // 启动当前权限页面的公开接口
    public static void startActivityForResult(Activity activity, int requestCode, String... permissions) {
        Intent intent = new Intent(activity, PermissionsAc.class);
        intent.putExtra(PERMISSIONS_EXTRA, permissions);
        ActivityCompat.startActivityForResult(activity, intent, requestCode, null);
    }

    @Override
    public void checkPermissions() {
        permissions = getPermissions();
        if (PermissionsChecker.lacksPermissions(this, permissions)) {
            requestPermissions(permissions);
        } else {
            allPermissionsGranted();
        }
    }

    // 返回传递的权限参数
    private String[] getPermissions() {
        return getIntent().getStringArrayExtra(PERMISSIONS_EXTRA);
    }

    // 请求权限兼容低版本
    private void requestPermissions(String... permissions) {
        ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
    }


    /**
     * 用户权限处理,
     * 如果全部获取, 则直接过.
     * 如果权限缺失, 则提示Dialog.
     *
     * @param requestCode  请求码
     * @param permissions  权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE && mPresenter.hasAllPermissionsGranted(grantResults)) {
            mPresenter.setRequireCheck(true);
            allPermissionsGranted();
        } else {
            mPresenter.setRequireCheck(false);
            showMissingPermissionDialog();
        }
    }

    // 全部权限均已获取
    private void allPermissionsGranted() {
        Intent intent = new Intent();
        if (permissions != null && permissions.length > 0) {
            intent.putExtra(PERMISSIONS_EXTRA, permissions);
        }
        setResult(PERMISSIONS_GRANTED, intent);
        finish();
    }

    // 显示缺失权限提示
    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PermissionsAc.this);
        builder.setTitle(R.string.wx_permission_help);
        builder.setMessage(R.string.wx_permission_declare);

        // 拒绝, 退出应用
        builder.setNegativeButton(R.string.wx_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setResult(PERMISSIONS_DENIED);
                finish();
            }
        });

        builder.setPositiveButton(R.string.wx_setting, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startAppSettings();
            }
        });

        builder.show();
    }

    // 启动应用的设置
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse(PACKAGE_URL_SCHEME + getPackageName()));
        startActivity(intent);
    }

    @Override
    public void setPresenter(PermissionsContract.Presenter presenter) {
        mPresenter = presenter;
    }
}