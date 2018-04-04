package com.zeusas.dp.ordm.task;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class SellerDataTaskTest {
	FileSystemXmlApplicationContext ctx;
	@Before
	public void setUp() throws Exception {
		try {
			ctx = new FileSystemXmlApplicationContext("config/ordm-unit-test.xml");
			ctx.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
@Test
	public void testExec() {
		SellerDataTask s = ctx.getBean(SellerDataTask.class);
		try {
			s.exec();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
