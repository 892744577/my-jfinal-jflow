package BP.Difference.Handler;

import com.jfinal.core.Controller;
/**
 * 
 * @author brycehan
 *
 */
public class IndexController extends Controller {

	public void index() {
		render("/index.jsp");
	}
}
