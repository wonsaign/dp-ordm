package com.zeusas.dp.ordm.utils;

public interface OrdmConfig {
	String ORDMCONFIG = "204";
	/** 订单超时时间配置*/
	String ORDERTIMEOUT="204007";
	
	/** 产品发货方式 */
	String DELIVERYWAY ="206";
	/** 仓库字典 */
	String WAREHOUSE ="205";
	/** 方式1:销售**/
	String DELIVERYWAY_SALES ="01";
	/**方式2:植物医生赠送*/
	String DELIVERYWAY_DRPRESENT ="02";
	/** 方式3：市场部赠送*/
	String DELIVERYWAY_MARKETPRESENT ="24";
	/*====
	 * From UserResource
	 */
	/** 物料配比 */
	String MATERIALDISCOUNT = "204006";
	/** 最小邮费金额 */
	String KEY_MINIAMOUNT = "204002";
	/** 包邮最小金额 */
	String KEY_MAXAMOUNT = "204001";
	/** 最大邮费 */
	String KEY_MAXPOSTAGE = "204003";
	/** 中间邮费 */
	String KEY_MIDPOSTAGE = "204004";
	/** 最小邮费(包邮) */
	String KEY_NOPOSTAGE = "204005";
	/** 个体户折扣 */
	public static final String KEY_INDIVIDUAL_DISCOUNT = "204008";
	
	/** 门店类型为直营*/
	String COUNTER_TYPE_DIRECT="直营";
}
