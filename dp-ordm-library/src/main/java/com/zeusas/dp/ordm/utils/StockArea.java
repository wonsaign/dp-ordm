package com.zeusas.dp.ordm.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.util.Assert;

/**
 * 使用接口来定义货舱常量
 * @author wangs
 *	
 */
public interface StockArea {
	/**
	 * 内蒙被划分了几个区域，将所有的内蒙全部加入，前段需要进行城市判断，选择仓库
	 */
	Set<String> STOCK = new HashSet<String>(Arrays.asList(
			// 北京仓 :10118  北京市     天津市     河北省       山西省    山东省  内蒙赤峰     呼市  宁夏回族自治区   内蒙包头市-乌海市
			"10118_北京市天津市河北省山西省山东省宁夏回族自治区内蒙古自治区-赤峰市呼和浩特市拉尔市包头市乌海市",
			// 武汉仓:10615 湖北省     湖南省     江西省   安徽省     河南省   江苏省  上海市  浙江省
			"10615_湖北省湖南省江西省安徽省河南省-无",
			// 西安仓:陕西省   甘肃省    新疆维吾尔自治区          青海省   
			"23227_陕西省甘肃省新疆维吾尔自治区青海省-无",
			// 成都仓:四川省    重庆市    西藏自治区
			"23228_四川省重庆市西藏自治区-无",
			// 昆明仓:云南省     贵州省
			"23229_云南省贵州省-无",
			// 广州仓:广东省     广西壮族自治区     福建省    海南省
			"23232_广东省广西壮族自治区福建省海南省-无",
			// 沈阳仓:辽宁省      吉林省     黑龙江省    内蒙古自治区-拉尔市
		    "23231_辽宁省吉林省黑龙江省内蒙古自治区-拉尔市",
			// 华东仓:浙江省   江苏省  上海市 
			"23230_浙江省江苏省上海市-无"
		));
	
	public static String getStockId(String province, String city){
		Assert.notNull(province,"");
		// 从全国定义的仓库定义里找出柜台号
		Set<String> stocks = StockArea.STOCK;
		// 内蒙古划分多仓
		final String neiMeng = "内蒙古自治区";
		if(neiMeng.equals(province)){
			return "10118";
		}
		for (String stock : stocks) {
			if(stock.contains(province) || stock.split("-")[1].contains(province)){
				// stock = "10615_湖北省湖南省江西省安徽省-无";
				String stockId = stock.split("_")[0];
				return stockId;
			}	
		}
		// 禁止返回null,设置0为空
		return null;
	}
}
