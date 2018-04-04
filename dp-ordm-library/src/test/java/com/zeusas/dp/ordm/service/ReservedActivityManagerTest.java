package com.zeusas.dp.ordm.service;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.util.Assert;

import com.zeusas.core.utils.AppContext;
import com.zeusas.dp.ordm.entity.CartDetail;
import com.zeusas.dp.ordm.entity.Counter;
import com.zeusas.dp.ordm.rev.entity.ReservedActivity;
import com.zeusas.dp.ordm.rev.service.ReservedActivityManager;
import com.zeusas.dp.ordm.rev.service.ReservedActivityService;
import com.zeusas.dp.ordm.service.CounterManager;

public class ReservedActivityManagerTest {
	ReservedActivityManager reservedActivityManager;
	CounterManager counterManager;

	@Before
	public void setUp() throws Exception {
		try {
			@SuppressWarnings("resource")
			FileSystemXmlApplicationContext ctx = new FileSystemXmlApplicationContext("config/ordm-unit-test.xml");
			ctx.start();
			reservedActivityManager = AppContext.getBean(ReservedActivityManager.class);
			counterManager = AppContext.getBean(CounterManager.class);
			counterManager.load();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetMyReservedActivity() {
		Counter counter = counterManager.getCounterByCode("000005");
		System.out.println(counter.getWarehouses());
		List<ReservedActivity> acts = reservedActivityManager.getMyReservedActivity(counter);
		Assert.notEmpty(acts, "");
		System.out.println(acts);
	}

	@Test
	public void testIsAvailableProduct() {
		Counter counter = counterManager.getCounterByCode("000005");
		System.out.println(counter.getWarehouses());
		boolean flag = reservedActivityManager.isAvailableProduct(counter, 23888);
		System.out.println(flag);
	}

	@Test
	public void testIsAvailableActivity() {
		Counter counter = counterManager.getCounterByCode("000005");
		System.out.println(counter.getWarehouses());
		boolean flag = reservedActivityManager.isAvailableActivity(counter, "17102707");
		System.out.println(flag);
	}

	@Test
	public void testAdd() {
		ReservedActivity reservedActivity = reservedActivityManager.get(1);
		reservedActivity.setRevId(2);
		reservedActivityManager.add(reservedActivity);
	}

	@Test
	public void testGet() {
		ReservedActivity ra0 = reservedActivityManager.get(1);
		ReservedActivityService as = AppContext.getBean(ReservedActivityService.class);
		ReservedActivity ra2 = as.get(1);
		Assert.isTrue(ra0.equals(ra2), "");
		System.out.println(ra0 == ra2);
	}

	@Test
	public void testFindall() {
		System.out.println(reservedActivityManager.findall().size());
	}

	@Test
	public void testFindAvaliable() {
		System.out.println(reservedActivityManager.findAvaliable().size());
	}

	@Test
	public void testUpdate() {
		ReservedActivity reservedActivity = reservedActivityManager.get(2);
		reservedActivity.setStatus(ReservedActivity.S_EXPIRED);
		reservedActivityManager.update(reservedActivity);
		int s = reservedActivityManager.get(2).getStatus();
		Assert.isTrue(s == ReservedActivity.S_EXPIRED, "状态更新错误");
		ReservedActivityService as = AppContext.getBean(ReservedActivityService.class);
		ReservedActivity ra2 = as.get(2);
		Assert.isTrue(ra2.getStatus() == ReservedActivity.S_EXPIRED, "状态更新错误");
	}

	@Test
	public void testAddUserToScheduled() {
		// reservedActivityManager.addUserToScheduled();
		throw new UnsupportedOperationException();
	}

	@Test
	public void testValidateCart() {
		List<CartDetail> details;
		CartDetailService  cartDetailService = AppContext.getBean(CartDetailService.class);
		details = new ArrayList<CartDetail>();
		CartDetail detail = cartDetailService.get(1711040179L);
		details.add(detail);
		Assert.isTrue(reservedActivityManager.validateCart(details),"");
	}

}
