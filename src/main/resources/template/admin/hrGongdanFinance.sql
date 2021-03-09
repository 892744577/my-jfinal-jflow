#namespace("admin.hrGongdanFinance")
  #sql ("sumBookFee")
    SELECT IFNULL(SUM(buyNum*standardFee+adjustmentFee),0) FROM hr_gongdan_book WHERE zt IN ('2','3') AND creator = ?
  #end
  #sql ("sumfee")
    SELECT SUM(chargeFee) FROM hr_gongdan_finance_fee a
    LEFT JOIN hr_gongdan_book b ON a.preServiceNo = b.orderNumber WHERE b.creator = ?
  #end
  #sql ("sumCharge")
    SELECT SUM(money) FROM hr_gongdan_finance_charge a WHERE b.nmbauc014 = ?
  #end
#end