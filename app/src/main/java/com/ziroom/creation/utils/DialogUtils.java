package com.ziroom.creation.utils;

import android.app.ProgressDialog;
import android.content.Context;

import com.ziroom.creation.base.Constant;

/**
 * 加载框
 * Created by lmnrenbc on 2017/11/26.
 */
public class DialogUtils {

    private static ProgressDialog mProgressDialog;
    private static Context mContext;

    /**
     * 显示加载框
     *
     * @param context
     */
    public static void showProgressDailog(Context context) {
        showProgressDailog(context, true);
    }

    public static void showProgressDailog(Context context, boolean cancelable) {
        if (!context.equals(mContext)) {
            //是新页面
            closeProgressDialog();
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setProgress(ProgressDialog.STYLE_SPINNER);//圆形
            mProgressDialog.setMessage(Constant.DATA_LOADING);
            mProgressDialog.setCancelable(cancelable);
            mProgressDialog.show();
            mContext = context;
        } else {
            //是已有页面，则重新显示
            if (mProgressDialog != null && !mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }
        }
    }

    public static void closeProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
