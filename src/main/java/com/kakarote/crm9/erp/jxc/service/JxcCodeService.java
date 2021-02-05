package com.kakarote.crm9.erp.jxc.service;

import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.jxc.entity.JxcOrderDeliveryCode;

import java.util.List;

public class JxcCodeService {
    /**
     * 获取多个客户的一个产品
     * @param entity
     * @return
     */
    public List<JxcOrderDeliveryCode> getByCode(JxcOrderDeliveryCode entity) {
        Kv kv = Kv.by("search",entity.getCode());
        return JxcOrderDeliveryCode.dao.find(Db.getSqlPara("admin.jxcCode.queryPageList",kv));
    }
    /**
     * 获取出货单的多个产品
     * @param entity
     * @return
     */
    public List<JxcOrderDeliveryCode> getByDocno(JxcOrderDeliveryCode entity) {
        Kv kv = Kv.by("search",entity.getDocno());
        //return JxcOrderDeliveryCode.dao.find(Db.getSqlPara("admin.jxcCode.queryPageList",kv).getSql(),entity.getDocno(),entity.getDocno());
        return JxcOrderDeliveryCode.dao.find(Db.getSqlPara("admin.jxcCode.queryPageList",kv));
    }
    /**
     * 获取一个客户的一个产品
     * @param entity
     * @return
     */
    public JxcOrderDeliveryCode getByCodeAndCustomer(JxcOrderDeliveryCode entity) {
        Kv kv = Kv.by("search",entity.getCode()).set("customer",entity.getCustomer());
        return JxcOrderDeliveryCode.dao.findFirst(Db.getSqlPara("admin.jxcCode.queryPageList",kv));
    }

    /**
     * 获取出一个客户的货单的多个产品
     * @param basePageRequest
     * @return
     */
    public Page<Record> getByDocnoAndCustomer(BasePageRequest basePageRequest) {
        String docno = basePageRequest.getJsonObject().getString("docno");
        String customer = basePageRequest.getJsonObject().getString("customer");
        Kv kv = Kv.by("search",docno).set("customer",customer);
        return Db.paginate(
                basePageRequest.getPage(),
                basePageRequest.getLimit(),
                Db.getSqlPara("admin.jxcCode.queryPageList",kv));
    }
}
