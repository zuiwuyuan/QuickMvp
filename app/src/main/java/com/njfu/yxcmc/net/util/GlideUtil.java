package com.njfu.yxcmc.net.util;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.njfu.yxcmc.net.RemoteApi;
import com.njfu.yxcmc.net.RetrofitClient;

/**
 * 为glide添加token
 */
public class GlideUtil {

    public static GlideUrl getImageGlideUrl(String picUrl) {

        String imgFullUrl = RemoteApi.getFileUrl(picUrl);
        return new GlideUrl(imgFullUrl, new LazyHeaders.Builder().addHeader("Authorization",
                RetrofitClient.getAuthorization()).build());
    }
}
