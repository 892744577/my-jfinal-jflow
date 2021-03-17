#namespace("admin.hrGongdanFinanceChargeService")
  #sql ("getHrGongdanFinanceChargeByNo")
    select * from hr_gongdan_finance_charge_service a where a.nmbaucdocno = ? LIMIT 0,1
  #end
#end
