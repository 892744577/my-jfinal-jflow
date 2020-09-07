package com.kakarote.crm9.erp.wx.config;

import com.kakarote.crm9.erp.wx.util.JsonUtils;
import lombok.Data;

import java.util.List;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Data
public class WxCpProperties {
  /**
   * 设置企业微信的corpId
   */
  private String corpId;

  private List<AppConfig> appConfigs;

  @Data
  public static class AppConfig {
    /**
     * 设置企业微信应用的AgentId
     */
    private Integer agentId;

    /**
     * 设置企业微信应用的Secret
     */
    private String secret;

    /**
     * 设置企业微信应用的token
     */
    private String token;

    /**
     * 设置企业微信应用的EncodingAESKey
     */
    private String aesKey;

  }

  @Override
  public String toString() {
    return JsonUtils.toJson(this);
  }
}
