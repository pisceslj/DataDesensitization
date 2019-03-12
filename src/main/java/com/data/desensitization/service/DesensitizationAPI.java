package com.data.desensitization.service;

import java.util.ArrayList;
import java.util.List;

public interface DesensitizationAPI {
	// data algorithm API
	// parser the values according to the table field 
	String desensitize(String valuesStr, List<String> field);
	
	
	// truncated
	
	
	
	// mask shield
	String nameMask(String name);
	String idCardMask(String idCard);
	String phoneMask(String phone);
	String telMask(String tel);
	String emailMask(String email);
	String addressMask(String address, int sensitiveSize);
	
	// random replacement
	
	
	
	// mapping replacement
	String nameMapReplace(String name);
	String idCardMapReplace(String idCard);
	
	String addressMapReplace(String adress, int select);
	
	// address offset
	
	
	
	// Rounding
	
	
	
}
