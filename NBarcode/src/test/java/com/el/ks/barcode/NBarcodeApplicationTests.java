package com.el.ks.barcode;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class NBarcodeApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Test
	public void testString() {
		String value = "aaaa";
		System.out.println(StringUtils.capitalize(value));
	}
}
