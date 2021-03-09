#namespace("admin.hrGongdanYzjOvertime")
  #sql ("getHrGongdanYzjOvertime")
    SELECT d.* FROM hr_gongdan_yzj_overtime d where d.serialNo = ?
  #end
  #sql ("getHrGongdanYzjOvertimeDetail")
    SELECT d.* FROM hr_gongdan_yzj_overtime_detail d where d.serialNumWidget = ? and d.overtimeBegin = ? and d.overtimeEnd = ?
  #end
#end
