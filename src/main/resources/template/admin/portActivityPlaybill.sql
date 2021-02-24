#namespace("admin.portActivityPlaybill")
  #sql ("getActivityPlaybillByWxOpenId")
    SELECT d.* FROM port_activity_playbill d where d.pb_source_openid = ? and pb_ac_id=?
  #end

  #sql ("getActivityPlaybillByWxOpenId1")
    SELECT * FROM port_activity_playbill WHERE pb_source_openid = ? and pb_ac_id = ? LIMIT 0,1
  #end
#end