package com.zeusas.dp.ordm.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.zeusas.common.service.OnStartApplication;
import com.zeusas.core.service.ServiceException;
import com.zeusas.core.utils.AppContext;
import com.zeusas.core.utils.BeanDup;
import com.zeusas.dp.ordm.entity.Counter;
import com.zeusas.dp.ordm.entity.Customer;
import com.zeusas.dp.ordm.service.CounterManager;
import com.zeusas.dp.ordm.service.CounterService;
import com.zeusas.dp.ordm.service.MaterialTemplateService;

@Service
public class CounterManagerImpl extends OnStartApplication implements CounterManager {

	static Logger logger = LoggerFactory.getLogger(CounterManagerImpl.class);
	// 全部门店
	final Map<Integer, Counter> allCounter;
	final Map<String, Counter> codeMap;
	final Map<Integer, List<Counter>> customerIdMap;
	final Map<String, Counter> nameMap;
	final Map<String, Set<Integer>> groupCode;

	@Autowired
	private CounterService counterService;

	public CounterManagerImpl() {
		allCounter = new LinkedHashMap<>();
		codeMap = new HashMap<>();
		customerIdMap = new HashMap<>();
		nameMap = new HashMap<>();
		groupCode = new HashMap<>();
	}

	@Override
	public void load() {
		allCounter.clear();
		codeMap.clear();
		customerIdMap.clear();
		nameMap.clear();
		groupCode.clear();

		List<Counter> all = counterService.findAll();

		for (Counter c : all) {
			allCounter.put(c.getCounterId(), c);
			if(!c.getStatus()){
				continue;
			}
			codeMap.put(c.getCounterCode(), c);
			nameMap.put(c.getCounterName(), c);

			String code = getGroupCode(c);
			if (!Strings.isNullOrEmpty(code)) {
				if (!groupCode.containsKey(code)) {
					groupCode.put(code, new HashSet<>());
				}
				groupCode.get(code).add(c.getCustomerId());
			}
		}

		for (Counter c : allCounter.values()) {
			if (!customerIdMap.containsKey(c.getCustomerId())) {
				List<Counter> cltemp1 = new ArrayList<Counter>(allCounter.size());
				customerIdMap.put(c.getCustomerId(), cltemp1);
			}
			customerIdMap.get(c.getCustomerId()).add(c);
		}

		for (List<?> c : customerIdMap.values()) {
			((ArrayList<?>) c).trimToSize();
		}
	}

	public Counter getCounterById(Integer Id) {
		return allCounter.get(Id);
	}

	public Counter getCounterByCode(String counterCode) {
		return codeMap.get(counterCode);
	}

	public List<Counter> getCounterByCustomerId(Integer CustomerId) {
		List<Counter> cl = customerIdMap.get(CustomerId);
		return cl == null ? new ArrayList<>(0) : cl;
	}

	public Collection<Counter> findAll() {
		return allCounter.values();
	}

	public List<Counter> pagination(int page, int num) {
		final List<Counter> pageList = new ArrayList<Counter>(page);
		int size = allCounter.size();
		int start = (page - 1) * num;
		int end = num * page;
		// 如果超出范围，返回空？
		if (start >= size) {
			return pageList;
		}
		end = end < size ? end : size;

		Collection<Counter> all = allCounter.values();
		Iterator<Counter> itr = all.iterator();

		for (int i = 0; i < end && itr.hasNext(); i++) {
			Counter c = itr.next();
			if (i < start) {
				continue;
			}
			pageList.add(c);
		}
		return pageList;
	}

	public List<Counter> findByName(String Name) {
		List<Counter> cl = new ArrayList<Counter>();
		Set<String> keys = nameMap.keySet();
		for (String key : keys) {
			if (key.contains(Name)) {
				cl.add(nameMap.get(key));
			}
		}
		return cl;
	}

	@Override
	public void onStartLoad() {
		if (allCounter.size() == 0) {
			load();
		}
	}

	@Override
	public Counter get(Integer id) {
		return this.allCounter.get(id);
	}

	// public Map<Integer, Counter> getAllCounter() {
	// return this.allCounter;
	// }

	@Override
	public void update(Counter counter) throws ServiceException {
		try {
			Counter dbCounter = counterService.get(counter.getCounterId());
			Counter cacheCounter = allCounter.get(counter.getCounterId());

			BeanDup.dupNotNull(counter, dbCounter, "shippingAddress", "warehouses", "status","newCounter");
			BeanDup.dupNotNull(counter, cacheCounter, "shippingAddress", "warehouses", "status","newCounter");

			counterService.update(dbCounter);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 获取客户编码中能确定身份的串
	 * 
	 * 运营商编码为例外 为B.Y.XXX 通过其柜台编码来计算如武汉 W.J.001 取W(排除其柜台中例外B.Y.XXX) 代理商 分销商 以武汉为例
	 * 编码为 W.D.人名 W.F.人名 
	 * 加盟商 以武汉为例 W.J
	 * 
	 * @param customer
	 * @return
	 */
	@Override
	public String getCustomerGroupCode(Customer customer) {
		String customerType = customer.getCustomerType();
		String customserCode = customer.getCustomerCode();

		if (!Strings.isNullOrEmpty(customserCode)) {
			if ("运营商".equals(customerType)) {
				List<Counter> counters = getCounterByCustomerId(customer.getCustomerID());
				for (Counter counter : counters) {
					String groupCode = counter.getCounterType();
					if (groupCode != null && !groupCode.startsWith("B.Y.")) {
						return groupCode.substring(0, groupCode.indexOf("."));
					}
				}
			}
			if ("分销商".equals(customerType) || "代理商".equals(customerType)) {
				String[] str = customserCode.split("\\.");
				return customserCode.substring(0, customserCode.indexOf(str[3]) - 1);
			}
			if ("加盟商".equals(customerType)|| "直营".equals(customerType)) {
				return customserCode.substring(0, customserCode.indexOf("."));
			}
		}
		return null;
	}

	@Override
	public List<Counter> findCounterForOperator(Customer customer) {
		String code = getCustomerGroupCode(customer);
		List<Counter> counters = new ArrayList<>();
		for (Counter counter : findAll()) {
			if (counter.getCounterType() != null//
					&& counter.getCounterType().startsWith(code + "J")) {
				counters.add(counter);
			}
		}
		return counters;
	}
	
	@Override
	public String getGroupCodeByCustomer(Customer customer) {
		 List<Counter> counters=getCounterByCustomerId(customer.getCustomerID());
		 String groupcode=null;
		 String customerCode=customer.getCustomerCode();
		 String customerType=customer.getCustomerType();
		 if("直营".equals(customerType)){
			 return customerCode.substring(0,customerCode.indexOf("."));
		 }
		 if("加盟商".equals(customerType)){
			 return customerCode.substring(0,customerCode.indexOf(".J."));
		 }
		 for (Counter counter : counters) {
			 String counterType=counter.getCounterType();
			 if("运营商".equals(customerType)){
				 if(!counterType.startsWith("B.Y")){
					 return counterType.substring(0, counterType.indexOf("."));
				 }
			 }
			 if("代理商".equals(customerType)||"分销商".equals(customerType)){
				 if(!counterType.startsWith("B.Y")){
					 String[] str = counterType.split("\\.");
						return counterType.substring(0, counterType.indexOf(str[3]) - 1);
				 }
			 }
		}
		return groupcode;
	}

	@Override
	public List<Counter> findByCounterId(Set<String> counterId) {
		List<Counter> counters = findAll().stream()//
				.filter(e -> counterId.contains(e.getCounterId().toString()))//
				.collect(Collectors.toList());
		return (counters == null || counters.isEmpty()) ? new ArrayList<>(0) : counters;
	}

	@Override
	public List<Integer> findCounterIdForOperator(Customer customer) {
		String code = getCustomerGroupCode(customer);
		List<Integer> counterIds = new ArrayList<>();
		for (Counter counter : findAll()) {
			if (counter.getCounterType() != null//
					&& counter.getCounterType().startsWith(code + "J")) {
				counterIds.add(counter.getCounterId());
			}
		}
		return counterIds;
	}

	@Override
	public Set<Integer> getCustomerIdSByGroupCode(String code) {
		return groupCode.get(code)==null?new HashSet<>():groupCode.get(code);
	}
	
	@Override
	public Set<Integer> getCustomerIdSByFirstCode(String code) {
		Set<Integer> customerIds =new HashSet<>();
		for (String groupcode : groupCode.keySet()) {
			if(groupcode.startsWith(code)){
				customerIds.addAll(groupCode.get(groupcode));
			}
		}
		return customerIds;
	}

	@Override
	public String getGroupCode(Counter counter) {
		String code = null;
		String counterType = counter.getCounterType();
		if (!Strings.isNullOrEmpty(counterType)) {
			String[] str = counterType.split("\\.");
			String secondCode = str[1];
			// Z J
			if ("Z".equals(secondCode) || "J".equals(secondCode)) {
				code = counterType.substring(0, counterType.indexOf("."));
			} else if ("D".equals(secondCode) || "F".equals(secondCode)) {
			// D F
				code = counterType.substring(0, counterType.indexOf(str[3]) - 1);
			}
			// Y NOP
		}
		return code;
	}
	
	@Override
	public void excute(Integer counterId, String orderNo) throws ServiceException {
		MaterialTemplateService templateService = AppContext.getBean(MaterialTemplateService.class);
		try {
			templateService.excute(counterId, orderNo);
		} catch (Exception e) {
			logger.error("向订单添加新店物料错误");
			throw new ServiceException(e);
		}
		
	}
}
