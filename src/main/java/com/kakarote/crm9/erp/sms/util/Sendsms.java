package com.kakarote.crm9.erp.sms.util;//接口类型：互亿无线触发短信接口，支持发送验证码短信、订单通知短信等。
// 账户注册：请通过该地址开通账户http://sms.ihuyi.com/register.html
// 注意事项：
//（1）调试期间，请用默认的模板进行测试，默认模板详见接口文档；
//（2）请使用APIID（查看APIID请登录用户中心->验证码短信->产品总览->APIID）及 APIkey来调用接口；
//（3）该代码仅供接入互亿无线短信接口参考使用，客户可根据实际需要自行编写；

import com.jfinal.aop.Aop;
import com.kakarote.crm9.common.util.HttpHelper;
import com.kakarote.crm9.erp.sms.entity.Sms;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Slf4j
public class Sendsms {

    private static final String APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded;charset=GBK";
    private static final int timeoutMillis = 5000;

    private static String url = "http://106.ihuyi.cn/webservice/sms.php?method=Submit";

    private static  RedisUtils redisUtils = Aop.get(RedisUtils.class);

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

    public Sms send(Sms entity){
        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod(url);

        client.getParams().setContentCharset("GBK");
        method.setRequestHeader("ContentType","application/x-www-form-urlencoded;charset=GBK");

        HashMap headers = new HashMap();
        headers.put("ContentType","application/x-www-form-urlencoded;charset=GBK");

        //int mobile_code = (int)((Math.random()*9+1)*100000);
        String content = new String("您的验证码是：" + entity.getMobile_code() + "。请不要把验证码泄露给其他人。");
        NameValuePair[] data = {//提交短信
                new NameValuePair("account", entity.getAccount()), //查看用户名是登录用户中心->验证码短信->产品总览->APIID
                new NameValuePair("password", entity.getPassword()),  //查看密码请登录用户中心->验证码短信->产品总览->APIKEY
                new NameValuePair("password", StringUtil.MD5Encode(entity.getPassword())),
                new NameValuePair("mobile", entity.getMobile()),
                new NameValuePair("content", content),
        };
        HashMap params = new HashMap();
        params.put("ContentType","application/x-www-form-urlencoded;charset=GBK");

        method.setRequestBody(data);

        try {
            log.info("=============================================Sendsms==============================================请求method:"+entity.getMobile_code());
            client.executeMethod(method);
//            HttpHelper.post(headers, params, url, timeoutMillis);
            log.info("=============================================Sendsms==================================================返回method:"+url);
            String SubmitResult =method.getResponseBodyAsString();

            Document doc = DocumentHelper.parseText(SubmitResult);
            Element root = doc.getRootElement();

            String code = root.elementText("code");
            String msg = root.elementText("msg");
            String smsid = root.elementText("smsid");

            //设置参数
            entity.setCode(code);
            entity.setMsg(msg);
            entity.setSmsid(smsid);


            if("2".equals(code)){
                //成功后加入redis缓存
                redisUtils.getAndSet(entity.getMobile(),Integer.toString(entity.getMobile_code()));
                //redisUtils.increment(entity.getMobile()+"-count");
                redisUtils.expireKey(entity.getMobile(),600); //10分钟内有效
                //redisUtils.expireKey(entity.getMobile()+"-count",1, TimeUnit.HOURS); //10分钟内有效
                log.info("============================================Sendsms=============================================短信请求成功！");
            }

        }catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        } finally{
            // Release connection
            method.releaseConnection();
            //client.getConnectionManager().shutdown();
        }

        return entity;
    }

}