#namespace("admin.portActivityHelper")
  #sql ("getPortActivityHelperByHelperOpenId")
    SELECT * FROM port_activity_helper WHERE assistId = ? and helperOpenId = ? LIMIT 0,1
  #end
  #sql ("getPortActivityHelperByAssistId")
    select * from port_activity_helper where assistId = ?
  #end
#end