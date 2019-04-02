package com.data.desensitization.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface DesensitizationAPI {
	// data algorithm API
	// parser the values according to the table field 
	Map<String, Object> desensitize(Map<String, Object>list);
	
	
	// truncated
	
	
	
	// mask shield
	String nameMask(String name);
	String idCardMask(String idCard);
	String phoneMask(String phone);
	String telMask(String tel);
	String emailMask(String email);
	String addressMask(String address);
	
	// random replacement
	
	
	
	// mapping replacement
	String nameMapReplace(String name);
	String idCardMapReplace(String idCard);
	
	String addressMapReplace(String adress, int select);
	
	// address offset
	
	
	
	// Rounding
	
	
	
}
