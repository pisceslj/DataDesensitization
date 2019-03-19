package com.data.desensitization.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.data.desensitization.service.Desensitization;
import com.data.desensitization.service.Utils;

@SpringBootApplication(scanBasePackages = "controller")
@EnableAutoConfiguration
@RestController
@RequestMapping("/sql")
public class SqlPraserController {
	@Autowired
	public static Desensitization desensitize = new Desensitization();
	
	@Autowired
	public DbController db;
	
	public static Utils utils = new Utils();
	
	@Value("${src.database}")
	private String database;
	
	@RequestMapping("/parse")
	public void fieldParsing() {
		// config the change object
		// get all the tables' name from the source database
		String FindTables = "select table_name from information_schema.tables where table_schema='"+database+"'";
		List<Map<String, Object>> tables = db.getData(FindTables);
		
		for (Map<String, Object> map : tables) {
			Object value = null;
			Set<Entry<String, Object>> entries = map.entrySet();
         if(entries != null) {
        	 	Iterator<Entry<String, Object>> iterator = entries.iterator();
            while(iterator.hasNext()) {
            	Entry<String, Object> entry =(Entry<String, Object>) iterator.next();
               value = entry.getValue();
            	}
	       	}
         String FindData = "select * from "+value;
         List<Map<String, Object>> lists = db.getData(FindData);
         for (Map<String, Object> list : lists) {
        	 	Map<String, Object> newData = desensitize.desensitize(list);
        	 	db.insert(value.toString(), newData);
         	}
		}				
	}
}
