package com.teemocode.commons.util.reflect;

import java.util.Arrays;
import java.util.Collection;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.teemocode.commons.util.reflect.PojoUtil;

public class PojoUtilTest {
	@Test(enabled = false)
	public void testMaptoObj() {
		//		fail("Not yet implemented");
	}

	@Test(enabled = false)
	public void testObjtoMapObjectMapOfObjectObject() {
		//		fail("Not yet implemented");
	}

	@Test(enabled = false)
	public void testObjtoMapObjectMapOfObjectObjectBoolean() {
		//		fail("Not yet implemented");
	}

	@Test(enabled = false)
	public void testMaptoMap() {
		//		fail("Not yet implemented");
	}

	@Test(enabled = false)
	public void testValueToObj() {
		//		fail("Not yet implemented");
	}

	@Test(enabled = false)
	public void testToArray() {
		String[] array = PojoUtil.toArray(PojoUtil.list("4","5","6"), String.class);
		Assert.assertTrue(Arrays.equals(array, new String[]{"4","5","6"}));
	}

	@Test(enabled = true)
	public void testMerge() {
		Assert.assertNull(PojoUtil.merge(new String[]{"1","2"}, null));

		Assert.assertTrue(Arrays.equals(
				(String[]) PojoUtil.merge(null, new String[] {"1", "2", "3"}),
				new String[]{"1","2","3"}));

		Assert.assertNull(PojoUtil.merge(PojoUtil.list("1","2"), null));

		Assert.assertTrue(PojoUtil.equals(
				(Collection<?>) PojoUtil.merge(null, PojoUtil.list("1","2")),
				PojoUtil.list("1","2"))	);

		Assert.assertTrue(Arrays.equals(
				PojoUtil.merge(new String[]{"1","2"}, new String[]{"1","2","3"}),
				new String[]{"1","2","3"})	);

		Assert.assertTrue(Arrays.equals(
				PojoUtil.merge(new String[]{"1","2"}, new String[]{"4","5","6"}),
				new String[]{"4","5","6"})	);

		Assert.assertTrue(Arrays.equals(
				PojoUtil.merge(new String[]{"1","2"}, new String[]{"1","2"}),
				new String[]{"1","2"})	);

		Assert.assertTrue(PojoUtil.equals(
				PojoUtil.merge(PojoUtil.list("1","2"), PojoUtil.list("1","2","3")),
				PojoUtil.list("1","2","3"))	);

		Assert.assertTrue(PojoUtil.equals(
				PojoUtil.merge(
						PojoUtil.list("1","2"), PojoUtil.list("4","5","6")),
						PojoUtil.list("4","5","6"))	);

		Assert.assertTrue(PojoUtil.equals(
				PojoUtil.merge(
						PojoUtil.list("1","2"), PojoUtil.list("1","2")),
						PojoUtil.list("1","2"))	);
	}
}
