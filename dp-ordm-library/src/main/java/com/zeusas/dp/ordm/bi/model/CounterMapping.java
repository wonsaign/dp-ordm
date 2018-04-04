package com.zeusas.dp.ordm.bi.model;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import com.zeusas.dp.ordm.entity.Counter;

public class CounterMapping implements Mapping<Counter,String> {
    /** serialVersionUID */
	private static final long serialVersionUID = 6960943711104873753L;
	/** CountID mapping to Index of the matrix */
	Map<String,Integer> mapToIndex =  new LinkedHashMap<>(3000);
	/** ID (row number) mapping to Counter  */
	Map<Integer, Counter> mapping = new LinkedHashMap<>(3000);

	public CounterMapping(Collection<Counter> cc){
		build(cc);
	}

	@Override
	public void build(Collection<Counter> cc) {
		int idx = 0;
		clear();
		for (Counter c:cc){
			mapToIndex.put(c.getCounterCode(), idx);
			mapping.put(idx, c);
			idx++;
		}		
	}

	@Override
	public Counter get(int idx) {
		return mapping.get(idx);
	}

	@Override
	public Integer index(String PK) {
		return mapToIndex.get(PK);
	}

	@Override
	public int size() {
		return mapping.size();
	}

	@Override
	public void clear() {
		this.mapping.clear();
		this.mapToIndex.clear();
	}

	@Override
	public Counter[] mapping() {
		return mapping.values().toArray(new Counter[size()]);
	}	
}
