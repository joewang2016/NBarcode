package com.el.ks.barcode.controller;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.el.ks.barcode.bean.F0101Bean;
import com.el.ks.barcode.bean.F0111Bean;
import com.el.ks.barcode.bean.F01151Bean;
import com.el.ks.barcode.bean.F0115Bean;
import com.el.ks.barcode.bean.F0116Bean;
import com.el.ks.barcode.bean.JDEBeanFacoty;
import com.el.ks.barcode.config.TagConfig;
import com.el.ks.barcode.util.RedisUtil;
import com.zaxxer.hikari.HikariDataSource;

@Controller
public class BackupController {
	Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private TagConfig config;

	@Autowired
	private RedisUtil util;

	@Autowired
	private HikariDataSource datasource;

	private String schema;

	
	@PostConstruct
	public void init(){
		schema = datasource.getSchema();
	}
	
	@RequestMapping("backup")
	@ResponseBody
	public String Backup(String table) {
		
		String result = "";
		List<String> columns = new ArrayList<String>();

		String sql = "select column_name from information_schema.columns where TABLE_SCHEMA='%s' and TABLE_NAME='%s' ";
		sql = String.format(sql, schema, table);
		log.info(sql);

		jdbcTemplate.query(sql, new RowCallbackHandler() {

			@Override
			public void processRow(ResultSet rs) throws SQLException {
				// TODO Auto-generated method stub
				String columnName = rs.getString(1);
				// log.info(columnName);
				columns.add(columnName);
			}
		});
		switch (table) {
		case "F0101": {
			sql = "select ABAN8,ABALPH from %s.F0101";
			sql = String.format(sql, schema);
			JDEBeanFacoty factory = JDEBeanFacoty.getInstance();

			HashMap<BigDecimal, F0101Bean> mapResult = new HashMap<BigDecimal, F0101Bean>();
			jdbcTemplate.query(sql, new RowCallbackHandler() {
				F0101Bean f0101 = null;

				@Override
				public void processRow(ResultSet rs) throws SQLException {
					// TODO Auto-generated method stub
					BigDecimal key = rs.getBigDecimal("ABAN8");
					if (!mapResult.containsKey(key)) {
						f0101 = (F0101Bean) factory.CreateJDE("F0101");
						f0101.setAn8(rs.getBigDecimal("ABAN8"));
						f0101.setAlph(rs.getString("ABALPH"));
						mapResult.put(key, f0101);
					}
				}

			});

			sql = "select WWAN8,WWIDLN,WWMLNM from %s.F0111";
			sql = String.format(sql, schema);
			jdbcTemplate.query(sql, new RowCallbackHandler() {
				F0111Bean f0111 = null;
				F0101Bean f0101 = null;

				@Override
				public void processRow(ResultSet rs) throws SQLException {
					// TODO Auto-generated method stub
					BigDecimal key = rs.getBigDecimal("WWAN8");
					f0111 = (F0111Bean) factory.CreateJDE("F0111");
					f0111.setIdln(rs.getBigDecimal("WWIDLN"));
					f0111.setMlnm(rs.getString("WWMLNM"));
					if (mapResult.containsKey(key)) {
						f0101 = mapResult.get(key);
						f0101.getF0111().add(f0111);
					}
				}

			});

			sql = "select WPAN8,WPIDLN,WPRCK7,WPPHTP,WPAR1,WPPH1 from %s.F0115";
			sql = String.format(sql, schema);
			jdbcTemplate.query(sql, new RowCallbackHandler() {
				F0115Bean f0115 = null;
				F0101Bean f0101 = null;

				@Override
				public void processRow(ResultSet rs) throws SQLException {
					// TODO Auto-generated method stub
					BigDecimal key = rs.getBigDecimal("WPAN8");
					f0115 = (F0115Bean) factory.CreateJDE("F0115");
					f0115.setIdln(rs.getBigDecimal("WPIDLN"));
					f0115.setRck7(rs.getBigDecimal("WPRCK7"));
					f0115.setPhtp(rs.getString("WPPHTP"));
					f0115.setAr1(rs.getString("WPAR1"));
					f0115.setPh1(rs.getString("WPPH1"));
					if (mapResult.containsKey(key)) {
						f0101 = mapResult.get(key);
						f0101.getF0115().add(f0115);
					}
				}

			});

			sql = "select EAAN8,EAIDLN,EARCK7,EAEMAL from %s.F01151";
			sql = String.format(sql, schema);
			jdbcTemplate.query(sql, new RowCallbackHandler() {
				F01151Bean f01151 = null;
				F0101Bean f0101 = null;

				@Override
				public void processRow(ResultSet rs) throws SQLException {
					// TODO Auto-generated method stub
					BigDecimal key = rs.getBigDecimal("EAAN8");
					f01151 = (F01151Bean) factory.CreateJDE("F01151");
					f01151.setIdln(rs.getBigDecimal("EAIDLN"));
					f01151.setRck7(rs.getBigDecimal("EARCK7"));
					f01151.setEmal(rs.getString("EAEMAL"));
					if (mapResult.containsKey(key)) {
						f0101 = mapResult.get(key);
						f0101.getF01151().add(f01151);
					}
				}

			});

			sql = "select ALAN8,ALADD1,ALADD2,ALADD3,ALADD4 from %s.F0116";
			sql = String.format(sql, schema);
			jdbcTemplate.query(sql, new RowCallbackHandler() {
				F0116Bean f0116 = null;
				F0101Bean f0101 = null;

				@Override
				public void processRow(ResultSet rs) throws SQLException {
					// TODO Auto-generated method stub
					BigDecimal key = rs.getBigDecimal("ALAN8");
					f0116 = (F0116Bean) factory.CreateJDE("F0116");
					f0116.setAdd1(rs.getString("ALADD1"));
					f0116.setAdd1(rs.getString("ALADD1"));
					f0116.setAdd1(rs.getString("ALADD1"));
					f0116.setAdd1(rs.getString("ALADD1"));
					if (mapResult.containsKey(key)) {
						f0101 = mapResult.get(key);
						f0101.getF0116().add(f0116);
					}
				}

			});
			for (Entry ele : mapResult.entrySet()) {
				String key = ((BigDecimal) ele.getKey()).toString();
				F0101Bean value = (F0101Bean) ele.getValue();
				util.hset("JDE", key, value);
			}
			System.out.println("Finished");
			break;
		}
		}
		return result;
	}
}
