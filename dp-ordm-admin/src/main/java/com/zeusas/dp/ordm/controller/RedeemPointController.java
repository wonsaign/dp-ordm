package com.zeusas.dp.ordm.controller;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.google.common.base.Strings;
import com.zeusas.common.data.Record;
import com.zeusas.common.service.DictManager;
import com.zeusas.common.utils.DSResponse;
import com.zeusas.common.utils.Status;
import com.zeusas.core.service.ServiceException;
import com.zeusas.dp.ordm.bean.RedeemPointBean;
import com.zeusas.dp.ordm.entity.Product;
import com.zeusas.dp.ordm.entity.RedeemPoint;
import com.zeusas.dp.ordm.service.CounterManager;
import com.zeusas.dp.ordm.service.ProductManager;
import com.zeusas.dp.ordm.service.RedeemPointService;
import com.zeusas.dp.ordm.utils.OrdmConfig;
import com.zeusas.dp.ordm.entity.Counter;

@Controller
@RequestMapping("/redeempoint")
public class RedeemPointController {

	@Autowired
	private CounterManager counterManager;
	@Autowired
	private ProductManager productManager;
	@Autowired
	private RedeemPointService redeemPointService;
	@Autowired
	private DictManager dictManager;

	private static String DELIVERYWAY = OrdmConfig.DELIVERYWAY;

	private static Logger logger = LoggerFactory.getLogger(RedeemPointController.class);

	@RequestMapping("/upload")
	public @ResponseBody DSResponse upload(@RequestParam("uploadFile") CommonsMultipartFile file,
			HttpServletResponse response, //
			HttpServletRequest request) {
		DSResponse ds = new DSResponse(Status.FAILURE);
		try {
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=utf-8");
			String realPath = this.getClass().getClassLoader().getResource("").getPath();
			String filename = file.getOriginalFilename();
			String path = realPath + "temp" + filename;
			String user = request.getRemoteUser();
			if (Strings.isNullOrEmpty(user))
				throw new SecurityException();
			// 格式校验成功
			List<Record> records = redeemPointService.getExcel(upload(path, file), ds);

			int row = records.size();
			List<RedeemPoint> redeems = new ArrayList<>(row);
			ds.getMessage().clear();
			ds.addMessage("错误数据，请检查输入数据内容!");
			boolean abortion = false;
			for (int i = 0; i < row; i++) {
				StringBuilder sb = null;
				Record r = records.get(i);
				// 数据是否校验成功
				String cCode;
				Product p = null;
				Integer num = 0;
				String deliveryWay = null;
				String remark = r.getString(4);
				if (counterManager.getCounterByCode(cCode = r.getString(0)) == null)
					sb = new StringBuilder("柜台不存在");
				if ((p = productManager.findByCode(r.getString(1))) == null)
					sb = (sb == null ? new StringBuilder() : sb).append("产品不存在;");
				else if ((num = r.getInteger(2)) <= 0)
					sb = (sb == null ? new StringBuilder() : sb).append(",产品数量不能小于1;");
				if (dictManager.lookUpByCode(DELIVERYWAY, deliveryWay = r.getString(3)) == null)
					sb = (sb == null ? new StringBuilder() : sb).append(",发货方式不存在;");
				if (sb != null) {
					ds.getMessage().add(new StringBuilder("第").append(i + 1).append("行").append(sb).toString());
					abortion = true;
				}
				if (!abortion)
					redeems.add(new RedeemPoint(cCode, p.getProductId(), num, user, deliveryWay, remark));
			}
			// 数据错误
			if (abortion)
				throw new Exception();
			redeems.stream().forEach(redeemPointService::createRedeemPoint);
			ds = DSResponse.SUCCESS;
		} catch (SecurityException e) {
			ds = new DSResponse(Status.LOGIN_REQUIRED);
		} catch (Exception e) {
			logger.error("记录不正确", e);
		}
		return ds;
	}

	// 接口，上传记录
	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public @ResponseBody DSResponse insert(
			@RequestParam(value = "redeems", required = true) List<RedeemPoint> redeems) {
		DSResponse ds = new DSResponse(Status.FAILURE);
		// 第i条记录
		int i = 1;
		boolean abortion = false;
		try {
			for (RedeemPoint r : redeems) {
				StringBuilder sb = null;
				Product p = null;
				if (counterManager.getCounterByCode(r.getCounterCode()) == null)
					sb = new StringBuilder("柜台不存在");
				if ((p = productManager.findByCode(r.getExcuteNo())) == null)
					sb = (sb == null ? new StringBuilder() : sb).append("产品不存在;");
				else if (r.getNum() <= 0)
					sb = (sb == null ? new StringBuilder() : sb).append(",产品数量不能小于1;");
				if (dictManager.lookUpByCode(DELIVERYWAY, r.getDeliveryWay()) == null)
					sb = (sb == null ? new StringBuilder() : sb).append(",发货方式不存在;");
				if (sb != null) {
					ds.getMessage().add(new StringBuilder("第").append(i).append("行").append(sb).toString());
					abortion = true;
				}
				i++;
				r.setExcuteNo(null);
				r.setProductId(p.getProductId());
			}
			if (abortion)
				throw new Exception();
			redeems.stream().forEach(redeemPointService::createRedeemPoint);
			ds = DSResponse.SUCCESS;
		} catch (Exception e) {
			logger.error("记录不正确", e);
		}
		return ds;
	}

	private static File upload(String path, CommonsMultipartFile file) throws IOException {
		File newFile = new File(path);
		if (!newFile.exists()) {
			newFile.mkdirs();
		} else {
			newFile.delete();
			newFile.mkdirs();
		}
		newFile.createNewFile();
		file.transferTo(newFile);
		return newFile;
	}

	@RequestMapping("/init")
	public String init(HttpServletRequest request) {
		List<RedeemPointBean> avalible = null;
		Collection<Counter> counters = null;
		try {

			// 获取所有柜台
			counters = counterManager.findAll();
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			List<RedeemPoint> records = redeemPointService.getAvalible();
			avalible = new ArrayList<>(records.size());
			for (RedeemPoint a : records) {
				String counterName = counterManager.getCounterByCode(a.getCounterCode()).getCounterName();
				String productName = productManager.get(a.getProductId()).getName();
				avalible.add(new RedeemPointBean(a.getId(), //
						counterName, productName, a.getNum(), //
						a.getAvalible(), //
						format.format(new Date(a.getCreatTime())), //
						a.getCreator(), //
						a.getExcuteNo(), dictManager.lookUpByCode(DELIVERYWAY, a.getDeliveryWay()).getName(), //
						a.getRemark()));
			}
		} catch (ServiceException e) {
			logger.error("初始化错误", e);
		}
		request.setAttribute("counters", counters);
		request.setAttribute("avalible", avalible);
		return "page/redeem";
	}

	@RequestMapping("/counter")
	public @ResponseBody List<RedeemPointBean> counter(HttpServletRequest request,
			@RequestParam(value = "counterCode", required = true) String counterCode) {
		List<RedeemPointBean> avalible = null;
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			List<RedeemPoint> records = redeemPointService.findByAvaiCounterCode(counterCode);
			avalible = new ArrayList<>(records.size());
			for (RedeemPoint a : records) {
				String counterName = counterManager.getCounterByCode(a.getCounterCode()).getCounterName();
				String productName = productManager.get(a.getProductId()).getName();
				avalible.add(new RedeemPointBean(a.getId(), //
						counterName, productName, a.getNum(), //
						a.getAvalible(), //
						format.format(new Date(a.getCreatTime())), //
						a.getCreator(), a.getExcuteNo(),
						dictManager.lookUpByCode(DELIVERYWAY, a.getDeliveryWay()).getName(), //
						a.getRemark()));
			}
		} catch (Exception e) {
			logger.error("查询错误", e);
		}
		return avalible;
	}

	@RequestMapping("/cancel")
	public @ResponseBody DSResponse cancel(HttpServletRequest request,
			@RequestParam(value = "id", required = true) Long id) {
		DSResponse response = DSResponse.SUCCESS;
		try {
			redeemPointService.cancel(id);
		} catch (Exception e) {
			response = new DSResponse(Status.FAILURE, e.getMessage());
			logger.error("删除失败", e);
		}
		return response;
	}
}
