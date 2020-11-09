#namespace("admin.wxcmsAccountAgentQrcode")
  #sql ("getAgentByQrcodeParam")
    SELECT d.* FROM wxcms_account_agent_qrcode d where d.qrcode_param = ?
  #end
#end
