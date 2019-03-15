package com.data.desensitization.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;



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
		// use the idCard[0:16] to multiply different numbers separately and the numbers from the first to the seventeenth is 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2  
		int[] intArr = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
		int sum = 0;
		for (int i = 0; i < intArr.length; i++) {
		// sum all the multiply results  
			sum += Character.digit(newIdCard.charAt(i), 10) * intArr[i];
		}
	    // use the sum result to divide 11 to get the remainder  
		int mod = sum % 11;
	    // the remainders are 0 1 2 3 4 5 6 7 8 9 10 and every remainder points to a idCard number:1 0 X 9 8 7 6 5 4 3 2  
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
	
	// convert the address into longitude and latitude
	public String convertAddr(String address) {
		BufferedReader in = null;
		String lng = "";
		String lat = "";
		try {
			address = URLEncoder.encode(address, "UTF-8");
			URL tirc = new URL("http://api.map.baidu.com/geocoder?address="+ address +"&output=json&key="+"7d9fbeb43e975cd1e9477a7e5d5e192a");  
         in = new BufferedReader(new InputStreamReader(tirc.openStream(),"UTF-8"));  
         String res;  
         StringBuilder sb = new StringBuilder("");  
         while((res = in.readLine())!=null){  
        	 	sb.append(res.trim());  
            }  
         String str = sb.toString();  
         if(StringUtils.isNotEmpty(str)) {  
            int lngStart = str.indexOf("lng\":");  
            int lngEnd = str.indexOf(",\"lat");  
            int latEnd = str.indexOf("},\"precise");  
            if(lngStart > 0 && lngEnd > 0 && latEnd > 0) {  
               lng = str.substring(lngStart+5, lngEnd);  
               lat = str.substring(lngEnd+7, latEnd);  
                }  
            }  
      } catch (Exception e) {  
            e.printStackTrace();  
      } finally {  
          try {  
            in.close();  
          } catch (IOException e) {  
            e.printStackTrace();  
             }  
         }
		return lng + ":" + lat;
	}
	
	// convert the longitude and latitude into new address
	public String convertPosition(double lng, double lat) throws MalformedURLException {
		String longitude = lng + "";
		String latitude = lat + "";
		BufferedReader in = null;
		String newAddr = "";
		URL tirc = new URL("http://api.map.baidu.com/geocoder?location="+ latitude+","+longitude+"&output=json&key="+"E4805d16520de693a3fe707cdc962045");  
		try {
			in = new BufferedReader(new InputStreamReader(tirc.openStream(),"UTF-8"));
			String res;  
		   StringBuilder sb = new StringBuilder("");  
		   while((res = in.readLine()) != null) {  
			   sb.append(res.trim());  
		    }  
		   String str = sb.toString();
		   ObjectMapper mapper = new ObjectMapper();
		   JsonNode locationNode = null;
		   JsonNode resultNode = null;
		   if(StringUtils.isNotEmpty(str)) {  
			   JsonNode jsonNode = mapper.readTree(str);
		      jsonNode.findValue("status").toString();
		      resultNode = jsonNode.findValue("result");
		      locationNode = resultNode.findValue("formatted_address");
		    }
		   newAddr = mapper.writeValueAsString(locationNode);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return newAddr;
	}
	
	// delete the first and the last characters 
	public String reMoveVar(String str, int begin, int end) {
		return str.substring(begin, str.length() - end);
	}
		
	public void getNameDatabase(String names[], String file) {
		try {
			String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
			String encoding = "utf-8";
			File sqlFile = new File(path + file);
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
					names[i++] = word;
					word = "";
				}
			}
			inputStreamReader.close();
			fileInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
