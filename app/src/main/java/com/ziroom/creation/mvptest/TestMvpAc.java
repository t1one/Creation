package com.ziroom.creation.mvptest;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ziroom.creation.R;
import com.ziroom.creation.base.BaseCompatAc;
import com.ziroom.creation.utils.ToastUtils;

public class TestMvpAc extends BaseCompatAc implements View.OnClickListener, TestMvpContract.View {
    private TestMvpContract.Presenter mPresenter;
    private TextView tv_result;
    private EditText et_1;
    private EditText et_2;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_test_mvp);
        new TestMvpPresenter(this);
        initActionBar();
        initView();
        setListener();
    }

    private void setListener() {
        btn.setOnClickListener(this);
    }

    private void initView() {
        tv_result = findViewById(R.id.tv_result);
        et_1 = findViewById(R.id.et_1);
        et_2 = findViewById(R.id.et_2);
        btn = findViewById(R.id.btn);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn:
                try {
                    mPresenter.getResult(Integer.parseInt(et_1.getText().toString()),
                            Integer.parseInt(et_2.getText().toString()));
                } catch (Exception e) {
                    ToastUtils.showToast(mContext, "请输入整数后计算");
                }
                break;
        }
    }

    @Override
    public void setResult(long result) {
        tv_result.setText("" + result);
    }

    @Override
    public void setPresenter(TestMvpContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
