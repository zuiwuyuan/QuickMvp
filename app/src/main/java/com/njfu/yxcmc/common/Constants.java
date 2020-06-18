package com.njfu.yxcmc.common;

import android.os.Environment;

import com.njfu.yxcmc.base.GlobalApp;

import java.io.File;

public class Constants {

    //================= PATH ====================

    public static final String PATH_DATA = GlobalApp.getInstance().getCacheDir().getAbsolutePath() + File.separator + "data";

    public static final String PATH_CACHE = PATH_DATA + "/NetCache";

    public static final String PATH_SDCARD = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "codeest" + File.separator + "GeekNews";

    public static final String KPI_FAV = "0x1001";
    public static final String KPI_UNFAV = "0x1002";
    public static final String USER_UPDATE_AVATAR = "0x1003";
    public static final String USER_HOME_MSG_DOTS = "0x1004";
}
