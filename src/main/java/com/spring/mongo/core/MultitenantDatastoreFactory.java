package com.spring.mongo.core;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.mongodb.morphia.Datastore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.spring.mongo.config.MorphiaAutoConfiguration;

@Component
public class MultitenantDatastoreFactory implements DatastoreFactory {

	@Autowired
	private MorphiaAutoConfiguration config;

	@Autowired
	private Datastore defaultDS;

	public Map<String, Datastore> dataStoreMapByTenantId = new HashMap<String, Datastore>();

	private final Lock lock = new ReentrantLock();

	private static boolean INDEXING_REQUIRED = Boolean.TRUE;

	private static final Logger LOG = LoggerFactory
			.getLogger(MultitenantDatastoreFactory.class);

	private static final InheritableThreadLocal<String> MONGO_DB_NAME = new InheritableThreadLocal<String>();

	public Datastore getDS() {
		Datastore tenantDS = getTenantDataStore();
		return tenantDS == null ? defaultDS : tenantDS;
	}

	private Datastore getTenantDataStore() {
		String tenantDBName = MONGO_DB_NAME.get();
		Datastore tenantDS = dataStoreMapByTenantId.get(tenantDBName);
		if (tenantDS == null) {
			lock.lock();
			try {
				tenantDS = dataStoreMapByTenantId.get(tenantDBName);
				if (tenantDS == null) {
					tenantDS = config.createDataStore(
							config.createMongoClient(), tenantDBName,
							INDEXING_REQUIRED);
					dataStoreMapByTenantId.put(tenantDBName, tenantDS);
				}
			} finally {
				lock.unlock();
			}
		}
		return tenantDS;
	}

	public static void setDatabaseNameForCurrentThread(final String databaseName) {
		LOG.debug("Switching to database: " + databaseName);
		MONGO_DB_NAME.set(databaseName);
	}

	public static void clearDatabaseNameForCurrentThread() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Removing database [" + MONGO_DB_NAME.get() + "]");
		}
		MONGO_DB_NAME.remove();
	}

}
