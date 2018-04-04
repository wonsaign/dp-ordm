package com.zeusas.dp.ordm.controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.zeusas.common.entity.Dictionary;
import com.zeusas.common.service.DictManager;
import com.zeusas.common.utils.DSResponse;
import com.zeusas.common.utils.Status;
import com.zeusas.dp.ordm.entity.MonthPresent;
import com.zeusas.dp.ordm.service.MonthPresentManager;
import com.zeusas.dp.ordm.service.MonthPresentService;
import com.zeusas.dp.ordm.utils.PoiUtil;

/**
 * 定义 每月赠送 的Controller 包含了产品的增删改查
 * 
 * @author jcm
 *
 */
@Controller
@RequestMapping("/monthpresent")
public class MonthPresentController {

	static Logger logger = LoggerFactory.getLogger(OrgUnitController.class);

	@Autowired
	private MonthPresentService ponthPresentService;
	@Autowired
	private DictManager dictManager;
	@Autowired
	private MonthPresentManager monthPresentManager;

	/**
	 * 跳转到 每月赠送
	 * 
	 * @return
	 */
	@RequestMapping("/present")
	public String forwardPresent(ModelMap mMap, HttpServletRequest request) {
		List<MonthPresent> all = ponthPresentService.findAll();
		// int size = all.size();
		// int max = (size % 10 == 0) ? (size / 10) : (size / 10 + 1);
		// List<MonthPresent> presents = size>10?all.subList(0, 10):all;
		//
		// request.setAttribute("max", max);
		// request.setAttribute("page", 1);
		mMap.addAttribute("presents", all);
		return "page/monthpresent";
	}

	@RequestMapping(value = "/presentpage")
	@ResponseBody
	public List<MonthPresent> customerPage(HttpServletRequest request,
			@RequestParam(value = "page", required = false) int page,
			@RequestParam(value = "num", required = false) int num,
			@RequestParam(value = "key", required = false) String key) {
		List<MonthPresent> allPresents = ponthPresentService.findAll();
		int size = allPresents.size();
		int startNo = (page - 1) * num;
		int endNo = page * num < size ? page * num : size;
		if (startNo >= size) {
			return new ArrayList<MonthPresent>(0);
		}
		List<MonthPresent> presents = allPresents.subList(startNo, endNo);
		return presents;
	}

	/**
	 * 跳转到 每天赠送 创建
	 * 
	 * @return
	 */
	@RequestMapping("/presentadd")
	public String forwardPresentAdd(HttpServletRequest request) {
		DSResponse dsResponse = new DSResponse();
		Dictionary dict = dictManager.get(MonthPresent.TYPE_DICT);
		if (dict.getChildren().isEmpty()) {
			dsResponse.setStatus(Status.FAILURE);
			dsResponse.addMessage("初始化类型出错");
		}
		dsResponse.setData(dict.getChildren());
		request.setAttribute("dsResponse", dsResponse);
		return "page/monthpresentadd";
	}

	/**
	 * 跳转到 每天赠送 保存
	 * 
	 * @return
	 */
	@RequestMapping("/presentsave")
	public String presentAddSave(HttpServletRequest request,
			@RequestParam(value = "file", required = false) MultipartFile file, String type) {
		DSResponse dsResponse = new DSResponse();
		try {
			// FIXME: 并未保存文件？
			if (file == null || file.getBytes().length == 0) {
				dsResponse.addMessage("上传文件失败");
				request.setAttribute("dsResponse", dsResponse);
				return "page/monthpresentadd";
			}
			InputStream is = file.getInputStream();
			PoiUtil.checkExcelVaild(file);

			String loginName = request.getRemoteUser();
			Workbook workbook = PoiUtil.getWorkbook(is, file);
			List<MonthPresent> monthPresents = monthPresentManager.readFile(workbook);
			for (MonthPresent m : monthPresents) {
				m.getContext().get(0).setType(type);
				monthPresentManager.saveOrUpdate(m, loginName);
			}
			//类型
			Dictionary dict = dictManager.get(MonthPresent.TYPE_DICT);
			if (dict.getChildren().isEmpty()) {
				dsResponse.setStatus(Status.FAILURE);
				dsResponse.addMessage("初始化类型出错");
			}
			dsResponse.setData(dict.getChildren());
			request.setAttribute("message", "添加成功！");
		} catch (Exception e) {
			request.setAttribute("message", "添加错误，请检查格式！ 错误消息:"+e.getMessage());
			logger.error("上传赠品错误！", e);
		}
		request.setAttribute("dsResponse", dsResponse);
		return "page/monthpresentadd";
	}

}
