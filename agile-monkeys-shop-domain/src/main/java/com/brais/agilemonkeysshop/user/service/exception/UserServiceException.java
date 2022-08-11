package com.brais.agilemonkeysshop.user.service.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class UserServiceException extends RuntimeException {

  private static final String TITLE_EXCEPTION = "User service exception";

  @RequiredArgsConstructor
  public enum Reason {
    USER_NOT_FOUND_EXCEPTION(1, "User not found exception");

    private final int id;

    private final String errorMessage;
  }

  private final String code;

  private final String title;

  private final String errorMessage;

  protected UserServiceException(UserServiceException.Reason reason, String title, String errorMessage) {
    super(errorMessage);
    this.code = String.valueOf(reason.id);
    this.title = title;
    this.errorMessage = errorMessage;
  }

  public static final class UserNotFoundServiceException extends UserServiceException {

    public UserNotFoundServiceException(String errorMessage) {
      super(Reason.USER_NOT_FOUND_EXCEPTION, TITLE_EXCEPTION + " - " + Reason.USER_NOT_FOUND_EXCEPTION.errorMessage, errorMessage);
    }
  }

}
