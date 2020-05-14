package com.el.ks.barcode.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import com.el.ks.barcode.bean.TagBean;

public class BarcodeCommon {

	public Float LineLength(String arg) {
		float rowLen = 0;
		for (int i = 0; i < arg.length(); i++) {
			char c = arg.charAt(i);
			if (c >= 0X20 && c <= 0X40 || c >= 0X5B && c <= 0X7E) {
				rowLen += 0.5f;
			} else if (c > 0X40 && c < 0X5B) {
				rowLen += 0.7f;
			} else {
				rowLen += 1;
			}
		}
		return rowLen;
	}

	public void ReplaceSpecialCode(String arg) {
		if (arg.indexOf('~') > 0) {
			arg = arg.replace("~", "_7E");
		}
	}

	public void GenerateSource(String str, File f) {
		File fTemp = null;
		try {
			fTemp = File.createTempFile("tmp", ".txt", f);
			FileOutputStream fos = new FileOutputStream(fTemp);
			OutputStreamWriter osw = new OutputStreamWriter(fos, "utf-8");
			osw.write(str);
			osw.flush();
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getChineseTagSN(TagBean bean) {
		String dc = (240) + bean.getEntity().getLitm();
		if (bean.getEntity().getLotn().trim().equals(""))
			dc += "(10)" + bean.getEntity().getLot1();
		else
			dc += "(21)" + bean.getEntity().getLotn();
		return dc;
	}
}
