package com.njfu.yxcmc.net.util;

import android.os.Build;
import android.text.TextUtils;

import com.njfu.yxcmc.BuildConfig;
import com.njfu.yxcmc.base.GloabApp;
import com.njfu.yxcmc.util.SystemUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class ParamUtils {

    public static JSONObject getJsonCommonParams() throws JSONException {

        JSONObject param = GloabApp.getInstance().getGlobalField().getCommonJsonParams();
        if (param != null) {
            return param;
        }

        param = new JSONObject();

        String radomCode = UUID.randomUUID().toString();
        String mobileBrand = Build.MANUFACTURER; // 手机品牌
        String mobileSys = Build.VERSION.RELEASE; // android系统版本

        String systemVersion = null;
        String mobileModel = null;
        try {
            systemVersion = Build.VERSION.INCREMENTAL;  //先取对应厂商的版本号
            if (TextUtils.isEmpty(systemVersion)) {
                systemVersion = Build.VERSION.RELEASE;  //如取不到，再取android版本号
            }
            mobileModel = Build.MODEL; // 手机型号
        } catch (Exception e1) {
            systemVersion = "获取错误";
            mobileModel = "获取错误";
        }

        param.put("mobileType", "android-phone");
        param.put("locales", Locale.CHINA.getLanguage());
        param.put("radomCode", radomCode);
        param.put("mobileBrand", mobileBrand);
        param.put("mobileSys", mobileSys);
        param.put("systemVersion", systemVersion);
        param.put("mobileModel", mobileModel);
        param.put("ip", SystemUtils.getHostIP());
        param.put("appVersion", BuildConfig.VERSION_NAME);// 应用版本号

        return param;
    }

    public static Map<String, String> getMapCommonParams() {

        Map<String, String> mapParams = GloabApp.getInstance().getGlobalField().getCommonMapParams();
        if (mapParams != null) {
            return mapParams;
        }

        mapParams = new HashMap<>();

        String radomCode = UUID.randomUUID().toString();
        String mobileBrand = Build.MANUFACTURER; // 手机品牌
        String mobileSys = Build.VERSION.RELEASE; // android系统版本

        String systemVersion = null;
        String mobileModel = null;
        try {
            systemVersion = Build.VERSION.INCREMENTAL;  //先取对应厂商的版本号
            if (TextUtils.isEmpty(systemVersion)) {
                systemVersion = Build.VERSION.RELEASE;  //如取不到，再取android版本号
            }
            mobileModel = Build.MODEL; // 手机型号
        } catch (Exception e) {
            systemVersion = "获取错误";
            mobileModel = "获取错误";
        }

        //添加统一参数
        mapParams.put("mobileType", "android-phone");
        mapParams.put("locales", Locale.CHINA.getLanguage());
        mapParams.put("radomCode", radomCode);
        mapParams.put("mobileBrand", mobileBrand);
        mapParams.put("mobileSys", mobileSys);
        mapParams.put("systemVersion", systemVersion);
        mapParams.put("mobileModel", mobileModel);
        mapParams.put("ip", SystemUtils.getHostIP());
        mapParams.put("appVersion", BuildConfig.VERSION_NAME);// 应用版本号

        GloabApp.getInstance().getGlobalField().setCommonMapParams(mapParams);

        return mapParams;
    }

}
