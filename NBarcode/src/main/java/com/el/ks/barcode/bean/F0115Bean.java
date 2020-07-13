package com.el.ks.barcode.bean;

import java.math.BigDecimal;

import com.el.ks.barcode.service.DataType;
import com.el.ks.barcode.service.Prefix;

import lombok.Data;

@Data
@Prefix(pre="WP")
public class F0115Bean {
	@DataType(type = "Decimal")
	private BigDecimal idln;
	@DataType(type = "Decimal")
	private BigDecimal rck7;
	@DataType
	private String phtp;
	@DataType
	private String ar1;
	@DataType
	private String ph1;
}
