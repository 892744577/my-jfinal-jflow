#namespace("admin.hrGongdanWdtTrade")
  #sql ("getWdtTradeInfoByTradeNo")
    SELECT d.* FROM hr_gongdan_wdt_trade d where d.trade_no = ?
  #end
#end
