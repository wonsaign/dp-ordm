package com.zeusas.dp.ordm.task;

import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.zeusas.core.utils.AppContext;

public class StorehousesSyncTaskTest {

	FileSystemXmlApplicationContext ctx;
	public void setUp() throws Exception {
		try {
			ctx = new FileSystemXmlApplicationContext("config/ordm-unit-test.xml");
			ctx.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testExec() {
		StorehousesSyncTask s = AppContext.getBean(StorehousesSyncTask.class);
		try {
			s.exec();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
