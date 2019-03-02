package com.data.desensitization.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.apache.commons.lang.StringUtils;

@Component
public class Desensitization implements DesensitizationAPI {

	@Autowired
	public static Desensitization desensitize;
	
	@PostConstruct
	public void init() {
		desensitize = this;
	}
	
	@Override
	public String desensitize(String valuesStr, List<String> field) {
		String result = "";
		// parser INSERT VALUES 
		for (int i = 0; i < field.size(); i++) {
			// check the table name
			if (field.get(i).contains("#")) {
				// split the field value
				List<String> values = new ArrayList<String>();
				int fromIndex = 0;
				while (valuesStr.indexOf("(", fromIndex) != -1) {
					values.add(valuesStr.substring(valuesStr.indexOf("(", fromIndex)+1, valuesStr.indexOf(")", fromIndex)));
					fromIndex = valuesStr.indexOf(")", fromIndex) + 1;
				}
				// match the values with the field
				List<String> matchValue = new ArrayList<String>();
				for (int j = 0; j < values.size(); j++) {
					result += doDesensitize(values.get(j), field);
				}
			}
		}
		return result;
	}

	public String doDesensitize(String str, List<String> field) {
		String result = "";
		String[] s = str.split(","); 
		for (int i = 0; i < field.size(); i++) {
			// execute the data algorithm
			if (field.get(i).contains("name")) {
				System.out.println(nameMask(s[i-1]));
			}
			/*if (field.get(i).contains("id")) {
				
			}*/
		
		}
		return result;
	}
	
	@Override
	public String nameMask(String fullName) {
		if (StringUtils.isEmpty(fullName)) {
			return "";
		}
		String fullNameTemp = fullName.substring(1, fullName.length()-1);
		String name = StringUtils.left(fullNameTemp, 1);
		
		return StringUtils.rightPad(name, StringUtils.length(fullNameTemp), "*");
	}

	@Override
	public boolean idCardMask(String idCard, String output) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addressMask(String address, String output) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean nameReplace(String name, String output) {
		// TODO Auto-generated method stub
		return false;
	}

}
