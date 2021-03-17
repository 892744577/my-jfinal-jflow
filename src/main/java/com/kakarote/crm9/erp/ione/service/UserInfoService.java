package com.kakarote.crm9.erp.ione.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Db;
import com.kakarote.crm9.common.service.HttpService;
import com.kakarote.crm9.erp.ione.entity.WxcmsIoneUserInfo;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
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
    public boolean syncUserInfo(String createDate) throws Exception {
        Map callPrama = new HashMap();
        Date date = getMaxCreateDate();
        callPrama.put("createDate",createDate);
        String ioneReturn = httpService.gatewayRequest(path, callPrama);
        JSONObject jo = JSONObject.parseObject(ioneReturn);
        JSONArray ja = jo.getJSONArray("data");
        for(int i = 0; i<ja.size();i++){
            JSONObject item = ja.getJSONObject(i);
            WxcmsIoneUserInfo wxcmsIoneUserInfo = new WxcmsIoneUserInfo();
            wxcmsIoneUserInfo.setUuid(item.getString("uuid"));
            wxcmsIoneUserInfo.setCreateUserUuid(item.getString("createUserUuid"));
            wxcmsIoneUserInfo.setCreateDate(item.getDate("createDate"));
            wxcmsIoneUserInfo.setModiUserUuid(item.getString("modiUserUuid"));
            wxcmsIoneUserInfo.setModiDate(item.getDate("modiDate"));
            wxcmsIoneUserInfo.setOpenId(item.getString("openId"));
            wxcmsIoneUserInfo.setMcuId(item.getString("mcuId"));
            wxcmsIoneUserInfo.setName(item.getString("name"));
            wxcmsIoneUserInfo.setPhone(item.getString("phone"));
            wxcmsIoneUserInfo.setSex(item.getString("sex"));
            wxcmsIoneUserInfo.setOriOpenId(item.getString("oriOpenId"));
            WxcmsIoneUserInfo wxcmsIoneUserInfoUpdate = WxcmsIoneUserInfo.dao.findById(item.getString("uuid"));
            if(wxcmsIoneUserInfoUpdate!=null){
                wxcmsIoneUserInfo.update();
            }else{
                wxcmsIoneUserInfo.save();
            }
        }
        return false;
    }
}
