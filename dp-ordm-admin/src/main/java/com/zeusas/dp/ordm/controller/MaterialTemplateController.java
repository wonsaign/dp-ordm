package com.zeusas.dp.ordm.controller;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zeusas.common.utils.DSResponse;
import com.zeusas.common.utils.Status;
import com.zeusas.dp.ordm.entity.FixedPrice;
import com.zeusas.dp.ordm.entity.MaterialTemplate;
import com.zeusas.dp.ordm.service.MaterialTemplateManager;

@Controller
@RequestMapping("/materialtemplate")
public class MaterialTemplateController {
	
	static Logger logger = LoggerFactory.getLogger(MaterialTemplateController.class);

	@Autowired
	private MaterialTemplateManager materialTemplateManager;
	
	@RequestMapping("/init")
	public String init(HttpServletRequest request){
		DSResponse dsResponse=new DSResponse();
		Collection<MaterialTemplate> templates= new ArrayList<>(materialTemplateManager.findAll());
		dsResponse.setData(templates);
		request.setAttribute("DSResponse",dsResponse);
		return "/page/materialtemplate";
	}
	
	@RequestMapping("/enable")
	@ResponseBody
	public DSResponse enable(HttpServletRequest request,
			@RequestParam(value = "Id", required = false) Integer Id) {
		DSResponse dsResponse = new DSResponse(Status.FAILURE);
		MaterialTemplate  materialTemplate = materialTemplateManager.get(Id);
		if (materialTemplate == null) {
			dsResponse.setMessage("启用失败,获取新店物料错误");
			return dsResponse;
		}
		materialTemplate.setStatus(true);
		try {
			materialTemplateManager.update(materialTemplate);
			dsResponse.setStatus(Status.SUCCESS);
			dsResponse.setMessage("启用成功");
		} catch (Exception e) {
			logger.error("启用失败",e);
		}
		return dsResponse;
	}
	
	@RequestMapping("/diable")
	@ResponseBody
	public DSResponse diable(HttpServletRequest request,
			@RequestParam(value = "Id", required = false) Integer Id) {
		DSResponse dsResponse = new DSResponse(Status.FAILURE);
		MaterialTemplate  materialTemplate = materialTemplateManager.get(Id);
		if (materialTemplate == null) {
			dsResponse.setMessage("禁用失败,获取策略错误");
			return dsResponse;
		}
		materialTemplate.setStatus(false);
		try {
			materialTemplateManager.update(materialTemplate);
			dsResponse.setStatus(Status.SUCCESS);
			dsResponse.setMessage("禁用成功");
		} catch (Exception e) {
			logger.error("禁用失败",e);
		}
		return dsResponse;
	}
}
