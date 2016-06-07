package com.spring.mongo.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.StopWatch;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.spring.mongo.db.utils.Constants;
import com.spring.mongo.entity.Address;
import com.spring.mongo.entity.Gender;
import com.spring.mongo.entity.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/config/spring-service.xml" })
public class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testSave() {
		MDC.put(Constants.MONGO_TENANT_DB, "uat");
		Assert.notNull(userRepository);
		StopWatch stop = new StopWatch();
		stop.start();
		List<User> users = users(100);
		userRepository.bulkInsert(users);
		stop.stop();
		System.out.println("The method took :" + stop.getTime());
		stop = new StopWatch();
		stop.start();
		users = users(100);
		userRepository.bulkInsert(users);
		stop.stop();
		System.out.println("The method took :" + stop.getTime());
		
	}
	
	@Test
	public void testSaveDuplicate() {
		MDC.put(Constants.MONGO_TENANT_DB, "uat");
		Assert.notNull(userRepository);
		StopWatch stop = new StopWatch();
		stop.start();
		List<User>users=new ArrayList<User>();
		User user=new User();
		user.setDisplayName("VIKASH");
		users.add(user);
		user=new User();
		user.setDisplayName("VIKASH1");
		users.add(user);
		user=new User();
		user.setDisplayName("VIKASH");
		users.add(user);
		user=new User();
		user.setDisplayName("VIKASH1");
		users.add(user);
		userRepository.bulkInsert(users);
		stop.stop();
		System.out.println("The method took :" + stop.getTime());
	}

	@Test
	public void testTotalCount() {
		MDC.put(Constants.MONGO_TENANT_DB, "uat");
		Assert.notNull(userRepository);
		StopWatch stop = new StopWatch();
		stop.start();
		long count= userRepository.count();
		System.out.println(count);
		stop.stop();
		System.out.println("The method took :" + stop.getTime());
	}
	
	@Test
	public void testFindIDs() {
		MDC.put(Constants.MONGO_TENANT_DB, "uat");
		Assert.notNull(userRepository);
		StopWatch stop = new StopWatch();
		stop.start();
		List<String> ids= userRepository.findIds();
		System.out.println(ids);
		stop.stop();
		System.out.println("The method took :" + stop.getTime());
	}

	
	@Test
	public void testGetUserByDisplayName() {
		Assert.notNull(userRepository);
		StopWatch stop = new StopWatch();
		stop.start();
		User user = userRepository.getUserByDisplayName("Vikash_0_1465029492780");
		System.out.println(user);
		stop.stop();
		System.out.println("The method took :" + stop.getTime());
	}

	private List<User> users(int numberOfUsers) {
		List<User> users = new ArrayList<User>();
		for (int i = 0; i < numberOfUsers; i++) {
			User user = new User();
			user.setName("Vikash Kumar Sinha"+"_"+i + "_"
					+ System.currentTimeMillis());
			user.setDisplayName("Vikash" +"_"+i + "_" + System.currentTimeMillis());
			user.setIc("Yes");
			user.setAge(35);
			user.setGender(Gender.MALE);
			user.setAddresses(addresses(100));
			users.add(user);
		}
		return users;
	}

	private List<Address> addresses(int numberOfAddresses) {
		List<Address> addresses = new ArrayList<Address>();
		for (int i = 0; i < numberOfAddresses; i++) {
			Address address = new Address();
			address.setCity("Patna"+"_"+i + "_" + System.currentTimeMillis());
			address.setState("Bihar"+"_"+i + "_" + System.currentTimeMillis());
			address.setCountry("India"+"_"+i + "_" + System.currentTimeMillis());
			addresses.add(address);
		}
		return addresses;
	}

	@After
	public void tearDown() throws Exception {
	}
}
