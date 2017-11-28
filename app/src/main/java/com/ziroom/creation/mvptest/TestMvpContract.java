package com.ziroom.creation.mvptest;

import com.ziroom.creation.base.BasePresenter;
import com.ziroom.creation.base.BaseView;

/**
 * Created by lmnrenbc on 2017/11/28.
 */

public interface TestMvpContract {
    interface View extends BaseView<Presenter> {
        void setResult(long result);

    }

    interface Presenter extends BasePresenter {
        void getResult(int param1, int param2);

    }
}
