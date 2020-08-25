package com.kakarote.crm9.erp.yeyx.service;

import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.yeyx.entity.HrGongdanAreaRelation;
import com.kakarote.crm9.erp.yeyx.entity.vo.HrGongdanAreaRelationRequest;

public class HrGongDanAreaRelationService {
    /**
     * @author tmr
     * 分页工单查询记录
     */
    public Page<Record> queryPageList(BasePageRequest basePageRequest) {
        //查询条件
        String province = basePageRequest.getJsonObject().getString("province");
        String city = basePageRequest.getJsonObject().getString("city");
        String district = basePageRequest.getJsonObject().getString("district");
        Kv kv = Kv.by("province",province).set("city",city).set("district",district);

        return Db.paginate(
                basePageRequest.getPage(),
                basePageRequest.getLimit(),
                Db.getSqlPara("admin.hrGongDanAreaRelation.queryPageList",kv)
        );
    }

    public int save(HrGongdanAreaRelationRequest hrGongdanAreaRelationRequest){
        int flag=0;
        String [] districts = hrGongdanAreaRelationRequest.getDistricts().split(",");
        for(int i=0;i<districts.length;i++){
            HrGongdanAreaRelation hrGongdanAreaRelation = new HrGongdanAreaRelation();
            hrGongdanAreaRelation.setProvince(districts[i].substring(0,2)+"0000");
            hrGongdanAreaRelation.setCity(districts[i].substring(0,4)+"00");
            hrGongdanAreaRelation.setDistrict(districts[i]);
            hrGongdanAreaRelation.setFkSystem(hrGongdanAreaRelationRequest.getFk_system());
            hrGongdanAreaRelation.setPriority(hrGongdanAreaRelationRequest.getPriority());
            hrGongdanAreaRelation.setValidFlag(hrGongdanAreaRelationRequest.getValid_flag());
            String [] fk_emps = hrGongdanAreaRelationRequest.getFk_emps().split(",");
            for(int j=0;j<fk_emps.length;j++){
                hrGongdanAreaRelation.setFkEmp(fk_emps[j]);
                if(hrGongdanAreaRelation.save()==true){
                    flag=flag+1;
                }
            }

        }
        return flag;
    }
}
