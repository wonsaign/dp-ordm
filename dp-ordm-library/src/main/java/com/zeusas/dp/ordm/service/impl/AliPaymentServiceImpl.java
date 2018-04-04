package com.zeusas.dp.ordm.service.impl;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.zeusas.core.dao.Dao;
import com.zeusas.core.dao.DaoException;
import com.zeusas.core.http.ActionException;
import com.zeusas.core.service.BasicService;
import com.zeusas.core.service.ServiceException;
import com.zeusas.core.utils.IOUtils;
import com.zeusas.core.utils.StringUtil;
import com.zeusas.dp.ordm.dao.AliPaymentDao;
import com.zeusas.dp.ordm.dao.OrderDao;
import com.zeusas.dp.ordm.entity.AliPayment;
import com.zeusas.dp.ordm.entity.Order;
import com.zeusas.dp.ordm.entity.OrderCredentials;
import com.zeusas.dp.ordm.service.AliPaymentService;
import com.zeusas.dp.ordm.service.OrderCredentialsService;
import com.zeusas.dp.ordm.service.OrderService;
import com.zeusas.security.auth.entity.AuthUser;

/**
 * 
 * @author fengx
 * @date 2017年1月17日 下午3:57:32
 */
@Service
public class AliPaymentServiceImpl extends BasicService<AliPayment, String>
		implements AliPaymentService {
	static Logger logger = LoggerFactory.getLogger(AliPaymentServiceImpl.class);
	
	@Autowired
	private AliPaymentDao dao;
	@Autowired
	private OrderDao orderDao;
	
	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderCredentialsService credentialsService;
	
	protected Dao<AliPayment, String> getDao() {
		return dao;
	}
	
	@Transactional
	public synchronized void save(AliPayment alip) {
		OutputStream is = null;
		try {
			is = new FileOutputStream("alipay_log.txt", true);
			is.write(JSON.toJSONString(alip).getBytes("utf-8"));
			is.write("\r\n".getBytes());
			is.close();
			is = null;
		} catch (Exception e) {
			logger.error("Save logger error:{}", JSON.toJSONString(alip), e);
		} finally {
			IOUtils.close(is);
		}
		
		try {
			dao.save(alip);
		} catch (Exception e) {
			logger.error("{}", JSON.toJSONString(alip), e);
		}
	}

	/**
	 * 检查是否交易成功
	 * 
	 * @param subject Order Number
	 * @param total_fee Total amount
	 * @param out_trade_no trade number
	 */
	public boolean checkSuccess(String subject, String ali_total_fee,
			String ali_trade_no) throws ServiceException {
		logger.info("Order info: {} {} {}",subject,ali_total_fee, ali_trade_no);
		
		// 根据支付宝的返回主键到数据库检索
		Order order = orderService.get(StringUtil.toLong(subject,-1));
		Assert.notNull(order,"订单为空");

		double aliTradeAmt = StringUtil.toDouble(ali_total_fee).doubleValue();

		// 订单号
		String order_no = order.getOrderNo();
		// 訂單縂金額
		double order_total_fee = order.getPaymentFee().doubleValue();
		// 訂單使用余額
		double blance = order.getUseBalance().doubleValue();
		
		//FIX ME:by shi  : totalFee->totalFee+order.getUseBalance()
		if ( Math.abs(order_total_fee - aliTradeAmt - blance) <= 0.001
				&& order_no.equals(ali_trade_no)) {
			// 数据库订单更改
			try {
				orderService.payByAlipay(order, aliTradeAmt);
				return true;
			}catch(Exception e) {
				logger.error("支付宝支付异常",subject);
				return false;
			}
		}
		logger.info("AliPay Fail,金额条件{},订单条件{}",Math.abs(order_total_fee - aliTradeAmt - blance) <= 0.001,
				order_no.equals(ali_trade_no));
		order.setOrderStatus(Order.status_ForFinancialRefuse);
		order.setCheckDesc("订单不合格,请联系财务");
		orderService.update(order);
		return false;
	}
	
	/**
	 * 检查合并订单是否交易成功
	 * 
	 * @param subject 子订单号
	 * @param total_fee 总支付金额（合并订单）
	 * @param out_trade_no 合并订单号
	 */
	@Transactional
	public boolean combineCheckSuccess(String ali_total_fee,
			String ali_trade_no) throws ServiceException {
		if(StringUtil.isEmpty(ali_trade_no)) {
			logger.info("合并订单号为空");
			return false;
		}
		logger.info("Order info: {} {}",ali_total_fee, ali_trade_no);
		
		OrderCredentials orderCredentials = credentialsService.get(ali_trade_no);
		
		if(orderCredentials==null){
			logger.info("找不到该合并订单：{}",ali_trade_no);
			return false;
		}
		
		List<String> orderNos = orderCredentials.getOrderNos();
		
		//子订单总金额之和
		double orders_total_fee = 0.0;
		
		// 根据支付宝的返回订单号到数据库检索
		for(String orderNo:orderNos){
			Order order = orderService.getsingleOrder(orderNo);
			Assert.notNull(order,"订单为空");
			orders_total_fee += order.getPaymentFee();
		}
		
		double aliTradeAmt = StringUtil.toDouble(ali_total_fee).doubleValue();

		// 訂單縂金額
		double order_total_fee = orderCredentials.getTotalAmt().doubleValue();
		// 訂單使用余額
		double blance = orderCredentials.getUseBlance().doubleValue();
		
		
		//FIX ME:by shi  : totalFee->totalFee+order.getUseBalance()
		if ( Math.abs(order_total_fee - aliTradeAmt - blance) <= 0.001 &&
				Math.abs(orders_total_fee - order_total_fee) <= 0.001) {
			// 数据库订单更改
			
		  for(String orderNo:orderNos){
			  try {
			  Order order = orderService.getsingleOrder(orderNo);
			  double paymentFee = order.getPaymentFee();
			  // 使用余额并且全额支付
			  if(blance - paymentFee >= 0){
				  blance -= paymentFee;
				  order.doPayment(paymentFee, 0, Order.PayType_aliPay, orderCredentials);
				  orderService.update(order);
				  order = orderService.getsingleOrder(orderNo);
				  orderService.payByAlipay(order,0);
			  } else if (blance > 0.001) {
			  // 扣除余额后，需支付金额
				  double payable = paymentFee - blance;
				  order.doPayment(blance, payable, Order.PayType_aliPay, orderCredentials);
				  orderService.update(order);
				  order = orderService.getsingleOrder(orderNo);
				  orderService.payByAlipay(order,payable);
				  blance = 0;
			  }else{
				  orderService.payByAlipay(order,order.getPaymentFee());
			  }
			  }catch(Exception e) {
				  logger.error("支付宝支付异常",orderNo);
				  return false;
			  }
		  }
		  return true;
		}
		logger.info("AliPay Fail,金额条件{},订单条件{}",Math.abs(order_total_fee - aliTradeAmt - blance) <= 0.001,
				Math.abs(orders_total_fee - order_total_fee) <= 0.001);
		//TODO
		//待处理：当不满足if条件时，如何处理子订单
		for(String orderNo:orderNos){
			 Order order = orderService.getsingleOrder(orderNo);
			 order.setOrderStatus(Order.status_ForFinancialRefuse);
			 order.setCheckDesc("订单不合格,请联系财务");
			 orderService.update(order);
		}
		return false;
	}

	/**
	 * 说明：在点击支付按钮的时候，更新用户支付人姓名以及支付人
	 * @author Wonsign
	 * @param orderID
	 * @param user
	 */
	@Transactional
	public boolean updateOUN(String orderID, AuthUser user)
			throws ServiceException {
		// 根据订单id获取当前订单
		Order order = orderDao.get(StringUtil.toLong(orderID));
		// 更新订单支付人
		try {
			if(order == null) {
				throw new ActionException("order is null");
			}
			order.setPayManName(user.getCommonName());
			order.setPayManId(user.getLoginName());
			orderDao.update(order);
			// 更新支付凭证支付人
			OrderCredentials credentials = credentialsService
					.getOrderCredentials(order.getOrderNo());
			credentials.setPayManId(user.getLoginName());
			credentials.setPayManName(user.getCommonName());
			credentialsService.update(credentials);
			return true;
		} catch (ActionException e) {
			logger.error("Order is null", e);
		} catch (Exception e) {
			logger.error("更新订单支付人失败,单号:{}",orderID, e);
		}
		return false;
	}
	
	@Transactional
	public void updateAlipayStatus(String payStatus,String orderNo,double paymentPrice) throws ServiceException{
		String hql = "OutTradeNo =?";
		try{
			List<AliPayment> alipays = dao.find(hql, orderNo);
			if(alipays==null||alipays.isEmpty()){
				return;
			}
			AliPayment alipay = alipays.get(0);
			String subject = alipay.getSubject();//订单主键
			double alipayPrice =  Double.parseDouble(alipay.getTotalFee());
			if(!Strings.isNullOrEmpty(subject)){
				Order order = orderDao.get(StringUtil.toLong(subject));
				Assert.notNull(order,"订单为空");
				order.setPayTypeId("1");//1代表支付宝支付
				order.setPaymentPrice(alipayPrice);//支付金额
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
	
	/**
	 * 说明：（合并支付）在点击支付按钮的时候，更新用户支付人姓名以及支付人
	 * @author wangbin
	 * @param orderNos
	 * @param user
	 */
	@Transactional
	public boolean updateCombineOUN(List<String> orderNos, AuthUser user) throws ServiceException {
		// 根据订单号获取当前订单
		List<Order> orders = new ArrayList<Order>();
		for (String orderNo : orderNos) {
			Order order = orderService.getsingleOrder(orderNo);
			orders.add(order);
		}
		// 更新订单支付人
		try {
			for (Order order : orders) {
				if (order == null) {
					logger.warn("存在定单为空！");
					continue;
				}
				order.setPayManName(user.getCommonName());
				order.setPayManId(user.getLoginName());
				orderDao.update(order);
				// 更新支付凭证支付人
				OrderCredentials credentials = credentialsService.getOrderCredentials(order
						.getOrderNo());
				credentials.setPayManId(user.getLoginName());
				credentials.setPayManName(user.getCommonName());
				credentialsService.update(credentials);
			}
			return true;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
}
