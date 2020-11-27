package com.kakarote.crm9.erp.fbt.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;

/*
 * @Description //签名工具类
 * @Author wangkaida
 * @Date 9:28 2020/11/27
 **/
@Slf4j
public class SignUtil {

	public static String getSign(String timestamp, String jsonData, String signKey) throws Exception {
		//使用相对应的方法进行字符串拼接,可以调用不同方法生成签名字符串
		String signStr = MessageFormat.format("timestamp={0}&data={1}&sign_key={2}", timestamp, jsonData, signKey);
		byte[] bytes;
		try {
			bytes = signStr.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			log.error("生成openapi签名出错", e);
			throw new Exception(e.getMessage());
		}
		String sign = DigestUtils.md5Hex(bytes);
		return sign;
	}

}
