package com.spring.mongo.repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.lang.reflect.ParameterizedType;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.QueryResults;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;
import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.DBCollection;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;
import com.spring.mongo.core.DatastoreFactory;

public abstract class AbstractGenericReposiroty<T, PK> implements
		GenericRepository<T, PK> {

	@Autowired
	protected DatastoreFactory dsFactory;

	protected Class<T> entityClazz;

	@SuppressWarnings("unchecked")
	public AbstractGenericReposiroty() {
		this.entityClazz = ((Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0]);
	}

	public void bulkInsert(List<T> entities) {
		bulkInsert(entities, WriteConcern.ACKNOWLEDGED);
	}

	public void bulkInsert(List<T> entities, WriteConcern writeConcern) {
		dsFactory.getDS().save(entities, writeConcern);
	}

	public T findOne(Map<String, String> fieldValueMap) {
		Query<T> query = dsFactory.getDS().createQuery(getEntityClass());
		for (Map.Entry<String, String> fieldAndValue : fieldValueMap.entrySet()) {
			query.field(fieldAndValue.getKey()).equalIgnoreCase(
					fieldAndValue.getValue());
		}
		return (T) query.get();

	}

	public long count() {
		return dsFactory.getDS().getCount(entityClazz);
	}

	public long count(final String key, final Object value) {
		return count(dsFactory.getDS().find(entityClazz, key, value));
	}

	public long count(final Query<T> query) {
		return dsFactory.getDS().getCount(query);
	}

	public Query<T> createQuery() {
		return dsFactory.getDS().createQuery(entityClazz);
	}

	public UpdateOperations<T> createUpdateOperations() {
		return dsFactory.getDS().createUpdateOperations(entityClazz);
	}

	public WriteResult delete(final T entity) {
		return dsFactory.getDS().delete(entity);
	}

	public WriteResult delete(final T entity, final WriteConcern wc) {
		return dsFactory.getDS().delete(entity, wc);
	}

	public WriteResult deleteById(final PK id) {
		return dsFactory.getDS().delete(entityClazz, id);
	}

	public WriteResult deleteByQuery(final Query<T> query) {
		return dsFactory.getDS().delete(query);
	}

	public void ensureIndexes() {
		dsFactory.getDS().ensureIndexes(entityClazz);
	}

	public boolean exists(final String key, final Object value) {
		return exists(dsFactory.getDS().find(entityClazz, key, value));
	}

	public boolean exists(final Query<T> query) {
		return dsFactory.getDS().getCount(query) > 0;
	}

	public QueryResults<T> find() {
		return createQuery();
	}

	public QueryResults<T> find(final Query<T> query) {
		return query;
	}

	@SuppressWarnings("unchecked")
	public List<PK> findIds() {
		return (List<PK>) keysToIds(dsFactory.getDS().find(entityClazz)
				.asKeyList());
	}

	@SuppressWarnings("unchecked")
	public List<PK> findIds(final String key, final Object value) {
		return (List<PK>) keysToIds(dsFactory.getDS()
				.find(entityClazz, key, value).asKeyList());
	}

	@SuppressWarnings("unchecked")
	public List<PK> findIds(final Query<T> query) {
		return (List<PK>) keysToIds(query.asKeyList());
	}

	public T findOne(final String key, final Object value) {
		return dsFactory.getDS().find(entityClazz, key, value).get();
	}

	public T findOne(final Query<T> query) {
		return query.get();
	}

	public Key<T> findOneId() {
		return findOneId(dsFactory.getDS().find(entityClazz));
	}

	public Key<T> findOneId(final String key, final Object value) {
		return findOneId(dsFactory.getDS().find(entityClazz, key, value));
	}

	public Key<T> findOneId(final Query<T> query) {
		Iterator<Key<T>> keys = query.fetchKeys().iterator();
		return keys.hasNext() ? keys.next() : null;
	}

	public T get(final PK id) {
		return dsFactory.getDS().get(entityClazz, id);
	}

	public DBCollection getCollection() {
		return dsFactory.getDS().getCollection(entityClazz);
	}

	public Datastore getDatastore() {
		return dsFactory.getDS();
	}

	public Class<T> getEntityClass() {
		return entityClazz;
	}

	public Key<T> save(final T entity) {
		return dsFactory.getDS().save(entity);
	}

	public Key<T> save(final T entity, final WriteConcern wc) {
		return dsFactory.getDS().save(entity, wc);
	}

	public UpdateResults update(final Query<T> query,
			final UpdateOperations<T> ops) {
		return dsFactory.getDS().update(query, ops);
	}

	public UpdateResults updateFirst(final Query<T> query,
			final UpdateOperations<T> ops) {
		return dsFactory.getDS().updateFirst(query, ops);
	}

	public Class<T> getEntityClazz() {
		return entityClazz;
	}

	/**
	 * Converts from a List<Key> to their id values
	 */
	protected List<?> keysToIds(final List<Key<T>> keys) {
		final List<Object> ids = new ArrayList<Object>(keys.size() * 2);
		for (final Key<T> key : keys) {
			ids.add(key.getId());
		}
		return ids;
	}

}
