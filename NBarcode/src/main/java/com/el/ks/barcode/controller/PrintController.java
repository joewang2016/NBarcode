package com.el.ks.barcode.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.el.ks.barcode.action.PrintTagAction;
import com.el.ks.barcode.config.Config;

@Controller
public class PrintController {
	
	@Autowired
	private PrintTagAction printAction;
	
	@GetMapping("main/test1")
	@ResponseBody
	public String test() throws CloneNotSupportedException {
		Config config = new Config();
		config.setTrconfig(printAction.getRconfig());
		config.setTconfig(printAction.getConfig());
		Config config1 = (Config) config.clone();
		config1.getTrconfig().setChars("100");
		System.out.println(config);
		System.out.println(config1);
		config1 = (Config) config.clone();
		System.out.println(config);
		System.out.println(config1);
		return printAction.getRconfig().toString();
	}
	
	public static void main(String[] args){
		String sql ="select * from proddta.F554801F where msky='%s'";
		String woSql = "select * from proddta.f554801H where dwupmj>=120308 order by dwupmj,dwupmt";
	}
	
}
