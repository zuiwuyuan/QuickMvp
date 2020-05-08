package com.njfu.yxcmc.presenter;


import android.app.Activity;

import com.njfu.yxcmc.base.BasePresenter;
import com.njfu.yxcmc.contract.SplashContract;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SplashPresenter extends BasePresenter<SplashContract.View> implements SplashContract.Presenter {

    private Activity mContext;

    public SplashPresenter() {

    }

    public SplashPresenter(Activity activity) {
        this.mContext = activity;
    }

    @Override
    public void checkUpdate() {
        //View是否绑定 如果没有绑定，就不执行网络请求
        if (!isViewAttached()) {
            return;
        }

        mView.showLoading();
        Observable.timer(2, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()) // timer 默认在新线程，所以需要切换回主线程
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {

                        mView.hideLoading();
                        mView.onSuccess(null);
                    }
                });
    }

}
