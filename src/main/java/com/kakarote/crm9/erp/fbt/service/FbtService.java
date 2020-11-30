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

    public boolean createDeptOrEmp(DeptReq deptReq,String deptInfoUrl) throws Exception {

        String accessToken = deptReq.getAccessToken();

        if (StringUtils.isNotBlank(accessToken)) {
            long timestamp = System.currentTimeMillis();

            String sign = SignUtil.getSign(String.valueOf(timestamp),deptReq.getData(),signKey);

            Map currentDeptInfoPrama = new HashMap();
            currentDeptInfoPrama.put("access_token", accessToken);
            currentDeptInfoPrama.put("timestamp", String.valueOf(timestamp));
            currentDeptInfoPrama.put("employee_id", deptReq.getEmployee_id());
            currentDeptInfoPrama.put("employee_type", deptReq.getEmployee_type());
            currentDeptInfoPrama.put("sign", sign);
            currentDeptInfoPrama.put("data", deptReq.getData());
            String deptReturn = tokenService.gatewayRequest(deptInfoUrl, currentDeptInfoPrama);
            if (deptReturn != null) {
                JSONObject deptResult = JSONObject.parseObject(deptReturn);
                if(deptResult.getInteger("code") == 0
                        && "success".equals(deptResult.getString("msg"))){
                    return true;
                }
            }

        }
        return false;
    }

    public String getAccessToken() throws Exception {
        //沙箱环境
        String tokenUrl = "https://open-plus-test.fenbeijinfu.com/open/api/auth/v1/dispense";
        //生产环境
        //String tokenUrl = "https://open-plus.fenbeitong.com/open/api/auth/v1/dispense";

        Map currentDeptInfoPrama = new HashMap();
        currentDeptInfoPrama.put("app_id", appId);
        currentDeptInfoPrama.put("app_key", appKey);
        String tokenReturn = tokenService.gatewayRequest(tokenUrl, currentDeptInfoPrama);

        JSONObject tokenResult = JSONObject.parseObject(tokenReturn);
        if(tokenResult.getInteger("code") == 0
          && tokenResult.getJSONObject("data") !=null){
            return tokenResult.getJSONObject("data").getString("access_token");
        }
        return null;
    }

}
