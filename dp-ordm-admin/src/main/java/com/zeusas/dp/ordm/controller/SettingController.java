package com.zeusas.dp.ordm.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.zeusas.common.entity.Dictionary;
import com.zeusas.common.service.DictManager;

/**
 * 后台 设置 邮费等设置
 * 
 * @author jcm
 * 
 */
@Controller
@RequestMapping("/settingadm")
public class SettingController {

	static Logger logger = LoggerFactory.getLogger(OrgUnitController.class);

	@Autowired
	private DictManager dictManager;

	/**
	 * 初始化
	 */
	@RequestMapping("/init")
	@ResponseBody
	public List<Dictionary> forwardSettingPage() {
		List<Dictionary> dictionarys = new ArrayList<Dictionary>();
		dictionarys.add(dictManager.lookUpByCode("204", "204"));// ORDM配置
		//dictionarys.add(dictManager.lookUpByCode("205", "205"));// 仓库
		return dictionarys;
	}

	/**
	 * 初始化 ordm配置
	 */
	@RequestMapping("/ordmsetting")
	@ResponseBody
	public List<Dictionary> ordmSettings() {
		List<Dictionary> dictionarys = new ArrayList<Dictionary>();
		dictionarys.add(dictManager.lookUpByCode("204", "204"));// ORDM配置
		// dictionarys.add(dictManager.lookUpByCode("205", "205"));//仓库
		return dictionarys;
	}

	/**
	 * 跳转到更新页面
	 */
	@RequestMapping("/setting_{did}")
	public String setting(ModelMap mMap, @PathVariable("did") String did) {
		List<Dictionary> dictionarys = new ArrayList<Dictionary>();
		dictionarys = dictManager.get(did).getChildren();
		mMap.addAttribute("dictionarys", dictionarys);
		return "/page/setting";
	}

	/**
	 * update页面显示
	 */
	@RequestMapping("/update")
	public String update(ModelMap mMap, @RequestParam("did") String did) {
		Dictionary dic = dictManager.get(did);
		mMap.addAttribute("dic", dic);
		return "/page/settingupdate";
	}

	/**
	 * updatesave更新
	 */
	@RequestMapping("/updatesave")
	public String updateSave(ModelMap mMap, @RequestParam("did") String did, @RequestParam("value") String value) {

		Dictionary dic = dictManager.get(did);
		if (dic == null) {
			mMap.addAttribute("message", "更新失败!");
		} else {
			dic.setName(value);
			dictManager.update(dic);
			mMap.addAttribute("message", "更新成功!");
		}
		mMap.addAttribute("dic", dic);
		return "/page/settingupdate";
	}
}
