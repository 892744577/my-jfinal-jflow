#namespace("admin.portActivityEnroll")
  #sql ("getEnroll")
    select * from port_activity_enroll where wx_openid = ? and en_ac_id=?
  #end
#end
