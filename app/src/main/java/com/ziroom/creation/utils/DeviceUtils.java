package com.ziroom.creation.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ziroom.creation.R;
import com.ziroom.creation.permission.PermissionsChecker;


/**
 * Created by lmnrenbc on 2017/11/25.
 * 设备工具类
 */

public class DeviceUtils {

    /* 获取App版本
     * 获取App版本
     * @param mContext
     * @return
     */
    public static String getAppVersion(Context mContext) {
        PackageInfo info = null;
        PackageManager manager = mContext.getPackageManager();
        try {
            info = manager.getPackageInfo(mContext.getPackageName(), 0);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return info.versionCode + "";
    }

    /*
     * 获取手机的唯一设备号
     */
    public static String getDeviceId(Context context) {

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String phone_imei = telephonyManager.getDeviceId();
        return phone_imei;
    }

    /*
     * 检查网络是否可用
     */
    public static boolean checkNet(Context context) {
        if (context == null) {
            return false;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 拨打电话
     *
     * @param phone 联系方式
     */
    public static void callPhone(final Context context, final String phone) {
        if (PermissionsChecker.checkPhonePermission((Activity) context)) {
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_base, null);
            TextView tv_message = (TextView) view.findViewById(R.id.tv_message);
            Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
            btn_ok.setText("呼叫");
            Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);

            final AlertDialog dialog = new AlertDialog.Builder(context, R.style.ThemeBaseDialog).create();
            dialog.show();
            dialog.getWindow().setContentView(view);
            tv_message.setText(phone);
            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                    context.startActivity(phoneIntent);
                }
            });
            btn_cancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
    }
}
