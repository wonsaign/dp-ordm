package com.zeusas.dp.ordm.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zeusas.common.entity.Dictionary;
import com.zeusas.common.service.DictManager;

/**
 * 本系统对产品分类的维护
 * 如果这里的类型维护是交给字典去维护，这里应该如何去实现
 * @author fx
 *
 */
@Controller
@RequestMapping("/ownertype")
public class PTypeController {

	//调用字典管理类 对产品类型进行维护
  private DictManager dictManager;
	/**
	 * 添加一个新的类型分类
	 * 
	 * @param request
	 * @param dictionary
	 * @param hardCode产品类型在字典表里面的硬码
	 * @return
	 */
	@RequestMapping("/addtype")
	public String add(HttpServletRequest request, Dictionary dictionary ,String hardCode) {
		return null;
	}


	/**
	 * 更新自定义的一个类型 不能删除
	 * 
	 * @param request
	 * @param dictionary
	 * @return
	 */
	@RequestMapping("/updatetype")
	public String update(HttpServletRequest request, Dictionary dictionary ) {
		return null;
	}

	/**
	 * 查找出本系统中自己维护的所有的类型
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/findtype")
	public String find(HttpServletRequest request) {
		return null;
	}

}
