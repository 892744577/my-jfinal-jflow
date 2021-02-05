#namespace("admin.checkDataAnalysis")
  #sql ("getAnalysisByPhoneAndDate")
     SELECT *  FROM check_data_analysis_2 a WHERE a.phone=? and a.stayDay=?
  #end
#end