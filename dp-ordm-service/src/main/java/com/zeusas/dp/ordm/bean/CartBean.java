package com.zeusas.dp.ordm.bean;

import static com.zeusas.dp.ordm.entity.Cart.STATUS_COMMIT;
import static com.zeusas.dp.ordm.entity.Cart.STATUS_OTHERLOCK;
import static com.zeusas.dp.ordm.entity.Cart.STATUS_OWNERLOCK;
import static com.zeusas.dp.ordm.entity.Cart.STATUS_UNLOCK;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.zeusas.dp.ordm.entity.Cart;
import com.zeusas.dp.ordm.entity.Counter;

/**
 * 封装一个购物车的bean.
 * 
 * @author fengx
 * @date 2016年12月6日 下午3:42:49
 */
public class CartBean  {

	
	/**柜台id*/
	Integer counterId;

	/**购物车状态的描述*/
	String desc;
	/**柜台名称*/
	String counterName;
	
	int status;

	public CartBean(Cart cart,Counter counter) {
		if(cart.getStatus().equals(STATUS_COMMIT)){
			this.desc="购物车已提交";
		}
		if(cart.getStatus().equals(STATUS_UNLOCK)){
			this.desc="购物车未锁定";
		}
		if(cart.getStatus().equals(STATUS_OWNERLOCK)){
			this.desc="购物车自己锁定";
		}
		if(cart.getStatus().equals(STATUS_OTHERLOCK)){
			this.desc="购物车他人锁定";
		}
		this.counterId=counter.getCounterId();
		this.counterName=counter.getCounterName();
	}
	
	public CartBean(Integer counterId,int status,String desc){
		this.counterId=counterId;
		this.status=status;
		this.desc=desc;
	}
	
	public CartBean() {
		super();
	}
 

	public Integer getCounterId() {
		return counterId;
	}

	public void setCounterId(Integer counterId) {
		this.counterId = counterId;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getCounterName() {
		return counterName;
	}

	public void setCounterName(String counterName) {
		this.counterName = counterName;
	}

	public int getStatus() {
		return status;
	}


public void setStatus(int status) {
	this.status = status;
}



	public List<CartBean> getCartBeans(List<Cart> carts, List<Counter> counters) {
		/** 遍历门店id找到所有没有初始化的门店购物车 */
		List<CartBean> cartbeans = new ArrayList<CartBean>();
		List<Integer> counterIds = new ArrayList<Integer>();
		List<Integer> counter_cart = new ArrayList<Integer>();
		Map<Integer, Cart> map_cart = new LinkedHashMap<Integer, Cart>();
		Map<Integer, Counter> map_counter = new HashMap<Integer, Counter>();

		for (Counter counter : counters) {
			counterIds.add(counter.getCounterId());
			map_counter.put(counter.getCounterId(), counter);
		}
		for (Cart cart : carts) {
			counter_cart.add(cart.getCounterId());
			map_cart.put(cart.getCounterId(), cart);
		}
		CartBean cartBean = null;
		for (Integer counterId : counterIds) {
			if (counter_cart.contains(counterId)) {
				Cart cart = map_cart.get(counterId);
				cartBean = new CartBean(cart, map_counter.get(counterId));
			} else {
				Cart cart = new Cart();
				cart.setStatus(STATUS_UNLOCK);
				cartBean = new CartBean(cart, map_counter.get(counterId));
			}
			cartbeans.add(cartBean);
		}
		return cartbeans;
	}



	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}