package com.kakarote.crm9.utils;

import BP.Difference.SystemConfig;
import net.sf.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author tangmanrong
 */
public class GdUtil {

    private static String appId = SystemConfig.getCS_AppSettings().get("GD.appId").toString();

    private static String secret = SystemConfig.getCS_AppSettings().get("GD.secret").toString();

    private static String getMd5(String jsonStr) {

        return getMd5(appId,secret,jsonStr,System.currentTimeMillis()/1000 );
    }

    public static String getMd5(String appId,String secret,String jsonData,long timestamp){
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(
                    ("appId"+appId + "jsonData" + jsonData + "timestamp" + timestamp+ "version1" + secret).getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("没有这个md5算法！");
        }
        String md5code = new BigInteger(1, secretBytes).toString(16);
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }

    public static void main(String[] args) {
        long timestamp = System.currentTimeMillis()/1000 ;
        System.out.println(timestamp );
        Map map = new LinkedHashMap();
        map.put("cityId",110100);
        map.put("latitude",40.014708);
        map.put("longitude",116.358663);
        map.put("productCount",1);
        map.put("productId",10271);
        //java对象变成json对象
        JSONObject jsonObject= JSONObject.fromObject(map);

        //json对象转换成json字符串
        String jsonStr=jsonObject.toString();
        System.out.println(getMd5("10083","c5f79d384b8024d5adddb872f9651f38",jsonStr,timestamp ));
    }


}
