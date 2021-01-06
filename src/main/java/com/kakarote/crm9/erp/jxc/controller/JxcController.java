package com.kakarote.crm9.erp.jxc.controller;

import BP.Tools.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.jxc.entity.JxcOrderDelivery;
import com.kakarote.crm9.erp.jxc.entity.JxcOrderDeliveryCode;
import com.kakarote.crm9.erp.jxc.entity.vo.JxcCodeRequest;
import com.kakarote.crm9.erp.jxc.entity.vo.JxcRequest;
import com.kakarote.crm9.erp.jxc.service.JxcService;
import com.kakarote.crm9.utils.R;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class JxcController extends Controller {

    @Inject
    private JxcService jxcService;

    /**
     * T100同步出货单数据到无忧
     */
    public void addJxcOrderDeliveryCode(){
        String rawData = getRawData();
        log.info("订单节点回调通知rawData===========:"+rawData);
        if(StringUtils.isNotBlank(rawData)){
            JxcRequest jxcRequest = JSONArray.parseObject(rawData, JxcRequest.class);

            JxcOrderDelivery jxcOrderDelivery = new JxcOrderDelivery();
            jxcOrderDelivery.setDocno(jxcRequest.getDocno());

            jxcOrderDelivery.setCustomer(jxcRequest.getCustomer());
            jxcOrderDelivery.setNum(jxcRequest.getNum());
            jxcOrderDelivery.setDealTime(jxcRequest.getDeal_time());
            jxcOrderDelivery.setOrderNo(jxcRequest.getOrder_no());
            jxcOrderDelivery.setOrderNum(jxcRequest.getOrder_num());
            jxcOrderDelivery.setOrderTime(jxcRequest.getOrder_time());
            //查询是否存在该出货单
            JxcOrderDelivery record = jxcService.getByDocno(jxcOrderDelivery);
            if(record!=null && record.getStr("docno")!=null){
                jxcOrderDelivery.setId(record.getInt("id").longValue());
                jxcOrderDelivery.update();
            }else{
                jxcOrderDelivery.save();
                for(int i = 0 ; i<jxcRequest.getCode_list().size();i++){
                    JxcCodeRequest jxcCodeRequest = jxcRequest.getCode_list().get(i);
                    JxcOrderDeliveryCode deliveryCode = new JxcOrderDeliveryCode();
                    deliveryCode.setOrderNo(jxcRequest.getOrder_no());
                    deliveryCode.setDocno(jxcRequest.getDocno());
                    deliveryCode.setCode(jxcCodeRequest.getCode());
                    deliveryCode.setColor(jxcCodeRequest.getColor());
                    deliveryCode.setModel(jxcCodeRequest.getModel());
                    deliveryCode.setZt(0);
                    deliveryCode.save();
                }
            }
        }
        renderJson(R.ok());
    }

    /**
     * 分页查询出货订单
     */
    public void queryDelivery(BasePageRequest basePageRequest){
        log.info("=======分页查询出货订单");
        renderJson(R.ok().put("data",jxcService.queryPageList(basePageRequest)));
    }

    /**
     * 根据出货单号查询出货单明细
     * @param jxcRequest
     */
    public void queryJxc(@Para("") JxcRequest jxcRequest){
        log.info("=======根据出货单号查询出货单明细");
        JxcOrderDeliveryCode jxcOrderDeliveryCode = new JxcOrderDeliveryCode();
        jxcOrderDeliveryCode.setDocno(jxcRequest.getDocno());
        renderJson(R.ok().put("data",jxcService.getCodeByDocno(jxcOrderDeliveryCode)));
    }
    /**
     * 创建其他机身码信息
     */
    public void createOtherDeliveryCode(@Para("") JxcCodeRequest JxcCodeRequest){
        log.info("=======创建其他入库锁");
        JxcOrderDeliveryCode jxcOrderDeliveryCode = new JxcOrderDeliveryCode();
        jxcOrderDeliveryCode.setDocno("other");
        jxcOrderDeliveryCode.setCode(JxcCodeRequest.getCode());
        jxcOrderDeliveryCode.setColor(JxcCodeRequest.getColor());
        jxcOrderDeliveryCode.setModel(JxcCodeRequest.getModel());
        jxcOrderDeliveryCode.setCustomer(JxcCodeRequest.getCustomer());
        jxcOrderDeliveryCode.save();
        renderJson(R.ok().put("data",jxcOrderDeliveryCode));
    }
    /**
     * 更新机身码入库状态
     */
    public void updateDeliveryCode(@Para("") JxcCodeRequest JxcCodeRequest){
        log.info("=======更新机身码状态");
        JxcOrderDeliveryCode jxcOrderDeliveryCode = new JxcOrderDeliveryCode();
        jxcOrderDeliveryCode.setCode(JxcCodeRequest.getCode());
        jxcOrderDeliveryCode = jxcService.getByCode(jxcOrderDeliveryCode);
        jxcOrderDeliveryCode.setZt(1);
        jxcOrderDeliveryCode.update();
        renderJson(R.ok().put("data",jxcOrderDeliveryCode));
    }

    /**
     * 批量更新机身码入库状态
     */
    public void updateDeliveryCodeByDocno(@Para("") JxcCodeRequest JxcCodeRequest){
        log.info("=======批量更新机身码入库状态");
        JxcOrderDeliveryCode jxcOrderDeliveryCode = new JxcOrderDeliveryCode();
        jxcOrderDeliveryCode.setDocno(JxcCodeRequest.getDocno());
        List<JxcOrderDeliveryCode> list = jxcService.getCodeByDocno(jxcOrderDeliveryCode);
        list.stream().forEach(item->{
            item.setZt(1);
            item.update();
        });
        renderJson(R.ok().put("data",jxcOrderDeliveryCode));
    }


}
