package com.njfu.yxcmc.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import androidx.core.app.ActivityCompat;

/**
 * 获取android设备唯一标识码
 * created by dlong in 2019/02/21
 */
public class GetAndroidUniqueMark {

    public static String getUniqueId(Context context) {
        @SuppressLint("HardwareIds")
        // ANDROID_ID是设备第一次启动时产生和存储的64bit的一个数，当设备被wipe后该数重置。
                String androidID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        @SuppressLint("HardwareIds")
        String id = androidID + Build.SERIAL; // +硬件序列号
        try {
            return toMD5(id);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return id;
        }
    }

    public static String toMD5(String text) throws NoSuchAlgorithmException {
        //获取摘要? MessageDigest
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        //通过摘要?对字符?的二进制字节数组进?hash计算
        byte[] digest = messageDigest.digest(text.getBytes());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digest.length; i++) {
            //循环每个字符 将计算结果转化为正整数?
            int digestInt = digest[i] & 0xff;
            //将10进制转化为较短的16进制
            String hexString = Integer.toHexString(digestInt);
            //转化结果如果是个位数会省?0,因此判断并补0
            if (hexString.length() < 2) {
                sb.append(0);
            }
            //将循环结果添加到缓冲区
            sb.append(hexString);
        }
        //返回整个结果
        return sb.toString().substring(8, 24);
    }

//    不可重置的设备标识符限制：从 Android 10 开始，去掉了READ_PHONE_STATE权限，取而代之的是一个系统级别的权限：
//    READ_PRIVILEGED_PHONE_STATE（只提供给系统app），只有拥有该权限才能访问设备的不可重置标识符（包含 IMEI 和序列号）。
//    那么在Android Q平台，我们的应用也不会再有IMEI和序列号。
//
//    受影响的方法：
//
//        Build
//            getSerial()
//        TelephonyManager
//            getImei()
//            getDeviceId()
//            getMeid()
//            getSimSerialNumber()
//            getSubscriberId()
//
//    针对(targetSDK >= 29)的设备唯一标识符适配，谷歌官方有提供解决方案，但是ID可变。ANDROID_ID的获取一直不受影响，但可能由于机型原因返回null，
//    两种方法半斤八两。目前没有很好的解决方案。


    //    虽然由于唯一标识符权限的更改会导致android.os.Build.getSerial()返回unknown,但是由于m_szDevIDShort是由硬件信息拼出来的，所以仍然保证了UUID的唯一性和持久性。
//    目前我们这边需要设备id的地方就是绑定手机，我们有考虑获取真正的设备id为空的情况，直接给了一个6位的随机字符串，这个问题对我们影响不大。
//    另外由于我们这边使用第三方推送，一般情况下推送库需要设备id才能知道到底是那个手机（具体看三方库的实现），需要实时关注推送库的更新，并进行使用。
    public static String getUUID(Context context) {

        String serial = null;
        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +
                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +
                Build.USER.length() % 10; //13 位
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return "serial";
                }
                serial = android.os.Build.getSerial();
            } else {
                serial = Build.SERIAL;
            }
            //API>=9 使用serial号
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            //serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }
        //使用硬件信息拼凑出来的15位号码
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

}