package com.njfu.yxcmc.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.njfu.yxcmc.util.AES256Help;

import androidx.annotation.NonNull;

public class CmssSharedPreferences {

    private static String sharedPreferencesName = FusionCode.SHARED_PREFERENCE_NAME;

    private static CmssSharedPreferences mInstance = null;
    private final boolean isEncrypted = false;
    private SharedPreferences mSharedPreferences = null;

    private CmssSharedPreferences(Context context) {
        synchronized (CmssSharedPreferences.class) {
            if (mSharedPreferences == null) {
                mSharedPreferences = context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE);
            }
        }
    }

    static void setSharedPreferencesName(@NonNull String name) {
        sharedPreferencesName = name;
    }

    public static CmssSharedPreferences getInstance(Context context) {
        synchronized (CmssSharedPreferences.class) {
            if (mInstance == null) {
                mInstance = new CmssSharedPreferences(context);
            }
            return mInstance;
        }
    }

    public void cleanAllContent() {
        mSharedPreferences.edit().clear().commit();
    }

    public void saveString(String key, String value) {
        mSharedPreferences.edit().putString(encryptString(key), encryptString(value)).commit();

    }

    public String getString(String key, String defValue) {
        return decryptString(mSharedPreferences.getString(encryptString(key), defValue));
    }

    public void saveInt(String key, int value) {
        mSharedPreferences.edit().putInt(encryptString(key), value).commit();
    }

    public int getInt(String key, int defValue) {
        return mSharedPreferences.getInt(encryptString(key), defValue);
    }

    public void saveString(SpKeyEnum key, String value) {
        mSharedPreferences.edit().putString(encryptString(key.spKey), encryptString(value)).commit();
    }

    public String getString(SpKeyEnum key, String defValue) {
        return decryptString(mSharedPreferences.getString(encryptString(key.spKey), defValue));
    }

    public void saveBoolean(String key, boolean value) {
        mSharedPreferences.edit().putBoolean(encryptString(key), value).commit();
    }

    public void removeBoolean(String key) {
        mSharedPreferences.edit().remove(encryptString(key)).commit();
    }

    public boolean getBoolean(String key, boolean defValue) {
        return mSharedPreferences.getBoolean(encryptString(key), defValue);
    }

    public void saveBoolean(SpKeyEnum key, boolean value) {
        mSharedPreferences.edit().putBoolean(encryptString(key.spKey), value).commit();
    }

    public boolean getBoolean(SpKeyEnum key, boolean defValue) {
        return mSharedPreferences.getBoolean(encryptString(key.spKey), defValue);
    }

    private String encryptString(String origString) {
        if (TextUtils.isEmpty(origString)) return origString;
        if (!isEncrypted) {
            return origString;
        }
        try {
            return AES256Help.encrypt(origString, getClass().getPackage().getName());
        } catch (Exception e) {

            return origString;
        }
    }

    private String decryptString(String encryptedString) {
        if (TextUtils.isEmpty(encryptedString)) return encryptedString;
        if (!isEncrypted) {
            return encryptedString;
        }
        try {
            return AES256Help.decrypt(encryptedString, getClass().getPackage().getName());
        } catch (Exception e) {

            return encryptedString;
        }
    }

    public enum SpKeyEnum {
        LOGIN_USER_NAME("LOGIN_USER_NAME"),
        LOGIN_USER_ID_CARD("LOGIN_USER_ID_CARD"),
        AI_DEVICE_ID("AI_DEVICE_ID"),
        HOME_SEARCH_HISTORY("HOME_SEARCH_HISTORY");

        private String spKey;

        SpKeyEnum(String spKey) {
            this.spKey = spKey;
        }
    }
}
