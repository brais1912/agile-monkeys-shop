package com.brais.agilemonkeysshop.rest.controller.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

import java.util.List;

import javax.validation.ValidationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.agilemonkeys.shop.model.CreateUserRequestDTO;
import com.agilemonkeys.shop.model.CreateUserResponseDTO;
import com.agilemonkeys.shop.model.DeleteUserResponseDTO;
import com.agilemonkeys.shop.model.UpdateUserRequestDTO;
import com.agilemonkeys.shop.model.UpdateUserResponseDTO;
import com.agilemonkeys.shop.model.UserDTO;
import com.agilemonkeys.shop.model.UserListResponseDTO;
import com.agilemonkeys.shop.model.UserStatusDTO;
import com.agilemonkeys.shop.model.UserTypeDTO;
import com.brais.agilemonkeysshop.FullUser;
import com.brais.agilemonkeysshop.rest.controller.user.controller.UserController;
import com.brais.agilemonkeysshop.rest.controller.user.mapper.UserApiMapper;
import com.brais.agilemonkeysshop.user.UserStatusEnum;
import com.brais.agilemonkeysshop.user.UserTypeEnum;
import com.brais.agilemonkeysshop.user.port.UserServicePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

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
      new UserDTO().id(USER_ID).username(USERNAME).password(PASSWORD).name(NAME).surname(SURNAME).age(AGE).userType(
          UserTypeDTO.ADMIN).userStatus(UserStatusDTO.INACTIVE);

  @Mock
  private UserServicePort userServicePort;

  @Mock
  private UserApiMapper userApiMapper;

  @InjectMocks
  private UserController userController;

  @Test
  void when_callToFindAll_expect_returnUserListResponseDTO() {
    List<FullUser> fullUserList = List.of(FULL_USER_TEST_1);
    List<UserDTO> userDTOList = List.of(USER_DTO);
    UserListResponseDTO expected = new UserListResponseDTO().userList(userDTOList);

    when(userServicePort.findAll()).thenReturn(fullUserList);
    when(userApiMapper.toUserListResponseDTO(fullUserList)).thenReturn(expected);

    ResponseEntity<UserListResponseDTO> actual = userController.findAllUsers();

    assertEquals(HttpStatus.OK, actual.getStatusCode());
    assertEquals(expected, actual.getBody());
  }

  @Test
  void when_callToCreateUser_expect_returnCreateUserResponseDTO() {
    CreateUserRequestDTO createUserRequestDTO = new CreateUserRequestDTO()
        .id(USER_ID)
        .username(USERNAME)
        .password(PASSWORD)
        .name(NAME)
        .surname(SURNAME)
        .age(AGE)
        .userType(UserTypeDTO.ADMIN)
        .userStatus(UserStatusDTO.INACTIVE);

    when(userApiMapper.toFullUser(createUserRequestDTO)).thenReturn(FULL_USER_TEST_1);
    when(userServicePort.create(FULL_USER_TEST_1)).thenReturn(FULL_USER_TEST_1);

    CreateUserResponseDTO expected = new CreateUserResponseDTO().id(USER_ID);
    ResponseEntity<CreateUserResponseDTO> actual = userController.createUser(createUserRequestDTO);

    assertEquals(HttpStatus.OK, actual.getStatusCode());
    assertEquals(expected, actual.getBody());
  }

  @Test
  void when_callToCreateUser_expect_returnValidationException() {
    CreateUserRequestDTO createUserRequestDTO = new CreateUserRequestDTO()
        .id(USER_ID)
        .username(USERNAME)
        .name(NAME)
        .surname(SURNAME)
        .age(AGE)
        .userType(UserTypeDTO.ADMIN)
        .userStatus(UserStatusDTO.INACTIVE);

    Mockito.verify(userApiMapper, never()).toFullUser(createUserRequestDTO);
    Mockito.verify(userServicePort, never()).create(FULL_USER_TEST_1);

    assertThrows(ValidationException.class, () -> userController.createUser(createUserRequestDTO));
  }

  @Test
  void when_callToUpdateUser_expect_returnUpdateUserResponseDTO() {
    UpdateUserRequestDTO updateUserRequestDTO = new UpdateUserRequestDTO()
        .username(USERNAME)
        .name(NAME)
        .surname(SURNAME)
        .age(AGE.toString())
        .userType(UserTypeDTO.ADMIN)
        .userStatus(UserStatusDTO.INACTIVE);

    when(userApiMapper.toFullUser(USER_ID, updateUserRequestDTO)).thenReturn(FULL_USER_TEST_1);
    when(userServicePort.update(FULL_USER_TEST_1)).thenReturn(FULL_USER_TEST_1);

    UpdateUserResponseDTO expected = new UpdateUserResponseDTO().id(USER_ID);
    ResponseEntity<UpdateUserResponseDTO> actual = userController.updateUser(USER_ID, updateUserRequestDTO);

    assertEquals(HttpStatus.OK, actual.getStatusCode());
    assertEquals(expected, actual.getBody());
  }

  @Test
  void when_callToDeleteUser_expect_returnDeleteUserResponseDTO() {
    doNothing().when(userServicePort).delete(USER_ID);

    DeleteUserResponseDTO expected = new DeleteUserResponseDTO().id(USER_ID);
    ResponseEntity<DeleteUserResponseDTO> actual = userController.deleteUser(USER_ID);

    assertEquals(HttpStatus.OK, actual.getStatusCode());
    assertEquals(expected, actual.getBody());
  }
}
