package com.kakarote.crm9.erp.yeyx.service;

import BP.Difference.SystemConfig;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Db;
import com.kakarote.crm9.common.util.EncodeUtil;
import com.kakarote.crm9.common.util.HttpHelper;
import com.kakarote.crm9.erp.wx.util.DateUtil;
import com.kakarote.crm9.erp.yeyx.entity.HrGongdanWdtTrade;
import com.kakarote.crm9.erp.yeyx.entity.HrGongdanWdtTradeGoods;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * @author tangmanrong
 */
@Slf4j
public class WdtService {
    private static final String APPLICATION_JSON = "application/json";
    private static final String APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
    private static final int timeoutMillis = 30000;

    @Getter
    private String sid = SystemConfig.getCS_AppSettings().get("WDT.sid").toString();
    @Getter
    private String appkey = SystemConfig.getCS_AppSettings().get("WDT.appkey").toString();
    @Getter
    private String qmAppkey = SystemConfig.getCS_AppSettings().get("WDT.qm.appkey").toString();
    @Getter
    private String targetAppkey = SystemConfig.getCS_AppSettings().get("WDT.qm.targetAppkey").toString();
    @Getter
    private String appsecret = SystemConfig.getCS_AppSettings().get("WDT.appsecret").toString();
    @Getter
    private String session = SystemConfig.getCS_AppSettings().get("WDT.session").toString();

    /**
     * 获取参数的json字符串
     * @param moreMapData
     * @return
     */
    public Map getJsonData(Map moreMapData) {
        Map map = new HashMap();
        try {
            long timestamp =  System.currentTimeMillis()/1000;
            map.put("sid",this.sid);
            map.put("appkey", this.appkey);
            map.put("timestamp", String.valueOf(timestamp));
            map.put("page_no", "0");
            map.put("page_size","500");
            map.putAll(moreMapData);
            map.put("sign",this.getSign(map));
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //获取签名
    public String getSign(Map params){
        // 第一步：检查参数是否已经排序
        String[] keys = (String[]) params.keySet().toArray(new String[0]);
        Arrays.sort(keys);

        // 第二步：把所有参数名和参数值串在一起
        StringBuilder query = new StringBuilder();
        for (String key : keys) {
            Object objValue = params.get(key);
            String value = (String) params.get(key);
            //循环对每个键值进行处理
            DecimalFormat decimalFormat1 = new DecimalFormat("00");
            String keyStr = decimalFormat1.format(key.length());
            DecimalFormat decimalFormat2 = new DecimalFormat("0000");
            String valueStr = decimalFormat2.format(value.length());
            query.append(keyStr).append("-").append(key).append(":").append(valueStr).append("-").append(value).append(";");
        }
        String queryStr = query.toString();
        queryStr = queryStr.substring(0,queryStr.lastIndexOf(";"))+this.appsecret;
        try {
            // 第三步：使用MD5加密
            return EncodeUtil.get32md5(queryStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 奇门获取订单数据
     * @param moreMapData
     * @return
     */
    public Map getQmJsonData(Map moreMapData) {
        Map map = new HashMap();
        try {
            map.put("sid",this.sid);
            map.put("app_key",this.qmAppkey);
            map.put("timestamp", DateUtil.changeDateTOStr(new Date()));
            map.put("method","wdt.trade.query");
            map.put("target_app_key", this.targetAppkey);
            map.put("format", "json");
            map.put("sign_method", "md5");
            map.put("v", "2.0");
            map.put("page_no", "0");
            map.put("page_size","500");
            map.putAll(moreMapData);
            map.put("sign", this.getQmSign(map));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    //获取签名
    public String getQmSign(Map params){
        // 第一步：检查参数是否已经排序
        String[] keys = (String[]) params.keySet().toArray(new String[0]);
        Arrays.sort(keys);

        // 第二步：把所有参数名和参数值串在一起
        StringBuilder query = new StringBuilder();
        query.append(this.appsecret);
        for (String key : keys) {
            String value = (String) params.get(key);
            query.append(key).append(value);

        }
        // 第三步：使用MD5加密
        query.append(this.appsecret);
        String queryStr = query.toString();
        try {
            // 第三步：使用MD5加密
            return EncodeUtil.get32md5Big(queryStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * gateway发送json参数POST请求
     * @param url
     * @param parm
     * @return
     * @throws Exception
     */
    public static String gatewayRequestJson(String url, String parm) throws Exception {
        Map headers = new HashMap(1); headers.put("Content-Type", APPLICATION_JSON);
        return HttpHelper.post(headers, parm, url, timeoutMillis);
    }

    /**
     * gateway发送application/x-www-form-urlencoded参数POST请求
     * @param url
     * @param parm
     * @return
     * @throws Exception
     */
    public String gatewayRequest(String url, Map parm) throws Exception {
        Map headers = new HashMap(1); headers.put("Content-Type", APPLICATION_X_WWW_FORM_URLENCODED);
        return HttpHelper.post(headers, parm, url, timeoutMillis);
    }

    /*
     * @Description //遍历保存销售订单信息到数据库
     * @Author wangkaida
     * @Date 15:25 2021/1/12
     * @Param [result]
     * @return boolean
     **/
    public boolean saveTradeInfo(String result) {
        JSONObject tradeInfoResult = JSONObject.parseObject(result).getJSONObject("response");
        if(tradeInfoResult.getInteger("errorcode") == 0
                && tradeInfoResult.getJSONArray("trades") != null
                && tradeInfoResult.getJSONArray("trades").size() > 0){
            JSONArray tradeInfoList= tradeInfoResult.getJSONArray("trades");
            for(int i=0;i<tradeInfoList.size();i++){
                JSONObject tradeInfo = tradeInfoList.getJSONObject(i);
                //先判断trade_no是否已经存在数据库
                String tradeNo = tradeInfo.getString("trade_no");
                HrGongdanWdtTrade wdtTradeInfoDb = HrGongdanWdtTrade.dao.findFirst(Db.getSql("admin.hrGongdanWdtTrade.getWdtTradeInfoByTradeNo"),tradeNo);
                HrGongdanWdtTrade hrGongdanWdtTrade = new HrGongdanWdtTrade();
                hrGongdanWdtTrade.setTradeId(tradeInfo.getInteger("trade_id"));
                hrGongdanWdtTrade.setTradeNo(tradeNo);
                hrGongdanWdtTrade.setPlatformId(tradeInfo.getInteger("platform_id"));
                hrGongdanWdtTrade.setShopNo(tradeInfo.getString("shop_no"));
                hrGongdanWdtTrade.setShopName(tradeInfo.getString("shop_name"));
                hrGongdanWdtTrade.setShopRemark(tradeInfo.getString("shop_remark"));
                hrGongdanWdtTrade.setWarehouseType(tradeInfo.getInteger("warehouse_type"));
                hrGongdanWdtTrade.setWarehouseNo(tradeInfo.getString("warehouse_no"));
                hrGongdanWdtTrade.setSrcTids(tradeInfo.getString("src_tids"));
                hrGongdanWdtTrade.setTradeStatus(tradeInfo.getInteger("trade_status"));
                hrGongdanWdtTrade.setConsignStatus(tradeInfo.getInteger("consign_status"));
                hrGongdanWdtTrade.setTradeType(tradeInfo.getInteger("trade_type"));
                hrGongdanWdtTrade.setDeliveryTerm(tradeInfo.getInteger("delivery_term"));
                hrGongdanWdtTrade.setFreezeReason(tradeInfo.getInteger("freeze_reason"));
                hrGongdanWdtTrade.setRefundStatus(tradeInfo.getInteger("refund_status"));
                hrGongdanWdtTrade.setFenxiaoType(tradeInfo.getInteger("fenxiao_type"));
                hrGongdanWdtTrade.setFenxiaoNick(tradeInfo.getString("fenxiao_nick"));
                hrGongdanWdtTrade.setTradeTime(tradeInfo.getDate("trade_time"));
                hrGongdanWdtTrade.setPayTime(tradeInfo.getDate("pay_time"));
                hrGongdanWdtTrade.setCustomerName(tradeInfo.getString("customer_name"));
                hrGongdanWdtTrade.setCustomerNo(tradeInfo.getString("customer_no"));
                hrGongdanWdtTrade.setPayAccount(tradeInfo.getString("pay_account"));
                hrGongdanWdtTrade.setBuyerNick(tradeInfo.getString("buyer_nick"));
                hrGongdanWdtTrade.setReceiverName(tradeInfo.getString("receiver_name"));
                hrGongdanWdtTrade.setReceiverProvince(tradeInfo.getInteger("receiver_province"));
                hrGongdanWdtTrade.setReceiverCity(tradeInfo.getInteger("receiver_city"));
                hrGongdanWdtTrade.setReceiverDistrict(tradeInfo.getInteger("receiver_district"));
                hrGongdanWdtTrade.setReceiverAddress(tradeInfo.getString("receiver_address"));
                hrGongdanWdtTrade.setReceiverMobile(tradeInfo.getString("receiver_mobile"));
                hrGongdanWdtTrade.setReceiverTelno(tradeInfo.getString("receiver_telno"));
                hrGongdanWdtTrade.setReceiverZip(tradeInfo.getString("receiver_zip"));
                hrGongdanWdtTrade.setReceiverArea(tradeInfo.getString("receiver_area"));
                hrGongdanWdtTrade.setReceiverRing(tradeInfo.getString("receiver_ring"));
                hrGongdanWdtTrade.setReceiverDtb(tradeInfo.getString("receiver_dtb"));
                hrGongdanWdtTrade.setToDeliverTime(tradeInfo.getString("to_deliver_time"));
                hrGongdanWdtTrade.setBadReason(tradeInfo.getInteger("bad_reason"));
                hrGongdanWdtTrade.setLogisticsId(tradeInfo.getInteger("logistics_id"));
                hrGongdanWdtTrade.setLogisticsName(tradeInfo.getString("logistics_name"));
                hrGongdanWdtTrade.setLogisticsCode(tradeInfo.getString("logistics_code"));
                hrGongdanWdtTrade.setLogisticsType(tradeInfo.getInteger("logistics_type"));
                hrGongdanWdtTrade.setLogisticsNo(tradeInfo.getString("logistics_no"));
                hrGongdanWdtTrade.setShopId(tradeInfo.getInteger("shop_id"));
                hrGongdanWdtTrade.setWarehouseId(tradeInfo.getInteger("warehouse_id"));
                hrGongdanWdtTrade.setCheckStep(tradeInfo.getInteger("check_step"));
                hrGongdanWdtTrade.setUnmergeMask(tradeInfo.getInteger("unmerge_mask"));
                hrGongdanWdtTrade.setDelayToTime(tradeInfo.getString("delay_to_time"));
                hrGongdanWdtTrade.setCustomerType(tradeInfo.getInteger("customer_type"));
                hrGongdanWdtTrade.setCustomerId(tradeInfo.getString("customer_id"));
                hrGongdanWdtTrade.setReceiverCountry(tradeInfo.getInteger("receiver_country"));
                hrGongdanWdtTrade.setPreChargeTime(tradeInfo.getString("pre_charge_time"));
                hrGongdanWdtTrade.setIsPrevNotify(tradeInfo.getInteger("is_prev_notify"));
                hrGongdanWdtTrade.setNoteCount(tradeInfo.getInteger("note_count"));
                hrGongdanWdtTrade.setBuyerMessageCount(tradeInfo.getInteger("buyer_message_count"));
                hrGongdanWdtTrade.setCsRemarkCount(tradeInfo.getInteger("cs_remark_count"));
                hrGongdanWdtTrade.setCsRemarkChangeCount(tradeInfo.getInteger("cs_remark_change_count"));
                hrGongdanWdtTrade.setDiscountChange(tradeInfo.getBigDecimal("discount_change"));
                hrGongdanWdtTrade.setTradePrepay(tradeInfo.getBigDecimal("trade_prepay"));
                hrGongdanWdtTrade.setPiAmount(tradeInfo.getBigDecimal("pi_amount"));
                hrGongdanWdtTrade.setOtherCost(tradeInfo.getBigDecimal("other_cost"));
                hrGongdanWdtTrade.setVolume(tradeInfo.getBigDecimal("volume"));
                hrGongdanWdtTrade.setSalesScore(tradeInfo.getString("sales_score"));
                hrGongdanWdtTrade.setFlagId(tradeInfo.getInteger("flag_id"));
                hrGongdanWdtTrade.setIsSealed(tradeInfo.getInteger("is_sealed"));
                hrGongdanWdtTrade.setGiftMask(tradeInfo.getInteger("gift_mask"));
                hrGongdanWdtTrade.setSplitFromTradeId(tradeInfo.getString("split_from_trade_id"));
                hrGongdanWdtTrade.setLogisticsTemplateId(tradeInfo.getString("logistics_template_id"));
                hrGongdanWdtTrade.setSendbillTemplateId(tradeInfo.getString("sendbill_template_id"));
                hrGongdanWdtTrade.setRevertReason(tradeInfo.getInteger("revert_reason"));
                hrGongdanWdtTrade.setCancelReason(tradeInfo.getInteger("cancel_reason"));
                hrGongdanWdtTrade.setIsUnpaymentSms(tradeInfo.getInteger("is_unpayment_sms"));
                hrGongdanWdtTrade.setPackageId(tradeInfo.getString("package_id"));
                hrGongdanWdtTrade.setTradeMask(tradeInfo.getString("trade_mask"));
                hrGongdanWdtTrade.setReserve(tradeInfo.getString("reserve"));
                hrGongdanWdtTrade.setLargeType(tradeInfo.getInteger("large_type"));
                hrGongdanWdtTrade.setBuyerMessage(tradeInfo.getString("buyer_message"));
                hrGongdanWdtTrade.setCsRemark(tradeInfo.getString("cs_remark"));
                hrGongdanWdtTrade.setRemarkFlag(tradeInfo.getInteger("remark_flag"));
                hrGongdanWdtTrade.setPrintRemark(tradeInfo.getString("print_remark"));
                hrGongdanWdtTrade.setGoodsTypeCount(tradeInfo.getInteger("goods_type_count"));
                hrGongdanWdtTrade.setGoodsCount(tradeInfo.getBigDecimal("goods_count"));
                hrGongdanWdtTrade.setGoodsAmount(tradeInfo.getBigDecimal("goods_amount"));
                hrGongdanWdtTrade.setPostAmount(tradeInfo.getBigDecimal("post_amount"));
                hrGongdanWdtTrade.setOtherAmount(tradeInfo.getBigDecimal("other_amount"));
                hrGongdanWdtTrade.setDiscount(tradeInfo.getBigDecimal("discount"));
                hrGongdanWdtTrade.setReceivable(tradeInfo.getBigDecimal("receivable"));
                hrGongdanWdtTrade.setDapAmount(tradeInfo.getBigDecimal("dap_amount"));
                hrGongdanWdtTrade.setCodAmount(tradeInfo.getBigDecimal("cod_amount"));
                hrGongdanWdtTrade.setExtCodFee(tradeInfo.getBigDecimal("ext_cod_fee"));
                hrGongdanWdtTrade.setGoodsCost(tradeInfo.getBigDecimal("goods_cost"));
                hrGongdanWdtTrade.setPostCost(tradeInfo.getBigDecimal("post_cost"));
                hrGongdanWdtTrade.setPaid(tradeInfo.getBigDecimal("paid"));
                hrGongdanWdtTrade.setWeight(tradeInfo.getBigDecimal("weight"));
                hrGongdanWdtTrade.setProfit(tradeInfo.getBigDecimal("profit"));
                hrGongdanWdtTrade.setTax(tradeInfo.getBigDecimal("tax"));
                hrGongdanWdtTrade.setTaxRate(tradeInfo.getBigDecimal("tax_rate"));
                hrGongdanWdtTrade.setCommission(tradeInfo.getBigDecimal("commission"));
                hrGongdanWdtTrade.setInvoiceType(tradeInfo.getInteger("invoice_type"));
                hrGongdanWdtTrade.setInvoiceTitle(tradeInfo.getString("invoice_title"));
                hrGongdanWdtTrade.setInvoiceContent(tradeInfo.getString("invoice_content"));
                hrGongdanWdtTrade.setSalesmanId(tradeInfo.getInteger("salesman_id"));
                hrGongdanWdtTrade.setCheckerId(tradeInfo.getInteger("checker_id"));
                hrGongdanWdtTrade.setFullname(tradeInfo.getString("fullname"));
                hrGongdanWdtTrade.setCheckerName(tradeInfo.getString("checker_name"));
                hrGongdanWdtTrade.setFcheckerId(tradeInfo.getInteger("fchecker_id"));
                hrGongdanWdtTrade.setCheckouterId(tradeInfo.getInteger("checkouter_id"));
                hrGongdanWdtTrade.setStockoutNo(tradeInfo.getString("stockout_no"));
                hrGongdanWdtTrade.setFlagName(tradeInfo.getString("flag_name"));
                hrGongdanWdtTrade.setTradeFrom(tradeInfo.getInteger("trade_from"));
                hrGongdanWdtTrade.setSingleSpecNo(tradeInfo.getString("single_spec_no"));
                hrGongdanWdtTrade.setRawGoodsCount(tradeInfo.getBigDecimal("raw_goods_count"));
                hrGongdanWdtTrade.setRawGoodsTypeCount(tradeInfo.getInteger("raw_goods_type_count"));
                hrGongdanWdtTrade.setCurrency(tradeInfo.getString("currency"));
                hrGongdanWdtTrade.setSplitPackageNum(tradeInfo.getInteger("split_package_num"));
                hrGongdanWdtTrade.setInvoiceId(tradeInfo.getInteger("invoice_id"));
                hrGongdanWdtTrade.setVersionId(tradeInfo.getInteger("version_id"));
                hrGongdanWdtTrade.setModified(tradeInfo.getDate("modified"));
                hrGongdanWdtTrade.setCreated(tradeInfo.getDate("created"));
                hrGongdanWdtTrade.setIdCardType(tradeInfo.getInteger("id_card_type"));
                hrGongdanWdtTrade.setIdCard(tradeInfo.getString("id_card"));
                if (wdtTradeInfoDb == null) {
                    hrGongdanWdtTrade.save();
                }else {
                    hrGongdanWdtTrade.update();
                }
                //遍历保存商品列表信息到数据库
                JSONArray goodsInfoList= tradeInfo.getJSONArray("goods_list");
                saveOrUpdateGoodsInfo(goodsInfoList);
            }
            return true;
        }
        return false;
    }

    private void saveOrUpdateGoodsInfo(JSONArray goodsInfoList) {
        for(int i=0;i<goodsInfoList.size();i++){
            JSONObject goodsInfo = goodsInfoList.getJSONObject(i);
            //先判断rec_id是否已经存在数据库
            int recId = goodsInfo.getInteger("rec_id");
            HrGongdanWdtTradeGoods hrGongdanWdtTradeGoodsDb = HrGongdanWdtTradeGoods.dao.findFirst(Db.getSql("admin.hrGongdanWdtTradeGoods.getWdtTradeGoodsByRecId"),recId);
            HrGongdanWdtTradeGoods hrGongdanWdtTradeGoods = new HrGongdanWdtTradeGoods();
            hrGongdanWdtTradeGoods.setRecId(goodsInfo.getInteger("rec_id"));
            hrGongdanWdtTradeGoods.setTradeId(goodsInfo.getInteger("trade_id"));
            hrGongdanWdtTradeGoods.setSpecId(goodsInfo.getInteger("spec_id"));
            hrGongdanWdtTradeGoods.setPlatformId(goodsInfo.getInteger("platform_id"));
            hrGongdanWdtTradeGoods.setSrcOid(goodsInfo.getString("src_oid"));
            hrGongdanWdtTradeGoods.setPlatformGoodsId(goodsInfo.getString("platform_goods_id"));
            hrGongdanWdtTradeGoods.setPlatformSpecId(goodsInfo.getString("platform_spec_id"));
            hrGongdanWdtTradeGoods.setSuiteId(goodsInfo.getInteger("suite_id"));
            hrGongdanWdtTradeGoods.setFlag(goodsInfo.getInteger("flag"));
            hrGongdanWdtTradeGoods.setSrcTid(goodsInfo.getString("src_tid"));
            hrGongdanWdtTradeGoods.setGiftType(goodsInfo.getInteger("gift_type"));
            hrGongdanWdtTradeGoods.setRefundStatus(goodsInfo.getInteger("refund_status"));
            hrGongdanWdtTradeGoods.setGuaranteeMode(goodsInfo.getInteger("guarantee_mode"));
            hrGongdanWdtTradeGoods.setDeliveryTerm(goodsInfo.getInteger("delivery_term"));
            hrGongdanWdtTradeGoods.setBindOid(goodsInfo.getString("bind_oid"));
            hrGongdanWdtTradeGoods.setNum(goodsInfo.getBigDecimal("num"));
            hrGongdanWdtTradeGoods.setPrice(goodsInfo.getBigDecimal("price"));
            hrGongdanWdtTradeGoods.setActualNum(goodsInfo.getBigDecimal("actual_num"));
            hrGongdanWdtTradeGoods.setRefundNum(goodsInfo.getBigDecimal("refund_num"));
            hrGongdanWdtTradeGoods.setOrderPrice(goodsInfo.getBigDecimal("order_price"));
            hrGongdanWdtTradeGoods.setSharePrice(goodsInfo.getBigDecimal("share_price"));
            hrGongdanWdtTradeGoods.setAdjust(goodsInfo.getBigDecimal("adjust"));
            hrGongdanWdtTradeGoods.setDiscount(goodsInfo.getBigDecimal("discount"));
            hrGongdanWdtTradeGoods.setShareAmount(goodsInfo.getBigDecimal("share_amount"));
            hrGongdanWdtTradeGoods.setSharePost(goodsInfo.getBigDecimal("share_post"));
            hrGongdanWdtTradeGoods.setPaid(goodsInfo.getBigDecimal("paid"));
            hrGongdanWdtTradeGoods.setGoodsName(goodsInfo.getString("goods_name"));
            hrGongdanWdtTradeGoods.setProp2(goodsInfo.getString("prop2"));
            hrGongdanWdtTradeGoods.setBarcode(goodsInfo.getString("barcode"));
            hrGongdanWdtTradeGoods.setGoodsId(goodsInfo.getInteger("goods_id"));
            hrGongdanWdtTradeGoods.setGoodsNo(goodsInfo.getString("goods_no"));
            hrGongdanWdtTradeGoods.setSpecName(goodsInfo.getString("spec_name"));
            hrGongdanWdtTradeGoods.setSpecNo(goodsInfo.getString("spec_no"));
            hrGongdanWdtTradeGoods.setSpecCode(goodsInfo.getString("spec_code"));
            hrGongdanWdtTradeGoods.setSuiteNo(goodsInfo.getString("suite_no"));
            hrGongdanWdtTradeGoods.setSuiteName(goodsInfo.getString("suite_name"));
            hrGongdanWdtTradeGoods.setSuiteNum(goodsInfo.getBigDecimal("suite_num"));
            hrGongdanWdtTradeGoods.setSuiteAmount(goodsInfo.getBigDecimal("suite_amount"));
            hrGongdanWdtTradeGoods.setSuiteDiscount(goodsInfo.getBigDecimal("suite_discount"));
            hrGongdanWdtTradeGoods.setShareAmount2(goodsInfo.getBigDecimal("share_amount2"));
            hrGongdanWdtTradeGoods.setIsPrintSuite(goodsInfo.getInteger("is_print_suite"));
            hrGongdanWdtTradeGoods.setIsZeroCost(goodsInfo.getInteger("is_zero_cost"));
            hrGongdanWdtTradeGoods.setStockReserved(goodsInfo.getInteger("stock_reserved"));
            hrGongdanWdtTradeGoods.setIsConsigned(goodsInfo.getInteger("is_consigned"));
            hrGongdanWdtTradeGoods.setIsReceived(goodsInfo.getInteger("is_received"));
            hrGongdanWdtTradeGoods.setIsMaster(goodsInfo.getInteger("is_master"));
            hrGongdanWdtTradeGoods.setApiGoodsName(goodsInfo.getString("api_goods_name"));
            hrGongdanWdtTradeGoods.setApiSpecName(goodsInfo.getString("api_spec_name"));
            hrGongdanWdtTradeGoods.setWeight(goodsInfo.getBigDecimal("weight"));
            hrGongdanWdtTradeGoods.setCommission(goodsInfo.getBigDecimal("commission"));
            hrGongdanWdtTradeGoods.setGoodsType(goodsInfo.getInteger("goods_type"));
            hrGongdanWdtTradeGoods.setLargeType(goodsInfo.getInteger("large_type"));
            hrGongdanWdtTradeGoods.setInvoiceType(goodsInfo.getInteger("invoice_type"));
            hrGongdanWdtTradeGoods.setInvoiceContent(goodsInfo.getString("invoice_content"));
            hrGongdanWdtTradeGoods.setFromMask(goodsInfo.getInteger("from_mask"));
            hrGongdanWdtTradeGoods.setCid(goodsInfo.getInteger("cid"));
            hrGongdanWdtTradeGoods.setRemark(goodsInfo.getString("remark"));
            hrGongdanWdtTradeGoods.setModified(goodsInfo.getDate("modified"));
            hrGongdanWdtTradeGoods.setCreated(goodsInfo.getDate("created"));
            hrGongdanWdtTradeGoods.setTaxRate(goodsInfo.getBigDecimal("tax_rate"));
            hrGongdanWdtTradeGoods.setBaseUnitId(goodsInfo.getInteger("base_unit_id"));
            hrGongdanWdtTradeGoods.setUnitName(goodsInfo.getString("unit_name"));
            hrGongdanWdtTradeGoods.setPayId(goodsInfo.getString("pay_id"));
            hrGongdanWdtTradeGoods.setPayStatus(goodsInfo.getInteger("pay_status"));
            hrGongdanWdtTradeGoods.setPayTime(goodsInfo.getDate("pay_time"));
            if (hrGongdanWdtTradeGoodsDb == null) {
                hrGongdanWdtTradeGoods.save();
            }else {
                hrGongdanWdtTradeGoods.update();
            }
        }
    }
}