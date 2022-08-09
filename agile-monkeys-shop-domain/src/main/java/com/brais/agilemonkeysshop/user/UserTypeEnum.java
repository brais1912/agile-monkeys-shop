package com.brais.agilemonkeysshop.user;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum UserTypeEnum {
  ADMIN("admin"),
  USER("user");

  private final String value;

  public String getValue() {
    return value;
  }
}
