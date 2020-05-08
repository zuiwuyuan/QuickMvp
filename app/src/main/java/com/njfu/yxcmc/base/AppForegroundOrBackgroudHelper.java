package com.njfu.yxcmc.base;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.apkfuns.logutils.LogUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * ..应用前后台状态监听帮助类，仅在Application中使用
 */

class AppForegroundOrBackgroudHelper {

    private OnAppStatusListener mOnAppStatusListener;
    private Set<Activity> mActivitySet = new HashSet<>();
    private final Activity[] currentActivity = new Activity[1];

    AppForegroundOrBackgroudHelper() {
    }

    /**
     * 注册状态监听，仅在Application中使用
     *
     * @param application application
     * @param listener    listener
     */
    void register(Application application, OnAppStatusListener listener) {
        mOnAppStatusListener = listener;
        application.registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
    }

    void unRegister(Application application) {
        application.unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks);
    }

    private Application.ActivityLifecycleCallbacks activityLifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {
        //打开的Activity数量统计
        private int activityStartCount = 0;

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            mActivitySet.add(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {
            activityStartCount++;
            //数值从0变到1说明是从后台切到前台
            if (activityStartCount == 1) {
                //从后台切到前台
                if (mOnAppStatusListener != null) {
                    mOnAppStatusListener.onFront(activity);
                }
            }
        }

        @Override
        public void onActivityResumed(Activity activity) {
            currentActivity[0] = activity;
        }

        @Override
        public void onActivityPaused(Activity activity) {
        }

        @Override
        public void onActivityStopped(Activity activity) {
            if (activity == currentActivity[0]) currentActivity[0] = null;
            activityStartCount--;
            //数值从1到0说明是从前台切到后台
            if (activityStartCount == 0) {
                //从前台切到后台
                if (mOnAppStatusListener != null) {
                    mOnAppStatusListener.onBack(activity);
                }
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            try {
                mActivitySet.remove(activity);
            } catch (Exception e) {
                LogUtils.e(e);
            }
        }
    };

    void exitApp() {
        for (Activity activity : mActivitySet) {
            activity.finish();
        }
    }

    interface OnAppStatusListener {
        void onFront(Activity activity);

        void onBack(Activity activity);
    }

    boolean isAppRunning() {
        return !mActivitySet.isEmpty();
    }

    int getActivityCount() {
        return mActivitySet.size();
    }

    @Nullable
    Activity getCurrentActivity() {
        return currentActivity[0];
    }
}
