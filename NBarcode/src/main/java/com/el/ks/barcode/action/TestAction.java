package com.el.ks.barcode.action;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class TestAction {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File file = new File("/Volumes/SSK/LOCAL PACKAGE/jdedebug_3516_052920.log");
		String filepath = "/Volumes/SSK/LOCAL PACKAGE/";
		File fto = null;
		try {
			Long rows = 1L;
			BufferedReader br = new BufferedReader(new FileReader(file));
			BufferedWriter bw = null;
			String str = "";
			Boolean begin = true;
			while ((str = br.readLine()) != null) {
				if ((rows % 3840998L) == 1) {
					if (!begin) {
						bw.flush();
						bw.close();
					}
					String f = filepath + "%d.log";
					f = String.format(f, rows);
					fto = new File(f);
					bw = new BufferedWriter(new FileWriter(fto));
					if (begin) {
						begin = false;
					}
				}
				bw.write(str);
				bw.write("\r\n");
				rows++;
			}
			bw.flush();
			bw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Integer NextRow(Integer top) {
		top += 10;
		return top;
	}

}
