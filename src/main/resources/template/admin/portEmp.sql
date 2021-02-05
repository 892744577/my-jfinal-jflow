#namespace("admin.portEmp")
  #sql ("queryPageList")
    SELECT d.* FROM port_emp d where 1=1
  #end
  #sql ("getEmpByWxAppOpenId")
    SELECT d.* FROM port_emp d where d.WxAppOpenId = ?
  #end
  #sql ("getEmpByPass")
    SELECT d.* FROM port_emp d where d.No = ? and d.Pass = ?
  #end
  #sql ("getEmpByTel")
    SELECT d.* FROM port_emp d where d.tel = ?
  #end
  #sql ("getPortEmpByTeamNo")
    SELECT d.* FROM port_emp d where 1=1 and d.accountType=1 and d.teamNo = ?
  #end
  #sql ("getPortEmpByAccountType")
    SELECT d.*,
      (
      SELECT COUNT(*) FROM wxcms_account_qrcode_fans a
      LEFT JOIN wxcms_account_shop b ON a.event_key=b.shopNo
      WHERE d.teamNo=b.agentNo
      ) teamFansNum,
      (
      SELECT COUNT(*) FROM wxcms_account_qrcode_fans a
      LEFT JOIN wxcms_account_shop b ON a.event_key=b.shopNo
      WHERE DATE_FORMAT(a.create_time, '%y-%m-%d' )=DATE_FORMAT(NOW(), '%y-%m-%d' ) AND d.teamNo=b.agentNo
      ) todayTeamFansNum
    FROM port_emp d WHERE 1=1 AND d.accountType=1
  #end
  #sql ("getPortEmpByShopId")
    SELECT d.* FROM port_emp d left join wxcms_account_shop s on d.teamNo = s.agentNo where d.accountType='1' and s.id = ?
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
  select g.* from (
    SELECT e.*,a.fk_dept,a.teamNo,a.No FROM port_emp a
        LEFT JOIN wxcms_account_shop b ON a.teamNo = b.agentNo
        LEFT JOIN wxcms_account_shop_qrcode c ON b.id = c.shop_id
        LEFT JOIN wxcms_account_qrcode_fans d ON c.qrcode_param = d.event_key
        LEFT JOIN wxcms_account_fans e ON d.fromuser_name = e.open_id
        WHERE 1=1 AND e.open_id IS NOT NULL
        #if(teamNo1)
          and a.teamNo = #para(teamNo1)
        #end
        #if(portEmpNo1)
          and a.No = #para(portEmpNo1)
        #end
    UNION
     SELECT c.*,a.fk_dept,a.teamNo,a.No FROM port_emp a
    LEFT JOIN wxcms_account_qrcode_fans b ON a.teamNo = b.event_key
    LEFT JOIN wxcms_account_fans c ON b.fromuser_name = c.open_id
    WHERE 1=1 AND c.open_id IS NOT NULL
    #if(teamNo2)
      and a.teamNo = #para(teamNo2)
    #end
    #if(portEmpNo2)
      and a.No = #para(portEmpNo2)
    #end
    ) g
  #end
#end
