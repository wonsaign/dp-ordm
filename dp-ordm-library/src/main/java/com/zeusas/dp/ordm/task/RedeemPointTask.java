package com.zeusas.dp.ordm.task;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zeusas.common.task.CronTask;
import com.zeusas.core.utils.AppContext;
import com.zeusas.dp.ordm.service.RedeemPointService;

/**
 *积分兑换系统制单
 */
public class RedeemPointTask extends CronTask {
	static final Logger logger = LoggerFactory.getLogger(RedeemPointTask.class);


	@Override
	public void exec() throws Exception {
//		Calendar cal = Calendar.getInstance();
//		int day = cal.get(Calendar.DATE);
		//每月5、15、25三天
//		if(day%5==0&&day/5%2==1){
			buildOrder();
//		}
	}

	@Override
	protected boolean ready() {
		return valid;
	}
	
	private void buildOrder(){
		try {
			RedeemPointService redeemPointService=AppContext.getBean(RedeemPointService.class);
			redeemPointService.buildSystemOrder();
		} catch (Exception e) {
			logger.error("积分兑换系统制单错误",e);
		}
	}
}
