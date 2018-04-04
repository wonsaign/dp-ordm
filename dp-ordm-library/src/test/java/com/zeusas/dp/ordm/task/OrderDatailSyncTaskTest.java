package com.zeusas.dp.ordm.task;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.zeusas.core.utils.AppContext;
import com.zeusas.core.utils.DateTime;

public class OrderDatailSyncTaskTest {

	@Before
	public void setUp() throws Exception {
		try {
			FileSystemXmlApplicationContext ctx = new FileSystemXmlApplicationContext("config/ordm-unit-test.xml");
			ctx.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testSync() {
		try {
			System.out.println(DateTime.formatDate(DateTime.YYYY_MM_DD_HMS, new Date()));
			OrderDatailSyncTask cisync = AppContext.getBean(OrderDatailSyncTask.class);
			cisync.exec();
			System.out.println(DateTime.formatDate(DateTime.YYYY_MM_DD_HMS, new Date()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
