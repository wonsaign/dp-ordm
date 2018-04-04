package com.zeusas.dp.ordm.task;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.zeusas.core.utils.AppContext;

public class ProductSyncTaskTest {

	public void setUp() throws Exception {
		try {
			FileSystemXmlApplicationContext ctx = new FileSystemXmlApplicationContext("config/ordm-unit-test.xml");
			ctx.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testSync() {
		try {
			ProductSyncTask cisync = AppContext.getBean(ProductSyncTask.class);
			cisync.exec();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
