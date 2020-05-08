package com.njfu.yxcmc.net.util;

import android.text.TextUtils;

import com.apkfuns.logutils.LogUtils;

import okhttp3.logging.HttpLoggingInterceptor;

public class HttpLogger implements HttpLoggingInterceptor.Logger {
    @Override
    public void log(String message) {
        if (!TextUtils.isEmpty(message)) {
            if (message.contains("POST http") || message.startsWith("GET http")) {
                LogUtils.e(message);
            }
            if (message.startsWith("{") || message.startsWith("[")) {
                LogUtils.e(message);
            }
        }
    }
}