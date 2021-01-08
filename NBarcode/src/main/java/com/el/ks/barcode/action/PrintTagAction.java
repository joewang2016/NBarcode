package com.el.ks.barcode.action;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.el.ks.barcode.bean.TagBean;
import com.el.ks.barcode.bean.TagDetailBean;
import com.el.ks.barcode.bean.TagDetailThirdBean;
import com.el.ks.barcode.bean.TagTemplateBean;
import com.el.ks.barcode.config.Config;
import com.el.ks.barcode.config.PrinterConfig;
import com.el.ks.barcode.config.TagConfig;
import com.el.ks.barcode.config.TagDCConfig;
import com.el.ks.barcode.config.TagEmptyConfig;
import com.el.ks.barcode.config.TagLeftConfig;
import com.el.ks.barcode.config.TagLogoConfig;
import com.el.ks.barcode.config.TagRightConfig;
import com.el.ks.barcode.model.ChinaTagModel;
import com.el.ks.barcode.service.Const;
import com.el.ks.barcode.service.TagDetail;
import com.el.ks.barcode.util.OSinfo;

import lombok.Data;
import lombok.Setter;

@Data
@Service
public class PrintTagAction {

	Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private TagConfig config;
	@Autowired
	private TagLeftConfig lconfig;
	@Autowired
	private TagRightConfig rconfig;
	@Autowired
	private PrinterConfig pconfig;
	@Autowired
	private TagLogoConfig logoConfig;
	@Autowired
	private TagDCConfig dcConfig;
	@Autowired
	private TagEmptyConfig emptyConfig;
	@Autowired
	private ChinaTagModel model;

	// tag content
	@Autowired
	private TagTemplateBean templateBean;

	@Setter
	private TagBean tag;

	private StringBuffer prnBuff = null;
	private boolean beginPrint = false;
	private List<TagTemplateBean> tleft;
	private List<TagTemplateBean> tright;
	Integer lleft = 0;
	Integer ltop = 0;
	Integer rleft = 0;
	Integer rtop = 0;
	Integer etop = 0;
	Integer eleft = 0;
	String filename = "";

	private Config gconfig;
	private Config cloneConfig;

	Map<String, String> protectMap = new HashMap<String, String>();

	public void PrintTagAll() {
		PrintTagAll(false);
	}

	public void PrintTagAll(Boolean reprint) {
		init();
		if (tag.getEntity().getLitm().equals(rconfig.getLitm())) {
			cloneConfig.getTconfig().setFontheight(rconfig.getSpc_fontheight());
			cloneConfig.getTconfig().setFontwidth(rconfig.getSpc_fontwidth());
			cloneConfig.getTconfig().setVpadding(rconfig.getSpc_vpadding());
			cloneConfig.getTrconfig().setChars(rconfig.getSpc_chars());
		}
		List list = null;
		if (StringUtils.isBlank(tag.getMessage()))
			list = model.GetTagDetailByDoc();
		else
			list = model.GetTagDetail(reprint);
		if (list.size() <= 0)
			return;
		prnBuff = new StringBuffer();
		BeginPrint();
		Boolean third = false;
		for (Object ele : list) {
			if (ele instanceof TagDetailBean) {
				TagDetailBean detail = (TagDetailBean) ele;
				GetTagDetail(detail);
				ChangeFont(config.getFontheight(), config.getFontwidth(), 0);
				PrintLeftPart();
				PrintQMark();
				PrintLogo();
				// Right Part
				ChangeFont(cloneConfig.getTconfig().getFontheight(), cloneConfig.getTconfig().getFontwidth(), 0);
				PrintRightPart();
				PrintRightDimensionString(StringUtils.substring(detail.getImsrp1(), 0, 1) + getKSDimensionCode());
				PrintDimensionCode(dcConfig.getLeftmargin(), dcConfig.getTopmargin(), dcConfig.getFontheight(),
						getKSDimensionCode());
			} else if (ele instanceof TagDetailThirdBean) {
				ChangeFont(emptyConfig.getFontheight(), emptyConfig.getFontwidth(), 0);
				TagDetailThirdBean detail = (TagDetailThirdBean) ele;
				GetTagDetail(detail);
				PrintThirdTag();
				third = true;
				break;
			} else if (ele instanceof String) {
				ChangeFont(emptyConfig.getFontheight(), emptyConfig.getFontwidth(), 0);
				String detail = (String) ele;
				PrintEmptyTagRow(detail);
				third = true;
				break;
			}
		}
		if (third) {
			PrintLogo();
			PrintRightDimensionString(emptyConfig.getFontheight(), emptyConfig.getFontwidth(), dcConfig.getStrwidth(),
					getKSDimensionCode());
			PrintDimensionCode(dcConfig.getLeftmargin(), dcConfig.getTopmargin(), dcConfig.getFontheight(),
					getKSDimensionCode());
		}
		EndPrint();
		log.info(prnBuff);
		WriteLog();
		PrintOut();
	}

	private void PrintEmptyTagRow(String detail) {
		// TODO Auto-generated method stub
		List<String> list = this.LineBreak(detail, Integer.parseInt(emptyConfig.getChars()));
		if (list.size() == 1) {
			PrintRow(eleft, etop, emptyConfig.getFontheight(), emptyConfig.getFontwidth(), detail);
			etop = NextRow(etop, Integer.parseInt(emptyConfig.getFontheight()),
					Integer.parseInt(emptyConfig.getVpadding()));
		} else {
			etop = NextRow(etop, Integer.parseInt(emptyConfig.getFontheight()),
					Integer.parseInt(emptyConfig.getVpadding()));
			PrintBlock(Integer.parseInt(emptyConfig.getWidth()), etop, emptyConfig.getFontheight(),
					emptyConfig.getFontwidth(), emptyConfig.getVpadding(), detail);
			etop = NextRow(etop, Integer.parseInt(emptyConfig.getFontheight()),
					Integer.parseInt(emptyConfig.getVpadding()));
		}
	}

	private void PrintThirdTag() {
		// TODO Auto-generated method stub
		for (TagTemplateBean ele : tleft) {
			if (ele.getValue() == null)
				continue;
			if (ele.getIndex() == 3) {
				String desc = ele.getValue();
				desc = desc.replace("，", ",");
				if (desc.indexOf(",") > 0)
					desc = desc.substring(0, desc.indexOf(","));
				ele.setValue(desc);
			}
			PrintThirdRow(ele);
		}
	}

	private void PrintThirdRow(TagTemplateBean bean) {
		// TODO Auto-generated method stub
		String row = StringUtils.join(bean.getLabel(), bean.getValue());
		if (bean.getIndex() > 0 && bean.getIndex() < 5) {
			PrintRow(eleft, etop, emptyConfig.getFontheight(), emptyConfig.getFontwidth(), row);
			etop = NextRow(etop, Integer.parseInt(emptyConfig.getFontheight()),
					Integer.parseInt(emptyConfig.getVpadding()));
		} else {
			List<String> list = this.LineBreak(row, Integer.parseInt(emptyConfig.getChars()));
			if (list.size() == 1) {
				PrintRow(eleft, etop, emptyConfig.getFontheight(), emptyConfig.getFontwidth(), row);
				etop = NextRow(etop, Integer.parseInt(emptyConfig.getFontheight()),
						Integer.parseInt(emptyConfig.getVpadding()));
			} else {
				PrintRow(eleft, etop, emptyConfig.getFontheight(), emptyConfig.getFontwidth(), bean.getLabel());

				etop = NextRow(etop, Integer.parseInt(emptyConfig.getFontheight()),
						Integer.parseInt(emptyConfig.getVpadding()));

				Double len = StringLen(bean.getLabel());
				Integer width = (int) (Integer.parseInt(emptyConfig.getWidth())
						- len * Integer.parseInt(emptyConfig.getFontwidth()));

				PrintBlock(width, etop, emptyConfig.getFontheight(), emptyConfig.getFontwidth(),
						emptyConfig.getVpadding(), bean.getValue());

				etop = NextRow(etop, Integer.parseInt(emptyConfig.getFontheight()),
						Integer.parseInt(emptyConfig.getVpadding()));
			}
		}
	}

	private void PrintOut() {
		// TODO Auto-generated method stub
		if (filename.length() > 0) {
			String command = StringUtils.join(pconfig.getWgq(), tag.getPrinter(), StringUtils.SPACE, filename);
			log.info(command);
			if (OSinfo.isWindows()) {
				Runtime run = Runtime.getRuntime();
				Process p;
				try {
					p = run.exec(command);
					p.waitFor();
				} catch (IOException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private void WriteLog() {
		// TODO Auto-generated method stub
		File f = new File(config.getWorkspace());
		File tempFile = null;

		try {
			tempFile = File.createTempFile("tmp", ".txt", f);
			filename = tempFile.getAbsolutePath();
			FileUtils.write(tempFile, prnBuff, "UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void PrintRightDimensionString(String arg) {
		// TODO Auto-generated method stub

		PrintRightDimensionString(config.getFontheight(), config.getFontwidth(), dcConfig.getStrwidth(), arg);
	}

	private void PrintRightDimensionString(String fheight, String fwidth, String width, String arg) {
		// TODO Auto-generated method stub
		prnBuff.append("^FT").append(dcConfig.getStrleft()).append(",").append(dcConfig.getStrtop());
		prnBuff.append("^A1N,").append(fheight).append(",").append(fwidth);
		prnBuff.append("^FB").append(width).append(",1,,R\r\n");
		if (StringUtils.contains(arg, '~')) {
			prnBuff.append("^FH");
			arg = arg.replace("~", "_7E");
		}
		prnBuff.append("^FD").append(arg.trim()).append("^FS\r\n");
	}

	private void PrintLeftPart() {
		for (TagTemplateBean ele : tleft) {
			if (ele.getIndex() == 13)
				continue;
			if (ele.getIndex() == 4)
				ele.setValue(tag.getDate());
			if (ele.getIndex() == 2) {
				String desc = ele.getValue();
				desc = desc.replace("，", ",");
				if (desc.indexOf(",") > 0)
					desc = desc.substring(0, desc.indexOf(","));
				ele.setValue(desc);
			}
			PrintLeftRow(ele);
		}
	}

	private void PrintRightPart() {
		for (TagTemplateBean ele : tright) {
			if (!tag.getEntity().getLitm().equals(rconfig.getLitm())) {
				if (ele.getIndex() > 20)
					continue;
			}
			PrintRightRow(ele);
		}
	}

	@PostConstruct
	public void init() {
		lleft = Integer.parseInt(lconfig.getLeftmargin());
		ltop = Integer.parseInt(lconfig.getTopmargin());
		rleft = Integer.parseInt(rconfig.getLeftmargin());
		rtop = Integer.parseInt(rconfig.getTopmargin());
		eleft = Integer.parseInt(emptyConfig.getLeftmargin());
		etop = Integer.parseInt(emptyConfig.getTopmargin());
		protectMap.put("B", "B");
		protectMap.put("BF", "C");
		protectMap.put("CF", "D");
		protectMap.put("AP", "E");
		protectMap.put("APG", "F");
		protectMap.put("DG", "G");
		protectMap.put("NE", "H");
		protectMap.put("DL", "I");
		protectMap.put("DP-B", "J");
		protectMap.put("DP-BF", "K");
		protectMap.put("DP-CF", "L");

		gconfig = new Config();
		gconfig.setTconfig(config);
		gconfig.setTrconfig(rconfig);

		try {
			cloneConfig = (Config) gconfig.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void BeginPrint() {
		beginPrint = true;
		prnBuff.append("^XA\r\n");
		prnBuff.append("^CW1,E:FONTR.TTF\r\n");
		prnBuff.append("^SEE:GB18030.DAT^CI28,21,36\r\n");
		prnBuff.append("^LH10,10\r\n");
	}

	private void GetTagDetail(Object bean) {
		if (bean == null)
			return;
		Class clazz = bean.getClass();
		tleft = new ArrayList<TagTemplateBean>();
		tright = new ArrayList<TagTemplateBean>();
		try {
			Method method = null;
			String methodname = "";
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				if (field.isAnnotationPresent(TagDetail.class)) {
					TagDetail annotation = field.getAnnotation(TagDetail.class);
					methodname = "get" + StringUtils.capitalize(field.getName());
					method = clazz.getMethod(methodname);
					method.setAccessible(true);
					String value = StringUtils.trim((String) method.invoke(bean));
					String label = annotation.name();
					TagTemplateBean tplt = new TagTemplateBean();
					tplt.setIndex(annotation.index());
					tplt.setLabel(label);
					tplt.setValue(value);
					if (annotation.pos().equals("L"))
						tleft.add(tplt);
					else
						tright.add(tplt);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private void ChangeFont(String h, String w, Integer fontIndex) {
		if (fontIndex == 0)
			prnBuff.append("^A@N,").append(h).append(",").append(w).append(",E:FONTR.TTF\r\n");
		if (fontIndex == 1)
			prnBuff.append("^A@N,").append(h).append(",").append(w).append(",E:KARLSTOR.TTF\r\n");
	}

	private Integer NextRow(Integer top, Integer vpadding) {
		// top += Integer.parseInt(config.getFontheight()) + vpadding;
		// return top;
		return NextRow(top, Integer.parseInt(config.getFontheight()), vpadding);
	}

	private Integer NextRow(Integer top, Integer fontheight, Integer vpadding) {
		top += fontheight + vpadding;
		return top;
	}

	private void PrintRightRow(TagTemplateBean bean) {
		// TODO Auto-generated method stub
		String value = bean.getValue();
		String label = bean.getLabel();
		String content = StringUtils.join(label, value);

		List<String> list = this.LineBreak(content, Integer.parseInt(cloneConfig.getTrconfig().getChars()));
		if (list.size() == 1) {
			if (bean.getIndex() == 18) {
				PrintRow(rleft, rtop, label, cloneConfig);
				// Pic Font
				if (!StringUtils.isAllBlank(protectMap.get(value))) {
					//ChangeFont(rconfig.getPic_fontheight(), rconfig.getPic_fontwidth(), 1);

					PrintPic(protectMap.get(value));
					ChangeFont(cloneConfig.getTconfig().getFontheight(), cloneConfig.getTconfig().getFontwidth(), 0);
				}
			} else
				PrintRow(rleft, rtop, content, cloneConfig);
			rtop = NextRow(rtop, Integer.parseInt(cloneConfig.getTconfig().getVpadding()));
		} else if (list.size() > 1) {
			PrintRow(rleft, rtop, label, cloneConfig);
			rtop = NextRow(rtop, Integer.parseInt(cloneConfig.getTconfig().getVpadding()));
			Double len = StringLen(label);
			Integer width = (int) (Integer.parseInt(rconfig.getWidth())
					- len * Integer.parseInt(cloneConfig.getTconfig().getFontwidth()));
			PrintBlock(width, rtop, value);
			rtop = NextRow(rtop, Integer.parseInt(cloneConfig.getTconfig().getVpadding()));
		}
	}
	
	private void PrintPic(String arg){
		String row = "^FT^A@N,%s,%s,E:KARLSTOR.TTF^FD%s^FS";
		row = String.format(row, rconfig.getPic_fontheight(), rconfig.getPic_fontwidth(),arg);
		prnBuff.append(row).append("\r\n");
	}

	private void PrintLeftRow(TagTemplateBean bean) {
		// TODO Auto-generated method stub
		String value = bean.getValue();
		String label = bean.getLabel();
		String content = StringUtils.join(label, value);
		List<String> list = this.LineBreak(content, Integer.parseInt(lconfig.getChars()));
		if (list.size() == 1) {
			if (bean.getIndex() == 5) {
				content = StringUtils.join(StringUtils.repeat(StringUtils.SPACE, 4), bean.getLabel(), bean.getValue());
				PrintRow(0, 0, content);
			} else
				PrintRow(lleft, ltop, content);
			if (bean.getIndex() != 5)
				ltop = NextRow(ltop, Integer.parseInt(config.getVpadding()));
		} else if (list.size() > 1) {
			PrintRow(lleft, ltop, label);
			ltop = NextRow(ltop, Integer.parseInt(config.getVpadding()));
			Double len = StringLen(label);
			Integer width = (int) (Integer.parseInt(lconfig.getWidth())
					- len * Integer.parseInt(config.getFontwidth()));
			PrintBlock(width, ltop, value);
			ltop = NextRow(ltop, Integer.parseInt(config.getVpadding()));
		}
	}

	private void EndPrint() {
		prnBuff.append("^MMC\r\n");
		prnBuff.append("^XZ\r\n");
	}

	private void PrintQMark() {
		prnBuff.append("^FT");
		ChangeFont("31", "30", 0);
		prnBuff.append("^FD    合格^FS\r\n");
	}

	private void PrintRow(Integer left, Integer top, String arg) {
		if (!StringUtils.isAllBlank(arg))
			PrintRow(left, top, config.getFontheight(), config.getFontwidth(), arg);
	}

	private void PrintRow(Integer left, Integer top, String arg, Config config) {
		if (!StringUtils.isAllBlank(arg))
			PrintRow(left, top, config.getTconfig().getFontheight(), config.getTconfig().getFontwidth(), arg);
	}

	private void PrintRow(Integer left, Integer top, String height, String width, String arg) {
		prnBuff.append("^FT");
		if (left != 0)
			prnBuff.append(left).append(",").append(top);

		prnBuff.append("^A1N,").append(height).append(",").append(width);
		if (StringUtils.contains(arg, '~')) {
			prnBuff.append("^FH");
			arg = arg.replace("~", "_7E");
		}
		prnBuff.append("^FD").append(arg).append("^FS\r\n");
	}

	private void PrintBlock(Integer width, Integer top, String arg) {
		PrintBlock(width, top, config.getFontheight(), config.getFontwidth(), config.getVpadding(), arg);
	}

	private void PrintBlock(Integer width, Integer top, String fheight, String fwidth, String vpadding, String arg) {
		prnBuff.append("^FT,").append(top).append("^A1N,").append(fheight).append(",").append(fwidth);
		prnBuff.append("^FB").append(width).append(",2,").append(vpadding).append(",\r\n");

		if (StringUtils.contains(arg, '~')) {
			prnBuff.append("^FH");
			arg = arg.replace("~", "_7E");
		}
		prnBuff.append("^FD").append(arg.trim()).append("^FS\r\n");
	}

	private void PrintBlock(Integer width, Integer left, Integer top, String fheight, String fwidth, String vpadding,
			String arg) {
		prnBuff.append("^FT").append(left).append(",").append(top).append("^A1N,").append(fheight).append(",")
				.append(fwidth);
		prnBuff.append("^FB").append(width).append(",2,").append(vpadding).append(",\r\n");

		if (StringUtils.contains(arg, '~')) {
			prnBuff.append("^FH");
			arg = arg.replace("~", "_7E");
		}
		prnBuff.append("^FD").append(arg.trim()).append("^FS\r\n");
	}

	private void PrintDimensionCode(String left, String top, String height, String arg) {
		prnBuff.append("^FT").append(left).append(",").append(top).append("\r\n");
		prnBuff.append("^BXN,").append(height).append(",200\r\n");
		prnBuff.append("^FD").append(arg).append("^KS\r\n");
	}

	private void PrintImage(String arg) {
		prnBuff.append("^FT");
		ChangeFont(config.getFontheight(), config.getFontwidth(), 1);
		prnBuff.append("^FD  ").append(arg).append("^FS\r\n");
	}

	private String getKSDimensionCode() {
		String dc = "";
		// if (tag.getEntity() != null) {
		// dc = "(240)" + tag.getEntity().getLitm();
		// if (StringUtils.isBlank(tag.getEntity().getLotn()))
		// dc += "(10)" + tag.getEntity().getLot1();
		// else
		// dc += "(21)" + tag.getEntity().getLotn();
		// }
		// if (StringUtils.compare(tag.getDate(), " ") > 0) {
		// dc += "(11)" + StringUtils.remove(tag.getDate(), '-');
		// }
		dc = tag.getEntity().toChinaString();
		return dc;
	}

	private void PrintLogo() {
		prnBuff.append("^FT").append(logoConfig.getLeftmargin()).append(",").append(logoConfig.getTopmargin());
		ChangeFont(logoConfig.getFontheight(), logoConfig.getFontwidth(), 1);
		prnBuff.append("^FD").append(Const.LOGO).append("^FS\r\n");
	}

	private Double CharLen(char c) {
		Double len = 0d;
		if (c >= 0X20 && c <= 0X40 || c >= 0X5B && c <= 0X7E) {
			len = 0.5d;
		} else if (c > 0X40 && c < 0X5B) {
			len = 0.9d;
		} else {
			len = 1d;
		}
		return len;
	}

	private Double StringLen(String arg) {
		char c = 0;
		Double rowLen = 0d;
		for (int i = 0; i < arg.length(); i++) {
			c = arg.charAt(i);
			rowLen += CharLen(c);
		}
		return rowLen;
	}

	private ArrayList<String> LineBreak(String arg, Integer chars) {
		ArrayList<String> result = new ArrayList<String>();
		char c = 0;
		StringBuffer ele = new StringBuffer();
		Double rowLen = 0d;
		for (int i = 0; i < arg.length(); i++) {
			c = arg.charAt(i);
			rowLen += CharLen(c);
			if (rowLen >= chars) {
				result.add(ele.toString());
				ele.setLength(0);
				rowLen = 0d;
				ele.append(c);
			} else
				ele.append(c);
		}
		if (ele.length() > 0)
			result.add(ele.toString());
		if (result.size() > 2) {
			log.info(StringUtils.repeat('*', 20));
			log.info(result);
			log.info(StringUtils.repeat('*', 20));
		}
		return result;
	}
}
