package com.kakarote.crm9.erp.wx.config;

import BP.Difference.SystemConfig;
import lombok.Getter;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Getter
public enum WxCpAgentIdEmun {
  agent0(Integer.parseInt(SystemConfig.getCS_AppSettings().get("CP.CORPAGENTID").toString()),
          SystemConfig.getCS_AppSettings().get("CP.CORPSECRET").toString(),"通讯录应用id"),
  agent1(Integer.parseInt(SystemConfig.getCS_AppSettings().get("CP1.AGENTID").toString()),
          SystemConfig.getCS_AppSettings().get("CP1.APPSECRET").toString(),"待办待阅"),
  agent2(Integer.parseInt(SystemConfig.getCS_AppSettings().get("CP2.AGENTID").toString()),
          SystemConfig.getCS_AppSettings().get("CP2.APPSECRET").toString(),"无忧服务"),
  agent3(Integer.parseInt(SystemConfig.getCS_AppSettings().get("CP3.AGENTID").toString()),
          SystemConfig.getCS_AppSettings().get("CP3.APPSECRET").toString(),"工程项目"),
  agent4(Integer.parseInt(SystemConfig.getCS_AppSettings().get("CP4.AGENTID").toString()),
          SystemConfig.getCS_AppSettings().get("CP4.APPSECRET").toString(),"工时排行"),
  agent5(Integer.parseInt(SystemConfig.getCS_AppSettings().get("CP5.AGENTID").toString()),
          SystemConfig.getCS_AppSettings().get("CP5.APPSECRET").toString(),"工时排行");

  private Integer code;
  private String scret;
  private String description;
  public static String corpId=SystemConfig.getCS_AppSettings().get("CP.CORPID").toString();
  WxCpAgentIdEmun(Integer code,String scret,String description) {
    this.code = code;
    this.scret = scret;
    this.description = description;
  }
}
