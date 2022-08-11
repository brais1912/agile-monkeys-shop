package com.brais.agilemonkeysshop.user.service.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class UserAdapterException extends RuntimeException {

  private static final String TITLE_EXCEPTION = "User adapter exception";

  @RequiredArgsConstructor
  public enum Reason {
    EXISTING_USER_WITH_SAME_ID(1, "Existing user with same ID exception"),
    EXISTING_USER_WITH_SAME_USERNAME(1, "Existing user with same Username exception");

    private final int id;

    private final String errorMessage;
  }

  private final String code;

  private final String title;

  private final String errorMessage;

  protected UserAdapterException(Reason reason, String title, String errorMessage) {
    super(errorMessage);
    this.code = String.valueOf(reason.id);
    this.title = title;
    this.errorMessage = errorMessage;
  }

  public static final class ExistingUserWithSameIdException extends UserAdapterException {

    public ExistingUserWithSameIdException(String errorMessage) {
      super(Reason.EXISTING_USER_WITH_SAME_ID, TITLE_EXCEPTION + " - " + Reason.EXISTING_USER_WITH_SAME_ID.errorMessage, errorMessage);
    }
  }

  public static final class ExistingUserWithSameUsernameException extends UserAdapterException {

    public ExistingUserWithSameUsernameException(String errorMessage) {
      super(Reason.EXISTING_USER_WITH_SAME_USERNAME, TITLE_EXCEPTION + " - " + Reason.EXISTING_USER_WITH_SAME_USERNAME.errorMessage,
          errorMessage);
    }
  }
}
