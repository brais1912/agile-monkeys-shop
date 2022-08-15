package com.brais.agilemonkeysshop.security.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.brais.agilemonkeysshop.LiteUser;
import com.brais.agilemonkeysshop.user.UserStatusEnum;
import com.brais.agilemonkeysshop.user.UserTypeEnum;
import com.brais.agilemonkeysshop.user.persistence.UserPersistencePort;
import com.brais.agilemonkeysshop.user.service.exception.UserServiceException.UserNotActiveStatusException;
import com.brais.agilemonkeysshop.user.service.exception.UserServiceException.UserNotFoundServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserSecurityServiceTest {

  private static final String USER_ID = "1";

  private static final String USERNAME = "username";

  private static final String PASSWORD = "password";

  private static final UserTypeEnum USER_TYPE = UserTypeEnum.ADMIN;

  private static final UserStatusEnum USER_STATUS = UserStatusEnum.INACTIVE;

  private static final LiteUser LITE_USER_TEST = new LiteUser(USERNAME, PASSWORD, USER_TYPE.getValue(), USER_STATUS.getValue());

  private static final LiteUser LITE_USER_TEST_ACTIVE =
      new LiteUser(USERNAME, PASSWORD, USER_TYPE.getValue(), UserStatusEnum.ACTIVE.getValue());

  @Mock
  private UserPersistencePort userPersistencePort;

  @InjectMocks
  private UserSecurityService userSecurityService;

  @Test
  void when_callToLoadUserByName_expect_returnUserDetais() {
    UserDetails expected = User.withUsername(USERNAME).password(PASSWORD).roles("admin").build();

    when(userPersistencePort.findByUsername(USERNAME)).thenReturn(Optional.of(LITE_USER_TEST_ACTIVE));

    UserDetails actual = userSecurityService.loadUserByUsername(USERNAME);

    assertEquals(expected, actual);
  }

  @Test
  void when_callToLoadUserByName_expect_returnUserNotActiveStatusException() {
    when(userPersistencePort.findByUsername(USERNAME)).thenReturn(Optional.of(LITE_USER_TEST));

    assertThrows(UserNotActiveStatusException.class, () -> userSecurityService.loadUserByUsername(USERNAME));
  }

  @Test
  void when_callToLoadUserByName_expect_returnUserNotFoundServiceException() {
    when(userPersistencePort.findByUsername(USERNAME)).thenReturn(Optional.empty());

    assertThrows(UserNotFoundServiceException.class, () -> userSecurityService.loadUserByUsername(USERNAME));
  }
}
