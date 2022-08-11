package com.brais.agilemonkeysshop.customer.service.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class CustomerAdapterException extends RuntimeException {

  private static final String TITLE_EXCEPTION = "Customer adapter exception";

  @RequiredArgsConstructor
  public enum Reason {
    EXISTING_CUSTOMER_WITH_SAME_ID(1, "Existing customer with same ID exception");

    private final int id;

    private final String errorMessage;
  }

  private final String code;

  private final String title;

  private final String errorMessage;

  protected CustomerAdapterException(CustomerAdapterException.Reason reason, String title, String errorMessage) {
    super(errorMessage);
    this.code = String.valueOf(reason.id);
    this.title = title;
    this.errorMessage = errorMessage;
  }

  public static final class ExistingCustomerWithSameIdException extends CustomerAdapterException {

    public ExistingCustomerWithSameIdException(String errorMessage) {
      super(Reason.EXISTING_CUSTOMER_WITH_SAME_ID, TITLE_EXCEPTION + " - " + Reason.EXISTING_CUSTOMER_WITH_SAME_ID.errorMessage,
          errorMessage);
    }
  }
}
