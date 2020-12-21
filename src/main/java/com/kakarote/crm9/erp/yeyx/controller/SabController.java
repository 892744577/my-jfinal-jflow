package com.kakarote.crm9.erp.yeyx.controller;

import BP.Port.Emp;
import BP.Tools.StringUtils;
import BP.WF.GenerWorkFlow;
import BP.WF.SendReturnObjs;
import BP.Web.WebUser;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import com.jfinal.core.NotAction;
import com.jfinal.core.paragetter.Para;
import com.kakarote.crm9.erp.yeyx.entity.HrGongdan;
import com.kakarote.crm9.erp.yeyx.entity.HrGongdanSabLog;
import com.kakarote.crm9.erp.yeyx.entity.vo.ToDoRequest;
import com.kakarote.crm9.utils.R;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Hashtable;

@Slf4j
public class SabController extends Controller {

    /**
     * 订单节点回统一入口
     */
    public void toDo(@Para("") ToDoRequest toDoRequest){
        if(toDoRequest.getJsonData() == null || "".equals(toDoRequest.getJsonData())) {
            renderJson(R.ok().put("code",40003).put("message","参数不能为空"));
        }else {
            try {
                log.info("==================当前登陆人：" + WebUser.getNo());
                if (WebUser.getNo() != "") {

                } else {
                    WebUser.SignInOfGenerAuth(new Emp("PengHaiFeng"), "PengHaiFeng");
                }
                JSONObject jsonObject = JSONObject.parseObject(toDoRequest.getJsonData());
                log.info("==================锁安帮推送参数："+ toDoRequest.getJsonData());
                if("master_info".equals(toDoRequest.getFunId())) {

                    log.info("==================订单节点回调通知备注master_info");
                    HrGongdanSabLog hrGongdanSabLog = saveHrGongdanSabLog(
                            toDoRequest.getFunId(),
                            jsonObject.getString("thirdOrderId"),
                            jsonObject.getString("ShowCode"),
                            (new Date()).getTime());
                    if(StringUtils.isNotBlank(jsonObject.getString("ConfirmTime"))){
                        hrGongdanSabLog.setDutyTime(jsonObject.getString("ConfirmTime"));
                    }
                    hrGongdanSabLog.setMasterName(jsonObject.getString("MasterWorkerName"));
                    hrGongdanSabLog.setMasterPhone(jsonObject.getString("MasterWorkerMobile"));
                    hrGongdanSabLog.save();
                    //流转到分派成功
                    this.master_info(hrGongdanSabLog);
                }else if ("master_visit".equals(toDoRequest.getFunId())) {
                    log.info("==================订单节点回调通知备注master_visit");
                    HrGongdanSabLog hrGongdanSabLog = saveHrGongdanSabLog(
                            toDoRequest.getFunId(),
                            jsonObject.getString("thirdOrderId"),
                            jsonObject.getString("ShowCode"),
                            (new Date()).getTime());
                    hrGongdanSabLog.save();
                    this.master_visit(hrGongdanSabLog);
                }else if ("order_complete".equals(toDoRequest.getFunId())) {
                    log.info("==================订单节点回调通知备注order_complete");
                    HrGongdanSabLog hrGongdanSabLog = saveHrGongdanSabLog(
                            toDoRequest.getFunId(),
                            jsonObject.getString("thirdOrderId"),
                            jsonObject.getString("ShowCode"),
                            (new Date()).getTime());
                    hrGongdanSabLog.setCompleteTime(jsonObject.getString("FinishTime"));
                    hrGongdanSabLog.setCompleteUrl(jsonObject.getString("FinishImages"));
                    hrGongdanSabLog.save();
                    this.order_complete(hrGongdanSabLog);
                }else if ("order_cancel".equals(toDoRequest.getFunId())) {
                    log.info("==================订单节点回调通知备注order_cancel");
                    HrGongdanSabLog hrGongdanSabLog = saveHrGongdanSabLog(
                            toDoRequest.getFunId(),
                            jsonObject.getString("thirdOrderId"),
                            jsonObject.getString("ShowCode"),
                            (new Date()).getTime());
                    hrGongdanSabLog.save();
                }
            } catch (Exception e) {
                log.error("==================接收信息流转异常原因：", e);
                log.error("==================接收信息流转异常堆栈：", e.getStackTrace());
                renderJson(R.ok().put("IsSuccess", false).put("message", "未知错误"));
            }
        }
        renderJson(R.ok().put("IsSuccess",true));
    }

    @NotAction
    public HrGongdanSabLog saveHrGongdanSabLog(String funcId,String thirdOrderId,String orderId,Long optTime){
        //录入记录
        HrGongdanSabLog hrGongdanSabLog = new HrGongdanSabLog();
        hrGongdanSabLog.setFuncId(funcId);
        hrGongdanSabLog.setThirdOrderId(thirdOrderId);
        hrGongdanSabLog.setOrderId(orderId);
        hrGongdanSabLog.setOptTime(optTime);
        return hrGongdanSabLog;
    }

    /**
     * 确认订单->分派成功
     */
    @NotAction
    public void master_info(HrGongdanSabLog hrGongdanSabLog) throws Exception{
        //若当前节点不是902，则不流转，直接更新数据
        GenerWorkFlow gwf = new GenerWorkFlow();
        gwf.setWorkID(Long.parseLong(hrGongdanSabLog.getThirdOrderId().split("-")[1]));
        gwf.RetrieveFromDBSources();

        //若是确认订单节点，往下流转，其他得只是更新数据
        if(915 == gwf.getFK_Node()){
            //发送流程
            Hashtable myhtSend = new Hashtable();
            myhtSend.put("masterName", hrGongdanSabLog.getMasterName());
            myhtSend.put("masterPhone", hrGongdanSabLog.getMasterPhone());
            SendReturnObjs returnObjs = BP.WF.Dev2Interface.Node_SendWork(
                    hrGongdanSabLog.getThirdOrderId().split("-")[0],
                    Long.parseLong(hrGongdanSabLog.getThirdOrderId().split("-")[1]),
                    myhtSend,null,916,null);
        }else{
            HrGongdan hrGongdan = new HrGongdan();
            hrGongdan.setOID(Integer.valueOf(hrGongdanSabLog.getThirdOrderId().split("-")[1]));
            hrGongdan.setMasterName(hrGongdanSabLog.getMasterName());
            hrGongdan.setMasterPhone(hrGongdanSabLog.getMasterPhone());
            hrGongdan.update();
        }

    }

    /**
     * 分派成功->预约确认
     * @param hrGongdanSabLog
     * @throws Exception
     */
    @NotAction
    public void master_visit(HrGongdanSabLog hrGongdanSabLog) throws Exception{
        //若当前节点不是903，则不流转，直接更新数据
        GenerWorkFlow gwf = new GenerWorkFlow();
        gwf.setWorkID(Long.parseLong(hrGongdanSabLog.getThirdOrderId().split("-")[1]));
        gwf.RetrieveFromDBSources();

        //若是确认订单节点，往下流转，其他得只是更新数据
        if(916 == gwf.getFK_Node()){
            //发送流程
            Hashtable myhtSend = new Hashtable();
            SendReturnObjs returnObjs = BP.WF.Dev2Interface.Node_SendWork(
                    hrGongdanSabLog.getThirdOrderId().split("-")[0],
                    Long.parseLong(hrGongdanSabLog.getThirdOrderId().split("-")[1]),
                    myhtSend,null,917,null);
        }

    }


    /**
     * 预约确认->完成
     * @param hrGongdanSabLog
     * @throws Exception
     */
    @NotAction
    public void order_complete(HrGongdanSabLog hrGongdanSabLog) throws Exception{
        //若当前节点不是905，则不流转，直接更新数据
        GenerWorkFlow gwf = new GenerWorkFlow();
        gwf.setWorkID(Long.parseLong(hrGongdanSabLog.getThirdOrderId().split("-")[1]));
        gwf.RetrieveFromDBSources();

        //若是确认订单节点，往下流转，其他得只是更新数据
        if(917 == gwf.getFK_Node() || 916 == gwf.getFK_Node()){
            Hashtable myhtSend = new Hashtable();
            //发送流程
            myhtSend.put("productPictureUrls", hrGongdanSabLog.getCompleteUrl());
            SendReturnObjs returnObjs = BP.WF.Dev2Interface.Node_SendWork(
                    hrGongdanSabLog.getThirdOrderId().split("-")[0],
                    Long.parseLong(hrGongdanSabLog.getThirdOrderId().split("-")[1]),
                    myhtSend, null, 906, null);
        }else{
            HrGongdan hrGongdan = new HrGongdan();
            hrGongdan.setOID(Integer.valueOf(hrGongdanSabLog.getThirdOrderId().split("-")[1]));
            hrGongdan.setProductPictureUrls(hrGongdanSabLog.getCompleteUrl());
            hrGongdan.update();
        }
    }

    @NotAction
    public void order_cancel(HrGongdanSabLog hrGongdanSabLog) throws Exception{
        Hashtable myhtSend = new Hashtable();
        SendReturnObjs returnObjs = BP.WF.Dev2Interface.Node_SendWork(
                hrGongdanSabLog.getThirdOrderId().split("-")[0],
                Long.parseLong(hrGongdanSabLog.getThirdOrderId().split("-")[1]),
                myhtSend, null, 907, null);
    }
}
