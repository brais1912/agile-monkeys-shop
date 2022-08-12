package com.brais.agilemonkeysshop.rest.controller.user;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import com.agilemonkeys.shop.model.CreateUserRequestDTO;
import com.agilemonkeys.shop.model.UpdateUserRequestDTO;
import com.agilemonkeys.shop.model.UserDTO;
import com.agilemonkeys.shop.model.UserListResponseDTO;
import com.agilemonkeys.shop.model.UserStatusDTO;
import com.agilemonkeys.shop.model.UserTypeDTO;
import com.brais.agilemonkeysshop.FullUser;
import com.brais.agilemonkeysshop.rest.controller.user.mapper.UserApiMapper;
import com.brais.agilemonkeysshop.rest.controller.user.mapper.UserApiMapperImpl;
import com.brais.agilemonkeysshop.user.UserStatusEnum;
import com.brais.agilemonkeysshop.user.UserTypeEnum;
import org.junit.jupiter.api.Test;

public class UserApiMapperTest {

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

  private static final UserDTO USER_DTO =
      new UserDTO().id(USER_ID).username(USERNAME).password(PASSWORD).name(NAME).surname(SURNAME).age(AGE).userType(UserTypeDTO.ADMIN)
          .userStatus(UserStatusDTO.INACTIVE);

  private UserApiMapper userApiMapper = new UserApiMapperImpl();

  @Test
  void when_callToUserListResponseDTO_expect_returnUserListResponseDTO() {
    UserListResponseDTO actual = userApiMapper.toUserListResponseDTO(List.of(FULL_USER_TEST_1));

    assertEquals(new UserListResponseDTO().userList(List.of(USER_DTO)), actual);
  }

  @Test
  void when_callToUserDTO_expect_returnUserDTO() {
    UserDTO actual = userApiMapper.toUserDTO(FULL_USER_TEST_1);

    assertEquals(USER_DTO, actual);
  }

  @Test
  void when_callToFullUserCreate_expect_returnFullUser() {
    FullUser actual = userApiMapper.toFullUser(
        new CreateUserRequestDTO().id(USER_ID).username(USERNAME).password(PASSWORD).name(NAME).surname(SURNAME).age(AGE)
            .userType(UserTypeDTO.ADMIN).userStatus(UserStatusDTO.INACTIVE));

    assertEquals(FULL_USER_TEST_1, actual);
  }

  @Test
  void when_callToFullUserUpdate_expect_returnFullUser() {
    FullUser actual = userApiMapper.toFullUser(USER_ID,
        new UpdateUserRequestDTO().username(USERNAME).password(PASSWORD).name(NAME).surname(SURNAME).age(AGE.toString())
            .userType(UserTypeDTO.ADMIN).userStatus(UserStatusDTO.INACTIVE));

    assertEquals(FULL_USER_TEST_1, actual);
  }

}
