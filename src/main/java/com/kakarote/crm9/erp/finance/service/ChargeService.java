package com.kakarote.crm9.erp.finance.service;

import BP.Difference.SystemConfig;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Db;
import com.kakarote.crm9.common.service.HttpService;
import com.kakarote.crm9.erp.finance.vo.HrGongdanFinanceCharge;
import com.kakarote.crm9.erp.finance.vo.HrGongdanFinanceChargeService;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class ChargeService {

    @Inject
    private HttpService httpService;

    @Getter
    private String path = SystemConfig.getCS_AppSettings().get("T100.PATH").toString();

    /**
     * 公共参数
     * @param paramJson
     * @return
     */
    public JSONObject getT100Public(JSONObject paramJson){
        JSONObject callPrama = new JSONObject();
        callPrama.fluentPut("host",new JSONObject().fluentPut("acct","tiptop"));
        callPrama.fluentPut("service",new JSONObject().fluentPut("prod","T100").fluentPut("name","get_xmdc_lsae").fluentPut("ip","39.108.226.152").fluentPut("id","topprd"));
        callPrama.fluentPut("datakey",new JSONObject().fluentPut("CompanyId","TN10").fluentPut("EntId","100"));
        callPrama.fluentPut("payload",new JSONObject().fluentPut("std_data",paramJson));
        return callPrama;
    }

    /**
     * 请求参数
     * @param paramJson
     * @return
     */
    public JSONObject getT100PublicParam1(JSONObject paramJson) {
        JSONObject paramJsonMore = new JSONObject().fluentPut("parameter",paramJson);
        return getT100Public(paramJsonMore);
    }
    public JSONObject getT100PublicParam2(JSONObject paramJson,JSONArray detailListJson) {
        JSONObject paramJsonMore = new JSONObject().fluentPut("parameter",paramJson).fluentPut("detailListJson",detailListJson);
        return getT100Public(paramJsonMore);
    }

    /**
     * 调用T100费用接口--安装费
     * @return
     * @throws Exception
     */
    public boolean callT100ChargeApi4() throws Exception {
        JSONObject paramJson = new JSONObject().fluentPut("type",4);
        String t100Return = httpService.gatewayRequestJson(path, getT100PublicParam1(paramJson).toJSONString());
        JSONObject t100Result = JSONObject.parseObject(t100Return);
        if (t100Result != null) {
            //code = "0"表示查询成功
            String code = t100Result.getJSONObject("payload").getJSONObject("std_data").getJSONObject("execution").getString("code");
            if (!"0".equals(code)) {
                log.info("调用T100费用接口失败!");
                return false ;
            }else {
                JSONArray jsonArray = t100Result.getJSONObject("payload").getJSONObject("std_data").getJSONObject("parameter").getJSONArray("rows");
                for (Object chargeObject : jsonArray) {
                    JSONObject chargeJson = (JSONObject)chargeObject;
                    //遍历保存到数据库,存在则更新,不存在则插入
                    String nmbaucdocno = chargeJson.getString("nmbaucdocno");
                    HrGongdanFinanceCharge hrGongdanFinanceChargeDb = HrGongdanFinanceCharge.dao.findFirst(Db.getSql("admin.hrGongdanFinanceCharge.getHrGongdanFinanceChargeByNo"), nmbaucdocno);
                    HrGongdanFinanceCharge hrGongdanFinanceCharge = new HrGongdanFinanceCharge();
                    hrGongdanFinanceCharge.setNmbaucdocno(nmbaucdocno);
                    hrGongdanFinanceCharge.setNmbauc014(chargeJson.getString("nmbauc014"));
                    hrGongdanFinanceCharge.setNmbauc021(chargeJson.getString("nmbauc021"));
                    hrGongdanFinanceCharge.setMoney(chargeJson.getBigDecimal("money"));
                    if (hrGongdanFinanceChargeDb == null) {
                        hrGongdanFinanceCharge.save();
                    }else {
                        hrGongdanFinanceCharge.update();
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 调用T100费用接口--无忧服务费
     * @return
     * @throws Exception
     */
    public boolean callT100ChargeApi5() throws Exception {
        JSONObject paramJson = new JSONObject().fluentPut("type",5);
        String t100Return = httpService.gatewayRequestJson(path, getT100PublicParam1(paramJson).toJSONString());
        JSONObject t100Result = JSONObject.parseObject(t100Return);
        if (t100Result != null) {
            //code = "0"表示查询成功
            String code = t100Result.getJSONObject("payload").getJSONObject("std_data").getJSONObject("execution").getString("code");
            if (!"0".equals(code)) {
                log.info("调用T100费用接口失败!");
                return false ;
            }else {
                JSONArray jsonArray = t100Result.getJSONObject("payload").getJSONObject("std_data").getJSONObject("parameter").getJSONArray("rows");
                for (Object chargeObject : jsonArray) {
                    JSONObject chargeJson = (JSONObject)chargeObject;
                    //遍历保存到数据库,存在则更新,不存在则插入
                    String nmbaucdocno = chargeJson.getString("nmbaucdocno");
                    HrGongdanFinanceChargeService hrGongdanFinanceChargeServiceDb =
                            HrGongdanFinanceChargeService.dao.findFirst(Db.getSql("admin.hrGongdanFinanceChargeService.getHrGongdanFinanceChargeByNo"), nmbaucdocno);
                    HrGongdanFinanceChargeService hrGongdanFinanceChargeService = new HrGongdanFinanceChargeService();
                    hrGongdanFinanceChargeService.setNmbaucdocno(nmbaucdocno);
                    hrGongdanFinanceChargeService.setNmbauc014(chargeJson.getString("nmbauc014"));
                    hrGongdanFinanceChargeService.setNmbauc021(chargeJson.getString("nmbauc021"));
                    hrGongdanFinanceChargeService.setMoney(chargeJson.getBigDecimal("money"));
                    if (hrGongdanFinanceChargeServiceDb == null) {
                        hrGongdanFinanceChargeService.save();
                    }else {
                        hrGongdanFinanceChargeService.update();
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 调用T100费用接口--扣费
     * @param customerNo 团队编号
     * @param no  报备流程单号
     * @param num 数量
     * @return
     * @throws Exception
     */
    public JSONObject callT100ChargeApi6(String customerNo, String no, String num, JSONArray detailListJson) throws Exception {
        JSONObject paramJson = new JSONObject().fluentPut("type", 6).fluentPut("customerNo", customerNo).fluentPut("no", no).fluentPut("num", num);
        String t100Return = httpService.gatewayRequestJson(path, getT100PublicParam2(paramJson,detailListJson).toJSONString());
        JSONObject t100Result = JSONObject.parseObject(t100Return);
        if (t100Result != null) {
            //code = "0"表示查询成功
            return t100Result.getJSONObject("payload").getJSONObject("std_data").getJSONObject("execution");
        }else{
            return null;
        }
    }


    /**
     * 调用T100保修机身码接口-全部
     * @return
     * @throws Exception
     */
    public JSONObject callT100ChargeApi7() throws Exception {
        JSONObject paramJson = new JSONObject().fluentPut("type", 7);
        String t100Return = httpService.gatewayRequestJson(path, getT100PublicParam1(paramJson).toJSONString());
        JSONObject t100Result = JSONObject.parseObject(t100Return);
        if (t100Result != null) {
            //code = "0"表示查询成功
            return t100Result.getJSONObject("payload").getJSONObject("std_data").getJSONObject("execution");
        }else{
            return null;
        }
    }

}