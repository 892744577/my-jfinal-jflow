#namespace("admin.hrGongDan")
  #sql ("getHrGongDanPageList")
    select a.*,b.FK_Node,b.NodeName from hr_gongdan a left join wf_generworkflow b on a.oid = b.WorkID
    LEFT JOIN wf_generworkerlist c ON c.WorkID = b.WorkID AND c.FK_Node = b.FK_Node
    where 1=1 and  b.FK_Node != 901
      #if(search)
      and (a.serviceNo like CONCAT('%',#para(search),'%') or a.address like CONCAT('%',#para(search),'%') or a.contactName like CONCAT('%',#para(search),'%') or a.telephone like CONCAT('%',#para(search),'%'))
      #end
      #if(today)
      and DATE_FORMAT(a.rdt, '%Y-%m-%d') = #para(today)
      #end
      #if(weekend)
      and YEARWEEK(DATE_FORMAT(a.rdt,'%Y-%m-%d'),1) = YEARWEEK(#para(weekend),1)
      #end
      #if(yearmonth)
      and DATE_FORMAT(a.rdt,'%Y%m') = #para(yearmonth)
      #end
      #if(confirm)
      and b.FK_Node = #para(confirm)
      #end
      #if(overtime)
      and b.FK_Node = 903 and timestampadd(day, 1, c.rdt) < #para(overtime)
      #end
      #if(toBeCompleted)
      and b.FK_Node != #para(toBeCompleted)
      #end

      order by a.rdt desc
  #end
  #sql ("getHrGongDanByOrderId")
    select * from hr_gongdan a where a.orderId=#para(orderId)
  #end
#end
