package com.zeusas.dp.ordm.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.zeusas.core.service.IService;
import com.zeusas.dp.ordm.entity.PreOrder;
import com.zeusas.security.auth.entity.AuthUser;

public interface PreOrderService extends IService<PreOrder, Long> {
	/**
	 * 导入订单模板在doc文件夹内，导入成功数据保存于数据库
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public boolean insertExcel(File file) throws IOException ;
	/**
	 * 前台传入订单code列表(之前通过systemOrderService导入的代付款订单，通过此方法合并为一个柜台，商品金星汇总，价格重新计算--主要运费)
	 * @param orderNos
	 * @param counterCode
	 * @return
	 */
	
	public void combineOrder(List<String> orderNos,String counterCode,AuthUser au);
	
}
