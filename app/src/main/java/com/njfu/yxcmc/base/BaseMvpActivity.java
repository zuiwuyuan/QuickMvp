package com.njfu.yxcmc.base;

import android.os.Bundle;

import com.apkfuns.logutils.LogUtils;
import com.njfu.yxcmc.net.APIService;
import com.njfu.yxcmc.net.RemoteApi;
import com.njfu.yxcmc.net.RetrofitClient;
import com.njfu.yxcmc.net.util.ParamUtils;
import com.njfu.yxcmc.widget.dialog.ThemeAlertDialog;
import com.rx2androidnetworking.Rx2AndroidNetworking;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.AutoDisposeConverter;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public abstract class BaseMvpActivity<T extends BasePresenter> extends BaseActivity implements BaseView {

    protected T mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void showError(String msg) {
        toastMsg(msg);
    }

    @Override
    public void onErrorCode(int code, String msg) {
        if (-2 == code && msg != null && msg.contains("重新登录")) {

            ThemeAlertDialog.showAlert(this, "提示", msg,
                    false, "取消", "确定", new ThemeAlertDialog.OnAlertAction() {
                        @Override
                        public void onLeftClick() {
                            GlobalApp.getInstance().exitApp();
                        }

                        @Override
                        public void onRightClick() {
                            GlobalApp.getInstance().restartApp();
                        }
                    });
        } else {
            toastMsg(msg);
        }
    }

    /**
     * 访问统计日志记录接口
     *
     * @param typeCode 类型
     * @param itemId   id
     */
    protected void addVisitRecordToRemote(String typeCode, String itemId) {

        try {
            JSONObject param = ParamUtils.getJsonCommonParams();

            param.put("resourceType", typeCode);
            param.put("menuCode", itemId);

            Rx2AndroidNetworking.post(RemoteApi.getHostBasePathUrl() + APIService.HOME_INSERT_VISIT_LOG)
                    .addJSONObjectBody(param)
                    .addHeaders("Authorization", RetrofitClient.getAuthorization())
                    .addHeaders("Content-Type", "application/json;charset=UTF-8")
                    .build()
                    .getJSONObjectFlowable()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<JSONObject>() {
                        @Override
                        public void accept(JSONObject dataObj) throws Exception {
                        }
                    });

        } catch (JSONException e) {
            LogUtils.e(e);
        }
    }

    @Override
    public void showLoading() {
        showLoadingDialog(null, 0);
    }

    @Override
    public void hideLoading() {
        dismissLoadingDialog();
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        super.onDestroy();
    }

    /**
     * 绑定生命周期 防止MVP内存泄漏
     *
     * @param <T>
     * @return
     */
    @Override
    public <T> AutoDisposeConverter<T> bindAutoDispose() {
        return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider
                .from(this, Lifecycle.Event.ON_DESTROY));
    }

}
