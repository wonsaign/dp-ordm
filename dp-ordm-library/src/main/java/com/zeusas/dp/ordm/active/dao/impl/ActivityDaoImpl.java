package com.zeusas.dp.ordm.active.dao.impl;

import org.springframework.stereotype.Repository;

import com.zeusas.common.dao.CoreBasicDao;
import com.zeusas.dp.ordm.active.dao.ActivityDao;
import com.zeusas.dp.ordm.active.model.Activity;

@Repository
public class ActivityDaoImpl extends CoreBasicDao<Activity, String> implements ActivityDao {

}
