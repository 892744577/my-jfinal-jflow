package com.kakarote.crm9.erp.yzj.service;


import BP.Difference.SystemConfig;
import com.kakarote.crm9.erp.yzj.util.HttpHelper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * @author tangmanrong
 */
@Slf4j
public class YeyxService {
    private static final String APPLICATION_JSON = "application/json";
    private static final String APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
    private static final int timeoutMillis = 3000;

    @Getter
    private String appId = SystemConfig.getCS_AppSettings().get("GD.appId").toString();
    @Getter
    private String secret = SystemConfig.getCS_AppSettings().get("GD.secret").toString();

    public String getMd5(String jsonStr,long timestamp) {

        return getMd5(this.appId,this.secret,jsonStr,timestamp );
    }

    public String getMd5(String appId,String secret,String jsonData,long timestamp){
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

    /**
     * gateway发送json参数POST请求
     * @param url
     * @param parm
     * @return
     * @throws Exception
     */
    public static String gatewayRequestJson(String url, String parm) throws Exception {
        Map headers = new HashMap(1); headers.put("Content-Type", APPLICATION_JSON);
        return HttpHelper.post(headers, parm, url, timeoutMillis);
    }

    /**
     * gateway发送application/x-www-form-urlencoded参数POST请求
     * @param url
     * @param parm
     * @return
     * @throws Exception
     */
    public String gatewayRequest(String url, Map parm) throws Exception {
        Map headers = new HashMap(1); headers.put("Content-Type", APPLICATION_X_WWW_FORM_URLENCODED);
        return HttpHelper.post(headers, parm, url, timeoutMillis);
    }

    /*public static void main(String[] args) {
        long timestamp = System.currentTimeMillis()/1000 ;
        System.out.println(timestamp );
        Map map = new LinkedHashMap();
        map.put("cityId",110100);
        map.put("latitude",40.014708);
        map.put("longitude",116.358663);
        map.put("productCount",1);
        map.put("productId",10271);
        //java对象变成json对象
        net.sf.json.JSONObject jsonObject= net.sf.json.JSONObject.fromObject(map);

        //json对象转换成json字符串
        String jsonStr=jsonObject.toString();
        System.out.println(getMd5("10083","c5f79d384b8024d5adddb872f9651f38",jsonStr,timestamp ));
    }*/
}