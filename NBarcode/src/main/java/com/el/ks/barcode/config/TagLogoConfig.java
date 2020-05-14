package com.el.ks.barcode.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "tag.logo")
public class TagLogoConfig {
	private String topmargin;
	private String leftmargin;
	private String fontheight;
	private String fontwidth;
}
