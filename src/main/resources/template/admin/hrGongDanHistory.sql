#namespace("admin.hrGongDanHistory")
  #sql ("getHrGongDanHistoryPageList")
    select a.* from hr_gongdan_history a
      where 1=1
      #if(search)
      and (a.title like CONCAT('%',#para(search),'%') or a.orderNo like CONCAT('%',#para(search),'%')
      or a.fuselageCode like CONCAT('%',#para(search),'%'))
      #end
      order by a.applicationDate desc
  #end
#end
