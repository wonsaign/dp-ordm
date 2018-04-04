package com.zeusas.dp.ordm.bean;

import java.util.Objects;

import com.zeusas.core.entity.TaskTrace;

public class CartLocker implements TaskTrace {
	final Long cartId;
	final Integer counterId;

	String userId;

	public CartLocker(Long chartId, Integer counterId) {
		this.cartId = chartId;
		this.counterId = counterId;
	}

	public CartLocker(Long cartId, Integer counterId, String userId) {
		this.cartId = cartId;
		this.counterId = counterId;
		this.userId = userId;
	}

	public Long getCartId() {
		return cartId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getCounterId() {
		return counterId;
	}

	public synchronized void lock(String userId) {
		if (this.userId == null) {
			this.userId = userId;
			return;
		} else if (userId.equals(userId)) {
			return;
		}
		throw new RuntimeException("Cart is locked by another!");
	}

	public void unlock(String userId) {
		if (userId.equals(this.userId)) {
			this.userId = null;
		}
	}

	/**
	 * 判断锁是否被其他人给锁了  true=被其他锁了
	 * @param id  userId
	 * @return
	 */
	public boolean isLocked(String id) {
		return userId != null && !userId.equals(id);
	}


	@Override
	public void start() {
		// NOP
	}

	@Override
	public void release() {
		this.userId = null;
	}

	public int hashCode() {
		return this.counterId == null ? 0 : this.counterId.hashCode();
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || !(obj instanceof CartLocker)) {
			return false;
		}
		CartLocker b = (CartLocker) obj;
		return Objects.equals(this.cartId, b.cartId) //
				&& Objects.equals(this.counterId, b.counterId)//
				&& Objects.equals(this.userId, b.userId);
	}

	@Override
	public String toString() {
		return "CartLocker [cartId=" + cartId + ", counterId=" + counterId + ", userId=" + userId + "]";
	}
}
