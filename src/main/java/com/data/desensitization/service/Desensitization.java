package com.data.desensitization.service;

import java.util.ArrayList;

public abstract class Desensitization implements DesensitizationAPI {

	@Override
	public String desensitize(String valuesStr, ArrayList<String> field) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean nameMask(String name, String output) {
		// TODO Auto-generated method stub
		return false;
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
