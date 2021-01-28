package com.kakarote.crm9.erp.fbt.cron;

import BP.Difference.SystemConfig;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Aop;
import com.kakarote.crm9.erp.fbt.service.FbtService;
import com.kakarote.crm9.erp.fbt.vo.DeptReq;
import com.kakarote.crm9.erp.wx.util.DateUtil;
import com.landray.kmss.sys.organization.client.WebServiceClient;
import com.landray.kmss.sys.organization.client.WebServiceConfig;
import com.landray.kmss.sys.organization.webservice.out.ISysSynchroGetOrgWebService;
import com.landray.kmss.sys.organization.webservice.out.SysSynchroGetOrgInfoContext;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/*
 * @Description //分贝通同步部门定时任务
 * @Author wangkaida
 * @Date 9:59 2021/1/28
 **/
@Slf4j
public class FbtDeptCron implements Runnable {

    @Getter
    private String appId = SystemConfig.getCS_AppSettings().get("FBT.appId").toString();

    @Override
    public void run() {
        WebServiceConfig cfg = WebServiceConfig.getInstance();

        try {
            Object service = WebServiceClient.callService(cfg.getAddress(), cfg.getServiceClass());
            ISysSynchroGetOrgWebService isysSynchroGetOrgWebService = (ISysSynchroGetOrgWebService)service;
            SysSynchroGetOrgInfoContext sysSynchroGetOrgInfoContext =new SysSynchroGetOrgInfoContext();
            sysSynchroGetOrgInfoContext.setReturnOrgType("[{\"type\":\"dept\"}]");
            sysSynchroGetOrgInfoContext.setCount(2000);
            String deptReturn = JSON.toJSONString(isysSynchroGetOrgWebService.getUpdatedElements(sysSynchroGetOrgInfoContext));

            JSONObject deptResult = JSONObject.parseObject(deptReturn);
            if (deptResult != null) {
                if(deptResult.getInteger("returnState") == 2
                        && deptResult.getString("message") !=null){
                    String message = deptResult.getString("message");
                    JSONArray deptArray = JSONArray.parseArray(message);
                    FbtService fbtService = Aop.get(FbtService.class);
                    //获取分贝通AccessToken
                    String token = fbtService.getAccessToken();
                    if (!StringUtils.isNotBlank(token)) {
                        log.info("调用获取AccessToken接口出错!");
                        return;
                    }
                    for (Object object: deptArray) {
                        JSONObject jsonObject = (JSONObject)object;
                        //判断alterTime是昨天或者今天的记录才进行保存
                        String alterTime = jsonObject.getString("alterTime");
                        String parent = jsonObject.getString("parent");
                        String name = jsonObject.getString("name");
                        String id = jsonObject.getString("id");
                        boolean isAvailable = jsonObject.getBoolean("isAvailable");
                        String alterTimeResult = DateUtil.getDateDetail(alterTime);
                        if (alterTimeResult != null) {
                            if (isAvailable && StringUtils.isNotBlank(parent)) {
                                JSONObject item = new JSONObject();
                                item.put("company_id",appId);
                                item.put("org_unit_name",name);
                                item.put("third_parent_id",parent);
                                item.put("third_org_id",id);
                                item.put("operator_id","5fa9f84969fb75d268dc4071");


                                //调用分贝通添加部门接口
                                DeptReq deptReq = new DeptReq();
                                deptReq.setEmployee_id("5fa9f84969fb75d268dc4071");
                                deptReq.setEmployee_type("0");
                                deptReq.setData(item.toJSONString());
                                deptReq.setAccessToken(token);
                                boolean result = fbtService.createDeptOrEmp(deptReq,
                                        fbtService.getPath()+"/openapi/func/department/create");
                                if (result) {
                                    log.info("调用添加部门接口成功!");
                                }else {
                                    log.info("调用添加部门接口出错!");
                                }

                                boolean updateResult = fbtService.createDeptOrEmp(deptReq,
                                        fbtService.getPath()+"/openapi/func/department/update");
                                if (updateResult) {
                                    log.info("调用更新部门接口成功!");
                                }else {
                                    log.info("调用更新部门接口出错!");
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}