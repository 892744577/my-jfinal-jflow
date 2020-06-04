package com.kakarote.crm9.erp.yzj.controller;

import BP.DA.DBAccess;
import BP.DA.Paras;
import BP.Difference.SystemConfig;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.kakarote.crm9.erp.admin.entity.PortDept;
import com.kakarote.crm9.erp.admin.entity.PortEmp;
import com.kakarote.crm9.erp.yzj.service.TokenService;
import com.kakarote.crm9.utils.R;

import java.util.*;

/**
 * 微信中台，根据手机号获取公众号openid
 */
public class WxMiddleWareController extends Controller {

    @Inject
    private TokenService tokenService;

    public void getWxOpenIdByPhone(){

        List<PortEmp> portEmpList = PortEmp.dao.find("select a.* from port_emp a where a.yzjJobNo like '%ZN%'");

        List<String> phoneList = new ArrayList<String>();

        if (portEmpList.size() > 0) {
            for (PortEmp portEmp:portEmpList) {
                phoneList.add(portEmp.getTel());
            }

            String[] phoneArray = phoneList.toArray(new String[phoneList.size()]);
            String url = "http://app.aptenon.com:80/api/v1/tenon-social-adapter/tenon/weixin/userInfo/getWxOpenIdByPhone";
            try {
                String result = tokenService.gatewayRequestJson(url, JSONObject.toJSONString(phoneArray));
                JSONArray resultArray = JSONObject.parseObject(result).getJSONArray("data");
                if(resultArray !=null && resultArray.size()>0){
                    for(int i=0;i<resultArray.size();i++){
                        JSONObject userObject = resultArray.getJSONObject(i);
                        String phone = userObject.getString("phone");
                        String openId = userObject.getString("openId");
                        //把openId更新到数据库表port_emp对应字段
                        Paras ps = new Paras();
                        ps.Add("WxOpenId", openId);
                        ps.Add("Tel", phone);
                        String sql = "UPDATE port_emp SET WxOpenId="+ SystemConfig.getAppCenterDBVarStr()+"WxOpenId WHERE Tel=" + SystemConfig.getAppCenterDBVarStr()
                                + "Tel";
                        int num = DBAccess.RunSQL(sql, ps);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            renderJson(R.ok().put("msg","更新成功!").put("code","000000"));
        }

    }

}
