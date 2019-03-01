package com.data.desensitization.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableAutoConfiguration
@RestController
public class SqlPraserController {
	
	@RequestMapping("/")
	public void fieldParsing() {
		// load the SQL file
		List<String> WordsList = new ArrayList<String>();  // save all the words
		List<String> TableFieldList = new ArrayList<String>();  // save all the table name and all the field name
		List<String> DataTableList = new ArrayList<String>();  // save all the data table values
		try {
			String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
			String encoding = "utf-8";
			File sqlFile = new File(path + "privateinfo.sql");
			FileInputStream fileInputStream = new FileInputStream(sqlFile);
			// convert the byte stream into character stream
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, encoding);
			// save SQL file's context into words
			int str;
			String word = "";
			String temp = "";
			while ( (str = inputStreamReader.read()) > 0) {
				if (!((char)str == ' ')) {
					temp = (char)str + "";
					word = word + temp;
				} else {
					WordsList.add(word);
					word = "";
				}
			}
			inputStreamReader.close();
			fileInputStream.close();
			
			// test WordsList
			/*for (int i = 0; i < WordsList.size(); i++) {
				System.out.println(WordsList.get(i).toString());
			}*/
			
			// filter the key words
			for (int i = 0; i < WordsList.size(); i++) {
				if (WordsList.get(i).contains("CREATE") && WordsList.get(i+1).contains("TABLE")) {
					// save the table name
					TableFieldList.add(reMoveVar(WordsList.get(i+2)) + "#");
				}
				if (WordsList.get(i).contains("varchar")|| WordsList.get(i).contains("int") 
						|| WordsList.get(i).contains("datetime") || WordsList.get(i).contains("text")) {
					// save the <field name, comment>
					String field = WordsList.get(i-1).toString();
					for (int j = i+1; !WordsList.get(j).contains(","); j++) {
						if (WordsList.get(j).contains("COMMENT")) {
							String temp
							field = WordsList.get(j+1).substring(1, ); 
						}
					}
					TableFieldList.add(field);
				}
				
				// replace the values in the Insert SQL 
				if (WordsList.get(i).contains("VALUES")) {
					
				}
			}
			
			// test TableFieldList
			for (int i = 0; i < TableFieldList.size(); i++) {
				System.out.println(TableFieldList.get(i));
			}
			
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
	public static String reMoveVar(String str) {
		return str.substring(1, str.length() - 1);
	}
	
	public static void main(String[] args) {
		SpringApplication.run(SqlPraserController.class);
	}
}
