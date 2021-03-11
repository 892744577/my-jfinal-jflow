#namespace("admin.hrGongdanFinance")
  #sql ("sumBookFee")
    SELECT IFNULL(SUM(buyNum*standardFee+adjustmentFee),0) FROM hr_gongdan_book WHERE zt IN ('0','1','2') AND creator = ?
  #end
  #sql ("sumFee")
    SELECT IFNULL(SUM(chargeFee),0) FROM hr_gongdan_finance_fee a
    LEFT JOIN hr_gongdan_book b ON a.preServiceNo = b.orderNumber WHERE b.creator = ?
  #end
  #sql ("sumCharge")
    SELECT IFNULL(SUM(money),0) FROM hr_gongdan_finance_charge a WHERE a.nmbauc014 = ?
  #end
#end