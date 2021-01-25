#namespace("admin.competitiveNews")
  #sql ("queryPageList")
    SELECT d.* FROM wxcms_competitive_news d
    where 1=1
    #if(search)
      and (a.opinion1 LIKE concat('%',#para(search),'%') or a.opinion2 LIKE concat('%',#para(search),'%')
      or a.opinion3 LIKE concat('%',#para(search),'%') or a.opinion4 like CONCAT('%',#para(search),'%'))
    #end
  #end
#end
