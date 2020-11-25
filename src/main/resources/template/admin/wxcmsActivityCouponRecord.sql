#namespace("admin.wxcmsActivityCouponRecord")
  #sql ("getActivityCouponSendRecord")
    SELECT count(*) FROM wxcms_activity_coupon_record d where d.coupon_id = ? and d.open_id = ?
  #end
#end
