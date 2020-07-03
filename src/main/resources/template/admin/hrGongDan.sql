#namespace("admin.hrGongDan")
  #sql ("getHrGongDanPageList")
    select a.*,b.FK_Node,b.NodeName from hr_gongdan a left join wf_generworkflow b on a.oid = b.WorkID
    where 1=1
      #if(today)
      and DATE_FORMAT(a.rdt, '%Y-%m-%d') = #para(today)
      #end
      #if(weekend)
      and YEARWEEK(DATE_FORMAT(a.rdt,'%Y-%m-%d'),1) = YEARWEEK(#para(weekend),1)
      #end
      #if(yearmonth)
      and DATE_FORMAT(a.rdt,'%Y%m') = #para(yearmonth)
      #end
      #if(yearmonth)
      and DATE_FORMAT(a.rdt,'%Y%m') = #para(yearmonth)
      #end
      #if(search)
      and (a.serviceNo like CONCAT('%',#para(search),'%') or a.address like CONCAT('%',#para(search),'%') or a.contactName like CONCAT('%',#para(search),'%') or a.telephone like CONCAT('%',#para(search),'%'))
      #end
      order by a.rdt desc
  #end
#end
