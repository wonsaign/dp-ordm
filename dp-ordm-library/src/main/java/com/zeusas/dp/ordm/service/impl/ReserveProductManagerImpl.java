package com.zeusas.dp.ordm.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.zeusas.common.service.OnStartApplication;
import com.zeusas.core.service.ServiceException;
import com.zeusas.core.utils.BeanDup;
import com.zeusas.core.utils.DateTime;
import com.zeusas.core.utils.StringUtil;
import com.zeusas.dp.ordm.entity.Counter;
import com.zeusas.dp.ordm.entity.ReserveProduct;
import com.zeusas.dp.ordm.service.ReserveProductManager;
import com.zeusas.dp.ordm.service.ReserveProductService;
@Service
public class ReserveProductManagerImpl extends OnStartApplication implements ReserveProductManager {

	static Logger logger = LoggerFactory.getLogger(ReserveProductManagerImpl.class);

	final Map<Integer, ReserveProduct> productId_Map;

	private long lastUpdate;
	
	@Autowired
	private ReserveProductService reserveProductService;

	public ReserveProductManagerImpl() {
		lastUpdate = -1;
		productId_Map = new HashMap<>();
	}

	@Override
	public void load() {
		List<ReserveProduct> reserves = reserveProductService.findAll();
		productId_Map.clear();

		for (ReserveProduct reserve : reserves) {
			productId_Map.put(reserve.getProductId(), reserve);
		}

	}

	@Override
	public void onStartLoad() {
		if (productId_Map.size() == 0) {
			load();
		}
	}

	@Override
	public ReserveProduct get(Integer productId) {
		if (!DateTime.checkPeriod(Calendar.HOUR, this.lastUpdate)) {
			this.load();
			this.lastUpdate = System.currentTimeMillis();
		}
		return productId_Map.get(productId);
	}

	@Override
	public void add(ReserveProduct reserveProduct) throws ServiceException {
		ReserveProduct reserve = productId_Map.get(reserveProduct.getProductId());
		if (reserve != null) {
			throw new ServiceException("创建预订产品错误，预定产品已存在");
		}
		if (reserveProduct.getReserveStart() == null //
				|| reserveProduct.getReserveEnd() == null //
				|| reserveProduct.getExcuteStart() == null //
				|| reserveProduct.getExcuteEnd() == null) {
			throw new ServiceException("创建预订产品错误，时间不能空");
		}
		Date now = new Date();
		if (now.after(reserveProduct.getReserveStart())//
				&& now.before(reserveProduct.getReserveEnd())) {
			reserveProduct.setAvalible(true);
		} else {
			reserveProduct.setAvalible(false);
		}
		reserveProduct.setStatus(ReserveProduct.STATUS_RESERVABLE);
		reserveProductService.save(reserveProduct);
		productId_Map.put(reserveProduct.getProductId(), reserveProduct);
	}

	@Override
	public void update(ReserveProduct reserveProduct) throws ServiceException {
		try {
			Integer productId = reserveProduct.getProductId();
			ReserveProduct cacheReserve = productId_Map.get(productId);
			ReserveProduct dbReserve = reserveProductService.get(productId);
			cacheReserve.setLastUpdate(System.currentTimeMillis());
			dbReserve.setLastUpdate(System.currentTimeMillis());
			BeanDup.dupNotNull(reserveProduct, cacheReserve, "reserveStart", "reserveEnd", "excuteStart", "excuteEnd","status","updator","context");
			BeanDup.dupNotNull(reserveProduct, dbReserve, "reserveStart", "reserveEnd", "excuteStart", "excuteEnd","status","updator","context");
			reserveProductService.update(dbReserve);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Boolean isReserving(Integer productId,String stockId) {
		Long now=System.currentTimeMillis();
		return isReserving(productId, now,stockId);
	}

	@Override
	public Boolean isReserving(Integer productId, Long time,String stockId) {
		ReserveProduct reserve = productId_Map.get(productId);
		if (reserve == null || !reserve.getAvalible()){
			return false;
		}
		//默认赋值 取设置默认时间
		long rserveStart=reserve.getReserveStart().getTime();
		long reserveEnd=reserve.getReserveEnd().getTime();
		int status=reserve.getStatus(stockId);
		//如果设置了仓库 取该仓库时间
		if(time<rserveStart //
				||time >reserveEnd //
				||status !=ReserveProduct.STATUS_RESERVED){
			return false;
		}
		return true;
	}

	static boolean checkName(ReserveProduct r, String[] ww) {
		StringBuilder txt = new StringBuilder();
		txt.setLength(0);
		txt.append(r.getProductName()).append(':');
		return (StringUtil.matchAll(txt.toString(), ww));
	}
	
	@Override
	public List<ReserveProduct> findByName(String name) {
		if (StringUtil.isEmpty(name)) {
			return new ArrayList<>(0);
		}

		String ww[] = StringUtil.split(name, "\\s+");
		return productId_Map.values().parallelStream()//
				.filter(e -> checkName(e, ww))//
				.collect(Collectors.toList());
	}

	@Override
	public List<ReserveProduct> findAll() {
		return new ArrayList<ReserveProduct>(productId_Map.values());
	}

	@Override
	public void changeAvalible(Integer id,boolean avalible) {
		try {
			ReserveProduct cacheReserve = productId_Map.get(id);
			cacheReserve.setAvalible(avalible);
			reserveProductService.update(cacheReserve);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}


	@Override
	public Boolean isSelling(Integer productId,String stockId) {
		ReserveProduct reserve = productId_Map.get(productId);
		if (reserve == null) {
			return true;
		}
		if(!reserve.getAvalible()){
			return true;
		}
		// 默认赋值 取设置默认时间
		int status=reserve.getStatus(stockId);
		
		if(status==ReserveProduct.STATUS_BUY//
				||status==ReserveProduct.STATUS_DONE){
			return true;
		}
		return false;
	}

	@Override
	public void reset(ReserveProduct reserveProduct) {
		try {
			Integer productId = reserveProduct.getProductId();
			ReserveProduct cacheReserve = productId_Map.get(productId);
			ReserveProduct dbReserve = reserveProductService.get(productId);
			Long lastUdate =System.currentTimeMillis();
			
			cacheReserve.setLastUpdate(lastUdate);
			cacheReserve.setStatus(ReserveProduct.STATUS_RESERVABLE);
			cacheReserve.setAvalible(false);
			dbReserve.setLastUpdate(lastUdate);
			dbReserve.setStatus(ReserveProduct.STATUS_RESERVABLE);
			dbReserve.setAvalible(false);
			
			BeanDup.dupNotNull(reserveProduct, cacheReserve, "reserveStart", "reserveEnd", "excuteStart", "excuteEnd","status","updator");
			BeanDup.dupNotNull(reserveProduct, dbReserve, "reserveStart", "reserveEnd", "excuteStart", "excuteEnd","status","updator");

			reserveProductService.update(dbReserve);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	
	}
	
	@Override
	public void changeStatus(Integer productId,Integer status, String wid) throws ServiceException{
		ReserveProduct reserveProduct = this.get(productId);
		if (reserveProduct == null) {
			throw new ServiceException("更新打欠产品状态失败,打欠产品不存在,产品Id：" + productId + "仓库：" + wid);
		}
		try {
			reserveProduct.setOrAddStatus(wid, status);
			reserveProductService.update(reserveProduct);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			logger.error("更改打欠产品状态错误，产品Id：{}仓库：{}",productId,wid);
			throw new ServiceException(e);
		}

	}

	@Override
	public List<ReserveProduct> findMyReserveProduct(Counter counter) {
		List<ReserveProduct> reserveProducts= findAll();
		List<ReserveProduct> myReserveProducts=new ArrayList<>(reserveProducts.size());
		String wid=counter.getWarehouses();
		Assert.notNull(wid, "获取打欠");
		for (ReserveProduct reserveProduct : reserveProducts) {
			if(this.isReserving(reserveProduct.getProductId(), wid)){
				myReserveProducts.add(reserveProduct);
			}
		}
		return myReserveProducts;
	}

	@Override
	public Boolean isReservable(Integer productId, String stockId) {
		ReserveProduct reserve = productId_Map.get(productId);
		if (reserve == null) {
			return false;
		}
		if (!reserve.getAvalible()) {
			return false;
		}
		// 默认赋值 取设置默认时间
		Long rserveStart = reserve.getReserveStart().getTime();
		Long reserveEnd = reserve.getReserveEnd().getTime();
		Long time = System.currentTimeMillis();

		// 状态
		int status = reserve.getStatus(stockId);
		// 如果设置了仓库 取该仓库时间
		if (time < rserveStart || time > reserveEnd //
				||ReserveProduct.STATUS_RESERVABLE.intValue()!=status) {
			return false;
		}
		return true;
	}
	
	@Override
	public List<String> ListBatchNo() {
		Set<String> all=new LinkedHashSet<>();
		List<ReserveProduct> reserveProducts = findAll();
		for (ReserveProduct reserveProduct : reserveProducts) {
			all.addAll(reserveProduct.getBatchGroup());
		}
		return new ArrayList<>(all);
	}
}
