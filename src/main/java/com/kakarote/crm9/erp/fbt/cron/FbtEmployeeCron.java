package com.kakarote.crm9.erp.fbt.cron;

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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/*
 * @Description //分贝通同步人员定时任务
 * @Author wangkaida
 * @Date 13:29 2020/12/21
 **/
@Slf4j
public class FbtEmployeeCron implements Runnable {
    @Override
    public void run() {
        WebServiceConfig cfg = WebServiceConfig.getInstance();

        try {
            Object service = WebServiceClient.callService(cfg.getAddress(), cfg.getServiceClass());
            ISysSynchroGetOrgWebService isysSynchroGetOrgWebService = (ISysSynchroGetOrgWebService)service;
            SysSynchroGetOrgInfoContext sysSynchroGetOrgInfoContext =new SysSynchroGetOrgInfoContext();
            sysSynchroGetOrgInfoContext.setReturnOrgType("[{\"type\":\"person\"}]");
            sysSynchroGetOrgInfoContext.setCount(2000);
            String employeeReturn = JSON.toJSONString(isysSynchroGetOrgWebService.getUpdatedElements(sysSynchroGetOrgInfoContext));

            JSONObject employeeResult = JSONObject.parseObject(employeeReturn);
            if (employeeResult != null) {
                if(employeeResult.getInteger("returnState") == 2
                        && employeeResult.getString("message") !=null){
                    String message = employeeResult.getString("message");
                    JSONArray empArray = JSONArray.parseArray(message);
                    JSONArray employeeList = new JSONArray();
                    for (Object object: empArray) {
                        JSONObject jsonObject = (JSONObject)object;
                        //判断alterTime是昨天或者今天的记录才进行保存
                        String alterTime = jsonObject.getString("alterTime");
                        String parent = jsonObject.getString("parent");
                        String mobileNo = jsonObject.getString("mobileNo");
                        String name = jsonObject.getString("name");
                        String id = jsonObject.getString("id");
                        JSONObject customProps = jsonObject.getJSONObject("customProps");
                        String fenbeiquanxian = customProps.getString("fenbeiquanxian");
                        String alterTimeResult = DateUtil.getDateDetail(alterTime);
                        if (alterTimeResult != null) {
                            if (StringUtils.isNotBlank(parent) && StringUtils.isNotBlank(mobileNo)) {
                                JSONObject item = new JSONObject();
                                item.put("name",name);
                                item.put("phone",StringUtils.isNotBlank(mobileNo)?mobileNo:"");
                                item.put("third_org_unit_id",parent);
                                item.put("third_employee_id",id);
                                item.put("role_type",customProps != null && StringUtils.isNotBlank(fenbeiquanxian)?Integer.parseInt(fenbeiquanxian):"6");
                                employeeList.add(item);
                            }
                        }
                    }
                    JSONObject dataObject = new JSONObject();
                    dataObject.put("employee_list",employeeList);
                    String dataStr = dataObject.toJSONString();
                    FbtService fbtService = Aop.get(FbtService.class);
                    //获取分贝通AccessToken
                    String token = fbtService.getAccessToken();
                    if (!StringUtils.isNotBlank(token)) {
                        log.info("调用获取AccessToken接口出错!");
                        return;
                    }

                    //调用分贝通添加员工接口
                    DeptReq deptReq = new DeptReq();
                    deptReq.setEmployee_id("5fa9f84969fb75d268dc4071");
                    deptReq.setEmployee_type("0");
                    deptReq.setData(dataStr);
                    deptReq.setAccessToken(token);
                    boolean result = fbtService.createDeptOrEmp(deptReq,
                            fbtService.getPath()+"/openapi/func/employee/create");
                    if (result) {
                        log.info("调用添加员工接口成功!");
                    }else {
                        log.info("调用添加员工接口出错!");
                    }

                    boolean updateResult = fbtService.createDeptOrEmp(deptReq,
                            fbtService.getPath()+"/openapi/func/employee/update");
                    if (updateResult) {
                        log.info("调用更新员工接口成功!");
                    }else {
                        log.info("调用更新员工接口出错!");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}