#namespace("admin.hrGongDanBook")
  #sql ("getBookInitNum")
    select count(*) from hr_gongdan_book a where DATE_FORMAT(a.create_time, '%Y%m%d') = #para(today)
  #end
  #sql ("getBookByOrderNumber")
    select * from hr_gongdan_book a where a.orderNumber = #para(orderNumber)
  #end
  #sql ("queryPageList")
    select a.*,(case when a.deal='2' then '2' when (SELECT COUNT(*)>0 FROM hr_gongdan_log t1 WHERE t1.preServiceNo=a.orderNumber)=1 then '1' else '0' end) deal_1 from hr_gongdan_book a where 1=1
    #if(search)
      and (a.orderNumber like CONCAT('%',#para(search),'%') or a.contact like CONCAT('%',#para(search),'%') or a.phone like CONCAT('%',#para(search),'%') or a.address like CONCAT('%',#para(search),'%') or a.remark like CONCAT('%',#para(search),'%'))
    #end
    #if(customerNo)
      and a.customerNo = #para(customerNo)
    #end
    #if(open_id)
      and a.open_id = #para(open_id)
    #end
    order by a.create_time desc
  #end
#end
