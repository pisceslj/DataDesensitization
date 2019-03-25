package com.data.desensitization.service;

import java.io.IOException;
import java.sql.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.data.desensitization.service.datastructure.BPlusTree;

import org.apache.commons.lang.StringUtils;

@Component
public class Desensitization implements DesensitizationAPI {

	@Autowired
	public static Desensitization desensitize;
	
	public static Utils utils = new Utils();
	
	@Override
	public Map<String, Object> desensitize(Map<String, Object>list) {
		Object key = null;
		Object value = null;
		Map<String, String> classes = new HashMap<String, String>();
		utils.initFieldClasses(classes);
		Set<Entry<String, Object>> entries = list.entrySet();
		if(entries != null) {
			Iterator<Entry<String, Object>> iterator = entries.iterator();
			while(iterator.hasNext()) {
				Entry<String, Object> entry =(Entry<String, Object>) iterator.next();
				key = entry.getKey();
				value = entry.getValue();
				
				for (String k : classes.keySet()) {
					String[] v = classes.get(k).split(",");
					List<String> fieldList = Arrays.asList(v);
					// execute the data algorithm
					if (k.contains("NAME") && fieldList.contains(key)) {
						value = nameMask(value.toString());
						//value = nameMapReplace(value.toString());
						break;
					}
					if (k.contains("IDCARD") && fieldList.contains(key)) {
						value = idCardMask(value.toString());
						//value = idCardMapReplace(value.toString());
						break;
					}
					if (k.contains("PHONE") && fieldList.contains(key)) {
						value = phoneMask(value.toString());
						break;
					}
					if (k.contains("TELEPHONE") && fieldList.contains(key)) {
						value = telMask(value.toString());
						break;
					}
					if (k.contains("EMAIL") && fieldList.contains(key)) {
						value = emailMask(value.toString());
						break;
					}
					if (k.contains("ADDRESS") && fieldList.contains(key)) {
						//int index = value.toString().indexOf("路");
						//value = addressMask(value.toString(), index);
						value = addressMapReplace(value.toString(), 1); // select the first method
						break;
					}
				}
				entry.setValue(value);
			}
       }// end of if
		return list;
	}
	
	/*
	 * mask 
	 */
	@Override
	public String nameMask(String fullName) {
		if (StringUtils.isBlank(fullName)) {
			return "";
		}
		String name = StringUtils.left(fullName, 1);
		
		return StringUtils.rightPad(name, StringUtils.length(fullName), "*");
	}

	@Override
	public String idCardMask(String idCard) {
		if (StringUtils.isBlank(idCard)) {
			return "";
		}
		String idcard = StringUtils.right(idCard, 4);
		
		return StringUtils.leftPad(idcard, StringUtils.length(idCard), "*");
	}

	@Override
	public String phoneMask(String phone) {
		if (StringUtils.isBlank(phone)) {
			return "";
		}
		
		return StringUtils.left(phone, 3).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(phone, 4), StringUtils.length(phone), "*"), "***"));
	}

	@Override
	public String telMask(String tel) {
		if (StringUtils.isBlank(tel)) {
			return "";
		}
		
		return StringUtils.leftPad(StringUtils.right(tel, 4), StringUtils.length(tel), "*");
	}
	
	@Override
	public String emailMask(String email) {
		if (StringUtils.isBlank(email)) {
			return "";
		}
		
		int index = StringUtils.indexOf(email, "@");
		if (index <= 1) {
			return email;
		} else {
			return StringUtils.rightPad(StringUtils.left(email, 1), index, "*").concat(StringUtils.mid(email, index, StringUtils.length(email)));
		}
	}
	
	@Override
	public String addressMask(String address, int index) {
		if (StringUtils.isBlank(address)) {
			return "";
		}
		int length = StringUtils.length(address);
		return StringUtils.rightPad(StringUtils.left(address, index), length, "*");
	}

	/*
	 * mapping replace 
	 */
	@Override
	public String nameMapReplace(String name) {
		if (StringUtils.isBlank(name)) {
			return "";
		}
		int size1 = 101;
		int size2 = 134; 
		int order = 4;
		String[] nameDB = new String[101];
		String[] names = new String[101];
		Double[] proportion = new Double[101];
		String[] firstnameDB = new String[134];
		String[] firstnames = new String[134];
		Double[] firstProportion = new Double[134];
		//get the nameDatabase and split the name and its proportion
		utils.getNameDatabase(nameDB, "surname");
		DecimalFormat df = new DecimalFormat("0.000000");
		for (int i = 0; i < nameDB.length; i++) {
			if (nameDB[i] != null) {
				names[i] = nameDB[i].split(" ")[0];
				proportion[i] = Double.valueOf(df.format(Double.valueOf(nameDB[i].split(" ")[1])));
			}
		}
		// initialize the B+ Tree with the surname's proportion and the first name's proportion
		BPlusTree<Double, String> tree1 = new BPlusTree<Double, String>(order);
		for (int i = 0; i < size1; i++) {
			if (proportion[i] != null && names[i] != null) {
				tree1.insert(proportion[i], names[i]);
			}
		}
		// change the surname randomly
		double randomNumber1 = Math.random();
		randomNumber1 = Double.valueOf(df.format(Double.valueOf(randomNumber1)));
		String surname = tree1.get(randomNumber1);
		
		// get the firstname and split the first name and its proportion
		utils.getNameDatabase(firstnameDB, "firstname");
		for (int i = 0; i < firstnameDB.length; i++) {
			if (firstnameDB[i] != null) {
				firstnames[i] = firstnameDB[i].split(" ")[0];
				firstProportion[i] = Double.valueOf(df.format(Double.valueOf(firstnameDB[i].split(" ")[1])));
			}
		}
		// initialize the B+ Tree with the first name's proportion
		BPlusTree<Double, String> tree2 = new BPlusTree<Double, String>(order);
		for (int j = 0; j < size2; j++) {
			if (firstProportion[j] != null && firstnames[j] != null) {
				tree2.insert(firstProportion[j], firstnames[j]);
			}
		}
		// change the first name randomly
		// according to the length of the name
		String firstname = "";
		for (int k = 1; k < name.length(); k++) {
			double randomNumber2 = Math.random();
			randomNumber2 = Double.valueOf(df.format(Double.valueOf(randomNumber2)));
			firstname += tree2.get(randomNumber2);
		}
		
		return surname+firstname;
	}

	@Override
	public String addressMapReplace(String address, int select) {
		// convert the address into longitude and latitude
		String result = utils.convertAddr(address);
		double lng = Float.parseFloat(result.split(":")[0]);
		double lat = Float.parseFloat(result.split(":")[1]);
		// select the algorithm to deal with longitude and latitude
		switch(select) {
		case 1:
			// data jitter: 1 degree is equal to 111 kilometers
			lng = lng + 1.1;
			lat = lat + 1.1;
			if (lng < 73.66 || lng > 135.05) {
				lng = lng * 0.95;
			}
			if (lat < 3.86 || lat > 53.55) {
				lat = lat * 0.95;
			}
			break;
		case 2:
			// random exchange
			break;
		case 3:
			// fuzzification
			break;
		}
		String newAddress = "";
		try {
			newAddress = utils.convertPosition(lng, lat);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return utils.reMoveVar(newAddress, 1, 1)  +  "******";
	}
	
	@Override
	public String idCardMapReplace(String idCard) {
		if (StringUtils.isBlank(idCard)) {
			return "";
		}
		String[] codes = new String[3465]; // 3465
		// initialize the address code
		utils.initAddressCode(codes);
		// create a key S to encode the true address code
		String newAddr = codes[utils.encodeAddrCode(idCard.substring(0, 6), codes.length)];
		String newAddrCode = "";
		if (newAddr.length() > 0) {
			newAddrCode = newAddr.split(" ")[0];
		}
		// change the date
		String newDate = utils.encodeDate(idCard.substring(6, 14)) + "";
		// calculate the new sequence code
		String newSeqCode = utils.calSeqCode(idCard.substring(14, 17)) + "";
		// calculate the new check code
		String newCheckCode = utils.calCheckCode(newAddrCode + newDate + newSeqCode);
		
		return newAddrCode + newDate + newSeqCode + newCheckCode;
	}
}
