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
    select a.* from port_emp a left join gpm_groupemp b on a.No = b.FK_Emp left join gpm_group c on b.FK_Group = c.No where b.FK_Group in ('02','03')
  #end
  #sql ("getEmpAndActivityEmpByTel")
    SELECT d.* FROM port_emp d where d.tel = ?
    union
    SELECT d.* FROM port_activity_emp d where d.tel = ?
  #end
  #sql ("queryFansByFkDeptPageList")
    select e.* from port_emp a
    left join wxcms_account_shop b on a.FK_Dept = b.agentNo
    left join wxcms_account_shop_qrcode c on b.id = c.shop_id
    left join wxcms_account_qrcode_fans d on c.qrcode_param = d.event_key
    left join wxcms_account_fans e on d.fromuser_name = e.open_id
    where 1=1
    #if(fkDept)
      and a.FK_Dept = #para(fkDept)
    #end
    #if(portEmpNo)
      and a.No = #para(portEmpNo)
    #end
  #end
  #sql ("getPortEmpByTeamNo")
    SELECT d.* FROM port_emp d where d.accountType='1' and d.teamNo = ?
  #end
  #sql ("getPortEmpByShopId")
    SELECT d.* FROM port_emp d left join wxcms_account_shop s on d.teamNo = s.agentNo where d.accountType='1' and s.id = ?
  #end
#end
