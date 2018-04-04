package com.zeusas.dp.ordm.service;


import java.util.List;

import com.zeusas.core.service.IService;
import com.zeusas.dp.ordm.entity.CounterProduct;
import com.zeusas.dp.ordm.entity.ProductSeller;

public interface CounterProductService extends IService<CounterProduct, String> {
	
	List<ProductSeller>getGlobalProduct();

}
