package com.zeusas.dp.ordm.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.common.base.Strings;
import com.zeusas.core.service.ServiceException;
import com.zeusas.core.utils.BeanDup;
import com.zeusas.dp.ordm.bean.ReserveProductBean;
import com.zeusas.dp.ordm.bean.ReserveRecordBean;
import com.zeusas.dp.ordm.entity.Counter;
import com.zeusas.dp.ordm.entity.Customer;
import com.zeusas.dp.ordm.entity.Product;
import com.zeusas.dp.ordm.entity.ReserveProduct;
import com.zeusas.dp.ordm.entity.ReserveRecord;
import com.zeusas.dp.ordm.service.CounterManager;
import com.zeusas.dp.ordm.service.CustomerManager;
import com.zeusas.dp.ordm.service.ProductManager;
import com.zeusas.dp.ordm.service.ReserveProductManager;
import com.zeusas.dp.ordm.service.ReserveRecordService;
import com.zeusas.security.auth.entity.AuthUser;
import com.zeusas.security.auth.http.BasicController;

/**
 * 打欠设置
 * 
 * @author zhensx
 *
 */
@Controller
@RequestMapping("/reserveproduct")
public class ReserveProductController extends BasicController {
	static final Logger logger = LoggerFactory.getLogger(ReserveProductController.class);
		
	@Autowired
	ReserveProductManager rpManager;
	@Autowired
	ProductManager pManager;
	@Autowired
	ReserveRecordService recordService;
	@Autowired
	private CounterManager counterManager;
	@Autowired
	private CustomerManager customerManager;

	/**
	 * 初始化列表
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/init")
	public String role(HttpServletRequest request) {
		List<ReserveProduct> rpList = rpManager.findAll();
		List<ReserveProductBean> beans = getBeans(rpList);
		request.setAttribute("rpList", beans);
		return "/page/reserveproduct";
	}

	/**
	 * 条件查询
	 * 
	 * @param request
	 * @param name
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/search")
	public String search(HttpServletRequest request,
			@RequestParam(value = "productName", required = false) String productName) {
		List<ReserveProduct> rpList;
		if ("".equals(productName.trim())) {
			rpList = rpManager.findAll();
		} else {
			rpList = rpManager.findByName(productName);
		}
		List<ReserveProductBean> beans = getBeans(rpList);
		request.setAttribute("rpList", beans);
		return "/page/reserveproduct";
	}

	private List<ReserveProductBean> getBeans(List<ReserveProduct> rpList) {
		List<ReserveProductBean> beans = new ArrayList<>();
		for (ReserveProduct reserveProduct : rpList) {
			ReserveProductBean bean = new ReserveProductBean();
			BeanDup.dupIgnore(reserveProduct, bean, "totalReserve");
			Integer productId = reserveProduct.getProductId();
			Date reserveStart = reserveProduct.getReserveStart();
			Date reserveEnd = reserveProduct.getReserveEnd();
			int total = recordService.findByTime(productId, reserveStart, reserveEnd);
			bean.setTotalReserve(total);
			beans.add(bean);
		}
		return beans;
	}

	/**
	 * 添加界面跳转
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/add")
	public String add(HttpServletRequest request) {
		List<Product> all = pManager.findAllAvaible();
		
		List<Map<String, Object>> forReturn = new ArrayList<>();
		
		for (Product p : all) {
			Map<String, Object> map = new HashMap<>();
			map.put("productId", p.getProductId());
			
			if (Product.TYPEID_PRODUCT.equals(p.getTypeId())) {
				map.put("name", p.getName()+"(正品)");
			}else if (Product.TYPEID_PRESENT.equals(p.getTypeId())) {
				map.put("name", p.getName()+"(赠品)");
			}else if (Product.TYPEID_METERIAL.equals(p.getTypeId())) {
				map.put("name", p.getName()+"(物料)");
			}
			
			forReturn.add(map);
		}
	
		request.setAttribute("product",forReturn );
		return "/page/reserveproductadd";
	}

	/**
	 * 创建操作
	 * 
	 * @param request
	 * @param model
	 * @param reserveProduct
	 * @return
	 */
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(HttpServletRequest request, Model model,
			@ModelAttribute("reserveProduct") ReserveProduct reserveProduct) {
		AuthUser authUser = super.getAuthUser();
		reserveProduct.setCreator(authUser.getLoginName());
		request.setAttribute("product", pManager.findAll());
		request.setAttribute("reserveProduct", reserveProduct);
		try {
			ReserveProduct rp = rpManager.get(reserveProduct.getProductId());
			if (rp != null) {
				model.addAttribute("message", "该打欠规则已存在");
				model.addAttribute("createflag", false);
			} else {
				rpManager.add(reserveProduct);
				model.addAttribute("message", "创建成功");
				model.addAttribute("createflag", true);
			}

		} catch (ServiceException e) {
			logger.error("创建打欠规则失败!", e);
			model.addAttribute("message", "创建失败");
			model.addAttribute("createflag", false);
		}
		return "/page/reserveproductadd";
	}

	/**
	 * 更新页面跳转
	 * 
	 * @param request
	 * @param productId
	 * @return
	 */
	@RequestMapping("/updateshow")
	public String updateshow(HttpServletRequest request, @RequestParam(value = "productId") Integer productId) {
		ReserveProduct reserveproduct = rpManager.get(productId);
		request.setAttribute("product", pManager.findAll());
		request.setAttribute("reserveProduct", reserveproduct);
		return "/page/reserveproductupdate";
	}

	@RequestMapping("/update")
	public String update(HttpServletRequest request, Model model,
			@ModelAttribute("reserveProduct") ReserveProduct reserveProduct) {
		request.setAttribute("product", pManager.findAll());
		request.setAttribute("reserveProduct", reserveProduct);
		reserveProduct.setLastUpdate(System.currentTimeMillis());
		try {
			rpManager.update(reserveProduct);
			model.addAttribute("message", "更新成功");
			model.addAttribute("createflag", true);
		} catch (ServiceException e) {
			logger.error("更新打欠规则错误", e);
			model.addAttribute("message", "更新失败");
			model.addAttribute("createflag", false);
		}
		return "/page/reserveproductupdate";
	}

	/**
	 * 启用
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/enable")
	public String enable(@RequestParam(value = "parameter", required = false) Integer id) {

		try {
			rpManager.changeAvalible(id, true);
		} catch (Exception e) {
			logger.error("启用失败", e);
		}
		return "/page/index";
	}

	/**
	 * 禁用
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/disable")
	public String disable(@RequestParam(value = "parameter", required = false) Integer id) {
		try {
			rpManager.changeAvalible(id, false);
		} catch (Exception e) {
			logger.error("禁用失败", e);
		}
		return "/page/index";
	}
	
	/**
	 * 打欠记录报表页面
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/reserverecord")
	public String reserveRecordPage(HttpServletRequest request) {
		List<ReserveRecord> recordList = recordService.findAll();
		request.setAttribute("recordList", getReserveRecordBean(recordList));
		return "/page/reserverecord";
	}
	
	/**
	 * 打欠记录报表查询
	 * 
	 * @param request
	 * @param name
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/searchrecord")
	public String searchrecord(HttpServletRequest request,
			@RequestParam(value = "customerName", required = false) String customerName,
			@RequestParam(value = "counterName", required = false) String counterName,
			@RequestParam(value = "orderNo", required = false) String orderNo) {
		
		List<ReserveRecord> recordList = new ArrayList<>();
		List<Counter> counterList = new ArrayList<>(); 
		List<Customer> customerList = new ArrayList<>();
		Set<String> counters = new HashSet<>();
		Set<Integer> customers = new HashSet<>();
		if (!Strings.isNullOrEmpty(counterName)) {
			counterList = counterManager.findAll().stream()//
			.filter(e-> Strings.isNullOrEmpty(counterName) //
					|| e.getCounterName().indexOf(counterName) > -1)//
			.collect(Collectors.toList());
		}
		if (!Strings.isNullOrEmpty(customerName)) {
			customerList = customerManager.findByName(customerName);
		}
		if (!counterList.isEmpty()) {
			for (Counter counter : counterList) {
				counters.add(counter.getCounterCode());
			}
		}
		if (!customerList.isEmpty()) {
			for (Customer customer : customerList) {
				customers.add(customer.getCustomerID());
			}
		}
		recordList = recordService.findAll().stream()//
				.filter(e-> ((counters.isEmpty() || counters.contains(e.getCounterCode()))
						&& (customers.isEmpty() || customers.contains(e.getCustomerId()))
						&& (Strings.isNullOrEmpty(orderNo) || e.getOrderNo().indexOf(orderNo) > -1)))//
				.collect(Collectors.toList());
		request.setAttribute("recordList", getReserveRecordBean(recordList));
		request.setAttribute("customerName", customerName);
		request.setAttribute("counterName", counterName);
		request.setAttribute("orderNo", orderNo);
		return "/page/reserverecord";
	}

	/**
	 * 转换打欠记录
	 * @param recordList
	 * @return
	 */
	private List<ReserveRecordBean> getReserveRecordBean(List<ReserveRecord> recordList) {
		List<ReserveRecordBean> resList = new ArrayList<>();
		ReserveRecordBean bean = null;
		for (ReserveRecord record : recordList) {
			bean = new ReserveRecordBean(record);
			Customer customer = customerManager.get(record.getCustomerId());
			if (customer != null) {
				bean.setCustomerName(customer.getCustomerName());
			}
			Counter counter = counterManager.getCounterByCode(record.getCounterCode());
			if (counter != null) {
				bean.setCounterName(counter.getCounterName());
			}
			Product product = pManager.get(record.getProductId());
			if (product != null) {
				bean.setProductCode(product.getProductCode());
				bean.setProductName(product.getName());
			}
			resList.add(bean);
		}
		return resList;
	}
}
