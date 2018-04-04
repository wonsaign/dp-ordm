package com.zeusas.dp.ordm.data.utils;
/**
 * 
 * @author shihx
 * @date 2016年12月9日 上午10:46:59
 */
public class PathUtil {
	public static String getParentPath(String PString){
		PString=PString.substring(0,PString.length()-1);
		int i=PString.lastIndexOf("/");
		PString=PString.substring(0,i+1);
		return PString;
	}
}
