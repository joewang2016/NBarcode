package com.el.ks.barcode.model;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import com.el.ks.barcode.bean.OpenQuantityBean;
import com.el.ks.barcode.bean.TagBean;
import com.el.ks.barcode.bean.TagDetailBean;
import com.el.ks.barcode.bean.TagDetailThirdBean;
import com.el.ks.barcode.bean.TagScanResultBean;
import com.el.ks.barcode.config.TagConfig;
import com.el.ks.barcode.service.Const;
import com.el.ks.barcode.service.TagDetail;
import com.el.ks.barcode.util.DateFormat;
import com.el.ks.barcode.util.DateTool;
import com.el.ks.barcode.util.StringTool;
import com.zaxxer.hikari.HikariDataSource;

import lombok.Setter;

@Repository
public class ChinaTagModel {

	Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private TagConfig config;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private HikariDataSource datasource;

	private String schema;

	private String user = " ";
	@Setter
	private TagBean tag;

	public ChinaTagModel() {
	}

	@PostConstruct
	public void init() {
		schema = datasource.getSchema();
	}

	@SuppressWarnings({ "unused", "unchecked" })
	public List<TagScanResultBean> GetLicenseList() {

		// initTag();

		List<TagScanResultBean> lresult = new ArrayList<TagScanResultBean>();

		List storeResult = ExecStoreGetLicenseStatus();

		if (storeResult.size() > 0) {
			Map row = (HashMap) storeResult.get(0);
			String CFDAStus = (String) row.get("CFDAStus");

			String sql = "select RHLOTN,RHALPH,RDAITM,RDLITM,RDADD4,RHEFFT,RDSRP6 from %s.F56B4310 inner join %s.F550002 on ABDL01=RDLOTN and ABLITM=RDLITM "
					+ "inner join %s.F550001 on RDLOTN=RHLOTN where ABDL10='%s' and ABVR01='%s' and ABLITM='%s' and RDSRP6='%s'";
			sql = String.format(sql, schema, schema, schema, tag.getDl01(), tag.getVr01(), tag.getEntity().getLitm(),
					CFDAStus);
			log.info(sql);

			jdbcTemplate.query(sql, new RowCallbackHandler() {

				@Override
				public void processRow(ResultSet rs) throws SQLException {
					// TODO Auto-generated method stub
					TagScanResultBean ret = new TagScanResultBean();
					ret.setRDLITM(rs.getString("RDLITM"));
					ret.setRDADD4(rs.getString("RDADD4"));
					ret.setRDAITM(rs.getString("RDAITM"));
					ret.setRDSRP6(rs.getString("RDSRP6"));
					ret.setRHALPH(rs.getString("RHALPH"));
					ret.setRHEFFT(DateFormat.CJulian(rs.getInt("RHEFFT")));
					ret.setRHLOTN(rs.getString("RHLOTN"));
					lresult.add(ret);
				}
			});
			if (lresult.size() == 0) {
				TagScanResultBean ret = new TagScanResultBean();
				ret.setRDLITM(" ");
				ret.setRDADD4(" ");
				ret.setRDAITM(" ");
				ret.setRDSRP6(" ");
				ret.setRHALPH(" ");
				ret.setRHEFFT(" ");
				ret.setRHLOTN(" ");
				lresult.add(ret);
			}

		}
		return lresult;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List ExecStoreGetLicenseStatus() {
		// TODO Auto-generated method stub
		List list = (List) jdbcTemplate.execute(new CallableStatementCreator() {

			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				// TODO Auto-generated method stub
				String sql = "execute %s.BarcodeLabel ?,?,?,?";
				sql = String.format(sql, schema);
				log.info(sql);

				CallableStatement cs = con.prepareCall(sql);
				cs.setDate(1, new java.sql.Date(DateTool.parseDate(tag.getDate(), "yyyy-MM-dd").getTime()));
				cs.setString(2, tag.getEntity().getLitm());
				cs.setString(3, tag.getVr01());
				cs.setString(4, tag.getDl01());
				return cs;
			}
		}, new CallableStatementCallback() {

			@Override
			public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
				// TODO Auto-generated method stub
				List resultsMap = new ArrayList();
				cs.execute();
				ResultSet rs = cs.executeQuery();
				if (rs.next()) {
					Map rowMap = new HashMap();
					rowMap.put("CFDAID", rs.getString("RHLOTN"));
					rowMap.put("CFDAStus", rs.getString("CFDAStus"));
					rowMap.put("LabelTy", rs.getString("LabelTy"));
					resultsMap.add(rowMap);
				}
				rs.close();
				return resultsMap;
			}
		});

		return list;
	}

	public void SaveScanData(TagScanResultBean scan) {
		// TODO Auto-generated method stub
		// initTag();

		GetNextSerialNumber();

		String sql = "select F56B4310.*,(ABUORG-ABMATH01) ABUOPN from %s.F56B4310 where ABDL10='%s' and ABVR01='%s' and ABLITM='%s' and (1=1)";
		if (getSRCE().equals("7"))
			sql = sql + " and ABLOTN='" + tag.getEntity().getLot1() + "'";
		log.info(StringUtils.repeat('*', 20));
		log.info(tag);
		log.info(StringUtils.repeat('*', 20));
		Integer qty = Integer.valueOf(tag.getCountnb().trim());
		sql = String.format(sql, schema, tag.getDl01(), tag.getVr01(), tag.getEntity().getLitm());
		if (qty > 0)
			sql = sql.replace("1=1", "ABMATH01<ABUORG");
		log.info(sql);

		jdbcTemplate.query(sql, new RowCallbackHandler() {

			String insertTempalte = "insert into %s.F56B4311 (PLEV01,PLVR01,PLDOCO,PLKCOO,PLDCTO,PLLITM,PLUORG,PLLOT1,"
					+ "PLLOCN,PLCNID,PLURDT,PLDGL,PLDL10,PLUSD1,PLURAT,PLURRF,PLINIKEY,PLPID,PLUSER,PLUPMJ,PLUPMT,PLIR05,PLMCU,PLLOTN,PLURCD,PLDL12) values ('%s','%s','%d','%s','%s','%s',"
					+ "'%s','%s','%s','%s','%d','%d','%s','%d','%d','%s',%s,'%s','%s',%d,%d,'%s','%s',N'%s','%s','%s')";
			String updateF56B4310Tempalte = "update %s.F56B4310 set ABEV01='1',ABMATH01=ABMATH01+%d where ABDOCO='%d' and ABDCTO='%s' and ABKCOO='%s' and ABLITM='%s' and ABUKID=%d";
			String updateF56B4311Template = "update %s.F56B4311 set PLUSD1 = %d,PLLOTN=N'%s' where PLVR01='%s' and PLLITM='%s' and PLLOT1='%s' and "
					+ "PLCNID='%s' and PLDL10='%s'";
			String insertSql = "";
			String updateSql = "";

			Integer qty = Integer.valueOf(tag.getCountnb().trim());
			Integer uopn = 0;
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Boolean insert = true;

			@Override
			public void processRow(ResultSet rs) throws SQLException {
				// TODO Auto-generated method stub
				if (insert) {
					uopn = rs.getInt("ABUOPN") / 100;
					if (qty > uopn) {
						qty -= uopn;
					} else {
						uopn = qty;
						qty -= uopn;
					}

					insertSql = String.format(insertTempalte, schema, "0", tag.getVr01(), rs.getInt("ABDOCO"),
							rs.getString("ABKCOO"), rs.getString("ABDCTO"), rs.getString("ABLITM"), uopn * 100,
							tag.getEntity().getLot1(), rs.getString("ABLOCN"), rs.getString("ABCNID"),
							rs.getInt("ABURDT"), rs.getInt("ABDGL"), tag.getDl01(), DateFormat.CJDEInt(tag.getDate()),
							Integer.valueOf(tag.getPages()) * 100, tag.getPrinter(), "newid()", "BARCODE", user,
							DateFormat.CJDEInt(dateFormat.format(new Date())), DateFormat.CJDETime(),
							rs.getString("ABIR05"), rs.getString("ABMCU"), scan.getRHLOTN(), scan.getRDSRP6(),
							scan.getRDAITM());
					log.info(insertSql);
					jdbcTemplate.update(insertSql);

					updateSql = String.format(updateF56B4310Tempalte, schema, (Integer) uopn * 100, rs.getInt("ABDOCO"),
							rs.getString("ABDCTO"), rs.getString("ABKCOO"), rs.getString("ABLITM"),
							rs.getInt("ABUKID"));
					log.info(updateSql);
					jdbcTemplate.update(updateSql);

					updateSql = String.format(updateF56B4311Template, schema, DateFormat.CJDEInt(tag.getDate()),
							scan.getRHLOTN(), tag.getVr01(), rs.getString("ABLITM"), tag.getEntity().getLot1(),
							rs.getString("ABCNID"), tag.getDl01());
					log.info(updateSql);
					jdbcTemplate.update(updateSql);
					if (qty == 0)
						insert = false;
				}
			}

		});
	}

	private String getSRCE() {
		String srce = "";
		String sql = "select IMSRCE from %s.F4101 where IMLITM='%s'";
		sql = String.format(sql, schema, tag.getEntity().getLitm());
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		if (list.size() > 0)
			return (String) list.get(0).get("IMSRCE");
		return "";
	}

	private synchronized void GetNextSerialNumber() {
		String sql = "";
		int jDate = DateFormat.getJulianDate();

		sql = "select SLLOT1 from %s.F56B4313 where SLUPMJ=%d and SLVR01='%s' and SLLITM='%s'";
		sql = String.format(sql, schema, jDate, tag.getVr01(), tag.getEntity().getLitm());
		log.info(sql);

		if (tag.getEntity().getLot1().equals(Const.MAX_LOT1)) {

			Integer nn = 1;
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
			if (list.size() > 0) {
				nn = Integer.parseInt((String) list.get(0).get("SLLOT1"));
				sql = "update %s.F56B4313 set SLLOT1='%d' where SLUPMJ=%d and SLVR01='%s' and SLLITM='%s'";
				sql = String.format(sql, schema, nn + 1, jDate, tag.getVr01(), tag.getEntity().getLitm());
				jdbcTemplate.execute(sql);
				log.info(sql);
			} else {
				sql = "insert %s.F56B4313 values('%d','%s','%s','%d')";
				sql = String.format(sql, schema, jDate, tag.getVr01(), tag.getEntity().getLitm(), nn + 1);
				jdbcTemplate.execute(sql);
				log.info(sql);
			}
			tag.getEntity().setLot1(String.valueOf(jDate) + StringTool.lpad(String.valueOf(nn), "0", 4));
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List GetTagDetail() {

		List lstatus = ExecStoreGetLicenseStatus();
		Map row = (HashMap) lstatus.get(0);
		String CFDAID = (String) row.get("CFDAID");
		String CFDAStus = (String) row.get("CFDAStus");
		String LabelTy = (String) row.get("LabelTy");

		String sql = "";
		List lresult = new ArrayList();

		if ("T".equals(LabelTy) && CFDAID.compareTo(" ") <= 0) {
			sql = "select IMDSC1,IMDSC2 from %s.F4101 where IMLITM='%s'";
			sql = String.format(sql, schema, tag.getEntity().getLitm());
			log.info(sql);
			jdbcTemplate.query(sql, new RowCallbackHandler() {

				@Override
				public void processRow(ResultSet rs) throws SQLException {
					// TODO Auto-generated method stub
					String desc = StringUtils.join(rs.getString("IMDSC1"), rs.getString("IMDSC2"));
					if (StringUtils.compare(desc, " ") > 0) {
						lresult.add("产品名称: " + desc);
						lresult.add("型号: " + tag.getEntity().getLitm());
						lresult.add("生产日期: " + tag.getDate());
						lresult.add("销售商名称: 卡尔史托斯内窥镜（上海）有限公司");
						lresult.add("销售商联系方式: 800-819-9500/400-670-9500");
						lresult.add("销售商住所: 中国（上海）自由贸易试验区龙东大道3000号5号楼701A室");
					}
				}
			});
		} else if (LabelTy.compareTo(" ") > 0) {
			String szLotn = "";
			if (CFDAID.compareTo(" ") > 0)
				szLotn = "RDLOTN=N'" + CFDAID + "'";
			else
				szLotn = "1=1";

			sql = "select * from %s.F550002 inner join %s.F550001 on RDLOTN=RHLOTN inner join %s.F4101 on RDITM=IMITM where RDLITM='%s' "
					+ "and %s and RDSRP6='%s'";
			sql = String.format(sql, schema, schema, schema, tag.getEntity().getLitm(), szLotn, CFDAStus);
			log.info(sql);
			if (CFDAStus.equals("Y"))
				jdbcTemplate.query(sql, new RowCallbackHandler() {

					@Override
					public void processRow(ResultSet rs) throws SQLException {
						// TODO Auto-generated method stub
						System.out.println("Query");
						TagDetailBean detail = new TagDetailBean();
						WrapTagDetail(rs, detail);
						log.info(detail);
						lresult.add(detail);
					}
				});
			else
				jdbcTemplate.query(sql, new RowCallbackHandler() {

					@Override
					public void processRow(ResultSet rs) throws SQLException {
						// TODO Auto-generated method stub
						System.out.println("Query");
						TagDetailThirdBean detail = new TagDetailThirdBean();
						WrapTagDetail(rs, detail);
						log.info(detail);
						lresult.add(detail);
					}
				});
		}
		return lresult;
	}

	private void WrapTagDetail(ResultSet rs, Object bean) {
		if (bean == null)
			return;
		Class clazz = bean.getClass();
		try {
			Method method = null;
			String methodname = "";
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				if (field.isAnnotationPresent(TagDetail.class)) {
					TagDetail annotation = field.getAnnotation(TagDetail.class);
					boolean db = annotation.db();
					if (db) {
						methodname = "set" + StringUtils.capitalize(field.getName());
						method = clazz.getMethod(methodname, String.class);
						method.setAccessible(true);
						String value = rs.getString(field.getName()).trim();
						if (StringUtils.isBlank(value) && annotation.pos().equals("R"))
							value = Const.CORRECT;
						method.invoke(bean, value);
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public String CheckRequiredList() {
		// if (message.equals(""))
		// return "0";
		// message = message.replace("^", "(");
		// SNBean sne = new SNBean();
		// sne = util.ParseTag(message);
		// String litm = sne.getLitm();

		String sql = "select 1 from %s.F554101A where MALITM='%s' and MAEV01='Y'";
		sql = String.format(sql, schema, tag.getEntity().getLitm());
		log.info(sql);

		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		if (list.size() > 0)
			return "1";
		return "0";
	}

	public List<OpenQuantityBean> GetOpenQty() {
		List<OpenQuantityBean> lRet = new ArrayList<OpenQuantityBean>();
		OpenQuantityBean bean = null;
		String vr01 = tag.getVr01();
		// get Open Quantity
		if (vr01 == null)
			vr01 = " ";
		String vr03 = "";
		if (vr01.compareTo(" ") > 0) {
			vr03 = String.format("ABVR01='%s'", vr01);
		} else {
			vr03 = "1=1";
		}

		String sql = "select * from View_Barcode_Label_ScanList where ABDL10='%s' and %s";

		sql = String.format(sql, tag.getDl01(), vr03);
		log.info(sql);

		jdbcTemplate.query(sql, new RowCallbackHandler() {

			OpenQuantityBean bean = null;

			@Override
			public void processRow(ResultSet rs) throws SQLException {
				// TODO Auto-generated method stub
				bean = new OpenQuantityBean();
				bean.setCNID(rs.getString("ABCNID"));
				bean.setDL10(rs.getString("ABDL10"));
				bean.setLITM(rs.getString("ABLITM"));
				bean.setPQOH(String.valueOf(rs.getInt("PDUOPN") / 100));
				bean.setVR01(rs.getString("ABVR01"));
				bean.setUORG(String.valueOf(rs.getInt("PLUORG") / 100));
				int diff = (rs.getInt("PDUOPN") - rs.getInt("PLUORG")) / 100;
				bean.setDIFF(String.valueOf(diff));
				if (diff > 0)
					lRet.add(bean);
			}
		});

		if (lRet.size() <= 0) {
			bean = new OpenQuantityBean();
			bean.setCNID(" ");
			bean.setDL10(" ");
			bean.setLITM(" ");
			bean.setPQOH(" ");
			bean.setVR01(" ");
			bean.setUORG(" ");
			bean.setDIFF(" ");
			lRet.add(bean);
		}
		if (lRet.size() > 0)
			return lRet;
		else
			return null;
	}

	public boolean validItem() {
		// initTag();

		if (Integer.valueOf(tag.getCountnb().trim()) > 1) {
			return false;
		}
		if (!checkSN())
			return false;

		if (!checkUORG())
			return false;

		if (!checkExists())
			return false;

		return true;
	}

	private Boolean checkExists() {
		String sql = "select 1 from %s.F4311 inner join (select distinct ABDL10,ABVR01,ABCNID,ABKCOO,ABDCTO,ABDOCO from %s.F56B4310 where ABDL10='%s' and ABVR01='%s' and ABLITM='%s') V1 on PDDOCO=ABDOCO and PDDCTO=ABDCTO and PDKCOO=ABKCOO where PDLTTR!='980' and PDUOPN!=0";
		sql = String.format(sql, schema, schema, tag.getDl01(), tag.getVr01(), tag.getEntity().getLitm());
		log.info(sql);
		if (jdbcTemplate.queryForList(sql).size() > 0) {
			sql = "select * from "
					+ "(select sum(PDUOPN) PDUOPN,ABDL10,ABVR01,PDLITM,ABCNID from %s.F4311 inner join (select distinct ABLITM,ABDL10,ABVR01,ABCNID,ABKCOO,ABDCTO,ABDOCO from %s.F56B4310 where ABDL10='%s' and ABVR01='%s' and ABLITM='%s') V1 on PDDOCO=ABDOCO and PDDCTO=ABDCTO and PDKCOO=ABKCOO and ABLITM=PDLITM where PDLTTR!='980' and PDUOPN!=0 group by ABDL10,ABVR01,PDLITM,ABCNID) V3 "
					+ "left join "
					+ "(select sum(PLUORG) PLUORG,PLDL10,PLVR01,PLLITM,PLCNID from %s.F56B4311 where PLEV01!='3' and PLDL10='%s' and PLVR01='%s' and PLLITM='%s' and PLEV01!='4' and PLUORG!=0 group by PLDL10,PLVR01,PLLITM,PLCNID) V2 "
					+ "on ABDL10=PLDL10 and ABVR01=PLVR01 and PLLITM=PDLITM";

			sql = String.format(sql, schema, schema, tag.getDl01(), tag.getVr01(), tag.getEntity().getLitm(), schema,
					tag.getDl01(), tag.getVr01(), tag.getEntity().getLitm());
			log.info(sql);
			Double uorg = 0.0;
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
			if (list.size() > 0) {
				uorg = ((Double) list.get(0).get("PDUOPN") - (Double) list.get(0).get("PLUORG")) / 100;
				if (Integer.valueOf(tag.getCountnb().trim()) > uorg)
					return false;
				else {
					sql = "select sum(ABUORG)/100 PDUOPN,ABDL10,ABVR01,ABLITM,ABCNID from %s.F56B4310 where ABDL10='%s' and ABVR01='%s' and ABLITM='%s' group by ABDL10,ABVR01,ABLITM,ABCNID";
					sql = String.format(sql, schema, tag.getDl01(), tag.getVr01(), tag.getEntity().getLitm());
					log.info(sql);
					list = jdbcTemplate.queryForList(sql);
					if (Integer.valueOf(tag.getCountnb()) > (Double) list.get(0).get("PDUOPN"))
						return false;
				}
			} else
				return false;
		} else
			return false;

		return true;
	}

	private Boolean checkUORG() {
		String sql = "select sum(PLUORG)/100 PLUORG from %s.F56B4311 where PLLITM='%s' and PLLOT1='%s' and PLEV01!='4' and PLDL10='%s'";
		sql = String.format(sql, schema, tag.getEntity().getLitm(), tag.getEntity().getLot1(), tag.getDl01());
		log.info(sql);

		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		if (list.size() > 0) {
			Double uorg = ((Double) list.get(0).get("PLUORG"));
			if (uorg + Integer.valueOf(tag.getCountnb().trim()) > 1)
				return false;
		}
		return true;
	}

	private Boolean checkSN() {
		String sql = "select 1 from %s.F56B4310 where ABVR01='%s' and ABDL10='%s' and ABLITM='%s' and ABLOTN='%s'";
		sql = String.format(sql, schema, tag.getVr01(), tag.getDl01(), tag.getEntity().getLitm(),
				tag.getEntity().getLot1());
		log.info(sql);
		Integer count = jdbcTemplate.queryForObject(sql, new Object[] {}, Integer.class);
		if (count != 1)
			return false;
		return true;
	}
}
