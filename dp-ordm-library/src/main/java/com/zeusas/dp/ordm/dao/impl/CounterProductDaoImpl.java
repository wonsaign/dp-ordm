package com.zeusas.dp.ordm.dao.impl;

import org.springframework.stereotype.Repository;

import com.zeusas.common.dao.CoreBasicDao;
import com.zeusas.core.utils.BeanDup;
import com.zeusas.dp.ordm.dao.CounterProductDao;
import com.zeusas.dp.ordm.entity.CounterProduct;

/**
 * 
 * @author fengx
 *@date 2016年12月22日 下午2:03:30
 */

@Repository
public class CounterProductDaoImpl extends CoreBasicDao<CounterProduct, String> implements CounterProductDao {

	
	@Override
	public void save(CounterProduct t) {
		CounterProduct update = get(t.getCounterCode());
		if (update == null) {
			super.save(t);
		} else {
			BeanDup.dupNotNull(t, update);
			update(update);
		}
	}
	
}
