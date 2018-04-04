package com.zeusas.dp.ordm.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.zeusas.common.dao.CoreBasicDao;
import com.zeusas.core.dao.DaoException;
import com.zeusas.dp.ordm.dao.CartDetailDescDao;
import com.zeusas.dp.ordm.entity.CartDetail;
import com.zeusas.dp.ordm.entity.CartDetailDesc;

@Repository
public class CartDetailDescDaoImpl extends CoreBasicDao<CartDetailDesc, Long>  implements CartDetailDescDao{

	private static Logger logger = LoggerFactory.getLogger(CartDetailDescDaoImpl.class);
	@Override
	public List<CartDetailDesc> getCartDescByCartDetail(CartDetail cartDetail) throws DaoException {
		List<CartDetailDesc> cdes ;
		String where = "WHERE cartDetailId = ? ";
		long detailId = cartDetail.getDetailId();
		try{
			cdes =  find(where,detailId );
		}catch (Exception e) {
			logger.error("获取所有购物车明细异常 ,hql:{},detailId:{}",where,detailId);
			throw new DaoException(e);
		}
		return cdes;
	}

	@Override
	public void deleteDetailAndDesc(Long detailId) throws DaoException {
		String sql = "DELETE FROM " + entityClass.getName() + " WHERE cartDetailId =?";
		try{
			// 删除明细的所有的描述
			super.execute(sql, detailId);
		}catch (Exception e) {
			logger.error("删除明细异常 ,sql:{},detailId:{}",sql,detailId);
			throw new DaoException(e);
		}
	}

	@Override
	public List<CartDetailDesc> getCartDescByCartDetail(List<Long> detailIds) throws DaoException {
		Map<String, Object> map = new HashMap<String, Object>(0);
		map.put("detailIds", detailIds);
		List<CartDetailDesc> cdes;
		String sql = "WHERE cartDetailId IN (:detailIds) ";
		try{
			cdes = find(sql, map);
		}catch (Exception e) {
			logger.error("获取购物车明细异常 ,sql:{}",sql);
			throw new DaoException(e);
		}
		return  cdes;
	}

}
