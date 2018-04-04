package com.zeusas.dp.ordm.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.zeusas.common.data.TypeConverter;
import com.zeusas.common.entity.Dictionary;
import com.zeusas.common.service.DictManager;
import com.zeusas.core.utils.AppContext;
import com.zeusas.core.utils.DateTime;
import com.zeusas.dp.ordm.entity.StockReserve;
import com.zeusas.dp.ordm.stock.entity.Item;

public class StockReserveServiceTest {
	
	StockReserveService stockReserveService;
	DictManager dictManager;

	@Before
	public void setUp() throws Exception {
		try {
			@SuppressWarnings("resource")
			FileSystemXmlApplicationContext ctx = new FileSystemXmlApplicationContext("config/ordm-unit-test.xml");
			ctx.start();
			stockReserveService = AppContext.getBean(StockReserveService.class);
			dictManager =AppContext.getBean(DictManager.class);
			dictManager.reload();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testCreate() {
		try {
			StockReserve stockReserve =new StockReserve();
			stockReserve.setProductId(683);
			stockReserve.setProductName("X展架");
			stockReserve.setCreator("root");
			stockReserve.setStartTime(DateTime.toDate(DateTime.YYYY_MM_DD, "2018-01-01").getTime());
			stockReserve.setEndTime(DateTime.toDate(DateTime.YYYY_MM_DD, "2018-02-15").getTime());
			stockReserve.setValid(true);
			stockReserve.setDescription("活动");
			List<Item> items=new ArrayList<>(8);
			Dictionary warehouse= dictManager.get("205");
			for (Dictionary dictionary : warehouse.getChildren()) {
				Item item=new Item(683, TypeConverter.toInteger(dictionary.getHardCode()), 100);
				items.add(item);
			}
			stockReserve.setDetail(items);
			stockReserveService.create(stockReserve);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Test
	public void testFindReserveProduct() {
		try {
			List<StockReserve> reserves= stockReserveService.findReserveProduct(683);
			for (StockReserve stockReserve : reserves) {
				System.err.println(stockReserve);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testFindByDesc() {
		try {
			List<StockReserve> reserves= stockReserveService.findByDesc("活");
			for (StockReserve stockReserve : reserves) {
				System.err.println(stockReserve);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testFindAvalible() {
		try {
			List<StockReserve> reserves= stockReserveService.findAvalible();
			for (StockReserve stockReserve : reserves) {
				System.err.println(stockReserve);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testfindAvalibleByProductId() {
		try {
			List<StockReserve> reserves= stockReserveService.findAvalibleByProductId(683);
			for (StockReserve stockReserve : reserves) {
				System.err.println(stockReserve);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
