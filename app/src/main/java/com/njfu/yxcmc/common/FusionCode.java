package com.njfu.yxcmc.common;

import android.os.Environment;

public class FusionCode {

    public final static String SHARED_PREFERENCE_NAME = "quickmvp_preference"; // SharedPreference文件名

    public final static String STRING_TO_REPLACE_NULL_VALUE = "--";// 代替空数据的字符

    // SD卡根目录
    public final static String SD_ROOTPATH = Environment
            .getExternalStorageDirectory() + "";

    // 自定义文件夹跟目录
    public final static String FILE_LOCALPATH = "/CMSS_DataHome/";

    // 自动升级下载相关
    public final static String AUTOUPDATE_LOCALPATH = FILE_LOCALPATH + "Update/";

    public final static String AUTOUPDATE_FILENAME = "MobileBI_update.apk";

    // 图片下载相关
    public final static String IMAGES_LOCALPATH = FILE_LOCALPATH + "Images/";

    // 皮肤下载相关
    public final static String SKIN_LOCALPATH = FILE_LOCALPATH + "Skin/";

    //浮动导航说明图
    public final static String GUIDE_LOCALPATH = FILE_LOCALPATH + "Guide/";

    // 报表大小类缓存路径
    public final static String REPORT_LOCALPATH = FILE_LOCALPATH + "Report/";

    // 报表大小类缓存文件名
//	public final static String GLIDE_CACHE_LOCAPATH = FILE_LOCALPATH + "Glide/";
    public final static String GLIDE_CACHE_LOCAPATH = "/Glide/";

    // 地图数据
    public final static String MAP_LOCALPATH = FILE_LOCALPATH + "Map/";

    // 文件下载相关
    public final static String DOWNLOAD_LOCALPATH = FILE_LOCALPATH + "Download/";

    // 邮件截图路径
    public final static String MAIL_LOCALPATH = FILE_LOCALPATH + "Mail/";

    // 错误日志保存地址
    public final static String ERROR_LOCALPATH = FILE_LOCALPATH + "CrashInfos/";

    // 错误照片地址
    public final static String PHOTO_LOCALPATH = FILE_LOCALPATH + "tempPhoto/";

    // 下载状态 0 成功，1 文件已存在，-1 下载失败，2 下载中,3 开始下载
    public final static int DOWNLOAD_COMPLETE = 0;
    public final static int DOWNLOAD_ED = 1;
    public final static int DOWNLOAD_FAIL = -1;
    public final static int DOWNLOAD_PROS = 2;
    public final static int DOWNLOAD_START = 3;

    // 发送未读消息数量的广播
    public final static String UNREAD_NEWS_BROADCAST_ACTION = "com.linkage.bi.pad.UNREAD_NEWS_BROADCAST_ACTION";

    // 发送未读消息数量的广播
    public final static String UNREAD_NEWS_BROADCAST_STRING = "com.linkage.bi.pad.UNREAD_NEWS_BROADCAST_STRING";

}
