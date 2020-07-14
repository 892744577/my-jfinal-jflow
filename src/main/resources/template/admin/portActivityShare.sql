#namespace("admin.portActivityShare")
  #sql ("secondStep")
    SELECT d.* FROM port_activity_share d where d.sr_to_share_openid!=? and d.sr_to_share_openid=?
  #end
  #sql ("thirdStep")
    SELECT d.* FROM port_activity_share d,
        (SELECT MIN(a.id) id ,MIN(a.create_time) create_time FROM port_activity_share a
        WHERE  a.sr_share_openid != a.sr_to_share_openid GROUP BY a.sr_to_share_openid) b
    WHERE d.id = b.id AND d.create_time=b.create_time AND d.sr_pb_id = ? and (d.sr_share_openid = ?
    or d.sr_share_openid = ? )
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
#end
