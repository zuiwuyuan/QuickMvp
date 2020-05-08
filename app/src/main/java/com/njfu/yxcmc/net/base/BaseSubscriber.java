package com.njfu.yxcmc.net.base;

import com.google.gson.JsonParseException;
import com.njfu.yxcmc.base.BaseView;

import org.json.JSONException;

import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;

import javax.net.ssl.SSLHandshakeException;

import io.reactivex.observers.DisposableObserver;
import retrofit2.HttpException;

/**
 * 作者： ch
 * 时间： 2019/11/21 14:05
 * 描述：
 * 来源：
 */
public abstract class BaseSubscriber<T> extends DisposableObserver<T> {

    protected BaseView baseView;

    private boolean isShowDialog;

    public BaseSubscriber(BaseView view) {
        this.baseView = view;
    }

    public BaseSubscriber(BaseView view, boolean isShowDialog) {
        this.baseView = view;
        this.isShowDialog = isShowDialog;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (baseView != null && isShowDialog) {
            baseView.showLoading();
        }
    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    @Override
    public void onError(Throwable e) {
        if (baseView != null && isShowDialog) {
            baseView.hideLoading();
        }
        BaseException be = null;

        if (e != null) {

            if (e instanceof BaseException) {
                be = (BaseException) e;

                //回调到view层 处理 或者根据项目情况处理（比如token过期，重新登录）
                if (baseView != null) {
                    baseView.onErrorCode(be.getErrorCode(), be.getErrorMsg());
                } else {
                    onError(be.getErrorMsg());
                }

            } else {
                if (e instanceof HttpException) {
                    //   HTTP错误
                    be = new BaseException(BaseException.BAD_NETWORK_MSG, e, BaseException.BAD_NETWORK);
                } else if (e instanceof SocketTimeoutException || e instanceof InterruptedIOException) {
                    //  连接超时
                    be = new BaseException(BaseException.CONNECT_TIMEOUT_MSG, e, BaseException.CONNECT_TIMEOUT);
                } else if (e instanceof SSLHandshakeException) {
                    //  证书错误
                    be = new BaseException(BaseException.CONNECT_TIMEOUT_MSG, e, BaseException.HTTPS_HANDSHAKE_FAILED);
                } else if (e instanceof JsonParseException || e instanceof JSONException || e instanceof ParseException) {
                    //  解析错误
                    be = new BaseException(BaseException.PARSE_ERROR_MSG, e, BaseException.PARSE_ERROR);
                } else if (e instanceof UnknownHostException || e instanceof ConnectException || e instanceof SocketException) {
                    //   连接错误
                    be = new BaseException(BaseException.CONNECT_ERROR_MSG, e, BaseException.CONNECT_ERROR);
                } else {
                    be = new BaseException(BaseException.OTHER_MSG, e, BaseException.OTHER);
                }
            }
        } else {
            be = new BaseException(BaseException.OTHER_MSG, e, BaseException.OTHER);
        }

        onError(be.getErrorMsg());
    }

    @Override
    public void onComplete() {
        if (baseView != null && isShowDialog) {
            baseView.hideLoading();
        }
    }

    public abstract void onSuccess(T data);

    public abstract void onError(String msg);
}
