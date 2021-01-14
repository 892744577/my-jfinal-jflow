#namespace("admin.wxcmsAccountTeamQrcode")
  #sql ("getTeamByQrcodeParam")
    SELECT d.* FROM wxcms_account_team_qrcode d where d.qrcode_param = ?
  #end
#end
