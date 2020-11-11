#namespace("admin.wxcmsAccountAgent")
  #sql ("queryPageList")
    SELECT a.* FROM wxcms_account_agent a where 1=1
    #if(search)
      and (a.province LIKE concat('%',#para(search),'%') or a.city LIKE concat('%',#para(search),'%')
      or a.district LIKE concat('%',#para(search),'%') or a.agentNo like CONCAT('%',#para(search),'%'))
    #end
  #end
#end
