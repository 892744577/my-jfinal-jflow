#namespace("admin.portEmp")
  #sql ("getEmpByWxAppOpenId")
    SELECT d.* FROM port_emp d where d.WxAppOpenId = ?
  #end
  #sql ("queryPageList")
    SELECT d.* FROM port_emp d
  #end
  #sql ("queryAfterSalePortEmpList")
    select a.* from port_emp a left join gpm_groupemp b on a.No = b.FK_Emp left join gpm_group c on b.FK_Group = c.No where b.FK_Group = '02'
  #end
#end
