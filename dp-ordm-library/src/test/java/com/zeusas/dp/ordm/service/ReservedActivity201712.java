package com.zeusas.dp.ordm.service;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.zeusas.core.utils.AppContext;
import com.zeusas.dp.ordm.active.model.PeriodActivity;
import com.zeusas.dp.ordm.rev.entity.ReservedActivity;
import com.zeusas.dp.ordm.rev.entity.ReservedActivityContext;
import com.zeusas.dp.ordm.rev.service.ReservedActivityManager;


public class ReservedActivity201712 {
	private FileSystemXmlApplicationContext aContext;
	ReservedActivityManager reservedActivityManager;
	CounterManager counterManager;
	ProductManager pm;

	void init() {
		aContext = new FileSystemXmlApplicationContext("config/ordm-unit-test.xml");
		aContext.start();
		counterManager = AppContext.getBean(CounterManager.class);
		reservedActivityManager = aContext.getBean(ReservedActivityManager.class);
		pm = aContext.getBean(ProductManager.class);
		//pm.reload();
	}


	public void 预定会测试(){
		ReservedActivity reservedActivity = new ReservedActivity();
		reservedActivity.setContent(null);
		reservedActivity.setSubject(null);
		reservedActivity.setStatus(0);
		reservedActivity.setType(null);
		reservedActivity.setRevId(1001);
		
		ReservedActivityContext  rac = new ReservedActivityContext();
		Set<Integer> products = new HashSet<>();
		products.add(23097);
		products.add(23885);
		products.add(23888);
		products.add(21380);
		Set<String> activities = new HashSet<>();
		activities.add("17101651");

		
		List<PeriodActivity> stocks = new ArrayList<>();
		PeriodActivity pa1 = new PeriodActivity();
		pa1.setTo(20171225);
		pa1.setFrom(20171116);
		Set<String> s1 = new HashSet<>();
		s1.add("23228");
		s1.add("23229");
		s1.add("23227");
		s1.add("23231");
		s1.add("10615");
		s1.add("10118");
		pa1.setVal(s1);
		stocks.add(pa1);
		
		rac.setStocks(stocks);
		rac.setProducts(products);
		rac.setActivities(activities);
		rac.setZones(null);
		rac.setCounters(null);
		
		reservedActivity.setContext(rac);
		reservedActivityManager.add(reservedActivity);
	}
	
	public static void main(String[] args) {
		ReservedActivity201712 a = new ReservedActivity201712();
		a.init();
		a.预定会测试();
	}


}
