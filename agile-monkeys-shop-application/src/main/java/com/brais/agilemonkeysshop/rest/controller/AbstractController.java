package com.brais.agilemonkeysshop.rest.controller;

import java.util.Arrays;
import java.util.Objects;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import com.brais.agilemonkeysshop.customer.config.CustomerPhotoConfig;

public abstract class AbstractController {

  @Autowired
  private CustomerPhotoConfig customerPhotoConfig;

  protected String getPhotoUrlIfPhotoExists(MultipartFile photo) {
    return photo != null && !Objects.requireNonNull(photo.getOriginalFilename()).isEmpty()
        ? customerPhotoConfig.getPhotoUrl()
        : null;
  }

  protected void validateRequiredFieldsOrThrowException(String... fields) {
    Arrays.stream(fields).forEach(this::validateField);
  }

  private void validateField(String field) {
    if (field == null || field.isEmpty()) {
      throw new ValidationException("One of the required fields (id, username, password, name or surname) is missing");
    }
  }

  protected String getLoggedUserId() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    return auth.getName();
  }

}
