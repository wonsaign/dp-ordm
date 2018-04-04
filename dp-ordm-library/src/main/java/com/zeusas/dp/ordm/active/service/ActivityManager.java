package com.zeusas.dp.ordm.active.service;

import java.util.Collection;
import java.util.List;

import org.hibernate.sql.Update;

import com.zeusas.dp.ordm.active.model.Activity;
import com.zeusas.dp.ordm.entity.CartDetail;
import com.zeusas.dp.ordm.entity.Counter;
import com.zeusas.dp.ordm.entity.Item;

public interface ActivityManager {

	/** 判断一个活动是否有效 并且判断加入的产品是否符合活动规则 */
	boolean validateCheck(String actId, List<Item> actityGoods,
			List<Item> actityExtra);

	/** 新增一个活动 */
	void add(Activity activity);

	/** 修改一个活动状态 */
	boolean modifyActiveStatus(Activity activity, boolean flag);

	/** 获取一个活动 */
	Activity get(String actId);

	/** 根据活动类型拿到该类型所有的有效的活动 */
	List<Activity> findByType(String type);

	/**
	 * 找到客户自己的活动
	 * 
	 * @param counterid
	 * @param zone
	 * @return
	 */
	List<Activity> findMyActivities(Counter counter);
	
	/**
	 * 最近3年的所有活动
	 * 
	 * @return
	 */
	Collection<Activity> values();

	/**
	 * 找到所有有效活动，排序为日期
	 * 
	 * @return
	 */
	List<Activity> findAvaliable();
	
	/** 拿到全国门店的所有活动*/
	List<Activity>findGlobal();
	/**
	 * 购物车条目校验
	 * @param details
	 * @return 如果校验成功返回true 其他false
	 */
	boolean validateCart(List<CartDetail> details);
	/**
	 * 修改该活动该仓库打欠状态
	 * @param activity
	 * @param wid
	 * @param status
	 */
	void changeReserveStatus(String actId,String wid,Integer status);
	/**
	 * 根据产品 修改该活动该仓库打欠状态
	 * @param productId
	 * @param wid
	 * @param status
	 */
	void changeToReserved(Integer productId,Counter counter);
	
	/**
	 * 根据柜台 获取我的打欠活动
	 * @param counter
	 * @return
	 */
	List<Activity> findMyRevActivity(Counter counter);
	/**
	 * 获取我的可打欠活动
	 * @param counter
	 * @param productId
	 * @return
	 */
	boolean isReservable(Counter counter,Integer productId);
	
}
