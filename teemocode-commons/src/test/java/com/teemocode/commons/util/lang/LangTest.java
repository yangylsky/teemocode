package com.teemocode.commons.util.lang;

import org.testng.Assert;
import org.testng.annotations.Test;

public class LangTest {
	@Test(enabled=true)
	public void test1() {
		Integer i1 = Integer.valueOf(3);
		Assert.assertTrue(i1 == 3);
		Integer i2 = new Integer(3);
		Assert.assertFalse(i1 == i2);
		Assert.assertTrue(i2 == 3);
	}
}
