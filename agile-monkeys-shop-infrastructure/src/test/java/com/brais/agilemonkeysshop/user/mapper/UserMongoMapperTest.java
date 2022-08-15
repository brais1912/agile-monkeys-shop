package com.brais.agilemonkeysshop.user.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import com.brais.agilemonkeysshop.FullUser;
import com.brais.agilemonkeysshop.LiteUser;
import com.brais.agilemonkeysshop.user.UserStatusEnum;
import com.brais.agilemonkeysshop.user.UserTypeEnum;
import com.brais.agilemonkeysshop.user.entity.User;
import org.junit.jupiter.api.Test;

class UserMongoMapperTest {

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

  private static final LiteUser LITE_USER_TEST = new LiteUser(USERNAME, PASSWORD, USER_TYPE.getValue(), USER_STATUS.getValue());

  private static final User USER = User.builder()
      .id(USER_ID)
      .username(USERNAME)
      .password(PASSWORD)
      .name(NAME)
      .surname(SURNAME)
      .age(AGE)
      .userType(USER_TYPE.getValue())
      .userStatus(USER_STATUS.getValue())
      .build();

  private final UserMongoMapper userMongoMapper = new UserMongoMapperImpl();

  @Test
  void when_callToFullUserList_expect_returnListFullUser() {
    List<FullUser> actual = userMongoMapper.toFullUserList(List.of(USER));

    assertEquals(List.of(FULL_USER_TEST_1), actual);
  }

  @Test
  void when_callToFullUser_expect_returnFullUser() {
    FullUser actual = userMongoMapper.toFullUser(USER);

    assertEquals(FULL_USER_TEST_1, actual);
  }

  @Test
  void when_callToLiteUser_expect_returnLiteUser() {
    LiteUser actual = userMongoMapper.toLiteUser(USER);

    assertEquals(LITE_USER_TEST, actual);
  }

  @Test
  void when_callToUser_expect_returnUser() {
    User actual = userMongoMapper.toUser(FULL_USER_TEST_1);

    assertEquals(USER.getId(), actual.getId());
    assertEquals(USER.getUsername(), actual.getUsername());
    assertNotNull(USER.getPassword());
    assertEquals(USER.getName(), actual.getName());
    assertEquals(USER.getSurname(), actual.getSurname());
    assertEquals(USER.getUserType(), actual.getUserType());
    assertEquals(USER.getUserStatus(), actual.getUserStatus());
  }
}
