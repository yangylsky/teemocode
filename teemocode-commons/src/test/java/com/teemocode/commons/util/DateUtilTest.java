package com.teemocode.commons.util;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.teemocode.commons.util.DateUtil;

public class DateUtilTest {
	@Test
	public void testTruncateToDay() {
		Date d = DateUtil.parseTime("2008-12-16 12:05:44");
		Date d1 = DateUtil.parseDate("2008-12-16");
		Date d2 = DateUtil.parseDate("2008-12-13");
		Date d3 = DateUtil.parseDate("2008-12-23");

		Assert.assertEquals(d1, DateUtil.truncateToDay(d));
		Assert.assertEquals(d2, DateUtil.truncateToDay(d, -3));
		Assert.assertEquals(d3, DateUtil.truncateToDay(d, 7));
	}

	@Test
	public void testIsLeapYearInt() {
	}

	@Test
	public void testIsLeapYearDate() {
	}

	@Test
	public void testGetYear() {
		Date d1 = DateUtil.getDate(2009, 2, 25);
		Date d2 = DateUtil.getDate(2011, 11, 30);

		Assert.assertEquals(2009, DateUtil.getYear(d1));
		Assert.assertEquals(2011, DateUtil.getYear(d2));
	}

	@Test
	public void testGetMonth() {
		Date d1 = DateUtil.getDate(2009, 2, 25);
		Date d2 = DateUtil.getDate(2011, 11, 30);

		Assert.assertEquals(2, DateUtil.getMonth(d1));
		Assert.assertEquals(11, DateUtil.getMonth(d2));
	}

	@Test
	public void testGetDayOfMonth() {
		Date d1 = DateUtil.getDate(2009, 2, 25);
		Date d2 = DateUtil.getDate(2011, 11, 30);

		Assert.assertEquals(25, DateUtil.getDayOfMonth(d1));
		Assert.assertEquals(30, DateUtil.getDayOfMonth(d2));
	}

	@Test
	public void testGetLastDayOfMonth() {
		Date d1 = DateUtil.getDate(2009, 1, 25);
		Date d2 = DateUtil.getDate(2011, 11, 30);
		Assert.assertEquals(DateUtil.getDate(2009, 1, 28, 23, 59, 59, 999), DateUtil.getLastDayOfMonth(d1));
		Assert.assertEquals(DateUtil.getDate(2011, 11, 31, 23, 59, 59, 999), DateUtil.getLastDayOfMonth(d2));
	}

	@Test
	public void testParseDateObject() {
		String strDate = "2008-12-02";
		Assert.assertEquals(strDate, DateUtil.formatDate(DateUtil.parseDate(strDate)));
		Assert.assertEquals(strDate, DateUtil.formatDate(DateUtil.parseDate("12/02/2008")));
	}

	@Test
	public void testGetDaysBetween() {
		Date d1 = DateUtil.getDate(2008, 12, 1, 12, 5, 44);
		Date d2 = DateUtil.getDate(2008, 12, 21, 5, 25, 32);
		Assert.assertEquals(20, DateUtil.getDaysBetween(d1, d2));
		Assert.assertEquals(19, DateUtil.getDaysBetween(d1, d2, false));

		d1 = DateUtil.getDate(2008, 12, 11, 12, 5, 44);
		d2 = DateUtil.getDate(2009, 1, 4, 11, 35, 32);
		Assert.assertEquals(24, DateUtil.getDaysBetween(d1, d2));
		Assert.assertEquals(23, DateUtil.getDaysBetween(d1, d2, false));
	}

	@Test
	public void testGetDays() {
		Date d1 = DateUtil.getDate(2008, 12, 27, 12, 5, 44);
		Date d2 = DateUtil.getDate(2009, 1, 2, 5, 25, 32);

		List<Date> days1 = Arrays.asList(new Date[] {DateUtil.getDate(2008, 12, 27), DateUtil.getDate(2008, 12, 28),
				DateUtil.getDate(2008, 12, 29), DateUtil.getDate(2008, 12, 30), DateUtil.getDate(2008, 12, 31),
				DateUtil.getDate(2009, 1, 1), DateUtil.getDate(2009, 1, 2)});
		Assert.assertEquals(days1, DateUtil.getDays(d1, d2));
	}

	@Test
	public void testGetDaysOfThisYear() {
		Date d1 = DateUtil.getDate(2008, 11, 27, 12, 5, 44);
		Date d2 = DateUtil.getDate(2009, 0, 2, 5, 25, 32);
		Date d3 = DateUtil.getDate(1900, 0, 2, 5, 25, 32);

		Assert.assertEquals(366, DateUtil.getDaysOfYear(d1));
		Assert.assertEquals(365, DateUtil.getDaysOfYear(d2));
		Assert.assertEquals(365, DateUtil.getDaysOfYear(d3));
	}
}
