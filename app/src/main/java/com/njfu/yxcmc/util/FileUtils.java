package com.njfu.yxcmc.util;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import com.apkfuns.logutils.LogUtils;
import com.njfu.yxcmc.provider.ContextProvider;

import java.io.File;
import java.io.FileOutputStream;

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
     * <p>
     * 通过迁移到Context＃getExternalFilesDir（String），MediaStore或Intent＃ACTION_OPEN_DOCUMENT之类的替代方案，应用程序可以继续访问共享/外部存储中存储的内容。
     * getExternalFilesDir(null)将返回您的应用存储文件夹，位于（内部存储）/Android/data/your.app.name/file/
     */
    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);// 判断sd卡是否存在

        LogUtils.e("sdCardExist : " + sdCardExist);
        if (sdCardExist) {
            LogUtils.e("SDK_INT : " + Build.VERSION.SDK_INT);
            if (Build.VERSION.SDK_INT >= 29) {
                //Android10之后
                sdDir = ContextProvider.get().getContext().getExternalFilesDir(null);
            } else {
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

    /**
     * 将字符串写入sd卡文件
     *
     * @param dirPath  文件子路径，如"/asiainfo/log/"
     * @param fileName 文件名，如"asiainfo_cmc.log"
     * @param content  内容
     */
    public static void writeToFile(String dirPath, String fileName, String content) {

        FileOutputStream fos = null;

        try {

            /* 判断sd的外部设置状态是否可以读写 */
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

                String sdDir = null;
                if (Build.VERSION.SDK_INT >= 29) {
                    sdDir = ContextProvider.get().getContext().getExternalFilesDir(null) + dirPath;
                } else {
                    sdDir = Environment.getExternalStorageDirectory() + dirPath;// 获取SD卡根目录
                }

                File dirFile = new File(sdDir);

                if (!dirFile.exists()) {
                    dirFile.mkdirs();
                }

                File file = new File(dirFile, fileName);
                // 先清空内容再写入
                fos = new FileOutputStream(file);

                byte[] buffer = content.getBytes();
                fos.write(buffer);
                fos.close();
            }


        } catch (Exception ex) {

            ex.printStackTrace();

            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取目录文件大小
     *
     * @param dir 目录文件
     * @return
     */
    public static long getDirSize(File dir) {
        if (dir == null) {
            return 0;
        }
        if (!dir.isDirectory()) {
            return 0;
        }
        long dirSize = 0;
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                dirSize += file.length();
            } else if (file.isDirectory()) {
                dirSize += getDirSize(file); // 递归调用继续统计
            }
        }
        return dirSize;
    }

    /**
     * 删除某个目录+文件
     *
     * @param filePath       路径
     * @param deleteThisPath 是否删除该目录
     */
    public static void deleteFolderFile(String filePath, boolean deleteThisPath) {
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFolderFile(files[i].getAbsolutePath(), true);
                }
            }
            if (deleteThisPath) {
                if (!file.isDirectory()) {
                    file.delete();
                } else {
                    if (file.listFiles().length == 0) {
                        file.delete();
                    }
                }
            }
        }
    }
}
