package com.el.ks.barcode.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.el.ks.barcode.bean.SNBean;
import com.el.ks.barcode.config.TagConfig;

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

		String reg = "";

		sn = sn.trim();
		sn = clean(sn);
		if (sn.indexOf("(240)") < 0)
			sn = sn.replaceFirst("240", "(240)");
		// if (sn.indexOf("(11)") < 0)
		// sn = sn + "(11)19700101";
		log.info(sn);
		String key = StringUtils.replace(sn, "(", "");
		key = StringUtils.replace(key, ")", "");
		if (util.hasKey(key))
			return (SNBean) util.get(key);

		reg = "([\\(]{0,1}01[\\)]{0,1})(\\d{14})";
		pattern = Pattern.compile(reg);
		matcher = pattern.matcher(sn);
		int c = 0;
		if (matcher.matches()) {
			bean.setGtin(matcher.group(1));
		}

		reg = "([\\(]{1}240[\\)]{0,1})(\\w+)([\\(]{0,1}[\\w-\\(-\\)]*)";
		pattern = Pattern.compile(reg);
		matcher = pattern.matcher(sn);
		if (matcher.matches()) {
			bean.setLitm(matcher.group(2));
		}

		reg = ".*([\\(]{1}10[\\)]{0,1})([\\w-_]+)([\\(]{0,1}[\\w-\\(-\\)]*)";
		pattern = Pattern.compile(reg);
		matcher = pattern.matcher(sn);
		if (matcher.matches()) {
			bean.setLotn(matcher.group(2));
			bean.setLot1("9999");
			bean.setLott("10");
		}

		reg = ".*([\\(]{1}21[\\)]{0,1})([\\w-_]+)([\\(]{0,1}[\\w-\\(-\\)]*)";
		pattern = Pattern.compile(reg);
		matcher = pattern.matcher(sn);
		if (matcher.matches()) {
			bean.setLotn(matcher.group(2));
			bean.setLot1(matcher.group(2));
			bean.setLott("21");
		}

		reg = ".*([\\(]{1}11[\\)]{0,1})(\\d{8})([\\(]{0,1}[\\w-\\(-\\)]*)";
		pattern = Pattern.compile(reg);
		matcher = pattern.matcher(sn);
		if (matcher.matches()) {
			bean.setFrom(matcher.group(2));
			bean.setDate(matcher.group(2));
		}

		reg = ".*([\\(]{1}17[\\)]{0,1})(\\d{8})([\\(]{0,1}[\\w-\\(-\\)]*)";
		pattern = Pattern.compile(reg);
		matcher = pattern.matcher(sn);
		if (matcher.matches()) {
			bean.setTo(matcher.group(2));
		}
		
		
		log.info(bean);
		return bean;
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
