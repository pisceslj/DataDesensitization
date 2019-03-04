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
			/*int str;
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
			}*/
			
			// cut into string with ";"
			int str2;
			String word2 = "";
			String temp2 = "";
			List<String> sqlsList = new ArrayList<String>();
			while ( (str2 = inputStreamReader.read()) > 0) {
				if (!((char)str2 == ';')) {
					temp2 = (char)str2 + "";
					word2 = word2 + temp2;
				} else {
					sqlsList.add(word2);
					word2 = "";
				}
			}
			
			inputStreamReader.close();
			fileInputStream.close();
			
			// test WordsList
			for (int i = 0; i < sqlsList.size(); i++) {
				System.out.println(sqlsList.get(i));
			}
			
			// filter the key words
			/*for (int i = 0; i < WordsList.size(); i++) {
				if (WordsList.get(i).contains("CREATE") && WordsList.get(i+1).contains("TABLE")) {
					// save the table name
					TableFieldList.add(reMoveVar(WordsList.get(i+2), 1, 1) + "#");
				}
				if (WordsList.get(i).contains("varchar")|| WordsList.get(i).contains("int") 
						|| WordsList.get(i).contains("datetime") || WordsList.get(i).contains("text")) {
					// save the <field name, comment>
					String field = reMoveVar(WordsList.get(i-1), 1, 1);
					for (int j = i+1; !WordsList.get(j).contains(","); j++) {
						if (WordsList.get(j).contains("COMMENT")) {
							field = "<" + field + "," + reMoveVar(WordsList.get(j+1), 1, 4) + ">"; 
						}
					}
					TableFieldList.add(field);
				}
				
				// replace the values in the Insert SQL 
				String output = "";
				String valueTemp = "";
				if (WordsList.get(i).contains("VALUES")) {
					int k = i;
					while (!(WordsList.get(k+1).contains(";"))) {
						valueTemp += WordsList.get(k+1);
						valueTemp += " ";
						++k;
					}
					valueTemp += WordsList.get(k+1);
					//System.out.println(WordsList.get(k+1));
					output = desensitize.desensitize(valueTemp, TableFieldList);
					WordsList.set(i+1, output);
					//System.out.println(valueTemp);
					//System.out.println(output);
				}
			}*/
			
			// test TableFieldList
			/*for (int i = 0; i < TableFieldList.size(); i++) {
				System.out.println(TableFieldList.get(i));
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
