package com.data.desensitization.service;

import java.util.ArrayList;

public interface DesensitizationAPI {
	// data desensitize algorithm API
	// parser the values according to the table field 
	String desensitize(String valuesStr, ArrayList<String> field);
	
	
	// truncated
	
	
	
	// mask shield
	boolean nameMask(String name, String output);
	boolean idCardMask(String idCard, String output);
	boolean addressMask(String address, String output);
	
	// random replacement
	
	
	
	// mapping replacement
	boolean nameReplace(String name, String output);
	
	
	// address offset
	
	
	
	// Rounding
	
	
	
}
