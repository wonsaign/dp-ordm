package com.zeusas.dp.ordm.bi.model;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import com.zeusas.dp.ordm.entity.Product;

public class ProductMapping implements  Mapping<Product,Integer>  {
	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	// Product -> Index
	final Map<Integer, Integer> mapToIndex = new LinkedHashMap<>(1000);
	// Index - > Product
	final Map<Integer, Product> mapping = new LinkedHashMap<>(1000);

	public ProductMapping(Collection<Product> pp) {
		build(pp);
	}
	
	public void build(Collection<Product> pp){
		clear();
		int idx = 0;
		for (Product p : pp) {
			mapToIndex.put(p.getProductId(), idx);
			mapping.put(idx, p);
			idx++;
		}
	}

	public Product[] mapping(){
		return mapping.values().toArray(new Product[size()]);
	}
	
	@Override
	public Product get(int idx) {
		return mapping.get(idx);
	}

	@Override
	public Integer index(Integer PK) {
		return mapToIndex.get(PK);
	}
	
	public int size(){
		return mapToIndex.size();
	}

	@Override
	public void clear() {
		this.mapping.clear();
		this.mapToIndex.clear();
	}
}
