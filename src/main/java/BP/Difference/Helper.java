package BP.Difference;

import java.io.IOException;
import java.util.Properties;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;

/**
 * 工具类
 * @author brycehan
 *
 */
public class Helper {

	/**
	 * @return
	 * @throws IOException
	 */
	public static Properties loadResource() throws IOException {
		Prop prop = null;
		if(prop == null) {
			prop = PropKit.use("jflow.properties", "UTF-8");
		}

		return prop.getProperties();
	}
}
