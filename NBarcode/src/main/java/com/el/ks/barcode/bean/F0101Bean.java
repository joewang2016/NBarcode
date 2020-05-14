package com.el.ks.barcode.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class F0101Bean {
	private BigDecimal an8;
	private String alph;

	private List<F0111Bean> f0111 = new ArrayList<F0111Bean>();
	private List<F0115Bean> f0115 = new ArrayList<F0115Bean>();
	private List<F01151Bean> f01151 = new ArrayList<F01151Bean>();
	private List<F0116Bean> f0116 = new ArrayList<F0116Bean>();

}
