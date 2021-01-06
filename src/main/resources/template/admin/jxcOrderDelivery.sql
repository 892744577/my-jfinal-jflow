#namespace("admin.jxc")
  #sql ("getByDocno")
    select * from jxc_order_delivery a where a.docno=#para(docno)
  #end
  #sql ("queryPageList")
    select a.* FROM jxc_order_delivery a WHERE 1=1
    #if(search)
      and (a.docno like CONCAT('%',#para(search),'%'))
    #end
    order by a.deal_time desc
  #end
#end