package com.njfu.yxcmc.cache;

import android.content.Context;

import org.json.JSONObject;

public class JsonObjCache extends BaseCache {

    public JsonObjCache(Context context) {
        super(context);
    }

    /**
     * 保存jsonObj信息
     */
    public void saveNewestSample(String key, JSONObject jsonData) {
        mCache.put(key, jsonData);
    }

    public void saveNewestSample(String key, JSONObject jsonData, int outTime) {
        mCache.put(key, jsonData, outTime);
    }

    /**
     * 获取jsonObj信息
     *
     * @return
     */
    public JSONObject getNewestSampleInfo(String key) {
        return mCache.getAsJSONObject(key);
    }

    /**
     * 移除缓存
     */
    public void removeNewestSampleInfo(String key) {
        mCache.remove(key);
    }
}