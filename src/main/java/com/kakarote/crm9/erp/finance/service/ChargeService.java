package com.kakarote.crm9.erp.finance.service;

import BP.Difference.SystemConfig;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Db;
import com.kakarote.crm9.common.service.HttpService;
import com.kakarote.crm9.erp.finance.vo.HrGongdanFinanceCharge;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Data
@Slf4j
public class ChargeService {

    @Inject
    private HttpService httpService;

    @Getter
    private String path = SystemConfig.getCS_AppSettings().get("T100.PATH").toString();

    /*
     * @Description //调用T100费用接口
     * @Author wangkaida
     * @Date 14:54 2021/2/25
     * @Param []
     * @return boolean
     **/
    public boolean callT100ChargeApi() throws Exception {
        JSONObject callPrama = new JSONObject();
        callPrama.fluentPut("host",new JSONObject().fluentPut("acct","tiptop"));
        callPrama.fluentPut("service",new JSONObject().fluentPut("prod","T100").fluentPut("name","get_xmdc_lsae").fluentPut("ip","39.108.226.152").fluentPut("id","topprd"));
        callPrama.fluentPut("datakey",new JSONObject().fluentPut("CompanyId","TN10").fluentPut("EntId","100"));
        callPrama.fluentPut("payload",new JSONObject().fluentPut("std_data",new JSONObject().fluentPut("parameter",new JSONObject().fluentPut("type",4))));
        String t100Return = httpService.gatewayRequestJson(path, callPrama.toJSONString());
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
                    String nmbauc014 = chargeJson.getString("nmbauc014");
                    String nmbauc021 = chargeJson.getString("nmbauc021");
                    BigDecimal money = chargeJson.getBigDecimal("money");

                    HrGongdanFinanceCharge hrGongdanFinanceChargeDb = HrGongdanFinanceCharge.dao.findFirst(Db.getSql("admin.hrGongdanFinanceCharge.getHrGongdanFinanceChargeByNo"), nmbaucdocno);
                    HrGongdanFinanceCharge hrGongdanFinanceCharge = new HrGongdanFinanceCharge();
                    hrGongdanFinanceCharge.setNmbaucdocno(nmbaucdocno);
                    hrGongdanFinanceCharge.setNmbauc014(nmbauc014);
                    hrGongdanFinanceCharge.setNmbauc021(nmbauc021);
                    hrGongdanFinanceCharge.setMoney(money);
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
}