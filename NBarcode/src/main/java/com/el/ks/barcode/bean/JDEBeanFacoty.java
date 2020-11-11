package com.el.ks.barcode.bean;

import com.el.ks.barcode.service.IJDEBeanFactory;
import com.el.ks.barcode.util.StringTool;

public class JDEBeanFacoty<T> implements IJDEBeanFactory {

	private static JDEBeanFacoty factory;

	public static JDEBeanFacoty getInstance() {
		if (factory == null)
			factory = new JDEBeanFacoty();
		return factory;
	}

	@Override
	public T CreateJDE(String name) {
		// TODO Auto-generated method stub
		String clazzName = "com.el.ks.barcode.bean." + StringTool.up1stLetter(name) + "Bean";
		try {
			Class clazz = Class.forName(clazzName);
			T obj = (T) clazz.newInstance();
			return obj;
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public static void main(String[] args) {
		JDEBeanFacoty<F0111Bean> factory = JDEBeanFacoty.getInstance();
		F0111Bean bean = factory.CreateJDE("F0111");
		System.out.println(bean);
	}

}
