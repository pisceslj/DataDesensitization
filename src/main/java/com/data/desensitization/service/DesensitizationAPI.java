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
	boolean idCardMask(String idCard, String output);
	boolean addressMask(String address, String output);
	
	// random replacement
	
	
	
	// mapping replacement
	boolean nameReplace(String name, String output);
	
	
	// address offset
	
	
	
	// Rounding
	
	
	
}
