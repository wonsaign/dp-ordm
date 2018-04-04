package com.zeusas.dp.ordm.entity;

import com.alibaba.fastjson.JSON;
import com.zeusas.core.entity.ImmutableSetType;

/**
 * 定义产品关联策略关联的产品的集合
 * 
 * @author fengx
 * @date 2016年12月13日 下午1:32:31
 */
public class AssociateProductSetType extends ImmutableSetType<AssociatedProduct> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	public AssociateProductSetType() {
		super(AssociatedProduct.class);
	}

	public String toString(){
		return JSON.toJSONString(this);
	}
}