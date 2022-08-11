package com.brais.agilemonkeysshop.rest.controller.advice.mapper;

import com.agilemonkeys.shop.model.ErrorResponseDTO;
import org.mapstruct.Mapper;

@Mapper
public interface ErrorResponseMapper {

  ErrorResponseDTO toErrorResponseDto(String code, String title, String description, String... additionalData);

}
