package com.zeusas.dp.ordm.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zeusas.core.service.ServiceException;
import com.zeusas.core.utils.BeanDup;
import com.zeusas.security.auth.entity.Role;
import com.zeusas.security.auth.service.AuthCenterManager;
@Controller
@RequestMapping("/roleadm")
public class RoleController {

	
	static Logger logger = LoggerFactory.getLogger(RoleController.class);
	
	@Autowired
	private AuthCenterManager acm;
	
	/**
	 * 显示所有角色
	 * @return
	 */
	@RequestMapping("/role")
	public String role(HttpServletRequest request) throws IOException {
		List<Role> rl=acm.findAllRoles();
		request.setAttribute("Rolelist", rl);
		return "/page/role";
	}
	
//	@RequiresRoles("12")
	@RequestMapping("/addbtn")
	public String addRolrButton(HttpServletRequest request) throws IOException {
		//具有创建角色的权限
		
		String path="/page/role";

		path="/page/roleadd";
		return path;
	}
	
	/**
	 * 创建角色
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/updatebtn_{rid}")
	public String updateRoleButton(HttpServletRequest request
			,@PathVariable(value = "rid") String rid) throws IOException {
		
		return "/page/roleadd";
	}
	
	/**
	 * 创建角色
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/roleadd")
	public String creatRole(HttpServletRequest request,Role r) throws IOException {
		
		
		return "/page/roleadd";
	}
	
	@RequestMapping("/enable")
	public String enable(@RequestParam(value="parameter",required=false)String rid) throws IOException{
		Role r=new Role();
		try {
			BeanDup.dup(acm.getRole(rid), r);
			r.setStatus(Role.role_enable);
			acm.updateRole(r);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return "/roleadm/role";
	}
	@RequestMapping("/disable")
	public String disable(@RequestParam(value="parameter",required=false)String rid) throws IOException{
		Role r=new Role();
		try {
			BeanDup.dup(acm.getRole(rid), r);
			r.setStatus(Role.role_disable);
			acm.updateRole(r);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return "/roleadm/role";
	}

}
