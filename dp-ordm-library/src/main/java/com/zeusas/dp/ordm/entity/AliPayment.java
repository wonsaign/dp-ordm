package com.zeusas.dp.ordm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.JSON;
import com.zeusas.core.entity.IEntity;

/**
 * UserDetail entity. @author fx 用户细节表，用户的地址，姓名等
 */
@Entity
@Table(name = "bus_alipay")
public class AliPayment implements IEntity {

	private static final long serialVersionUID = 1L;
	
	public final static String ID_PAYMENTID="PAYMENTID";
	// 用户ID
	@Id
	@Column(name = "PAYMENTID", unique = true, nullable = false)
	private String paymentId;// 主键 String/Interger类型你自己定义
	
	@Column(name = "BODY")
	private String body;// 备注信息
						// %E5%8D%B3%E6%97%B6%E5%88%B0%E8%B4%A6%E6%B5%8B%E8%AF%95
	@Column(name = "BUYEREMAIL")
	private String buyerEmail;// 付款人邮箱 wonsaign%40foxmail.com
	@Column(name = "BUYERID")
	private String buyerId;// 付款人ID 2088602130199612
	@Column(name = "EXTERFACE")
	private String exterface;// 接口号名称 create_direct_pay_by_user
	@Column(name = "ISSUCCESS")
	private String isSuccess;// 是否成功 T
	@Column(name = "NOTIFYID")
	private String notifyId;// 异步通知ID
							// RqPnCoPT3K9%252Fvwbh3InZd4701iHO39cY9zvZRH7Ei07E%252Fgk3BcieIEDPqZNlOsqxUipF
	@Column(name = "NOTIFYTIME")
	private String notifyTime;// 异步通知时间 2017-01-11+14%3A36%3A43
	@Column(name = "NOTIFYTYPE")
	private String notifyType;// 异步通知类型 trade_status_sync
	@Column(name = "OUTTRADENO")
	private String outTradeNo;// 页面显示的表 test20170111143322
	@Column(name = "PAYMENTTYPE")
	private String paymentType;// 支付类型 1
	@Column(name = "SELLEREMAIL")
	private String sellerEmail;// 收款方邮箱 mhzfzfb%40drplant.com.cn
	@Column(name = "SELLERID")
	private String sellerId;// 收款方ID 2088521492867561
	@Column(name = "SUBJECT")
	private String subject;// 订单名称 test%E5%95%86%E5%93%81123
	@Column(name = "TOTALFEE")
	private String totalFee;// 付款金额 0.01
	@Column(name = "TRADENO")
	private String tradeNo;// 订单号 2017011121001004610212338566
	@Column(name = "TRADESTATUS")
	private String tradeStatus;// 订单状态 TRADE_SUCCESS
	@Column(name = "SIGN")
	private String sign;// 签名
						// hNgW4pqGgWurPRoXBRDUvcpvqoYzxCGeA2MeF7aUkhjgEeVjHI%2BbS7EO%2FWiJTP7HLXTGH4PMuToQy%2BHHFCM8dDA7h3WL78HtpQyPQywE%2FeOA5jf1ga%2BBP1kDMso4vFlcskYzpEtj%2FlWr%2Fxez7Tp0osPmGWqmQrAhuttnSNChISQ%3D
	@Column(name = "SIGNTYPE")
	private String signType;// 签名类型 RSA

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getBuyerEmail() {
		return buyerEmail;
	}

	public void setBuyerEmail(String buyerEmail) {
		this.buyerEmail = buyerEmail;
	}

	public String getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}

	public String getExterface() {
		return exterface;
	}

	public void setExterface(String exterface) {
		this.exterface = exterface;
	}

	public String getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(String isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getNotifyId() {
		return notifyId;
	}

	public void setNotifyId(String notifyId) {
		this.notifyId = notifyId;
	}

	public String getNotifyTime() {
		return notifyTime;
	}

	public void setNotifyTime(String notifyTime) {
		this.notifyTime = notifyTime;
	}

	public String getNotifyType() {
		return notifyType;
	}

	public void setNotifyType(String notifyType) {
		this.notifyType = notifyType;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getSellerEmail() {
		return sellerEmail;
	}

	public void setSellerEmail(String sellerEmail) {
		this.sellerEmail = sellerEmail;
	}

	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public String getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}
	
	
	

	public int hashCode() {
		return (paymentId == null) ? 0 : paymentId.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AliPayment other = (AliPayment) obj;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		if (buyerEmail == null) {
			if (other.buyerEmail != null)
				return false;
		} else if (!buyerEmail.equals(other.buyerEmail))
			return false;
		if (buyerId == null) {
			if (other.buyerId != null)
				return false;
		} else if (!buyerId.equals(other.buyerId))
			return false;
		if (exterface == null) {
			if (other.exterface != null)
				return false;
		} else if (!exterface.equals(other.exterface))
			return false;
		if (isSuccess == null) {
			if (other.isSuccess != null)
				return false;
		} else if (!isSuccess.equals(other.isSuccess))
			return false;
		if (notifyId == null) {
			if (other.notifyId != null)
				return false;
		} else if (!notifyId.equals(other.notifyId))
			return false;
		if (notifyTime == null) {
			if (other.notifyTime != null)
				return false;
		} else if (!notifyTime.equals(other.notifyTime))
			return false;
		if (notifyType == null) {
			if (other.notifyType != null)
				return false;
		} else if (!notifyType.equals(other.notifyType))
			return false;
		if (outTradeNo == null) {
			if (other.outTradeNo != null)
				return false;
		} else if (!outTradeNo.equals(other.outTradeNo))
			return false;
		if (paymentId == null) {
			if (other.paymentId != null)
				return false;
		} else if (!paymentId.equals(other.paymentId))
			return false;
		if (paymentType == null) {
			if (other.paymentType != null)
				return false;
		} else if (!paymentType.equals(other.paymentType))
			return false;
		if (sellerEmail == null) {
			if (other.sellerEmail != null)
				return false;
		} else if (!sellerEmail.equals(other.sellerEmail))
			return false;
		if (sellerId == null) {
			if (other.sellerId != null)
				return false;
		} else if (!sellerId.equals(other.sellerId))
			return false;
		if (sign == null) {
			if (other.sign != null)
				return false;
		} else if (!sign.equals(other.sign))
			return false;
		if (signType == null) {
			if (other.signType != null)
				return false;
		} else if (!signType.equals(other.signType))
			return false;
		if (subject == null) {
			if (other.subject != null)
				return false;
		} else if (!subject.equals(other.subject))
			return false;
		if (totalFee == null) {
			if (other.totalFee != null)
				return false;
		} else if (!totalFee.equals(other.totalFee))
			return false;
		if (tradeNo == null) {
			if (other.tradeNo != null)
				return false;
		} else if (!tradeNo.equals(other.tradeNo))
			return false;
		if (tradeStatus == null) {
			if (other.tradeStatus != null)
				return false;
		} else if (!tradeStatus.equals(other.tradeStatus))
			return false;
		return true;
	}

	public String toString() {
		return JSON.toJSONString(this);
	}
}