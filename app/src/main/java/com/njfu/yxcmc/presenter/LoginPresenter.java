package com.njfu.yxcmc.presenter;


import android.app.Activity;

import com.njfu.yxcmc.base.BasePresenter;
import com.njfu.yxcmc.bean.UserModel;
import com.njfu.yxcmc.contract.LoginContract;
import com.njfu.yxcmc.net.base.BaseSubscriber;

public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter {

    private Activity mContext;

    public LoginPresenter() {

    }

    public LoginPresenter(Activity activity) {
        this.mContext = activity;
    }

    @Override
    public void login(String username, String password) {
        //View是否绑定 如果没有绑定，就不执行网络请求
        if (!isViewAttached()) {
            return;
        }

        addDisposable(apiService.login(username, password), new BaseSubscriber<UserModel>(mView, true) {

            @Override
            public void onSuccess(UserModel data) {
                mView.onLoginSuccess(data);
            }

            @Override
            public void onError(String msg) {

            }
        });
    }

}
