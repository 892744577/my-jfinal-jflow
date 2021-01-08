package com.kakarote.crm9.erp.yeyx.service;

import BP.Difference.SystemConfig;
import com.alibaba.fastjson.JSON;
import com.kakarote.crm9.common.util.EncodeUtil;
import com.kakarote.crm9.common.util.HttpHelper;
import com.kakarote.crm9.erp.wx.util.DateUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * @author tangmanrong
 */
@Slf4j
public class WdtService {
    private static final String APPLICATION_JSON = "application/json";
    private static final String APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
    private static final int timeoutMillis = 30000;

    @Getter
    private String sid = SystemConfig.getCS_AppSettings().get("WDT.sid").toString();
    @Getter
    private String appkey = SystemConfig.getCS_AppSettings().get("WDT.appkey").toString();
    @Getter
    private String qmAppkey = SystemConfig.getCS_AppSettings().get("WDT.qm.appkey").toString();
    @Getter
    private String appsecret = SystemConfig.getCS_AppSettings().get("WDT.appsecret").toString();
    @Getter
    private String session = SystemConfig.getCS_AppSettings().get("WDT.session").toString();

    /**
     * 获取参数的json字符串
     * @param moreMapData
     * @return
     */
    public String getJsonData(Map moreMapData) {
        Map map = new HashMap();
        try {
            //Base64 加密
            long timestamp =  System.currentTimeMillis()/1000;
            map.put("timestamp",timestamp);
            map.put("sid",this.sid);
            map.put("appkey", this.appkey);
            map.put("page_no", 0);
            map.put("page_size",500);
            map.put("start_time", moreMapData.get("start_time"));
            map.put("end_time", moreMapData.get("end_time"));
            map.putAll(moreMapData);

            //1、计算长度，并拼接
//            for(String key : (String [])map.keySet().toArray()){
//                String value = String.valueOf(map.get(key));
//                value.length();
//            }
            //2、排序

            map.put("sign",this.getSign(map));
            return JSON.toJSONString(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 奇门获取订单数据
     * @param moreMapData
     * @return
     */
    public Map getQmJsonData(Map moreMapData) {
        Map map = new HashMap();
        try {
            map.put("app_key",this.qmAppkey);
            map.put("method","taobao.crm.order.detail.get");
            map.put("target_app_key", this.appkey);

            map.put("session", this.session);
            map.put("timestamp", DateUtil.changeDateTOStr(new Date()));
            map.put("format", "json");
            map.put("v", "2.0");

            //业务参数
//            map.put("start_time", moreMapData.get("start_time"));
//            map.put("end_time", moreMapData.get("end_time"));
            map.put("page_no", "0");
            map.put("page_size","500");
            map.putAll(moreMapData);
            map.put("sign", this.getSign(map));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    //获取签名
    public String getSign(Map params){
        // 第一步：检查参数是否已经排序
        String[] keys = (String[]) params.keySet().toArray(new String[0]);
        Arrays.sort(keys);

        // 第二步：把所有参数名和参数值串在一起
        StringBuilder query = new StringBuilder();
        query.append(this.appsecret);
        for (String key : keys) {
            Object objValue = params.get(key);
            String value = null;
            if(objValue instanceof Integer){
                value = String.valueOf(objValue);
            }else if(objValue instanceof Long){
                value = String.valueOf(objValue);
            }else if(objValue instanceof Date){
                value = DateUtil.changeDateTOStr((Date)objValue);
            }else if(objValue instanceof String){
                value = String.valueOf(objValue);
            }
            query.append(key).append(value);

        }
        // 第三步：使用MD5加密
        byte[] bytes = new byte[0];
//        query.append(this.appsecret);
        try {
            bytes = EncodeUtil.getMD5(query.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(bytes);

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
}