package com.el.ks.barcode.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "tag.empty")
public class TagEmptyConfig {
	private String vpadding;
	private String chars;
	private String fontwidth;
	private String fontheight;
	private String topmargin;
	private String leftmargin;
	private String width;
}
