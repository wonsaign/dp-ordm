package com.zeusas.dp.ordm.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.zeusas.security.auth.entity.OrgUnit;
import com.zeusas.security.auth.service.AuthCenterManager;

/**
 * 
 * @author shihx
 * @date 2016年12月21日 上午9:11:58
 */
@Controller
@RequestMapping("/org")
public class OrgUnitController {
	
	
	static Logger logger = LoggerFactory.getLogger(OrgUnitController.class);

	@Autowired
	private AuthCenterManager acm;
	
	@RequestMapping("/init")
	@ResponseBody
	public String init(HttpServletRequest request){
		return "/page/org";
	}
	
	@RequestMapping("/Child")
	@ResponseBody
	public List<OrgUnit> getchild(HttpServletRequest request,@RequestParam(value="orgId",required=false)Integer orgId){
		OrgUnit org=null;
		List<OrgUnit> orgs=null;
		try {
			org=acm.getOrgUnitById(orgId);
		} catch (Exception e) {
			logger.error("获取组织失败 OrgId []",orgId,e);
		}
		if(org!=null){
			orgs=org.getChildren();
		}
		return orgs;
	}
}
