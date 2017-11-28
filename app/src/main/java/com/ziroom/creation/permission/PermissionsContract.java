package com.ziroom.creation.permission;

import com.ziroom.creation.base.BasePresenter;
import com.ziroom.creation.base.BaseView;

/**
 * 权限页面与业务控制器合约类
 * Created by lmnrenbc on 2017/11/25.
 */

public interface PermissionsContract {

    interface View extends BaseView<Presenter> {

        /**
         * 检察权限
         */
        void checkPermissions();
    }


    interface Presenter extends BasePresenter {

        /**
         * 是否全部的权限均通过授权
         *
         * @param grantResults
         * @return
         */
        boolean hasAllPermissionsGranted(int[] grantResults);

        /**
         * 设置是否需要检察权限
         *
         * @param require
         */
        void setRequireCheck(boolean require);

        /**
         * 需要检察权限
         */
        void checkPermissions();

    }
}
