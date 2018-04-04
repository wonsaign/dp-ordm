package com.zeusas.dp.ordm.service;

import java.rmi.ServerException;
import java.util.List;
import java.util.Set;

import com.zeusas.dp.ordm.entity.Customer;

/**
 * 
 * @author shihx
 * @date 2016年12月14日 上午10:07:42
 */
public interface CustomerManager {
	
	public List<Customer> findAll();
	
	public Customer get(Integer id);
	
	public List<Customer> findByName(String Name);
	
	public List<Customer> pagination(int page,int num);
	
	public void updateCounterSet(Customer c) throws ServerException;
	
	public List<Customer> findAllChildren(Customer customer);
	
	/**
	 * 获取子所有子客户柜台id
	 * @param customer
	 * @return
	 */
	public List<Integer> findAllChildrenCounterId(Customer customer);
	
	public Set<String> findAllChildrenCounters(Customer customer);
	
	/**
	 * 客户编码(代理商 分销商 加盟商)
	 * W.J.001 W.Z.003 ->W
	 * W.D.某某某.J.001 W.D.某某某.Z.001 ->W.D
	 * W.F.某某某.001 ->W.F
	 * B.Z->B.Z
	 * B->B
	 * B.Z.赣-B.Z
	 * @param counter
	 * @return
	 */
	public String getGroupCode(Customer customer);
	
	public List<Customer> getCustomerByGroupCode(String code);
	
	void load();
}
