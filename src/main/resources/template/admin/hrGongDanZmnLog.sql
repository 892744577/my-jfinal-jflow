#namespace("admin.hrGongDanZmnLog")
  #sql ("queryPageList")
    select * from hr_gongdan_zmn_log a
    where 1=1
    #if(search)
      and (a.thirdOrderId like CONCAT('%',#para(search),'%'))
    #end
    order by a.id desc
  #end
#end
