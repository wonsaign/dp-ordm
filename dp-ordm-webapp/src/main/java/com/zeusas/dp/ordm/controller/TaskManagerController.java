package com.zeusas.dp.ordm.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zeusas.common.task.TaskBean;
import com.zeusas.dp.ordm.bean.TaskBeanforWeb;

@Controller
@RequestMapping("/task")
public class TaskManagerController extends TaskManager {
	
	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public String show(HttpServletRequest request) throws IOException {
		List<TaskBean> taskBeans = this.list();
		List<TaskBeanforWeb> taskBeanforWeb = new ArrayList<>(taskBeans.size());
		for (TaskBean taskBean : taskBeans) {
			TaskBeanforWeb t = new TaskBeanforWeb(taskBean);
			taskBeanforWeb.add(t);
		}
		request.setAttribute("taskBeanforWeb", taskBeanforWeb);
		return "task";
	}
}
