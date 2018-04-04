package com.zeusas.dp.ordm.service;

import java.util.List;

import com.zeusas.core.service.IService;
import com.zeusas.dp.ordm.entity.CounterSerial;
import com.zeusas.dp.ordm.entity.SerialSeller;

public interface CounterSerialService  extends IService<CounterSerial, String>{
	
	List<SerialSeller>getGlobalSerial();
	CounterSerial  getGlobalCounterSerial();

}
