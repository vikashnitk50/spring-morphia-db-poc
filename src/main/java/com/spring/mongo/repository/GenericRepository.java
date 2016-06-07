package com.spring.mongo.repository;

import java.util.List;
import java.util.Map;

import org.mongodb.morphia.dao.DAO;

import com.mongodb.WriteConcern;

/**
 * 
 * This is generic Repository Interface - this will be 
 * implemented by AbstractGenericReposiroty 
 *
 * @param <T>
 * @param <PK>
 */
public interface GenericRepository<T, PK> extends DAO<T, PK> {

	public void bulkInsert(List<T> entities);

	public void bulkInsert(List<T> entities, WriteConcern writeConcern);

	public T findOne(Map<String, String> fieldValueMap);
	

}
