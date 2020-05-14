package com.el.ks.barcode.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateFormat extends SimpleDateFormat {
	/**
	 * 
	 * 
	 */
	private static final long serialVersionUID = -3069856269190275981L;

	public static SimpleDateFormat initFormat(String format) {
		SimpleDateFormat f = new SimpleDateFormat(format);
		f.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));//
		return f;
	}

	public static String CJulian(int julian) {
		int v_year;
		int v_day;
		String szDt = "";
		if (julian != 0) {
			v_year = julian / 1000;
			v_year = 1900 + v_year;
			v_day = julian % 1000;
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.YEAR, v_year);
			calendar.set(Calendar.DAY_OF_YEAR, v_day);
			szDt = StringTool.lpad(String.valueOf(calendar.get(Calendar.MONTH) + 1), "0", 2) + "/"
					+ StringTool.lpad(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)), "0", 2) + "/"
					+ String.valueOf(calendar.get(Calendar.YEAR));
			return szDt;
		} else {
			return null;
		}
	}

	public static int CJDEInt(String szDate) {
		Date x = null;
		int v_year = 0;
		int v_day = 0;

		try {
			if (szDate.trim().equals("") || szDate == null) {
				return 0;
			} else {
				szDate = szDate.replaceAll("/", "-");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				x = sdf.parse(szDate);
			}
			Calendar cal = Calendar.getInstance();
			cal.setTime(x);
			v_year = cal.get(Calendar.YEAR);
			v_day = cal.get(Calendar.DAY_OF_YEAR);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return (v_year - 1900) * 1000 + v_day;
	}

	public static int CJDETime() {
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		int second = c.get(Calendar.SECOND);
		String sTime = String.format("%02d%02d%02d", hour, minute, second);
		
		return Integer.valueOf(sTime);
	}

	public static void main(String[] args) {
		System.out.println(DateFormat.CJDETime());
	}
	
	public static int getJulianDate() {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String time = format.format(date);
		return DateFormat.CJDEInt(time);
	}
}
