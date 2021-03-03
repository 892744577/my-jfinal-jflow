package com.kakarote.crm9.erp.yeyx.service;

import com.kakarote.crm9.erp.yeyx.entity.HrGongdanFinanceFee;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
@Data
public class HrGongdanChargeService {

    public void saveHrGongdanCharge(String serviceNo,
                                    String preServiceNo,
                                    Integer productCount,
                                    BigDecimal servicePrice,
                                    BigDecimal serviceExtraCharge,
                                    BigDecimal chargeFee,
                                    String CDT){
        //保存数据到费用记录表
        HrGongdanFinanceFee hrGongdanFinanceFee = new HrGongdanFinanceFee();
        hrGongdanFinanceFee.setServiceNo(serviceNo);
        hrGongdanFinanceFee.setPreServiceNo(preServiceNo);
        hrGongdanFinanceFee.setProductCount(productCount);
        hrGongdanFinanceFee.setServicePrice(servicePrice);
        hrGongdanFinanceFee.setServiceExtraCharge(serviceExtraCharge);
        hrGongdanFinanceFee.setChargeFee(chargeFee);
        hrGongdanFinanceFee.setCDT(CDT);
        hrGongdanFinanceFee.save();
    }
}
