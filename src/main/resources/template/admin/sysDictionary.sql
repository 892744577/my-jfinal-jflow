#namespace("admin.dictionary")
  #sql ("getSysSfTableByNo")
    select * from sys_sftable a where a.No=?
  #end
  #sql ("getSysEnummainByNo")
    select * from sys_enummain a where a.No=?
  #end
#end