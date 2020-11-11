#namespace("admin.wxcmsAccountAgent")
  #sql ("queryPageList")
    SELECT a.*,b.nick_name,b.gender,b.head_img_url FROM wxcms_account_agent a
    LEFT JOIN wxcms_account_fans b ON a.open_id = b.open_id
    where 1=1
    #if(search)
      and (a.province LIKE concat('%',#para(search),'%') or a.city LIKE concat('%',#para(search),'%')
      or a.district LIKE concat('%',#para(search),'%') or a.agentNo like CONCAT('%',#para(search),'%'))
    #end
  #end
#end
