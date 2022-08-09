package com.brais.agilemonkeysshop.user;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum UserStatusEnum {
  ACTIVE("active"),
  INACTIVE("inactive"),
  SUSPENDED("suspended");

  private final String value;

  public String getValue() {
    return value;
  }
}
