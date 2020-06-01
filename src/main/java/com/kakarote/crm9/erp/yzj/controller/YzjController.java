package com.kakarote.crm9.erp.yzj.controller;

import com.kakarote.crm9.erp.admin.entity.PortDept;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.kakarote.crm9.erp.admin.entity.PortEmp;
import com.kakarote.crm9.erp.oa.entity.OaEvent;
import com.kakarote.crm9.erp.yzj.service.TokenService;
import com.kakarote.crm9.utils.R;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class YzjController extends Controller {

    @Inject
    private TokenService tokenService;

    /**
     * @author wyq
     * oa同步组织机构
     */
    public void queryList(@Para("") OaEvent oaEvent) throws Exception {
        String userInfoUrl = tokenService.getGatewayHost().concat("/openimport/open/dept/getall?accessToken=").concat(tokenService.getAccessToken(null, tokenService.getErpSecret(), tokenService.getEid(), "resGroupSecret"));
        Map currentUserInfoPrama = new HashMap();
        Map currentUserInfoJson= new HashMap();
        currentUserInfoJson.put("eid", tokenService.getEid());
        currentUserInfoPrama.put("data",JSONObject.toJSONString(currentUserInfoJson));
        currentUserInfoPrama.put("eid", tokenService.getEid());
        currentUserInfoPrama.put("nonce", UUID.randomUUID().toString().replace("-", "").substring(0, 15));
        String deptReturn = tokenService.gatewayRequest(userInfoUrl, currentUserInfoPrama);
        JSONObject deptListResult = JSONObject.parseObject(deptReturn);
        if("true".equals(deptListResult.get("success").toString())
                && deptListResult.getJSONArray("data") !=null
                && deptListResult.getJSONArray("data").size()>0){
            JSONArray deptList= deptListResult.getJSONArray("data");
            for(int i=0;deptList.size()>i;i++){
                JSONObject dept= deptList.getJSONObject(i);
                if("5af924c3e4b0738621a1cafb".equals(dept.getString("id"))){

                }else{
                    PortDept portDept = new PortDept();
                    portDept.setNo(dept.getString("id"));
                    portDept.setName(dept.getString("name"));
                    portDept.setNameOfPath(dept.getString("department"));
                    if("5af924c3e4b0738621a1cafb".equals(dept.getString("parentId"))){
                        portDept.setParentNo("100");
                    }else{
                        portDept.setParentNo(dept.getString("parentId"));
                    }
                    portDept.save();
                }

            }
        }
        renderJson(R.ok());
    }
}
