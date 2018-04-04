package com.zeusas.dp.ordm.utils;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;

public class UriUtil {
	public static final String FMT_PRODUCT = "productfmt";
	public static final String FMT_SERIAL = "serialfmt";
	public static final String FMT_Order = "orderpayfmt";
	public static final String FMT_ACTI = "activfmt";
	// 合并订单凭证上传路径
	public static final String FMT_COMB = "combinefmt";
	
	/**
	 * 比如获取产品的路径
	 * p/系列id(传入)/
	 * 还需要传入匹配类型，
	 * @param ss
	 */
	public static String getURI(String pattern, Object... ss) {
		String fmt = UriResource.get(pattern);
		String uri = null;
		switch (pattern) {
		case FMT_PRODUCT:
			uri = MessageFormat.format(fmt, ss);
			break;
		case FMT_Order:
			Calendar cd = Calendar.getInstance();
			int dd = cd.get(Calendar.DATE);
			uri = MessageFormat.format(fmt, new Date(), dd);
			break;
		case FMT_ACTI:
			uri = MessageFormat.format(fmt, new Date());
			break;
		case FMT_SERIAL:
			uri = fmt;
			break;
		case FMT_COMB:
			Calendar cdc = Calendar.getInstance();
			int ddc = cdc.get(Calendar.DATE);
			uri = MessageFormat.format(fmt, new Date(), ddc);
			break;
		default:
			throw new RuntimeException("定义的类型【" + pattern + "】不存在！");
		}
		return uri;
	}
}
