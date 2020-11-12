#namespace("admin.wxcmsAccountShop")
  #sql ("getAccountShopByAddress")
    SELECT d.* FROM wxcms_account_shop d where 1=1
    #if(province)
      and d.province LIKE concat('%',#para(province),'%')
    #end
    #if(city)
      and d.city LIKE concat('%',#para(city),'%')
    #end
    #if(district)
      and d.district LIKE concat('%',#para(district),'%')
    #end
  #end
  #sql ("queryPageList")
    SELECT a.*,b.qrcode_param,b.qrcode_url FROM wxcms_account_shop a
    left join wxcms_account_shop_qrcode b on a.id=b.shop_id
    where 1=1
    #if(search)
      and (a.province LIKE concat('%',#para(search),'%') or a.city LIKE concat('%',#para(search),'%')
      or a.district LIKE concat('%',#para(search),'%') or a.shopNo like CONCAT('%',#para(search),'%'))
    #end
    order by a.shopNo desc
  #end
#end
