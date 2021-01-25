#namespace("admin.competitive")
  #sql ("queryPageList")
    SELECT d.* FROM wxcms_competitive d
    where 1=1
    #if(search)
      and (a.brand LIKE concat('%',#para(search),'%') or a.product LIKE concat('%',#para(search),'%')
      or a.advantage LIKE concat('%',#para(search),'%') or a.disadvantage like CONCAT('%',#para(search),'%'))
    #end
  #end
#end
