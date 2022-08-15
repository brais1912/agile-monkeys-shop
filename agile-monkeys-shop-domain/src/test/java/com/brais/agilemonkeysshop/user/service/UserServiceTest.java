package com.brais.agilemonkeysshop.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.brais.agilemonkeysshop.FullUser;
import com.brais.agilemonkeysshop.LiteUser;
import com.brais.agilemonkeysshop.user.UserStatusEnum;
import com.brais.agilemonkeysshop.user.UserTypeEnum;
import com.brais.agilemonkeysshop.user.persistence.UserPersistencePort;
import com.brais.agilemonkeysshop.user.service.exception.UserServiceException.UserNotFoundServiceException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  private static final String USER_ID = "1";

  private static final String USERNAME = "username";

  private static final String PASSWORD = "password";

  private static final String NAME = "name";

  private static final String SURNAME = "surname";

  private static final Integer AGE = 20;

  private static final UserTypeEnum USER_TYPE = UserTypeEnum.ADMIN;

  private static final UserStatusEnum USER_STATUS = UserStatusEnum.INACTIVE;

  private static final FullUser FULL_USER_TEST_1 =
      new FullUser(USER_ID, USERNAME, PASSWORD, NAME, SURNAME, AGE, USER_TYPE, USER_STATUS);

  private static final FullUser FULL_USER_TEST_2 = new FullUser(USER_ID, USERNAME, PASSWORD, NAME, SURNAME, AGE, null, null);

  private static final LiteUser LITE_USER_TEST = new LiteUser(USERNAME, PASSWORD, USER_TYPE.getValue(), USER_STATUS.getValue());

  @Mock
  private UserPersistencePort userPersistencePort;

  @InjectMocks
  private UserServiceImpl userService;

  @Nested
  class FindAll {

    @ParameterizedTest
    @MethodSource("fromFindAllDataSet")
    void when_callToFindAll_expect_returnFullUserList(List<FullUser> testFullUser, List<FullUser> expected) {
      when(userPersistencePort.findAll()).thenReturn(testFullUser);

      List<FullUser> actual = userService.findAll();

      Mockito.verify(userPersistencePort).findAll();
      assertEquals(expected, actual);
    }

    static Stream<Arguments> fromFindAllDataSet() {
      return Stream.of(
          arguments(List.of(), List.of()),
          arguments(List.of(FULL_USER_TEST_1), List.of(FULL_USER_TEST_1)),
          arguments(List.of(FULL_USER_TEST_2), List.of(FULL_USER_TEST_2)),
          arguments(null, null));
    }
  }

  @Nested
  class Create {

    @Test
    void when_callToCreate_expect_callReturnFullUser() {
      when(userPersistencePort.create(FULL_USER_TEST_1)).thenReturn(FULL_USER_TEST_1);

      FullUser actual = userService.create(FULL_USER_TEST_1);

      Mockito.verify(userPersistencePort).create(FULL_USER_TEST_1);
      assertEquals(FULL_USER_TEST_1, actual);
    }

    @Test
    void when_callToCreate_expect_callReturnNull() {
      when(userPersistencePort.create(FULL_USER_TEST_1)).thenReturn(null);

      FullUser actual = userService.create(FULL_USER_TEST_1);

      Mockito.verify(userPersistencePort).create(FULL_USER_TEST_1);
      assertNull(actual);
    }
  }

  @Nested
  class Update {

    @Test
    void when_callToUpdate_expect_callReturnFullUser() {
      when(userPersistencePort.update(FULL_USER_TEST_1)).thenReturn(Optional.of(FULL_USER_TEST_1));

      FullUser actual = userService.update(FULL_USER_TEST_1);

      Mockito.verify(userPersistencePort).update(FULL_USER_TEST_1);
      assertEquals(FULL_USER_TEST_1, actual);
    }

    @Test
    void when_callToCreate_expect_callReturnOptionalEmpty_userNotFoundExceptionThrown() {
      when(userPersistencePort.update(FULL_USER_TEST_1)).thenReturn(Optional.empty());

      assertThrows(UserNotFoundServiceException.class, () -> userService.update(FULL_USER_TEST_1));
      Mockito.verify(userPersistencePort).update(FULL_USER_TEST_1);
    }
  }

  @Nested
  class Delete {

    @Test
    void when_callToDelete_expect_callNoExceptionThrown() {
      when(userPersistencePort.findById(USER_ID)).thenReturn(Optional.of(LITE_USER_TEST));
      doNothing().when(userPersistencePort).delete(USER_ID);

      userService.delete(USER_ID);

      Mockito.verify(userPersistencePort).findById(USER_ID);
      Mockito.verify(userPersistencePort).delete(USER_ID);
    }

    @Test
    void when_callToDelete_expect_callExceptionThrown() {
      when(userPersistencePort.findById(USER_ID)).thenReturn(Optional.empty());

      assertThrows(UserNotFoundServiceException.class, () -> userService.delete(USER_ID));
      Mockito.verify(userPersistencePort).findById(USER_ID);
      Mockito.verify(userPersistencePort, never()).delete(USER_ID);
    }
  }

}
