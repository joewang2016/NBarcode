package com.el.ks.barcode.util;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StringTool {
	public static boolean isInArray(String str, String[] strArray) {
		boolean result = false;
		for (int i = 0; i < strArray.length; i++) {
			if (strArray[i].equals("")) {
				result = false;
			} else if (str.indexOf(strArray[i].trim()) != -1) {
				result = true;
				break;
			}
		}
		return result;
	}

	public static boolean isNull(String str) {
		String string = str;
		if (str == null)
			string = "";
		if (string.equals("")) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isNotNull(String str) {
		return !isNull(str);
	}

	// 大写首字毄1�7
	public static String up1stLetter(String word) {
		char[] chars = word.toCharArray();
		if (chars[0] >= 'a' && chars[0] < 'z') {
			chars[0] = (char) (chars[0] - 32);
		}
		String up1srWord = new String(chars);
		return up1srWord;
	}

	// 小写首字毄1�7
	public static String lower1stLetter(String word) {
		char[] chars = word.toCharArray();
		if (chars[0] >= 'A' && chars[0] < 'Z') {
			chars[0] = (char) (chars[0] + 32);
		}
		String up1srWord = new String(chars);
		return up1srWord;
	}

	public static String toString(Object inObj, String dataFormat) {
		String obj = null;
		if (inObj != null) {
			if (inObj.getClass().equals(Date.class)
					|| inObj.getClass().equals(Timestamp.class)) {
				obj = DateTool.getFormatDate(dataFormat, (Timestamp) inObj);
			} else {
				obj = inObj.toString();
			}
		} else {
			obj = "";
		}
		return obj;
	}

	/**
	 * 将输入的字串左补0到指定位敄1�7
	 * 
	 * @param String
	 *            str 源字丄1�7
	 * @param int totalSize 补齐后的位数
	 * @return boolean
	 */
	public static String addZero(String str, int totalSize) {
		if (str == null)
			str = "";
		int length = str.length();
		if (totalSize == length) {
			return str;
		}
		if (totalSize < length) {
			return str.substring(length - totalSize, length - 1);
		}
		for (int i = 0; i < totalSize - length; i++) {
			str = "0" + str;
		}
		return str;
	}

	/**
	 * 返回字符串的副本，忽略前寄1�7'0'〄1�7
	 * 
	 * @param String
	 *            str 源字丄1�7
	 * @return str
	 */
	public static String trimLeadingZero(String str) {
		if (str == null || str.length() == 0)
			return str;

		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(0) == '0') {
				str = str.substring(1);
			} else {
				break;
			}
		}
		return str;
	}

	public static String nullToEmpty(Object sourceString) {
		if (sourceString == null) {
			return "";
		}
		return sourceString.toString();
	}

	public static Object StringToObj(String s, Class clazz) {
		Object obj = null;
		if (s.equals("NULL")) {
			obj = null;
		} else if (clazz.equals(Integer.class)) {
			obj = Integer.parseInt(s);
		} else if (clazz.equals(int.class)) {
			obj = Integer.parseInt(s);
		} else if (clazz.equals(Long.class)) {
			obj = Long.parseLong(s);
		} else if (clazz.equals(long.class)) {
			obj = Long.parseLong(s);
		} else if (clazz.equals(Double.class)) {
			obj = Double.parseDouble(s);
		} else if (clazz.equals(double.class)) {
			obj = Double.parseDouble(s);
		} else if (clazz.equals(Date.class)) {
			try {
				obj = DateTool.getDateTime(s, "yyyy-MM-dd HH:mm:ss:SSS");
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else if (clazz.equals(String.class)) {
			obj = s;
		} else {
			// 不支持类垄1�7
			// LogTool.inf(StringTool.class,"不支持类垄1�7"+clazz.getName());
		}
		return obj;
	}

	public static String ObjToString(Object obj) {
		String str = null;
		if (obj == null) {
			str = "";
		} else {
			Class clazz = obj.getClass();
			if (clazz.equals(Integer.class)) {
				str = obj.toString();
			} else if (clazz.equals(int.class)) {
				str = obj + "";
			} else if (clazz.equals(Long.class)) {
				str = obj.toString();
			} else if (clazz.equals(long.class)) {
				str = obj + "";
			} else if (clazz.equals(Double.class)) {
				str = obj.toString();
			} else if (clazz.equals(double.class)) {
				str = obj + "";
			} else if (clazz.equals(Date.class)) {
				str = DateTool.getFormatDate("yyyy-MM-dd HH:mm:ss:SSS",
						(Date) obj);

			} else if (clazz.equals(String.class)) {
				str = obj + "";

			} else if (clazz.getSimpleName().endsWith("Exception")) {

				// str= LogTool.getExceptionStr((Exception)obj);

			} else {
				// 不支持类垄1�7
				// LogTool.inf(StringTool.class,"不支持类垄1�7"+clazz.getName());
				str = "";
			}
		}
		return str;
	}

	/**
	 * 按大写字母分割字符串
	 * 
	 * @return
	 */
	public static List<String> splitByUpLetter(String str) {
		List<String> list = new ArrayList();
		char[] c = str.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < c.length; i++) {
			if (Character.isUpperCase(c[i])) {
				list.add(sb.toString());
				sb = new StringBuffer();
			}
			sb.append(c[i]);
			if (i == c.length - 1) {
				list.add(sb.toString());
			}
		}
		return list;
	}

	public static void main(String[] args) {
		String data = "aaa";
		System.out.println(data.indexOf("b"));
	}

	public static String lpad(String arg1, String pre, int len) {
		if (arg1 == null)
			return " ";
		int l = arg1.length();
		if (l > len)
			return arg1;
		else {
			int d = len - l;
			StringBuffer t = new StringBuffer();
			for (int i = d; i > 0; i--)
				t.append(pre);
			t.append(arg1);
			return t.toString();
		}
	}

	public static String rtrim(String arg1) {
		if (arg1.endsWith(" ")) {
			int length = arg1.length();
			char[] data = arg1.toCharArray();
			int lastNotSpace = 0;
			for (int i = length - 1; i >= 0; i--) {
				if (data[i] != ' ') {
					lastNotSpace = i;
					break;
				}
			}
			return arg1.substring(0, lastNotSpace + 1);
		}
		return arg1;
	}

	public static String[] stringToArray(String data, String split) {
		if (data.trim().equals("") || data == null)
			return null;
		return data.split("\\" + split);
	}

	public static String NumberToString(int num) {
		BigDecimal bd = new BigDecimal(num);
		return bd.toPlainString();
	}
	
	public static String ArrayDiffer(ArrayList<String> arr) {
		String result = "";
		int iresult = 0;
		if (arr.size() > 1) {
			iresult = Integer.parseInt(arr.get(0));
			for (int i = 1; i < arr.size(); i++) {
				iresult -= Integer.parseInt(arr.get(i));
			}
			result = String.valueOf(iresult);
		} else
			result = arr.get(0);
		return result;
	}
}
