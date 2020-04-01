package com.el.ks.barcode;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableAutoConfiguration
@RestController
@EnableCaching
public class NBarcodeApplication extends SpringBootServletInitializer{
	
	@Autowired
	JdbcTemplate template;

	Logger log = Logger.getLogger(this.getClass());
	
	public static void main(String[] args) {
		SpringApplication.run(NBarcodeApplication.class, args);
	}
	
	protected SpringApplicationBuilder config(SpringApplicationBuilder applicationBuilder){
        return applicationBuilder.sources(this.getClass());
    }
	
}
