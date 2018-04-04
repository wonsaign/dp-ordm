package com.zeusas.dp.ordm.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 定义查询条件的接口
 * @author fengx
 *@date 2017年6月5日 上午10:59:42
 */
public class Conditions {
	
	/** */
	Long start;
	/** */
	Long end;
	/** */
	List<Integer>counterId;
	/** */
	String status;
	
	
	public Conditions(Collection<Integer> counterId) {
		super();
		this.counterId = new ArrayList<>(counterId);
	}
	public Conditions(Collection<Integer> counterId, String status) {
		super();
		this.counterId = new ArrayList<>(counterId);
		this.status = status;
	}
	
	public Conditions( Collection<Integer> counterId, String status,Long start, Long end) {
		super();
		this.start = start;
		this.end = end;
		this.counterId = new ArrayList<>(counterId);
		this.status = status;
	}
	public Long getStart() {
		return start;
	}
	public void setStart(Long start) {
		this.start = start;
	}
	public Long getEnd() {
		return end;
	}
	public void setEnd(Long end) {
		this.end = end;
	}
	public List<Integer> getCounterId() {
		return counterId;
	}
	public void setCounterId(List<Integer> counterId) {
		this.counterId = counterId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
	
	
	
	
	
	 

}
