#namespace("admin.hrGongDan")
  #sql ("getHrGongDanPageList")
    select a.*,b.FK_Node,b.NodeName from hr_gongdan a left join wf_generworkflow b on a.oid = b.WorkID
    where 1=1 and  b.FK_Node != 901
      #if(search)
      and (a.serviceNo like CONCAT('%',#para(search),'%') or a.orderId like CONCAT('%',#para(search),'%') or a.address like CONCAT('%',#para(search),'%') or a.contactName like CONCAT('%',#para(search),'%') or a.telephone like CONCAT('%',#para(search),'%'))
      #end
      #if(serviceSystem)
      and a.serviceSystem = #para(serviceSystem)
      #end
      #if(acceptor)
      and a.acceptor = #para(acceptor)
      #end
      #if(master)
        and EXISTS(
        SELECT * FROM nd9track d WHERE d.ndfrom='908' AND d.tag LIKE CONCAT('%',#para(master),'%') AND a.oid=d.WorkID)
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
      and b.FK_Node = 903 and timestampadd(day, 1, (select max(c.rdt) from wf_generworkerlist c where c.FK_Node = b.FK_Node)) < #para(overtime)
      #end
      #if(toBeCompleted)
      and b.FK_Node != #para(toBeCompleted)
      #end
      #if(serviceSp)
      and a.serviceSp = #para(serviceSp)
      #end

      order by a.rdt desc
  #end
  #sql ("getHrGongDanByOrderId")
    select * from hr_gongdan a where a.orderId=#para(orderId)
  #end
  #sql ("getHrGongDanListPage")
    #(select) FROM (
    select a.*,b.FK_Node,b.NodeName,c.name acceptorName from hr_gongdan a left join wf_generworkflow b on a.oid = b.WorkID left join port_emp c on a.acceptor = c.no
    where 1=1 and  b.FK_Node != 901
      #if(search)
      and (a.serviceNo like CONCAT('%',#para(search),'%') or a.orderId like CONCAT('%',#para(search),'%') or a.address like CONCAT('%',#para(search),'%') or a.contactName like CONCAT('%',#para(search),'%') or a.telephone like CONCAT('%',#para(search),'%'))
      #end
      #if(serviceSystem)
      and a.serviceSystem = #para(serviceSystem)
      #end
      #if(acceptor)
      and a.acceptor = #para(acceptor)
      #end
      #if(master)
        and EXISTS(
        SELECT * FROM nd9track d WHERE d.ndfrom='908' AND d.tag LIKE CONCAT('%',#para(master),'%') AND a.oid=d.WorkID)
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
      and b.FK_Node = 903 and timestampadd(day, 1, (select max(c.rdt) from wf_generworkerlist c where c.FK_Node = b.FK_Node)) < #para(overtime)
      #end
      #if(toBeCompleted)
      and b.FK_Node != #para(toBeCompleted)
      #end
      #if(serviceSp)
      and a.serviceSp = #para(serviceSp)
      #end
    ) as views
        where 1=1
         #for(query : queryList) #if(query.get("type")==2) #(query.get("sql")) #end #end
        ORDER BY
        #(orderByKey) #(orderByType)
        #if(page&&limit) LIMIT #(page),#(limit) #end
  #end
  #sql ("queryHrGongDanListCount")
    SELECT count(1) FROM (
    select a.*,b.FK_Node,b.NodeName,c.name acceptorName from hr_gongdan a left join wf_generworkflow b on a.oid = b.WorkID left join port_emp c on a.acceptor = c.no
    where 1=1 and  b.FK_Node != 901
      #if(search)
      and (a.serviceNo like CONCAT('%',#para(search),'%') or a.orderId like CONCAT('%',#para(search),'%') or a.address like CONCAT('%',#para(search),'%') or a.contactName like CONCAT('%',#para(search),'%') or a.telephone like CONCAT('%',#para(search),'%'))
      #end
      #if(serviceSystem)
      and a.serviceSystem = #para(serviceSystem)
      #end
      #if(acceptor)
      and a.acceptor = #para(acceptor)
      #end
      #if(master)
        and EXISTS(
        SELECT * FROM nd9track d WHERE d.ndfrom='908' AND d.tag LIKE CONCAT('%',#para(master),'%') AND a.oid=d.WorkID)
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
      and b.FK_Node = 903 and timestampadd(day, 1, (select max(c.rdt) from wf_generworkerlist c where c.FK_Node = b.FK_Node)) < #para(overtime)
      #end
      #if(toBeCompleted)
      and b.FK_Node != #para(toBeCompleted)
      #end
      #if(serviceSp)
      and a.serviceSp = #para(serviceSp)
      #end
    ) as views
        where 1=1
         #for(query : queryList) #if(query.get("type")==2) #(query.get("sql")) #end #end
        ORDER BY
        #(orderByKey) #(orderByType)
  #end
#end
