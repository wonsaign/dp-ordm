package com.zeusas.dp.ordm.controller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.zeusas.common.entity.Dictionary;
import com.zeusas.common.service.DictManager;
import com.zeusas.common.utils.DSResponse;
import com.zeusas.core.http.HttpUtil;
import com.zeusas.core.http.QHttpClients;
import com.zeusas.core.utils.AppContext;
import com.zeusas.core.utils.StringUtil;
import com.zeusas.dp.ordm.active.bean.UserActivities;
import com.zeusas.dp.ordm.active.model.Activity;
import com.zeusas.dp.ordm.active.service.ActivityManager;
import com.zeusas.dp.ordm.bean.StockBean;
import com.zeusas.dp.ordm.entity.Product;
import com.zeusas.dp.ordm.entity.ProductRelationPolicy;
import com.zeusas.dp.ordm.entity.ReserveProduct;
import com.zeusas.dp.ordm.service.PRPolicyManager;
import com.zeusas.dp.ordm.service.ProductManager;
import com.zeusas.dp.ordm.service.ReserveProductManager;
import com.zeusas.dp.ordm.service.StockService;
import com.zeusas.dp.ordm.stock.entity.Item;
import com.zeusas.dp.ordm.stock.entity.RequestParameters;
import com.zeusas.dp.ordm.stock.entity.ResultMessage;
import com.zeusas.dp.ordm.task.StorehousesSyncTask;

@Controller
@RequestMapping("/microservice")
public class StockController extends OrdmBasicController {
	static Logger logger = LoggerFactory.getLogger(StockController.class);
	
	@Autowired
	private StockService stockService;
	@Autowired
	private ProductManager productManager;
	@Autowired
	private DictManager dictManager;
	@Autowired
	PRPolicyManager pRPolicyManager;
	@Autowired
	ReserveProductManager reserveProductManager;
	@Autowired
	ActivityManager activityManager;

	/**
	 * 取得产品、物料的库存信息
	 * 
	 * @param type： 空（Ａ）: 11389 物料 11386正品 11388赠品
	 * @return 库存信息
	 */
	@RequestMapping(value = "/getall.do")
	public @ResponseBody Map<String,Collection<StockBean>> getAll(
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "stockId", required = false) String stockId,
			ModelMap mp) {
		// 设置默认仓库,设法弄成全局的
		setAutoMyStock();
		// 设置前段StockId
		setResAttr(mp);
		
		StorehousesSyncTask task = AppContext.getBean(StorehousesSyncTask.class);
		Map<Integer, Map<Integer, Item>>stocks = task.getStockInfo();
		// 设置仓库号为空 返回NULL
		if(HttpUtil.isEmpty(stockId) && StringUtil.isEmpty(stockId)){
			return null;
		}
		Map<Integer, Item> selectStock = stocks.get(Integer.parseInt(stockId));
		
		Collection<Item> stockItems = selectStock.values();
		UserActivities userActs = super.getUserActivityList();
		Map<Integer, StockBean> stockBeans=new HashMap<>(stockItems.size()*4/3);
		for (Item item : stockItems) {
			Product p = productManager.get(item.getPid());
			if (p == null || !p.isAvalible()) {
				continue;
			}

			int qty = item.getV();
			//11389 物料 11386正品 11388赠品
			String typeId = p.getTypeId();
			if(!typeId.equals(type) || qty <= 0 ){
				continue;
			}

			Integer pid = p.getProductId();
			if(stockBeans.get(pid)==null){
				StockBean bean = new StockBean(pid, //pid
						p.getLogisticsCode(), //wmsId
						p.getName(), //name
						p.getSpecifications(), //spec
						qty, // 库存
						p.getFitemClassId(), //classId
						userActs.isJoinProduct(pid));//joinAct
				stockBeans.put(pid, bean);
			}
		}
		// 205代表仓库
		Dictionary dict = dictManager.lookUpByCode("205", stockId);
		
		Map<String,Collection<StockBean>> webMp = new HashMap<>();
		
		Comparator<StockBean> cp = (a, b) -> (a.getClassId() + ":" + a.getName())//
				.compareTo(b.getClassId() + ":" + b.getName());

		Collections.sort(new ArrayList<>(stockBeans.values()), cp);
		webMp.put(dict.getName(), stockBeans.values());
		// id name
		return webMp;
	}
	
	/**
	 * 兼容手机端
	 * @param type
	 * @param stockId
	 * @param mp
	 * @return
	 */
	@RequestMapping(value = "/mobilestock")
	public String mobieGetStockQty(
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "stockId", required = false) String stockId,
			ModelMap mp) {
		// 设置默认仓库,设法弄成全局的
		setAutoMyStock();
		// 设置前段StockId
		setResAttr(mp);
		
		StorehousesSyncTask task = AppContext.getBean(StorehousesSyncTask.class);
		Map<Integer, Map<Integer, Item>>stocks = task.getStockInfo();
		// 设置仓库号为空 返回NULL
		if(HttpUtil.isEmpty(stockId) && StringUtil.isEmpty(stockId)){
			return null;
		}
		Map<Integer, Item> selectStock = stocks.get(Integer.parseInt(stockId));
		
		Collection<Item> stockItems = selectStock.values();
		UserActivities userActs = super.getUserActivityList();
		Map<Integer, StockBean> stockBeans=new HashMap<>(stockItems.size()*4/3);
		for (Item item : stockItems) {
			Product p = productManager.get(item.getPid());
			if (p == null || !p.isAvalible()) {
				continue;
			}

			int qty = item.getV();
			//11389 物料 11386正品 11388赠品
			String typeId = p.getTypeId();
			if(!typeId.equals(type) || qty <= 0 ){
				continue;
			}

			Integer pid = p.getProductId();
			if(stockBeans.get(pid)==null){
				StockBean bean = new StockBean(pid, //pid
						p.getLogisticsCode(), //wmsId
						p.getName(), //name
						p.getSpecifications(), //spec
						qty, // 库存
						p.getFitemClassId(), //classId
						userActs.isJoinProduct(pid));//joinAct
				stockBeans.put(pid, bean);
			}
		}

		Comparator<StockBean> cp = (a, b) -> (a.getClassId() + ":" + a.getName())//
				.compareTo(b.getClassId() + ":" + b.getName());

		Collections.sort(new ArrayList<>(stockBeans.values()), cp);
		// id name
		mp.addAttribute("mobileStock",stockBeans.values());
		return "stock";
	}

	/**
	 * 请求参数中有id，这个id在前段传过来的就是产品的id
	 * @param items
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/stockservice.do")
	public @ResponseBody ResultMessage stockService(
			@RequestParam(value = "items", required = false) String items) throws IOException {
		try {
			if (StringUtil.isEmpty(items)) {
				return stockService.get();
			}
			// 校验请求参数准确性
			List<RequestParameters> requestParameters = JSON.parseArray(items, RequestParameters.class);
			ResultMessage errormessage = checkParameters(requestParameters);
			if (errormessage != null) {
				return errormessage;
			}
			// 获取所有库存结果
			// this is old
			// ResultMessage resultMessage_old = stockService.getDataByID(requestParameters);
			// New 
			ResultMessage resultMessage = stockService.getDataByID(requestParameters,super.getOrderCounter());
			if(resultMessage.getData().size()<0){
				return null;
			}
			return resultMessage;
		} catch (Exception e) {
			logger.error("取得库存信息错误。itemId={}",items,e);
		}
		return null;
	}
	
	@RequestMapping(value = "/getinv.do")
	public @ResponseBody ResultMessage getinv(
		   @RequestParam(value = "items", required = false) String items) throws IOException {
		try {
			if (StringUtil.isEmpty(items)) {
				return stockService.get();
			}
			// 校验请求参数准确性
			List<RequestParameters> requestParameters = JSON.parseArray(items, RequestParameters.class);
			ResultMessage errormessage = checkParameters(requestParameters);
			if (errormessage != null) {
				return errormessage;
			}
			// 获取所有库存结果
			ResultMessage resultMessage = stockService.getDataByID(requestParameters);
			if(resultMessage.getData().size()<0){
				return null;
			}
			return resultMessage;
		} catch (Exception e) {
			logger.error("取得库存信息错误。itemId={}",items,e);
		}
		return null;
	}

	public ResultMessage checkParameters(List<RequestParameters> lts) {
		/** 获取前端请求参数 */
		if (lts == null || lts.isEmpty()) {
			return new ResultMessage(0, 111, "参数为空", null);
		}

		for (RequestParameters t : lts) {
			if (t.getId() == 0 && t.getSubID() > 50) {
				return new ResultMessage(0, 111, "字段参数错误:transferID", null);
			}
		}
		return null;
	}
	
	/**
	 * 正常产品反转处理
	 * @param List<Integer> 产品标识
	 * Step 1 : 校验库存 已经在getDataByID()方法中实现
	 * Step 2 : 判断产品是否有打欠标识 getDataByID()方法中实现
	 * TODO :
	 * Step 3 : 在欠打ajax 判断条件<剩余量小于最小购货单位> -> 
	 *          @param pids 是过滤后小于最小购货单位集合  
	 *          condition 剩余量小于最小购货单位
	 *          ->①反转标识
	 *          ->②重新reload内存
	 * Step 4 : HttpClient 调用后台产品刷新
	 */
	// XXX:Review
	@RequestMapping(value = "/checkflag.do")
	public @ResponseBody DSResponse checkRerFlag(
			@RequestParam(value = "pids", required = false) String pids) {
		//  PID
		try {
			List<RequestParameters> requestParameters = JSON.parseArray(pids, RequestParameters.class);
			ResultMessage errormessage = checkParameters(requestParameters);
			if (errormessage != null) {
				return new DSResponse(-1 , "反转产品参数异常");
			}
			ResultMessage resultMessage = stockService.getDataByID(requestParameters,super.getOrderCounter());
			if(resultMessage.getData().size()<0){
				return new DSResponse(-1 , "反转产品参数异常");
			}
			// 开始反转 
			for (Item item : resultMessage.getData()) {
				// 判断一 : 是否当前产品已经是反转后的打欠产品
				int pid = item.getPid();
				String stockId = super.getOrderCounter().getWarehouses();
				// 可打欠标识
				boolean isReservable = reserveProductManager.isReservable(pid,stockId);
				// 活动可打欠
				boolean isActPro = activityManager.isReservable(super.getOrderCounter(), pid);
				// 可打欠标识 -> 产品
				Integer proCanReser =  item.getT();
				// 可打欠标识 -> 活动
				Integer actCanReser =  item.getA()==null?0:item.getA();
				// 可打欠 并且不是打欠进行中
				// 判断二 : 再次校验库存 ->剩余量小于最小购货单位 难度极其的大
				if(isReservable && proCanReser == 1) {
					reserveProductManager.changeStatus(pid, ReserveProduct.STATUS_RESERVED, stockId);
				}
				if(isActPro && actCanReser == 1) {
					// update act flag
					activityManager.changeToReserved(pid, super.getOrderCounter());;
				}
			}
			
			// 发送消息
			// this.sendAdmReload();
			// no message
			return new DSResponse(0,"处理成功");
		
		}catch(Exception e) {
			logger.error("反转异常,id{}",pids);
			e.printStackTrace();
			return new DSResponse(-1 , "反转产品参数异常");
		}
	
	}
	/**
	 * 反转标识重新重新reload内存
	 * Step3.2->
	 *  ->①反转标识
	 *  ->②重新reload内存
	 * Step 4 : HttpClient 调用后台产品刷新
	 * @param pids
	 */
	private void  sendAdmReload() {
		URI uri = null;
		final ResourceBundle config = ResourceBundle.getBundle("config");
		final String URI = config.getString("NOTIFY_ADMIN_PRODUCT");
		try {
			// HttpClient 发送请求
			QHttpClients qClient = new QHttpClients();
			// 调用URI 刷新后台产品标识 
			uri = new URI(URI);
			Map<String, String> params = new HashMap<String,String>();
			qClient.post(uri,params);
			} catch (URISyntaxException e) {
			logger.error("URI异常{}",uri);
		}
	}
	
	
}
