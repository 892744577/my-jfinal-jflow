#namespace("admin.portActivityEmp")
  #sql ("getActivityEmpByWxOpenId")
    SELECT d.* FROM port_activity_emp d where d.WxOpenId = ? and accountType=?
  #end
  #sql ("getActivityEmpByTel")
    SELECT * FROM port_activity_emp WHERE Tel = ? LIMIT 0,1
  #end
#end
