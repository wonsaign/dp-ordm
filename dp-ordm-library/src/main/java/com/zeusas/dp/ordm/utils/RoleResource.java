package com.zeusas.dp.ordm.utils;

import java.util.ResourceBundle;

public final class RoleResource {
	/** 前台禁止登陆角色 */
	public final static String FOREGROUND = "foreground";
	/** 后台禁止登陆角色 */
	public final static String BACKGROUND = "background";

	static ResourceBundle role_resource = ResourceBundle.getBundle("RoleResources");

	public static String get(String key) {
		return role_resource.getString(key);
	}
}
