package com.kakarote.crm9.erp.yeyx.service;


import BP.Difference.SystemConfig;
import com.kakarote.crm9.common.util.EncodeUtil;
import com.kakarote.crm9.common.util.HttpHelper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;


/**
 * @author tangmanrong
 */
@Slf4j
public class YeyxService {
    private static final String APPLICATION_JSON = "application/json";
    private static final String APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
    private static final int timeoutMillis = 30000;

    @Getter
    private String appId = SystemConfig.getCS_AppSettings().get("GD.appId").toString();
    @Getter
    private String secret = SystemConfig.getCS_AppSettings().get("GD.secret").toString();
    @Getter
    private String path = SystemConfig.getCS_AppSettings().get("GD.path").toString();

    public String getYeyxMd5(String jsonStr,long timestamp,String version) {
        return getYeyxMd5(this.appId,this.secret,jsonStr,timestamp,version );
    }

    public String getYeyxMd5(String appId,String secret,String jsonData,long timestamp,String version){
        return EncodeUtil.get32md5("appId" + appId + "jsonData" + jsonData + "timestamp" + timestamp + "version" + version + secret);
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
        jsonStr="{\"factory\":2,\"reworkId\":\"\",\"address\":\"1231\",\"facInWarranty\":0,\"gender\":\"\",\"productId\":10254,\"contactName\":\"33132\",\"telephone\":\"13580573264\",\"remark\":\"23123\",\"cityId\":0,\"type\":\"1\",\"productCount\":0,\"dutyTime\":\"2020-06-17 13:39:00.0\",\"facProductId\":\"12046\",\"street\":\"1231\",\"orderDiscount\":{\"amount\":0,\"remark\":\"\",\"sourceData\":\"\"},\"thirdOrderId\":\"YXSA202006170013\"}";
        YeyxService yeyxService = new YeyxService();
        System.out.println(yeyxService.getMd5("34077","0e39fd66fa4b4b239a1c240815103dbf",jsonStr,timestamp,"1" ));
    }*/
}