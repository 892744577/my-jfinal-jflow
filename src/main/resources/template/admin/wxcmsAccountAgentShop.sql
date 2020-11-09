#namespace("admin.wxcmsAccountAgentShop")
  #sql ("getShopByAgentId")
    SELECT d.* FROM wxcms_account_agent_shop d where d.agent_id = ?
  #end
#end
