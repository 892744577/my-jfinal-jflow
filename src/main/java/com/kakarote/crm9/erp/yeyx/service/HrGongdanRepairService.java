package com.kakarote.crm9.erp.yeyx.service;

import BP.Tools.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.wx.util.DateUtil;

import java.util.Date;

public class HrGongdanRepairService {
    /**
     * 获取记录条数
     * @return
     */
    public int getRepairByOrderNum() {
        String today = DateUtil.changeDateTOStr2(new Date());
        Kv kv= Kv.by("today", today);
        return  Db.queryInt(Db.getSqlPara("admin.hrGongDanRepair.getHrGongDanRepairInitNum",kv).getSql(),today);
    }
    /**
     * 根据orderId获取记录
     * @param basePageRequest
     * @return
     */
    public Page<Record> queryPageList(BasePageRequest basePageRequest) {
        JSONObject jsonObject = basePageRequest.getJsonObject();
        //查询条件
        String search = basePageRequest.getJsonObject().getString("search");
        Kv kv = Kv.by("search",search);
        //open_id
        String open_id = basePageRequest.getJsonObject().getString("openId");
        if(StringUtils.isNotEmpty(open_id)){
            kv.set("open_id",open_id);
        }
        if(StringUtils.isNotEmpty(open_id)){
            kv.set("open_id",open_id);
        }
        //处理状态
        String deal = basePageRequest.getJsonObject().getString("deal");
        if(StringUtils.isNotEmpty(deal)){
            kv.set("deal",deal);
        }
        return Db.paginate(
                basePageRequest.getPage(),
                basePageRequest.getLimit(),
                Db.getSqlPara("admin.hrGongDanRepair.queryPageList",kv)
        );
    }
}
