#namespace("admin.hrGongdanWdtTradeGoods")
  #sql ("getWdtTradeGoodsByRecId")
    SELECT d.* FROM hr_gongdan_wdt_trade_goods d where d.rec_id = ?
  #end
#end
