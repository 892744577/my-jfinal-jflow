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
#end
