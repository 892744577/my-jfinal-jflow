#namespace("admin.hrGongDanBook")
  #sql ("getHrGongDanBookInitNum")
    select * from hr_gongdan_book where orderNumber = #para(orderNumber)
  #end
  #sql ("queryPageList")
    select a.* from hr_gongdan_book a
    where 1=1
    #if(search)
      and (a.contact like CONCAT('%',#para(search),'%') or a.phone like CONCAT('%',#para(search),'%') or a.address like CONCAT('%',#para(search),'%') or a.remark like CONCAT('%',#para(search),'%'))
    #end
    order by a.create_time desc
  #end
#end
