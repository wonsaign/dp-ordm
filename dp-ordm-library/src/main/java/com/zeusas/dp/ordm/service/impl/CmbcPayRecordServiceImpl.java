package com.zeusas.dp.ordm.service.impl;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.zeusas.common.service.IdGenService;
import com.zeusas.core.dao.Dao;
import com.zeusas.core.dao.DaoException;
import com.zeusas.core.service.BasicService;
import com.zeusas.core.service.ServiceException;
import com.zeusas.core.utils.IOUtils;
import com.zeusas.core.utils.StringUtil;
import com.zeusas.dp.ordm.dao.CmbcPayRecordDao;
import com.zeusas.dp.ordm.dao.OrderDao;
import com.zeusas.dp.ordm.entity.CmbcPayRecord;
import com.zeusas.dp.ordm.entity.Order;
import com.zeusas.dp.ordm.service.CmbcPayRecordService;
import com.zeusas.dp.ordm.service.CounterManager;
import com.zeusas.dp.ordm.service.OrderService;

/**
 * 
 * @author wangs
 */
@Service
public class CmbcPayRecordServiceImpl extends BasicService<CmbcPayRecord, String>
		implements CmbcPayRecordService {
	
	static Logger logger = LoggerFactory.getLogger(CmbcPayRecordServiceImpl.class);
	
	public final static String ID_CMBCPAYID = "CMBCPAYID";
	
	@Autowired
	private CmbcPayRecordDao dao;
	@Autowired
	private OrderService orderService;
	@Autowired
	private IdGenService idGenService;
	@Autowired
	private CounterManager counterManager;
	@Autowired
	private OrderDao orderDao;
	
	protected Dao<CmbcPayRecord, String> getDao() {
		return dao;
	}
	
	@Override
	@Transactional
	public synchronized void save(CmbcPayRecord cmbcPR) {
		OutputStream is = null;
		try {
			is = new FileOutputStream("cmbcpay_log.txt", true);
			is.write(JSON.toJSONString(cmbcPR).getBytes("utf-8"));
			is.write("\r\n".getBytes());
			is.close();
		} catch (Exception e) {
			logger.error("Save Cmbclogger error:{}", JSON.toJSONString(cmbcPR), e);
		} finally {
			IOUtils.close(is);
		}
		
		try {
			idGenService.lock(ID_CMBCPAYID);
			cmbcPR.setCmbcId(idGenService.generateDateId((ID_CMBCPAYID)));
			dao.save(cmbcPR);
		} catch (Exception e) {
			logger.error("{}", JSON.toJSONString(cmbcPR), e);
		} finally{
			idGenService.unlock(ID_CMBCPAYID);
		}
	}


	@Override
	public boolean checkSuccess(String cmbcOrderNo, String txtAmt,
			String billStatus) throws ServiceException {
		logger.info("Order info: {} {} {}",cmbcOrderNo,txtAmt, billStatus);
		try{
			Order order = orderService.getsingleOrder(cmbcOrderNo);
			// 订单状态为“0” 代表交易成功
			if("0".equals(billStatus)){
				// 根据支付宝的返回主键到数据库检索
				Assert.notNull(order,"订单为空");
				
				double cmbcTxtAmt = StringUtil.toDouble(txtAmt).doubleValue();
				
				// 订单号
				String database_orderNo = order.getOrderNo();
				// 訂單縂金額
				double order_total_fee = order.getPaymentFee().doubleValue();
				// 訂單使用余額
				double blance = order.getUseBalance().doubleValue();
				
				//FIX ME:by shi  : totalFee->totalFee+order.getUseBalance()
				if ( Math.abs(order_total_fee - cmbcTxtAmt - blance) <= 0.001
						&& database_orderNo.equals(cmbcOrderNo)) {
					// 数据库订单更改
					orderService.payByCmbcpay(order, cmbcTxtAmt);
					// TODO: repair it
//					// XXX:添加物料，可能会产生异常，session主键不唯一,禁止事务嵌套事务,事务里尽量调用dao以便提高效率,非事务只能调用service
//					orderService.clear();
//					
//					orderService.addActivityToOrder(order.getOrderNo(),
//							counterManager.getCounterByCode(order.getCounterCode()));
					return true;
				}
			}
			order.setOrderStatus(Order.status_ForFinancialRefuse);
			order.setCheckDesc("订单不合格,请联系财务");
			orderService.update(order);
			return false;
		}catch (ServiceException e) {
			logger.error("民生支付异常。order:{}",cmbcOrderNo,e);
		}
		return false;
	}
	
	// XXX:Unuse?
	@Transactional
	public void updateCmbcStatus(String payStatus,String orderNo,double paymentPrice) throws ServiceException{
		String hql = "orderNo =?";
		try{
			List<CmbcPayRecord> cmbcPays = dao.find(hql, orderNo);
			if(cmbcPays==null||cmbcPays.isEmpty()){
				return;
			}
			CmbcPayRecord cmbcPay = cmbcPays.get(0);
			String oNo = cmbcPay.getOrderNo();//订单单号
			double cmbcPrice =  Double.parseDouble(cmbcPay.getTxtAmt());
			if(!Strings.isNullOrEmpty(oNo)){
				Order order = orderService.getsingleOrder(oNo);
				Assert.notNull(order,"订单为空");
				order.setPayTypeId(Order.PayType_cmbcPay);//7代表支付宝支付
				order.setPaymentPrice(cmbcPrice);//支付金额
				order.setPayManName(order.getUserName());//支付人姓名
				order.setPayManId(order.getUserId());//支付人ID
				order.setOrderStatus(Order.Status_DoPay);//修改订单状态
				orderDao.update(order);
			}
		}catch(DaoException de){
			logger.warn("更新支付宝订单状态异常");
			throw new ServiceException(de);
		}
	}

	@Override
	@Transactional
	public CmbcPayRecord getByOrderNo(String orderNo) {
		String where = "orderNo = ?";
		try {
			List<CmbcPayRecord> cmbcPayRecords  = find(where, orderNo);
			return cmbcPayRecords.isEmpty() ? null : cmbcPayRecords.get(0);
		} catch (Exception e) {
			logger.error("condition:{},orderNo:{}",where,orderNo);
			throw new DaoException();
		}
	}
	
	
}
