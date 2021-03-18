#namespace("admin.ioneWarrantyCard")
  #sql ("maxCreateDate")
    SELECT max(a.create_date) FROM wxcms_ione_warranty_card a
  #end
#end
