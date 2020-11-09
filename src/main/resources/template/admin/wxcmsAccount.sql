/*
CREATE VIEW wxcms_account_relation_qrcode AS
SELECT b.qrcode_param agent_qrcode_param,e.qrcode_param shop_qrcode_param FROM wxcms_account_agent a
LEFT JOIN wxcms_account_agent_qrcode b ON b.agent_id = a.open_id
LEFT JOIN wxcms_account_agent_shop c ON c.agent_id = a.open_id
LEFT JOIN wxcms_account_shop d ON d.id = c.shop_id
LEFT JOIN wxcms_account_shop_qrcode e ON d.id = e.shop_id
*/
#namespace("admin.wxcmsAccount")
  #sql ("getNewQrcode")
    SELECT * from wxcms_account_relation_qrcode a
    where 1=1 and shop_qrcode_param is not null
    #if(search)
      and (a.agent_qrcode_param  = #para(search) )
    #end
  #end
#end
