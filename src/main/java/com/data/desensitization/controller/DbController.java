package com.data.desensitization.controller;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Service;

@Service
public class DbController {
	// source database
	@Autowired
	private JdbcTemplate  jdbc;
	
	// target database
	//@Resource(name = "NamedParameterJdbcTemplate2")
	@Autowired
	private NamedParameterJdbcTemplate jdbc2;
	
	// select
	public List<Map<String, Object>> getData(String sql) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if (jdbc != null)
			list = jdbc.queryForList(sql);
		return list;
	}
	
	// total number
	public BigInteger getTotalNums(String sql) {
		Map<String, Object> map = new HashMap<String, Object>();
		BigInteger total;
		if (jdbc != null) {
			map = jdbc.queryForMap(sql);
		}
		total = (BigInteger) map.get("rows");
		return total;
	}
	
	// insert a new data
	/*public int insert(String table, Map<String, Object>list) {
		// parse the field and values
		Object key = "";
		Object value = "";
		Set<Entry<String, Object>> entries = list.entrySet();
		if(entries != null) {
			Iterator<Entry<String, Object>> iterator = entries.iterator();
			while(iterator.hasNext()) {
				Entry<String, Object> entry =(Entry<String, Object>) iterator.next();
				key += entry.getKey() + ",";
				value += "'" + entry.getValue() + "'" + ",";
        	}
			key = "(" + key.toString().substring(0, key.toString().length()-1) + ")";
			value = "(" + value.toString().substring(0, value.toString().length()-1) + ")";
       	}
		String field = "";
		String values = "";
		try {
			field = new String(key.toString().getBytes(), "utf-8");
			values = new String(value.toString().getBytes(), "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		table = "`" + table + "`";
		String sql = "insert into " + table + " " + field + " values " + values;
		//System.out.println(sql);
		return jdbc2.update(sql);
	}*/
	
	// get the table structure
	public List<Map<String, Object>> getTableStructure(String sql) {
		List<Map<String, Object>> map = new ArrayList<Map<String, Object>>();
		if (jdbc != null) {
			map = jdbc.queryForList(sql);
		}
		return map;
	}
	
	// insert with batch
	public void insertBatch(String table, List<Map<String, Object>> params, List<Map<String, Object>> tableStructure) {
		String fields = "";
		String values = "";
		for (int i = 0; i < tableStructure.size(); i++) {
			fields += tableStructure.get(i).get("column_name") + ",";
			values += ":" + tableStructure.get(i).get("column_name") + ",";
		}
		table = "`" + table + "`";
		fields = "(" + fields.substring(0, fields.toString().length()-1) + ")";
		values = "(" + values.substring(0, values.length()-1) + ")";
		String sql = "insert into " + table + " " + fields + " values " + values;
		// update the data with batch
		jdbc2.batchUpdate(sql, SqlParameterSourceUtils.createBatch(params.toArray()));
	}
	
	/**
	 * parse the longitude and latitude from the address 
	 * @param address
	 * @return
	 */
	public String getLngAndLat(Map<String, String> address) {
		String province = "", city = "", county = "", town = "", village = "";
		if (!address.isEmpty()) {
			village = address.get("village");
			if (village != null && village.length() != 0) {
				// send to MYSQL to get the longitude and latitude
				String data_village = getRow(village);
				if (data_village != "") {
					return data_village;
				}
			}
			town = address.get("town");
			if (town != null && town.length() != 0) {
				// send to MYSQL to get the longitude and latitude
				String data_town = getRow(town);
				if (data_town != "") {
					return data_town;
				}
			}
			county = address.get("country");
			if (county != null && county.length() != 0) {
				// send to MYSQL to get the longitude and latitude
				String data_county = getRow(county);
				if (data_county != "") {
					return data_county;
				}
			}
			city = address.get("city");
			if (city != null && city.length() != 0) {
				// send to MYSQL to get the longitude and latitude
				String data_city = getRow(city);
				if (data_city != "") {
					return data_city;
				}
			}
			province = address.get("province");
			if (province != null && province.length() != 0) {
				// send to MYSQL to get the longitude and latitude
				String data_province = getRow(province);
				if (data_province != "") {
					return data_province;
				}
			}
		}
		// no value, so return the default value
		return "116.23:39.54";
	}
	
	/**
	 * parse the address from longitude and latitude
	 * @param data
	 * @return
	 */
	/*public String getAddress(String data) {
		
	}*/
	
	public String getRow(String value) {
		String sql = "select BD09_LNG, BD09_LAT from t_md_areas where AREANAME regexp '" + value + "'";
		System.out.println(sql);
		Map<String, Object> map = new HashMap<String, Object>();
		if (jdbc != null) {
			map = jdbc.queryForMap(sql);
		}
		/*if (map.isEmpty()) {
			System.out.println("empty");
			return "";
		}*/
		for (String key : map.keySet())
			System.out.println(key  + ":" + map.get(key));
		return map.get("BD09_LNG").toString() + ":" + map.get("BD09_LAT").toString();
	}
}
