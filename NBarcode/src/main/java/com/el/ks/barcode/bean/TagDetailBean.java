package com.el.ks.barcode.bean;

import com.el.ks.barcode.service.TagDetail;

import lombok.Data;

@Data
public class TagDetailBean {
	@TagDetail(name = "产品名称: ", index = 0)
	private String rhalph = ""; // 产品名称

	@TagDetail(name = "规格型号: ", index = 1)
	private String rdaitm = ""; // 规格型号

	@TagDetail(name = "产品描述: ", index = 2)
	private String rdagen = ""; // 产品描述

	@TagDetail(name = "医疗器械注册证编号: ", index = 3)
	private String rhlotn = ""; // 医疗器械注册证编号

	@TagDetail(name = "生产日期: ", db = false, index = 4)
	private String date = ""; // 生产日期

	@TagDetail(name = "使用期限: ", index = 5)
	private String rdcmddsc01 = ""; // 使用期限

	@TagDetail(name = "注册人/生产企业名称: ", index = 6)
	private String rdadd3 = ""; // 注册人/生产企业名称

	@TagDetail(name = "注册人/生产企业住所: ", index = 7)
	private String rdk74adl1 = ""; // 注册人/生产企业住所

	@TagDetail(name = "注册人/生产企业联系方式: ", index = 8)
	private String rdcmddsc06 = ""; // 注册人/生产企业联系方式

	@TagDetail(name = "生产地址: ", index = 9)
	private String rdk74adl2 = ""; // 生产地址

	@TagDetail(name = "代理人名称: ", index = 10)
	private String rdadd1 = ""; // 代理人名称

	@TagDetail(name = "代理人联系方式: ", index = 11)
	private String rdadd2 = ""; // 代理人联系方式

	@TagDetail(name = "代理人住所: ", index = 12)
	private String rdcmddsc05 = ""; // 代理人住所

	@TagDetail(db = true, index = 13)
	private String imsrp1 = "";// fieldCode

	@TagDetail(name = "其他内容详见说明书", db = false, index = 14)
	private String content = "";// 其他内容详见说明书

	@TagDetail(name = "电源连接条件: ", pos = "R", index = 15)
	private String rdadd6 = "";// 电源连接条件

	@TagDetail(name = "输入功率: ", pos = "R", index = 16)
	private String rddsc1 = "";// 输入功率

	@TagDetail(name = "防护等级: ", pos = "R", index = 17)
	private String rdcmddsc = "";// 防护等级

	@TagDetail(name = "防电击程度: ", pos = "R", index = 18)
	private String rddsc2 = "";// 防电击程度

	@TagDetail(name = "防电击类型: ", pos = "R", index = 19)
	private String rddsc3 = "";// 防电击类型

	@TagDetail(name = "订货号: ", pos = "R", index = 20)
	private String rdlitm = "";// 订货号

	@TagDetail(name = "运行模式:运行10s, 停止30s", db = false, pos = "R", index = 21)
	private String add0 = "";// 
	
	@TagDetail(name = "单极切割:最大400W,500Ω,最大输出频率320KHz", db = false, pos = "R", index = 22)
	private String add1 = "";// 

	@TagDetail(name = "单极凝血:最大250W,500Ω,最大输出频率380KHz", db = false, pos = "R", index = 23)
	private String add2 = "";// 

	@TagDetail(name = "双极切割:最大400W,75Ω,最大输出频率350KHz", db = false, pos = "R", index = 24)
	private String add3 = "";// 

	@TagDetail(name = "双极凝血:最大350W,25Ω,最大输出频率380KHz", db = false, pos = "R", index = 25)
	private String add4 = "";// 

}
