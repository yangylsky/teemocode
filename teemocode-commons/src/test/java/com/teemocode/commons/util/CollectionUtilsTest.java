package com.teemocode.commons.util;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.ListUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

@SuppressWarnings("unchecked")
public class CollectionUtilsTest {
	@Test
	public void TestListUtilsSubtract() {
		List<Integer> list3 = ListUtils.subtract(getTestList2(), getTestList1());
		Assert.assertEquals(list3, Arrays.asList(new Integer[] {5, 6}));
	}

	@Test
	public void TestListUtilsIntersection() {
		List<Integer> list3 = ListUtils.intersection(getTestList2(), getTestList1());
		Assert.assertEquals(list3, Arrays.asList(new Integer[] {3, 4}));
	}

	private List<Integer> getTestList1() {
		return Arrays.asList(new Integer[] {1, 2, 3, 4});
	}

	private List<Integer> getTestList2() {
		return Arrays.asList(new Integer[] {3, 4, 5, 6});
	}
}
