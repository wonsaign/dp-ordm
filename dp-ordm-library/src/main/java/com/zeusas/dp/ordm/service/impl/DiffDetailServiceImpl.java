package com.zeusas.dp.ordm.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.zeusas.common.data.DDLDBMS;
import com.zeusas.common.data.Database;
import com.zeusas.common.data.DdlItem;
import com.zeusas.common.data.Record;
import com.zeusas.common.data.Table;
import com.zeusas.common.service.IdGenService;
import com.zeusas.core.dao.Dao;
import com.zeusas.core.dao.DaoException;
import com.zeusas.core.service.BasicService;
import com.zeusas.core.service.ServiceException;
import com.zeusas.core.utils.AppContext;
import com.zeusas.dp.ordm.dao.DiffDetailDao;
import com.zeusas.dp.ordm.dao.OrderDao;
import com.zeusas.dp.ordm.dao.OrderDetailDao;
import com.zeusas.dp.ordm.dao.PackageDao;
import com.zeusas.dp.ordm.dao.PackageDetailDao;
import com.zeusas.dp.ordm.entity.DiffDetail;
import com.zeusas.dp.ordm.entity.IPackage;
import com.zeusas.dp.ordm.entity.Order;
import com.zeusas.dp.ordm.entity.OrderDetail;
import com.zeusas.dp.ordm.entity.PackageDetail;
import com.zeusas.dp.ordm.entity.Product;
import com.zeusas.dp.ordm.service.DiffDetailService;
import com.zeusas.dp.ordm.service.ProductManager;

@Service
@Transactional
public class DiffDetailServiceImpl  extends BasicService<DiffDetail, Integer> implements DiffDetailService {

	private static Logger logger = LoggerFactory.getLogger(DiffDetailServiceImpl.class);
	
	final static String DDL_XML = "task/sync_ordersdetail_ddl.xml";
	final static String META = "SYNC_ORDERDETAIL";
	final static String ID_DIFFDETAIL = "DIFFDETAILID";
	
	@Autowired
  	private DiffDetailDao dao;
	@Autowired
  	private OrderDao orderdao;
	@Autowired
  	private OrderDetailDao orderDetaildao;
	@Autowired
	private PackageDao packageDao;
	@Autowired
	private PackageDetailDao packageDetailDao;
	@Autowired
	private IdGenService idGenService;
	
	
	
	@Override
	protected Dao<DiffDetail, Integer> getDao() {
		return dao;
	}

	@Override
	public List<DiffDetail> getDiffDetails(String orderNo) throws ServiceException {
		List<DiffDetail> diffDetails =new ArrayList<>();
		try {
			diffDetails = dao.getDiffDetails(orderNo);
		} catch (DaoException e) {
			logger.error("获取订单明细失败, orderNo={}",orderNo);
			throw new ServiceException(e);
		}
		return diffDetails;
	}

	/**
	 * Record: OrderNo, ProductId, RealQty, RealUnitPrice, RealTotalPrice, packagedetailId
	 * 
	 * @return
	 * @throws ServiceException
	 */
	@Override
	@Transactional(readOnly=true)
	public List<Record> getK3OrderDetail() throws ServiceException {
		List<Record> records = new ArrayList<>();
		Database k3DB = null;
		try {
			DDLDBMS.load(DDL_XML);
			DdlItem k3Ditem = DDLDBMS.getItem(META);
			k3DB = new Database(k3Ditem);
			Table k3DetailTB = k3DB.open("ORDERDETAIL_K3");
			records = k3DetailTB.values();
			k3DB.close();
			k3DB = null;
		} catch (Exception e) {
			throw new ServiceException(e);
		} finally {
			if (k3DB != null) {
				k3DB.close();
			}
		}
		return records;
	}
	@Override
	@Transactional(readOnly=true)
	public List<Order> getTodoOrder() throws ServiceException{
		List<Order> orders=new ArrayList<>();
		try {
			List<Order> todo = orderdao.findByDiffStatus(Order.diffStatus_todo);
			for (Order order : todo) {
				if(Order.status_WaitShip.equals(order.getOrderStatus())){
					orders.add(order);
				}
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		return orders;
	}
	
	@Override
	@Transactional
	public void saveDiffResult(Order order,List<Record> records) throws ServiceException{
		ProductManager  productManager =AppContext.getBean(ProductManager.class);
		//packageDetailId 与packageDetail 映射关系
		Map<String, PackageDetail> packageDetailMaps=new HashMap<>();
		//orderDetailId与orderDetail关系
		Map<Long, OrderDetail> orderDetailMaps=new HashMap<>();
		
		try {
			String orderNo=order.getOrderNo();
			IPackage Ipackage = packageDao.findByOrderNo(orderNo);
			Assert.notNull(Ipackage,"订单差分时包裹为空");
			List<PackageDetail> packageDetails = packageDetailDao.findByPackageId(Ipackage.getId());
			for (PackageDetail packageDetail : packageDetails) {
				packageDetailMaps.put(packageDetail.getId(), packageDetail);
			}
			List<OrderDetail> details = orderDetaildao.getOrderDetails(orderNo);
			for (OrderDetail orderDetail : details) {
				orderDetailMaps.put(orderDetail.getId(), orderDetail);
			}
			//Record: OrderNo, ProductId, RealQty, RealUnitPrice, RealTotalPrice, packagedetailId
			double realPrice=0d;
			for (Record record : records) {
				Integer productId=record.getInteger(2);
				Product product=productManager.get(productId);
				if(product==null){
					throw new ServiceException("产品不存在");
				}
				
				int realQty=record.getInteger(3);
				double realUnitPrice=record.getDouble(4);
				double realTotalPrice=record.getDouble(5);
				String packagedetailId =record.getString(6);
				
				if(!packageDetailMaps.containsKey(packagedetailId)){
					DiffDetail diffDetail=new DiffDetail(orderNo, productId, product.getName(), realQty, realUnitPrice, realTotalPrice);
					create(diffDetail);
				}else{
					PackageDetail packageDetail = packageDetailMaps.get(packagedetailId);
					packageDetail.setRealQty(realQty);
					OrderDetail orderDetail = orderDetailMaps.get(packageDetail.getOrderDetailId());
					orderDetail.setRealQty(orderDetail.getRealQty()+realQty);
					packageDetailDao.update(packageDetail);
				}
				realPrice+=realTotalPrice;
			}
			for (OrderDetail detail : details) {
				orderDetaildao.update(detail);
			}
			order.setRealFee(realPrice);
			order.setDiffStatus(Order.diffStatus_doen);
			orderdao.update(order);
		} catch (Exception e) {
			logger.error("保存订单差分结果错误，订单号{}",order.getOrderNo());
		}
	}

	@Override
	public void create(DiffDetail diffDetail) throws ServiceException {
		idGenService.lock(ID_DIFFDETAIL);
		try {
			String id = idGenService.generateStringId(ID_DIFFDETAIL);
			diffDetail.setId(Integer.parseInt(id));
			dao.save(diffDetail);
		} catch (Exception e) {
			throw new ServiceException(e);
		} finally {
			idGenService.unlock(ID_DIFFDETAIL);
		}
	}
	

}
