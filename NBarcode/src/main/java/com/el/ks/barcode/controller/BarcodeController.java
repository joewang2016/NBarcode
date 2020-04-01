package com.el.ks.barcode.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.el.ks.barcode.bean.TagBean;
import com.el.ks.barcode.bean.TagDetailBean;
import com.el.ks.barcode.bean.TagScanResultBean;
import com.el.ks.barcode.config.BarcodeConfig;
import com.el.ks.barcode.model.ChinaTagModel;
import com.el.ks.barcode.util.RedisUtil;

@Controller
public class BarcodeController {

	Logger log = Logger.getLogger(this.getClass());

	@Autowired
	RedisUtil util;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private BarcodeConfig config;

	@GetMapping("main")
	public String main() {
		return "main";
	}

	@RequestMapping("test")
	@ResponseBody
	public String test() {
		TagBean tag = new TagBean();
		tag.setCountnb("1");
		tag.setDate("2020-3-31");
		tag.setDl01("19R44");
		tag.setVr01("KS-ID8273CN");
		tag.setPages("1");
		tag.setPrinter("ZJ");
		tag.setUtil(util);
		tag.setMessage("(240)28132AA(21)AAAA");
		tag.ParseMessage();
		ChinaTagModel model = new ChinaTagModel();
		model.setJdbcTemplate(jdbcTemplate);
		model.setConfig(config);
		List<TagDetailBean> lresult = model.GetTagDetail(tag);
		return "index";
	}

	@RequestMapping(value = "insertscanresult", method = RequestMethod.POST)
	@ResponseBody
	public String insertscan(@RequestBody TagBean tag) {
		log.info(tag);
		ChinaTagModel model = new ChinaTagModel();
		List<TagScanResultBean> lresult = model.GetLicenseList(tag);
		if(lresult.size()>0){
			TagScanResultBean scan = lresult.get(0);
			String licence = scan.getRHLOTN();
			model.SaveScanData(tag,scan);
		}
		return "";
	}
}
