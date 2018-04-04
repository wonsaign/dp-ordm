package com.zeusas.dp.ordm.controller;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zeusas.common.utils.DSResponse;
import com.zeusas.common.utils.Status;
import com.zeusas.dp.ordm.active.model.Activity;
import com.zeusas.dp.ordm.active.service.ActivityManager;

/**
 * 活动
 * 
 * @author shihx
 * @date 2016年12月22日 下午3:01:01
 */
@Controller
@RequestMapping("/activity")
public class ActivityContronller {

	static Logger logger = LoggerFactory.getLogger(ActivityContronller.class);

	@Autowired
	private ActivityManager activityManager;

	/**
	 * 柜台页初始化
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/init")
	public String init(HttpServletRequest request) {
		DSResponse dsResponse =new DSResponse(Status.SUCCESS);
		Collection<Activity> activities= activityManager.values();
		dsResponse.setData(activities);
		request.setAttribute("DSResponse", dsResponse);
		return "/page/activity";
	}

}
