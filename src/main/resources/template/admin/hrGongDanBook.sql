#namespace("admin.hrGongDanBook")
  #sql ("getBookInitNum")
    select count(*) from hr_gongdan_book a where DATE_FORMAT(a.create_time, '%Y%m%d') = #para(today)
  #end
  #sql ("getBookByOrderNumber")
    select * from hr_gongdan_book a where a.orderNumber = #para(orderNumber)
  #end
  #sql ("queryPageList")
    SELECT a.*,(CASE WHEN a.deal='2' THEN '2' WHEN (SELECT COUNT(*)>0 FROM hr_gongdan_log t1 WHERE t1.preServiceNo=a.orderNumber)=1
    THEN '1' ELSE '0' END) deal_1,(SELECT t1.remark FROM hr_gongdan_log t1 WHERE t1.preServiceNo=a.orderNumber
    ORDER BY t1.create_time DESC LIMIT 1) returnReason FROM hr_gongdan_book a WHERE 1=1
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
