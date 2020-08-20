#namespace("admin.portEmp")
  #sql ("getEmpByWxAppOpenId")
    SELECT d.* FROM port_emp d where d.WxAppOpenId = ?
  #end
  #sql ("queryPageList")
    SELECT d.* FROM port_emp d
  #end
#end
