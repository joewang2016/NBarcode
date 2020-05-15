package com.el.ks.barcode.bean;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class TagTemplateBean {
	private String label;
	private String value;
	private Integer index;
}
