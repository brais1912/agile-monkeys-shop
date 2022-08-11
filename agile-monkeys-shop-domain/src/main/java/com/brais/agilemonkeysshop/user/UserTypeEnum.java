package com.brais.agilemonkeysshop.user;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum UserTypeEnum {
  ADMIN("admin"),
  USER("user"),
  UNDEFINED("undefined");

  private final String value;

  public String getValue() {
    return value;
  }

  public static UserTypeEnum fromValue(String value) {
    for (UserTypeEnum b : UserTypeEnum.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    return UNDEFINED;
  }
}
