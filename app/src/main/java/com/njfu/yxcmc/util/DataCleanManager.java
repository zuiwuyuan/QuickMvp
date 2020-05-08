package com.njfu.yxcmc.util;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

import com.njfu.yxcmc.common.FusionCode;

import java.io.File;
import java.math.BigDecimal;

public class DataCleanManager {

    /**
     * 获取缓存大小
     */
    public static String getTotalCacheSize(Context context) throws Exception {
        long cacheSize = getFolderSize(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cacheSize += getFolderSize(context.getExternalCacheDir());
        }
        return getFormatSize(cacheSize);
    }

    /**
     * 清除缓存
     */
    public static void clearAllCache(Context context) {
        deleteDir(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteDir(context.getExternalCacheDir());
        }
    }

    /**
     * 获取文件目录大小
     *
     * @param context
     * @return
     * @throws Exception
     */
    public static String getTotalFileSize(Context context) throws Exception {
        long fileSize = getFolderSize(context.getFilesDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            fileSize += getFolderSize(context.getExternalFilesDir(null));
        }
        return getFormatSize(fileSize);
    }

    public static void clearAllFile(Context context) {
        deleteDir(context.getFilesDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteDir(context.getExternalFilesDir(null));
        }
    }

    public static String getSdCardFileSize(Context context) throws Exception {
        long fileSize = getFolderSize(getSdcardFile(context));

        return getFormatSize(fileSize);
    }

    public static void clearSdcardFile(Context context) {
        try {
            deleteDir(getSdcardFile(context));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getTotalDataSize(Context context) throws Exception {
        long dataSize = getFolderSize(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            dataSize += getFolderSize(context.getExternalCacheDir());
        }
        dataSize += getFolderSize(context.getFilesDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            dataSize += getFolderSize(context.getExternalFilesDir(null));
        }

        dataSize += getFolderSize(getSdcardFile(context));

        return getFormatSize(dataSize);
    }

    public static void clearAllData(Context context) {
        clearAllCache(context);
        clearAllFile(context);
        clearSdcardFile(context);
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    // 获取文件大小
    //Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
    //Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            if (file != null) {

                File[] fileList = file.listFiles();
                if (fileList != null) {
                    for (int i = 0; i < fileList.length; i++) {
                        // 如果下面还有文件
                        if (fileList[i].isDirectory()) {
                            size = size + getFolderSize(fileList[i]);
                        } else {
                            size = size + fileList[i].length();
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }


    private static File getSdcardFile(Context context) throws Exception {

        File sdFile = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);// 判断sd卡是否存在

        File sdDir = null;
        if (sdCardExist) {
            if (Build.VERSION.SDK_INT < 29) {
                sdDir = Environment.getExternalStorageDirectory();// 获取SD卡根目录
                if (sdDir != null) {
                    sdFile = new File(sdDir.toString() + FusionCode.FILE_LOCALPATH);
                }
            }
        }

//        LogUtils.e("sdFile" + sdFile);

        return sdFile;
    }

    /**
     * 格式化单位
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return "0K";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "K";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "M";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }
}