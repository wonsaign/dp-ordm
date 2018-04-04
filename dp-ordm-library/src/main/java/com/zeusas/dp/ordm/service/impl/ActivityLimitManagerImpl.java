package com.zeusas.dp.ordm.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zeusas.common.data.TypeConverter;
import com.zeusas.common.service.OnStartApplication;
import com.zeusas.core.http.ActionException;
import com.zeusas.core.service.ServiceException;
import com.zeusas.core.utils.AppContext;
import com.zeusas.core.utils.BeanDup;
import com.zeusas.dp.ordm.entity.ActivityLimit;
import com.zeusas.dp.ordm.entity.Cart;
import com.zeusas.dp.ordm.entity.CartDetail;
import com.zeusas.dp.ordm.entity.Counter;
import com.zeusas.dp.ordm.entity.LimitContext;
import com.zeusas.dp.ordm.entity.Order;
import com.zeusas.dp.ordm.entity.OrderDetail;
import com.zeusas.dp.ordm.service.ActivityLimitManager;
import com.zeusas.dp.ordm.service.ActivityLimitService;
import com.zeusas.dp.ordm.service.CartService;
import com.zeusas.dp.ordm.service.CounterManager;
@Service
public class ActivityLimitManagerImpl extends OnStartApplication implements ActivityLimitManager {

	static Logger logger = LoggerFactory.getLogger(ActivityLimitManagerImpl.class);

	final Map<Integer, ActivityLimit> allLimit;
	@Autowired
	private ActivityLimitService activityLimitService;
	@Autowired
	private CartService cartService;

	public ActivityLimitManagerImpl() {
		allLimit = new HashMap<>();
	}

	@Override
	public void load() {
		List<ActivityLimit> limits = activityLimitService.findAll();

		allLimit.clear();

		for (ActivityLimit activityLimit : limits) {
			allLimit.put(activityLimit.getCustomerID(), activityLimit);
		}

	}

	@Override
	public void onStartLoad() {
		if (allLimit.size() == 0) {
			load();
		}
	}

	@Override
	public ActivityLimit get(Integer customerId) {
		return allLimit.get(customerId);
	}

	@Override
	public Map<String, LimitContext> getLimitContextAvaliable(Integer customerId) throws ActionException {
		CounterManager counterManager = AppContext.getBean(CounterManager.class);
		ActivityLimit limit = this.get(customerId);
		if (limit == null) {
			return null;
		}
		// 源数据
		ActivityLimit clone = new ActivityLimit();
		BeanDup.dup(limit, clone);
		// 结果集
		Map<String, LimitContext> resultData = clone.toContextMap();
		// 购物车里的是数据
		List<Counter> counters = counterManager.getCounterByCustomerId(customerId);
		List<Integer> counterIds = new ArrayList<>();
		for (Counter counter : counters) {
			counterIds.add(counter.getCounterId());
		}
		List<CartDetail> details = cartService.getCartDetailByCounter(counterIds);
		if (details != null) {

			for (CartDetail detail : details) {
				String activityId = detail.getActivityId();
				if (activityId == null) {
					continue;
				}
				if (resultData.containsKey(activityId)) {
					Integer qty = resultData.get(activityId).getQty();
					resultData.get(activityId).setQty(qty - detail.getQuantity());
				}
			}
		}
		return resultData;
	}

	@Override
	@Transactional
	public Boolean commitCartToOrder(Integer customerId, List<CartDetail> details) throws ServiceException {
		ActivityLimit limit = this.get(customerId);
		if (limit == null) {
			return false;
		}
		// 源数据
		ActivityLimit clone = new ActivityLimit();
		BeanDup.dup(limit, clone);
		Map<String, LimitContext> resultData = clone.toContextMap();
		try {
			for (CartDetail detail : details) {
				String activityId = detail.getActivityId();
				if (activityId == null) {
					continue;
				}
				if (resultData.containsKey(activityId)) {
					Integer qty = resultData.get(activityId).getQty() - detail.getQuantity();
					if (qty < 0) {
						throw new ServiceException("活动可定数量不足,客户id:" + customerId);
					}
					resultData.get(activityId).setQty(qty);
				}
			}
			activityLimitService.update(clone);
			return true;
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional
	public Boolean releaseOrderToCart(Order order, List<OrderDetail> details) throws ServiceException {
		Integer customerId = TypeConverter.toInteger(order.getCustomerId());

		ActivityLimit limit = this.get(customerId);
		if (limit == null) {
			return false;
		}
		// 源数据
		ActivityLimit clone = new ActivityLimit();
		BeanDup.dup(limit, clone);
		Map<String, LimitContext> resultData = clone.toContextMap();

		Map<Long, List<OrderDetail>> map_orderdetail = details.stream() //
				.filter(e -> e.getActivityId() != null)//
				.collect(Collectors.groupingBy(OrderDetail::getPid));
		try {

			for (List<OrderDetail> detailsGroup : map_orderdetail.values()) {
				//只拿第一条 因为同一个pid为一组明细 其活动id 套数一样
				OrderDetail first = detailsGroup.get(0);
				String activityId = first.getActivityId();
				if (activityId == null) {
					continue;
				}
				if (resultData.containsKey(activityId)) {
					LimitContext context = resultData.get(activityId);
					Integer qty = context.getQty() + first.getSuitNumber();
					Integer max = (int) (context.getMax() * context.getCoefficient());
					if (qty > max) {
						throw new ServiceException("释放活动数量异常，释放后大于最大可定数量，订单号："//
								+ order.getOrderNo() + "活动id" + activityId);
					}
					resultData.get(activityId).setQty(qty);
				}
			}
			activityLimitService.update(clone);
			return true;
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
}
