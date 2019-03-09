package com.data.desensitization.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
	// read the address code into array codes
	public void initAddressCode(String[] codes) {
		try {
			String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
			String encoding = "utf-8";
			File sqlFile = new File(path + "addressCode");
			FileInputStream fileInputStream = new FileInputStream(sqlFile);
			// convert the byte stream into character stream
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, encoding);
			// save file's context into words with ";"
			int str;
			String word = "";
			String temp = "";
			int i = 0;
			while ( (str = inputStreamReader.read()) > 0) {
				if (!((char)str == '\n')) {
					temp = (char)str + "";
					word = word + temp;
				} else {
					codes[i++] = word;
					word = "";
				}
			}
			inputStreamReader.close();
			fileInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// encode the address code
	public int encodeAddrCode(String addrCode, int length) {
		int S = 5; // !!! there are many methods to set the S 
		return (S * Integer.parseInt(addrCode)) % length;
	}
	
	// encode the date
	public String encodeDate(String date) {
		int S = 5;
		String result = "";
		try {
			result = CSTToUCT(UCTToCST(date).getTime() + (S * UCTToCST(date).getTime() % 1000));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	// calculate the sequence code
	public int calSeqCode(String seqCode) {
		int S = 5;
		return (Integer.parseInt(seqCode) * S) % 1000;
	}
	
	// calculate the check code
	public String calCheckCode(String newIdCard) {
		if (newIdCard.length() != 17) {
			return "-1";
		}
		
		Pattern pattern = Pattern.compile("^(\\d{6})(19|20)(\\d{2})(1[0-2]|0[1-9])(0[1-9]|[1-2][0-9]|3[0-1])(\\d{3})(\\d|X|x)?$");   
		Matcher matcher = pattern.matcher(newIdCard);
		
		if (!matcher.matches()) {
		      return "-1";
		}
		
		String matchDigit = "";
		// 1.将身份证号码前面的17位数分别乘以不同的系数。从第一位到第十七位的系数分别为：7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2  
		int[] intArr = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
		int sum = 0;
		for (int i = 0; i < intArr.length; i++) {
		// 2.将这17位数字和系数相乘的结果相加。  
			sum += Character.digit(newIdCard.charAt(i), 10) * intArr[i];
		}
	    // 3.用加出来和除以11，看余数是多少？  
		int mod = sum % 11;
	    // 4.余数只可能有0 1 2 3 4 5 6 7 8 9 10这11个数字。其分别对应的最后一位身份证的号码为1 0 X 9 8 7 6 5 4 3 2。  
		int[] intArr2 = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
		int[] intArr3 = { 1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2 };
	 
		for (int i = 0; i < intArr2.length; i++) {
			int j = intArr2[i];
	      if (j == mod) {
	        if (intArr3[i] > 57) {
	          matchDigit = String.valueOf((char) intArr3[i]);
	        } else {
	          matchDigit = String.valueOf(intArr3[i]);
	        }
	      }
	    }
		return matchDigit;
	}
	
	// UCT to CST
	public Date UCTToCST(String UCT) throws ParseException {
		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		date = sdf.parse(UCT);
		return date;
	}
	
	// CST to UCT
	public String CSTToUCT(long newTime) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(newTime);
		return dateFormat.format(cal.getTime()).toString();
	}
}
