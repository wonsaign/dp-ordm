package com.zeusas.dp.ordm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zeusas.core.dao.Dao;
import com.zeusas.core.service.BasicService;
import com.zeusas.dp.ordm.dao.CounterSerialDao;
import com.zeusas.dp.ordm.entity.Counter;
import com.zeusas.dp.ordm.entity.CounterSerial;
import com.zeusas.dp.ordm.entity.SerialSeller;
import com.zeusas.dp.ordm.service.CounterSerialService;

@Service
@Transactional
public class CounterSerialServiceImpl extends
		BasicService<CounterSerial, String> implements CounterSerialService {

	@Autowired
	private CounterSerialDao dao;

	@Override
	protected Dao<CounterSerial, String> getDao() {
		return dao;
	}

	@Override
	public List<SerialSeller> getGlobalSerial() {
		return get(Counter.GLOBALCODE).getSerials();
	}

	@Override
	public CounterSerial getGlobalCounterSerial() {
		CounterSerial counterSerial;
		 counterSerial=get(Counter.GLOBALCODE);
		return counterSerial;
	}
}
