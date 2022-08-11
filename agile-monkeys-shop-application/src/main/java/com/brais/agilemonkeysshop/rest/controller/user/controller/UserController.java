package com.brais.agilemonkeysshop.rest.controller.user.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agilemonkeys.shop.api.UserApi;
import com.agilemonkeys.shop.model.CreateUserRequestDTO;
import com.agilemonkeys.shop.model.CreateUserResponseDTO;
import com.agilemonkeys.shop.model.DeleteUserResponseDTO;
import com.agilemonkeys.shop.model.UpdateUserRequestDTO;
import com.agilemonkeys.shop.model.UpdateUserResponseDTO;
import com.agilemonkeys.shop.model.UserListResponseDTO;
import com.brais.agilemonkeysshop.FullUser;
import com.brais.agilemonkeysshop.rest.controller.AbstractController;
import com.brais.agilemonkeysshop.rest.controller.user.mapper.UserApiMapper;
import com.brais.agilemonkeysshop.user.port.UserServicePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("${openapi.agilemonkeysshop.base-path:/agile-monkeys/v1}")
public class UserController extends AbstractController implements UserApi {

  private static final Logger logger = LoggerFactory.getLogger(UserController.class);

  @Autowired
  private UserServicePort userServicePort;

  @Autowired
  private UserApiMapper userApiMapper;

  @Override
  public ResponseEntity<CreateUserResponseDTO> createUser(@Valid CreateUserRequestDTO createUserRequestDTO) {
    validateRequiredFieldsOrThrowException(createUserRequestDTO.getId(),
        createUserRequestDTO.getUsername(),
        createUserRequestDTO.getPassword(),
        createUserRequestDTO.getName(),
        createUserRequestDTO.getSurname());

    FullUser fullUser = userApiMapper.toFullUser(createUserRequestDTO);
    FullUser fullUserCreated = userServicePort.create(fullUser);

    return ResponseEntity.ok(new CreateUserResponseDTO().id(fullUserCreated.id()));
  }

  @Override
  public ResponseEntity<DeleteUserResponseDTO> deleteUser(String userId) {
    userServicePort.delete(userId);
    return ResponseEntity.ok(new DeleteUserResponseDTO().id(userId));
  }

  @Override
  public ResponseEntity<UserListResponseDTO> findAllUsers() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    logger.info("Datos usuario: {}", auth.getPrincipal());
    logger.info("Datos de permisos: {}", auth.getAuthorities());
    logger.info("Está autenticado: {}", auth.isAuthenticated());

    List<FullUser> fullUserList = userServicePort.findAll();

    UserListResponseDTO userListResponseDTO = userApiMapper.toUserListResponseDTO(fullUserList);
    return ResponseEntity.ok(userListResponseDTO);
  }

  @Override
  public ResponseEntity<UpdateUserResponseDTO> updateUser(String userId, UpdateUserRequestDTO updateUserRequestDTO) {
    FullUser fullUser = userApiMapper.toFullUser(userId, updateUserRequestDTO);
    FullUser fullUserUpdated = userServicePort.update(fullUser);

    return ResponseEntity.ok(new UpdateUserResponseDTO().id(fullUserUpdated.id()));
  }
}
