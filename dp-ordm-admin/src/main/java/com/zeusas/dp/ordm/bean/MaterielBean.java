package com.zeusas.dp.ordm.bean;
/**
 * 产品关联策略 关联赠品 物料
 * @author shihx
 * @date 2016年12月28日 下午4:27:16
 */
public class MaterielBean {
	private Integer pid;
	private String name;
	private double coeff;
	public Integer getPid() {
		return pid;
	}
	public void setPid(Integer pid) {
		this.pid = pid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getCoeff() {
		return coeff;
	}
	public void setCoeff(double coeff) {
		this.coeff = coeff;
	}
	
	
}
