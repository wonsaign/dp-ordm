package com.zeusas.dp.ordm.service;

import java.util.List;
import java.util.Map;

import com.zeusas.dp.ordm.active.model.ProductRule;
import com.zeusas.dp.ordm.entity.Counter;
import com.zeusas.dp.ordm.entity.Order;
import com.zeusas.dp.ordm.stock.entity.Item;
import com.zeusas.dp.ordm.stock.entity.RequestParameters;
import com.zeusas.dp.ordm.stock.entity.ResultMessage;

public interface StockService {
	ResultMessage get();

	ResultMessage getDataByID(List<RequestParameters> lrps);

	//根据仓库和产品id获取库存
	int getStockProductQty(String stockId, String productId);

	List<Item> getStrock(Integer productId);

	//获取所有库存(用于批量占用库存时)
	Map<Integer, Map<Integer, Item>> getallStock();

	//根据仓库和产品id获取库存以及打欠标志
	ResultMessage getDataByID(List<RequestParameters> requestParameters, Counter couter);
	
	int getSuitNum(ProductRule productRule,int suitNum,Order order);
	
	void setUpdated();
	
	void rebuildSrock() throws Exception ;
}
