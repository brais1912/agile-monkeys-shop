package com.brais.agilemonkeysshop.user.service;

import java.util.List;

import com.brais.agilemonkeysshop.FullUser;
import com.brais.agilemonkeysshop.user.persistence.UserPersistencePort;
import com.brais.agilemonkeysshop.user.port.UserServicePort;
import com.brais.agilemonkeysshop.user.service.exception.UserServiceException.UserNotFoundServiceException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserServiceImpl implements UserServicePort {

  private final UserPersistencePort userPersistencePort;

  @Override
  public List<FullUser> findAll() {
    return userPersistencePort.findAll();
  }

  @Override
  public FullUser create(FullUser fullUser) {
    return userPersistencePort.create(fullUser);
  }

  @Override
  public FullUser update(FullUser fullUser) {
    return userPersistencePort.update(fullUser).orElseThrow(() -> new UserNotFoundServiceException("User " + fullUser.id() + " not found"));
  }

  @Override
  public void delete(String userId) {
    userPersistencePort.findById(userId).orElseThrow(() -> new UserNotFoundServiceException("User " + userId + " not found"));
    userPersistencePort.delete(userId);
  }
}
