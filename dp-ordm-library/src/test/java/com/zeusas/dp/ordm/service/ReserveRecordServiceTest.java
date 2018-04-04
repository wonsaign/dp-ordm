package com.zeusas.dp.ordm.service;

import static org.junit.Assert.fail;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.shiro.util.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.zeusas.core.utils.AppContext;
import com.zeusas.dp.ordm.entity.ReserveRecord;

public class ReserveRecordServiceTest {
	ReserveRecordService reserveRecordService;

	@Before
	public void setUp() throws Exception {
		try {
			@SuppressWarnings("resource")
			FileSystemXmlApplicationContext ctx = new FileSystemXmlApplicationContext("config/ordm-unit-test.xml");
			ctx.start();
			reserveRecordService = AppContext.getBean(ReserveRecordService.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testFindRecordByStatusInteger() {
		System.out.println(reserveRecordService.findRecordByStatus(2).size());
	}

	@Test
	public void testFindRecordByStatusCollectionOfInteger() {
		Integer[] in = new Integer[] { 2, 3 };
		System.out.println(reserveRecordService.findRecordByStatus(Arrays.asList(in)).size());
	}

	@Test
	public void testGetRecordByOrderNo() {
		System.out.println(reserveRecordService.getRecordByOrderNo("201709200008"));
	}

	@Test
	public void testAdd() {
		reserveRecordService.add("201709200008");
	}

	@Test
	public void testFindWeitShipByCounterCode() {
		System.out.println(reserveRecordService.findWeitShipByCounterCode("000005"));
	}

	@Test
	public void testCancleReserve() {
		reserveRecordService.cancleReserve(1709200029L);
	}

	@Test
	public void testCancleReserveActivity() {
		reserveRecordService.cancleReserveActivity(1709200022L);
		Assert.isTrue((ReserveRecord.STATUS_CANCLE.equals(reserveRecordService.get(1709200022L).getStatus())), "");
	}

	@Test
	public void testFindByCustomerId() {
		System.out.println(reserveRecordService.findByCustomerId("15508").size());
	}

	@Test
	public void testFindByCustomerIdAndStatus() {
		List<Integer> status = new ArrayList<Integer>();
		status.add(2);
		status.add(5);
		System.out.println(reserveRecordService.findByCustomerIdAndStatus("1", status).size());
	}

	@Test
	public void testFindByProductId() {
		List<Integer> productIds = new ArrayList<Integer>();
		productIds.add(23885);
		productIds.add(22636);
		System.out.println(reserveRecordService.findByProductId(productIds).size());
	}

	@Test
	public void testFindByTime() throws Exception {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date reserveStart = format.parse("2017-08-14 09:32:08");
		Date reserveEnd = format.parse("2017-12-14 09:32:08");
		System.out.println(reserveRecordService.findByTime(23885, reserveStart, reserveEnd));
	}

	@Test
	public void testChangeSingleStatus() {
		reserveRecordService.changeSingleStatus(1, 1710140114L);
	}

	@Test
	public void testChangeActivityStatus() {
//		reserveRecordService.changeActivityStatus();
	}

}
