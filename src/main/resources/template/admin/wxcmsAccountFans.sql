#namespace("admin.wxcmsAccountFans")
  #sql ("getAccountFansByOpenId")
    SELECT d.* FROM wxcms_account_fans d where d.open_id = ?
  #end
  #sql ("queryPageList")
    SELECT a.* FROM wxcms_account_fans a where 1=1
    #if(search)
      and (a.province LIKE concat('%',#para(search),'%') or a.city LIKE concat('%',#para(search),'%')
      or a.district LIKE concat('%',#para(search),'%') or a.nick_name like CONCAT('%',#para(search),'%'))
    #end
  #end
  #sql ("delAccountFansByOpenId")
    delete FROM wxcms_account_fans where open_id = ?
  #end
#end
