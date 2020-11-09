#namespace("admin.wxcmsAccountFans")
  #sql ("getAccountFansByOpenId")
    SELECT d.* FROM wxcms_account_fans d where d.open_id = ?
  #end
#end
