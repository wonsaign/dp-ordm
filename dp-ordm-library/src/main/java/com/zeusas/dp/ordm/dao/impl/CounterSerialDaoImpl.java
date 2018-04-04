package com.zeusas.dp.ordm.dao.impl;

import org.springframework.stereotype.Repository;

import com.zeusas.common.dao.CoreBasicDao;
import com.zeusas.core.utils.BeanDup;
import com.zeusas.dp.ordm.dao.CounterSerialDao;
import com.zeusas.dp.ordm.entity.CounterSerial;

@Repository
public class CounterSerialDaoImpl extends CoreBasicDao<CounterSerial, String> implements CounterSerialDao {

	@Override
	public void save(CounterSerial t) {
		CounterSerial update=get(t.getCounterCode());
		if(update==null){
			super.save(t);
		}else{
			BeanDup.dup(t, update);
			update(update);
		}
	}
}
