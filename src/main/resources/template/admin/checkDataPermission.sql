#namespace("admin.checkDataPermission")
  #sql ("getCheckDataPermissionByPhone")
    SELECT d.* FROM check_data_permission d where d.phone = ?
  #end
#end
