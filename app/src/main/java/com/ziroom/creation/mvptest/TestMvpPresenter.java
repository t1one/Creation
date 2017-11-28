package com.ziroom.creation.mvptest;

/**
 * Created by lmnrenbc on 2017/11/28.
 */

public class TestMvpPresenter implements TestMvpContract.Presenter {

    private final TestMvpContract.View mView;

    public TestMvpPresenter(TestMvpContract.View testMvpView) {
        mView = testMvpView;
        mView.setPresenter(this);
    }

    @Override
    public void getResult(int param1, int param2) {
        mView.setResult(param1 * param2);
    }

    @Override
    public void start() {

    }
}
