package com.kakarote.crm9.common.util;

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
    public static byte[] getMD5(String srcString) throws Exception {
        java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
        md.update(srcString.getBytes("utf-8"));
        return md.digest();
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