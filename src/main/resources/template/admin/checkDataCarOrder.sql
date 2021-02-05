#namespace("admin.checkDataCarOrder")
  #sql ("maxCheckoutDate")
     SELECT MAX(a.create_time)  FROM check_data_car_order a
  #end
#end