package com.zeusas.dp.ordm.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * @author shihx
 * @date 2016年12月15日 上午8:49:27
 */
@Controller
@RequestMapping("/indexadm")
public class IndexController {

	static Logger logger = LoggerFactory.getLogger(IndexController.class);
	
	@RequestMapping("/index")
	public String index(){
		
		return "page/index";
	}

}
