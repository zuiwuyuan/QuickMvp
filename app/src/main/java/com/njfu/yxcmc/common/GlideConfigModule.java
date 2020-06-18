package com.njfu.yxcmc.common;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.njfu.yxcmc.util.FileUtils;

import java.io.File;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import okhttp3.OkHttpClient;

@GlideModule
public class GlideConfigModule extends AppGlideModule {

    private File diskCacheFolder;

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {

        diskCacheFolder = FileUtils.getDiskLruCacheDir(context, FusionCode.GLIDE_CACHE_LOCAPATH);

//        LogUtils.e("diskCacheFolder : "+diskCacheFolder.getAbsolutePath());

        //磁盘缓存配置（默认缓存大小250M，默认保存在内部存储中）

        long diskCacheSize = 1024 * 1024 * 500;
        //设置磁盘缓存保存在自己指定的目录下，且指定缓存大小
        builder.setDiskCache(new DiskLruCacheFactory(new DiskLruCacheFactory.CacheDirectoryGetter() {
            @Override
            public File getCacheDirectory() {
                return diskCacheFolder;
            }
        }, diskCacheSize));


        //设置内存缓存大小
        long memoryCacheSize = 1024 * 1024 * 50;
        builder.setMemoryCache(new LruResourceCache(memoryCacheSize));

        //设置Bitmap池
        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context)
                .setBitmapPoolScreens(3)
                .build();
        builder.setBitmapPool(new LruBitmapPool(calculator.getBitmapPoolSize()));
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {

        // register ModelLoaders here.
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .connectTimeout(45, TimeUnit.SECONDS)
                .readTimeout(45, TimeUnit.SECONDS)
                .writeTimeout(45, TimeUnit.SECONDS);

        OkHttpUrlLoader.Factory factory = new OkHttpUrlLoader.Factory(client.build());

        //替换组件，如网络请求组件
        registry.replace(GlideUrl.class, InputStream.class, factory);

    }

}