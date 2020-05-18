package com.el.ks.barcode.bean;

import com.el.ks.barcode.service.TagDetail;

import lombok.Data;

@Data
public class TagDetailThirdBean {

	@TagDetail(name = "产品名称: ", index = 0)
	private String rdagen;
	@TagDetail(name = "型号: ", index = 1)
	private String rdlitm;
	@TagDetail(name = "生产日期: ", index = 2, db = false)
	private String date;
	@TagDetail(name = "销售商名称: ", index = 3)
	private String rdadd1;
	@TagDetail(name = "销售商联系方式: ", db = false, index = 4)
	private String rdadd2;
	@TagDetail(name = "销售商住所: ", index = 5)
	private String rddmddsc05;
}