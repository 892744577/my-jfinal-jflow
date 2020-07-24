#namespace("gpm.menu")
    #sql("queryMenuByUserId")
      SELECT DISTINCT f.*,g.* FROM (
      /*人--》菜单*/
      SELECT fk_emp empid,fk_menu menuid FROM gpm_empmenu
      UNION
      /*人--》岗位--》菜单*/
      SELECT a.No empid,c.FK_Menu menuid FROM port_emp a
      LEFT JOIN port_deptempstation b ON a.no = b.FK_Emp
      LEFT JOIN gpm_stationmenu c ON c.FK_Station = b.FK_Station
      UNION
      /*人--》组 --》菜单*/
      SELECT a.No empid,c.FK_Menu menuid FROM port_emp a
      LEFT JOIN gpm_groupemp b ON a.No = b.FK_Emp
      LEFT JOIN gpm_groupmenu c ON  b.FK_Group = c.FK_Group
      UNION
      /*人--》组--》岗位--》菜单*/
      SELECT a.No empid,d.FK_Menu menuid FROM port_emp a
      LEFT JOIN gpm_groupemp b ON a.No = b.FK_Emp
      LEFT JOIN gpm_groupstation c ON b.FK_Group = c.FK_Group
      LEFT JOIN gpm_stationmenu d ON c.FK_Station = d.FK_Station ) f
      LEFT JOIN gpm_menu g ON g.No = f.menuid
      WHERE f.menuid IS NOT NULL
      #if(userNo)
      and f.empid = #para(userNo)
      #end
    #end
#end


