#namespace("admin.portActivityEmp")
  #sql ("getActivityEmpByWxOpenId")
    SELECT d.* FROM port_activity_emp d where d.WxOpenId = ? and accountType=?
  #end
#end
