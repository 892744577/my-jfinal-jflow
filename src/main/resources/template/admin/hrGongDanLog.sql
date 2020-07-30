#namespace("admin.hrGongDanLog")
  #sql ("queryPageList")
    select * from hr_gongdan_log a
    where 1=1
    #if(search)
      and (a.preServiceNo like CONCAT('%',#para(search),'%'))
    #end
    order by a.create_time desc
  #end
#end
