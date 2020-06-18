package com.njfu.yxcmc.contract;


import com.njfu.yxcmc.base.BaseView;
import com.njfu.yxcmc.bean.UserModel;


public interface LoginContract {

    interface View extends BaseView {

        void onLoginSuccess(UserModel userModel);

    }

    interface Presenter {

        void login(String username, String password);

    }
}
