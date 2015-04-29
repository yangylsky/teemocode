package tk.teemocode.commons.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;

@SuppressWarnings("deprecation")
public class DateUtil {
	private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);

	public static final String BUNDLE_KEY = "ApplicationResources";

	private static String defaultDatePattern = null;

	public static final int WeekSpan = 7;

	public static final int SCOND_ONE_DAY = 86400; //24*60*60*1000

	public static final int MILLISECOND_ONE_DAY = 86400000; //24*60*60*1000

	private DateUtil() {}

	/**
	 * 获取截去时，分，秒的当前日期
	 * @param days
	 * @return
	 */
	public static Date truncateToDay() {
		return truncateToDay(new Date());
	}

	/**
	 * 获取截去时，分，秒的指定日期
	 * @param days
	 * @return
	 */
	public static Date truncateToDay(Date date) {
		return truncateToDay(date, 0);
	}

	public static Date truncateToDay(int days) {
		return truncateToDay(new Date(), days);
	}

	/**
	 * 获取截去时，分，秒的指定增量日期
	 * @param days
	 * @return
	 */
	public static Date truncateToDay(Date date, int days) {
		Date theDate = DateUtils.addDays(date, days);
		return DateUtils.truncate(theDate, Calendar.DAY_OF_MONTH);
	}

	/**
	 * 判断一个年份是否是闰年
	 * @param year
	 * @return
	 */
	public static boolean isLeapYear(int year) {
		return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
	}

	/**
	 * 判断一个日期是否是闰年
	 * @param date
	 * @return
	 */
	public static boolean isLeapYear(Date date) {
		return isLeapYear(getYear(date));
	}

	/**
	 * 获取指定年月日的Date
	 * @param year
	 * @param month - 0~11
	 * @param date - 1~31
	 * @return
	 */
	public static Date getDate(int year, int month, int date) {
		return getDate(year, month, date, 0, 0, 0);
	}

	/**
	 * 获取指定年月日时分秒的Date
	 * @param year
	 * @param month - 0~11
	 * @param date - 1~31
	 * @param hourOfDay - 0~23
	 * @param minute - 0~59
	 * @param second - 0~59
	 * @return
	 */
	public static Date getDate(int year, int month, int date, int hourOfDay, int minute, int second) {
		return getDate(year, month, date, hourOfDay, minute, second, 0);
	}

	/**
	 * 获取指定年月日时分秒毫秒的Date
	 * @param year
	 * @param month - 0~11
	 * @param date - 1~31
	 * @param hourOfDay - 0~23
	 * @param minute - 0~59
	 * @param second - 0~59
	 * @param millisecond - 0~999
	 * @return
	 */
	public static Date getDate(int year, int month, int date, int hourOfDay, int minute, int second, int millisecond) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, date, hourOfDay, minute, second);
		cal.set(Calendar.MILLISECOND, millisecond);
		return cal.getTime();
	}

	public static int getYear(Date date) {
		return DateUtils.toCalendar(date).get(Calendar.YEAR);
	}

	public static int getMonth(Date date) {
		return DateUtils.toCalendar(date).get(Calendar.MONTH);
	}

	/**
	 * 返回指定时间段的星期序数,周1为1，周日为7
	 * @param date
	 * @return
	 */
	public static int getDayOfWeek(Date date) {
		if(DateUtils.toCalendar(date).get(Calendar.DAY_OF_WEEK) == 1) {
			return 7;
		} else {
			return DateUtils.toCalendar(date).get(Calendar.DAY_OF_WEEK) - 1;
		}
	}

	public static int getDayOfMonth(Date date) {
		return DateUtils.toCalendar(date).get(Calendar.DAY_OF_MONTH);
	}

	public static Date getFirstDayOfWeek() {
		return getFirstDayOfWeek(new Date());
	}

	public static Date getFirstDayOfWeek(Date date) {
		int dayOfWeek = DateUtils.toCalendar(date).get(Calendar.DAY_OF_WEEK);
		return DateUtils.addDays(date, -dayOfWeek);
	}

	public static Date getLastDayOfWeek() {
		return getLastDayOfWeek(new Date());
	}

	public static Date getLastDayOfWeek(Date date) {
		int dayOfWeek = DateUtils.toCalendar(date).get(Calendar.DAY_OF_WEEK);
		return DateUtils.addDays(date, WeekSpan - dayOfWeek);
	}

	public static Date getFirstDayOfMonth() {
		return getFirstDayOfMonth(new Date());
	}

	public static Date getFirstDayOfMonth(Date date) {
		return getDayOfMonth(date, 0);
	}

	public static Date getDayOfMonth(int offset) {
		return getDayOfMonth(new Date(), offset);
	}

	public static Date getDayOfMonth(Date date, int offset) {
		Calendar cal = DateUtils.toCalendar(DateUtils.truncate(DateUtils.addMonths(date, offset), Calendar.DAY_OF_MONTH));
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		return cal.getTime();
	}

	public static Date getLastDayOfMonth() {
		return getLastDayOfMonth(new Date());
	}

	public static Date getLastDayOfMonth(Date date) {
		Date tempDate = DateUtils.addMonths(truncateToDay(date), 1);
		tempDate = getFirstDayOfMonth(tempDate);
		return DateUtils.addMilliseconds(tempDate, -1);
	}

	public static Date getLastTimeOfDay(Date date) {
		Date tempDate = truncateToDay(date, 1);
		return DateUtils.addMilliseconds(tempDate, -1);
	}

	public static String getYear(Calendar cal) {
		return String.valueOf(cal.get(Calendar.YEAR));
	}

	public static String getMonth(Calendar cal) {
		return StringUtils.leftPad(String.valueOf(cal.get(Calendar.MONTH) + 1), 2, '0');
	}

	public static String getDay(Calendar cal) {
		return StringUtils.leftPad(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)), 2, '0');
	}

	public static String getHour(Calendar cal) {
		return StringUtils.leftPad(String.valueOf(cal.get(Calendar.HOUR_OF_DAY)), 2, '0');
	}

	public static String getMinute(Calendar cal) {
		return StringUtils.leftPad(String.valueOf(cal.get(Calendar.MINUTE)), 2, '0');
	}

	public static String getSecond(Calendar cal) {
		return StringUtils.leftPad(String.valueOf(cal.get(Calendar.SECOND)), 2, '0');
	}

	public static Date parseDate(String strDate) {
		return parseDate(strDate, new String[] {"yyyy-MM-dd", "MM/dd/yyyy"});
	}

	public static Date parseTime(String strDate) {
		return parseDate(strDate, new String[] {"yyyy-MM-dd HH:mm:ss", "MM/dd/yyyy HH:mm:ss"});
	}

	public static Date parseDate(String strDate, String... format) {
		try {
			if(StringUtils.isBlank(strDate)) {
				return null;
			}
			return DateUtils.parseDate(strDate, format);
		} catch(Exception e) {
			return null;
		}
	}

	public static String formatDate(Date date, String format) {
		if(date == null) {
			return "";
		}
		SimpleDateFormat simpleDateFORMat = new SimpleDateFormat(format);
		return simpleDateFORMat.format(date);
	}

	public static String formatDate(Date date) {
		return formatDate(date, "yyyy-MM-dd");
	}

	public static String formatTime(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * This method generates a string representation of a date/time in the format you specify on input
	 * @param aMask
	 *        the date pattern the string is in
	 * @param strDate
	 *        a string representation of a date
	 * @return a converted Date object
	 * @see java.text.SimpleDateFormat
	 * @throws ParseException
	 */
	public static final Date convertStringToDate(String aMask, String strDate) {
		SimpleDateFormat df = null;
		Date date = null;
		df = new SimpleDateFormat(aMask);
		try {
			date = df.parse(strDate);
		} catch(ParseException pe) {
			throw new IllegalArgumentException(pe.getMessage() + " " + pe.getErrorOffset());
		}

		return (date);
	}

	public static String getDatePattern() {
		Locale locale = LocaleContextHolder.getLocale();
		try {
			defaultDatePattern = ResourceBundle.getBundle(BUNDLE_KEY, locale).getString("date.format");
		} catch(MissingResourceException mse) {
			defaultDatePattern = "yyyy-MM-dd";
		}

		return defaultDatePattern;
	}

	public static String getDateTimePattern() {
		return DateUtil.getDatePattern() + " HH:mm:ss.S";
	}

	public static Date parseDate(Object obj) {
		Date check = null;
		if(obj != null && obj instanceof String) {
			String sa = (String) obj;
//			String[] parsePatterns = new String[] {
//					"yyyy-MM-dd'T'HH:mm:ss", //RFC 3339 date format (XW-473)
//					"yyyy-MM-dd",
//					"MM/dd/yyyy"};
//			return DateUtils.parseDate(sa, parsePatterns);
			Locale locale = Locale.getDefault();
			SimpleDateFormat d1 = (SimpleDateFormat) DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.LONG, locale);
			SimpleDateFormat d2 = (SimpleDateFormat) DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM, locale);
			SimpleDateFormat d3 = (SimpleDateFormat) DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, locale);
			SimpleDateFormat rfc3399 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			SimpleDateFormat d4 = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat d5 = new SimpleDateFormat("MM/dd/yyyy");
			SimpleDateFormat[] dfs = {d1, d2, d3, rfc3399, d4, d5}; //added RFC 3339 date format (XW-473)
			for(SimpleDateFormat df : dfs) {
				try {
					check = df.parse(sa);
					if(check != null) {
						return check;
					}
				} catch(ParseException ignore) {
				}
			}
		} else if(obj != null && obj instanceof Date) {
			return (Date) obj;
		}
		return null;
	}

	public static Date getRelativeDate(Date d, int days) {
		return DateUtils.addDays(d, days);
	}

	public static int getDaysBetween(Date date1, Date date2) {
		return getDaysBetween(date1, date2, true);
	}

	public static int getDaysBetween(Date date1, Date date2, boolean isTruncated) {
		if(date1 == null) {
			date1 = new Date();
		}
		if(date2 == null) {
			date2 = new Date();
		}
		if(isTruncated) {
			date1 = truncateToDay(date1);
			date2 = truncateToDay(date2);
		}

		return (int) ((date2.getTime()- date1.getTime()) / MILLISECOND_ONE_DAY);
	}

	/**
	 * 本月有几天
	 * @return
	 */
	public static int getDaysOfMonth(Date d) {
		Calendar calendar = DateUtils.toCalendar(d);
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 今年有几天
	 * @return
	 */
	public static long getDaysOfYear(Date d) {
		Calendar calendar = DateUtils.toCalendar(d);
		calendar.set(Calendar.DAY_OF_YEAR, 1);
		calendar.roll(Calendar.DAY_OF_YEAR, -1);
		return calendar.get(Calendar.DAY_OF_YEAR);
	}

	/**
	 * 是否过期
	 * @param d
	 * @return
	 */
	public static boolean isOverdue(Date d) {
		return d != null && d.after(new Date());
	}

	/**
	 * 指定日期加上当前的时、分、秒信息
	 * @param date
	 */
	public static void appendHMS(Date date) {
		mergeHMS(date, new Date());
	}

	public static void mergeHMS(Date date1, Date date2) {
		date1.setHours(date2.getHours());
		date1.setMinutes(date2.getMinutes());
		date1.setSeconds(date2.getSeconds());
	}

	/**
	 * 返回指定时间段内的整点天的日期
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static List<Date> getDays(Date date1, Date date2) {
		if(date1 == null || date2 == null) {
			throw new IllegalArgumentException("[DateUtil.getDays()]date1 or date2 can not be null!");
		}
		if(date1.after(date2)) {
			throw new IllegalArgumentException("[DateUtil.getDays()]date1 " + date1 + " can not be after date2 " + date2);
		}
		List<Date> days = new ArrayList<Date>();
		date2 = truncateToDay(date2);
		while(!date1.after(date2)) {
			date1 = truncateToDay(date1);
			days.add(date1);
			date1 = DateUtils.addDays(date1, 1);
		};
		return days;
	}

    /**
     * This method generates a string representation of a date's date/time
     * in the format you specify on input
     *
     * @param aMask the date pattern the string is in
     * @param aDate a date object
     * @return a formatted string representation of the date
     *
     * @see java.text.SimpleDateFormat
     */
	public static final String getDateTime(String mask, Date date) {
		SimpleDateFormat df = null;
		String returnValue = "";

		if(date == null) {
			logger.error("aDate is null!");
		} else {
			df = new SimpleDateFormat(mask);
			returnValue = df.format(date);
		}

		return (returnValue);
	}

	public static Date getNextDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + 1);
        return c.getTime();
    }

	public static boolean isSameYear(Date d1, Date d2) {
		return d1 != null && d2 != null && getYear(d1) == getYear(d2);
	}

	public static boolean isSameMonth(Date d1, Date d2) {
		return isSameYear(d1, d2) && getMonth(d1) == getMonth(d2);
	}

	public static boolean isSameDay(Date d1, Date d2) {
		return isSameMonth(d1, d2) && getDayOfMonth(d1) == getDayOfMonth(d2);
	}
}
