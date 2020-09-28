#namespace("admin.portActivityPlaybill")
  #sql ("getActivityPlaybillByWxOpenId")
    SELECT d.* FROM port_activity_playbill d where d.pb_source_openid = ? and pb_ac_id=?
  #end
#end