package com.njfu.yxcmc.util;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

import com.apkfuns.logutils.LogUtils;

import java.io.File;

public class FileUtils {

    /**
     * @param cacheDirName - 缓存的最终目录文件夹名称
     * @return - 获取硬盘缓存的目录
     */
    public static File getDiskLruCacheDir(Context context, String cacheDirName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        File file = new File(cachePath + "/" + cacheDirName);
        if (!file.exists()) {
            file.mkdirs();
        }

        return file;
    }

    public static File getDiskLruFileDir(Context context, String cacheDirName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalFilesDir(null).getPath();
        } else {
            cachePath = context.getFilesDir().getPath();
        }
        File file = new File(cachePath + "/" + cacheDirName);
        if (!file.exists()) {
            file.mkdirs();
        }

        return file;
    }

    public static File getAcacheFileDir(Context context) {

        File diskFile = getDiskLruFileDir(context, "Acache");
        if (diskFile.exists()) {
            return diskFile;
        }
        File cacheFile = getDiskLruCacheDir(context, "Acache");
        if (cacheFile.exists()) {
            return cacheFile;
        }

        return null;
    }

    /**
     * 原文链接：https://blog.csdn.net/qq_22859147/java/article/details/104601937
     * getExternalStorageDirectory（）-在API级别29中不推荐使用此方法。为了提高用户隐私，不建议直接访问共享/外部存储设备。
     * 当应用程序定位到Build.VERSION_CODES.Q时，此方法返回的路径不再可供应用程序直接访问。
     *
     * 通过迁移到Context＃getExternalFilesDir（String），MediaStore或Intent＃ACTION_OPEN_DOCUMENT之类的替代方案，应用程序可以继续访问共享/外部存储中存储的内容。
     * getExternalFilesDir(null)将返回您的应用存储文件夹，位于（内部存储）/Android/data/your.app.name/file/
     */
    public static String getSDPath(Context context) {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);// 判断sd卡是否存在

        LogUtils.e("sdCardExist : "+sdCardExist);
        if (sdCardExist) {
            LogUtils.e("SDK_INT : "+Build.VERSION.SDK_INT);
            if (Build.VERSION.SDK_INT>=29){
                //Android10之后
                sdDir = context.getExternalFilesDir(null);
            }else {
                sdDir = Environment.getExternalStorageDirectory();// 获取SD卡根目录
            }
        } else {
            sdDir = Environment.getRootDirectory();// 获取跟目录
        }

        if (sdDir != null) {
            LogUtils.e(sdDir.toString());
            return sdDir.toString();
        }

        return null;
    }

    public static String getImageCacheDir(Context context) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            // context.getFilesDir().getPath(); 不这样写  有些机型会报错
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                cachePath = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath();
            } else {
                cachePath = context.getExternalCacheDir().getPath();
            }
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath + File.separator + "image_select";
    }
}
