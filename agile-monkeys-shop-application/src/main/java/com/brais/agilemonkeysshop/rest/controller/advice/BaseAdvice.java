package com.brais.agilemonkeysshop.rest.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.agilemonkeys.shop.model.ErrorResponseDTO;
import com.brais.agilemonkeysshop.customer.service.exception.CustomerAdapterException;
import com.brais.agilemonkeysshop.customer.service.exception.CustomerAdapterException.ExistingCustomerWithSameIdException;
import com.brais.agilemonkeysshop.customer.service.exception.CustomerServiceException;
import com.brais.agilemonkeysshop.customer.service.exception.CustomerServiceException.CustomerNotFoundServiceException;
import com.brais.agilemonkeysshop.rest.controller.advice.mapper.ErrorResponseMapper;
import com.brais.agilemonkeysshop.user.service.exception.UserAdapterException;
import com.brais.agilemonkeysshop.user.service.exception.UserAdapterException.ExistingUserWithSameIdException;
import com.brais.agilemonkeysshop.user.service.exception.UserAdapterException.ExistingUserWithSameUsernameException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@ControllerAdvice(basePackages = "com.brais.agilemonkeysshop.rest.controller")
public class BaseAdvice extends ResponseEntityExceptionHandler {

  private static final String LOGGER_PREFIX = "[BASE ADVICE] ";

  private static final String JWT_EXCEPTION = "[JWT EXCEPTION] ";

  private final ErrorResponseMapper errorResponseMapper;

  @ExceptionHandler({CustomerNotFoundServiceException.class})
  protected ResponseEntity<ErrorResponseDTO> handleCustomerServiceException(CustomerServiceException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(buildErrorResponse(e.getCode(), e.getTitle(), e.getErrorMessage()));
  }

  @ExceptionHandler({ExistingUserWithSameUsernameException.class, ExistingUserWithSameIdException.class})
  protected ResponseEntity<ErrorResponseDTO> handleUserAdapterException(UserAdapterException e) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(buildErrorResponse(e.getCode(), e.getTitle(), e.getErrorMessage()));
  }

  @ExceptionHandler({ExistingCustomerWithSameIdException.class})
  protected ResponseEntity<ErrorResponseDTO> handleCustomerAdapterException(CustomerAdapterException e) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(buildErrorResponse(e.getCode(), e.getTitle(), e.getErrorMessage()));
  }

  private ErrorResponseDTO buildErrorResponse(String code, String className, String message) {
    ErrorResponseDTO errorResponseDTO = errorResponseMapper.toErrorResponseDto(code, className, message);

    log.error(LOGGER_PREFIX + JWT_EXCEPTION + code + ": " + className + " " + message);
    return errorResponseDTO;
  }

}
