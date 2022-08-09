package com.brais.agilemonkeysshop.rest.controller.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agilemonkeys.shop.api.UserApi;
import com.agilemonkeys.shop.model.CreateUserRequestDTO;
import com.agilemonkeys.shop.model.CreateUserResponseDTO;
import com.agilemonkeys.shop.model.DeleteUserResponseDTO;
import com.agilemonkeys.shop.model.UpdateUserResponseDTO;
import com.agilemonkeys.shop.model.UserListResponseDTO;
import com.brais.agilemonkeysshop.FullUser;
import com.brais.agilemonkeysshop.rest.controller.user.mapper.UserApiMapper;
import com.brais.agilemonkeysshop.user.port.UserServicePort;

@RestController
@RequestMapping("${openapi.agilemonkeysshop.base-path:/agile-monkeys/v1}")
public class UserController implements UserApi {

  @Autowired
  private UserServicePort userServicePort;

  @Autowired
  private UserApiMapper userApiMapper;

  @Override
  public ResponseEntity<CreateUserResponseDTO> createUser(CreateUserRequestDTO createUserRequestDTO) {
    return UserApi.super.createUser(createUserRequestDTO);
  }

  @Override
  public ResponseEntity<DeleteUserResponseDTO> deleteUser(String userId) {
    return UserApi.super.deleteUser(userId);
  }

  @Override
  public ResponseEntity<UserListResponseDTO> findAllUsers() {
    List<FullUser> fullUserList = userServicePort.findAll();

    UserListResponseDTO userListResponseDTO = userApiMapper.toUserListResponseDTO(fullUserList);
    return ResponseEntity.ok(userListResponseDTO);
  }

  @Override
  public ResponseEntity<UpdateUserResponseDTO> updateUser(String userId, String username, String password, String name, String surname,
      String age, String userType, String userStatus) {
    return UserApi.super.updateUser(userId, username, password, name, surname, age, userType, userStatus);
  }
}
