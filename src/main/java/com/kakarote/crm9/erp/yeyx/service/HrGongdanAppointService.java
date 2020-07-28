package com.kakarote.crm9.erp.yeyx.service;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;

public class HrGongdanAppointService {
    /**
     * 根据orderId获取记录
     * @param orderNumber
     * @return
     */
    public Record getAppointByOrderNum(String orderNumber) {
        return  Db.findFirst(Db.getSqlPara("admin.hrGongDanBook.getHrGongDanBookInitNum", Kv.by("orderNumber",orderNumber)));
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
        return Db.paginate(
                basePageRequest.getPage(),
                basePageRequest.getLimit(),
                Db.getSqlPara("admin.hrGongDanBook.queryPageList",kv)
        );
    }
}
