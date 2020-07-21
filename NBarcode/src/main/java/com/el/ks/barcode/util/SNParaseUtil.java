package com.el.ks.barcode.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.el.ks.barcode.bean.SNBean;
import com.el.ks.barcode.config.TagConfig;
import com.el.ks.barcode.service.Const;

@Component
public class SNParaseUtil {

	Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private TagConfig config;

	@Autowired
	private RedisUtil util;

	public SNBean ParseTag(String sn) {

		Pattern pattern = null;
		Matcher matcher = null;
		SNBean bean = new SNBean(sn);

		sn = sn.trim();
		sn = clean(sn);
		if (sn.indexOf("(240)") < 0)
			sn = sn.replaceFirst("240", "(240)");
		if (sn.indexOf("(11)") < 0)
			sn = sn + "(11)19700101";
		log.info(sn);
		if (util.hasKey(sn))
			return (SNBean) util.get(sn);

		String reg = "";
		if (config != null)
			reg = config.getReg();
		else
			reg = "\\d{0,16}[\\(]{0,}240[\\)]{0,}([\\w-*&]+)[\\(]{0,1}[\\(]{0,}([0-2,7]{0,2})[\\)]{0,}([\\w -_ ]*)[\\(]11[\\)](\\d{6,8})";
		pattern = Pattern.compile(reg);
		matcher = pattern.matcher(sn);
		int c = 0;
		if (matcher.matches()) {
			c = matcher.groupCount();
			switch (c) {
			case 1:
				bean.setLitm(matcher.group(1));
				break;
			case 3: {
				bean.setLitm(matcher.group(1));
				bean.setTag(matcher.group(2));
				String ts = matcher.group(3);
				switch (matcher.group(2)) {
				case "10":
					parse_10(bean, ts);
					break;
				case "21":
					bean.setLotn(matcher.group(3));
					bean.setLot1(matcher.group(3));
					break;
				case "17":
					parse_17(bean, ts);
				}

			}
			case 4: {
				bean.setLitm(matcher.group(1));
				bean.setTag(matcher.group(2));
				bean.setDate(matcher.group(4));
				String ts = matcher.group(3);
				switch (matcher.group(2)) {
				case "10":
					parse_10(bean, ts);
					break;
				case "21":
					bean.setLotn(matcher.group(3));
					bean.setLot1(matcher.group(3));
					break;
				case "17":
					parse_17(bean, ts);
				}
			}
			}
		}
		if (bean.getLot1().trim().equals("") && bean.getLotn().trim().equals(""))
			bean.setLot1(Const.MAX_LOT1);
		util.set(sn, bean, Const.EXPIRED);
		return bean;
	}

	private void parse_11(SNBean bean, String ts) {
		// TODO Auto-generated method stub
		bean.setDate(ts);
	}

	private void parse_17(SNBean bean, String ts) {
		// TODO Auto-generated method stub
		if (ts.compareTo("") > 0) {
			String reg = "[\\d]{6}([0-2]{0,2})([\\w- ]*)";
			Pattern pattern = null;
			Matcher matcher = null;
			pattern = Pattern.compile(reg);
			matcher = pattern.matcher(ts);
			if (matcher.matches()) {
				if (matcher.group(1).equals("10")) {
					bean.setLot1(matcher.group(2));
				} else {
					bean.setLotn(matcher.group(2));
					bean.setLot1(matcher.group(2));
				}
			}
		}
	}

	private void parse_10(SNBean bean, String ts) {
		// TODO Auto-generated method stub
		if (ts.compareTo("") > 0) {
			String reg = "[\\w- ]+[\\(]21([\\w- ]+)";
			Pattern pattern = null;
			Matcher matcher = null;
			pattern = Pattern.compile(reg);
			matcher = pattern.matcher(ts);
			if (matcher.matches()) {
				bean.setLotn(matcher.group(1));
				bean.setLot1(matcher.group(1));
			} else
				bean.setLot1(ts);
		}
	}

	private String clean(String arg) {
		String result = "";
		String s240 = "240";
		int index = arg.indexOf(s240) - 1;
		if (index >= 0) {
			for (int i = 0; i < index; i++) {
				char c = arg.charAt(i);
				if (c > '0' && c < '9')
					result += String.valueOf(c);
			}
			result += arg.substring(index);
		} else
			result = arg;

		log.info(result);
		return result;
	}

	public static void main(String[] args) {
		String s = "(240)11292ADU1(21)2255204(11)20190925";
		SNParaseUtil util = new SNParaseUtil();
		SNBean bean = util.ParseTag(s);
		System.out.println(bean.toString());
	}
}
