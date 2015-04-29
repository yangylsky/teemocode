package tk.teemocode.commons.util.lang;

import org.apache.commons.collections.map.MultiValueMap;
import org.testng.Assert;
import org.testng.annotations.Test;

import tk.teemocode.commons.util.lang.MoneyUtil;

public class MoneyUtilTest {
	@Test(enabled=false)
	public void testGetWeightedAverage1() {
		Double d1 = 150d;
		Double d2 = 149d;
		Long q1 = 0L;
		Long q2 = 1L;
		Double result = MoneyUtil.getWeightedAverage(d1, q1, d2, q2);
		Assert.assertEquals(149d, result);
	}

	@Test(enabled=false)
	public void testGetWeightedAverage2() {

	}

	@Test(enabled=true)
	public void testGetMultiValueMapWeightedAverage1() {
		MultiValueMap params = new MultiValueMap();
		params.put(150d, 4);
		params.put(125d, 3);
		params.put(150d, 3);
		params.put(140d, 8);
		params.put(150d, 9);
		params.put(140d, 6);
		params.put(110d, 2);
		params.put(150d, 5);
		Double result = MoneyUtil.getWeightedAverage(params);
		Assert.assertEquals(142.625, result);

		params.clear();
		params.put(150d, 4);
		params.put(125d, 3);
		params.put(150d, 3);
		params.put(140d, 0);
		params.put(150d, 9);
		params.put(140d, 6);
		params.put(110d, 2);
		params.put(150d, 5);
		result = MoneyUtil.getWeightedAverage(params);
		Assert.assertEquals(143.28125, result);
	}
}
