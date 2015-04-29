package tk.teemocode.commons.util.lang;

import org.apache.commons.lang.StringUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import tk.teemocode.commons.util.lang.StringUtil;

public class StringUtilTest {
	@Test
	public void testTrim() {
		String s = "  dsad\r\n\t  ";
		s = StringUtils.trim(s);
		Assert.assertEquals(s, "dsad");
	}

	@Test
	public void testRemoveBlankElement() {
		String[] arr1 = new String[] {"aaa", ""};
		String[] arr2 = StringUtil.removeBlankElement(arr1);

		Assert.assertEquals(new String[] {"aaa"}, arr2);

		arr1 = new String[] {"aaa", null, "bb", "", null};
		arr2 = StringUtil.removeBlankElement(arr1);

		Assert.assertEquals(new String[] {"aaa", "bb"}, arr2);
	}

	@Test
	public void testParseLongs() {
		String s = "  45,\r\n23 |2|\t6,8 ";
		Long[] r = StringUtil.parseLongs(s);
		Assert.assertEquals(r, new Long[] {45L, 23L, 2L, 6L, 8L});
	}

	@Test
	public void testMatch() {
		Assert.assertTrue(StringUtil.match("aabbcc", "*cc"));
		Assert.assertTrue(StringUtil.match("aabbcc", "????cc"));
		Assert.assertTrue(StringUtil.match("aabb\\cc", "*\\cc"));
		Assert.assertTrue(StringUtil.match("aabb\\[cc", "*\\[cc"));
		Assert.assertTrue(StringUtil.match("aabb\\[]cc", "*\\[]cc"));
		Assert.assertTrue(StringUtil.match("aabb\\[](cc", "*\\[](cc"));
		Assert.assertTrue(StringUtil.match("aabb\\[]()cc", "*\\[]()cc"));
		Assert.assertTrue(StringUtil.match("aabb\\[]().cc", "*\\[]().cc"));
	}

	@Test
	public void testRetainAll() {
		Assert.assertEquals(StringUtil.retainAll("1,2,4,5,7,8", "1,4,5"), "1,4,5");
		Assert.assertEquals(StringUtil.retainAll("1,2,4,5,7,8", "3,4,5,6"), "4,5");
		Assert.assertEquals(StringUtil.retainAll("1,2,4,5,7,8", "9,10,16"), "");
		Assert.assertEquals(StringUtil.retainAll("1,2,4,5,7,8", "1,2,4,5,7,8,9,10"), "1,2,4,5,7,8");
	}

	@Test
	public void testPolishNosString() {
		Assert.assertEquals("|N01|N02|N03|", StringUtil.polishNosString("|N01|N02|N03|"));
		Assert.assertEquals("|N01|N02|N03|", StringUtil.polishNosString("N01|N02|N03|"));
		Assert.assertEquals("|N01|N02|N03|", StringUtil.polishNosString("|N01|N02|N03"));
		Assert.assertEquals("|N01|N02|N03|", StringUtil.polishNosString("N01|N02|N03"));
		Assert.assertEquals("|N01|N02|N03|", StringUtil.polishNosString("|N01,N02;N03|"));
		Assert.assertEquals("|N01|N02|N03|", StringUtil.polishNosString("N01,N02;N03"));
		Assert.assertEquals("|N01|N02|N03|", StringUtil.polishNosString("|N01，N02；N03|"));
		Assert.assertEquals("|N01|N02|N03|", StringUtil.polishNosString("N01，N02；N03"));
	}
}
