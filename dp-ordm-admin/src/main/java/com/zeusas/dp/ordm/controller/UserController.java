package com.zeusas.dp.ordm.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.zeusas.core.service.ServiceException;
import com.zeusas.core.utils.BeanDup;
import com.zeusas.dp.ordm.bean.PRPBean;
import com.zeusas.dp.ordm.bean.UserBean;
import com.zeusas.dp.ordm.entity.Counter;
import com.zeusas.dp.ordm.entity.UserDetail;
import com.zeusas.dp.ordm.service.UserDetailService;
import com.zeusas.security.auth.entity.AuthUser;
import com.zeusas.security.auth.entity.Group;
import com.zeusas.security.auth.entity.OrgUnit;
import com.zeusas.security.auth.entity.Role;
import com.zeusas.security.auth.http.BasicController;
import com.zeusas.security.auth.service.AuthCenterManager;
import com.zeusas.security.auth.service.AuthentException;
import com.zeusas.security.auth.utils.DigestEncoder;

/**
 * 对用户的一些管理
 * 
 * @author fengx
 * @date 2016年12月6日 下午3:15:39
 */
@Controller
@RequestMapping("/useradm")
public class UserController extends BasicController{

	static Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private AuthCenterManager acm;
	@Autowired
	private UserDetailService uds;

	/**
	 * 获取用户该组织节点的所有用户信息 调到用户页
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/user")
	public String show(HttpServletRequest request) throws IOException {
		String username = request.getRemoteUser();
		UserBean ub = getUserInfo(username);
		// 获取用户组织
		OrgUnit org = acm.getOrgUnitById(ub.getAuthUser().getOrgUnit());
		List<UserBean> ubl = getUserBeanByOrg(org.getOrgId());
		// 获取所有数据太多 未分页
		request.setAttribute("UserBeanlist", ubl);
		request.setAttribute("requestTest", "requestTest");
		return "/page/user";
	}

	/**
	 * 查询组织显示 该组织的所有人(只能查询所在组织及下组的人)
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/search")
	public String search(HttpServletRequest request, @RequestParam(value = "name", required = false) String name)
			throws IOException {
		
		OrgUnit myOrg=BasicController.getOrgUnit(request);
		List<OrgUnit> childs=acm.findAllChildren(myOrg);
		
		List<UserBean> userBeans=new ArrayList<>();
		for (OrgUnit orgUnit : childs) {
			userBeans.addAll(getUserBeanByOrg(orgUnit.getOrgId(), name));
		}
		
		request.setAttribute("UserBeanlist", userBeans);
		return "/page/user";
	}

	 
	@RequestMapping("/enable")
	public String enable(@RequestParam(value="parameter",required=false)String name){
		AuthUser user=new AuthUser();
		try {
			BeanDup.dup(acm.getAuthUser(name), user);
			user.setStatus(AuthUser.user_enable);
			acm.updateAuthUser(user);
		} catch (ServiceException e) {
			logger.error("获取用户错误", e);
		}
		return "/page/index";
	}
	
	@RequestMapping("/disable")
	public String disable(@RequestParam(value="parameter",required=false)String name){
		AuthUser user=new AuthUser();
		try {
			BeanDup.dup(acm.getAuthUser(name), user);
			user.setStatus(AuthUser.user_disable);
			acm.updateAuthUser(user);
		} catch (ServiceException e) {
			logger.error("获取用户错误", e);
		}
		return "/page/index";
	}
	
	
	@RequestMapping("/updatebtn")
	public String updateUserButton(@RequestParam(value = "loginName") String loginName,HttpServletRequest request) {
		UserBean ub =getUserInfo(loginName);
		AuthUser authUser=null;
		UserDetail userDetail=null;
		try {
			authUser=acm.getAuthUser(loginName);
			userDetail=uds.get(authUser.getUid());
		} catch (ServiceException e) {
			logger.error("更新用户时 获取用户失败");
		}
		//组织
		List<OrgUnit> orgs=new ArrayList<OrgUnit>();
		OrgUnit org=acm.getOrgUnitById(ub.getAuthUser().getOrgUnit());
		orgs.add(org);
		
		//已选角色和可选角色
		List<Role> allRoles= getRloeByOrg(org);
		List<Role> myRoles=new ArrayList<>();
		Set<String> myRoleSet=authUser.getRoles();
		if(myRoleSet!=null){
			for (String rid : myRoleSet) {
				Role r=acm.getRole(rid);
				myRoles.add(r);
			}
		}
		request.setAttribute("myRoles", myRoles);
		request.setAttribute("allRoles", allRoles);
		request.setAttribute("authUser", authUser);
		request.setAttribute("userDetail", userDetail);
		request.setAttribute("Orglist", orgs);
		return "/page/userupdate";
	}
	/**
	 * 更新某用户的信息
	 * 
	 * @param authUser
	 * @param request
	 * @return
	 */
	@RequestMapping("/updateuser")
	public String updateUserInfo(HttpServletRequest request,Model model,
			@ModelAttribute("authUser")AuthUser authUser, 
			@ModelAttribute("userDetail")UserDetail userDetail) {
		String[] roles = request.getParameterValues("rid");
		Set<String> roleSet =new HashSet<>(roles.length);
		roleSet.addAll( Arrays.asList(roles));
		
		//比较
		try {
			AuthUser copyUser=new AuthUser();
			BeanDup.dup(acm.getAuthUser(authUser.getLoginName()), copyUser);
			AuthUser cacheUser =acm.getAuthUser(authUser.getLoginName());
			BeanDup.dupNotNull(authUser, copyUser);
			copyUser.setRoles(roleSet);
			//magic number 0 组织根节点的父id（实际不存在的节点） 前台select为请选择是value为0
			if(copyUser.getOrgUnit()==0){
				copyUser.setOrgUnit(cacheUser.getOrgUnit());
			}
			
			UserDetail copyUserDetail =uds.get(copyUser.getUid());
			UserDetail dbUserDetail=new UserDetail();
			BeanDup.dup(copyUserDetail, dbUserDetail);
			BeanDup.dupNotNull(userDetail, copyUserDetail);
			
			//密码不为空则加密  为空则取内存里的密码
			String newpwd=authUser.getPassword();
			String encodeNewpwd=DigestEncoder.encodePassword(copyUser.getLoginName(), newpwd);
			if(authUser.getPassword().length()>0||authUser.getPassword()!=null){
				copyUser.setPassword(encodeNewpwd);
			}else{
				copyUser.setPassword(cacheUser.getPassword());
			}
			
			if(copyUser.equals(cacheUser)&&copyUserDetail.equals(dbUserDetail)){
				model.addAttribute("message", "更新失败 未修改");
			}else{
				if(newpwd!=null&&newpwd.length()>0){
					acm.resetPassword(cacheUser.getLoginName(), newpwd);
				}
				acm.updateAuthUser(copyUser);
				copyUserDetail.setName(authUser.getCommonName());
				uds.update(copyUserDetail);
				model.addAttribute("message", "更新成功");
			}
			//回显
			echo(copyUser, copyUserDetail, request);
		}catch (AuthentException e) {
			e.printStackTrace();
			logger.error("密码修改错误",e);
			model.addAttribute("message", "更新失败");
			//回显
			echo(authUser, userDetail, request);
			return "/page/userupdate";
		} catch (ServiceException e) {
			e.printStackTrace();
			logger.error("用户修改错误",e);
			model.addAttribute("message", "更新失败");
			//回显
			echo(authUser, userDetail, request);
			return "/page/userupdate";
		} 
		return "/page/userupdate";
	}

	/**
	 * 级联 根据所选组织 返回可选角色
	 * @param orgId
	 * @return
	 */
	@RequestMapping(value = "/cascade")
	@ResponseBody
	public List<Role> cascade(@RequestParam(value="orgId",required=false)Integer orgId,HttpServletRequest request){
		OrgUnit firstOrg=acm.getOrgUnitById(orgId);
		List<Role> rl =getRloeByOrg(firstOrg);
		return rl;
	}
	
	/**
	 * 级修改组织按钮 返回操作用户组织
	 * @param orgId
	 * @return
	 */
	@RequestMapping(value = "/orgChange")
	@ResponseBody
	public List<OrgUnit> orgChangeBtn(HttpServletRequest request){
		List<OrgUnit> ol=new ArrayList<OrgUnit>();
		String loginname=request.getRemoteUser();
		AuthUser u;
		OrgUnit o;
		try {
			u = acm.getAuthUser(loginname);
			o=acm.getOrgUnitById(u.getOrgUnit());
			ol.add(o);
		} catch (ServiceException e) {
			logger.error("", e);
		}
		return ol;
	}

	/**
	 * 权限判断 跳转用户创建页 获取可选角色 可选组织
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/useradd")
	public String createUserButton(HttpServletRequest request) {
		String loginName = request.getRemoteUser();
	
		List<OrgUnit> ol = new ArrayList<OrgUnit>();
		List<OrgUnit> olc = null;
		AuthUser user = null;
		// 所在组织及旗下组织
		try {
			user = acm.getAuthUser(loginName);
			OrgUnit o = acm.getOrgUnitById(user.getOrgUnit());
			ol.add(o);
			olc = acm.findAllChildren(o);
		} catch (ServiceException e) {
			logger.error("", e);
		}
	
		// 获取所有角色
		// 级联 根据前台所选组织 显示可选角色
		//先显示第一个组织节点可选角色
		OrgUnit firstOrg=ol.get(0);
		List<Role> rl=getRloeByOrg(firstOrg);
		
		request.setAttribute("Orglist", ol);
		request.setAttribute("OrgChildlist", olc);
		request.setAttribute("Rolelist", rl);
		return "/page/useradd";
	}

	/**
	 * 创建一个用户
	 * 
	 * @param request
	 * @param counter
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/createuser", method = RequestMethod.POST)
	public String createUser(HttpServletRequest request,Model model,
			@ModelAttribute("authUser")AuthUser authUser, 
			@ModelAttribute("userDetail")UserDetail userDetail) {
		
		String[] roles = request.getParameterValues("rid");
		Set<String> roleSet =new HashSet<>(roles.length);
		roleSet.addAll( Arrays.asList(roles));
		try {
			authUser.setRoles(roleSet);
			authUser.setType(AuthUser.type_org);
			authUser.setStatus(1);
			acm.createAuthUser(authUser, authUser.getPassword());
			AuthUser u = acm.getAuthUser(authUser.getLoginName());
			userDetail.setUserId(u.getUid());
			userDetail.setName(authUser.getCommonName());
			userDetail.setStatus(true);
			uds.save(userDetail);
			model.addAttribute("message", "创建成功");
			model.addAttribute("createflag", true);
			echo(authUser, userDetail, request);
		} catch (Exception e) {
			logger.error("创建用户失败", e);
			model.addAttribute("message", "创建成功");
			model.addAttribute("createflag", false);
			echo(authUser, userDetail, request);
		}
		return "/page/useradd";
	}

	/**
	 * 
	 * @param request
	 * @param counter
	 * @param user
	 * @return
	 */
	@Deprecated
	@RequestMapping(value = "/deleteuser", method = RequestMethod.POST)
	public String deleteUser(HttpServletRequest request, @RequestParam Counter counter, @RequestParam AuthUser user) {
		return null;
	}

	/**
	 * AuthUser UserDetail拼成bean
	 * 
	 * @param username
	 * @return
	 */
	private UserBean getUserInfo(String username) {
		AuthUser user = new AuthUser();
		UserDetail ud = null;
		UserBean u=null;
		try {
			BeanDup.dup(acm.getAuthUser(username), user);
			// Set<String> roles 把rid换成commonName 用于显示
			Set<String> roleId = user.getRoles();
			Set<String> roleName = new HashSet<String>();
			for (String rid : roleId) {
				Role r = null;
				try {
					r = acm.getRole(rid);
				} catch (Exception e) {
					logger.error("根据角色ID获取角色错误！{角色ID:" + rid + "}", e);
				}
				if (r == null) {
					continue;
				}
				roleName.add(r.getCommonName());
			}
			user.setRoles(roleName);

			ud = uds.get(user.getUid());
		} catch (Exception e) {
			logger.error("获取用户的信息错误！{}", e);
		} finally {
			if(ud!=null){
				u = new UserBean(user, ud);
			}else{
				u =new UserBean(user);
			}
		}
		return u;
	}

	/**
	 * 获取某组织节点下所有用户
	 * 
	 * @param orgId
	 * @return
	 */
	private List<UserBean> getUserBeanByOrg(Integer orgId) {
		List<UserBean> ubl = new ArrayList<UserBean>();
		List<AuthUser> aul = acm.getAuthUsersByOrg(orgId);
		if(aul==null){
			return ubl;
		}
		for (AuthUser au : aul) {
			UserBean ub = getUserInfo(au.getLoginName());
			ubl.add(ub);
		}
		return ubl;
	}
	
	private List<UserBean> getUserBeanByOrg(Integer orgId,String name) {
		List<UserBean> ubl = new ArrayList<UserBean>();
		List<AuthUser> aul = acm.getAuthUsersByOrg(orgId);
		if(aul==null){
			return ubl;
		}
		for (AuthUser au : aul) {
			if(au.getCommonName().indexOf(name)>-1||au.getLoginName().indexOf(name)>-1){
				UserBean ub = getUserInfo(au.getLoginName());
				ubl.add(ub);
			}
		}
		return ubl;
	}
	
	/**
	 * 根据组织级别 取group
	 * @param org
	 * @return
	 */
	private List<Role> getRloeByOrg(OrgUnit org){
		Group g=acm.getGroup(org.getLevel());
		Set<String> roles = g.getRoles();
		List<Role> rl =new ArrayList<Role>(roles.size());
		for (String s : roles) {
			Role r=acm.getRole(s);
			rl.add(r);
		}
		return rl;
	}
	
	/**
	 * 回显
	 * @param authUser
	 * @param userDetail
	 * @param request
	 */
	private void echo(AuthUser authUser,UserDetail userDetail,HttpServletRequest request){
		//组织
		List<OrgUnit> orgs=new ArrayList<OrgUnit>();
		OrgUnit org=acm.getOrgUnitById(authUser.getOrgUnit());
		orgs.add(org);
		//已选角色和可选角色
		List<Role> allRoles= getRloeByOrg(org);
		List<Role> myRoles=new ArrayList<>();
		Set<String> myRoleSet=authUser.getRoles();
		if(!myRoleSet.isEmpty()){
			for (String rid : myRoleSet) {
				Role r=acm.getRole(rid);
				myRoles.add(r);
			}
		}
		request.setAttribute("myRoles", myRoles);
		request.setAttribute("allRoles", allRoles);
		request.setAttribute("authUser", authUser);
		request.setAttribute("userDetail", userDetail);
		request.setAttribute("Orglist", orgs);
	}
}
