package com.el.ks.barcode.bean;

import com.el.ks.barcode.service.TagDetail;

import lombok.Data;

@Data
public class TagDetailBean {
	@TagDetail(name = "产品名称: ")
	private String rhalph = ""; // 产品名称

	@TagDetail(name = "规格型号: ")
	private String rdaitm = ""; // 规格型号

	@TagDetail(name = "产品描述: ")
	private String rdagen = ""; // 产品描述

	@TagDetail(name = "医疗器械注册证编号: ")
	private String rhlotn = ""; // 医疗器械注册证编号

	@TagDetail(name = "生产日期: ", db = false)
	private String date = ""; // 生产日期

	@TagDetail(name = "使用期限: ")
	private String rdcmddsc01 = ""; // 使用期限

	@TagDetail(name = "注册人/生产企业名称: ")
	private String rdadd3 = ""; // 注册人/生产企业名称

	@TagDetail(name = "注册人/生产企业住所: ")
	private String rdk74adl1 = ""; // 注册人/生产企业住所

	@TagDetail(name = "注册人/生产企业联系方式: ")
	private String rdcmddsc06 = ""; // 注册人/生产企业联系方式

	@TagDetail(name = "生产地址: ")
	private String rdk74adl2 = ""; // 生产地址

	@TagDetail(name = "代理人名称: ")
	private String rdadd1 = ""; // 代理人名称

	@TagDetail(name = "代理人联系方式: ")
	private String rdadd2 = ""; // 代理人联系方式

	@TagDetail(name = "代理人住所: ")
	private String rdcmddsc05 = ""; // 代理人住所
	@TagDetail(db = true)
	private String imsrp1 = "";// fieldCode

	@TagDetail(name = "电源连接条件: ")
	private String rdadd6 = "";// 电源连接条件

	@TagDetail(name = "输入功率: ")
	private String rddsc1 = "";// 输入功率

	@TagDetail(name = "防护等级: ")
	private String rdcmddsc = "";// 防护等级

	@TagDetail(name = "防电击程度: ")
	private String rddsc2 = "";// 防电击程度

	@TagDetail(name = "防电击类型: ")
	private String rddsc3 = "";// 防电击类型

	@TagDetail(name = "订货号: ")
	private String rdlitm = "";// 订货号

	@TagDetail(name = "其他内容详见说明书", db = false)
	private String content = "";// 其他内容详见说明书
}
