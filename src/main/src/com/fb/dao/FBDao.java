package com.fb.dao;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

public interface FBDao {
	
	public Object queryForObject(String name, Object critObj) throws DataAccessException;

	@SuppressWarnings("unchecked")
	public List queryForList(String name, Object critObj) throws DataAccessException;

	public Object insert(String name, Object paramObj) throws DataAccessException;

	public int update(String name, Object paramObj) throws DataAccessException;
	
	public int delete(String name, Object paramObj) throws DataAccessException;
	
	@SuppressWarnings("unchecked")
	public Map call(String name, Map paramMap) throws DataAccessException;

}
