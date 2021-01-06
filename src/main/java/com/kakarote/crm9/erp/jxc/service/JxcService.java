package com.kakarote.crm9.erp.jxc.service;

import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.jxc.entity.JxcOrderDelivery;
import com.kakarote.crm9.erp.jxc.entity.JxcOrderDeliveryCode;

import java.util.List;

public class JxcService {
    /**
     * 获取出货单
     * @param entity
     * @return
     */
    public JxcOrderDelivery getByDocno(JxcOrderDelivery entity){
        return JxcOrderDelivery.dao.findFirst(Db.getSqlPara("admin.jxc.getByDocno",entity).getSql(),entity.getDocno());
    }

    /**
     * 获取出货单明细
     * @param entity
     * @return
     */
    public List<JxcOrderDeliveryCode> getCodeByDocno(JxcOrderDeliveryCode entity) {
        return JxcOrderDeliveryCode.dao.find(Db.getSqlPara("admin.jxc.getCodeByDocno",entity).getSql(),entity.getDocno());
    }
    /**
     * 获取机身码信息
     * @param entity
     * @return
     */
    public JxcOrderDeliveryCode getByCode(JxcOrderDeliveryCode entity) {
        return JxcOrderDeliveryCode.dao.findFirst(Db.getSqlPara("admin.jxc.getByCode",entity).getSql(),entity.getCode());
    }
    /**
     * 分页查询出货单
     */
    public Page<Record> queryPageList(BasePageRequest basePageRequest) {
        //查询条件
        String search = basePageRequest.getJsonObject().getString("search");
        Kv kv = Kv.by("search",search);
        Page<Record> jxcOrderDeliveryResult = Db.paginate(
                basePageRequest.getPage(),
                basePageRequest.getLimit(),
                Db.getSqlPara("admin.jxc.queryPageList",kv));
        List<Record> jxcOrderDeliveryList = jxcOrderDeliveryResult.getList();
        /*for(int i =0 ; i<jxcOrderDeliveryList.getList().size();i++){
            Record record = jxcOrderDeliveryList.getList().get(i);
            JxcOrderDeliveryCode jxcOrderDeliveryCode = new JxcOrderDeliveryCode();
            jxcOrderDeliveryCode.setDocno(record.getStr("docno"));
            List<Record> jxcOrderDeliveryCodeList = this.getCodeByDocno(jxcOrderDeliveryCode);
            record.set("detail",jxcOrderDeliveryCodeList);
            jxcOrderDeliveryList.setList();
        }*/

        jxcOrderDeliveryList.stream().forEach(record -> {
            JxcOrderDeliveryCode jxcOrderDeliveryCode = new JxcOrderDeliveryCode();
            jxcOrderDeliveryCode.setDocno(record.getStr("docno"));
            List<JxcOrderDeliveryCode> jxcOrderDeliveryCodeList = this.getCodeByDocno(jxcOrderDeliveryCode);
            record.set("detail",jxcOrderDeliveryCodeList);
        });
        jxcOrderDeliveryResult.setList(jxcOrderDeliveryList);
        return jxcOrderDeliveryResult;
    }
}
