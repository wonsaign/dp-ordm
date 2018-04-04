package com.zeusas.dp.ordm.dao.impl;

import org.springframework.stereotype.Repository;

import com.zeusas.common.dao.CoreBasicDao;
import com.zeusas.dp.ordm.dao.ActivityLimitDao;
import com.zeusas.dp.ordm.entity.ActivityLimit;

@Repository
public class ActivityLimitDaoImpl extends CoreBasicDao<ActivityLimit, Integer> implements ActivityLimitDao {

}
