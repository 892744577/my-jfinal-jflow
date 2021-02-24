#namespace("admin.portActivity")
  #sql ("getPortActivityByPbId")
    SELECT a.*,b.pb_source_openid FROM port_activity a left join port_activity_playbill b on a.id = b.pb_ac_id where b.id = ? LIMIT 0,1
  #end
  #sql ("getPortActivityByShareId")
    SELECT a.*,b.id pbId,c.sr_share_openid,c.sr_to_share_openid,c.sr_as_id FROM port_activity a left join port_activity_playbill b on a.id = b.pb_ac_id left join port_activity_share c on b.id = c.sr_pb_id where c.id = ? LIMIT 0,1
  #end
  #sql ("getPortActivityByAcId")
    SELECT * FROM port_activity WHERE id = ? LIMIT 0,1
  #end
  #sql ("getPortActivityBySourceOpenId")
    SELECT a.*,b.id pbId FROM port_activity a left join port_activity_playbill b on a.id = b.pb_ac_id where b.pb_source_openid = ? and b.pb_ac_id = ? LIMIT 0,1
  #end
  #sql ("getPortActivityEmpByWxOpenId")
    SELECT * FROM port_activity_emp WHERE WxOpenId = ? and accountType = ?  LIMIT 0,1
  #end
#end