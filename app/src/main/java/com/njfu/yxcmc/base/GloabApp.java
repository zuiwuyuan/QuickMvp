package com.njfu.yxcmc.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.androidnetworking.AndroidNetworking;
import com.apkfuns.logutils.LogUtils;
import com.njfu.yxcmc.common.CmssSharedPreferences;
import com.njfu.yxcmc.common.GlobalField;
import com.njfu.yxcmc.net.RetrofitClient;

import androidx.annotation.Nullable;

public class GloabApp extends Application {

    private static GloabApp instance;

    private GlobalField mGlobalField;

    private CmssSharedPreferences mPrefs;

    protected AppForegroundOrBackgroudHelper mAppStatusHelper = new AppForegroundOrBackgroudHelper();

    private Context mContext;

    public static synchronized GloabApp getInstance() {
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

        mAppStatusHelper.register(this, new AppForegroundOrBackgroudHelper.OnAppStatusListener() {
            @Override
            public void onFront(Activity activity) {
                //应用切到前台处理
                if (mGlobalField == null || TextUtils.isEmpty(mGlobalField.getPatternLock())) {
                    return;
                }
//                if (activity instanceof MainPageActivity
//                        || activity instanceof FullScreenContainerActivity
//                        || activity instanceof ContainerActivity
//                        || activity instanceof FullScreenLandscapeContainerActivity) {
//                    if (((BaseCompatActivity) activity).isAutoCheckPatternLock()) {
//                        isCheckPatternLock = true;
//                        Bundle bundle = new Bundle();
//                        bundle.putInt("mode_type", ActivityPatternLock.MODE_TYPE.AUTHORISE.ordinal());
//                        Intent intent = new Intent(AiApplication.this, ActivityPatternLock.class);
//                        intent.putExtras(bundle);
//                        activity.startActivity(intent);
//                    } else {
//                        ((BaseCompatActivity) activity).setAutoCheckPatternLock(true);
//                    }
//                }
            }

            @Override
            public void onBack(Activity activity) {
                //应用切到后台处理
            }
        });

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
        mAppStatusHelper.exitApp();
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

    public boolean isAppRunning() {
        return mAppStatusHelper.isAppRunning();
    }

    public int getActivityCount() {
        return mAppStatusHelper.getActivityCount();
    }

    @Nullable
    public Activity getCurrentActivity() {
        return mAppStatusHelper.getCurrentActivity();
    }
}
