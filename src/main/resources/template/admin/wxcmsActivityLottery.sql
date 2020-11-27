#namespace("admin.wxcmsActivityLottery")
  #sql ("getActivityLotteryByOpenid")
    SELECT d.* FROM wxcms_activity_lottery d where  d.ac_id = ? and d.open_id = ?
  #end
  #sql ("getActivityLotteryByWinner")
    SELECT d.* FROM wxcms_activity_lottery d where  d.ac_id = ? and d.win_or_not = ?
  #end
#end
