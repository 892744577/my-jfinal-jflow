package com.kakarote.crm9.erp.yeyx.service;

import BP.Web.WebUser;
import com.jfinal.plugin.activerecord.Db;
import com.kakarote.crm9.erp.admin.entity.PortEmp;
import com.kakarote.crm9.erp.yeyx.entity.HrGongdanFinanceFee;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
@Data
public class HrGongdanFinanceService {

    /**
     * 增加扣费记录
     * @param serviceNo
     * @param preServiceNo
     * @param productCount
     * @param servicePrice
     * @param serviceExtraCharge
     * @param chargeFee
     * @param CDT
     */
    public void saveHrGongdanFee(String serviceNo,
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

    /**
     * 删除扣费记录
     * */
    public int deleteHrGongdanFee(String serviceNo){
       return Db.delete("delete from hr_gongdan_finance_fee WHERE serviceNo = ?", serviceNo);
    }
    /**
     * 计算信用余额
     * @return
     * @throws Exception
     */
    public BigDecimal getRemaining() throws Exception {
        //已使用费用
        //BigDecimal sumFee = Db.queryBigDecimal(Db.getSql("admin.hrGongdanFinance.sumFee"), WebUser.getNo());
        BigDecimal sumBookFee = Db.queryBigDecimal(Db.getSql("admin.hrGongdanFinance.sumBookFee"), WebUser.getNo());
        //信用额度
        BigDecimal credit = new BigDecimal(200);
        //总充值金额
        PortEmp portEmp = PortEmp.dao.findById(WebUser.getNo());
        BigDecimal sumCharge = Db.queryBigDecimal(Db.getSql("admin.hrGongdanFinance.sumCharge"), portEmp.getTeamNo());
        //计算剩余
        return sumCharge.add(credit).subtract(sumBookFee);
    }
}
