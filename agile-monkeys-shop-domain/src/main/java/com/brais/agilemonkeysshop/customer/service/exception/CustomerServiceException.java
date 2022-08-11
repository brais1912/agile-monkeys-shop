package com.brais.agilemonkeysshop.customer.service.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class CustomerServiceException extends RuntimeException {

  private static final String TITLE_EXCEPTION = "Customer service exception";

  @RequiredArgsConstructor
  public enum Reason {
    CUSTOMER_NOT_FOUND_EXCEPTION(1, "Customer not found exception");

    private final int id;

    private final String errorMessage;
  }

  private final String code;

  private final String title;

  private final String errorMessage;

  protected CustomerServiceException(Reason reason, String title, String errorMessage) {
    super(errorMessage);
    this.code = String.valueOf(reason.id);
    this.title = title;
    this.errorMessage = errorMessage;
  }

  public static final class CustomerNotFoundServiceException extends CustomerServiceException {

    public CustomerNotFoundServiceException(String errorMessage) {
      super(Reason.CUSTOMER_NOT_FOUND_EXCEPTION, TITLE_EXCEPTION + " - " + Reason.CUSTOMER_NOT_FOUND_EXCEPTION.errorMessage, errorMessage);
    }
  }
}
