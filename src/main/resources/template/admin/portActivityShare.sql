#namespace("admin.portActivityShare")
  #sql ("validFlagOrNot")
    SELECT b.* FROM
    (SELECT a.* FROM port_activity_share a WHERE  a.sr_share_openid != a.sr_to_share_openid
    GROUP BY a.sr_to_share_openid HAVING MIN(a.create_time)=a.create_time) b
    WHERE b.sr_pb_id=? AND b.sr_share_openid != ?
    AND (SELECT c.pb_source_openid FROM port_activity_playbill c WHERE c.id=b.sr_pb_id)!=?
  #end
#end
