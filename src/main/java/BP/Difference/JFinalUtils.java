/**
 * 
 */
package BP.Difference;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.druid.DruidPlugin;
import lombok.Data;

import java.io.IOException;
import java.util.Properties;

/**
 * JFinal工具类
 * @author brycehan
 *
 */
@Data
public class JFinalUtils {

	private static Prop prop;
	private static Properties properties;
	private static DruidPlugin druidPlugin;

	/**
	 * 加载配置文件
	 */
	public static Prop loadConfig() {
		if(prop == null) {
			prop = PropKit.useFirstFound("jflow.properties");
		}
		return prop;
	}

	/**
	 * @return
	 * @throws IOException
	 */
	public static Properties loadResource() throws IOException {
		loadConfig();
		return prop.getProperties();
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
	/**
	 * 获取Druid插件
	 * @return
	 */
	public static DruidPlugin getDruidPlugin2() {
		loadConfig();
		if(druidPlugin == null) {
			druidPlugin = new DruidPlugin(prop.get("mysql2.AppCenterDSN"), prop.get("mysql2.JflowUser"), prop.get("mysql2.JflowPassword"));
		}
		return druidPlugin;
	}
}
