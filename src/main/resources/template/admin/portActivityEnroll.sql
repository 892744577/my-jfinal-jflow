#namespace("admin.portActivityEnroll")
  #sql ("getEnroll")
    select * from port_activity_enroll where wx_openid = ?
  #end
#end
