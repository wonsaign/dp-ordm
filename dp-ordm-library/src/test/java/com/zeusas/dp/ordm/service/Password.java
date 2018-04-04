package com.zeusas.dp.ordm.service;

import java.util.Date;
import java.util.Random;

import org.junit.Test;

import com.zeusas.core.utils.DateTime;
import com.zeusas.security.auth.utils.DigestEncoder;

public class Password {
//	 //静态方法，便于作为工具类  
//    public String getMd5(String plainText) {  
//        try {  
//            MessageDigest md = MessageDigest.getInstance("MD5");  
//            md.update(plainText.getBytes());  
//            byte b[] = md.digest();  
//  
//            int i;  
//  
//            StringBuffer buf = new StringBuffer("");  
//            for (int offset = 0; offset < b.length; offset++) {  
//                i = b[offset];  
//                if (i < 0)  
//                    i += 256;  
//                if (i < 16)  
//                    buf.append("0");  
//                buf.append(Integer.toHexString(i));  
//            }  
//            //32位加密  
//            return buf.toString();  
//            // 16位的加密  
//            //return buf.toString().substring(8, 24);  
//        } catch (NoSuchAlgorithmException e) {  
//            e.printStackTrace();  
//            return null;  
//        }  
//  
//    }  
	
	private static String getFixLenthString(int strLength) {  
	      
	    Random rm = new Random();  
	      
	    // 获得随机数  
	    double pross = (1 + rm.nextDouble()) * Math.pow(10, strLength);  
	  
	    // 将获得的获得随机数转化为字符串  
	    String fixLenthString = String.valueOf(pross);  
	  
	    // 返回固定的长度的随机数  
	    return fixLenthString.substring(1, strLength + 1);  
	}
    @Test
	public void testName() throws Exception {
    	System.out.println(DigestEncoder.encodePassword("heliushuo", "930351"));
    	System.out.println(DigestEncoder.encodePassword("jzzwys", "505317"));
    	System.out.println(DigestEncoder.encodePassword("root", "ordm2017!!"));
    	System.out.println(getFixLenthString(6));
    	System.out.println(DateTime.formatDate(DateTime.YYYY_MM_DD_HMS, new Date(1506528000000l)));
    	System.out.println(DateTime.formatDate(DateTime.YYYY_MM_DD_HMS, new Date(1506441600000l)));
    	System.out.println(DateTime.formatDate(DateTime.YYYY_MM_DD_HMS, new Date(1505836800000l)));
    	System.out.println(DateTime.formatDate(DateTime.YYYY_MM_DD_HMS, new Date(1508947200000l)));
    	System.out.println(new Date().getTime());
    	System.out.println(DateTime.toDate(DateTime.YYYY_MM_DD, "2017-08-01").getTime());
    	System.out.println(DateTime.toDate(DateTime.YYYY_MM_DD, "2017-10-14").getTime());
    }
      
}
