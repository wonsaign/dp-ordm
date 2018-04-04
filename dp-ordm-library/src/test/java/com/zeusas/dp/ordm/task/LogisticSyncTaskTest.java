package com.zeusas.dp.ordm.task;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.zeusas.core.utils.AppContext;

public class LogisticSyncTaskTest {
	@SuppressWarnings("resource")
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
		LogisticSyncTask lgsync = AppContext.getBean(LogisticSyncTask.class);
		try {
			lgsync.exec();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
