package com.zeusas.dp.ordm.controller;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.zeusas.dp.ordm.utils.RoleResource;
import com.zeusas.security.auth.entity.AuthUser;
import com.zeusas.security.auth.http.BasicController;
import com.zeusas.security.auth.http.LoginController;
import com.zeusas.security.auth.service.AuthCenterManager;

@Controller
public class DpLoginController extends LoginController {

	@Autowired
	private AuthCenterManager authCenterManager;
	
	static Logger logger = LoggerFactory.getLogger(DpLoginController.class);

	@RequestMapping("/login")
	public String login(HttpServletRequest request, HttpServletResponse response) {
		try {
			if (!super.authUserLogin(request)) {
				return "login";
			}
		} catch (Exception e) {
			logger.warn("Shiro key-token is null from HttpRequest or cookie is not open : ",e.getMessage());
		}
		
		if(request.getCookies()==null)
			return "login";
		
		// 用户状态 0 为禁用
		if (BasicController.getAuthUser(request).getStatus() == 0) {
			request.setAttribute("message_login", "用户被禁用");
			//已经认证需要清除session
			return logout(request);
		}

		String allowRoleJSON = RoleResource.get(RoleResource.FOREGROUND);
		List<String> allowRoles = JSON.parseArray(allowRoleJSON, String.class);

		AuthUser user = BasicController.getAuthUser(request);

		// 后台用户
		if (AuthUser.type_org.equals(user.getType())) {
			request.setAttribute("message_login", "权限不足 请登录后台");
			//已经认证需要清除session
			return logout(request);
		}

		Set<String> roleset = user.getRoles();
		// 判断角色是否允许登录
		for (String role : roleset) {
			if (allowRoles.contains(role)) {
				// 如果存在许可权限
				request.getSession().setAttribute("flogin", "1");
				return "redirect:/ordm/index.do";
			}
		}
		request.setAttribute("message_login", "权限不足");
		//已经认证需要清除session
		return logout(request);
	}

	@RequestMapping("/mlogin")
	public String mlogin(HttpServletRequest request, HttpServletResponse response) {
		String mac = request.getParameter("macaddr");
		String bound = request.getParameter("loginflag");

//		System.out.println(request.getHeader("user-agent").indexOf("Android"));
		
		if (!super.authUserLogin(request)) {
			return "redirect:mlogin-info.jsp";
		}
		// 用户状态 0 为禁用
		if (BasicController.getAuthUser(request).getStatus() == 0) {
			request.setAttribute("message_login", "用户被禁用");
			//已经认证需要清除session
			super.logout(request);
			return "redirect:mlogin-info.jsp";
		}

		String allowRoleJSON = RoleResource.get(RoleResource.FOREGROUND);
		List<String> allowRoles = JSON.parseArray(allowRoleJSON, String.class);

		AuthUser user = BasicController.getAuthUser(request);

		// 后台用户
		if (AuthUser.type_org.equals(user.getType())) {
			request.setAttribute("message_login", "权限不足 请登录后台");
			//已经认证需要清除session
			super.logout(request);
			return "redirect:mlogin-info.jsp";
		}

		Set<String> roleset = user.getRoles();
		// 判断角色是否允许登录
		for (String role : roleset) {
			if (allowRoles.contains(role)) {
				// 如果存在许可权限
				request.getSession().setAttribute("flogin", "1");
				
				if ("2".equals(bound)) {
					// Update macAddr
					user.setMacAddr(mac);
					authCenterManager.updateAuthUser(user);
				}
				
				return "redirect:/ordm/index.do";
			}
		}
		request.setAttribute("message_login", "权限不足");
		//已经认证需要清除session
		
		super.logout(request);
		return  "redirect:mlogin-info.jsp";
	}

	@RequestMapping("/logout")
	public String logout(HttpServletRequest request) {
		super.logout(request);
		return "login";
	}

}
