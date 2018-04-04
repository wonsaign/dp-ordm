package com.zeusas.dp.ordm.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.zeusas.core.service.ServiceException;
import com.zeusas.dp.ordm.utils.RoleResource;
import com.zeusas.security.auth.entity.AuthUser;
import com.zeusas.security.auth.http.BasicController;
import com.zeusas.security.auth.http.LoginController;
import com.zeusas.security.auth.service.AuthCenterManager;

/**
 * 后台管理的登录
 * 
 * @author fengx
 * @date 2016年12月14日 上午9:51:09
 */
@Controller
@RequestMapping("/admin")
public class DpLoginController extends LoginController {

	static Logger logger = LoggerFactory.getLogger(DpLoginController.class);

	@Autowired
	private AuthCenterManager authCenterManager;

	@RequestMapping("/login")
	public String login(HttpServletRequest request) {
		Boolean flag = false;
		if (!super.authUserLogin(request)) {
			return "redirect:../login.jsp";
			// 用户状态 0 为禁用
		} else if (BasicController.getAuthUser(request).getStatus() == 0) {
			request.setAttribute("message_login", "用户被禁用");
			return "redirect:../login.jsp";
		} else {
			String allowRoleJSON = RoleResource.get(RoleResource.BACKGROUND);
			List<String> allowRoles = JSON.parseArray(allowRoleJSON, String.class);
			try {
				AuthUser user = BasicController.getAuthUser(request);
				Set<String> roleset = user.getRoles();
				// 判断角色是否允许登录
				if (roleset != null && !roleset.isEmpty()) {
					for (String myrole : roleset) {
						if (allowRoles.contains(myrole)) {
							flag = true;
							break;
						}
						if (!flag) {
							request.setAttribute("message_login", "权限不足");
						}
					}
				}
			} catch (Exception e) {
				logger.error("获取用户失败");
			}
		}
		return flag ? "redirect:/indexadm/index.do" : "redirect:../login.jsp";
	}

	@RequestMapping("/logout")
	public String logout(HttpServletRequest request) {
		super.logout(request);
		return "redirect:../login.jsp";
	}
}
