/**
 * 
 */
package BP.Difference;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.druid.DruidPlugin;

/**
 * JFinal工具类
 * @author brycehan
 *
 */
public class JFinalUtils {

	private static Prop prop;
	private static DruidPlugin druidPlugin;
	
	/**
	 * 加载配置文件
	 */
	public static void loadConfig() {
		if(prop == null) {
			prop = PropKit.useFirstFound("jflow.properties");
		}
	}
	
	/**
	 * 获取Druid插件
	 * @return
	 */
	public static DruidPlugin getDruidPlugin() {
		loadConfig();
		
		if(druidPlugin == null) {
			druidPlugin = new DruidPlugin(prop.get("AppCenterDSN"), prop.get("JflowUser"), prop.get("JflowPassword"));
		}
		return druidPlugin;
	}
}
