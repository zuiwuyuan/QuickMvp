package com.njfu.yxcmc.base;

import com.njfu.yxcmc.net.APIService;
import com.njfu.yxcmc.net.RetrofitClient;
import com.njfu.yxcmc.net.base.BaseSubscriber;
import com.njfu.yxcmc.net.util.ParamsBuilder;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class BasePresenter<V extends BaseView> {

    private CompositeDisposable compositeDisposable;

    protected V mView;

    protected APIService apiService = RetrofitClient.getInstance().getServiceRxCustomGson();

    public void addDisposable(Observable<?> observable, BaseSubscriber observer) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer));
    }

    public void removeDisposable() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
    }

    protected ParamsBuilder getParamsBuilder() {
        return new ParamsBuilder();
    }

    /**
     * 绑定view，一般在初始化中调用该方法
     *
     * @param view view
     */
    public void attachView(V view) {
        this.mView = view;
    }

    /**
     * 解除绑定view，一般在onDestroy中调用
     */

    public void detachView() {
        this.mView = null;
        removeDisposable();
    }

    /**
     * View是否绑定
     *
     * @return
     */
    public boolean isViewAttached() {
        return mView != null;
    }
}
