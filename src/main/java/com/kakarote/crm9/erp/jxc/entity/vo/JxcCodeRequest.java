package com.kakarote.crm9.erp.jxc.entity.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class JxcCodeRequest {
    private String order_no;
    private String docno;
    private String code; //机身码
    private String color;
    private String model;
    private String customer;
    private BigDecimal salePrice; //销售价格
    private String salor;//销售人
    private String outType;//出库类型
    private String shop;//上样门店
}
