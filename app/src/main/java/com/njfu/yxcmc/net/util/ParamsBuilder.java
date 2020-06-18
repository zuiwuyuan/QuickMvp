package com.njfu.yxcmc.net.util;

import android.text.TextUtils;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ParamsBuilder {

    private static Gson gson;

    static {
        gson = new Gson();
    }

    private Map<String, Object> map;

    public ParamsBuilder() {
        this.map = new HashMap<>();
    }

    /**
     * 添加参数
     */
    public ParamsBuilder addParams(String key, Object value) {
        if (TextUtils.isEmpty(key)) {
            throw new NullPointerException("key 不能为空");
        }
        map.put(key, value);
        return this;
    }


    /**
     * 添加全局参数
     */
    public ParamsBuilder addCommonMap() {

        map.putAll(ParamUtils.getMapCommonParams());

        return this;
    }

    /**
     * 返回map集合
     */
    public Map bulid() {
        return map;
    }

    /**
     * 返回json字符串
     */
    public String toJson() {
        return gson.toJson(map);
    }

    /**
     * 返回RequestBody
     */
    public RequestBody toRequestBody() {
        return RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), toJson());
    }

    public <T> RequestBody getRequestBody(Map<String, T> params) {

        addCommonMap();

        for (Map.Entry<String, T> entry : params.entrySet()) {
            addParams(entry.getKey(), entry.getValue());
        }

        return toRequestBody();
    }


    public RequestBody getRequestBody() {

        addCommonMap();

        return toRequestBody();
    }
}