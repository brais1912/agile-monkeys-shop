package com.brais.agilemonkeysshop.user;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum UserStatusEnum {
  ACTIVE("active"),
  INACTIVE("inactive"),
  SUSPENDED("suspended"),
  UNKNOWN("unknown");

  private final String value;

  public String getValue() {
    return value;
  }

  public static UserStatusEnum fromValue(String value) {
    for (UserStatusEnum b : UserStatusEnum.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    return UNKNOWN;
  }
}
