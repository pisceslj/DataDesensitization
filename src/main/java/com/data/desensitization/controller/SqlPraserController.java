package com.data.desensitization.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
				if (sqlsList.get(i).contains("CREATE TABLE")) {
					// save the table name
					String[] fields = sqlsList.get(i).split(" ");
					for (int j = 0; j < fields.length; j++) {
						if (fields[j].contains("CREATE") && fields[j+1].contains("TABLE")) {
							if (fields[j+2].startsWith("`") && fields[j+2].endsWith("`")) {
								TableFieldList.add(reMoveVar(fields[j+2], 1, 1) + "#");
							}
						}
						if (fields[j].contains("varchar") || fields[j].contains("int") 
								|| fields[j].contains("datetime") || fields[j].contains("text")) {
							// save the field name
							if (fields[j-1].startsWith("`") && fields[j-1].endsWith("`")) {
								TableFieldList.add(reMoveVar(fields[j-1], 1, 1));
							} else {
								break; // meet some pseudo match
							}
						}
					} // end for
				} // end if
				
				if (sqlsList.get(i).contains("INSERT INTO")) {
					// replace the values in the Insert SQL 
					String insert = sqlsList.get(i);
					String values = insert.substring(insert.indexOf("VALUES")+7);
					String output = desensitize.desensitize(values, TableFieldList);
					//WordsList.set(i+1, output);
					System.out.println(values);
					System.out.println(output);
				}
			}
			
			// test TableFieldList
			/*for (int k = 0; k < TableFieldList.size(); k++) {
				System.out.println(TableFieldList);
			}*/
			
			// split the TableFieldList into TableList and FieldList
			/*int Tab = 1;
			for (int i = 0; i < TableFieldList.size(); i++) {
				if (TableFieldList.get(i).contains("#")) {
					if (Tab == 1) {
						
					}
				}
			}
			*/
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	

	/*
	 * delete the first and the last characters 
	 * 
	 * */
	public static String reMoveVar(String str, int begin, int end) {
		return str.substring(begin, str.length() - end);
	}
	
	public static void main(String[] args) {
		SpringApplication.run(SqlPraserController.class);
	}
}
