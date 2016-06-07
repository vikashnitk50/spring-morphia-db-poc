package com.spring.mongo.db.interceptors;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.StringUtils;
import org.mongodb.morphia.dao.DAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.aop.framework.ReflectiveMethodInvocation;

import com.spring.mongo.core.MultitenantDatastoreFactory;
import com.spring.mongo.db.utils.Constants;

public class MongoMultiTenancyInterceptor implements MethodInterceptor {

	private static final Logger LOG = LoggerFactory.getLogger(MongoMultiTenancyInterceptor.class);

	public Object invoke(MethodInvocation invocation) throws Throwable {

		ReflectiveMethodInvocation reflectiveMethodInvocation = (ReflectiveMethodInvocation) invocation;

		Object target = reflectiveMethodInvocation.getThis();

		if (target instanceof DAO) {

			String tenantDBName = MDC.get(Constants.MONGO_TENANT_DB);
			
			LOG.info("Switching to database:  "+tenantDBName);
			
			if(StringUtils.isNotBlank(tenantDBName)){
				MultitenantDatastoreFactory.setDatabaseNameForCurrentThread(tenantDBName);
			}

		}
		Object object= invocation.proceed();
		
		MultitenantDatastoreFactory.clearDatabaseNameForCurrentThread();
		
		return object;

	}

}
