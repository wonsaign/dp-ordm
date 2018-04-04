package com.zeusas.dp.ordm.bean;
/**
 * 把产品id Name 系列id name 封装在一起前台显示用的bean
 * 用于产品关联策略选择
 * @author shihx
 * @date 2016年12月28日 下午2:03:48
 */
public class ProductSeriesBean {
	
	//产品id 或者系列id
	private String id;
	//产品名 或者系列名
	private String name;
	
	public ProductSeriesBean() {
	}
	public ProductSeriesBean(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	

}
