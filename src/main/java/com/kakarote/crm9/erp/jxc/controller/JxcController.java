package com.kakarote.crm9.erp.jxc.controller;

import BP.Tools.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.NotAction;
import com.jfinal.core.paragetter.Para;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.admin.service.PortEmpService;
import com.kakarote.crm9.erp.jxc.entity.JxcOrderDelivery;
import com.kakarote.crm9.erp.jxc.entity.JxcOrderDeliveryCode;
import com.kakarote.crm9.erp.jxc.entity.vo.JxcCodeRequest;
import com.kakarote.crm9.erp.jxc.entity.vo.JxcRequest;
import com.kakarote.crm9.erp.jxc.service.JxcCodeService;
import com.kakarote.crm9.erp.jxc.service.JxcService;
import com.kakarote.crm9.utils.R;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;

@Slf4j
public class JxcController extends Controller {

    @Inject
    private JxcService jxcService;
    @Inject
    private JxcCodeService jxcCodeService;
    @Inject
    private PortEmpService portEmpService;

    /**
     * 初始化JxcOrderDeliveryCode
     * @return
     */
    @NotAction
    public JxcOrderDeliveryCode initJxcOrderDeliveryCode(JxcCodeRequest jxcCodeRequest){
        JxcOrderDeliveryCode deliveryCode = new JxcOrderDeliveryCode();
        deliveryCode.setCode(jxcCodeRequest.getCode());
        deliveryCode.setColor(jxcCodeRequest.getColor());
        deliveryCode.setModel(jxcCodeRequest.getModel());

        deliveryCode.setCreateTime(new Date());
        return deliveryCode;
    }

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
                    JxcOrderDeliveryCode deliveryCode = initJxcOrderDeliveryCode(jxcCodeRequest);
                    deliveryCode.setOrderNo(jxcRequest.getOrder_no());
                    deliveryCode.setDocno(jxcRequest.getDocno());
                    deliveryCode.setCustomer(jxcRequest.getCustomer());
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
     * 分页查询其他出货
     */
    public void getByOtherAndCustomer(BasePageRequest basePageRequest){
        log.info("=======根据机其他标志和客户编号查询记录");
        //basePageRequest.setJsonObject(basePageRequest.getJsonObject().fluentPut("docno","other"));
        renderJson(R.ok().put("data",jxcCodeService.getByDocnoAndCustomer(basePageRequest)));
    }

    /**
     * 根据出货单号查询出货单明细
     * @param jxcRequest
     */
    public void queryJxc(@Para("") JxcRequest jxcRequest){
        log.info("=======根据出货单号查询出货单明细");
        JxcOrderDeliveryCode jxcOrderDeliveryCode = new JxcOrderDeliveryCode();
        jxcOrderDeliveryCode.setDocno(jxcRequest.getDocno());
        renderJson(R.ok().put("data",jxcCodeService.getByDocno(jxcOrderDeliveryCode)));
    }
    /**
     * 根据机身码查询记录
     */
    public void getByCode(@Para("") JxcCodeRequest jxcCodeRequest){
        log.info("=======根据机身码查询记录");
        JxcOrderDeliveryCode jxcOrderDeliveryCode = new JxcOrderDeliveryCode();
        jxcOrderDeliveryCode.setCode(jxcCodeRequest.getCode());
        List<JxcOrderDeliveryCode> list = jxcCodeService.getByCode(jxcOrderDeliveryCode);
        renderJson(R.ok().put("data",list));
    }
    /**
     * 根据机身码和客户编号查询记录
     */
    public void getByCodeAndCustomer(@Para("") JxcCodeRequest jxcCodeRequest){
        log.info("=======根据机身码和客户编号查询记录");
        JxcOrderDeliveryCode jxcOrderDeliveryCode = new JxcOrderDeliveryCode();
        jxcOrderDeliveryCode.setCode(jxcCodeRequest.getCode());
        jxcOrderDeliveryCode.setCustomer(jxcCodeRequest.getCustomer());
        jxcOrderDeliveryCode = jxcCodeService.getByCodeAndCustomer(jxcOrderDeliveryCode);
        renderJson(R.ok().put("data",jxcOrderDeliveryCode));
    }

    /**
     * 创建其他机身码信息
     */
    public void createOtherDeliveryCode(@Para("") JxcCodeRequest jxcCodeRequest){
        log.info("=======创建其他入库锁");
        JxcOrderDeliveryCode deliveryCode = initJxcOrderDeliveryCode(jxcCodeRequest);
        deliveryCode.setDocno("other");
        deliveryCode.setCustomer(jxcCodeRequest.getCustomer());
        deliveryCode.save();
        renderJson(R.ok().put("data",deliveryCode));
    }
    /**
     * 入库
     */
    public void updateDeliveryCode(@Para("") JxcCodeRequest jxcCodeRequest){
        log.info("=======更新机身码状态");
        JxcOrderDeliveryCode jxcOrderDeliveryCode = new JxcOrderDeliveryCode();
        jxcOrderDeliveryCode.setCode(jxcCodeRequest.getCode());
        jxcOrderDeliveryCode.setCustomer(jxcCodeRequest.getCustomer());
        jxcOrderDeliveryCode = jxcCodeService.getByCodeAndCustomer(jxcOrderDeliveryCode);
        jxcOrderDeliveryCode.setRkTime(new Date());
        jxcOrderDeliveryCode.setRkZt(1);
        jxcOrderDeliveryCode.update();
        renderJson(R.ok().put("data",jxcOrderDeliveryCode));
    }

    /**
     * 批量入库
     */
    public void updateDeliveryCodeByDocno(@Para("") JxcCodeRequest JxcCodeRequest){
        log.info("=======批量更新机身码入库状态");
        JxcOrderDeliveryCode jxcOrderDeliveryCode = new JxcOrderDeliveryCode();
        jxcOrderDeliveryCode.setDocno(JxcCodeRequest.getDocno());
        List<JxcOrderDeliveryCode> list = jxcCodeService.getByDocno(jxcOrderDeliveryCode);
        list.stream().forEach(item->{
            item.setRkTime(new Date());
            item.setRkZt(1);
            item.update();
        });
        renderJson(R.ok().put("data",jxcOrderDeliveryCode));
    }
    /**
     * 出库
     */
    public void updateOutDeliveryCode(@Para("") JxcCodeRequest jxcCodeRequest){
        log.info("=======更新机身码出库状态");
        JxcOrderDeliveryCode jxcOrderDeliveryCode = new JxcOrderDeliveryCode();
        jxcOrderDeliveryCode.setCode(jxcCodeRequest.getCode());
        jxcOrderDeliveryCode.setCustomer(jxcCodeRequest.getCustomer());
        jxcOrderDeliveryCode = jxcCodeService.getByCodeAndCustomer(jxcOrderDeliveryCode);
        if("other".equals(jxcOrderDeliveryCode.getDocno())){
            jxcOrderDeliveryCode.setOutType(jxcCodeRequest.getOutType());
            jxcOrderDeliveryCode.setShop(jxcCodeRequest.getShop());
        }
        jxcOrderDeliveryCode.setCkTime(new Date());
        jxcOrderDeliveryCode.setCkZt(1);
        jxcOrderDeliveryCode.update();
        renderJson(R.ok().put("data",jxcOrderDeliveryCode));
    }
    /**
     * 销售
     */
    public void updateSaleDeliveryCode(@Para("") JxcCodeRequest jxcCodeRequest){
        log.info("=======销售");
        JxcOrderDeliveryCode jxcOrderDeliveryCode = new JxcOrderDeliveryCode();
        jxcOrderDeliveryCode.setCode(jxcCodeRequest.getCode());
        jxcOrderDeliveryCode.setCustomer(jxcCodeRequest.getCustomer());
        jxcOrderDeliveryCode = jxcCodeService.getByCodeAndCustomer(jxcOrderDeliveryCode);
        jxcOrderDeliveryCode.setSalePrice(jxcCodeRequest.getSalePrice());
        jxcOrderDeliveryCode.setSalor(jxcCodeRequest.getSalor());
        jxcOrderDeliveryCode.setSaleTime(new Date());
        jxcOrderDeliveryCode.setXsZt(1);
        jxcOrderDeliveryCode.update();
        renderJson(R.ok().put("data",jxcOrderDeliveryCode));
    }

    /**
     * 根据团队编码查负责人
     */
    public void getPortEmpByTeamNo(String teamNo){
        renderJson(R.ok().put("data",portEmpService.getPortEmpByTeamNo(teamNo)));
    }


}
