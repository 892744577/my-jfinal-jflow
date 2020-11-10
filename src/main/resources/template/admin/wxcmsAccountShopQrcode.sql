#namespace("admin.wxcmsAccountShopQrcode")
  #sql ("getShopByQrcodeParam")
    SELECT d.* FROM wxcms_account_shop_qrcode d where d.qrcode_param = ?
  #end
  #sql ("getQrcodeParamByShopId")
    SELECT d.* FROM wxcms_account_shop_qrcode d where d.shop_id = ?
  #end
#end
