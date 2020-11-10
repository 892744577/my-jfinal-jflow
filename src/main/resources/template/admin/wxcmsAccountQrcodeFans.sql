#namespace("admin.wxcmsAccountQrcodeFans")
  #sql ("getFansByFromUserName")
    SELECT d.* FROM wxcms_account_qrcode_fans d where d.fromuser_name = ?
  #end
  #sql ("getFansByQrcodeParam")
    SELECT d.* FROM wxcms_account_qrcode_fans d where d.fromuser_name = ? and d.event_key = ?
  #end
  #sql ("getFansByShopId")
    select a.* from wxcms_account_qrcode_fans a left join wxcms_account_shop_qrcode b on a.event_key = b.qrcode_param where b.shopid = ?
  #end
#end
