package com.zeusas.dp.ordm.service;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.record.FeatHdrRecord;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.alibaba.fastjson.JSON;
import com.zeusas.common.data.TypeConverter;
import com.zeusas.common.entity.Dictionary;
import com.zeusas.common.service.DictManager;
import com.zeusas.core.utils.AppContext;
import com.zeusas.core.utils.DateTime;
import com.zeusas.dp.ordm.entity.Counter;
import com.zeusas.dp.ordm.entity.Item;
import com.zeusas.dp.ordm.entity.MonthPresent;
import com.zeusas.dp.ordm.entity.PresentContext;
import com.zeusas.dp.ordm.entity.Product;

public class MonthPresentManagerTest {

	FileSystemXmlApplicationContext ctx;
	MonthPresentManager manager;
	ProductManager productManager;
	CounterManager counterManager;
	MonthPresentService monthPresentService;
	DictManager dictManager;
	@Before
	public void setUp() throws Exception {
		try {
			ctx = new FileSystemXmlApplicationContext("config/ordm-unit-test.xml");
			manager= AppContext.getBean(MonthPresentManager.class);
			productManager= AppContext.getBean(ProductManager.class);
			counterManager= AppContext.getBean(CounterManager.class);
			monthPresentService= AppContext.getBean(MonthPresentService.class);
			dictManager =AppContext.getBean(DictManager.class);
			ctx.start();
			counterManager.load();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void lookup() {
		dictManager.reload();
		Dictionary deliveryWay = dictManager.lookUpByCode("206", "08");
		System.out.println(deliveryWay);
		System.out.println("++");
	}
	
	@Test
	public void testFindByYearMonth() {
	    Map<String,MonthPresent> m=	manager.findByYearMonth(201801);
	    for (MonthPresent monthPresent : m.values()) {
	    	String lastUpdate =DateTime.formatDate(DateTime.YYYY_MM_DD_HMS, monthPresent.getLastUpdate());
	    	if(lastUpdate.contains("2018-01-19 17:52:3")){
//	    		System.out.println(monthPresent);
	    		List<PresentContext> contexts= monthPresent.getContext();
	    		int size =contexts.size();
	    		
	    		PresentContext fix= contexts.get(size-1);
	    		fix.setStatus(true);
	    		fix.setExcuteNo("10086");
//	    		System.out.println(contexts.get(size-1));
//	    		break;
	    		monthPresentService.update(monthPresent);
	    	}
	    	
//	    	String contextStr=monthPresent.getContext().toString();
//	    	if(contextStr.contains("deliveryWayId\":\"8")){
////	    		System.err.println(context);
//	    		List<PresentContext> contexts=monthPresent.getContext();
//	    		Integer errIndex=null;
//	    		for (PresentContext context : contexts) {
//	    			int index=context.getIndex();
//					for (Item item : context.getItems()) {
//						if("8".equals(item.getDeliveryWayId())){
//							errIndex=context.getIndex();
//							item.setDeliveryWayId("08");
//							context.setExcuteNo("10086");
//							context.setStatus(true);
//						}
//					}
//					if(errIndex!=null&&index>errIndex){
//						context.setExcuteNo(null);
//						context.setStatus(false);
//					}
//				}
//	    	}
//	    	manager.update(monthPresent);
		}
	}
//
	@Test
	public void testFindAll() throws IOException {
//		FileWriter fw = new FileWriter(new File("undo.txt"));
//		StringBuilder line = new StringBuilder();
		Integer yearMonth=manager.getyearmonth();
		
		Map<String, MonthPresent>  presents=new HashMap<>();
		Map<String, MonthPresent> map = manager.findByYearMonth(201801);
		Map<String, MonthPresent> map_2018 = manager.findByYearMonth(201802);
		for (Map.Entry<String, MonthPresent> e : map.entrySet()) {
			List<PresentContext> contexts = e.getValue().getContext();
			String counterCode = e.getValue().getCounterCode();
			Counter counter = counterManager.getCounterByCode(counterCode);
			if(counter==null){
				continue;
			}
//			String counterName = counter.getCounterName();
			for (PresentContext presentContext : contexts) {
				if (!presentContext.getStatus()) {
					
					if(!presents.keySet().contains(counterCode)){
						MonthPresent present=new MonthPresent(counter);
						present.setYearMonth(yearMonth);
						present.setCreator("root");
						presents.put(counterCode, present);
					}
					presents.get(counterCode).addContext(presentContext);
					
					Set<Item> items = presentContext.getItems();
					for (Item item : items) {
						String pid = item.getId();
						String deliveryWay = item.getDeliveryWayId();
						int num = item.getNum();
						Product product = productManager.get(TypeConverter.toInteger(pid));
						if (product == null) {
							continue;
						}
						
//						line.append(counterCode).append(",")//
//								.append(counterName).append(",")//
//								.append(deliveryWay).append(",")//
//								.append(product.getName()).append(",")//
//								.append(product.getProductCode()).append(",")//
//								.append(num).append("\r\n");
//						System.out.println(counterCode+" "+deliveryWay+" "+product.getProductCode()+" "+num);
//						fw.write(line.toString().trim());
					}
				}
			}
		}
		System.out.println("presents:"+presents.size());
		if (map_2018 == null || map_2018.isEmpty()) {
			System.out.println("map_2018 is null");
		}
		for (Map.Entry<String, MonthPresent> p : presents.entrySet()) {
			MonthPresent monthPresent = null;
			if (map_2018!=null && !map_2018.isEmpty()) {
				monthPresent= map_2018.get(p.getValue().getCounterCode());
			}
			if(monthPresent!=null){
				monthPresent.addContext(p.getValue().getContext());
				monthPresent.setLastUpdate(new Date());
				monthPresentService.update(monthPresent);
			}else{
				monthPresentService.createMonthPresent(p.getValue());
			}
		}
	}
	
	@Test
	public void 根据类型查询未发() throws IOException {
		Map<String, MonthPresent> map_2018 = manager.findByYearMonth(201802);
		int count =0;
		for (Map.Entry<String, MonthPresent> e : map_2018.entrySet()) {
			List<PresentContext> context = e.getValue().getContext();
			
			for (PresentContext presentContext : context) {
				if("11".equals(presentContext.getType())//
//						&&presentContext.getExcuteNo()==null
//						&&!presentContext.getStatus()
						){
					count++;
//					break;
				}
			}
		}
		System.out.println("智慧门店未发门店数量"+count);
	}
	
	@Test
	public void 根据发货方式查询未发() throws IOException {
		Map<String, MonthPresent> map_2018 = manager.findByYearMonth(201802);
		int count =0;
		
		Set<String> counters=new HashSet<>(map_2018.values().size()*4/3);
		Set<String> all=new HashSet<>(map_2018.values().size()*4/3);
		
		for (Map.Entry<String, MonthPresent> e : map_2018.entrySet()) {
			List<PresentContext> context = e.getValue().getContext();
			String counterCode=e.getValue().getCounterCode();
			for (PresentContext presentContext : context) {
				Set<Item> items = presentContext.getItems();
				for (Item item : items) {
					if("39".equals(item.getDeliveryWayId())){
						all.add(counterCode);
					}
					
					if("39".equals(item.getDeliveryWayId())
							&&presentContext.getExcuteNo()==null
							&&!presentContext.getStatus()){
						counters.add(counterCode);
					}
					break;
				}
			}
		}
		System.out.println("智慧门店所有数量"+ all.size());
		System.out.println("智慧门店未发门店数量"+ counters.size());
	}
	
	@Test
	public void 月度物料月底数据迁移() throws IOException {
		Integer yearMonth=manager.getyearmonth();
		//合并后的
		Map<String, MonthPresent>  presents=new HashMap<>();
		//上月数据
		List<MonthPresent> lastMonth = monthPresentService.findByYearMonth(201802);
		//当月数据
		Map<String, MonthPresent> map_2018 = manager.findByYearMonth(201803);
		
		for ( MonthPresent e : lastMonth) {
			List<PresentContext> contexts = e.getContext();
			String counterCode = e.getCounterCode();
			Counter counter = counterManager.getCounterByCode(counterCode);
			//Manager内无法通过counterCode找到counter 则构造一个 counter只用于构造MonthPresent
			if(counter==null){
				counter=new Counter();
				counter.setCounterCode(counterCode);
				counter.setCounterName(e.getCounterName());
			}
			for (PresentContext presentContext : contexts) {
				if (!presentContext.getStatus()) {
					
					if(!presents.keySet().contains(counterCode)){
						MonthPresent present=new MonthPresent(counter);
						present.setYearMonth(yearMonth);
						present.setCreator("root");
						presents.put(counterCode, present);
					}
					presents.get(counterCode).addContext(presentContext);
				}
			}
		}
		System.out.println("presents:"+presents.size());
		if (map_2018 == null || map_2018.isEmpty()) {
			System.out.println("map_2018 is null");
		}
		for (Map.Entry<String, MonthPresent> p : presents.entrySet()) {
			MonthPresent monthPresent = null;
			if (map_2018!=null && !map_2018.isEmpty()) {
				monthPresent= map_2018.get(p.getValue().getCounterCode());
			}
			if(monthPresent!=null){
				monthPresent.addContext(p.getValue().getContext());
				monthPresent.setLastUpdate(new Date());
				monthPresentService.update(monthPresent);
			}else{
				monthPresentService.createMonthPresent(p.getValue());
			}
		}
	}

}
