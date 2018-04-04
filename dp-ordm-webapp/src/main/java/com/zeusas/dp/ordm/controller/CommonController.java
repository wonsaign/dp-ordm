package com.zeusas.dp.ordm.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zeusas.common.entity.Dictionary;
import com.zeusas.common.service.DictManager;
import com.zeusas.dp.ordm.bi.service.ProductSellerManager;
import com.zeusas.dp.ordm.entity.Counter;
import com.zeusas.dp.ordm.entity.CounterSerial;
import com.zeusas.dp.ordm.entity.Notification;
import com.zeusas.dp.ordm.entity.Product;
import com.zeusas.dp.ordm.entity.SerialSeller;
import com.zeusas.dp.ordm.service.CounterManager;
import com.zeusas.dp.ordm.service.NotificationService;

/**
 * 通用头部
 * @author wonsign
 *
 */
@Controller
public class CommonController extends OrdmBasicController{
	static Logger logger = LoggerFactory.getLogger(CommonController.class);
	
	@Autowired
	private CounterManager counterManager;
	@Autowired
	private DictManager dictManager;
	@Autowired
	private ProductSellerManager productSellerManager;
	@Autowired
	private NotificationService notificationService;
	/**
	 * 添加一个新的类型分类
	 * 设置message全局的
	 * @param request
	 * @param dictionary
	 * @param hardCode产品类型在字典表里面的硬码
	 * @return
	 */
	@RequestMapping("/series")
	@ResponseBody
	public List<Dictionary> chooseSeries(
			String counterCode) throws IOException {
		List<Dictionary> localSerials = new ArrayList<Dictionary>();
		try {
			//设置柜台号
			super.setCounterCode(counterCode);
			Counter  counter = counterManager.getCounterByCode(counterCode);
			//设置柜台
			super.setOrderCounter(counter);
			// 放入session缓存中
			CounterSerial cs = super.getCounterSerial(counterCode);
			if (cs == null) {
				cs = productSellerManager.getSeriesSeller(counterCode);
				super.setCounterSerial(cs);
			}
			/*// ②Message读取
			List<Notification> notification = notificationService.getLast(2);
			mp.addAttribute("notify", notification);*/
			
			CounterSerial localCounterSerial = getCounterSerial(counterCode);
			//session超时会为空
			if(localCounterSerial==null){
				localCounterSerial=productSellerManager.getGlobalSeriesSeller();
			}
			List<SerialSeller> localSerialSellers = localCounterSerial
					.getSerials();
			for (SerialSeller serialSeller : localSerialSellers) {
				Dictionary dictionary = dictManager.lookUpByCode(
						Product.PRODUCT_POSITIVE_SERIES, serialSeller.getSid());
				localSerials.add(dictionary);
			}
		} catch (Exception e) {
			logger.error("本地热销系列显示错误,柜台{}",counterCode, e);
		}
		return localSerials;
	}

	/**
	 * 兼容手机app 
	 * @param mp
	 * @return
	 */
	@RequestMapping("/msg")
	public String getMessages(ModelMap mp){
		List<Notification> notification = notificationService.getLast(2);
		mp.addAttribute("notify", notification);
		
		return "message";
	}
}
