#namespace("admin.hrGongDanFjf")
  #sql ("getHrGongDanFjfInitNum")
    select count(*) from hr_gongdan_fjf a where DATE_FORMAT(a.createTime, '%Y%m%d') = #para(today)
  #end
  #sql ("queryPageList")
    select * from hr_gongdan_fjf a
    where 1=1
    #if(search)
      and (a.serviceNo like CONCAT('%',#para(search),'%') or a.workId like CONCAT('%',#para(search),'%'))
    #end
    order by a.createTime desc
  #end
#end
