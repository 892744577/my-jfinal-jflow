package com.kakarote.crm9.erp.wx.util;

import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/*
 * @Description //百度地图工具类
 * @Author wangkaida
 * @Date 10:59 2020/11/10
 **/
public class BaiduMapUtils {
	/**
	 * 百度地图通过经纬度来获取地址,传入参数纬度lat、经度lng
	 * @param lat
	 * @param lng
	 * @return
	 */
	public static String getCity(String lat, String lng) {	
		JSONObject obj = getLocationInfo(lat, lng).getJSONObject("result").getJSONObject("addressComponent");
		return obj.getString("city");	
	}
	
	/**
	 * 
	 * MethodName: getLocationInfo
	 * @Description: 通过经度纬度获取详细地址信息
	 * @author wangkaida
	 * @date 2019年8月20日
	 */
	public static JSONObject getLocationInfo(String x, String y) {
		String url = "http://api.map.baidu.com/reverse_geocoding/v3/?ak=CCzXVl083fQmbD5zSaYvY98EyMuZTLwv&output=json&coordtype=wgs84ll&location=" + y + "," + x;
		JSONObject obj = JSONObject.parseObject(HttpClientUtils.sendHttpGet(url));
//		System.out.println(obj);
		return obj;
	}
	
	/**
	 * 
	 * MethodName: changeLocationToBaidu
	 * @Description: 把微信获取到的经纬度转变为百度的经纬度
	 * @author wangkaida
	 * @date 2019年8月20日
	 */
	public static JSONObject changeLocationToBaidu(String lat, String lng) {
		String url = "http://api.map.baidu.com/geoconv/v1/?coords=" + lng + "," + lat + "&from=1&to=5&ak=CCzXVl083fQmbD5zSaYvY98EyMuZTLwv";
		JSONObject obj = JSONObject.parseObject(HttpClientUtils.sendHttpGet(url));
//		System.out.println(obj);
		return obj;
	}
 
	/**
	 * 百度地图通过地址来获取经纬度，传入参数address
	 * @param address
	 * @return
	 */
	public static Map<String,Double> getLngAndLat(String address){
        Map<String,Double> map=new HashMap<String, Double>();
        String url = "http://api.map.baidu.com/geocoder/v2/?address="+address+"&output=json&ak=CCzXVl083fQmbD5zSaYvY98EyMuZTLwv";
        String json = loadJSON(url);
//        JSONObject obj = JSONObject.fromObject(json);
        JSONObject obj = JSONObject.parseObject(json);
        if(obj.get("status").toString().equals("0")){
            double lng=obj.getJSONObject("result").getJSONObject("location").getDouble("lng");
            double lat=obj.getJSONObject("result").getJSONObject("location").getDouble("lat");
            map.put("lng", lng);
            map.put("lat", lat);
//            System.out.println("经度：" + lng + "--- 纬度：" + lat);
        }else{
            System.out.println("未找到相匹配的经纬度！");
        }
        return map;
    }
	
    public static String loadJSON (String url) {
        StringBuilder json = new StringBuilder();
        try {
            URL oracle = new URL(url);
            URLConnection yc = oracle.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    yc.getInputStream()));
            String inputLine = null;
            while ( (inputLine = in.readLine()) != null) {
                json.append(inputLine);
            }
            in.close();
        } catch (Exception e) {
        }
        return json.toString();
    }

}
