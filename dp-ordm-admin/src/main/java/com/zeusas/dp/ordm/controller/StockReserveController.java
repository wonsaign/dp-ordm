package com.zeusas.dp.ordm.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.zeusas.common.entity.Dictionary;
import com.zeusas.common.service.DictManager;
import com.zeusas.common.utils.DSResponse;
import com.zeusas.common.utils.Status;
import com.zeusas.core.http.QHttpClients;
import com.zeusas.core.service.ServiceException;
import com.zeusas.core.utils.AppContext;
import com.zeusas.dp.ordm.bean.ProductReserveBean;
import com.zeusas.dp.ordm.bean.StockDetailBean;
import com.zeusas.dp.ordm.bean.StockReserveBean;
import com.zeusas.dp.ordm.entity.Product;
import com.zeusas.dp.ordm.entity.StockReserve;
import com.zeusas.dp.ordm.service.ProductManager;
import com.zeusas.dp.ordm.service.StockReserveService;
import com.zeusas.dp.ordm.service.StockService;
import com.zeusas.dp.ordm.stock.entity.Item;
import com.zeusas.dp.ordm.task.StorehousesSyncTask;

@Controller
@RequestMapping("/stockReserve")
public class StockReserveController {
	static Logger logger = LoggerFactory.getLogger(StockReserveController.class);
	@Autowired
	private StockReserveService stockReserveService;
	@Autowired
	private StockService stockService;
	@Autowired
	private DictManager dictManager;
	@Autowired
	private ProductManager productManager;

	private final static String TYPE = "205";
	private final static ResourceBundle config = ResourceBundle.getBundle("config");
	private final static String ORDM_WAREHOUSE = config.getString("ORDM_WAREHOUSE");
	// 仓库id名映射
	final private Map<Integer, String> stockMapping = new ConcurrentHashMap<>(16);

	@RequestMapping("/init")
	public String initReserve(HttpServletRequest request, Model model) {
		Map<Integer, StockReserveBean> beans = new HashMap<>();
		// 产品信息
		List<Product> products = null;
		try {
			// 获取商品可用数量
			// Key1:StockId, Key2:ProductId, Value:amount
			Map<Integer, Map<Integer, Item>> avai = stockService.getallStock();

			// 获取预留商品数量
			List<StockReserve> reserved = stockReserveService.findAvalible();
			products = productManager.findAll();
			for (StockReserve sr : reserved) {
				// 产品对应的库存信息
				StockReserveBean srb;
				Integer pid = sr.getProductId();
				if ((srb = beans.get(sr.getProductId())) == null) {
					srb = new StockReserveBean(sr);
					// 索引
					beans.put(pid, srb);
				}
				// 预留记录
				for (Item item : sr.getDetail()) {
					Integer stockId = item.getW();
					StockDetailBean detailBean;
					// 可用库存
					Item i;
					if ((detailBean = srb.get(stockId)) == null) {
						String stockName = stockMapping.computeIfAbsent(stockId, this::getStockName);
						// 默认仓库不删除产品
						i = avai.get(stockId).get(pid);
						if (i == null) {
							logger.error("记录{}错误,仓库{}不存在产品{}", sr.getId(), stockName, sr.getProductName());
							continue;
						}
						detailBean = new StockDetailBean(stockName, i);
						srb.put(stockId, detailBean);
					}
					int reserveAmt = item.getV();
					detailBean.setReserveAmt(detailBean.getReserveAmt() + reserveAmt);
					detailBean.setTotalAmt(detailBean.getTotalAmt() + reserveAmt);

					// 一个产品总预留数量
					srb.setReserveAmt(srb.getReserveAmt() + reserveAmt);
					srb.setTotalAmt(srb.getTotalAmt() + reserveAmt);
				}
			}

			// 各仓可用的总和
			// 遍历八大仓库
			for (Map.Entry<Integer, Map<Integer, Item>> chooseStock : avai.entrySet()) {
				// 遍历产品
				for (Map.Entry<Integer, StockReserveBean> e : beans.entrySet()) {
					Item item;
					// 库存
					if ((item = chooseStock.getValue().get(e.getKey())) == null)
						continue;
					StockReserveBean bean = e.getValue();
					Integer inventory = item.getV();
					Integer stockId = chooseStock.getKey();
					bean.putIfAbsent(stockId, new StockDetailBean(stockId,
							stockMapping.computeIfAbsent(stockId, this::getStockName), inventory, 0, inventory));
					bean.setAvailableAmt(bean.getAvailableAmt() + inventory);
					bean.setTotalAmt(bean.getTotalAmt() + inventory);
				}
			}

		} catch (Exception e) {
			logger.error("库存查看失败", e);
		}
		request.setAttribute("product", products);
		request.setAttribute("stock", beans.values().toArray(new StockReserveBean[beans.values().size()]));
		return "/page/productStock";
	}

	private String getStockName(Integer stockId) {
		Dictionary dic;
		return (dic = dictManager.lookUpByCode(TYPE, stockId.toString())) == null ? null : dic.getName();
	}

	// 取得某一产品的预留记录
	@RequestMapping("/record")
	@ResponseBody
	public List<ProductReserveBean>[] productRecord(HttpServletRequest request,
			@RequestParam(value = "productId", required = true) Integer productId) {
		@SuppressWarnings("unchecked")
		List<ProductReserveBean>[] records = new List[3];
		try {
			// 有效记录
			List<StockReserve> validReserves = stockReserveService.findAvalibleByProductId(productId);
			List<ProductReserveBean> validRecords = new ArrayList<>(validReserves.size());
			for (StockReserve res : validReserves) {
				validRecords.add(new ProductReserveBean(res, compose(res.getDetail())));
			}
			records[0] = validRecords;
			// 作废数据
			List<StockReserve> cancelReserves = stockReserveService.findCancelByProductId(productId);
			List<ProductReserveBean> cancelRecords = new ArrayList<>(cancelReserves.size());
			for (StockReserve res : cancelReserves) {
				cancelRecords.add(new ProductReserveBean(res, compose(res.getDetail()), res.getCancellation()));
			}
			records[1] = cancelRecords;
			// 过期数据
			List<StockReserve> expireReserves = stockReserveService.findExpireByProductId(productId);
			List<ProductReserveBean> expireRecords = new ArrayList<>(expireReserves.size());
			for (StockReserve res : expireReserves) {
				expireRecords.add(new ProductReserveBean(res, compose(res.getDetail())));
			}
			records[2] = expireRecords;
		} catch (Exception e) {
			logger.error("查询产品{}库存失败", productId, e);
		}
		return records;
	}

	// 
	private List<String> compose(List<Item> details) {
		List<String> result = new ArrayList<>();
		for (Item item : details)
			result.add(stockMapping.computeIfAbsent(item.getW(), this::getStockName) + " " + item.getV());
		return result;
	}

	// 查看某个产品各个仓的库存
	@RequestMapping("/productinstock")
	@ResponseBody
	public List<StockDetailBean> setting(HttpServletRequest request,
			@RequestParam(value = "productId", required = true) Integer productId) {
		List<Item> available;
		List<StockDetailBean> productinstock = null;
		try {
			available = stockService.getStrock(productId);
			productinstock = new ArrayList<>(available.size());
			for (Item item : available) {
				Integer v;
				if ((v = item.getV()) != null && v > 0)
					productinstock.add(new StockDetailBean(getStockName(item.getW()), item));
			}
		} catch (Exception e) {
			logger.error("查询产品库存失败", e);
		}
		return productinstock;
	}

	// 预留商品保存
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public DSResponse save(HttpServletRequest request,
			@RequestParam(value = "productName", required = false) String productName, //
			@RequestParam(value = "productId", required = true) Integer productId, //
			@RequestParam(value = "startTime", required = true) String startTime, //
			@RequestParam(value = "endTime", required = true) String endTime, //
			@RequestParam(value = "description", required = false) String description, //
			@RequestParam(value = "w", required = true) Integer[] w, //
			@RequestParam(value = "v", required = true) Integer[] v) {

		int len;
		List<Item> list = new ArrayList<>(len = w.length);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			String user = request.getRemoteUser();
			if (Strings.isNullOrEmpty(user))
				throw new SecurityException("用户未登录");
			for (int i = 0; i < len; i++)
				list.add(new Item(productId, w[i], v[i]));
			StockReserve record = new StockReserve(productId, productName, sdf.parse(startTime).getTime(),
					sdf.parse(endTime).getTime(), description, list);
			record.setCreator(user);
			record.setProductName(productManager.get(record.getProductId()).getName());
			record.setValid(true);
			if (!checkValid(record))
				throw new ServiceException(Status.FAILURE.val);
			if (!record.getDetail().isEmpty()) {
				stockReserveService.create(record);
				// 刷新内存
				flush();
			}
		} catch (Exception e) {
			logger.error("预留库存失败", e);
			return new DSResponse(Status.FAILURE, e.getMessage());
		}
		return DSResponse.SUCCESS;
	}

	// 校验前端数据
	private boolean checkValid(StockReserve newrecord) {
		if (newrecord.getStartTime() == null || newrecord.getEndTime() == null
				|| newrecord.getStartTime() > newrecord.getEndTime()
				|| newrecord.getEndTime() < System.currentTimeMillis())
			return false;
		Iterator<Item> iter = newrecord.getDetail().iterator();
		while (iter.hasNext()) {
			Item item = iter.next();
			int v = item.getV() == null ? 0 : item.getV();
			if (v == 0)
				iter.remove();
			else if (v < 0
					|| stockService.getStockProductQty(item.getW().toString(), newrecord.getProductId().toString()) < v)
				return false;
		}
		return true;
	}

	@RequestMapping(value = "/cancel", method = RequestMethod.GET)
	@ResponseBody
	public DSResponse cancel(HttpServletRequest request, @RequestParam(value = "id", required = true) String id,
			@RequestParam(value = "cancellation", required = true) String cancellation) {
		StockReserve record;
		try {
			String user = request.getRemoteUser();
			record = stockReserveService.get(id);
			if (Strings.isNullOrEmpty(user) //
					|| !user.equals(record.getCreator()))
				throw new SecurityException("您不能作废他人预留记录!");
			if (System.currentTimeMillis() < record.getEndTime()//
					&& Boolean.TRUE.equals(record.getValid())) {
				record.setValid(false);
				record.setLastUpdate(System.currentTimeMillis());
				record.setModifier(request.getRemoteUser());
				record.setCancellation(cancellation);
				stockReserveService.update(record);
				flush();
			} else
				throw new IllegalArgumentException("记录已经作废，无法取消!");
		} catch (Exception e) {
			logger.error("取消预留库存失败", e);
			return new DSResponse(Status.FAILURE, e.getMessage());
		}
		return DSResponse.SUCCESS;
	}

	// 刷新内存
	private void flush() throws Exception {
		try {
			StorehousesSyncTask task = AppContext.getBean(StorehousesSyncTask.class);
			QHttpClients client = new QHttpClients(10000);
			// 刷新内存
			task.exec();
			String resp = new String(client.get(ORDM_WAREHOUSE, null));
			DSResponse ds = JSON.parseObject(resp, DSResponse.class);
			if (ds.getStatus() != Status.SUCCESS.id)
				throw new Exception("直发内存刷新失败");
		} catch (Exception e) {
			throw new Exception("直发内存刷新失败");
		}
	}
}
