package com.zeusas.dp.ordm.entity;

import java.io.Serializable;

public class RuleContext  implements Serializable{
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -300432091159552348L;
	
	/** 规则一： 一口价*/
	FixedPrice fixedPrice;

	public FixedPrice getFixedPrice() {
		return fixedPrice;
	}

	public void setFixedPrice(FixedPrice fixedPrice) {
		this.fixedPrice = fixedPrice;
	}
	
	
	
	

}
