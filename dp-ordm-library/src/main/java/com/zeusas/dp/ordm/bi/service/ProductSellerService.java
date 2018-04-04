package com.zeusas.dp.ordm.bi.service;

import java.util.Collection;
import java.util.List;

import com.zeusas.core.service.ServiceException;
import com.zeusas.dp.ordm.entity.Counter;
import com.zeusas.dp.ordm.entity.Product;

public interface ProductSellerService {
	public void prepare(Collection<Counter> counter, List<Product> products);
	public void doExport();
	public void doFinal();
	public void close();
	public void loadData(String start, String end) throws ServiceException;
}
