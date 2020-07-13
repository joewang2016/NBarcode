package com.el.ks.barcode.bean;

import com.el.ks.barcode.service.DataType;
import com.el.ks.barcode.service.Prefix;

import lombok.Data;

@Data
@Prefix(pre="AL")
public class F0116Bean {
	@DataType
	private String add1;
	@DataType
	private String add2;
	@DataType
	private String add3;
	@DataType
	private String add4;
	@DataType
	private String addz;
	@DataType
	private String cty1;
	@DataType
	private String coun;
	@DataType
	private String ctr;
}
