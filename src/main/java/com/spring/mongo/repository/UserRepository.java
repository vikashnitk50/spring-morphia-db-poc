package com.spring.mongo.repository;

import com.spring.mongo.entity.User;

public interface UserRepository extends GenericRepository<User, String> {

	public User getUserByDisplayName(String displayName);
}
