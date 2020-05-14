package com.el.ks.barcode.action;

public class TestAction {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TestAction action = new TestAction();
		Integer top = 12;
		for (int i = 0; i < 10; i++) {
			System.out.println(top);
			top = action.NextRow(action.NextRow(top));
		}
	}

	private Integer NextRow(Integer top) {
		top += 10;
		return top;
	}

}
