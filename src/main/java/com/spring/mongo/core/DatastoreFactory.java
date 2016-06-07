package com.spring.mongo.core;

import org.mongodb.morphia.Datastore;

public interface DatastoreFactory {
	
	public Datastore getDS();

}
