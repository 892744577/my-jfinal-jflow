package com.kakarote.crm9.erp.yeyx.controller;

import BP.Port.Emp;
import BP.WF.SendReturnObjs;
import BP.Web.WebUser;
import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.kakarote.crm9.erp.yeyx.entity.HrGongdan;
import com.kakarote.crm9.erp.yeyx.entity.vo.*;
import com.kakarote.crm9.erp.yeyx.service.YeyxService;
import com.kakarote.crm9.utils.R;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Hashtable;

/**
 * 言而有信第三方订单平台接口
 */
@Slf4j
public class YeyxController extends Controller {

    @Inject
    private YeyxService yeyxService;

    /**
     * 改约，不涉及流程流转
     */
    public void duty_time(@Para("") DutyTimeRequest dutyTimeRequest){
        HrGongdan hrGongdan = new HrGongdan();
        hrGongdan.setOID(Integer.valueOf(dutyTimeRequest.getThirdOrderId().split("-")[1]));
        hrGongdan.setDutyTime(new Date(dutyTimeRequest.getDutyTime()));
        hrGongdan.update();
        log.info("==================接收改约时间："+JSON.toJSONString(dutyTimeRequest));
        renderJson(R.ok().put("code",200).put("message","成功"));
    }
    /**
     * 派单,流程从订单确认流转到下一环节，派单
     */
    public void master_info(@Para("") MasterInfoRequest masterInfoRequest){
        try {
            if(WebUser.getNo()!=""){

            }else{
                WebUser.SignInOfGenerAuth(new Emp("ZhouPan"), "ZhouPan");
            }
            Hashtable myhtSend = new Hashtable();
            //发送流程
            myhtSend.put("masterName", masterInfoRequest.getMasterName());
            myhtSend.put("masterPhone", masterInfoRequest.getMasterPhone());
            SendReturnObjs returnObjs = BP.WF.Dev2Interface.Node_SendWork(
                    masterInfoRequest.getThirdOrderId().split("-")[0],
                    Long.parseLong(masterInfoRequest.getThirdOrderId().split("-")[1]),
                    myhtSend,null,903,"ZhouPan");
            WebUser.Exit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("==================接收派单事件："+JSON.toJSONString(masterInfoRequest));
        renderJson(R.ok().put("code",200).put("message","成功"));
    }
    /**
     * 上门
     */
    public void master_visit(@Para("") MasterVisitRequest masterVisitRequest){
        try {
            if(WebUser.getNo()!=""){

            }else{
                WebUser.SignInOfGenerAuth(new Emp("ZhouPan"), "ZhouPan");
            }
            Hashtable myhtSend = new Hashtable();
            //发送流程
            SendReturnObjs returnObjs = BP.WF.Dev2Interface.Node_SendWork(
                    masterVisitRequest.getThirdOrderId().split("-")[0],
                    Long.parseLong(masterVisitRequest.getThirdOrderId().split("-")[1]),
                    myhtSend,null,905,"ZhouPan");
            WebUser.Exit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("==================接收派单事件："+JSON.toJSONString(masterVisitRequest));
        renderJson(R.ok().put("code",200).put("message","成功"));
    }

    /**
     * 完成
     */
    public void order_complete(@Para("") OrderCompleteRequest orderCompleteRequest){
        try {
            if(WebUser.getNo()!=""){

            }else{
                WebUser.SignInOfGenerAuth(new Emp("ZhouPan"), "ZhouPan");
            }
            Hashtable myhtSend = new Hashtable();
            //发送流程
            myhtSend.put("productPictureUrls", orderCompleteRequest.getProductPictureUrls());
            SendReturnObjs returnObjs = BP.WF.Dev2Interface.Node_SendWork(
                    orderCompleteRequest.getThirdOrderId().split("-")[0],
                    Long.parseLong(orderCompleteRequest.getThirdOrderId().split("-")[1]),
                    myhtSend,null,906,"ZhouPan");
            WebUser.Exit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("==================接收派单事件："+JSON.toJSONString(orderCompleteRequest));
        renderJson(R.ok().put("code",200).put("message","成功"));
    }

    /**
     * 商家备注，不涉及流程流转
     * @param factoryRemarkRequest
     */
    public void factory_remark(@Para("") FactoryRemarkRequest factoryRemarkRequest){
        renderJson(R.ok().put("code",200).put("message","成功"));
    }

}
