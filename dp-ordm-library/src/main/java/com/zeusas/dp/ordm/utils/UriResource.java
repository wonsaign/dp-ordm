package com.zeusas.dp.ordm.utils;

import java.util.ResourceBundle;

public class UriResource {

	final static ResourceBundle user_resource = ResourceBundle.getBundle("UriResource");

	public static String get(String key) {
		return user_resource.getString(key);
	}
}
