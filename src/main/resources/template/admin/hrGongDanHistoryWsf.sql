#namespace("admin.hrGongDanHistoryWsf")
  #sql ("getHrGongDanHistoryWsfPageList")
    select a.* from hr_gongdan_history_wsf a
      where 1=1
      #if(search)
      and (a.orderNo like CONCAT('%',#para(search),'%') or a.address like CONCAT('%',#para(search),'%')
      or a.customerNote like CONCAT('%',#para(search),'%') or a.printNote like CONCAT('%',#para(search),'%')
      or a.internalNote like CONCAT('%',#para(search),'%') or a.consignee like CONCAT('%',#para(search),'%')
       or a.receivePhone like CONCAT('%',#para(search),'%'))
      #end
      order by a.trialDate desc
  #end
#end
