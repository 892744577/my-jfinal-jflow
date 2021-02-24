#namespace("admin.portActivityShare")
  #sql ("secondStep")
    SELECT d.* FROM port_activity_share d left join port_activity_playbill b on b.id=d.sr_pb_id where d.sr_to_share_openid=? and b.pb_ac_id=?
  #end
  #sql ("thirdStep")
    SELECT a.* FROM (SELECT d.* FROM port_activity_share d left join port_activity_playbill b on b.id=d.sr_pb_id WHERE d.sr_share_openid != d.sr_to_share_openid and b.pb_ac_id=?) a
    WHERE  a.sr_share_openid = ? OR a.sr_to_share_openid  = ?
  #end
  #sql ("thirdStep_bak_20200715")
    SELECT d.* FROM port_activity_share d,
        (SELECT MIN(a.id) id ,MIN(a.create_time) create_time FROM port_activity_share a
        WHERE  a.sr_share_openid != a.sr_to_share_openid GROUP BY a.sr_to_share_openid) b
    WHERE d.id = b.id AND d.create_time=b.create_time and (d.sr_share_openid = ?
    or d.sr_to_share_openid = ? )
  #end

  #sql ("statistics1")
    SELECT count(*) FROM port_activity_share d where 1=1 and
    d.sr_pb_id in ( SELECT c.id FROM port_activity_playbill c WHERE c.pb_source_openid= #para(openid))
    #if(today)
      and DATE_FORMAT(d.create_time, '%Y-%m-%d') = #para(today)
    #end
  #end
  #sql ("statistics2")
    SELECT count(*) FROM port_activity_share d where 1=1 and valid_flag=1 and
    d.sr_pb_id in ( SELECT c.id FROM port_activity_playbill c WHERE c.pb_source_openid= #para(openid) )
    #if(today)
      and DATE_FORMAT(d.create_time, '%Y-%m-%d') = #para(today)
    #end
  #end
  #sql ("countHelper")
    select count(*) from port_activity_helper a left join port_activity_assist b on a.assistId = b.id where b.as_productid = ?
  #end
  #sql ("countAssist")
    select count(*) from port_activity_assist b where b.as_productid = ?
  #end
  #sql ("countInvolvedNum")
    select count(*) from port_activity_enroll
  #end
  #sql ("countAssistNum")
    select count(*) from port_activity_share b where b.valid_flag = 1
  #end
  #sql ("countBrowseNum")
    select count(*) from port_activity_share
  #end
  #sql ("excel")
    SELECT a.wxopenid,a.name,e.numb total,f.numb readTotal,e1.numb today,f1.numb readToday,g.address FROM port_activity_emp a
    LEFT JOIN  (
      SELECT c.pb_source_openid,COUNT(*) numb FROM port_activity_share d
      LEFT JOIN port_activity_playbill c ON d.sr_pb_id = c.id
      WHERE 1=1 AND valid_flag=1 and c.pb_ac_id=? GROUP BY d.sr_pb_id,c.pb_source_openid
      ) e ON a.wxopenid= e.pb_source_openid
    LEFT JOIN  (
      SELECT h.pb_source_openid,COUNT(*) numb FROM port_activity_share q
      LEFT JOIN port_activity_playbill h ON q.sr_pb_id = h.id where h.pb_ac_id=?
      GROUP BY q.sr_pb_id,h.pb_source_openid
      ) f ON a.wxopenid= f.pb_source_openid
    LEFT JOIN  (
      SELECT c1.pb_source_openid,COUNT(*) numb FROM port_activity_share d1
      LEFT JOIN port_activity_playbill c1 ON d1.sr_pb_id = c1.id
      WHERE 1=1 and c1.pb_ac_id=?  AND valid_flag=1 AND DATE_FORMAT(NOW(),'%m-%d-%Y')=DATE_FORMAT(d1.`create_time`,'%m-%d-%Y')
      GROUP BY d1.sr_pb_id,c1.pb_source_openid
      ) e1 ON a.wxopenid= e1.pb_source_openid
    LEFT JOIN  (
      SELECT h1.pb_source_openid,COUNT(*) numb FROM port_activity_share q1
      LEFT JOIN port_activity_playbill h1 ON q1.sr_pb_id = h1.id
      WHERE  h1.pb_ac_id=?  and DATE_FORMAT(NOW(),'%m-%d-%Y')=DATE_FORMAT(q1.`create_time`,'%m-%d-%Y')
      GROUP BY q1.sr_pb_id,h1.pb_source_openid
      ) f1 ON a.wxopenid=f1.pb_source_openid
    LEFT JOIN port_activity_address g ON g.id=a.OrgNo where a.accountType=?
  #end

  #sql ("getPortActivityShareByOpenId")
    SELECT * FROM port_activity_share WHERE sr_pb_id = ? and sr_share_openid = ? and sr_to_share_openid = ? LIMIT 0,1
  #end
#end
