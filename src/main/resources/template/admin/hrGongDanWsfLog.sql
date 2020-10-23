#namespace("admin.hrGongDanWsfLog")
  #sql ("queryPageList")
    select * from hr_gongdan_wsf_log a
    where 1=1
    #if(search)
      and (a.thirdOrderId like CONCAT('%',#para(search),'-','%'))
    #end
    order by a.optTime desc
  #end
#end
