package com.brais.agilemonkeysshop.user.adapter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import com.brais.agilemonkeysshop.FullUser;
import com.brais.agilemonkeysshop.LiteUser;
import com.brais.agilemonkeysshop.user.UserStatusEnum;
import com.brais.agilemonkeysshop.user.UserTypeEnum;
import com.brais.agilemonkeysshop.user.entity.User;
import com.brais.agilemonkeysshop.user.mapper.UserMongoMapper;
import com.brais.agilemonkeysshop.user.repository.UserRepository;
import com.brais.agilemonkeysshop.user.service.exception.UserAdapterException.ExistingUserWithSameIdException;
import com.brais.agilemonkeysshop.user.service.exception.UserAdapterException.ExistingUserWithSameUsernameException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserMongoAdapterTest {

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

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserMongoMapper userMongoMapper;

  @InjectMocks
  private UserMongoAdapter userMongoAdapter;

  @Test
  void when_callToFindAll_expect_returnListFulLUser() {
    when(userRepository.findAll()).thenReturn(List.of(USER));
    when(userMongoMapper.toFullUserList(List.of(USER))).thenReturn(List.of(FULL_USER_TEST_1));

    List<FullUser> actual = userMongoAdapter.findAll();

    assertEquals(List.of(FULL_USER_TEST_1), actual);
  }

  @Test
  void when_callFindById_expect_returnOptionalLiteUser() {
    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(USER));
    when(userMongoMapper.toLiteUser(USER)).thenReturn(LITE_USER_TEST);

    Optional<LiteUser> actual = userMongoAdapter.findById(USER_ID);

    assertEquals(Optional.of(LITE_USER_TEST), actual);
  }

  @Test
  void when_callFindById_expect_returnOptionalEmpty() {
    when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

    Optional<LiteUser> actual = userMongoAdapter.findById(USER_ID);

    Mockito.verify(userMongoMapper, never()).toLiteUser(USER);
    assertEquals(Optional.empty(), actual);
  }

  @Test
  void when_callFindByUsername_expect_returnOptionalLiteUser() {
    when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(USER));
    when(userMongoMapper.toLiteUser(USER)).thenReturn(LITE_USER_TEST);

    Optional<LiteUser> actual = userMongoAdapter.findByUsername(USERNAME);

    assertEquals(Optional.of(LITE_USER_TEST), actual);
  }

  @Test
  void when_callFindByUsername_expect_returnOptionalEmpty() {
    when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());

    Optional<LiteUser> actual = userMongoAdapter.findByUsername(USERNAME);

    Mockito.verify(userMongoMapper, never()).toLiteUser(USER);
    assertEquals(Optional.empty(), actual);
  }

  @Test
  void when_callCreate_expect_returnFullUser() {
    when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());
    when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());
    when(userMongoMapper.toUser(FULL_USER_TEST_1)).thenReturn(USER);
    when(userRepository.insert(USER)).thenReturn(USER);
    when(userMongoMapper.toFullUser(USER)).thenReturn(FULL_USER_TEST_1);

    FullUser actual = userMongoAdapter.create(FULL_USER_TEST_1);

    assertEquals(FULL_USER_TEST_1, actual);
  }

  @Test
  void when_callCreate_expect_returnExistingUserWithSameIdException() {
    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(USER));

    Mockito.verify(userRepository, never()).findByUsername(any());
    Mockito.verify(userMongoMapper, never()).toUser(any());
    Mockito.verify(userRepository, never()).insert(any(User.class));
    Mockito.verify(userMongoMapper, never()).toFullUser(any());
    assertThrows(ExistingUserWithSameIdException.class, () -> userMongoAdapter.create(FULL_USER_TEST_1));
  }

  @Test
  void when_callCreate_expect_returnExistingUserWithSameUsernameException() {
    when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());
    when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(USER));

    Mockito.verify(userMongoMapper, never()).toUser(any());
    Mockito.verify(userRepository, never()).insert(any(User.class));
    Mockito.verify(userMongoMapper, never()).toFullUser(any());
    assertThrows(ExistingUserWithSameUsernameException.class, () -> userMongoAdapter.create(FULL_USER_TEST_1));
  }

  @Test
  void when_callUpdate_expect_returnOptionalFullUser() {
    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(USER));
    when(userMongoMapper.toUser(FULL_USER_TEST_1)).thenReturn(USER);
    when(userRepository.save(USER)).thenReturn(USER);

    Optional<FullUser> actual = userMongoAdapter.update(FULL_USER_TEST_1);

    Mockito.verify(userRepository, never()).findByUsername(any());
    assertEquals(Optional.of(FULL_USER_TEST_1), actual);
  }

  @Test
  void when_callUpdate_expect_returnExistingUserWithSameUsernameException() {
    User existingUserWithDifferentUserName = User.builder()
        .id(USER_ID)
        .username("different")
        .password(PASSWORD)
        .name(NAME)
        .surname(SURNAME)
        .build();
    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(existingUserWithDifferentUserName));
    when(userMongoMapper.toUser(FULL_USER_TEST_1)).thenReturn(USER);
    when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(existingUserWithDifferentUserName));

    Mockito.verify(userRepository, never()).save(any());
    assertThrows(ExistingUserWithSameUsernameException.class, () -> userMongoAdapter.update(FULL_USER_TEST_1));
  }

  @Test
  void when_callDelete_expect_userRepositoryIsCalled() {
    doNothing().when(userRepository).deleteById(USER_ID);

    userMongoAdapter.delete(USER_ID);

    Mockito.verify(userRepository).deleteById(USER_ID);
  }

}
