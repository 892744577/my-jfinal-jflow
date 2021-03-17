#namespace("admin.ioneUserInfo")
  #sql ("maxCreateDate")
    SELECT max(a.create_date) FROM wxcms_ione_user_info a
  #end
  #sql ("deleteByOpenidAndCode")
    delete FROM wxcms_ione_user_info a where a.openid=? and
  #end
#end
