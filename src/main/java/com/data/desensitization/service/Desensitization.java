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
					result = result + "(" + doDesensitize(values.get(j), field) + ")" + ",";
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
				s[i-1] = "'" + nameMask(s[i-1]) + "'";
			}
			if (field.get(i).contains("idCard")) {
				s[i-1] = "'" + idCardMask(s[i-1]) + "'";
			}
			if (field.get(i).contains("phone")) {
				s[i-1] = "'" + phoneMask(s[i-1]) + "'";
			}
			if (field.get(i).contains("tel")) {
				s[i-1] = "'" + telMask(s[i-1]) + "'";
			}
			if (field.get(i).contains("email")) {
				s[i-1] = "'" + emailMask(s[i-1]) + "'";
			}
			if (field.get(i).contains("addr")) {
				int index = s[i-1].indexOf("路");
				s[i-1] = "'" + addressMask(s[i-1], index) + "'";
			}
		}
		int j;
		for (j = 0; j < s.length; j++) {
			result = result + s[j] + ",";
		}
		//result += s[j];
		
		return result;
	}
	
	@Override
	public String nameMask(String fullName) {
		if (StringUtils.isBlank(fullName)) {
			return "";
		}
		String fullNameTemp = fullName.substring(1, fullName.length()-1);
		String name = StringUtils.left(fullNameTemp, 1);
		
		return StringUtils.rightPad(name, StringUtils.length(fullNameTemp), "*");
	}

	@Override
	public String idCardMask(String idCard) {
		if (StringUtils.isBlank(idCard)) {
			return "";
		}
		String idCardTemp = idCard.substring(1, idCard.length()-1);
		String idcard = StringUtils.right(idCardTemp, 4);
		
		return StringUtils.leftPad(idcard, StringUtils.length(idCardTemp), "*");
	}

	@Override
	public String phoneMask(String phone) {
		if (StringUtils.isBlank(phone)) {
			return "";
		}
		String phoneTemp = phone.substring(1, phone.length()-1);
		
		return StringUtils.left(phoneTemp, 3).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(phoneTemp, 4), StringUtils.length(phoneTemp), "*"), "***"));
	}

	@Override
	public String telMask(String tel) {
		if (StringUtils.isBlank(tel)) {
			return "";
		}
		String telTemp = tel.substring(1, tel.length()-1);
		
		return StringUtils.leftPad(StringUtils.right(telTemp, 4), StringUtils.length(telTemp), "*");
	}
	
	@Override
	public String emailMask(String email) {
		if (StringUtils.isBlank(email)) {
			return "";
		}
		String emailTemp = email.substring(1, email.length()-1);
		int index = StringUtils.indexOf(emailTemp, "@");
		if (index <= 1) {
			return emailTemp;
		} else {
			return StringUtils.rightPad(StringUtils.left(emailTemp, 1), index, "*").concat(StringUtils.mid(emailTemp, index, StringUtils.length(emailTemp)));
		}
	}
	
	@Override
	public String addressMask(String address, int index) {
		if (StringUtils.isBlank(address)) {
			return "";
		}
		String addressTemp = address.substring(1, address.length()-1);
		int length = StringUtils.length(addressTemp);
		return StringUtils.rightPad(StringUtils.left(addressTemp, index), length, "*");
	}

	@Override
	public boolean nameReplace(String name, String output) {
		// TODO Auto-generated method stub
		return false;
	}

}
