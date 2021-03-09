package com.kakarote.crm9.erp.fbt.util;

import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

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

    public static void main(String[] args) throws Exception {
        JSONObject obj = JSONObject.parseObject(AreaUtil.getAreaJson());
        System.out.println(obj.getJSONObject("county_list").values());
    }
}
