#namespace("admin.portActivityAddress")
  #sql ("getAddress")
    select * from port_activity_address where visiable=1 order by id desc
  #end
#end
