package com.ziroom.creation.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ziroom.creation.R;
import com.ziroom.creation.utils.DeviceUtils;
import com.ziroom.creation.utils.DialogUtils;
import com.ziroom.creation.utils.ToastUtils;

import static com.ziroom.creation.base.Constant.PERMISSIONS_DENIED;
import static com.ziroom.creation.base.Constant.PERMISSIONS_REQUEST_CODE;

/**
 * 基础页面
 * Created by lmnrenbc on 2017/4/27.
 */

public abstract class BaseCompatAc extends AppCompatActivity {
    protected ImageView mActionBarBack;
    protected TextView mActionBarTitle;
    protected ImageView mActionBarSetting;
    protected TextView mActionBarRightText;
    private AlertDialog dialog;
    private ViewGroup inflate;
    private LinearLayout layout_content;
    private boolean showCry = false;
    private boolean isShowEmpty = false;
    protected Context mContext;

    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            savedInstanceState.putParcelable("android:support:fragments", null);
        }
        super.onCreate(savedInstanceState);

        super.setContentView(R.layout.base_compat_frame);
        layout_content = (LinearLayout) super.findViewById(R.id.frame_content);
        mContext = this;
        setOnReloadClickListener(new ReloadClickListener() {
            @Override
            public void onReloadClick() {
                reloadData();// 访问网络获取数据的方法
            }
        });
    }

    /**
     * 默认隐藏
     * 初始化标题栏
     */
    protected void initActionBar() {
        LinearLayout actionbar = (LinearLayout) findViewById(R.id.actionbar);
        actionbar.setVisibility(View.VISIBLE);
        mActionBarBack = (ImageView) findViewById(R.id.actionbar_back);
        mActionBarTitle = (TextView) findViewById(R.id.actionbar_title);
        mActionBarSetting = (ImageView) findViewById(R.id.actionbar_setting);
        mActionBarRightText = (TextView) findViewById(R.id.actionbar_right_text);
        mActionBarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void reloadData() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissDialog();
    }

    public void dismissDialog() {
        DialogUtils.closeProgressDialog();
    }


    /**
     * 点击空白处软键盘消失
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && this.getCurrentFocus() != null && this.getCurrentFocus().getWindowToken() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                //清除焦点
                v.clearFocus();
                return true;
            }
        }
        return false;
    }

    /**
     * 检查网络状态
     *
     * @return
     */
    public boolean checkNetwork() {
        boolean isNetworkAvailable = DeviceUtils.checkNet(mContext);
        if (!isNetworkAvailable) {
            ToastUtils.showToast(this, Constant.ERROR_NET);
            if (showCry && !isShowEmpty) {
                showEmptyView(inflate);
            }
        } else {
            if (showCry && isShowEmpty) {
                closeEmpty(inflate);
            }
        }
        return isNetworkAvailable;
    }

    public View showEmptyView(ViewGroup rootView) {
        return showEmptyView("加载失败了", "", false, rootView);
    }

    public View showEmptyView(boolean hide, ViewGroup rootView) {
        return showEmptyView("加载失败了", "", hide, rootView);
    }

    /**
     * 显示无信息提示 rootView 需要替换的viewgroup tips 无信息提示语
     */
    public View showEmptyView(String cause, String hint, boolean hide, ViewGroup rootView) {

        if (rootView == null) {
            return null;
        }
        isShowEmpty = true;
        // 清除之前的覆盖层
        View coverView = rootView.findViewById(R.id.ll_empty);
        if (coverView != null) {
            rootView.removeView(coverView);
        }
        LayoutInflater mInflater = LayoutInflater.from(this);
        int count = rootView.getChildCount();
        for (int i = 0; i < count; i++) {
            rootView.getChildAt(i).setVisibility(View.GONE);
        }
        View emptyView = mInflater.inflate(R.layout.empty_view, rootView, true);
        LinearLayout ll_reload = (LinearLayout) emptyView.findViewById(R.id.ll_reload);
        if (hide) {
            ll_reload.setVisibility(View.GONE);
        } else {
            ll_reload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onReloadClickListener.onReloadClick();
                }
            });
        }
        if (!TextUtils.isEmpty(cause)) {
            TextView tv_cause = (TextView) emptyView.findViewById(R.id.tv_cause);
            tv_cause.setText(cause);
        }
        if (!TextUtils.isEmpty(hint)) {
            TextView tv_hint = (TextView) emptyView.findViewById(R.id.tv_hint);
            tv_hint.setText(hint);
        }
        return emptyView;
    }

    /**
     * 重置无信息页面,恢复之前的界面
     */
    public void closeEmpty(ViewGroup rootView) {

        if (rootView == null) {
            return;
        }
        isShowEmpty = false;
        View coverView = rootView.findViewById(R.id.ll_empty);
        if (coverView != null) {
            rootView.removeView(coverView);
        }
        int count = rootView.getChildCount();
        for (int i = 0; i < count; i++) {
            if (!"personControl".equals(rootView.getChildAt(i).getTag())) {
                rootView.getChildAt(i).setVisibility(View.VISIBLE);
            }
        }
    }

    protected ReloadClickListener onReloadClickListener;

    public ReloadClickListener getOnReloadClickListener() {
        return onReloadClickListener;
    }

    public void setOnReloadClickListener(
            ReloadClickListener onReloadClickListener) {
        this.onReloadClickListener = onReloadClickListener;
    }

    public interface ReloadClickListener {
        void onReloadClick();
    }

    /**
     * 返回键->是否返回的dialog
     */
    protected void backDialog(final Context context, final String text) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_base, null);
        TextView tv_message = (TextView) view.findViewById(R.id.tv_message);
        Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        dialog = new AlertDialog.Builder(context, R.style.ThemeBaseDialog).create();
        dialog.show();
        dialog.getWindow().setContentView(view);
        tv_message.setText(text);
        btn_ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // 确定返回
                dialog.dismiss();
                finish();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // 取消的操作, 继续留在该界面
                dialog.dismiss();
            }
        });
    }

    /**
     * 自定义Dialog，需要操作实现接口即可
     *
     * @param context
     * @param text
     * @param impl
     */
    protected void baseDialog(final Context context, final String text, final BaseAc.BackDialogImpl impl) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_base, null);
        TextView tv_message = (TextView) view.findViewById(R.id.tv_message);
        Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        dialog = new AlertDialog.Builder(context, R.style.ThemeBaseDialog).create();
        dialog.show();
        dialog.getWindow().setContentView(view);
        tv_message.setText(text);
        btn_ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                impl.clickOk();
                dialog.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                impl.clickCancel();
                dialog.dismiss();
            }
        });
    }

    public void setContentView(int layoutResID) {
        inflate = (ViewGroup) getLayoutInflater().inflate(layoutResID, null);
        setContentView(inflate);
    }

    public void setContentView(View view) {
        if (layout_content != null && inflate != null) {
            layout_content.removeAllViews();
            inflate.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            layout_content.addView(inflate);
        } else {
            super.setContentView(view);
        }
    }

    public void setShowCry(boolean showCry) {
        this.showCry = showCry;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == PERMISSIONS_REQUEST_CODE && resultCode == PERMISSIONS_DENIED) {
            return;
        }
    }

}