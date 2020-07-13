package com.el.ks.barcode.bean;

import java.math.BigDecimal;

import com.el.ks.barcode.service.DataType;
import com.el.ks.barcode.service.Prefix;

import lombok.Data;

@Data
@Prefix(pre="WW")
public class F0111Bean {
	@DataType(type = "Decimal")
	private BigDecimal idln;
	@DataType
	private String mlnm;
}
