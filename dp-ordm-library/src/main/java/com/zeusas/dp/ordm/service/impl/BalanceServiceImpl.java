package com.zeusas.dp.ordm.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zeusas.common.data.TypeConverter;
import com.zeusas.core.dao.Dao;
import com.zeusas.core.service.BasicService;
import com.zeusas.core.service.ServiceException;
import com.zeusas.dp.ordm.dao.BalanceDao;
import com.zeusas.dp.ordm.entity.Balance;
import com.zeusas.dp.ordm.entity.Customer;
import com.zeusas.dp.ordm.entity.Order;
import com.zeusas.dp.ordm.entity.OrderCredentials;
import com.zeusas.dp.ordm.entity.ReserveRecord;
import com.zeusas.dp.ordm.service.BalanceService;
import com.zeusas.dp.ordm.service.CustomerManager;
import com.zeusas.dp.ordm.service.OrderCredentialsService;
import com.zeusas.dp.ordm.service.OrderService;
import com.zeusas.dp.ordm.service.ReserveRecordService;

@Service
@Transactional
public class BalanceServiceImpl extends BasicService<Balance, Integer> implements BalanceService {

	static Logger logger = LoggerFactory.getLogger(BalanceServiceImpl.class);

	@Autowired
	private BalanceDao dao;
	@Autowired
	private OrderService orderService;
	@Autowired
	private CustomerManager customerManager;
	@Autowired
	private OrderCredentialsService credentialsService;
	@Autowired
	private ReserveRecordService reserveRecordService;
	

	@Override
	protected Dao<Balance, Integer> getDao() {
		return dao;
	}

	@Override
	@Transactional(readOnly=true)
	public double getBalance(int customerId) throws ServiceException {
		Double d = TypeConverter.toDouble(dao.getBalance(customerId));
		return d == null ? 0 : d.doubleValue();
	}

	@Override
	@Transactional(readOnly=true)
	public double getUsefulBalance(int customerId) throws ServiceException {
		Customer customer = customerManager.get(customerId);

		Set<String> conterIdSet = customer.getCounters();
		if(conterIdSet.isEmpty()&&customer.getLevel()<4){
			conterIdSet=customerManager.findAllChildrenCounters(customer);
		}
		List<Integer> conterIds=new ArrayList<>();
		for (String counterId : conterIdSet) {
			conterIds.add(Integer.parseInt(counterId));
		}

		//订单父凭证
		Set<String> ocid=new HashSet<>();
		
		double lockBalance=0.0;
		
		// 未付款 已付款 锁定的余额
		List<Order> unpayOrders = orderService.getOrders(conterIds, Order.Status_UnPay);
		if (unpayOrders != null) {
			for (Order order : unpayOrders) {
				if (order.isMerger()) {
					OrderCredentials credentials = credentialsService.getCombineOrderCredentials(order.getOrderNo());
					if (credentials != null && !ocid.contains(credentials.getOcid())) {
						ocid.add(credentials.getOcid());
						lockBalance += credentials.getUseBlance();
					}
				} else if (order.getUseBalance() != null) {
					lockBalance += order.getUseBalance();
				}
			}
		}
		
		List<Order> orders=new ArrayList<>();
		
		List<Order> dopayOrders=orderService.getOrders(conterIds, Order.Status_DoPay);
		List<Order> deliveryOrders=orderService.getOrders(conterIds, //
		Order.status_LogisticsDelivery);
		if(dopayOrders!=null){
			orders.addAll(dopayOrders);
		}
		if(deliveryOrders!=null){
			orders.addAll(deliveryOrders);
		}
		
		for (Order order : orders) {
			Double userBlance=order.getUseBalance();
			if(userBlance!=null){
				lockBalance+=userBlance;
			}
		}
//		//预订产品 
//		List<Integer> status= new ArrayList<>();
//		status.add(ReserveRecord.status_reserve);
//		status.add(ReserveRecord.status_wait);
//		
//		List<ReserveRecord> records=reserveRecordService.findRecordByStatus(status);
//		for (ReserveRecord reserveRecord : records) {
//			lockBalance+=reserveRecord.getUnitPrice()*reserveRecord.getQuantity();
//		}
		
		//可用余额 金蝶余额取反 -未付款、已付款、待推送订单使用的余额-未发货预订产品余额
		double balance = 0 - getBalance(customerId);
		balance = balance - lockBalance;
		if(Math.abs(balance) <= 0.01){
			balance = 0.0;
		}
		return balance;
	}

}
