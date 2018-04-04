package com.zeusas.dp.ordm.task;

import java.sql.Connection;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.zeusas.core.utils.AppContext;

public class CountersInfoSyncTaskTest {

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
			CountersInfoSyncTask cisync = AppContext.getBean(CountersInfoSyncTask.class);
			cisync.exec();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void t(String t) {
		try {
			DataSource ds = (DataSource) AppContext.getBean(t);
			Connection c = ds.getConnection();
			System.out.println(c);
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
