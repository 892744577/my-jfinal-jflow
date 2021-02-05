#namespace("admin.checkDataHotelOrder")
  #sql ("maxCheckoutDate")
     SELECT MAX(a.checkout_date)  FROM check_data_hotel_order a
  #end
#end