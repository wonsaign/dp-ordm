package com.zeusas.dp.ordm.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.zeusas.core.http.HttpUtil;
import com.zeusas.core.utils.AppContext;
import com.zeusas.core.utils.StringUtil;
import com.zeusas.dp.ordm.active.bean.UserActivities;
import com.zeusas.dp.ordm.bean.StockBean;
import com.zeusas.dp.ordm.entity.Product;
import com.zeusas.dp.ordm.service.ProductManager;
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

	/**
	 * 取得产品、物料的库存信息
	 * 
	 * @param type： 空（Ａ）: 11389 物料 11386正品 11388赠品
	 * @return 库存信息
	 */
	@RequestMapping(value = "/getall.do")
	public @ResponseBody Collection<StockBean> getAll(
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
		return stockBeans.values();
	}

	/**
	 * 兼容手机端
	 * @param type
	 * @param stockId
	 * @param mp
	 * @return
	 */
	@RequestMapping("/mobilestock")
	public String mobieGetStockQty(
			ModelMap mp) {
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
}
