package com.zeusas.dp.ordm.task;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.zeusas.core.utils.AppContext;

public class BalanceEndTaskTest {
	@Before
	public void setUp(){
		FileSystemXmlApplicationContext ctx ;
	 ctx = new FileSystemXmlApplicationContext("config/ordm-unit-test.xml");
	ctx.start();
	}
	
	@Test
	public void testExec() {
		BalanceEndTask task = AppContext.getBean(BalanceEndTask.class);
		try {
			task.exec();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
