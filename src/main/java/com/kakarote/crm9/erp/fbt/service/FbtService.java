package com.kakarote.crm9.erp.fbt.service;

import BP.Difference.SystemConfig;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.kakarote.crm9.erp.fbt.util.SignUtil;
import com.kakarote.crm9.erp.fbt.vo.DeptReq;
import com.kakarote.crm9.erp.yzj.service.TokenService;
import lombok.Data;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Data
public class FbtService {

    @Inject
    private TokenService tokenService;

    @Getter
    private String appId = SystemConfig.getCS_AppSettings().get("FBT.appId").toString();

    @Getter
    private String appKey = SystemConfig.getCS_AppSettings().get("FBT.appKey").toString();

    @Getter
    private String signKey = SystemConfig.getCS_AppSettings().get("FBT.signKey").toString();

    @Getter
    private String path = SystemConfig.getCS_AppSettings().get("FBT.path").toString();

    /**
     * 处理请求参数
     * @param deptReq
     * @return
     * @throws Exception
     */
    public Map getParamMap(DeptReq deptReq) throws Exception {
        long timestamp = System.currentTimeMillis();
        String sign = SignUtil.getSign(String.valueOf(timestamp),deptReq.getData(),signKey);
        Map param = new HashMap();
        param.put("access_token", deptReq.getAccessToken());
        param.put("timestamp", String.valueOf(timestamp));
        param.put("employee_id", deptReq.getEmployee_id());
        param.put("employee_type", deptReq.getEmployee_type());
        param.put("sign", sign);
        param.put("data", deptReq.getData());
        return param;
    }

    /**
     * 处理结果
     * @param result
     * @return
     */
    public boolean doResult(String result){
        if (result != null) {
            JSONObject resultObject = JSONObject.parseObject(result);
            if (resultObject.getInteger("code") == 0
                    && "success".equals(resultObject.getString("msg"))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 创建人员或部门
     * @param deptReq
     * @param deptInfoUrl
     * @return
     * @throws Exception
     */
    public boolean createDeptOrEmp(DeptReq deptReq,String deptInfoUrl) throws Exception {
        String accessToken = deptReq.getAccessToken();
        if (StringUtils.isNotBlank(accessToken)) {
            Map currentDeptInfoParam = this.getParamMap(deptReq);
            String result = tokenService.gatewayRequest(deptInfoUrl, currentDeptInfoParam);
            return this.doResult(result);
        }
        return false;
    }

    /**
     * 创建行程申请单
     * @param deptReq
     * @param deptInfoUrl
     * @return
     * @throws Exception
     */
    public boolean travelOrder(DeptReq deptReq,String deptInfoUrl) throws Exception {
        String accessToken = this.getAccessToken();
        if (StringUtils.isNotBlank(accessToken)) {
            deptReq.setAccessToken(accessToken);
            Map currentTravelOrderParam = this.getParamMap(deptReq);
            String result = tokenService.gatewayRequest(deptInfoUrl, currentTravelOrderParam);
            return this.doResult(result);
        }
        return false;
    }

    /**
     * 获取accessToken
     * @return
     * @throws Exception
     */
    public String getAccessToken() throws Exception {
        Map currentDeptInfoPrama = new HashMap();
        currentDeptInfoPrama.put("app_id", appId);
        currentDeptInfoPrama.put("app_key", appKey);
        String tokenReturn = tokenService.gatewayRequest(path+"/open/api/auth/v1/dispense", currentDeptInfoPrama);
        JSONObject tokenResult = JSONObject.parseObject(tokenReturn);
        if (tokenResult != null) {
            if(tokenResult.getInteger("code") == 0
                    && tokenResult.getJSONObject("data") !=null){
                return tokenResult.getJSONObject("data").getString("access_token");
            }
        }
        return null;
    }
}
