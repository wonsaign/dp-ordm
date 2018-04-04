package com.zeusas.dp.ordm.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.zeusas.core.service.ServiceException;
import com.zeusas.dp.ordm.entity.Counter;
import com.zeusas.dp.ordm.entity.Customer;

/**
 * 
 * @author shihx
 * @date 2016年12月19日 下午6:13:12
 */
public interface CounterManager {

	public Counter get(Integer id);

	/**
	 * 获取有效柜台 但是当前有效柜台和==有柜台
	 * @param Id
	 * @return
	 */
	public Counter getCounterById(Integer Id);

	public Counter getCounterByCode(String counterCode);

	public List<Counter> getCounterByCustomerId(Integer CustomerId);

	public Collection<Counter> findAll();
	@Deprecated
	public List<Counter> pagination(int page, int num);

	public List<Counter> findByName(String Name);

//	public Map<Integer, Counter> getAllCounter();
	
	public void update(Counter counter)throws ServiceException;

	/**
	 * 根据客户 (运营商 代理商) 获取其加盟柜台集合
	 * @param customerId
	 * @param customerType
	 * @return
	 */
	public List<Counter> findCounterForOperator(Customer customer);
	
	/**
	 * 根据客户 (运营商 代理商) 获取其加盟柜台集合
	 * @param customerId
	 * @param customerType
	 * @return
	 */
	@Deprecated
	public List<Integer> findCounterIdForOperator(Customer customer);
	
	/**
	 * 根据客户编码获取客户ID集合(他的加盟客户)
	 * W 
	 * W.D.XXX
	 * W.F.XXX
	 * @param code
	 * @return
	 */
	public Set<Integer> getCustomerIdSByGroupCode(String code);
	/**
	 * D.J.黑.002 ->D
	 * W.F.赵兰.002 -> W.F.赵兰
	 * B.D.李安.Z.016 ->B.D.李安
	 * Y.Z.滇.028 -Y
	 * @param customer
	 * @return
	 */
	public String getGroupCodeByCustomer(Customer customer);
	/**
	 * 根据客户编码获取客户ID集合(他以他的加盟客户)
	 * W 
	 * W.D
	 * W.F
	 * @param code
	 * @return
	 */
	public Set<Integer> getCustomerIdSByFirstCode(String code);
	/**
	 * 柜台所属客户编码
	 * W.J.001 W.Z.003 ->W
	 * W.D.xxx.J.001 W.D.xxx.J.001 ->W.D.xxx
	 * W.F.xxx.001 ->W.F.xxx
	 * @param counter
	 * @return
	 */
	public String getGroupCode(Counter counter);
	
	public List<Counter> findByCounterId(Set<String> counterId);
	/**
	 * 获取客户编码 
	 * W.J.001 W.Z.003 ->W
	 * W.D.xxx.J.001 W.D.xxx.J.001 ->W.D.xxx
	 * W.F.xxx.001 ->W.F.xxx
	 * @param customer
	 * @return
	 */
	public String getCustomerGroupCode(Customer customer);
	
	/**
	 * 把新店物料加到订单 更新柜台新店标志
	 * 因为需要更新柜台 物料模板不需要更新 所以该方法在这
	 * @param counterId
	 * @throws ServiceException
	 */
	public void excute(Integer counterId ,String orderNo)throws ServiceException;
	
	void load();

}
