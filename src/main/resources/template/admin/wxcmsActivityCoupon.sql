#namespace("admin.wxcmsActivityCoupon")
  #sql ("getActivityCouponFirst")
    SELECT d.* FROM wxcms_activity_coupon d order by id desc limit 1
  #end
#end
