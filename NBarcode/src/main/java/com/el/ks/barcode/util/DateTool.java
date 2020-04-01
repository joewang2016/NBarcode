package com.el.ks.barcode.util;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.context.i18n.LocaleContextHolder;

public class DateTool {

	private static Logger log = Logger.getLogger(DateTool.class);

	private static String defaultDatePattern = null;

	public static final String DATABASE_DATE_TIME_MASK = "YYYY-MM-DD HH24:MI:SS";

	public static final String DATE_MASK = "yyyy-MM-dd";

	public static final String DATE_TIME_MASK = "yyyy-MM-dd HH:mm:ss";

	public static final String TIME_PATTERN_MASK_1 = "HH:mm:ss";

	public static final String TIME_PATTERN_MASK_2 = "HH:mm";

	public static final String TIME_PATTERN_MASK_3 = "yyyy-MM";

	public static final Pattern DATE_PATTERN_1 = Pattern.compile("\\d{4}-\\d{2}-\\d{2}$");

	public static final Pattern DATE_PATTERN_2 = Pattern.compile("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$");

	public static final Pattern TIME_PATTERN_1 = Pattern.compile("\\d{2}:\\d{2}:\\d{2}$");

	public static final Pattern TIME_PATTERN_2 = Pattern.compile("\\d{2}:\\d{2}$");

	public static final int DT_LASTDAY_OF_MONTH = 1; // 当前月的朄1�7后一处1�7

	public static final int DT_TODAY_IN_LAST_MONTH = 2; // 上一个月的当处1�7

	public static final int DT_FIRSTDAY_OF_MONTH = 3; // 当前月的第一处1�7

	static {
		System.setProperty("user.timezone", "Asia/Shanghai");

	}

	public static synchronized String getDatePattern() {
		Locale locale = LocaleContextHolder.getLocale();
		try {
			defaultDatePattern = "MM/dd/yyyy";
		} catch (MissingResourceException mse) {
			defaultDatePattern = "yyyy-dd-MM";
		}
		return defaultDatePattern;
	}

	/**
	 * This method attempts to convert an Oracle-formatted date in the form
	 * dd-MMM-yyyy to mm/dd/yyyy.
	 * 
	 * @param aDate
	 *            date from database as a string
	 * @return formatted string for the ui
	 */
	public static final String getDate(Date aDate) {
		SimpleDateFormat df = null;
		String returnValue = "";

		if (aDate != null) {
			df = new SimpleDateFormat(getDatePattern());
			returnValue = df.format(aDate);
		}

		return (returnValue);
	}

	/**
	 * This method generates a string representation of a date/time in the
	 * format you specify on input
	 * 
	 * @param aMask
	 *            the date pattern the string is in
	 * @param strDate
	 *            a string representation of a date
	 * @return a converted Date object
	 * @see java.text.SimpleDateFormat
	 * @throws ParseException
	 */
	public static final Date convertStringToDate(String aMask, String strDate) throws ParseException {
		SimpleDateFormat df = null;
		Date date = null;
		df = new SimpleDateFormat(aMask);

		if (log.isDebugEnabled()) {
			log.debug("converting '" + strDate + "' to date with mask '" + aMask + "'");
		}

		try {
			date = df.parse(strDate);
		} catch (ParseException pe) {
			// log.error("ParseException: " + pe);
			throw new ParseException(pe.getMessage(), pe.getErrorOffset());
		}

		return (date);
	}

	/**
	 * This method returns the current date time in the format: MM/dd/yyyy HH:MM
	 * a
	 * 
	 * @param theTime
	 *            the current time
	 * @return the current date/time
	 */
	public static String getTimeNow(Date theTime) {
		return getDateTime(DateTool.DATE_TIME_MASK, theTime);
	}

	/**
	 * This method returns the current date in the format: MM/dd/yyyy
	 * 
	 * @return the current date
	 * @throws ParseException
	 */
	public static Calendar getToday() throws ParseException {
		Date today = new Date();
		SimpleDateFormat df = new SimpleDateFormat(getDatePattern());

		// This seems like quite a hack (date -> string -> date),
		// but it works ;-)
		String todayAsString = df.format(today);
		Calendar cal = new GregorianCalendar();
		cal.setTime(convertStringToDate(todayAsString));

		return cal;
	}

	/**
	 * This method generates a string representation of a date's date/time in
	 * the format you specify on input
	 * 
	 * @param aMask
	 *            the date pattern the string is in
	 * @param aDate
	 *            a date object
	 * @return a formatted string representation of the date
	 * 
	 * @see java.text.SimpleDateFormat
	 */
	public static final String getDateTime(String aMask, Date aDate) {
		SimpleDateFormat df = null;
		String returnValue = "";

		if (aDate == null) {
			log.error("aDate is null!");
		} else {
			df = new SimpleDateFormat(aMask);
			returnValue = df.format(aDate);
		}

		return (returnValue);
	}

	/**
	 * 解析日期字符串至日期类型内容
	 * 
	 * @param date
	 *            日期字符丄1�7
	 * @param format
	 *            与日期字符串格式匹配的格弄1�7
	 * @return 解析后返回的日期
	 */
	public static java.util.Date parseDate(String date, String format) {
		try {
			SimpleDateFormat formatter;
			if (null == format)
				throw new IllegalArgumentException("错误的日期格式");
			formatter = new SimpleDateFormat(format);
			ParsePosition pos = new ParsePosition(0);
			return formatter.parse(date, pos);
		} catch (Exception e) {
			throw new IllegalArgumentException("错误的日期格式" + date + " " + e);
		}
	}

	/**
	 * This method generates a string representation of a date based on the
	 * System Property 'dateFormat' in the format you specify on input
	 * 
	 * @param aDate
	 *            A date to convert
	 * @return a string representation of the date
	 */
	public static final String convertDateToString(Date aDate) {
		return getDateTime(getDatePattern(), aDate);
	}

	/**
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static final String convertDataToString(Date date, String pattern) {
		return getDateTime(pattern, date);
	}

	/**
	 * This method converts a String to a date using the datePattern
	 * 
	 * @param strDate
	 *            the date to convert (in format MM/dd/yyyy)
	 * @return a date object
	 * 
	 * @throws ParseException
	 */
	public static Date convertStringToDate(String strDate) throws ParseException {
		Date aDate = null;
		try {
			if (log.isDebugEnabled()) {
				log.debug("converting date with pattern: " + getDatePattern());
			}
			aDate = convertStringToDate(getDatePattern(), strDate);
		} catch (ParseException pe) {
			log.error("Could not convert '" + strDate + "' to a date, throwing exception");
			pe.printStackTrace();
			throw new ParseException(pe.getMessage(), pe.getErrorOffset());

		}
		return aDate;
	}

	/**
	 * 解析日期字符串至日期类型内容,返回java.sql.Date日期类型数据内容
	 * 
	 * @param date
	 *            日期字符丄1�7
	 * @param format
	 *            与日期字符串格式匹配的格弄1�7
	 * @return 解析后返回的日期
	 */
	public static java.sql.Date parseSqlDate(String date, String format) {
		try {
			SimpleDateFormat formatter;
			if (null == format)
				throw new IllegalArgumentException("错误的日期格弄1�7");
			formatter = new SimpleDateFormat(format);
			ParsePosition pos = new ParsePosition(0);
			java.util.Date utilDate = formatter.parse(date, pos);
			return new java.sql.Date(utilDate.getTime());
		} catch (Exception e) {
			throw new IllegalArgumentException("错误的日朄1�7" + date + " " + e);
		}
	}

	/**
	 * 获取日期格式
	 * 
	 * @param date
	 *            日期字符丄1�7
	 * @return 日期格式
	 * @throws Exception
	 *             非法参数或不支持格式错误信息
	 */
	public static String getDatePattern(String date) throws Exception {
		if (date == null || "".equals(date)) {
			throw new java.lang.IllegalArgumentException("非法日期参数，无法解析日朄1�7");
		}
		if (DateTool.DATE_PATTERN_1.matcher(date).find()) {
			return "yyyy-MM-dd";
		} else if (DATE_PATTERN_2.matcher(date).find()) {
			return "yyyy-MM-dd HH:mm:ss";
		} else if (TIME_PATTERN_1.matcher(date).find()) {
			return "HH:mm:ss";
		} else if (TIME_PATTERN_2.matcher(date).find()) {
			return "HH:mm";
		} else {
			throw new Exception("未知日期格式，无法解析日朄1�7");
		}
	}

	/**
	 * 获取系统时间yyyy-MM-dd HH:mm:ss
	 */
	public static String getSystemDateTime() {
		return DateTool.convertDataToString(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 获取系统时间yyyy-MM-dd
	 */
	public static String getSystemDate() {
		return DateTool.convertDataToString(new Date(), "yyyy-MM-dd");
	}

	/**
	 * 获取系统年份
	 * 
	 * @return
	 */
	public static String getSystemYear() {
		return DateTool.convertDataToString(new Date(), "yyyy");
	}

	public static String getSystemYear(String szDate) throws ParseException {
		return DateTool.convertDataToString(convertStringToDate("yyyy-MM-dd", szDate), "yyyy");
	}

	/**
	 * 截取日期,可以截取到年，月，日，时，分，秒
	 * 
	 * @param date
	 * @param truncType
	 *            ：Calendar.YEAR＄1�7 Calendar.YEAR＄1�7 Calendar.DAY_OF_MONTH＄1�7
	 *            Calendar.HOUR_OF_DAY＄1�7 Calendar.MINUTE＄1�7 Calendar.SECOND
	 * @return
	 * @author ykbao
	 * @since 2010-1-22
	 */
	public static Date trunc(Date date, int truncType) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int year, month, day, hour, minute, second;
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		hour = calendar.get(Calendar.HOUR_OF_DAY);
		minute = calendar.get(Calendar.MINUTE);
		second = calendar.get(Calendar.SECOND);

		switch (truncType) {
		case Calendar.YEAR:
			month = Calendar.JANUARY;
		case Calendar.MONTH:
			day = 1;
		case Calendar.DAY_OF_MONTH:
			hour = 0;
		case Calendar.HOUR_OF_DAY:
			minute = 0;
		case Calendar.MINUTE:
			second = 0;
		case Calendar.SECOND:
		default:
		}
		calendar.set(year, month, day, hour, minute, second);
		return calendar.getTime();
	}

	/**
	 * 加减日期,可以加减年，月，日，时，分，秒�1�7�1�7
	 * 
	 * @param date
	 * @param field
	 *            ：Calendar.YEAR＄1�7 Calendar.YEAR＄1�7 Calendar.DAY_OF_MONTH＄1�7
	 *            Calendar.HOUR_OF_DAY＄1�7 Calendar.MINUTE＄1�7 Calendar.SECOND
	 * @param value
	 *            : 要加减的值，可以为负
	 * @return
	 * @author ykbao
	 * @since 2010-1-22
	 */
	public static Date add(Date date, int field, int value) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(field, calendar.get(field) + value);
		return calendar.getTime();
	}

	/**
	 * 取当前日朄1�7(处1�7)的当前周的第丄1�7天和朄1�7后一天日朄1�7
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static String[] getWeek(String date, String format) {
		String[] result = new String[2];
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parseDate(date, format));
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek == 1) {
			calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 7);
		}
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - dayOfWeek + 2);
		result[0] = getDateTime(format, calendar.getTime());
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 6);
		result[1] = getDateTime(format, calendar.getTime());

		return result;
	}

	/**
	 * 取当前日朄1�7(处1�7)的当前月的第丄1�7天和朄1�7后一天日朄1�7
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static String[] getMonth(String date, String format) {
		String[] result = new String[2];
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parseDate(date, format));
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		result[0] = getDateTime(format, calendar.getTime());
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 1);
		result[1] = getDateTime(format, calendar.getTime());

		return result;
	}

	/**
	 * 取当前月份第丄1�7天日期和朄1�7后一天日朄1�7 (yyyy-MM-dd)
	 * 
	 * @return String
	 */
	public static String[] getMothByDay(String date) {
		String[] result = new String[2];
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parseDate(date, "yyyy-MM-dd"));
		Calendar cpcalendar = (Calendar) calendar.clone();
		cpcalendar.set(Calendar.DAY_OF_MONTH, 1);
		result[0] = df.format(new Date(cpcalendar.getTimeInMillis()));
		cpcalendar.add(Calendar.MONTH, 1);
		cpcalendar.add(Calendar.DATE, -1);
		result[1] = df.format(new Date(cpcalendar.getTimeInMillis()));
		return result;
	}

	/**
	 * 取当前月份第丄1�7天日期和朄1�7后一天日朄1�7 (yyyy-MM-dd)
	 * 
	 * @return String
	 */
	public static String[] getMothByDay(String date, String pattern) {
		String[] result = new String[2];
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parseDate(date, pattern));
		Calendar cpcalendar = (Calendar) calendar.clone();
		cpcalendar.set(Calendar.DAY_OF_MONTH, 1);
		result[0] = df.format(new Date(cpcalendar.getTimeInMillis()));
		cpcalendar.add(Calendar.MONTH, 1);
		cpcalendar.add(Calendar.DATE, -1);
		result[1] = df.format(new Date(cpcalendar.getTimeInMillis()));
		return result;
	}

	public static String getTimePoint(int point) {
		long unit = 3600000;
		Calendar calendar = new GregorianCalendar(2000, 0, 1, 0, 0, 0);
		Date date = new Date(calendar.getTimeInMillis() + unit * point);

		return getDateTime(TIME_PATTERN_MASK_2, date);
	}

	public static String getWeekPoint(int point) {
		String result = null;

		switch (point) {
		case 0:
			result = "周一";
			break;
		case 1:
			result = "周二";
			break;
		case 2:
			result = "周三";
			break;
		case 3:
			result = "周四";
			break;
		case 4:
			result = "周五";
			break;
		case 5:
			result = "周六";
			break;
		case 6:
			result = "周日";
			break;
		}

		return result;
	}

	public static String getWeekPoint(Date date) {
		String result = null;

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

		switch (dayOfWeek) {
		case 1:
			result = "周日";
			break;
		case 2:
			result = "周一";
			break;
		case 3:
			result = "周二";
			break;
		case 4:
			result = "周三";
			break;
		case 5:
			result = "周四";
			break;
		case 6:
			result = "周五";
			break;
		case 7:
			result = "周六";
			break;
		}

		return result;
	}

	public static int getDays4Month(String[] date, String format) {
		int result = 0;
		for (String day : date) {
			if (day != null && !day.equals("") && !day.equalsIgnoreCase("null")) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(parseDate(day, format));
				calendar.set(Calendar.DAY_OF_MONTH, 1);
				calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
				calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 1);

				int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

				result = dayOfMonth > result ? dayOfMonth : result;
			}
		}

		return result;
	}

	public static String getMonthPoint(Date date) {
		String result = null;

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		result = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));

		return result;
	}

	public static String getLastWeek(String date, String format) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parseDate(date, format));
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 7);

		return getDateTime(format, calendar.getTime());
	}

	public static String getLastDay(String date, String format) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parseDate(date, format));
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 1);

		return getDateTime(format, calendar.getTime());
	}

	/**
	 * 获取特定日期的格式化日期，以当前日期为基凄1�7
	 * 
	 * @param mode
	 *            int
	 *            指定的格式：DT_LASTDAY_OF_MONTH，DT_TODAY_IN_LAST_MONTH，DT_FIRSTDAY_OF_MONTH
	 * @return String
	 */
	public static String formatDate(int mode) {
		String retStr = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		switch (mode) {
		case DateTool.DT_LASTDAY_OF_MONTH:
			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
			break;
		case DateTool.DT_TODAY_IN_LAST_MONTH:
			cal.add(Calendar.MONTH, -1);
			break;
		case DateTool.DT_FIRSTDAY_OF_MONTH:
			cal.set(Calendar.DAY_OF_MONTH, 1);
			break;
		default:
			break;
		}
		retStr = sdf.format(cal.getTime());
		return retStr;
	}

	/**
	 * @see 是否闰年
	 */
	public static boolean isLeapYear(int year) {
		if ((year % 4 == 0) && ((year % 100 != 0) | (year % 400 == 0))) {
			return true;
		} else {
			return false;
		}
	}

	public static Date getYesterday() {
		Date currentTime = new Date();
		return DateTool.getDateAdd(currentTime, Calendar.DATE, -1);
	}

	public static Date getTomorrow() {
		Date currentTime = new Date();
		return DateTool.getDateAdd(currentTime, Calendar.DATE, 1);
	}

	/**
	 * 
	 * @param date
	 * @param i
	 * @see 获取date前i天的日期
	 * @return
	 */
	public static Date getPreviousDay(Date date, int i) {
		return DateTool.getDateAdd(date, Calendar.DATE, (-1) * i);
	}

	/**
	 * 
	 * @param date
	 * @param i
	 * @see 获取date后i天的日期
	 * @return
	 */
	public static Date getNextDay(Date date, int i) {
		return DateTool.getDateAdd(date, Calendar.DATE, i);
	}

	public static String getFormatDate(String format, Date date) {
		String dateString = "";
		if (date != null) {
			SimpleDateFormat formatter = DateFormat.initFormat(format);
			dateString = formatter.format(date);
		}
		return dateString;
	}

	public static Date getDate(String date) throws ParseException {
		SimpleDateFormat formatter = DateFormat.initFormat("yyyy-MM-dd");
		return formatter.parse(date);
	}

	public static Date getDateTime(String date) throws ParseException {
		Date date1;

		try {
			date1 = DateFormat.initFormat("yyyy-MM-dd HH:mm:ss:SSS").parse(date);
		} catch (Exception e) {
			try {
				date1 = DateFormat.initFormat("yyyy-MM-dd HH:mm").parse(date);
			} catch (ParseException e1) {
				try {
					date1 = DateFormat.initFormat("yyyy-MM-dd").parse(date);
				} catch (ParseException e2) {
					date1 = DateFormat.initFormat("yyyy MM dd").parse(date);
				}
			}
		}
		return date1;
	}

	public static Date getDateTime(String date, String format) throws ParseException {
		return (DateFormat.initFormat(format)).parse(date);
	}

	public static Date getTime(String time) throws ParseException {
		return (DateFormat.initFormat("HH:mm:ss")).parse(time);
	}

	public static int getAge(Date birthDay) throws Exception {
		Calendar cal = Calendar.getInstance();

		if (cal.before(birthDay)) {
			throw new IllegalArgumentException("The birthDay is before Now.It's unbelievable!");
		}

		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH);
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
		cal.setTime(birthDay);

		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH);
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

		int age = yearNow - yearBirth;

		if (monthNow <= monthBirth) {
			if (monthNow == monthBirth) {
				if (dayOfMonthNow < dayOfMonthBirth) {
					age--;
				} else {
					// do nothing
				}
			} else {
				age--;
			}
		} else {
		}

		return age;
	}

	/**
	 * 
	 * @param date1
	 * @param calendarEx
	 *            like:Calendar.DATE
	 * @param time
	 * @return
	 */
	public static Date getDateAdd(Date date1, int calendarEx, int time) {
		Date endday = date1;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(endday);
		calendar.add(calendarEx, time);
		return calendar.getTime();

	}

	/**
	 * @see 获取某年月的扄1�7有日朄1�7
	 * @param year
	 * @param month
	 * @return List<Date>
	 */
	public static List getDateList(String year, String month) {
		List datelist = new ArrayList();
		String bigMonth[] = { "01", "03", "05", "07", "08", "10", "12" };
		String smallMonth[] = { "04", "06", "09", "11" };
		Date tempdate;
		try {
			// 大月
			if (StringTool.isInArray(month, bigMonth)) {
				for (int i = 1; i <= 31; i++) {
					tempdate = DateTool.getDate(year + "-" + month + "-" + i);
					datelist.add(tempdate);
				}
			}
			// 小月
			else if (StringTool.isInArray(month, smallMonth)) {
				for (int i = 1; i <= 30; i++) {
					tempdate = DateTool.getDate(year + "-" + month + "-" + i);
					datelist.add(tempdate);
				}
			}
			// 闰年2朄1�7
			else if (DateTool.isLeapYear(Integer.parseInt(year))) {
				for (int i = 1; i <= 29; i++) {
					tempdate = DateTool.getDate(year + "-" + month + "-" + i);
					datelist.add(tempdate);
				}
			}
			// 平年2朄1�7
			else {
				for (int i = 1; i <= 28; i++) {
					tempdate = DateTool.getDate(year + "-" + month + "-" + i);
					datelist.add(tempdate);
				}
			}

		} catch (Exception e) {
			// LogTool.debug(DateTool.class, e);
		}

		return datelist;

	}

	public static String getPeriod(String szDate) throws ParseException {
		return DateTool.convertDataToString(convertStringToDate("yyyy-MM-dd", szDate), "MM");
	}

	public static void main(String[] args) throws Exception {
		System.out.println(DateTool.parseDate("2017-7-12", "yyyy-MM-dd"));
	}
}
