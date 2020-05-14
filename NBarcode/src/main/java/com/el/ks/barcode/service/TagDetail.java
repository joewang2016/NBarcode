package com.el.ks.barcode.service;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface TagDetail {
	String name() default "";

	boolean db() default true;

	String pos() default "L";

	int index() default 0;
}
