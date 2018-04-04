package com.zeusas.dp.ordm.task;


import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.zeusas.core.utils.AppContext;
import com.zeusas.security.auth.service.AuthCenterManager;

public class CustomersInfoSyncTaskTest {

	AuthCenterManager acm;
	@Before
	public void setUp() throws Exception {
		try {
			FileSystemXmlApplicationContext ctx = new FileSystemXmlApplicationContext("config/ordm-unit-test.xml");
			acm=AppContext.getBean(AuthCenterManager.class);
			ctx.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testSync() {
		try {
			CustomersInfoSyncTask cisync = AppContext.getBean(CustomersInfoSyncTask.class);
			acm.loadAuthUser();
			cisync.exec();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
