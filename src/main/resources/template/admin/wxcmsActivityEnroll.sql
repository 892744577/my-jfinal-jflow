#namespace("admin.wxcmsActivityEnroll")
  #sql ("getEnroll")
    SELECT d.* FROM wxcms_activity_enroll d where d.ac_id = ? and d.open_id = ?
  #end
#end
