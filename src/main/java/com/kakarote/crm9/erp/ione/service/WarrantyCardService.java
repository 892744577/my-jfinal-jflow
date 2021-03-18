package com.kakarote.crm9.erp.ione.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Db;
import com.kakarote.crm9.common.service.HttpService;
import com.kakarote.crm9.erp.ione.entity.WxcmsIoneWarrantyCard;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class WarrantyCardService {

    @Inject
    private HttpService httpService;
    @Getter
    private String path = "http://app.aptenon.com/api/v1/tenon-social-adapter/tenon/weixin/warrantyCards/getAllWarrantyCard";

    /**
     * 查询最大创建时间
     * @return
     */
    public Date getMaxCreateDate() {
        return Db.queryDate(Db.getSql("admin.ioneWarrantyCard.maxCreateDate"));
    }

    /**
     * 查询单条数据
     * @return
     */
    public WxcmsIoneWarrantyCard getWxcmsIoneWarrantyCard(String uuid) {
        return WxcmsIoneWarrantyCard.dao.findById(uuid);
    }

    /**
     * 同步保修卡
     * @return
     */
    public boolean syncWarrantyCard(String createDate) throws Exception {
        Map callPrama = new HashMap();
        Date date = getMaxCreateDate();
        callPrama.put("createDate",createDate);
        String ioneReturn = httpService.gatewayRequest(path, callPrama);
        JSONObject jo = JSONObject.parseObject(ioneReturn);
        JSONArray ja = jo.getJSONArray("data");
        for(int i = 0; i<ja.size();i++){
            JSONObject item = ja.getJSONObject(i);
            WxcmsIoneWarrantyCard wxcmsIoneWarrantyCard = new WxcmsIoneWarrantyCard();
            wxcmsIoneWarrantyCard.setUuid(item.getString("uuid"));
            wxcmsIoneWarrantyCard.setCreateUserUuid(item.getString("createUserUuid"));
            wxcmsIoneWarrantyCard.setCreateDate(item.getDate("createDate"));
            wxcmsIoneWarrantyCard.setModiUserUuid(item.getString("modiUserUuid"));
            wxcmsIoneWarrantyCard.setModiDate(item.getDate("modiDate"));
            wxcmsIoneWarrantyCard.setProductUuid(item.getString("productUuid"));
            wxcmsIoneWarrantyCard.setMcuId(item.getString("mcuId"));
            wxcmsIoneWarrantyCard.setStartDate(item.getDate("startDate"));
            wxcmsIoneWarrantyCard.setEndDate(item.getDate("endDate"));
            wxcmsIoneWarrantyCard.setStatus(item.getString("status"));
            WxcmsIoneWarrantyCard wxcmsIoneWarrantyCardUpdate = WxcmsIoneWarrantyCard.dao.findById(item.getString("uuid"));
            if(wxcmsIoneWarrantyCardUpdate!=null){
                wxcmsIoneWarrantyCard.update();
            }else{
                wxcmsIoneWarrantyCard.save();
            }
        }
        return true;
    }
}
