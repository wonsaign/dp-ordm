package com.zeusas.dp.ordm.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zeusas.common.utils.DSResponse;
import com.zeusas.core.http.HttpUtil;
import com.zeusas.core.utils.BeanDup;
import com.zeusas.dp.ordm.bean.AccountBalanceBean;
import com.zeusas.dp.ordm.bean.UserBean;
import com.zeusas.dp.ordm.entity.Counter;
import com.zeusas.dp.ordm.entity.Customer;
import com.zeusas.dp.ordm.entity.K3Acount;
import com.zeusas.dp.ordm.entity.K3Order;
import com.zeusas.dp.ordm.entity.K3Receive;
import com.zeusas.dp.ordm.entity.UserCustomer;
import com.zeusas.dp.ordm.entity.UserDetail;
import com.zeusas.dp.ordm.service.BalanceService;
import com.zeusas.dp.ordm.service.CounterManager;
import com.zeusas.dp.ordm.service.CustomerManager;
import com.zeusas.dp.ordm.service.K3AcountService;
import com.zeusas.dp.ordm.service.K3OrderService;
import com.zeusas.dp.ordm.service.K3ReceiveService;
import com.zeusas.dp.ordm.service.UserCustomerManager;
import com.zeusas.dp.ordm.service.UserDetailService;
import com.zeusas.security.auth.entity.AuthUser;
import com.zeusas.security.auth.entity.Group;
import com.zeusas.security.auth.entity.OrgUnit;
import com.zeusas.security.auth.entity.Role;
import com.zeusas.security.auth.service.AuthCenterManager;
import com.zeusas.security.auth.service.AuthUserService;
import com.zeusas.security.auth.service.AuthentException;
import com.zeusas.security.auth.utils.DigestEncoder;

/**
 * 对用户的一些管理 用户信息获取原则：尽量从数据库取得，前端不可靠
 * 
 * @author fengx
 * @date 2016年12月6日 下午3:15:39
 */
@Controller
@RequestMapping("/user")
public class UserController extends OrdmBasicController {

	final static Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserDetailService userDetailService;
	@Autowired
	private AuthCenterManager authCenterManager;
	@Autowired
	private AuthUserService authUserService;
	@Autowired
	private CounterManager counterManage;
	@Autowired
	private CustomerManager customerManager;
	@Autowired
	private UserCustomerManager userCustomerManager;
	@Autowired
	private BalanceService balanceService;
	@Autowired
	private K3AcountService k3AcountService;
	@Autowired
	private K3OrderService k3OrderService;
	@Autowired
	private K3ReceiveService k3ReceiveService;

	final static String KEY = "f5a4897fsb778gkbi52ziou6nphq2syz0dpzb83hyrv0l9rrx4p3t37x77puv8extg9pb894kz5488ql5sgcb1zve6b88kv8iifmj9ptxquxke79x1q506wckzw81war";
	final static String JSON_CONTENTTYPE = "application/json; charset=utf-8";
	/**
	 * 自己获取某个用户的信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/info")
	public String getUserInfo(ModelMap mp, HttpServletRequest request) throws IOException {
		String username = request.getRemoteUser();
		// 设置默认仓库,设法弄成全局的
		setAutoMyStock();
		// 设置前段StockId
		setResAttr(mp);
		try {
			AuthUser auther = authCenterManager.getAuthUser(username);
			request.setAttribute("password", auther.getPassword());
			UserDetail udetail = userDetailService.get(auther.getUid());

			UserCustomer userCustomer = userCustomerManager.getByLoginName(super.getAuthUser().getLoginName());

			Set<String> counterSet = userCustomer.getCounters();
			StringBuffer sb = new StringBuffer();
			for (String counterId : counterSet) {
				Counter counter = counterManage.getCounterById(Integer.parseInt(counterId));
				if(counter.getStatus()){
					sb.append(counter.getCounterName()).append(' ');
				}
			}
			Set<String> roleSet = auther.getRoles();
			StringBuffer roles = new StringBuffer();
			for (String s : roleSet) {
				Role r = authCenterManager.getRole(s);
				roles.append(r.getCommonName()).append(' ');;
			}
			// 老板
			if (auther.getRoles().contains("14")) {
				double usefulBalance = getUseflBalance(request);
				request.setAttribute("usefulBalance", usefulBalance);
			}
			request.setAttribute("name", udetail.getName());
			request.setAttribute("mobile", udetail.getMobile());
			request.setAttribute("roles", roles.toString());
			request.setAttribute("counters", sb.toString());
			// 获取 待审核订单、待付款订单、待收货订单、财务退回 的数量
			// FIX ME:
			super.setAllOrderSize();
			mp.addAttribute("all_order_size", super.getAllOrderSize());

		} catch (Exception e) {
			logger.error("获取用户: [{}]的信息错误！",username, e);
		}
		return "user";
	}

	@RequestMapping("/update")
	public String updateUserInfo(UserDetail userBean, @RequestParam(value = "oldPwd", required = false) String oldPwd,
			@RequestParam(value = "newPwd", required = false) String newPwd, HttpServletRequest request)
			throws IOException {
		String username = request.getRemoteUser();
		try {
			AuthUser auther = authCenterManager.getAuthUser(username);
			UserDetail userDetail = userDetailService.get(auther.getUid());
			userDetail.setMobile(userBean.getMobile());
			userDetail.setName(userBean.getName());
			userDetailService.update(userDetail);
			authCenterManager.changePassword(username, oldPwd, newPwd);
		} catch (AuthentException e) {
			logger.error("获取用户的信息错误！{}", e);
			request.setAttribute("error._message", "认证超时");
		} catch (Exception e) {
			logger.error("获取用户的信息错误！{}", e);
			request.setAttribute("error._message", "登录失败");
		}
		return "forward:/user/info.do";
	}

	/**
	 * @author jcm
	 * @param request
	 * @return setting
	 */
	@RequestMapping("/setting")
	public String setIndex(ModelMap mp, HttpServletRequest request) throws IOException {
		List<UserBean> userBeans = null;
		List<String> userIds = new ArrayList<>();
		List<String> loginNames = new ArrayList<>();
		// 设置默认仓库,设法弄成全局的
		setAutoMyStock();
		// 设置前段StockId
		setResAttr(mp);
		try {
			List<UserCustomer> userCustomers = getUserCustomers();
			for (UserCustomer userCustomer : userCustomers) {
				userIds.add(userCustomer.getUserId());
				loginNames.add(userCustomer.getLoginName());
			}

			// 封装 userBean
			userBeans = new ArrayList<>(userIds.size());

			Map<String, UserDetail> userIdMap = new HashMap<>(userIds.size() * 4 / 3);
			List<UserDetail> userDetails = userDetailService.findAllUserDetail(userIds);
			for (UserDetail userDetail : userDetails) {
				userIdMap.put(userDetail.getUserId(), userDetail);
			}
			for (String name : loginNames) {
				AuthUser user = authCenterManager.getAuthUser(name);
				UserBean ub = new UserBean();
				ub.setAuthUser(user);
				ub.setUserDetail(userIdMap.get(user.getUid()));
				ub = getRloe(ub);
				userBeans.add(ub);
			}
		} catch (Exception e) {
			logger.error("获取客户下所有用户错误", e);
		}

		mp.addAttribute("userBean", userBeans);
		return "setting";
	}

	@RequestMapping("/findusers")
	public String findUsers(ModelMap mp, @RequestParam(value = "searchkey", required = false) String searchkey,
			HttpServletRequest request) throws IOException {
		final List<UserBean> userBeans = new ArrayList<>();
		List<String> userIds = new ArrayList<>();
		List<String> loginNames = new ArrayList<>();
		try {
			List<UserCustomer> userCustomers = getUserCustomers();
			for (UserCustomer userCustomer : userCustomers) {
				userIds.add(userCustomer.getUserId());
				loginNames.add(userCustomer.getLoginName());
			}

			// 封装 userBean
			Map<String, UserDetail> userIdMap = new HashMap<>(userIds.size() * 4 / 3);
			List<UserDetail> userDetails = userDetailService.findAllUserDetail(userIds);
			for (UserDetail userDetail : userDetails) {
				userIdMap.put(userDetail.getUserId(), userDetail);
			}
			for (String name : loginNames) {
				AuthUser user = authCenterManager.getAuthUser(name);
				if (user.getLoginName().contains(searchkey) || user.getCommonName().contains(searchkey)) {
					UserBean ub = new UserBean();
					ub.setAuthUser(user);
					ub.setUserDetail(userIdMap.get(user.getUid()));
					ub = getRloe(ub);
					userBeans.add(ub);
				}
			}
		} catch (Exception e) {
			logger.error("查找用户错误，条件：{}",searchkey, e);
		}
		mp.addAttribute("userBean", userBeans);
		return "setting";
	}

	/**
	 * 把用户 AuthUser 和 UserDetail 打包
	 * 
	 * @return
	 * @deprecated
	 */
	public List<UserBean> getUserInfo(AuthUser boss, String counterCode) throws IOException {
		List<UserBean> uBean = new ArrayList<UserBean>();
		UserDetail ud = new UserDetail();
		UserBean ub = new UserBean();
		List<AuthUser> alist = findUsersByCounterCode(counterCode);
		if (alist != null) {
			for (AuthUser au : alist) {
				ud = userDetailService.get(au.getUid());
				ub.setAuthUser(au);
				ub.setUserDetail(ud);
				uBean.add(ub);
			}
		}
		return uBean;
	}

	/**
	 * 通过柜台号查询柜台下的用户
	 * 
	 * @author jcm
	 * @param boss
	 * @param counterCode
	 * @return
	 * @deprecated
	 */
	private List<AuthUser> findUsersByCounterCode(String counterCode) throws IOException {
		List<AuthUser> users = new ArrayList<AuthUser>();
		try {
			if (!com.google.common.base.Strings.isNullOrEmpty(counterCode)) {
				OrgUnit org = authCenterManager.getOrgUnitByCode(counterCode);
				if (org != null) {
					users = authCenterManager.getAuthUsersByOrg(org.getOrgId());
				}
			}
		} catch (Exception e) {
			logger.error("通过柜台号获取用户出错！",e);
		}

		return users;
	}

	/**
	 * 获取setting页面的柜台号到搜索框中
	 * 
	 * @param username
	 * @return
	 * @deprecated
	 */
	public List<OrgUnit> getCounterCodeInSetting(String username) throws IOException {
		List<OrgUnit> orgUnitList = new ArrayList<OrgUnit>();
		try {
			AuthUser aUser = authCenterManager.getAuthUser(username);
			Integer orgID = aUser.getOrgUnit();
			OrgUnit orgUnit = authCenterManager.getOrgUnitById(orgID);
			List<OrgUnit> orgUnits = orgUnit.getChildren();
			for (OrgUnit o : orgUnits) {
				if (o.getLevel() == OrgUnit.LEVEL_COUNTER) {
					orgUnitList.add(o);
				}
			}
		} catch (Exception e) {
			logger.error("获取用户出错！", e);
		}
		return orgUnitList;
	}

	/**
	 * 创建页面初始化 角色默认显示第一个柜台可选角色
	 * 
	 * @param request
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/useradd")
	public String useradd(HttpServletRequest request, Model model) throws IOException {
		String username = request.getRemoteUser();
		List<Counter> counters = new ArrayList<>(0);
		try {
			UserCustomer userCustomer = userCustomerManager.getByLoginName(username);
			Integer customerId = userCustomer.getCustomerId();
			counters.addAll(counterManage.findByCounterId(userCustomer.getCounters()));
			AuthUser user = authCenterManager.getAuthUser(username);
			OrgUnit orgUnit = authCenterManager.getOrgUnitById(user.getOrgUnit());
			List<Role> rl = getRloeByOrg(orgUnit);
			model.addAttribute("customerId", customerId);
			model.addAttribute("orgUnit", orgUnit.getOrgId());
			model.addAttribute("counters", counters);
			model.addAttribute("roles", rl);
		} catch (Exception e) {
			logger.error("创建用页面初始化错误", e);
		}
		return "useradd";
	}

	/**
	 * 级联 根据所选组织 返回可选角色
	 * 
	 * @param orgId
	 * @return
	 */
	@RequestMapping(value = "/cascade")
	@ResponseBody
	public List<Role> cascade(@RequestParam(value = "orgId", required = false) Integer orgId,
			HttpServletRequest request) {
		OrgUnit selectOrg = authCenterManager.getOrgUnitById(orgId);
		return getRloeByOrg(selectOrg);
	}

	@RequestMapping(value = "/userupdate")
	public String userupdate(@RequestParam(value = "loginName") String loginName, HttpServletRequest request,
			Model model) throws IOException {
		String username = request.getRemoteUser();
		List<Counter> allCounters = new ArrayList<>();
		List<Counter> myCounters = new ArrayList<>();
		try {
			// 获取用户信息
			AuthUser aUser = authCenterManager.getAuthUser(loginName);
			UserDetail userDe = userDetailService.get(aUser.getUid());
			// 获取所有柜台
			UserCustomer userCustomer = userCustomerManager.getByLoginName(username);
			Integer customerId = userCustomer.getCustomerId();
			
			allCounters.addAll(counterManage.getCounterByCustomerId(customerId));
			if(allCounters.isEmpty()){
				allCounters.addAll(counterManage.findByCounterId(userCustomer.getCounters()));
			}
			// 当前登录获取自己柜台
			UserCustomer myUserCustomer = userCustomerManager.getByLoginName(loginName);
			Set<String> counterSet = myUserCustomer.getCounters();
			if (counterSet != null && !counterSet.isEmpty()) {
				for (String counterId : counterSet) {
					Counter counter = counterManage.getCounterById(Integer.parseInt(counterId));
					if (counter != null) {
						myCounters.add(counter);
					}
				}
			}

			// 获取修改人其及旗下组织
			AuthUser boss = authCenterManager.getAuthUser(username);
			OrgUnit bosOrg = authCenterManager.getOrgUnitById(boss.getOrgUnit());
			// 获取自己角色
			Set<String> myRolesSet = aUser.getRoles();
			List<Role> myRoles = new ArrayList<Role>(myRolesSet.size());
			for (String s : myRolesSet) {
				Role r = authCenterManager.getRole(s);
				myRoles.add(r);
			}
			// 获取组织角色
			Set<String> allRolesSet = authCenterManager.getGroup(bosOrg.getLevel()).getRoles();
			List<Role> allRoles = new ArrayList<Role>(allRolesSet.size());
			for (String s : allRolesSet) {
				Role r = authCenterManager.getRole(s);
				allRoles.add(r);
			}
			boolean idboss = false;
			// 14 老板
			if (myRoles.contains(authCenterManager.getRole("14"))) {
				idboss = true;
			}

			model.addAttribute("allCounters", allCounters);
			model.addAttribute("myCounters", myCounters);
			model.addAttribute("allRoles", allRoles);
			model.addAttribute("myRoles", myRoles);
			model.addAttribute("orgUnit", bosOrg.getOrgId());
			model.addAttribute("aUser", aUser);
			model.addAttribute("mobile", userDe.getMobile());
			model.addAttribute("idboss", idboss);
		} catch (Exception e) {
			logger.error("", e);
		}
		return "userupdate";
	}

	// 创建用户之后，保存
	@RequestMapping(value = "/addsave", method = RequestMethod.POST)
	public String addsave(ModelMap mp, HttpServletRequest request, AuthUser authUser, UserDetail userDetail)
			throws IOException {
		// 获取角色
		String[] roles = request.getParameterValues("rid");
		Set<String> roleSet = new HashSet<>(roles.length);
		roleSet.addAll(Arrays.asList(roles));
		// 获取柜台
		String[] counters = request.getParameterValues("counterId");
		Set<String> counterSet = new HashSet<>(counters.length);
		counterSet.addAll(Arrays.asList(counters));

		String customerId = request.getParameter("customerId");

		try {
			authUser.setRoles(roleSet);
			authUser.setStatus(AuthUser.user_enable);
			authUser.setType(AuthUser.type_customer);
			authCenterManager.createAuthUser(authUser, authUser.getPassword());
			AuthUser user = authCenterManager.getAuthUser(authUser.getLoginName());
			userDetail.setUserId(user.getUid());
			userDetail.setName(authUser.getCommonName());
			userDetail.setStatus(true);
			userDetail.setLastUpdate(System.currentTimeMillis());
			userDetailService.save(userDetail);

			// 从创建者的UserCustomer中找到客户的用户id和loginName
			UserCustomer createrUC = userCustomerManager.getByLoginName(request.getRemoteUser());

			UserCustomer myUserCustomer = new UserCustomer();
			myUserCustomer.setUserId(user.getUid());
			myUserCustomer.setLoginName(user.getLoginName());
			myUserCustomer.setCustomerId(Integer.parseInt(customerId));
			myUserCustomer.setCustomerLoginName(createrUC.getCustomerLoginName());
			myUserCustomer.setCustomerUserId(createrUC.getCustomerUserId());
			myUserCustomer.setCounters(counterSet);
			userCustomerManager.create(myUserCustomer);
		} catch (Exception e) {
			logger.error("创建用户失败:", e);
		}
		return "forward:/user/setting.do";
	}

	/**
	 * 保持需要更新的用户信息 如果用户名重复则直接返回到页面 否则执行插入
	 * 
	 * @author jcm
	 * @param user
	 * @param oldLoginName
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/updatesave", method = RequestMethod.POST)
	public String updatesave(AuthUser user, UserDetail detail, ModelMap model, HttpServletRequest request)
			throws IOException {
		// 获取角色
		String[] roles = request.getParameterValues("rid");
		Set<String> roleSet = new HashSet<>(roles.length);
		roleSet.addAll(Arrays.asList(roles));
		// 获取柜台
		String[] counters = request.getParameterValues("counterId");
		Set<String> counterSet = new HashSet<>(counters.length);
		counterSet.addAll(Arrays.asList(counters));
		String newPwd = user.getPassword();
		String encodeNewPwd = DigestEncoder.encodePassword(user.getLoginName(), newPwd);
		try {
			String oldPwd = authCenterManager.getAuthUser(user.getLoginName()).getPassword();
			// 重置密码
			if (!oldPwd.equals(encodeNewPwd) && newPwd.length() > 0 && newPwd != null) {
				authCenterManager.resetPassword(user.getLoginName(), newPwd);
			}
			// 更新AuthUser
			user.setRoles(roleSet);
			AuthUser userUpdate = authCenterManager.getAuthUser(user.getLoginName());
			// userUpdate.setRoles(roleSet);
			authCenterManager.updateAuthUser(user);
			// 更新UserDetail
			UserDetail udUpdate = userDetailService.get(userUpdate.getUid());
			BeanDup.dupNotNull(detail, udUpdate);
			userDetailService.update(udUpdate);
			// 更新UserCustomer
			UserCustomer userCustomer = userCustomerManager.getByLoginName(userUpdate.getLoginName());
			userCustomer.setCounters(counterSet);
			userCustomerManager.update(userCustomer);
			model.addAttribute("massage", "success");
		} catch (Exception e) {
			logger.info(super.getAuthUser().getLoginName() + "修改了用户" + user.getLoginName());
			model.addAttribute("massage", "falser");
		}
		return "forward:/user/userupdate.do";
	}

	/**
	 * 把需要更新的用户打包成 UserBean
	 * 
	 * @author jcm
	 * @param user
	 *            ,request
	 * @return UserBean
	 * @deprecated
	 */
	UserBean packUserData(AuthUser user, HttpServletRequest request) {
		UserBean uBean = new UserBean();
		UserDetail ud = new UserDetail();
		Set<String> role = new HashSet<>();
		role.add(request.getParameter("rid"));
		user.setLoginName(request.getParameter("loginName"));
		user.setCommonName(request.getParameter("commonName"));
		user.setPassword(request.getParameter("password"));
		user.setOrgUnit(Integer.parseInt(request.getParameter("orgId")));
		user.setRoles(role);
		ud = userDetailService.get(user.getUid());
		ud.setMobile(request.getParameter("mobile"));
		ud.setName(request.getParameter("commonName"));
		uBean.setAuthUser(user);
		uBean.setUserDetail(ud);
		return uBean;
	}

	@RequestMapping("/changestatus")
	public String changeStatus(ModelMap mp,
			@RequestParam(value = "uid", required = false) String uid,
			@RequestParam(value = "status", required = false) Integer status,
			HttpServletRequest request) throws IOException {
		try {
			AuthUser newUser = authUserService.get(uid);
			if (newUser != null) {
				newUser.setStatus(status);
				authCenterManager.updateAuthUser(newUser);
			} else {
				logger.warn("用户不存在？状态未更新！UserID={} status={}", uid, status);
			}
		} catch (Exception e) {
			logger.error("修改状态异常, useid={}, status={}", uid, status, e);
		}
		return "forward:/user/setting.do";
	}

	/**
	 * 根据组织级别 取group
	 * 
	 * @param org
	 * @return
	 */
	private List<Role> getRloeByOrg(OrgUnit org) {
		Group g = authCenterManager.getGroup(org.getLevel());
		Set<String> roles = g.getRoles();
		List<Role> rl = new ArrayList<Role>(roles.size());
		for (String s : roles) {
			Role r = authCenterManager.getRole(s);
			rl.add(r);
		}
		return rl;
	}

	/**
	 * 根据组织级别 取group
	 * 
	 * @param org
	 * @return
	 */
	private UserBean getRloe(UserBean userBean) {
		Set<String> roles = userBean.getAuthUser().getRoles();
		StringBuffer sb = new StringBuffer();
		for (String s : roles) {
			Role r = authCenterManager.getRole(s);
			sb.append(r.getCommonName() + " ");
		}
		userBean.setRoles(sb.toString());
		return userBean;
	}

	/**
	 * 修改时候先判断密码是否输入正确
	 * 
	 * @param oldpass
	 * @return
	 */
	@RequestMapping(value = "checkpass")
	@ResponseBody
	private String checkPassword(@RequestParam(value = "oldpass", required = true) String oldpass,
			HttpServletRequest request) {
		String name = request.getRemoteUser();
		try {
			AuthUser au = authCenterManager.getAuthUser(name);
			String oldPwdencode = DigestEncoder.encodePassword(au.getLoginName(), oldpass);
			if (oldPwdencode.equals(au.getPassword())) {
				return "success";
			}
		} catch (Exception e) {
			logger.error("获取用户:［{}］出错！",name, e);
		}
		//FIXME: spell error!
		return "falser";
	}

	/**
	 * 获取该客户以及其子客户的所有 UserCustomer
	 * 
	 * @return
	 */
	private List<UserCustomer> getUserCustomers() {
		List<UserCustomer> userCustomers = new ArrayList<>();
		AuthUser myuser = getAuthUser();

		UserCustomer myuserCustomer = userCustomerManager.getByLoginName(myuser.getLoginName());
		Customer mycustomer = customerManager.get(myuserCustomer.getCustomerId());

		// 自己客户下所有用户 id loginName
		userCustomers.addAll(userCustomerManager.getByCustomerId(myuserCustomer.getCustomerId()));

		// 子客户
		List<Customer> childs = new ArrayList<>();
		if (mycustomer.getChildren() != null) {
			childs = customerManager.findAllChildren(mycustomer);
		}

		// 子客户下所有用户 id loginName
		if (!childs.isEmpty()) {
			for (Customer child : childs) {
				Integer customerid=child.getCustomerID();
				List<UserCustomer> usercustomers=userCustomerManager.getByCustomerId(customerid);
				if(usercustomers!=null){
					userCustomers.addAll(usercustomers);
				}
//				userCustomers.addAll(userCustomerManager.getByCustomerId(child.getCustomerID()));

			}
		}
		return userCustomers;
	}

	private double getUseflBalance(HttpServletRequest request) {
		AuthUser authUser = this.getAuthUser();
		UserCustomer uc = userCustomerManager.getByLoginName(authUser.getLoginName());
		return balanceService.getUsefulBalance(uc.getCustomerId());
	}
	
	@RequestMapping("/balanceinit")
	public String balanceinit(ModelMap mp, HttpServletRequest request) throws IOException {
		return "blance_detail";
	}
	
	/**
	 * 查看订单明细
	 * 
	 * @author wangs
	 * @param startTime
	 *            start time "yyyy-MM"
	 * @param endTime
	 *            start time "yyyy-MM"
	 */
	@RequestMapping(value = "/viewBlanceDetail", produces = JSON_CONTENTTYPE)
	@ResponseBody
	public DSResponse getBalanceDetail(String startTime, String endTime) {
		
		String loginName = getAuthUser().getLoginName();
		UserCustomer userCustomer = userCustomerManager.getByLoginName(loginName);
		int customerId = userCustomer.getCustomerId();
		// 默认为当前月
		Calendar cal = Calendar.getInstance();
		int day = cal.get(Calendar.DATE);
		
		cal.add(Calendar.MONDAY, day > 25 ? 1 : 0);
		String year = String.valueOf(cal.get(Calendar.YEAR));
		int month = cal.get(Calendar.MONTH) + 1;
		
		if (HttpUtil.isEmpty(startTime) || HttpUtil.isEmpty(endTime)) {
			StringBuffer b = new StringBuffer();
			startTime = b.append(year)//
					.append(month < 10 ? "0" : "")//
					.append(String.valueOf(month))//
					.toString();
			endTime = startTime;
		}

		// format start / end time
		startTime = startTime.replace("-", "");
		endTime = endTime.replace("-", "");
		
		final List<AccountBalanceBean> balanceBeans = new ArrayList<>();
		try {
		List<K3Acount> k3Acount = k3AcountService.getByCustomerId(customerId, //
				startTime, //
				endTime);// 只有一条 切不能为空

		for (K3Acount acount : k3Acount) {
			AccountBalanceBean accountBalanceBean = new AccountBalanceBean(null,
					formatDate(acount.getfYear(), acount.getfPeriod()-1)+"-26", //26代表每个月的第一天
					acount.getfPeriod()+"月期初", //
					0.0-acount.getBeginMoney(), //
					"期初");
			balanceBeans.add(accountBalanceBean);
		}
		List<K3Order> k3Order = k3OrderService.getByCustomerId(customerId, startTime, endTime);
		for (K3Order order : k3Order) {
			AccountBalanceBean accountBalanceBean = new AccountBalanceBean(order.getfNumber(), //
					order.getfDate(), //
					order.getfExplanation(), //
					order.getfAmount(), //
					order.getfTypeName());
			balanceBeans.add(accountBalanceBean);
		}

		List<K3Receive> k3Receive = k3ReceiveService.getByCustomerId(customerId, //
				startTime, endTime);
		for (K3Receive receive : k3Receive) {
			AccountBalanceBean accountBalanceBean = new AccountBalanceBean(receive.getfNumber(), //
					receive.getfDate(), //
					receive.getfExplanation(), //
					receive.getfAmount(), //
					receive.getfTypeName());
			balanceBeans.add(accountBalanceBean);
		}
		} catch (Exception e) {
			logger.error("取得用户:[{}]余额详细信息错误，开始时间：{}结束时间：{}",loginName,startTime,endTime, e);
		}
		Collections.sort(balanceBeans,Comparator.comparing(AccountBalanceBean::getfDate));

		return new DSResponse(balanceBeans);
	}

	@RequestMapping(value = "/viewMobileBlanceDetail", produces = JSON_CONTENTTYPE)
	@ResponseBody
	public String getMobileBalanceDetail(String startTime, String endTime,Model mp) {
		
		String loginName = getAuthUser().getLoginName();
		UserCustomer userCustomer = userCustomerManager.getByLoginName(loginName);
		int customerId = userCustomer.getCustomerId();
		// 默认为当前月
		Calendar cal = Calendar.getInstance();
		int day = cal.get(Calendar.DATE);
		
		cal.add(Calendar.MONDAY, day > 25 ? 1 : 0);
		String year = String.valueOf(cal.get(Calendar.YEAR));
		int month = cal.get(Calendar.MONTH) + 1;
		
		if (HttpUtil.isEmpty(startTime) || HttpUtil.isEmpty(endTime)) {
			StringBuffer b = new StringBuffer();
			startTime = b.append(year)//
					.append(month < 10 ? "0" : "")//
					.append(String.valueOf(month))//
					.toString();
			endTime = startTime;
		}

		// format start / end time
		startTime = startTime.replace("-", "");
		endTime = endTime.replace("-", "");
		
		final List<AccountBalanceBean> balanceBeans = new ArrayList<>();
		try {
		List<K3Acount> k3Acount = k3AcountService.getByCustomerId(customerId, //
				startTime, //
				endTime);// 只有一条 切不能为空

		for (K3Acount acount : k3Acount) {
			AccountBalanceBean accountBalanceBean = new AccountBalanceBean(null,
					formatDate(acount.getfYear(), acount.getfPeriod()-1)+"-26", //26代表每个月的第一天
					acount.getfPeriod()+"月期初", //
					0.0-acount.getBeginMoney(), //
					"期初");
			balanceBeans.add(accountBalanceBean);
		}
		List<K3Order> k3Order = k3OrderService.getByCustomerId(customerId, startTime, endTime);
		for (K3Order order : k3Order) {
			AccountBalanceBean accountBalanceBean = new AccountBalanceBean(order.getfNumber(), //
					order.getfDate(), //
					order.getfExplanation(), //
					0.0-order.getfAmount(), //
					order.getfTypeName());
			balanceBeans.add(accountBalanceBean);
		}

		List<K3Receive> k3Receive = k3ReceiveService.getByCustomerId(customerId, //
				startTime, endTime);
		for (K3Receive receive : k3Receive) {
			AccountBalanceBean accountBalanceBean = new AccountBalanceBean(receive.getfNumber(), //
					receive.getfDate(), //
					receive.getfExplanation(), //
					0.0-receive.getfAmount(), //
					receive.getfTypeName());
			balanceBeans.add(accountBalanceBean);
		}
		} catch (Exception e) {
			logger.error("取得用户:[{}]余额详细信息错误，开始时间：{}结束时间：{}",loginName,startTime,endTime, e);
		}
		Collections.sort(balanceBeans,Comparator.comparing(AccountBalanceBean::getfDate));
		mp.addAttribute("balanceBeans",balanceBeans);
		return "balance_detail";
	}
	
	
	String formatDate(int y, int m) {
		StringBuilder b = new StringBuilder();
		b.append(y).append('-');
		if (m < 10) {
			b.append('0');
		}
		b.append(m);
		return b.toString();
	}
}
