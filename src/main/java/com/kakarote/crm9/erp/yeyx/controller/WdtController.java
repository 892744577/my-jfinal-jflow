package com.kakarote.crm9.erp.yeyx.controller;

import cn.hutool.core.util.StrUtil;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.kakarote.crm9.erp.wx.util.DateUtil;
import com.kakarote.crm9.erp.yeyx.entity.vo.HrGongdanWdtTradeRequest;
import com.kakarote.crm9.erp.yeyx.service.WdtService;
import com.kakarote.crm9.utils.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class WdtController extends Controller {

    @Inject
    private WdtService wdtService;

    public void getOrder(@Para("") HrGongdanWdtTradeRequest hrGongdanWdtTradeRequest) throws Exception{
//        renderJson(R.ok().put("data",wdtService.gatewayRequest("http://qimen.api.taobao.com/router/qmtest",wdtService.getQmJsonData(new HashMap()))));
        Map map = new HashMap();
        map.put("start_time", hrGongdanWdtTradeRequest.getStartTime());
        map.put("end_time", hrGongdanWdtTradeRequest.getEndTime());
//        map.put("start_time", "2021-01-10 10:00:00");
//        map.put("end_time", "2021-01-10 11:00:00");
//        String result = wdtService.gatewayRequest("https://sandbox.wangdian.cn/openapi2/trade_query.php",wdtService.getJsonData(map));
//        String result = wdtService.gatewayRequest("https://api.wangdian.cn/openapi2/trade_query.php",wdtService.getJsonData(map));
        String result = wdtService.gatewayRequest("http://hu3cgwt0tc.api.taobao.com/router/qm",wdtService.getQmJsonData(map));
        boolean saveResult = wdtService.saveTradeInfo(result);
        if (saveResult == true) {
            renderJson(R.ok().put("msg", "遍历保存销售订单信息到数据库成功!").put("code", "000000"));
        }else {
            renderJson(R.error("遍历保存销售订单信息到数据库失败!").put("data", null).put("code", "000005"));
            return;
        }
//        renderJson(R.ok().put("data",wdtService.gatewayRequestJson("https://sandbox.wangdian.cn/openapi2/trade_query.php",wdtService.getJsonData(map))));
    }

    /*
     * @Description //根据年月遍历获取销售订单数据信息
     * @Author wangkaida
     * @Date 11:32 2021/1/14
     * @Param []
     * @return void
     **/
    public void getOrderByYearMonth(@Para("year") String year) throws Exception{
        if(StrUtil.isEmpty(year)){
            renderJson(R.error("年份不能为空!").put("data",null).put("code","000001"));
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        DecimalFormat decimalFormat = new DecimalFormat("00");
        for (int i = 1; i <= 12; i++) {
            String month = decimalFormat.format(i);
            int days = DateUtil.getDaysOfMonth(sdf.parse(year+"-"+month));
            for (int j = 1; j <= days; j++) {
                String day = decimalFormat.format(j);
                for (int k = 0; k < 24 ; k++) {
                    Map map = new HashMap();
                    String sHour = decimalFormat.format(k);
                    String eHour = decimalFormat.format(k+1);
                    map.put("start_time", year+"-"+month+"-"+day+" "+sHour+":00:00");
                    map.put("end_time", year+"-"+month+"-"+day+" "+eHour+":00:00");
                    String result = wdtService.gatewayRequest("http://hu3cgwt0tc.api.taobao.com/router/qm",wdtService.getQmJsonData(map));
                    boolean saveResult = wdtService.saveTradeInfo(result);
                    if (saveResult == true) {
                        renderJson(R.ok().put("msg", "遍历保存销售订单信息到数据库成功!").put("code", "000000"));
                    }else {
                        renderJson(R.error("遍历保存销售订单信息到数据库失败!"+year+"-"+month+"-"+day+" "+sHour+":00:00"+"到"+year+"-"+month+"-"+day+" "+eHour+":00:00").put("data", null).put("code", "000005"));
                    }
                }
            }
        }
    }

}