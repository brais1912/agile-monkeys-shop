package com.brais.agilemonkeysshop.rest.controller.advice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;

import com.agilemonkeys.shop.model.ErrorResponseDTO;
import com.brais.agilemonkeysshop.rest.controller.advice.mapper.ErrorResponseMapper;
import com.brais.agilemonkeysshop.rest.controller.advice.mapper.ErrorResponseMapperImpl;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ErrorResponseMapperTest {

  private static final String CODE = "code";

  private static final String TITLE = "title";

  private static final String DESCRIPTION = "description";

  ErrorResponseMapper errorResponseMapper = new ErrorResponseMapperImpl();

  @ParameterizedTest
  @MethodSource("fromToErrorResponseDtoDataSet")
  void when_callToErrorResponseDto_expect_callReturnErrorResponseDto(String code, String title, String description,
      ErrorResponseDTO expected) {

    ErrorResponseDTO actual = errorResponseMapper.toErrorResponseDto(code, title, description, null);

    assertEquals(expected, actual);
  }

  static Stream<Arguments> fromToErrorResponseDtoDataSet() {
    ErrorResponseDTO errorResponseDTO =
        new ErrorResponseDTO().code(CODE).title(TITLE).description(DESCRIPTION);

    return Stream.of(
        arguments(null, null, null, null, null),
        arguments("code", "title", "description", errorResponseDTO));
  }
}
