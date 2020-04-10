package BP.Difference.Handler;

public class SFTableHandler_Controller extends com.jfinal.core.Controller{
	
	
	 public void Demo_HandlerEmps() throws Exception
     {
         BP.Port.Emps emps = new BP.Port.Emps();
         emps.RetrieveAll();
//         return emps.ToJson();
         renderJson(emps.ToJson());
     }

}
