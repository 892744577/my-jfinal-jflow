package com.kakarote.crm9.erp.jxc.entity.vo;

import lombok.Data;

@Data
public class JxcReportRequest {
    private String no; //登陆人账号
    private String jsondata; //从表数据   [{"mcuid":xxx},]
}
