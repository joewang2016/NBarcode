package com.el.ks.barcode.action;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.el.ks.barcode.bean.OpenQuantityBean;
import com.el.ks.barcode.bean.SNBean;
import com.el.ks.barcode.bean.TagBean;
import com.el.ks.barcode.bean.TagDetailBean;
import com.el.ks.barcode.bean.TagScanResultBean;
import com.el.ks.barcode.model.ChinaTagModel;
import com.el.ks.barcode.util.SNParaseUtil;

@Service
public class ChinaTagQueryAction {
	@Autowired
	private ChinaTagModel chinaTagModel;

	@Autowired
	private SNParaseUtil util;

	private TagBean tag;

	public void setTag(TagBean tag) {
		this.tag = tag;
		if (StringUtils.isBlank(tag.getCountnb()))
			tag.setCountnb("0");
		if (tag.getEntity() == null && !StringUtils.isBlank(tag.getMessage())) {
			String message = tag.getMessage().replace("^", "(");
			SNBean sne = new SNBean();
			sne = util.ParseTag(message);
			tag.setEntity(sne);
		}
		chinaTagModel.setTag(tag);
	}

	public List<TagScanResultBean> GetLicenseList() {
		return chinaTagModel.GetLicenseList();
	}

	public List<TagDetailBean> GetTagDetail() {
		return chinaTagModel.GetTagDetail();
	}

	public List<OpenQuantityBean> GetOpenQuantity() {
		return chinaTagModel.GetOpenQty();
	}

	public String RequiryList() {
		return chinaTagModel.CheckRequiredList();
	}

	public void SaveScanData(TagScanResultBean scan) {
		chinaTagModel.SaveScanData(scan);
	}

	public boolean ValidateItem() {
		return chinaTagModel.validItem();
	}

	public List<TagScanResultBean> QueryLicenceList() {
		return chinaTagModel.QueryLicenceList();
	}
	
	public List<SNBean> SearchZJ(TagBean bean){
		return chinaTagModel.SearchZJ(bean.getVr01(), bean.getDoco(), bean.getDl01());
	}
	
	public void insertReprint(TagBean bean){
		chinaTagModel.insertReprint(bean);
	}
}
