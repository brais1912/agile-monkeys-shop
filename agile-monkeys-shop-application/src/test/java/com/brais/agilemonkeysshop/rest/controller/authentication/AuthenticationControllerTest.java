package com.brais.agilemonkeysshop.rest.controller.authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.brais.agilemonkeysshop.security.AuthenticationRequest;
import com.brais.agilemonkeysshop.security.TokenInfo;
import com.brais.agilemonkeysshop.security.service.JwtUtilService;
import com.brais.agilemonkeysshop.security.service.UserSecurityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

  private static final String USERNAME = "admin";

  private static final String PASSWORD = "password";

  private static final String JWT_TOKEN = "jwtToken";

  private static final UserDetails USER_DETAILS = User.withUsername(USERNAME).password(PASSWORD).roles("admin").build();

  private static final AuthenticationRequest AUTHENTICATION_REQUEST = new AuthenticationRequest(USERNAME, PASSWORD);

  @Mock
  private AuthenticationManager authenticationManager;

  @Mock
  private UserSecurityService userSecurityService;

  @Mock
  private JwtUtilService jwtUtilService;

  @InjectMocks
  private AuthenticationController authenticationController;

  @Test
  void when_callToAuthenticate_expect_returnJwtToken() {
    when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(USERNAME, PASSWORD))).thenReturn(
        new TestingAuthenticationToken(new Object(), new Object()));
    when(userSecurityService.loadUserByUsername(USERNAME)).thenReturn(USER_DETAILS);
    when(jwtUtilService.generateToken(USER_DETAILS)).thenReturn(JWT_TOKEN);

    ResponseEntity<TokenInfo> actual = authenticationController.authenticate(AUTHENTICATION_REQUEST);

    assertEquals(HttpStatus.OK, actual.getStatusCode());
    assertNotNull(actual.getBody());
    assertEquals(JWT_TOKEN, actual.getBody().jwtToken());
  }
}
