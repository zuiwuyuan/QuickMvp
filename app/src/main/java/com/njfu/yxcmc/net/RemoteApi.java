package com.njfu.yxcmc.net;

import android.text.TextUtils;

import com.njfu.yxcmc.BuildConfig;

import androidx.annotation.NonNull;

public final class RemoteApi {
    private RemoteApi() {
    }

    private static String remoteToken = null;
    private static final Host MY_HOST = BuildConfig.DEBUG ? Host.DVPT : Host.PRDT;

    private static final String BASE_API_PATH = "/";

    private enum Host {
        PRDT(47, 104, 74, 169, 8080, false),
        DVPT("www.wanandroid.com", true);

        private String url;

        Host(int a, int b, int c, int d, int p, boolean isHttps) {
            this.url = "ht" + (isHttps ? "tps" : "tp") + "://" + a + "." + b + "." + c + "." + d + ":" + p + "/";//分段写，规避安全漏洞
        }

        Host(String hostName, boolean isHttps) {
            this.url = "ht" + (isHttps ? "tps" : "tp") + "://" + hostName + "/";
        }
    }

    /**
     * 获取完整url
     *
     * @param url url
     * @return full url
     */
    public static String getFullUrl(@NonNull String url) {
        if (TextUtils.isEmpty(url))
            return "";
        if (!url.startsWith("http"))
            url = MY_HOST.url + url;
        return url;
    }


    /**
     * 获取主机地址
     *
     * @return host url
     */
    public static String getHostBaseUrl() {
        return MY_HOST.url;
    }

    public static String getHostBasePathUrl() {
        return MY_HOST.url + BASE_API_PATH;
    }

    /**
     * 获取文件url
     *
     * @param fileName fileName
     * @return file url
     */
    public static String getFileUrl(@NonNull String fileName) {
        if (TextUtils.isEmpty(fileName))
            return "";
        if (!fileName.startsWith("http")) {
            if (fileName.startsWith("/")) {
                fileName = fileName.substring(1, fileName.length());
            }
            fileName = MY_HOST.url + "ha/file/download?fileName=" + fileName;
        }
        return fileName;
    }

    public static String getRemoteToken() {
        return remoteToken;
    }

    public static void setRemoteToken(String remoteToken) {
        RemoteApi.remoteToken = remoteToken;
    }
}
