#namespace("admin.hrGongdanWdtTradeGoods")
  #sql ("getWdtTradeGoodsByRecId")
    SELECT d.* FROM hr_gongdan_wdt_trade_goods d where d.rec_id = ?
  #end

  #sql ("queryPageList")
    select * from hr_gongdan_wdt_trade_goods a where a.trade_id = #para(tradeId)
    order by a.created desc
  #end
#end
