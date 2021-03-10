package com.kakarote.crm9.erp.fbt.util;

import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Set;

public class AreaUtil {
    public static String getAreaJson() throws IOException {
        char cbuf[] = new char[200000];
        InputStreamReader input =new InputStreamReader(new FileInputStream(new File("src//area.json")),"UTF-8");
        int len =input.read(cbuf);
        String text =new String(cbuf,0,len);
        return text;
        //1.构造一个json对象
        //JSONObject obj = JSONObject.parseObject(text);   //过滤读出的utf-8前三个标签字节,从{开始读取
    }

    /*
     * @Description //由县级市找到地级市
     * @Author wangkaida
     * @Date 16:44 2021/3/9
     * @Param [county]
     * @return java.lang.String
     **/
    public static String getCityByCounty(String county) throws IOException {
        JSONObject obj = JSONObject.parseObject(AreaUtil.getAreaJson());
        JSONObject jsonObject =obj.getJSONObject("county_list");
        Set<Map.Entry<String, Object>> entrySet = jsonObject.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            if (county.equals(entry.getValue())){
                String entryKey = entry.getKey();
                //把最后两位置为0
                String keyNew = entryKey.substring(0,4)+"00";
                //获取地级市列表
                JSONObject cityObject =obj.getJSONObject("city_list");
                Set<Map.Entry<String, Object>> cityEntrySet = cityObject.entrySet();
                for (Map.Entry<String, Object> cityEntry : cityEntrySet) {
                    if (keyNew.equals(cityEntry.getKey())) {
                        return cityEntry.getValue().toString();
                    }
                }
            }
        }

        return null;
    }

    /*
     * @Description //判断是否为地级市测试
     * @Author wangkaida
     * @Date 18:07 2021/3/9
     * @Param [address]
     * @return boolean
     **/
    public static boolean isCity(String address) throws IOException {
        JSONObject obj = JSONObject.parseObject(AreaUtil.getAreaJson());
        //获取地级市列表
        JSONObject cityObject =obj.getJSONObject("city_list");
        Set<Map.Entry<String, Object>> cityEntrySet = cityObject.entrySet();
        for (Map.Entry<String, Object> cityEntry : cityEntrySet) {
            if (address.equals(cityEntry.getValue())) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) throws Exception {
        JSONObject obj = JSONObject.parseObject(AreaUtil.getAreaJson());
        System.out.println(obj.getJSONObject("county_list").values());
    }

}