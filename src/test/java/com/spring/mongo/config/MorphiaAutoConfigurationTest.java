package com.spring.mongo.config;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mongodb.morphia.Datastore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.spring.mongo.entity.Gender;
import com.spring.mongo.entity.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/config/spring-service.xml" })
public class MorphiaAutoConfigurationTest {

	@Autowired
	private Datastore datastore;

	@Test
	public void testDatastore() {
		System.out.println(datastore);
		Assert.notNull(datastore);
		List<User> users = users(100);
		datastore.save(users);

	}

	private List<User> users(int numberOfUsers) {
		List<User> users = new ArrayList<User>();
		for (int i = 0; i < numberOfUsers; i++) {
			User user = new User();
			user.setName("Vikash Kumar Sinha" + "_"
					+ System.currentTimeMillis());
			user.setIc("Yes");
			user.setAge(35);
			user.setGender(Gender.MALE);
			users.add(user);
		}
		return users;

	}
}
