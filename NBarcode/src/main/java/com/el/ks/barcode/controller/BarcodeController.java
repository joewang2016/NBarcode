package com.el.ks.barcode.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.el.ks.barcode.action.ChinaTagQueryAction;
import com.el.ks.barcode.action.PrintTagAction;
import com.el.ks.barcode.bean.OpenQuantityBean;
import com.el.ks.barcode.bean.SNBean;
import com.el.ks.barcode.bean.TagBean;
import com.el.ks.barcode.bean.TagScanResultBean;
import com.el.ks.barcode.bean.ThirdSNBean;
import com.el.ks.barcode.service.Const;

@Controller
public class BarcodeController {

	Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private TagBean tag;

	@Autowired
	private ChinaTagQueryAction tagAction;

	@Autowired
	private PrintTagAction printAction;

	@GetMapping("main")
	public String main() {
		return "main";
	}

	@GetMapping("preview")
	public String preview() {
		return "preview";
	}

	@GetMapping("reprint")
	public String reprint() {
		return "welcome";
	}

	@GetMapping("printzj")
	public String printzj() {
		return "welcome3";
	}

	@RequestMapping("test")
	@ResponseBody
	public String test() {
		tag.setCountnb("1");
		tag.setDate("2020-3-31");
		tag.setDl01("19R44");
		tag.setVr01("KS-ID8273CN");
		tag.setPages("1");
		tag.setPrinter("ZJ");
		tag.setMessage("(240)28132AA(21)AAAA");

		//List<TagDetailBean> lresult = tagAction.GetTagDetail();
		return "index";
	}

	@RequestMapping(value = "insertScan", method = RequestMethod.POST)
	@ResponseBody
	public String insertscan(@RequestBody TagBean tag) {
		log.info(tag);
		tagAction.setTag(tag);
		if (this.validateItem()) {
			List<TagScanResultBean> lresult = tagAction.GetLicenseList();
			if (lresult.size() > 0) {
				TagScanResultBean scan = lresult.get(0);
				String licence = scan.getRHLOTN();
				tagAction.setTag(tag);
				tagAction.SaveScanData(scan);

				// Print China Tag
				PrintTag(scan);
			}
			return Const.SUCCESS;
		} else
			return "1";
	}

	private void PrintTag(TagScanResultBean scan) {
		// TODO Auto-generated method stub
		printAction.setTag(tag);
		printAction.PrintTagAll();
	}

	@RequestMapping(value = "queryForScanResult", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject queryForScanResult(String dl01, String vr01) {
		tag.setDl01(dl01);
		tag.setVr01(vr01);
		tagAction.setTag(tag);

		List<OpenQuantityBean> lresult = tagAction.GetOpenQuantity();
		if (lresult != null) {
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			jsonMap.put("total", lresult.size());
			jsonMap.put("rows", lresult);
			JSONObject jsonObj = (JSONObject) JSON.toJSON(jsonMap);
			log.info(jsonObj);
			return jsonObj;
		}
		return null;
	}

	@RequestMapping(value = "requireList", method = RequestMethod.POST)
	@ResponseBody
	public String RequireList(String message) {
		tag.setMessage(message);
		tag.setEntity(null);
		tagAction.setTag(tag);
		return tagAction.RequiryList();
	}

	@RequestMapping(value = "queryLicence", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject GetLicence(String dl01, String vr01, String message, String date) {
		tag.setDl01(dl01);
		tag.setVr01(vr01);
		tag.setMessage(message);
		tag.setDate(date);
		tagAction.setTag(tag);
		List<TagScanResultBean> resultList = tagAction.GetLicenseList();
		if (resultList != null) {
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			jsonMap.put("total", resultList.size());
			jsonMap.put("rows", resultList);
			JSONObject jsonObj = (JSONObject) JSON.toJSON(jsonMap);
			log.info(jsonObj);
			return jsonObj;
		}
		return null;
	}

	private boolean validateItem() {
		return tagAction.ValidateItem();
	}

	@RequestMapping("queryList")
	@ResponseBody
	public JSONObject QueryLicenceList(String date, String message) {
		tag.setMessage(message);
		tag.setDate(date);
		tagAction.setTag(tag);
		List<TagScanResultBean> resultList = tagAction.QueryLicenceList();
		if (resultList != null) {
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			jsonMap.put("total", resultList.size());
			jsonMap.put("rows", resultList);
			JSONObject jsonObj = (JSONObject) JSON.toJSON(jsonMap);
			log.info(jsonObj);
			return jsonObj;
		}
		return null;
	}

	@PostMapping(value = "searchZJ")
	@ResponseBody
	public String SearchZJ(@RequestBody TagBean tag) {
		List<SNBean> list = tagAction.SearchZJ(tag);
		for (SNBean ele : list) {
			ThirdSNBean tele = (ThirdSNBean) ele;
			tag.setEntity(tele);
			tag.setDate(tele.getDate());
			tagAction.setTag(tag);
			printAction.setTag(tag);
			for (int i = 0; i < tele.getUorg(); i++) {
				printAction.PrintTagAll();
			}
		}
		return Const.SUCCESS;
	}

	@PostMapping(value = "reprintzj")
	@ResponseBody
	public String ReprintZJ(@RequestBody TagBean tag) {
		tagAction.setTag(tag);
		int p = Integer.parseInt(tag.getPages());
		if (p > 0) {
			for (int i = 0; i < p; i++) {
				printAction.setTag(tag);
				printAction.PrintTagAll(true);
			}
		}
		return Const.SUCCESS;
	}
}
