package tk.teemocode.commons.component.json;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.testng.annotations.Test;

public class JSONUtilTest {
	@Test(enabled = true)
	public void testToJsonMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("aaa", "1111111111");
		map.put("bbb", 2222);
		JSONObject jsobj = new JSONObject(map);
		String strJson = jsobj.toString();
		System.out.println("json: " + strJson);
	}
}
