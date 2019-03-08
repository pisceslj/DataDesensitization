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
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.data.desensitization.service.Desensitization;

@EnableAutoConfiguration
@RestController
public class SqlPraserController {
	@Autowired
	public static Desensitization desensitize = new Desensitization();
	
	@RequestMapping("/")
	public void fieldParsing() {
		// load the SQL file
		List<String> TableFieldList = new ArrayList<String>();  // save all the table name and all the field name
		List<String> sqlsList = new ArrayList<String>();    // save all the SQLs
		try {
			String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
			String encoding = "utf-8";
			File sqlFile = new File(path + "privateinfo.sql");
			FileInputStream fileInputStream = new FileInputStream(sqlFile);
			// convert the byte stream into character stream
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, encoding);
			// save SQL file's context into words with ";"
			int str;
			String word = "";
			String temp = "";
			while ( (str = inputStreamReader.read()) > 0) {
				if (!((char)str == ';')) {
					temp = (char)str + "";
					word = word + temp;
				} else {
					sqlsList.add(word);
					word = "";
				}
			}
			
			inputStreamReader.close();
			fileInputStream.close();
			
			// test sqlsList
			/*for (int i = 0; i < sqlsList.size(); i++) {
				System.out.println(sqlsList.get(i));
			}*/
			
			for (int i = 0; i < sqlsList.size(); i++) {
				/* 
				 * filter the CREATE sentence 
				 */
				if (sqlsList.get(i).contains("CREATE TABLE")) {
					// save the table name
					String[] fields = sqlsList.get(i).split(" "); // split the CREATE sentence with blank space
					String tablename = "";
					for (int j = 0; j < fields.length; j++) {
						if (fields[j].contains("CREATE") && fields[j+1].contains("TABLE")) {
							if (fields[j+2].startsWith("`") && fields[j+2].endsWith("`")) {
								tablename = reMoveVar(fields[j+2], 1, 1);
								TableFieldList.add(tablename + "#");
							}
						}
						// match the fields
						if (fields[j].contains("char") || fields[j].contains("varchar") || fields[j].contains("tinyblob") || 
								fields[j].contains("tinytext") || fields[j].contains("blob") || fields[j].contains("text") || 
								fields[j].contains("mediumblob") || fields[j].contains("mediumtext") || fields[j].contains("longblob") ||
								fields[j].contains("longtext") || fields[j].contains("tinyint") || fields[j].contains("smallint") || 
								fields[j].contains("mediumint") || fields[j].contains("int") || fields[j].contains("integer") ||
								fields[j].contains("bigint") || fields[j].contains("float") || fields[j].contains("double") || 
								fields[j].contains("date") || fields[j].contains("time") || fields[j].contains("year") || 
								fields[j].contains("datetime") || fields[j].contains("timestamp")) {
							// save the field name
							if (fields[j-1].startsWith("`") && fields[j-1].endsWith("`")) {
								TableFieldList.add(reMoveVar(fields[j-1], 1, 1));
							} else {
								break; // meet some pseudo match
							}
						}
					} // end for
					TableFieldList.add("##" + tablename + "##");  // set the flag which points to the end of the table's fields 
				} // end if sentence which matches the CREATE TABLE
				
				/*
				 * replace the values in the Insert SQL 
				 */
				List<String> TableFieldSubList = new ArrayList<String>();  // save the matched table's name and fields
				if (sqlsList.get(i).contains("INSERT INTO")) {
					String tableName = sqlsList.get(i).split(" ")[2];
					int fromIndex = TableFieldList.indexOf(reMoveVar(tableName, 1, 1)+"#");
					int toIndex = TableFieldList.indexOf("##" + reMoveVar(tableName, 1, 1) + "##");
					TableFieldSubList = TableFieldList.subList(fromIndex, toIndex);
					String keywords = sqlsList.get(i).substring(0, sqlsList.get(i).indexOf("VALUES")+7);
					String values = sqlsList.get(i).substring(sqlsList.get(i).indexOf("VALUES")+7);
					String output = desensitize.desensitize(values, TableFieldSubList);
					sqlsList.set(i, keywords + output);
				}
				
				/*
				 *  replace the values in the Update SQL
				 */
				if (sqlsList.get(i).contains("UPDATE")) {
					
				}
			} // end SQLs for
			
			// test sqlsList after data operation
			/*for (int i = 0; i < sqlsList.size(); i++) {
				System.out.println(sqlsList.get(i));
			}*/
			
			// save the sqlsList to a SQL file
			for (int i = 0; i < sqlsList.size(); i++) {
				sqlsList.set(i, sqlsList.get(i) + ";");
			}
			try {
				writeNewSQLFile(sqlsList, path);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// write the list values into a new SQL file
	public static void writeNewSQLFile(List<String> sqls, String sqlPath) throws Exception {
		String encode = "utf-8";
		Date d = new Date();
		try {
			String newFile = sqlPath + d.getTime()+".sql";
			File file = new File(newFile);
			if (!file.exists()) {
				file.createNewFile();
			}
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(newFile), encode));
			for (String l:sqls) {
				System.out.println(l);
				writer.write(l + "\r\n");
			}
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// delete the first and the last characters 
	public static String reMoveVar(String str, int begin, int end) {
		return str.substring(begin, str.length() - end);
	}
	
	public static void main(String[] args) {
		SpringApplication.run(SqlPraserController.class);
	}
}
