#namespace("admin.hrGongdanFinanceCharge")
  #sql ("getHrGongdanFinanceChargeByNo")
    select * from hr_gongdan_finance_charge a where a.nmbaucdocno = ? LIMIT 0,1
  #end
#end
