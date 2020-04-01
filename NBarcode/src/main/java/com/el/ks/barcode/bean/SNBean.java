package com.el.ks.barcode.bean;

import lombok.Data;

@Data
public class SNBean {
	private String sn = "";
	private String litm = "";
	private String lotn = "";
	private String lot1 = "";
	private String tag = "";
	
	public SNBean(String sn) {
		this.sn = sn;
	}
	
	public SNBean(){
		
	}
}
