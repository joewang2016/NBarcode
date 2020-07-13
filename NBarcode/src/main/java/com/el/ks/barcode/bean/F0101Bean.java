package com.el.ks.barcode.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.el.ks.barcode.service.DataType;
import com.el.ks.barcode.service.Prefix;

import lombok.Data;

@Data
@Prefix(pre="AB")
public class F0101Bean {
	@DataType(type = "Decimal")
	private BigDecimal an8;
	@DataType
	private String alph;
	@DataType
	private String at1;

	private List<F0111Bean> f0111 = new ArrayList<F0111Bean>();
	private List<F0115Bean> f0115 = new ArrayList<F0115Bean>();
	private List<F01151Bean> f01151 = new ArrayList<F01151Bean>();
	private List<F0116Bean> f0116 = new ArrayList<F0116Bean>();

}
