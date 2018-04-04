package com.zeusas.dp.ordm.bean;

import com.zeusas.common.task.TaskBean;
import com.zeusas.core.utils.DateTime;

public class TaskBeanforWeb {
	private TaskBean taskBean;
	private String lastUpdate;

	public TaskBeanforWeb() {
	}

	public TaskBeanforWeb(TaskBean taskBean) {
		this.taskBean = taskBean;
		this.lastUpdate = DateTime.formatDate(DateTime.YYYY_MM_DD_HMS, taskBean.lastUpdate());
	}
	
	public TaskBean getTaskBean() {
		return taskBean;
	}

	public void setTaskBean(TaskBean taskBean) {
		this.taskBean = taskBean;
	}

	public String getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
}
