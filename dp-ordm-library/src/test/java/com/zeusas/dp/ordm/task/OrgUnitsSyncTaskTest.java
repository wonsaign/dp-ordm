package com.zeusas.dp.ordm.task;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.zeusas.core.utils.AppContext;

public class OrgUnitsSyncTaskTest {
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
			OrgUnitsSyncTask cisync = AppContext.getBean(OrgUnitsSyncTask.class);
			cisync.exec();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
