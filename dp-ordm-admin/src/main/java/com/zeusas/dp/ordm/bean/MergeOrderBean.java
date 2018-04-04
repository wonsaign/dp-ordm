package com.zeusas.dp.ordm.bean;

import java.text.DecimalFormat;
import java.util.List;

import com.zeusas.dp.ordm.entity.OrderCredentials;

public class MergeOrderBean {

	private String ocid;
	private Integer counterId;
	private String counterName;
	private String customerName;
	private String totalAmt;
	private String materialAmt;
	private String postage;
	private String useBlance;
	private String actualPay;
	private String customerDesc;
	private String checkerDesc;
	private List<String> imgUrl;
	private Boolean haveImg;
	private String usefulBalance;
	
	public MergeOrderBean() {
	}

	public MergeOrderBean(OrderCredentials credentials) {
		super();
		DecimalFormat df = new DecimalFormat("#0.00"); 
		this.ocid = credentials.getOcid();
		this.counterId = credentials.getCounterId();
		this.counterName = credentials.getCounterName();
		this.customerName = credentials.getCustomerName();
		
		Double totalAmt=credentials.getTotalAmt()==null?0.0:credentials.getTotalAmt();
		Double materialAmt=credentials.getMaterialAmt()==null?0.0:credentials.getMaterialAmt();
		Double postage=credentials.getPostage()==null?0.0:credentials.getPostage();
		Double useBlance=credentials.getUseBlance()==null?0.0:credentials.getUseBlance();
		Double actualPay=credentials.getActualPay()==null?0.0:credentials.getActualPay();
		this.totalAmt = df.format(totalAmt);
		this.materialAmt = df.format(materialAmt);
		this.postage = df.format(postage);
		this.useBlance = df.format(useBlance);
		this.actualPay = df.format(actualPay);
		
		this.customerDesc = credentials.getCounterName()==null?"":credentials.getCounterName();
		this.checkerDesc = credentials.getCheckerDesc()==null?"":credentials.getCheckerDesc();
		this.imgUrl = credentials.getImgUrl();
		this.haveImg = imgUrl.isEmpty()?false:true;
	}

	public String getOcid() {
		return ocid;
	}

	public void setOcid(String ocid) {
		this.ocid = ocid;
	}

	public Integer getCounterId() {
		return counterId;
	}

	public void setCounterId(Integer counterId) {
		this.counterId = counterId;
	}

	public String getCounterName() {
		return counterName;
	}

	public void setCounterName(String counterName) {
		this.counterName = counterName;
	}

	public String getTotalAmt() {
		return totalAmt;
	}

	public void setTotalAmt(String totalAmt) {
		this.totalAmt = totalAmt;
	}

	public String getMaterialAmt() {
		return materialAmt;
	}

	public void setMaterialAmt(String materialAmt) {
		this.materialAmt = materialAmt;
	}

	public String getPostage() {
		return postage;
	}

	public void setPostage(String postage) {
		this.postage = postage;
	}

	public String getUseBlance() {
		return useBlance;
	}

	public void setUseBlance(String useBlance) {
		this.useBlance = useBlance;
	}

	public String getActualPay() {
		return actualPay;
	}

	public void setActualPay(String actualPay) {
		this.actualPay = actualPay;
	}

	public String getCustomerDesc() {
		return customerDesc;
	}

	public void setCustomerDesc(String customerDesc) {
		this.customerDesc = customerDesc;
	}

	public String getCheckerDesc() {
		return checkerDesc;
	}

	public void setCheckerDesc(String checkerDesc) {
		this.checkerDesc = checkerDesc;
	}

	public List<String> getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(List<String> imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Boolean getHaveImg() {
		return haveImg;
	}

	public void setHaveImg(Boolean haveImg) {
		this.haveImg = haveImg;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getUsefulBalance() {
		return usefulBalance;
	}

	public void setUsefulBalance(Double usefulBalance) {
		DecimalFormat df = new DecimalFormat("#0.00");   
		this.usefulBalance = df.format(usefulBalance);
	}
	
}
