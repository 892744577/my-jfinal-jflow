package com.kakarote.crm9.common.util;

import java.math.BigInteger;
import java.util.Base64;

/**
 * @description: 加解密工具类
 * @author: zhangyifan@wshifu.com
 * @date: 2019-04-03 12:24
 */
public class EncodeUtil {

    /**
     * 生成md5
     * @param srcString
     * @return
     */
    public static byte[] getMD5(String srcString) {
        java.security.MessageDigest md = null;
        try {
            md = java.security.MessageDigest.getInstance("MD5");
            md.update(srcString.getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return md.digest();
    }
    /**
     * 生成md532位 小写
     * @param srcString
     * @return
     */
    public static String get32md5(String srcString){
        String md5code= new BigInteger(1, getMD5(srcString)).toString(16);
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }

    /**
     * 生成md532位 大写
     * @param srcString
     * @return
     */
    public static String get32md5Big(String srcString){
        return get32md5(srcString).toUpperCase();
    }

    /**
     * 生成base64
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptBASE64(byte[] key){
        return Base64.getEncoder().encodeToString(key);
    }
    /**
     * 解密base64
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] decryptBASE64(String data){
        return Base64.getDecoder().decode(data);
    }

}