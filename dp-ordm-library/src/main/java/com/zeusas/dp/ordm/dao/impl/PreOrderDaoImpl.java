package com.zeusas.dp.ordm.dao.impl;

import org.springframework.stereotype.Repository;

import com.zeusas.common.dao.CoreBasicDao;
import com.zeusas.dp.ordm.dao.PreOrderDao;
import com.zeusas.dp.ordm.entity.PreOrder;

@Repository
public class PreOrderDaoImpl extends CoreBasicDao<PreOrder, Long> implements PreOrderDao {

}
