package com.zeusas.dp.ordm.rev.service.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.zeusas.common.data.TypeConverter;
import com.zeusas.common.service.OnStartApplication;
import com.zeusas.core.service.ServiceException;
import com.zeusas.core.utils.AppContext;
import com.zeusas.dp.ordm.active.model.PeriodActivity;
import com.zeusas.dp.ordm.entity.CartDetail;
import com.zeusas.dp.ordm.entity.Counter;
import com.zeusas.dp.ordm.entity.Customer;
import com.zeusas.dp.ordm.rev.entity.ReservedActivity;
import com.zeusas.dp.ordm.rev.entity.ReservedActivityContext;
import com.zeusas.dp.ordm.rev.service.ReservedActivityManager;
import com.zeusas.dp.ordm.rev.service.ReservedActivityService;
import com.zeusas.dp.ordm.service.CounterManager;
import com.zeusas.security.auth.entity.AuthUser;
@Service
public class ReservedActivityManagerImpl extends OnStartApplication implements ReservedActivityManager{

	static Logger logger = LoggerFactory.getLogger(ReservedActivityManagerImpl.class);
	
	final Map<Integer, ReservedActivity> allActivity;
	@Autowired
	private ReservedActivityService reservedActivityService;

	public ReservedActivityManagerImpl() {
		allActivity=new HashMap<>();
	}
	
	void load(){
		List<ReservedActivity> activities =reservedActivityService.findAll();
		allActivity.clear();
		for (ReservedActivity reservedActivity : activities) {
			allActivity.put(reservedActivity.getRevId(), reservedActivity);
		}
	}
	
	@Override
	public List<ReservedActivity> getMyReservedActivity(Counter counter) {
		return findAvaliable().stream()//
				.filter(e->e.validCheck(counter))//
				.collect(Collectors.toList());
	}

	@Override
	//FIXME 这个预订货有我仓库 但是当前不是我的活动
	//目前是柜台时间范围比仓库大 且两个范围都存在
	public boolean isAvailableProduct(Counter counter, Integer prudoctId) {
		// 包含所在仓库的预订会
		//DELETE START
//		Set<Integer> stockActivity = new HashSet<>();
//
//		String wid = counter.getWarehouses();
		//DELETE END
		for (ReservedActivity activity : findAvaliable()) {
			// UPDATE start 
			Long start=activity.getStartTime().getTime();
			Long end=activity.getEndTime().getTime();
			Long now =System.currentTimeMillis();
			if(now<start||now>end){
				continue;
			}
			ReservedActivityContext context = activity.getContext();
			boolean containsProduct=  context.containsProduct(prudoctId);
			if(containsProduct){
				return activity.validCheck(counter);
			}
//			Integer revId = activity.getRevId();
//			ReservedActivityContext context = activity.getContext();
//			Assert.notNull(context, "预订会正文不能为空");
//			// 装配所在仓库的预订会
//			//Add start
//			Set<String> wids=new HashSet<>();
//			//Add end
//			for (PeriodActivity stock : context.getStocks()) {
//				//Add start
//				wids.addAll(stock.getVal());
//				//Add end
//				if (stock.getVal().contains(wid) //
//						&& !stockActivity.contains(revId)) {
//					stockActivity.add(revId);
//				}
//			}
//			//Add start 该预订会设置了仓库 但是没有我仓库
//			if(!wids.isEmpty()&&!wids.contains(wid)){
//				return false;
//			}
//			//Add end
//			// 是所在仓库的预订会 但不是我的预订会
//			if (stockActivity.contains(revId)//
//					&& !activity.validCheck(counter)) {
//				return !context.containsProduct(prudoctId);
//			}
			//UPDATE END 
		}
		return true;
	}

	@Override
	public boolean isAvailableActivity(Counter counter, String activityId) {
		// 包含所在仓库的预订会
		// UPDATE START 
//		Set<Integer> stockActivity = new HashSet<>();
//		String wid = counter.getWarehouses();
		for (ReservedActivity activity : findAvaliable()) {
			Long start=activity.getStartTime().getTime();
			Long end=activity.getEndTime().getTime();
			Long now =System.currentTimeMillis();
			if(now<start||now>end){
				continue;
			}
			ReservedActivityContext context = activity.getContext();
			boolean containsActivity=  context.containsActivity(activityId);
			if(containsActivity){
				return activity.validCheck(counter);
			}
//			Integer revId = activity.getRevId();
//			ReservedActivityContext context = activity.getContext();
//			Assert.notNull(context, "预订会正文不能为空");
//			// 装配所在仓库的预订会
//			//Add start
//			Set<String> wids=new HashSet<>();
//			//Add end
//			for (PeriodActivity stock : context.getStocks()) {
//				//Add start
//				wids.addAll(stock.getVal());
//				//Add end
//				if (stock.getVal().contains(wid) //
//						&& !stockActivity.contains(revId)) {
//					stockActivity.add(revId);
//				}
//			}
//			//Add start 该预订会设置了仓库 但是没有我仓库
//			if(!wids.isEmpty()&&!wids.contains(wid)){
//				return false;
//			}
//			//Add end
//			// 是所在仓库的预订会 但不是我的预订会
//			if (stockActivity.contains(revId)//
//					&& !activity.validCheck(counter)) {
//				return !context.containsActivity(activityId);
//			}
			//UPDATE END 
		}
		return true;
	}

	@Override
	@Transactional
	public void add(ReservedActivity reservedActivity) throws ServiceException{
		try {
			reservedActivityService.save(reservedActivity);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		} 
	}

	@Override
	public ReservedActivity get(Integer revId) {
		return allActivity.get(revId);
	}

	@Override
	public Collection<ReservedActivity> findall() {
		return  allActivity.values();
	}

	@Override
	@Transactional
	public void update(ReservedActivity reservedActivity)throws ServiceException {
		try {
			reservedActivityService.update(reservedActivity);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		} 
		
	}

	@Override
	public void addUserToScheduled(Customer customer)throws ServiceException {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStartLoad() {
		if(allActivity.size()==0){
			load();
		}
	}

	@Override
	public List<ReservedActivity> findAvaliable() {
		return allActivity.values().stream()//
				.filter(e->e.isAvaliable())//
				.collect(Collectors.toList());
	}

	@Override
	public boolean validateCart(List<CartDetail> details) {
		for (CartDetail detail : details) {
			if(!validateCart(detail)){
				return false;
			}
		}
		return true;
	}
	
	private boolean validateCart(CartDetail detail) {
		Integer revId=detail.getRevId();
		if(revId==null){
			return true;
		}
		ReservedActivity reservedActivity=this.get(revId);
		if(reservedActivity==null){
			return false;
		}
		Long start=reservedActivity.getStartTime().getTime();
		Long end=reservedActivity.getEndTime().getTime();
		Long now =System.currentTimeMillis();
		if(now<start||now>end){
			return false;
		}
		return true;
	}

}
