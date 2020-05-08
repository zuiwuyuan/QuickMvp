package com.njfu.yxcmc.cache;

import android.content.Context;

import com.njfu.yxcmc.util.FileUtils;

public class BaseCache {

    ACache mCache;

    public BaseCache(Context context) {
        mCache = ACache.get(FileUtils.getAcacheFileDir(context));
    }
}
