package com.kakarote.crm9.erp.fbt.cron;

import BP.Tools.DateUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Aop;
import com.jfinal.plugin.activerecord.Db;
import com.kakarote.crm9.erp.fbt.service.FbtService;
import com.kakarote.crm9.erp.fbt.vo.CheckDataCarOrder;
import com.kakarote.crm9.erp.fbt.vo.DeptReq;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class FbtCarOrderCron implements Runnable {

    @Override
    public void run() {
        FbtService fbtService = Aop.get(FbtService.class);
        DeptReq deptReq = new DeptReq();
        deptReq.setEmployee_id("5fa9f84969fb75d268dc4071");
        deptReq.setEmployee_type("1");
        Map map =new HashMap<>();
        //开始时间按表中最大结束时间。若为空，则不填
        String maxCheckoutDate = Db.queryStr(Db.getSql("admin.checkDataCarOrder.maxCheckoutDate"));
        if(maxCheckoutDate != null){
            try {
                Date date = DateUtils.parse(maxCheckoutDate,DateUtils.YMDHMS_PATTERN);
                Calendar maxCheckoutCalendar = Calendar.getInstance();
                maxCheckoutCalendar.setTime(date);
                maxCheckoutCalendar.add(Calendar.SECOND,1);
                maxCheckoutCalendar.add(Calendar.DATE,-1);
                String maxCheckoutFormat = DateUtils.format(maxCheckoutCalendar.getTime(),DateUtils.YEAR_MONTH_DAY_PATTERN_MIDLINE);
                map.put("create_time_begin", maxCheckoutFormat);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.add(Calendar.DATE,-1);
        String currentDateFormat = DateUtils.format(currentCalendar.getTime(),DateUtils.YEAR_MONTH_DAY_PATTERN_MIDLINE);
        map.put("create_time_end",currentDateFormat);
        map.put("page_size",500);


        deptReq.setData(JSON.toJSONString(map));
        try {
            String result = fbtService.getOrder(deptReq,
                    fbtService.getPath()+"/openapi/func/orders/car/list");
            JSONObject resultObject = JSONObject.parseObject(result);
            if(resultObject.getJSONArray("results") != null){
                JSONArray resultArray = resultObject.getJSONArray("results");
                for(int i=0;i<resultArray.size();i++){
                    JSONObject item = resultArray.getJSONObject(i);
                    //保存到数据库
                    CheckDataCarOrder checkDataCarOrder = new CheckDataCarOrder();

                    JSONObject orderInfo = item.getJSONObject("order_info");
                    checkDataCarOrder.setOrderId(orderInfo.getString("order_id"));
                    checkDataCarOrder.setStatus(orderInfo.getInteger("status"));
                    checkDataCarOrder.setStatusName(orderInfo.getString("status_name"));
                    checkDataCarOrder.setSupplierOrderId(orderInfo.getString("supplier_order_id"));
                    checkDataCarOrder.setSupplierName(orderInfo.getString("supplier_name"));
                    checkDataCarOrder.setDepartureTime(orderInfo.getString("departure_time"));
                    checkDataCarOrder.setDepartureName(orderInfo.getString("departure_name"));
                    checkDataCarOrder.setDepartureAddress(orderInfo.getString("departure_address"));
                    checkDataCarOrder.setArrivalTime(orderInfo.getString("arrival_time"));
                    checkDataCarOrder.setArrivalName(orderInfo.getString("arrival_name"));
                    checkDataCarOrder.setArrivalAddress(orderInfo.getString("arrival_address"));
                    checkDataCarOrder.setCreateTime(orderInfo.getString("create_time"));

                    JSONObject priceInfo = item.getJSONObject("price_info");
                    checkDataCarOrder.setTotalPrice(priceInfo.getBigDecimal("total_price"));
                    checkDataCarOrder.setPersonalTotalPay(priceInfo.getBigDecimal("personal_total_pay"));


                    JSONObject userInfo = item.getJSONObject("user_info");
                    checkDataCarOrder.setCustomerId(userInfo.getString("id"));
                    checkDataCarOrder.setCustomerName(userInfo.getString("name"));
                    checkDataCarOrder.setCustomerPhone(userInfo.getString("phone"));

                    JSONObject passengerInfo = item.getJSONObject("passenger_info");
                    checkDataCarOrder.setPassengerName(passengerInfo.getString("name"));
                    checkDataCarOrder.setPassengerPhone(passengerInfo.getString("phone"));
                    checkDataCarOrder.save();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
