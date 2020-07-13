package com.el.ks.barcode.bean;

import org.springframework.stereotype.Component;

import com.el.ks.barcode.util.RedisUtil;
import com.el.ks.barcode.util.SNParaseUtil;

import lombok.Data;

@Component
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
	private String licence[];
	private String doco;

}
