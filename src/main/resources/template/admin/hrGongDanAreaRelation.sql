#namespace("admin.hrGongDanAreaRelation")
  #sql ("queryPageList")
    select a.* from hr_gongdan_area_relation a where a.province=#para(province)
    and a.city=#para(city) and a.district=#para(district) and a.valid_flag=1
  #end
#end
