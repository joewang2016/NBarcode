package com.el.ks.barcode.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "tag")
public class TagConfig implements Cloneable {
	private String reg;
	private String fontwidth;
	private String fontheight;
	private String vpadding;
	private String workspace;
	private String qtheight;

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
