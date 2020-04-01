package com.el.ks.barcode.model;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Component;

import com.el.ks.barcode.bean.TagBean;
import com.el.ks.barcode.bean.TagDetailBean;
import com.el.ks.barcode.bean.TagScanResultBean;
import com.el.ks.barcode.config.BarcodeConfig;
import com.el.ks.barcode.service.TagDetail;
import com.el.ks.barcode.util.DateFormat;
import com.el.ks.barcode.util.DateTool;
import com.el.ks.barcode.util.RedisUtil;

import lombok.Data;

@Data
@Component
public class ChinaTagModel {

	Logger log = Logger.getLogger(this.getClass());

	private BarcodeConfig config;
	private RedisUtil util;

	private JdbcTemplate jdbcTemplate;

	@SuppressWarnings({ "unused", "unchecked" })
	public List<TagScanResultBean> GetLicenseList(TagBean bean) {
		bean.setMessage(bean.getMessage().replace("^", "("));
		List<TagScanResultBean> lresult = new ArrayList<TagScanResultBean>();

		List storeResult = ExecStoreGetLicenseStatus(bean);

		if (storeResult.size() > 0) {
			Map row = (HashMap) storeResult.get(0);
			String CFDAStus = (String) row.get("CFDAStus");

			String sql = "select RHLOTN,RHALPH,RDAITM,RDLITM,RDADD4,RHEFFT,RDSRP6 from %s.F56B4310 inner join %s.F550002 on ABDL01=RDLOTN and ABLITM=RDLITM "
					+ "inner join %s.F550001 on RDLOTN=RHLOTN where ABDL10='%s' and ABVR01='%s' and ABLITM='%s' and RDSRP6='%s'";
			sql = String.format(sql, config.getSchema(), config.getSchema(), config.getSchema(), bean.getDl01(),
					bean.getVr01(), bean.getEntity().getLitm(), CFDAStus);
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
	private List ExecStoreGetLicenseStatus(TagBean bean) {
		// TODO Auto-generated method stub
		List list = (List) jdbcTemplate.execute(new CallableStatementCreator() {

			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				// TODO Auto-generated method stub
				String sql = "execute %s.BarcodeLabel ?,?,?,?";
				sql = String.format(sql, config.getSchema());
				log.info(sql);

				CallableStatement cs = con.prepareCall(sql);
				cs.setDate(1, new java.sql.Date(DateTool.parseDate(bean.getDate(), "yyyy-MM-dd").getTime()));
				cs.setString(2, bean.getEntity().getLitm());
				cs.setString(3, bean.getVr01());
				cs.setString(4, bean.getDl01());
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

	public void SaveScanData(TagBean tag, TagScanResultBean scan) {
		// TODO Auto-generated method stub
		String message = tag.getMessage();
		tag.setMessage(message.replace("^", "("));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<TagDetailBean> GetTagDetail(TagBean bean) {

		List lstatus = ExecStoreGetLicenseStatus(bean);
		Map row = (HashMap) lstatus.get(0);
		String CFDAID = (String) row.get("CFDAID");
		String CFDAStus = (String) row.get("CFDAStus");
		String LabelTy = (String) row.get("LabelTy");

		String szLotn = "";
		if (CFDAID.compareTo(" ") > 0)
			szLotn = "RDLOTN=N'" + CFDAID + "'";
		else
			szLotn = "1=1";

		String sql = "select * from %s.F550002 inner join %s.F550001 on RDLOTN=RHLOTN inner join %s.F4101 on RDITM=IMITM where RDLITM='%s' "
				+ "and %s and RDSRP6='%s'";
		sql = String.format(sql, config.getSchema(), config.getSchema(), config.getSchema(), bean.getEntity().getLitm(),
				szLotn, CFDAStus);
		log.info(sql);

		List<TagDetailBean> lresult = (List<TagDetailBean>) jdbcTemplate.execute(sql, new CallableStatementCallback() {

			@Override
			public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
				// TODO Auto-generated method stub
				List result = new ArrayList();
				cs.execute();
				ResultSet rs = cs.executeQuery();
				if (rs.next()) {
					TagDetailBean bean = new TagDetailBean();
					getTagDetail(rs, bean);
					log.info(bean);
					result.add(bean);
				}
				rs.close();
				return result;
			}
		});

		return lresult;
	}

	private void getTagDetail(ResultSet rs, TagDetailBean bean) {
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
						method.invoke(bean, value);
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
