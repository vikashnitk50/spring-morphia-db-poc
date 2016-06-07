package com.spring.mongo.entity;

public enum Priority {

  HIGH(1), MEDIUM(2), LOW(3);

  private int value;

  Priority(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public static Priority getPriority(int value) {
    Priority priorityArray[] = Priority.values();
    for (Priority priority : priorityArray) {
      if (value == priority.value) {
        return priority;
      }
    }
    return null;
  }

}
