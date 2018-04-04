package com.zeusas.dp.ordm.entity;

import com.alibaba.fastjson.JSON;

/**
 * 定义实体类里面的一些常量 主要有两种状态YES/NO
 * 
 * @author fengx
 * @date 2016年12月12日 上午11:08:46
 */
@Deprecated
class ZeusConstants {

	/**
	 * 针对活动组里面的常量的定义
	 */
	// 该产品是必选的
	public final static int REQUIRED = 0;
	// 该产品时可选的
	public final static int Option = 1;
	// 该产品走价格策略
	public final static int DOPricePolicy = 0;
	// 该产品不走价格策略
	public final static int UnPricePolicy = 1;
	// 该产品就是购买类型
	public final static int DOPURCHASE = 0;
	// 该产品就是赠送类型
	public final static int DOPRESENT = 1;
	// 该物料、赠品走费用比
	public final static int UNCOSTFEE = 0;
	// 该物料、赠品不走费用比
	public final static int DOCOSTFEE = 1;
	// 活动处于有效期
	public final static int ACTIVE = 0;
	// 活动失效
	public final static int UNACTIVE = 1;
	//设置爆品的常量
	public final static String PRODUCTYPE="102";

	public String toString(){
		return JSON.toJSONString(this);
	}
}
