package BP.Difference.Handler;

import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Inject;
import com.kakarote.crm9.common.service.HttpService;

import java.util.HashMap;

public class SFTableHandler_Controller extends com.jfinal.core.Controller{

    @Inject
    HttpService httpService;
	
	 public void Demo_HandlerEmps() throws Exception
    {
        BP.Port.Emps emps = new BP.Port.Emps();
        emps.RetrieveAll();
//         return emps.ToJson();
        renderJson(emps.ToJson());
    }

    public void productList() throws Exception
    {
        //获取商城的产品列表
        renderJson(httpService.gatewayRequestJson("http://ap:8201/weshop-admin/mobile/goods/listAfterSale?pageNum=1&pageSize=1000",
                JSON.toJSONString(new HashMap())));
    }
}
