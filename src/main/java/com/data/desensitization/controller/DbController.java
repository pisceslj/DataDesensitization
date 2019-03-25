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
import org.springframework.stereotype.Service;

@Service
public class DbController {
	// source database
	@Autowired
	private JdbcTemplate  jdbc;
	
	// target database
	@Resource(name = "jdbcTemplate2")
	private JdbcTemplate jdbc2;
	
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
	public int insert(String table, Map<String, Object>list) {
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
	}
}
