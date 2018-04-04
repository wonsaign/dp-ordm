package com.zeusas.dp.ordm.rev.service;

import java.util.Collection;
import java.util.List;

import com.zeusas.core.service.ServiceException;
import com.zeusas.dp.ordm.entity.CartDetail;
import com.zeusas.dp.ordm.entity.Counter;
import com.zeusas.dp.ordm.entity.Customer;
import com.zeusas.dp.ordm.rev.entity.ReservedActivity;

public interface ReservedActivityManager {
	/**
	 * 根据柜台仓库获取预订会 先校验时间有效性 校验仓库有效性 仓库不为空且不包含我的仓库=false 校验门店有效性
	 * 门店不为空且不包含我的门店=false
	 * 
	 * @param count
	 * @return
	 */
	List<ReservedActivity> getMyReservedActivity(Counter counter);

	/**
	 * 检查预订会单品是否可才订货页可见 仓库和门店都为空可见 仓库为空 柜台不为空 没有我的柜台=false 仓库不为空 没有我的仓库=false
	 * 
	 * @param count
	 * @param prudoctId
	 * @return
	 */
	boolean isAvailableProduct(Counter counter, Integer prudoctId);
	// 可能通过URL路径进入到预定会,判断是否是正确的用户,禁止加入预定活动到购物车
	boolean isAvailableActivity(Counter counter,String activityId);
	/** 添加一个预订会 */
	void add(ReservedActivity reservedActivity) throws ServiceException;

	/** 根据Id获取一个预订会 */
	ReservedActivity get(Integer revId);

	/** 获取所有预订会 */
	Collection<ReservedActivity> findall();
	
	/** 找到所有有效活动，排序为日期*/
	List<ReservedActivity> findAvaliable();

	/** 更新一个预订会 */
	void update(ReservedActivity reservedActivity)throws ServiceException;
	
	/**
	 *  现场情况可能产生 未报名的客户进入到现场 
	 *  需求 : 通过一个用户登录名 
	 *  实现 : 讲加入的用户全部添加到预定会现场正文
	 *  
	 */
	void addUserToScheduled(Customer customer)throws ServiceException;
	/**
	 * 预订会时间有效性校验
	 * @param details
	 * @return
	 */
	boolean validateCart(List<CartDetail> details);
	
	//XXX: 风险预估 : 预定会活动展现? 需要重点来review!
}
