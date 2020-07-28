#namespace("admin.hrGongDanRepair")
  #sql ("getHrGongDanRepairInitNum")
    select * from hr_gongdan_repair where orderNumber = #para(orderNumber)
  #end
  #sql ("queryPageList")
    select a.* from hr_gongdan_repair a
    #if(search)
      and (a.contact like CONCAT('%',#para(search),'%') or a.phone like CONCAT('%',#para(search),'%') or a.address like CONCAT('%',#para(search),'%') or a.remark like CONCAT('%',#para(search),'%'))
    #end
    order by a.create_time desc
  #end
#end
