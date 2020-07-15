package com.kakarote.crm9.erp.yeyx.controller;

import BP.DA.DBAccess;
import BP.DA.Paras;
import BP.Difference.SystemConfig;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.admin.entity.PortEmp;
import com.kakarote.crm9.erp.yeyx.entity.HrGongdan;
import com.kakarote.crm9.erp.yeyx.entity.vo.HrGongdanRequest;
import com.kakarote.crm9.erp.yeyx.service.HrGongDanService;
import com.kakarote.crm9.erp.yzj.service.TokenService;
import com.kakarote.crm9.utils.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HrGongDanController extends Controller {

    @Inject
    private HrGongDanService hrGongDanService;

    @Inject
    private TokenService tokenService;

    /**
     * @author tmr
     * 分页工单查询数据
     */
    public void queryPageList(BasePageRequest basePageRequest) {
        renderJson(R.ok().put("data",hrGongDanService.queryPageList(basePageRequest)));
    }

    /**
     * 校验数据
     * @param hrGongdanRequest
     */
    public void validate(@Para("") HrGongdanRequest hrGongdanRequest){

        renderJson(R.ok().put("data",hrGongDanService.update(hrGongdanRequest)));
    }

    /**
     * 批量校验数据
     */
    public void validateMore(){
        JSONArray array = JSON.parseArray(this.getRawData());
        List<HrGongdanRequest> list= array.toJavaList(HrGongdanRequest.class);
        for(int i=0;i<list.size();i++){
            hrGongDanService.update(list.get(i));
        }
        renderJson(R.ok());
    }

    /*
     * @Description //根据订单号获取保修卡信息接口
     * @Author wangkaida
     * @Date 11:06 2020/7/14
     * @Param [hrGongdanRequest]
     * @return void
     **/
    public void getWarrantyCardByShippingOrderNo(@Para("") HrGongdanRequest hrGongdanRequest){
        //根据orderId获取出货单号
        HrGongdan hrGongdanDb = HrGongdan.dao.findFirst(Db.getSqlPara("admin.hrGongDan.getHrGongDanByOrderId", Kv.by("orderId",hrGongdanRequest.getOrderId())));
        if (hrGongdanDb != null) {
            String shippingOrderNo = hrGongdanDb.getShippingOrderNo();

            String url = "http://app.aptenon.com:80/api/v1/tenon-social-adapter/tenon/weixin/warrantyCards/getWarrantyCardByShippingOrderNo";
            try {
                if (StrUtil.isNotEmpty(shippingOrderNo)) {
                    Map currentPrama = new HashMap();
                    currentPrama.put("shippingOrderNo", shippingOrderNo);
                    String result = tokenService.gatewayRequest(url, currentPrama);
                    JSONArray resultArray = JSONObject.parseObject(result).getJSONArray("data");
                    renderJson(R.ok().put("data", resultArray).put("code","000000"));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }else {
            renderJson(R.error("根据orderId获取到的工单信息为空!").put("data",null).put("code","000048"));
            return;
        }

    }

    /*
     * @Description //根据订单号批量获取保修卡信息接口
     * @Author wangkaida
     * @Date 15:24 2020/7/14
     * @Param [hrGongdanRequest]
     * @return void
     **/
    public void getWarrantyCardByShippingOrderNoArray(@Para("") HrGongdanRequest hrGongdanRequest){
        String orderIdJson = hrGongdanRequest.getOrderId();
        JSONArray orderIdArray = JSONObject.parseObject(orderIdJson).getJSONArray("orderId");
        List<String> shippingOrderNoList = new ArrayList<String>();
        for (Object orderId: orderIdArray) {
            //根据orderId获取出货单号
            HrGongdan hrGongdanDb = HrGongdan.dao.findFirst(Db.getSqlPara("admin.hrGongDan.getHrGongDanByOrderId", Kv.by("orderId",orderId.toString())));
            if (hrGongdanDb != null) {
                shippingOrderNoList.add(hrGongdanDb.getShippingOrderNo());
            }
        }
        String[] shippingOrderNoArray = shippingOrderNoList.toArray(new String[shippingOrderNoList.size()]);

        String url = "http://app.aptenon.com:80/api/v1/tenon-social-adapter/tenon/weixin/warrantyCards/getWarrantyCardByShippingOrderNoArray";
        try {
            String result = tokenService.gatewayRequestJson(url, JSONObject.toJSONString(shippingOrderNoArray));
            JSONArray resultArray = JSONObject.parseObject(result).getJSONArray("data");
            renderJson(R.ok().put("data", resultArray).put("code","000000"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
