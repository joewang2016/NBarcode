package com.el.ks.barcode.bean;

import com.el.ks.barcode.service.TagDetail;

import lombok.Data;

@Data
public class TagScanResultBean {
	@TagDetail
	private String RHLOTN;
	@TagDetail
	private String RHALPH;
	@TagDetail
	private String RDAITM;
	@TagDetail
	private String RDLITM;
	@TagDetail
	private String RDADD4;
	@TagDetail
	private String RHEFFT;
	@TagDetail
	private String RDSRP6;
}
