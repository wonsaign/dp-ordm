package com.zeusas.dp.ordm.service.impl;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zeusas.core.dao.Dao;
import com.zeusas.core.dao.DaoException;
import com.zeusas.core.service.BasicService;
import com.zeusas.core.service.ServiceException;
import com.zeusas.core.utils.AppContext;
import com.zeusas.dp.ordm.dao.AliPaymentDao;
import com.zeusas.dp.ordm.dao.OrderDao;
import com.zeusas.dp.ordm.dao.OrderDetailDao;
import com.zeusas.dp.ordm.dao.ReserveRecordDao;
import com.zeusas.dp.ordm.entity.Counter;
import com.zeusas.dp.ordm.entity.IPackage;
import com.zeusas.dp.ordm.entity.Order;
import com.zeusas.dp.ordm.entity.OrderDetail;
import com.zeusas.dp.ordm.entity.ReserveProduct;
import com.zeusas.dp.ordm.entity.ReserveRecord;
import com.zeusas.dp.ordm.service.CounterManager;
import com.zeusas.dp.ordm.service.ReserveProductManager;
import com.zeusas.dp.ordm.service.ReserveRecordService;

@Service
@Transactional
public class ReserveRecordServiceImpl extends BasicService<ReserveRecord, Long> implements ReserveRecordService {

	final static Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

	@Autowired
	private ReserveRecordDao dao;
	@Autowired
	private OrderDetailDao orderDetailDao;
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private AliPaymentDao aliPaymentDao;
	

	@Override
	protected Dao<ReserveRecord, Long> getDao() {
		return dao;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ReserveRecord> findRecordByStatus(Integer status) {
		return dao.findByStatus(status);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ReserveRecord> getRecordByOrderNo(String orderNo) {
		return dao.findByOrderNo(orderNo);
	}

	@Override
	//FIXME:校验库存生成打欠记录
	public void add(String orderNo) throws ServiceException {
		ReserveProductManager reserveProductManager = AppContext.getBean(ReserveProductManager.class);
		CounterManager counterManager = AppContext.getBean(CounterManager.class);

		List<OrderDetail> details = orderDetailDao.getOrderDetails(orderNo);
		if (details.isEmpty()) {
			return;
		}

		Order order = orderDao.findByOrderNo(orderNo);
		Long payTime = order.getOrderPayTime();

		Counter counter = counterManager.getCounterByCode(order.getCounterCode());
		try {
			for (OrderDetail orderDetail : details) {
				Integer productId = orderDetail.getProductId();
				// 为预订产品 且 可预订
				if (OrderDetail.TYPE_RESERVE==(orderDetail.getDetailType()) && //
						reserveProductManager.isReserving(productId, payTime,counter.getWarehouses())) {
					ReserveProduct reserveProduct = reserveProductManager.get(productId);
					ReserveRecord record = new ReserveRecord(orderDetail, counter, reserveProduct);
					dao.save(record);
				}
			}
		} catch (DaoException e) {
			logger.error("订单明细生成预订记录错误，订单号{}", orderNo);
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<ReserveRecord> findWeitShipByCounterCode(String counterCode) {
		return dao.findByCounterCodeAndStatus(counterCode, ReserveRecord.STATUS_WAIT);
	}

	@Override
	public void cancleReserve(Long orderDetailId) throws ServiceException {
		ReserveRecord record = dao.get(orderDetailId);
		if (record == null) {
			throw new ServiceException("该订单没有预订产品");
		}
		record.setStatus(ReserveRecord.STATUS_CANCLE);
		try {
			dao.update(record);
			double amt=record.getUnitPrice()*(record.getQuantity()-record.getFreeQty());
			//金额不为0才推
			if(amt>0.001){
				String orderNo=record.getOrderNo();
				Order order= orderDao.findByOrderNo(orderNo);
				amt=0-amt;
				aliPaymentDao.buildK3OtherReceive(IPackage.BILL_QX+orderNo, amt, order.getCounterId());
			}
		} catch (DaoException e) {
			logger.error("取消预订错误，订单明细ID", orderDetailId);
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<ReserveRecord> findRecordByStatus(Collection<Integer> status) {
		return dao.findByStatus(status);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ReserveRecord> findByCustomerId(String customerId) throws ServiceException {
		return dao.findByCustomerId(customerId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ReserveRecord> findByCustomerIdAndStatus(String customerId, Collection<Integer> status)
			throws ServiceException {
		return dao.findByCustomerIdAndStatus(customerId, status);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ReserveRecord> findByProductId(Collection<Integer> productIds) {
		return dao.findByProductId(productIds);
	}

	@Override
	@Transactional(readOnly = true)
	public int findByTime(Integer productId, Date reserveStart, Date reserveEnd) {

		String hql = " productId = ? AND createTime >= ? AND createTime <= ?";
		List<ReserveRecord> find = dao.find(hql, productId, reserveStart, reserveEnd);
		IntSummaryStatistics ss = find.stream().mapToInt(e -> e.getQuantity()).summaryStatistics();
		return (int) ss.getSum();
	}

	/**
	 * 根据pid吧状态更改为 ReserveRecord.status_cancle
	 */
	@Override
	public void cancleReserveActivity(Long orderDetailId) throws ServiceException {
		try {
			ReserveRecord reserveRecord = dao.get(orderDetailId);
			if (reserveRecord == null) {
				throw new ServiceException("打欠记录不存在,id:" + orderDetailId);
			}
			if (reserveRecord.getPid() == null) {
				throw new ServiceException("该记录不是活动打欠,id:" + orderDetailId);
			}
			List<ReserveRecord> records = dao.findByPid(reserveRecord.getPid());
			double amt = 0.0;
			for (ReserveRecord record : records) {
				amt += record.getUnitPrice() * (record.getQuantity() - record.getFreeQty());
			}
			// 金额不为0才推
			if (amt > 0.001) {
				String orderNo = reserveRecord.getOrderNo();
				Order order = orderDao.findByOrderNo(orderNo);
				amt=0-amt;
				aliPaymentDao.buildK3OtherReceive(IPackage.BILL_QX + orderNo, amt, order.getCounterId());
			}
			dao.changeActivityStatus(ReserveRecord.STATUS_CANCLE, reserveRecord.getPid());
		} catch (DaoException e) {
			logger.error("取消活动打欠记录失败，主键{}", orderDetailId);
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}

	@Override
	public void changeSingleStatus(Integer status, Long orderDetailId) {
		try {
			dao.changeSingleStatus(status, orderDetailId);
		} catch (DaoException e) {
			logger.error("更新单条打欠记录失败，主键{}，状态{}",orderDetailId ,status);
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		
	}

	@Override
	public void changeActivityStatus(Integer status, Long pid) {
		try {
			dao.changeActivityStatus(status, pid);
		} catch (DaoException e) {
			logger.error("更新单条打欠记录失败，活动标志{}，状态{}",pid ,status);
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Map<Integer, Map<Integer, Integer>> groupByProductWarehouse(Collection<Integer> status) {
		Map<Integer, Map<Integer, Integer>> group = new HashMap<>();
		try {
			List<ReserveRecord> records =  dao.findByStatus(status);
			for (ReserveRecord record : records) {
				Integer pid =record.getProductId();
				Integer wid =record.getWarehouse();
				int qty=record.getQuantity();
				
				if(!group.containsKey(pid)){
					group.put(pid, new HashMap<>());
				}
				if(!group.get(pid).containsKey(wid)){
					group.get(pid).put(wid, 0);
				}
				int v=qty+group.get(pid).get(wid);
				group.get(pid).put(wid, v);
			}
		} catch (DaoException e) {
			logger.error("统计打欠数量错误");
			throw new ServiceException("统计打欠数量错误",e);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		return group;
	}

}
