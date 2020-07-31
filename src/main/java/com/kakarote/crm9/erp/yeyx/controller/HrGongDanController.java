package com.kakarote.crm9.erp.yeyx.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.upload.UploadFile;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.common.constant.BaseConstant;
import com.kakarote.crm9.erp.admin.service.AdminSceneService;
import com.kakarote.crm9.erp.wx.util.DateUtil;
import com.kakarote.crm9.erp.yeyx.entity.HrGongdan;
import com.kakarote.crm9.erp.yeyx.entity.HrGongdanBook;
import com.kakarote.crm9.erp.yeyx.entity.HrGongdanLog;
import com.kakarote.crm9.erp.yeyx.entity.HrGongdanRepair;
import com.kakarote.crm9.erp.yeyx.entity.vo.HrGongdanLogRequest;
import com.kakarote.crm9.erp.yeyx.entity.vo.HrGongdanRepairRequest;
import com.kakarote.crm9.erp.yeyx.entity.vo.HrGongdanRequest;
import com.kakarote.crm9.erp.yeyx.service.HrGongDanService;
import com.kakarote.crm9.erp.yeyx.service.HrGongdanAppointService;
import com.kakarote.crm9.erp.yeyx.service.HrGongdanLogService;
import com.kakarote.crm9.erp.yeyx.service.HrGongdanRepairService;
import com.kakarote.crm9.erp.yzj.service.TokenService;
import com.kakarote.crm9.utils.FileUploadUtil;
import com.kakarote.crm9.utils.R;
import lombok.extern.slf4j.Slf4j;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Slf4j
public class HrGongDanController extends Controller {

    @Inject
    private HrGongDanService hrGongDanService;

    @Inject
    private HrGongdanRepairService hrGongdanRepairService;

    @Inject
    private HrGongdanAppointService hrGongdanAppointService;
    @Inject
    private HrGongdanLogService hrGongdanLogService;

    @Inject
    private TokenService tokenService;

    @Inject
    private AdminSceneService adminSceneService;

    /**
     * 保存预约单
     */
    public void saveHrGongdanAppoint(@Para("") HrGongdanRepairRequest hrGongdanRepairRequest) throws Exception  {
        log.info("=======保存预约单");
        HrGongdanBook hrGongdanBook = getModel(HrGongdanBook.class,"",true);
        String serialNum = (new DecimalFormat("00")).format(hrGongdanAppointService.getAppointByOrderNum());//流水号格式化
        hrGongdanBook.setOrderNumber("YY"+DateUtil.changeDateTOStr2(new Date())+ThreadLocalRandom.current().nextInt(10, 100)+serialNum);
        hrGongdanBook.setCreateTime(new Date());
        renderJson(R.ok().put("data",hrGongdanBook.save()));
    }
    /**
     * 预约单查询
     */
    public void queryPageListAppoint(BasePageRequest basePageRequest) throws Exception  {
        log.info("=======预约单查询");
        HrGongdanBook hrGongdanBook = getModel(HrGongdanBook.class,"",true);
        renderJson(R.ok().put("data",hrGongdanAppointService.queryPageList(basePageRequest)));
    }
    /**
     * 保存预约单处理记录
     */
    public void saveHrGongdanLogBook(@Para("") HrGongdanLogRequest hrGongdanLogRequest) {
        log.info("=======保存预约单处理记录");
        HrGongdanLog hrGongdanLog = getModel(HrGongdanLog.class,"",true);
        hrGongdanLog.setCreateTime(new Date());
        if("2".equals(hrGongdanLogRequest.getDeal())){
            HrGongdanBook hrGongdanBook = new HrGongdanBook();
            hrGongdanBook.setOrderNumber(hrGongdanLog.getPreServiceNo());
            hrGongdanBook.setDeal("2");
            hrGongdanBook.update();
        }
        renderJson(R.ok().put("data",hrGongdanLog.save()));
    }

    /**
     * 保存报修单
     */
    public void saveHrGongdanRepair(@Para("") HrGongdanRepairRequest hrGongdanRepairRequest) throws Exception  {
        log.info("=======保存报修单");
        HrGongdanRepair hrGongdanRepair = getModel(HrGongdanRepair.class,"",true);
        String serialNum = (new DecimalFormat("00")).format(hrGongdanRepairService.getRepairByOrderNum());//流水号格式化
        hrGongdanRepair.setOrderNumber("BX"+ DateUtil.changeDateTOStr2(new Date())+ThreadLocalRandom.current().nextInt(10, 100)+serialNum);
        String photos = upload(getFiles()).stream().map(item->item.get(FileUploadUtil.ACCESS_PATH)).collect(Collectors.joining(";"));
        hrGongdanRepair.setPhoto(photos);
        hrGongdanRepair.setCreateTime(new Date());
        renderJson(R.ok().put("data",hrGongdanRepair.save()));
    }
    /**
     * 报修单上传文件
     * @param list
     * @return
     */
    public List<Map<String, String>> upload(List<UploadFile> list ) {
        log.info("开始执行文件上传方法!");
        return FileUploadUtil.upload(list,BaseConstant.UPLOAD_PATH_GDCX,"");
    }
    /**
     * 报修单查询
     */
    public void queryPageListRepair(BasePageRequest basePageRequest) throws Exception  {
        log.info("=======报修单查询");
        renderJson(R.ok().put("data",hrGongdanRepairService.queryPageList(basePageRequest)));
    }
    /**
     * 保存报修单处理记录
     */
    public void saveHrGongdanLogRepair(@Para("") HrGongdanLogRequest hrGongdanLogRequest) throws Exception  {
        log.info("=======保存预约单处理记录");
        HrGongdanLog hrGongdanLog = getModel(HrGongdanLog.class,"",true);
        hrGongdanLog.setCreateTime(new Date());
        if("2".equals(hrGongdanLogRequest.getDeal()) ){
            HrGongdanRepair hrGongdanRepair = new HrGongdanRepair();
            hrGongdanRepair.setOrderNumber(hrGongdanLog.getPreServiceNo());
            hrGongdanRepair.setDeal("2");
            hrGongdanRepair.update();
        }
        renderJson(R.ok().put("data",hrGongdanLog.save()));
    }
    /**
     * @author tmr
     * 查询工单记录
     */
    public void queryPageListLog(BasePageRequest basePageRequest) {
        renderJson(R.ok().put("data",hrGongdanLogService.queryPageList(basePageRequest)));
    }

    /**
     * @author tmr
     * 分页工单查询数据
     */
    public void queryPageList(BasePageRequest basePageRequest) {
//        renderJson(R.ok().put("data",hrGongDanService.queryPageList(basePageRequest)));
        renderJson(adminSceneService.filterConditionAndGetPageList(basePageRequest));
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
