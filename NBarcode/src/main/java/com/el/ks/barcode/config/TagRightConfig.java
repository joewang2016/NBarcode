package com.el.ks.barcode.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "tag.right")
public class TagRightConfig {
	private String chars;
	private String topmargin;
	private String leftmargin;
	private String rows;
	private String width;
}
