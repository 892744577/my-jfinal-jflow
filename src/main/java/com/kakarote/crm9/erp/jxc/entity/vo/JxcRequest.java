package com.kakarote.crm9.erp.jxc.entity.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class JxcRequest {
    private String docno;
    private String customer;
    private Integer num;
    private Date deal_time;
    private String order_no;
    private Integer order_num;
    private Date order_time;
    private List<JxcCodeRequest> code_list = new ArrayList<JxcCodeRequest>();

}
