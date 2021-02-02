#namespace("admin.hrGongdanWdtTrade")
  #sql ("getWdtTradeInfoByTradeNo")
    SELECT d.* FROM hr_gongdan_wdt_trade d where d.trade_no = ?
  #end

  #sql ("queryPageList")
    select * from hr_gongdan_wdt_trade a
    where 1=1
    #if(search)
      and (a.trade_no like CONCAT('%',#para(search),'%') or a.receiver_telno like CONCAT('%',#para(search),'%')
       or a.receiver_mobile like CONCAT('%',#para(search),'%') or a.receiver_name like CONCAT('%',#para(search),'%') or a.customer_name like CONCAT('%',#para(search),'%'))
    #end
    order by a.created desc
  #end

  #sql ("getLatestWdtTradeInfo")
    SELECT d.* FROM hr_gongdan_wdt_trade d order by d.modified desc
  #end
#end
