#namespace("admin.hrGongDanRepair")
  #sql ("getRepairInitNum")
    select count(*) from hr_gongdan_repair a where DATE_FORMAT(a.create_time, '%Y%m%d') = #para(today)
  #end
  #sql ("getRepairByOrderNumber")
    select * from hr_gongdan_repair a where a.orderNumber = #para(orderNumber)
  #end
  #sql ("queryPageList")
    select * from (
    select a.*,(case when a.deal='2' then '2' when (SELECT COUNT(*)>0 FROM hr_gongdan_log t1
    WHERE t1.preServiceNo=a.orderNumber)=1 then '1' else '0' end) deal_1,
    (select count(*) from hr_gongdan_log c where c.preServiceNo = a.orderNumber) logNum from
    hr_gongdan_repair a) b where 1=1
    #if(search)
      and (b.orderNumber like CONCAT('%',#para(search),'%') or b.contact like CONCAT('%',#para(search),'%') or b.equipSNCode like CONCAT('%',#para(search),'%')
      or b.phone like CONCAT('%',#para(search),'%') or b.address like CONCAT('%',#para(search),'%') or b.remark like CONCAT('%',#para(search),'%'))
    #end
    #if(open_id)
      and b.open_id = #para(open_id)
    #end
    #if(deal)
      and b.deal_1 = #para(deal)
    #end
    order by b.create_time desc
  #end
#end
