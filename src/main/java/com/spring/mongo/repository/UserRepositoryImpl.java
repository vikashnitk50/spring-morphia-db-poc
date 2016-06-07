package com.spring.mongo.repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.spring.mongo.entity.User;

@Repository("userRepository")
public class UserRepositoryImpl extends AbstractGenericReposiroty<User, String>
		implements UserRepository {

	enum UserColumn {

		DISPLAY_NAME("display_name");

		private String value;

		UserColumn(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

	}

	public User getUserByDisplayName(String displayName) {
		Map<String, String> fieldValueMap = new HashMap<String, String>();
		fieldValueMap.put(UserColumn.DISPLAY_NAME.getValue(), displayName);
		return super.findOne(fieldValueMap);
	}

}
