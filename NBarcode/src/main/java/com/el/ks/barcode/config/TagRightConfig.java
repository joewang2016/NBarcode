package com.el.ks.barcode.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "tag.right")
public class TagRightConfig implements Cloneable {
	private String chars;
	private String topmargin;
	private String leftmargin;
	private String rows;
	private String width;
	private String litm;
	private String spc_fontwidth;
	private String spc_fontheight;
	private String spc_vpadding;
	private String spc_chars;

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
