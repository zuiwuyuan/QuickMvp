package com.njfu.yxcmc.contract;

import com.njfu.yxcmc.base.BaseView;
import org.json.JSONObject;

public interface SplashContract {

    interface View extends BaseView {
        void onSuccess(JSONObject upgradeObj);
    }

    interface Presenter {
        void checkUpdate();
    }
}
