package com.zeusas.dp.ordm.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zeusas.common.task.CronTask;
import com.zeusas.common.task.TaskBean;
import com.zeusas.common.utils.DSResponse;
import com.zeusas.core.task.TaskManagerService;
import com.zeusas.core.utils.AppContext;
import com.zeusas.dp.ordm.task.CancelOrdersTask;
import com.zeusas.dp.ordm.task.CountersInfoSyncTask;
import com.zeusas.dp.ordm.task.CustomersInfoSyncTask;
import com.zeusas.dp.ordm.task.MonthPresentTask;
import com.zeusas.dp.ordm.task.OrderDatailSyncTask;
import com.zeusas.dp.ordm.task.OrderSyncTask;
import com.zeusas.dp.ordm.task.OrgUnitsSyncTask;
import com.zeusas.dp.ordm.task.ProductSyncTask;
import com.zeusas.dp.ordm.task.ReserveOrderTask;
import com.zeusas.dp.ordm.task.SellerDataTask;
import com.zeusas.dp.ordm.task.StorehousesSyncTask;

public class TaskManager {

	static final Logger logger = LoggerFactory.getLogger(TaskManager.class);
	
	TaskManagerService tms = null;

	private TaskManagerService tms() {
		if (tms == null) {
			tms = AppContext.getBean(TaskManagerService.class);
		}
		return tms;
	}

	public List<TaskBean> list() {
		return tms().getTaskBeans();
	}

	/**
	 * 将某个任务暂停 flag=true 任务暂停
	 * 
	 * @param task
	 */
	void pauseTask(CronTask task, boolean flag) {
		tms().setPause(task, flag);
	}

	@RequestMapping("/shutdown")
	@ResponseBody
	public DSResponse shutdown() {
		tms().shutdown();
		return DSResponse.OK;
	}

	@RequestMapping("/pause_{id}")
	@ResponseBody
	public DSResponse pauseTask(@PathVariable(value = "id") String id) {
		switch (id) {
		case "cancelOrder":
			CronTask cancelOrder = AppContext.getBean(CancelOrdersTask.class);
			tms().setPause(cancelOrder, true);
			break;
		case "counter":
			CronTask counter = AppContext.getBean(CountersInfoSyncTask.class);
			tms().setPause(counter, true);
			break;
		case "customer":
			CronTask customer = AppContext.getBean(CustomersInfoSyncTask.class);
			tms().setPause(customer, true);
			break;
		case "order":
			CronTask Order = AppContext.getBean(OrderSyncTask.class);
			tms().setPause(Order, true);
			break;
		case "orgunit":
			CronTask orgunit = AppContext.getBean(OrgUnitsSyncTask.class);
			tms().setPause(orgunit, true);
			break;
		case "product":
			CronTask product = AppContext.getBean(ProductSyncTask.class);
			tms().setPause(product, true);
			break;
		case "sellerData":
			CronTask sellerData = AppContext.getBean(SellerDataTask.class);
			tms().setPause(sellerData, true);
			break;
		case "storehouse":
			CronTask storehouse = AppContext.getBean(StorehousesSyncTask.class);
			tms().setPause(storehouse, true);
			break;
		case "difforder":
			CronTask difforder = AppContext.getBean(OrderDatailSyncTask.class);
			tms().setPause(difforder, true);
			break;
		case "monthPresent":
			MonthPresentTask monthPresent = AppContext.getBean(MonthPresentTask.class);
			tms().setPause(monthPresent, true);
			break;
		case "reserveorder":
			ReserveOrderTask reserveorder = AppContext.getBean(ReserveOrderTask.class);
			tms().setPause(reserveorder, true);
			break;
		case "all":
			tms().setPause(true);
		default:
			// NOP
		}
		return DSResponse.OK;
	}

	@RequestMapping("/restart_{id}")
	@ResponseBody
	public DSResponse restartTask(@PathVariable(value = "id") String id) {
		switch (id) {
		case "cancelOrder":
			CronTask cancelOrder = AppContext.getBean(CancelOrdersTask.class);
			tms().setPause(cancelOrder, false);
			break;
		case "counter":
			CronTask counter = AppContext.getBean(CountersInfoSyncTask.class);
			tms().setPause(counter, false);
			break;
		case "customer":
			CronTask customer = AppContext.getBean(CustomersInfoSyncTask.class);
			tms().setPause(customer, false);
			break;
		case "order":
			CronTask Order = AppContext.getBean(OrderSyncTask.class);
			tms().setPause(Order, false);
			break;
		case "orgunit":
			CronTask orgunit = AppContext.getBean(OrgUnitsSyncTask.class);
			tms().setPause(orgunit, false);
			break;
		case "product":
			CronTask product = AppContext.getBean(ProductSyncTask.class);
			tms().setPause(product, false);
			break;
		case "sellerData":
			CronTask sellerData = AppContext.getBean(SellerDataTask.class);
			tms().setPause(sellerData, false);
			break;
		case "storehouse":
			CronTask storehouse = AppContext.getBean(StorehousesSyncTask.class);
			tms().setPause(storehouse, false);
			break;
		case "difforder":
			CronTask difforder = AppContext.getBean(OrderDatailSyncTask.class);
			tms().setPause(difforder, false);
			break;
		case "monthPresent":
			MonthPresentTask monthPresent = AppContext.getBean(MonthPresentTask.class);
			tms().setPause(monthPresent, false);
			break;
		case "reserveorder":
			ReserveOrderTask reserveorder = AppContext.getBean(ReserveOrderTask.class);
			tms().setPause(reserveorder, false);
			break;
		case "all":
			tms().setPause(false);
		default:
			// NOP
		}
		return DSResponse.OK;
	}

	@RequestMapping("/cancelOrder")
	@ResponseBody
	public DSResponse cancelOrder() {
		CronTask cancelOrder = AppContext.getBean(CancelOrdersTask.class);
		try {
			cancelOrder.exec();
		} catch (Exception e) {
			logger.error("定时同步出错,任务名{}",cancelOrder.getName(),e);
			return DSResponse.FAILURE;
		}
		return DSResponse.OK;
	}

	@RequestMapping("/counter")
	@ResponseBody
	public DSResponse Counter() {
		CronTask counter = AppContext.getBean(CountersInfoSyncTask.class);
		try {
			counter.exec();
		} catch (Exception e) {
			logger.error("定时同步出错,任务名{}",counter.getName(),e);
			return DSResponse.FAILURE;
		}
		return DSResponse.OK;
	}

	@RequestMapping("/customer")
	@ResponseBody
	public DSResponse Customer() {
		CronTask customer = AppContext.getBean(CustomersInfoSyncTask.class);
		try {
			customer.exec();
		} catch (Exception e) {
			logger.error("定时同步出错,任务名{}",customer.getName(),e);
			return DSResponse.FAILURE;
		}
		return DSResponse.OK;
	}

	@RequestMapping("/order")
	@ResponseBody
	public DSResponse Order() {
		CronTask order = AppContext.getBean(OrderSyncTask.class);
		try {
			order.exec();
		} catch (Exception e) {
			logger.error("定时同步出错,任务名{}",order.getName(),e);
			return DSResponse.FAILURE;
		}
		return DSResponse.OK;
	}

	@RequestMapping("/orgunit")
	@ResponseBody
	public DSResponse UrgUnit() {
		CronTask orgunit = AppContext.getBean(OrgUnitsSyncTask.class);
		try {
			orgunit.exec();
		} catch (Exception e) {
			logger.error("定时同步出错,任务名{}",orgunit.getName(),e);
			return DSResponse.FAILURE;
		}
		return DSResponse.OK;
	}

	@RequestMapping("/product")
	@ResponseBody
	public DSResponse Product() {
		CronTask product = AppContext.getBean(ProductSyncTask.class);
		try {
			product.exec();
		} catch (Exception e) {
			logger.error("定时同步出错,任务名{}",product.getName(),e);
			return DSResponse.FAILURE;
		}
		return DSResponse.OK;
	}

	@RequestMapping("/sellerData")
	@ResponseBody
	public DSResponse sellerData() {
		CronTask sellerData = AppContext.getBean(SellerDataTask.class);
		try {
			sellerData.exec();
		} catch (Exception e) {
			logger.error("定时同步出错,任务名{}",sellerData.getName(),e);
			return DSResponse.FAILURE;
		}
		return DSResponse.OK;
	}

	@RequestMapping("/storehouse")
	@ResponseBody
	public DSResponse Storehouse() {
		StorehousesSyncTask storehouse = AppContext.getBean(StorehousesSyncTask.class);
		try {
			storehouse.setUpdated();
			storehouse.exec();
		} catch (Exception e) {
			logger.error("重新计算库存错误,任务名{}",storehouse.getName(),e);
			return DSResponse.FAILURE;
		}
		return DSResponse.OK;
	}

	@RequestMapping("/difforder")
	@ResponseBody
	public DSResponse Difforder() {
		CronTask difforder = AppContext.getBean(OrderDatailSyncTask.class);
		try {
			difforder.exec();
		} catch (Exception e) {
			logger.error("定时同步出错,任务名{}",difforder.getName(),e);
			return DSResponse.FAILURE;
		}
		return DSResponse.OK;
	}
	
	@RequestMapping("/monthPresent")
	@ResponseBody
	public DSResponse MonthPresent() {
		MonthPresentTask task = AppContext.getBean(MonthPresentTask.class);
		try {
			task.exec();
		} catch (Exception e) {
			logger.error("定时同步出错,任务名{}",task.getName(),e);
			return DSResponse.FAILURE;
		}
		return DSResponse.OK;
	}
	
	@RequestMapping("/reserveOrder")
	@ResponseBody
	public DSResponse reserveOrder() {
		ReserveOrderTask task = AppContext.getBean(ReserveOrderTask.class);
		try {
			task.exec();
		} catch (Exception e) {
			logger.error("定时同步出错,任务名{}",task.getName(),e);
			return DSResponse.FAILURE;
		}
		return DSResponse.OK;
	}
	
}
