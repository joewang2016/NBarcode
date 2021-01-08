package com.el.ks.barcode.bean;

import lombok.Data;

@Data
public class SNBean {
	private String sn = "";
	private String litm = "";
	private String lotn = "";
	private String lot1 = "";
	private String date = "";
	private String tag = "";

	private String from = "";
	private String to = "";
	private String gtin = "";
	private String lott = "";

	public SNBean(String sn) {
		this.sn = sn;
	}

	public SNBean() {

	}

	public String toChinaString() {
		StringBuffer buff = new StringBuffer();
		if (litm.compareTo(" ") > 0)
			buff.append("(240)").append(litm);
		if (lott.equals("21"))
			if (lotn.compareTo(" ") > 0)
				buff.append("(21)").append(lotn);

		if (lott.equals("10"))
			if (lot1.compareTo(" ") > 0)
				buff.append("(10)").append(lotn);

		if (from.compareTo(" ") > 0)
			buff.append("(11)").append(from);
		if (to.compareTo(" ") > 0)
			buff.append("(17)").append(to);
		return buff.toString();
	}
}
