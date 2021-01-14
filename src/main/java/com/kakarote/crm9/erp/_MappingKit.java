package com.kakarote.crm9.erp;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.kakarote.crm9.erp.admin.entity.*;
import com.kakarote.crm9.erp.crm.entity.*;
import com.kakarote.crm9.erp.jxc.entity.JxcOrderDelivery;
import com.kakarote.crm9.erp.jxc.entity.JxcOrderDeliveryCode;
import com.kakarote.crm9.erp.oa.entity.*;
import com.kakarote.crm9.erp.work.entity.*;
import com.kakarote.crm9.erp.wxcms.entity.*;
import com.kakarote.crm9.erp.yeyx.entity.*;

public class _MappingKit {

	public static void mapping(ActiveRecordPlugin arp) {
		arp.addMapping("aptenon_admin_dept", "dept_id", AdminDept.class);
		arp.addMapping("aptenon_admin_examine", "examine_id", AdminExamine.class);
		arp.addMapping("aptenon_admin_config", "setting_id", AdminConfig.class);
		arp.addMapping("aptenon_admin_examine_record", "record_id", AdminExamineRecord.class);
		arp.addMapping("aptenon_admin_examine_step", "step_id", AdminExamineStep.class);
		arp.addMapping("aptenon_admin_examine_log", "log_id", AdminExamineLog.class);
		arp.addMapping("aptenon_admin_field", "field_id", AdminField.class);
		arp.addMapping("aptenon_admin_field_sort", "field_id", AdminFieldSort.class);
		arp.addMapping("aptenon_admin_field_style", "id", AdminFieldStyle.class);
		arp.addMapping("aptenon_admin_fieldv", "id", AdminFieldv.class);
		arp.addMapping("aptenon_admin_file", "file_id", AdminFile.class);
		arp.addMapping("aptenon_admin_menu", "menu_id", AdminMenu.class);
		arp.addMapping("aptenon_admin_message", "message_id", AdminMessage.class);
		arp.addMapping("aptenon_admin_record", "record_id", AdminRecord.class);
		arp.addMapping("aptenon_admin_role", "role_id", AdminRole.class);
		arp.addMapping("aptenon_admin_role_menu", "id", AdminRoleMenu.class);
		arp.addMapping("aptenon_admin_scene", "scene_id", AdminScene.class);
		arp.addMapping("aptenon_admin_scene_default", "default_id", AdminSceneDefault.class);
		arp.addMapping("aptenon_admin_user", "user_id", AdminUser.class);
		arp.addMapping("aptenon_admin_user_role", "id", AdminUserRole.class);
		arp.addMapping("aptenon_crm_business", "business_id", CrmBusiness.class);
		arp.addMapping("aptenon_crm_achievement", "achievement_id", CrmAchievement.class);
		arp.addMapping("aptenon_crm_action_record", "id", CrmActionRecord.class);
		arp.addMapping("aptenon_crm_business_product", "r_id", CrmBusinessProduct.class);
		arp.addMapping("aptenon_crm_business_status", "status_id", CrmBusinessStatus.class);
		arp.addMapping("aptenon_crm_business_type", "type_id", CrmBusinessType.class);
		arp.addMapping("aptenon_crm_contacts", "contacts_id", CrmContacts.class);
		arp.addMapping("aptenon_crm_contacts_business", "id", CrmContactsBusiness.class);
		arp.addMapping("aptenon_crm_contract", "contract_id", CrmContract.class);
		arp.addMapping("aptenon_crm_contract_product", "r_id", CrmContractProduct.class);
		arp.addMapping("aptenon_crm_customer", "customer_id", CrmCustomer.class);
		arp.addMapping("aptenon_crm_customer_setting", "setting_id", CrmCustomerSetting.class);
		arp.addMapping("aptenon_crm_customer_settinguser", "id", CrmCustomerSettingUser.class);
		arp.addMapping("aptenon_crm_leads", "leads_id", CrmLeads.class);
		arp.addMapping("aptenon_crm_owner_record", "record_id", CrmOwnerRecord.class);
		arp.addMapping("aptenon_crm_product", "product_id", CrmProduct.class);
		arp.addMapping("aptenon_crm_product_category", "category_id", CrmProductCategory.class);
		arp.addMapping("aptenon_crm_receivables", "receivables_id", CrmReceivables.class);
		arp.addMapping("aptenon_crm_receivables_plan", "plan_id", CrmReceivablesPlan.class);
		arp.addMapping("aptenon_oa_action_record", "log_id", OaActionRecord.class);
		arp.addMapping("aptenon_oa_announcement", "announcement_id", OaAnnouncement.class);
		arp.addMapping("aptenon_oa_event", "event_id", OaEvent.class);
		arp.addMapping("aptenon_oa_event_relation", "eventrelation_id", OaEventRelation.class);
		arp.addMapping("aptenon_oa_examine_category", "category_id", OaExamineCategory.class);
		arp.addMapping("aptenon_oa_examine_relation", "r_id", OaExamineRelation.class);
		arp.addMapping("aptenon_oa_examine_travel", "travel_id", OaExamineTravel.class);
		arp.addMapping("aptenon_oa_examine", "examine_id", OaExamine.class);
		arp.addMapping("aptenon_oa_examine_log", "log_id", OaExamineLog.class);
		arp.addMapping("aptenon_oa_examine_record", "record_id", OaExamineRecord.class);
		arp.addMapping("aptenon_oa_examine_step", "step_id", OaExamineStep.class);
		arp.addMapping("aptenon_oa_log", "log_id", OaLog.class);
		arp.addMapping("aptenon_oa_log_relation", "r_id", OaLogRelation.class);
		arp.addMapping("aptenon_task", "task_id", Task.class);
		arp.addMapping("aptenon_task_comment", "comment_id", TaskComment.class);
		arp.addMapping("aptenon_task_relation", "r_id", TaskRelation.class);
		arp.addMapping("aptenon_work", "work_id", Work.class);
		arp.addMapping("aptenon_work_task_class", "class_id", WorkTaskClass.class);
		arp.addMapping("aptenon_work_task_label", "label_id", WorkTaskLabel.class);
		arp.addMapping("aptenon_work_task_log", "log_id", WorkTaskLog.class);
		arp.addMapping("aptenon_work_user", "id", WorkUser.class);
		arp.addMapping("aptenon_crm_business_change", "change_id", CrmBusinessChange.class);
		//注册、工单
		arp.addMapping("hr_register", "OID", HrRegister.class);
		arp.addMapping("hr_gongdan", "OID", HrGongdan.class);
		arp.addMapping("hr_gongdan_area_relation", "id", HrGongdanAreaRelation.class);
		arp.addMapping("hr_gongdan_book", "id", HrGongdanBook.class);
		arp.addMapping("hr_gongdan_fjf", "id", HrGongdanFjf.class);
		arp.addMapping("hr_gongdan_history", "id", HrGongdanHistory.class);
		arp.addMapping("hr_gongdan_history_wsf", "id", HrGongdanHistoryWsf.class);
		arp.addMapping("hr_gongdan_log", "id", HrGongdanLog.class);
		arp.addMapping("hr_gongdan_repair", "id", HrGongdanRepair.class);
		arp.addMapping("hr_gongdan_sab_log", "id", HrGongdanSabLog.class);
		arp.addMapping("hr_gongdan_wsf_log", "id", HrGongdanWsfLog.class);
		arp.addMapping("hr_gongdan_zmn_log", "id", HrGongdanZmnLog.class);
        arp.addMapping("sys_sftable", "No", SysSftable.class);
        arp.addMapping("sys_enummain", "No", SysEnummain.class);
		//活动
		arp.addMapping("port_activity", "id", PortActivity.class);
		arp.addMapping("port_activity_address", "id", PortActivityAddress.class);
		arp.addMapping("port_activity_assist", "id", PortActivityAssist.class);
		arp.addMapping("port_activity_emp", "No", PortActivityEmp.class);
		arp.addMapping("port_activity_helper", "id", PortActivityHelper.class);
		arp.addMapping("port_activity_playbill", "id", PortActivityPlaybill.class);
		arp.addMapping("port_activity_share", "id", PortActivityShare.class);
		arp.addMapping("port_dept", "No", PortDept.class);
		arp.addMapping("port_emp", "No", PortEmp.class);
		arp.addMapping("port_emp_relation", "OID", PortEmpRelation.class);
		arp.addMapping("port_activity_enroll", "id", PortActivityEnroll.class);
		//微信粉丝管理
		arp.addMapping("wxcms_account_agent", "id", WxcmsAccountAgent.class);
		arp.addMapping("wxcms_account_agent_qrcode", "id", WxcmsAccountAgentQrcode.class);
		arp.addMapping("wxcms_account_agent_shop", "id", WxcmsAccountAgentShop.class);
		// Composite Primary Key order: id,open_id
		arp.addMapping("wxcms_account_fans", "id,open_id", WxcmsAccountFans.class);
		arp.addMapping("wxcms_account_qrcode_fans", "id", WxcmsAccountQrcodeFans.class);
		arp.addMapping("wxcms_account_shop", "id", WxcmsAccountShop.class);
		arp.addMapping("wxcms_account_shop_qrcode", "id", WxcmsAccountShopQrcode.class);
		arp.addMapping("wxcms_account_team_qrcode", "id", WxcmsAccountTeamQrcode.class);
		arp.addMapping("wxcms_activity_coupon", "id", WxcmsActivityCoupon.class);
		arp.addMapping("wxcms_activity_coupon_record", "coupon_id,open_id", WxcmsActivityCouponRecord.class);

		arp.addMapping("wxcms_activity_enroll", "id", WxcmsActivityEnroll.class);
		arp.addMapping("wxcms_activity_lottery", "id", WxcmsActivityLottery.class);
		arp.addMapping("wxcms_flu_code", "id", WxcmsFluCode.class);
		//进销存
		arp.addMapping("jxc_order_delivery", "id", JxcOrderDelivery.class);
		arp.addMapping("jxc_order_delivery_code", "id", JxcOrderDeliveryCode.class);
		//旺店通
		arp.addMapping("hr_gongdan_wdt_trade", "trade_no", HrGongdanWdtTrade.class);
		arp.addMapping("hr_gongdan_wdt_trade_goods", "rec_id", HrGongdanWdtTradeGoods.class);
	}
}

