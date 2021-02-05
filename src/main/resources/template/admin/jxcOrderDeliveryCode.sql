#namespace("admin.jxcCode")
  #sql ("queryPageList")
    select * from jxc_order_delivery_code a where 1=1
    #if(search)
      and (a.code=#para(search) or a.docno=#para(search))
    #end
    #if(customer)
      and a.customer=#para(customer)
    #end
    order by a.createTime
  #end
#end