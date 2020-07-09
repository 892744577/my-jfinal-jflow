#namespace("admin.portActivityEnroll")
  #sql ("getEnroll")
    select * from port_activity_enroll where openid = ?
  #end
#end
