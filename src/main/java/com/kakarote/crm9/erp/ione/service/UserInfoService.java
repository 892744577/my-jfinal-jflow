package com.kakarote.crm9.erp.ione.service;

import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Db;
import com.kakarote.crm9.common.service.HttpService;
import com.kakarote.crm9.erp.ione.entity.WxcmsIoneUserInfo;
import lombok.Getter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserInfoService {

    @Inject
    private HttpService httpService;
    @Getter
    private String path = "http://app.aptenon.com/api/v1/tenon-social-adapter/tenon/weixin/userInfo/getAllUserInfo";
    /**
     * 查询最大创建时间
     * @return
     */
    public Date getMaxCreateDate() {
        return Db.queryDate(Db.getSql("admin.ioneUserInfo.maxCreateDate"));
    }
    /**
     * 查询单条数据
     * @return
     */
    public WxcmsIoneUserInfo getWxcmsIoneUserInfo(String uuid) {
        return WxcmsIoneUserInfo.dao.findById(uuid);
    }
    /**
     * 同步用户
     * @return
     */
    public boolean syncUserInfo() throws Exception {
        Map callPrama = new HashMap();
        Date date = getMaxCreateDate();
        callPrama.put("createDate","2018-01-09");
        String t100Return = httpService.gatewayRequestJson(path, JSON.toJSONString(callPrama));

        return false;
    }
}
