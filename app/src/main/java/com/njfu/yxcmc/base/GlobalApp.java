package com.njfu.yxcmc.base;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.androidnetworking.AndroidNetworking;
import com.apkfuns.logutils.LogUtils;
import com.njfu.yxcmc.common.CmssSharedPreferences;
import com.njfu.yxcmc.common.GlobalField;
import com.njfu.yxcmc.net.RetrofitClient;

public class GlobalApp extends Application {

    private static GlobalApp instance;

    private GlobalField mGlobalField;

    private CmssSharedPreferences mPrefs;

    private Context mContext;

    public static synchronized GlobalApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        mContext = getApplicationContext();

//        LeakCanary.install(this);
        // Adding an Network Interceptor for Debugging purpose :

        AndroidNetworking.initialize(mContext, RetrofitClient.getAndroidNetworkingClient());

        mGlobalField = new GlobalField();

        mPrefs = CmssSharedPreferences.getInstance(this);

        LogUtils.getLogConfig()
                .configAllowLog(true)
                .configTagPrefix("quick-mvp-")
                .configShowBorders(false)
                .configFormatTag("%d{HH:mm:ss:SSS} %t %c{-5}");

    }

    public Context getContext() {
        return mContext;
    }

    public GlobalField getGlobalField() {
        return mGlobalField;
    }

    public void resetGlobalField() {
        mGlobalField = new GlobalField();
    }

    public CmssSharedPreferences getPrefs() {
        return mPrefs;
    }

    public void exitApp() {
        mGlobalField = new GlobalField();
    }

    public void restartApp() {
        exitApp();
        PackageManager pm = getPackageManager();
        Intent appLaunchIntent = pm.getLaunchIntentForPackage(getPackageName());
        if (appLaunchIntent != null) {
            startActivity(appLaunchIntent);
        }
    }

}
