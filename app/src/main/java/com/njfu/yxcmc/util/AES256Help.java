package com.njfu.yxcmc.util;


import java.io.UnsupportedEncodingException;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AES256Help{
    public static String encode="UTF-8";
    /**
     * 密钥算法
     * java6支持56位密钥，bouncycastle支持64位
     * 要支持265位加密 得替换jdk\jre\lib\security里的包(local_policy.jar,US_export_policy.jar)程序中要添加bcprov-jdk16-145-1.jar
     * */
    public static final String KEY_ALGORITHM="AES";
    /**
     * 加密/解密算法/工作模式/填充方式
     *
     * JAVA6 支持PKCS5PADDING填充方式
     * Bouncy castle支持PKCS7Padding填充方式
     * */
    public static final String CIPHER_ALGORITHM="AES/ECB/PKCS7Padding";
    /**
     * 转换密钥
     * @param key 二进制密钥
     * @return Key 密钥
     * */
    public static Key toKey(byte[] key) throws Exception{
        //实例化DES密钥
        //生成密钥
        SecretKey secretKey=new SecretKeySpec(key,KEY_ALGORITHM);
        return secretKey;
    }
    /**
     * 加密数据
     * @param data 待加密数据
     * @param key 密钥
     * @return byte[] 加密后的数据
     * */
    public static String encrypt(String data,String key) throws Exception{
        //还原密钥
        Key k=toKey(key.getBytes(encode));
        /**
         * 实例化
         * 使用 PKCS7PADDING 填充方式，按如下方式实现,就是调用bouncycastle组件实现
         * Cipher.getInstance(CIPHER_ALGORITHM,"BC")
         */
//		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        Cipher cipher=Cipher.getInstance(CIPHER_ALGORITHM, "BC");
        //初始化，设置为加密模式
        cipher.init(Cipher.ENCRYPT_MODE, k);
        //执行操作
        return parseByte2HexStr(cipher.doFinal(data.getBytes(encode)));
    }
    /**
     * 解密数据
     * @param data 待解密数据
     * @param key 密钥
     * @return byte[] 解密后的数据
     * */
    public static String decrypt(String data,String key) throws Exception{
        //欢迎密钥
        Key k =toKey(key.getBytes(encode));
        /**
         * 实例化
         * 使用 PKCS7PADDING 填充方式，按如下方式实现,就是调用bouncycastle组件实现
         * Cipher.getInstance(CIPHER_ALGORITHM,"BC")
         */
        Cipher cipher=Cipher.getInstance(CIPHER_ALGORITHM);
        //初始化，设置为解密模式
        cipher.init(Cipher.DECRYPT_MODE, k);
        //执行操作
        return new String(cipher.doFinal(parseHexStr2Byte(data)),encode);
    }
    /**
     * 将二进制转换成16进制
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte buf[]) {

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();

    }
    /***
     * 十六进制转2进制
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;

        byte[] result = new byte[hexStr.length()/2];

        for (int i = 0;i< hexStr.length()/2; i++) {

            int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);

            int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);

            result[i] = (byte) (high * 16 + low);

        }
        return result;

    }
//    public static void main(String[] args) throws UnsupportedEncodingException{
//
//        String str="你好中国ABCabc123";
//        //System.out.println("原文："+str);
//        String key="12345678901234567890123456789012";
//        try {
//            String x = AES256Help.encrypt(str, key);
//            //System.out.print("加密后："+x);
//            //System.out.print("\n");
//            //System.out.println("解密后："+AES256Help.decrypt(x, key));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}

