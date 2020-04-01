package com.el.ks.barcode.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.el.ks.barcode.bean.SNBean;
import com.el.ks.barcode.config.BarcodeConfig;
import com.el.ks.barcode.service.Const;

import lombok.Data;

@Data
public class SNParaseUtil {

	Logger log = Logger.getLogger(this.getClass());

	private BarcodeConfig config;
	private RedisUtil util;

	public SNBean ParseTag(String sn) {

		Pattern pattern = null;
		Matcher matcher = null;
		SNBean bean = new SNBean(sn);

		sn = sn.trim();
		sn = clean(sn);
		if (sn.indexOf("(240)") < 0)
			sn = sn.replaceFirst("240", "(240)");
		log.info(sn);
		if (util.hasKey(sn))
			return (SNBean) util.get(sn);

		String reg = "";
		if (config != null)
			reg = config.getReg();
		else
			reg = "\\d{0,16}[\\(]{0,}240[\\)]{0,}([\\w-*&]+)[\\(]{0,1}[\\(]{0,}([0-2,7]{0,2})[\\)]{0,}([\\w -_ ]*)";

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
			}
		}
		if (bean.getLot1().trim().equals("") && bean.getLotn().trim().equals(""))
			bean.setLot1("9999");
		util.set(sn, bean, Const.EXPIRED);
		return bean;
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
}