#namespace("admin.examineStep")
   #sql("queryExamineStepByExamineId")
      select * from aptenon_admin_examine_step where  examine_id = ?
   #end
   #sql("deleteExamineStepByExamineId")
      DELETE  FROM  aptenon_admin_examine_step WHERE examine_id = ?
   #end
   #sql("queryExamineStepByExamineIdOrderByStepNum")
      SELECT * FROM aptenon_admin_examine_step WHERE examine_id = ? ORDER BY step_num LIMIT 0,1
   #end
   #sql ("queryExamineStepOrderId")
   select order_id from aptenon_admin_examine_log
   where record_id = ? and is_recheck = 0 and examine_status !=0 order by order_id desc limit 1
   #end
   #sql("queryExamineStepByNextExamineIdOrderByStepId")
        SELECT * FROM aptenon_admin_examine_step WHERE examine_id = ?
        and step_num =  (SELECT step_num FROM aptenon_admin_examine_step where step_id = ?) + 1
   #end
   #sql("queryExamineRecordById")
     SELECT saer.* ,sau.img,sau.realname from aptenon_admin_examine_record as  saer
     LEFT JOIN aptenon_admin_user as sau on sau.user_id = saer.create_user
     WHERE saer.record_id = ?
   #end
   #sql("queryExamineStepByLastExamineIdOrderByStepId")
        SELECT * FROM aptenon_admin_examine_step WHERE examine_id = ?
        and step_num =  (SELECT step_num FROM aptenon_admin_examine_step where step_id = ?) - 1
   #end
#end
