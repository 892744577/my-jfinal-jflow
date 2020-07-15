#namespace("admin.portActivityShare")
  #sql ("secondStep")
    SELECT d.* FROM port_activity_share d where d.sr_share_openid!=d.sr_to_share_openid and d.sr_to_share_openid=?
  #end
  #sql ("thirdStep")
    SELECT a.* FROM (SELECT d.* FROM port_activity_share d WHERE d.sr_share_openid != d.sr_to_share_openid) a
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
#end
