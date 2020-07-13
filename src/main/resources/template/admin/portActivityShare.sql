#namespace("admin.portActivityShare")
  #sql ("secondStep")
    SELECT d.* FROM port_activity_share d where d.sr_to_share_openid=?
  #end
  #sql ("thirdStep")
    SELECT d.* FROM port_activity_share d,
        (SELECT MIN(a.id) id ,MIN(a.create_time) create_time FROM port_activity_share a
        WHERE  a.sr_share_openid != a.sr_to_share_openid GROUP BY a.sr_to_share_openid) b
    WHERE d.id = b.id AND d.create_time=b.create_time AND d.sr_pb_id = ? AND d.sr_share_openid != ?
    AND (SELECT c.pb_source_openid FROM port_activity_playbill c WHERE c.id=d.sr_pb_id) !=?
  #end
#end
