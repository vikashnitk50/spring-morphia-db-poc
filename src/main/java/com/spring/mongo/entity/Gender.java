package com.spring.mongo.entity;

public enum Gender {

  MALE("M"),

  FEMALE("F");

  private String value;

  Gender(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public static Gender getGender(String value) {
    Gender genderArray[] = Gender.values();
    for (Gender gender : genderArray) {
      if (gender.getValue().equalsIgnoreCase(value)) {
        return gender;
      }
    }
    return null;
  }

}
