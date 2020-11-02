package com.kakarote.crm9.erp.yeyx.service;

import BP.Difference.SystemConfig;
import com.alibaba.fastjson.JSON;
import com.kakarote.crm9.common.util.HttpHelper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Logger;

import java.util.*;


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
    private String appsecret = SystemConfig.getCS_AppSettings().get("WDT.appsecret").toString();
    @Getter
    private String path = SystemConfig.getCS_AppSettings().get("WDT.path").toString();

    public String getJsonData(String start_time,String end_time,Map moreMapData) {

        try {
            //Base64 加密
            Map map = new HashMap();
            long timestamp =  System.currentTimeMillis();
            map.put("timestamp",timestamp);
            map.put("sid",this.sid);
            map.put("appkey", this.appkey);
            map.put("start_time", this.appkey);
            map.put("end_time", this.appkey);
            map.put("page_no", 0);
            map.put("page_size",500);
            map.putAll(moreMapData);

            //1、计算长度，并拼接
            for(String key : (String [])map.keySet().toArray()){
                String value = String.valueOf(map.get(key));
                value.length();
            }
            //2、排序

            //map.put("sign",);
            return JSON.toJSONString(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    //字典排序
    public List listSort(List strList){
        Collections.sort(strList, new SpellComparator());
        for (int i = 0; i < strList.size(); i++) {
            System.out.println(strList.get(i));
        }
        return strList;

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

/**
 * 汉字拼音排序比较器
 */
class SpellComparator implements Comparator {

    private static Logger log = Logger.getLogger(SpellComparator.class.getClass());


    public int compare(Object o1, Object o2) {
        try {
            // 取得比较对象的汉字编码，并将其转换成字符串
            String s1 = new String(o1.toString().getBytes("UTF-8"), "ASCII");
            String s2 = new String(o2.toString().getBytes("UTF-8"), "ASCII");
            // 运用String类的 compareTo（）方法对两对象进行比较
            return s1.compareTo(s2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}