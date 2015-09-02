package com.fb.dao;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

public class FBDaoImpl extends SqlMapClientDaoSupport implements FBDao {

	private static Logger logger = Logger.getLogger(FBDaoImpl.class);

	private static String NAMESPACE = "FB" + ".";

	public void setNamespace(String namespace) {
		NAMESPACE = namespace + ".";
	}

	public Object queryForObject(String name, Object critObj) throws DataAccessException {
		logger.info("QueryObject: " + NAMESPACE + name);
		return this.getSqlMapClientTemplate().queryForObject(NAMESPACE + name, critObj);
	}

	@SuppressWarnings("unchecked")
	public List queryForList(String name, Object critObj) throws DataAccessException {
		logger.info("QueryList: " + NAMESPACE + name);
		return this.getSqlMapClientTemplate().queryForList(NAMESPACE + name, critObj);
	}

	public Object insert(String name, Object paramObj) throws DataAccessException {
		logger.info("Insert: " + NAMESPACE + name);
		return this.getSqlMapClientTemplate().insert(NAMESPACE + name, paramObj);
	}

	public int update(String name, Object paramObj) throws DataAccessException {
		logger.info("Update: " + NAMESPACE + name);
		return this.getSqlMapClientTemplate().update(NAMESPACE + name, paramObj);
	}

	public int delete(String name, Object paramObj) throws DataAccessException {
		logger.info("Delete: " + NAMESPACE + name);
		return this.getSqlMapClientTemplate().delete(NAMESPACE + name, paramObj);
	}
	
	@SuppressWarnings("unchecked")
	public Map call(String name, Map paramMap) throws DataAccessException {
		logger.info("Call: " + NAMESPACE + name);
		this.getSqlMapClientTemplate().queryForObject(NAMESPACE + name, paramMap);
		return paramMap;
	}

}
