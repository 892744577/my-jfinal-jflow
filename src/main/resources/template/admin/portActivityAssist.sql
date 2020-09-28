#namespace("admin.portActivityAssist")
  #sql ("getActivityAssistById")
    /*查询id的助力*/
    SELECT * FROM port_activity_assist WHERE id = ?
  #end
  #sql ("getActivityAssistByOpenid")
    /*查询某人所有的助力*/
    SELECT * FROM port_activity_assist WHERE as_openid = ?
  #end
  #sql ("isAssist")
    /*查询某人对某活动的某商品是否已经进行过助力*/
    SELECT * FROM port_activity_assist WHERE as_ac_id = ? and as_openid = ? and as_productid = ?
  #end
#end