#namespace("admin.jxc")
  #sql ("getCodeByDocno")
    select * from jxc_order_delivery_code a where a.docno=#para(docno)
  #end

  #sql ("getByCode")
    select * from jxc_order_delivery_code a where a.code=#para(code)
  #end
#end