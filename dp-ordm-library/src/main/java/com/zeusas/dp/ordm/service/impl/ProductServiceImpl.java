package com.zeusas.dp.ordm.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;
import com.zeusas.core.dao.Dao;
import com.zeusas.core.service.BasicService;
import com.zeusas.core.service.ServiceException;
import com.zeusas.core.utils.AppConfig;
import com.zeusas.dp.ordm.dao.ProductDao;
import com.zeusas.dp.ordm.entity.Product;
import com.zeusas.dp.ordm.service.ProductService;

@Service
@Transactional
public class ProductServiceImpl extends BasicService<Product, Integer>
		implements ProductService {
	@Autowired
	private ProductDao productDao;

	@Override
	protected Dao<Product, Integer> getDao() {
		return productDao;
	}

	public void save(Product p) {
		Product t = get(p.getProductId());
		if (t != null) {
			throw new ServiceException("产品已经存在！");
		}
		String uri = p.getImageURL();
		p.setImageURL(checkImage(uri));
		productDao.save(p);
	}

	private String checkImage(String uri) {
		if (Strings.isNullOrEmpty(uri) || uri.length() < 16) {
			return uri;
		}
		String prefix = AppConfig.getVfsPrefix();
		if (uri.startsWith(prefix)) {
			uri = uri.substring(prefix.length());
		}
		return uri;
	}
	
	public Product update(Product p) {
		String uri = p.getImageURL();
		p.setImageURL(checkImage(uri));
		return productDao.update(p);
	}

	public int update(Integer pk, Map<String, Object> values)
			throws ServiceException {
		String uri = (String) values.get("imageURL");
		if (!Strings.isNullOrEmpty(uri)) {
			values.put("imageURL", checkImage(uri));
		}
		return productDao.update(pk, values);
	}
}
