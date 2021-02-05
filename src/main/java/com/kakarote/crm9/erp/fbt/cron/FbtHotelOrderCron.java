package com.kakarote.crm9.erp.fbt.cron;

import BP.Tools.DateUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Aop;
import com.jfinal.plugin.activerecord.Db;
import com.kakarote.crm9.erp.fbt.service.FbtService;
import com.kakarote.crm9.erp.fbt.vo.CheckDataAnalysis2;
import com.kakarote.crm9.erp.fbt.vo.CheckDataHotelOrder;
import com.kakarote.crm9.erp.fbt.vo.DeptReq;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class FbtHotelOrderCron implements Runnable {

    @Override
    public void run() {
        FbtService fbtService = Aop.get(FbtService.class);
        DeptReq deptReq = new DeptReq();
        deptReq.setEmployee_id("5fa9f84969fb75d268dc4071");
        deptReq.setEmployee_type("1");
        Map map =new HashMap<>();
        //开始时间按表中最大结束时间。若为空，则不填
        String maxCheckoutDate = Db.queryStr(Db.getSql("admin.checkDataHotelOrder.maxCheckoutDate"));
        if(maxCheckoutDate != null){
            map.put("create_time_begin", maxCheckoutDate);
        }

        map.put("create_time_end",DateUtils.format(new Date(),DateUtils.YEAR_MONTH_DAY_PATTERN_MIDLINE));
        map.put("page_size",500);
        deptReq.setData(JSON.toJSONString(map));
        try {
            String result = fbtService.getOrder(deptReq,
                    fbtService.getPath()+"/openapi/func/orders/hotel/list");
            JSONObject resultObject = JSONObject.parseObject(result);
            if(resultObject.getJSONArray("results") != null){
                JSONArray resultArray = resultObject.getJSONArray("results");
                for(int i=0;i<resultArray.size();i++){
                    JSONObject item = resultArray.getJSONObject(i);
                    //保存到数据库
                    CheckDataHotelOrder checkDataHotelOrder = new CheckDataHotelOrder();

                    JSONObject orderInfo = item.getJSONObject("order_info");
                    checkDataHotelOrder.setOrderId(orderInfo.getString("order_id"));
                    checkDataHotelOrder.setStatus(orderInfo.getInteger("status"));
                    checkDataHotelOrder.setStatusName(orderInfo.getString("status_name"));
                    checkDataHotelOrder.setSupplierOrderId(orderInfo.getString("supplier_order_id"));
                    checkDataHotelOrder.setSupplierName(orderInfo.getString("supplier_name"));
                    checkDataHotelOrder.setCreateTime(orderInfo.getDate("create_time"));

                    JSONObject priceInfo = item.getJSONObject("price_info");
                    checkDataHotelOrder.setTotalPrice(priceInfo.getBigDecimal("total_price"));
                    checkDataHotelOrder.setPersonalTotalPay(priceInfo.getBigDecimal("personal_total_pay"));
                    checkDataHotelOrder.setTotalPayPrice(priceInfo.getBigDecimal("total_pay_price"));
                    checkDataHotelOrder.setActualPrice(priceInfo.getBigDecimal("actual_price"));


                    JSONObject userInfo = item.getJSONObject("user_info");
                    checkDataHotelOrder.setCustomerId(userInfo.getString("id"));
                    checkDataHotelOrder.setCustomerName(userInfo.getString("name"));
                    checkDataHotelOrder.setCustomerPhone(userInfo.getString("phone"));
                    checkDataHotelOrder.setUnitName(userInfo.getString("unit_name"));
                    checkDataHotelOrder.setFullOrgUnit(userInfo.getString("full_org_unit"));

                    JSONObject hotelInfo = item.getJSONObject("hotel_info");
                    checkDataHotelOrder.setHotelName(hotelInfo.getString("hotel_name"));
                    checkDataHotelOrder.setHotelPhone(hotelInfo.getString("hotel_phone"));
                    checkDataHotelOrder.setCheckinDate(hotelInfo.getDate("checkin_date"));
                    checkDataHotelOrder.setCheckoutDate(hotelInfo.getDate("checkout_date"));
                    checkDataHotelOrder.setCityName(hotelInfo.getString("city_name"));
                    checkDataHotelOrder.setHotelAddress(hotelInfo.getString("hotel_address"));
                    checkDataHotelOrder.setRoomType(hotelInfo.getString("room_type"));
                    checkDataHotelOrder.setDeal(0);
                    checkDataHotelOrder.save();

                    //固化结果，没啥意义
                    //计算天数
                    if(orderInfo.getInteger("status").equals(2501) || orderInfo.getInteger("status").equals(2800)){
                        Calendar start= Calendar.getInstance();
                        Calendar end= Calendar.getInstance();
                        start.setTime(hotelInfo.getDate("checkin_date"));
                        end.setTime(hotelInfo.getDate("checkout_date"));
                        long difference=end.getTimeInMillis()-start.getTimeInMillis();
                        long day=difference/(60*60*24*1000);
                        BigDecimal dayBigDecimal = new BigDecimal(day);
                        BigDecimal stayPrice = priceInfo.getBigDecimal("total_price").divide(dayBigDecimal);
                        log.info(userInfo.getString("name")+"相差天数："+day);
                        for(int j=0;j<day;j++){
                            //保存或更新
                            CheckDataAnalysis2 analysis = new CheckDataAnalysis2();
                            analysis.setPhone(userInfo.getString("phone"));
                            start.add(Calendar.DATE,1);
                            String stayDay = DateUtils.format(start.getTime(),DateUtils.YEAR_MONTH_DAY_PATTERN_MIDLINE);
                            analysis.setStayDay(stayDay);
                            CheckDataAnalysis2 oneAnalysis = CheckDataAnalysis2.dao.findFirst(
                                    Db.getSql("admin.checkDataAnalysis.getAnalysisByPhoneAndDate"),
                                    userInfo.getString("phone"),
                                    stayDay);
                            //处理地址
                            String hotelName = hotelInfo.getString("hotel_name");
                            int indexOf=0;
                            if(hotelName.indexOf("(")==0){
                                indexOf = hotelName.indexOf("（");
                            }else{
                                indexOf = hotelName.indexOf("(");
                            }
                            hotelName = hotelName.substring(indexOf+1,indexOf+3);
                            if(oneAnalysis!=null){
                                oneAnalysis.setStayPrice(stayPrice);
                                oneAnalysis.setStayCity(hotelInfo.getString("city_name"));
                                oneAnalysis.setStayCity(hotelName);
                                oneAnalysis.update();
                            }else{
                                analysis.setUserName(userInfo.getString("name"));
                                analysis.setStayPrice(stayPrice);
                                analysis.setStayCity(hotelInfo.getString("city_name"));
                                analysis.setCreateTime(new Date());
                                analysis.setStayCity(hotelName);
                                analysis.save();
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
