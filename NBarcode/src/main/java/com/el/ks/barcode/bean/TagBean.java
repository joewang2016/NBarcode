package com.el.ks.barcode.bean;

import com.el.ks.barcode.util.RedisUtil;
import com.el.ks.barcode.util.SNParaseUtil;

import lombok.Data;

@Data
public class TagBean {
	private String dl01;
	private String vr01;
	private String date;
	private String pages;
	private String countnb;
	private String printer;
	private String message;
	private SNBean entity;
	private String license;
	private RedisUtil util;

	public TagBean(RedisUtil util, String message) {
		this.message = message;
		this.util = util;
		SNParaseUtil service = new SNParaseUtil();
		service.setUtil(util);
		entity = service.ParseTag(message);
	}

	public TagBean() {

	}

	public void ParseMessage() {
		if (message != null && util != null) {
			SNParaseUtil service = new SNParaseUtil();
			service.setUtil(util);
			entity = service.ParseTag(message);
		}
	}
}
