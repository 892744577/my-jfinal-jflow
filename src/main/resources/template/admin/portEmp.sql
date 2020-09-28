#namespace("admin.portEmp")
  #sql ("getEmpByWxAppOpenId")
    SELECT d.* FROM port_emp d where d.WxAppOpenId = ?
  #end
  #sql ("getEmpByPass")
    SELECT d.* FROM port_emp d where d.No = ? and d.Pass = ?
  #end
  #sql ("getEmpByTel")
    SELECT d.* FROM port_emp d where d.tel = ?
  #end
  #sql ("queryPageList")
    SELECT d.* FROM port_emp d
  #end
  #sql ("queryAfterSalePortEmpList")
    select a.* from port_emp a left join gpm_groupemp b on a.No = b.FK_Emp left join gpm_group c on b.FK_Group = c.No where b.FK_Group in ('03')
  #end
#end
