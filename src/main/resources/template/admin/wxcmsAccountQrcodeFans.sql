#namespace("admin.wxcmsAccountQrcodeFans")
  #sql ("getFansByFromUserName")
    SELECT d.* FROM wxcms_account_qrcode_fans d where d.fromuser_name = ?
  #end
  #sql ("getFansByQrcodeParam")
    SELECT d.* FROM wxcms_account_qrcode_fans d where d.fromuser_name = ? and d.event_key = ?
  #end
#end
