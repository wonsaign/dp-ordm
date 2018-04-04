package com.zeusas.dp.ordm.active.bean;

import java.io.Serializable;

import com.zeusas.common.entity.Dictionary;

public class ActivityType implements Serializable {

	// 买赠 可选为1的活动类型
	final public static String TYPE_BUYGIVE = "01";
	// 买赠 可选为多个范围的活动类型
	final public static String TYPE_BUYGIVES = "11";
	// 买赠 产品自身
	final public static String TYPE_PRENSENTOWNER = "02";
	// 大礼包
	final public static String TYPE_BIGPACAKGE = "04";
	// 大礼包 的变型包
	final public static String TYPE_BIGPACAKGES = "41";
	/** 活动类型为固定套装 */
	final public static String TYPE_SUIT = "08";

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	/** 活动类型id */
	private String typeId;
	/** 活动名称 */
	private String name;
	/** 活动描述 */
	private String description;

	public ActivityType() {
	}

	public ActivityType(Dictionary dictionary) {
		this.typeId = dictionary.getHardCode();
		this.name = dictionary.name();
		this.description = dictionary.getSummary();
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
