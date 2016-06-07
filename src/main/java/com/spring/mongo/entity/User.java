package com.spring.mongo.entity;

import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.IndexOptions;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.annotations.Property;

import com.spring.mongo.db.utils.Constants;

@Entity(value = Constants.MONGO_DB_USER_COLLECTION)
@Indexes(@Index(fields = { @Field("display_name") }, options = @IndexOptions(unique = true)))
public class User extends BaseEntity {
	
	@Property("ic")
	private String ic;

	@Property("name")
	private String name;

	@Property("display_name")
	private String displayName;

	@Property("age")
	private int age;

	@Embedded(value = "addresses")
	private List<Address> addresses;

	@Embedded(value = "gender")
	private Gender gender;


	public String getIc() {
		return ic;
	}

	public void setIc(String ic) {
		this.ic = ic;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}


	public List<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}