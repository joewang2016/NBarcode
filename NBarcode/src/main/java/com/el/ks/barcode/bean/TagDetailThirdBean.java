package com.el.ks.barcode.bean;

import com.el.ks.barcode.service.TagDetail;

import lombok.Data;

@Data
public class TagDetailThirdBean {

	@TagDetail(name = "产品名称: ", index = 0)
	private String rhalph;
	@TagDetail(name = "订货号: ", index = 1)
	private String rdlitm;
	@TagDetail(name = "规格型号: ", index = 2)
	private String rdaitm;
	@TagDetail(name = "产品描述: ", index = 3)
	private String rdagen;
	@TagDetail(name = "销售商名称: ", index = 4)
	private String rdadd1;
	@TagDetail(name = "销售商联系方式: ", index = 5)
	private String rdadd2;
	@TagDetail(name = "销售商住所: ", index = 6)
	private String rdcmddsc05;
}
