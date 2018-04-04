package com.zeusas.dp.ordm.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.zeusas.common.dao.CoreBasicDao;
import com.zeusas.core.dao.DaoException;
import com.zeusas.dp.ordm.dao.MonthPresentDao;
import com.zeusas.dp.ordm.entity.MonthPresent;

/**
 * 
 * @author fengx
 *@date 2017年1月22日 上午11:10:32
 */
@Repository
public class MonthPresentDaoImpl extends CoreBasicDao<MonthPresent, Long> implements MonthPresentDao {

	private static Logger logger = LoggerFactory.getLogger(MonthPresentDaoImpl.class);
	
	@Override
	public List<MonthPresent> findByYearMonth(Integer yearMonth) throws DaoException {
		String where = "WHERE  yearMonth = (:yearMonth) ";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("yearMonth", yearMonth);
		try {
			return find(where, map);
		} catch (Exception e) {
			logger.error("Condition: {}, 年月{}", where, yearMonth);
			throw new DaoException(e);
		}
	}

}
