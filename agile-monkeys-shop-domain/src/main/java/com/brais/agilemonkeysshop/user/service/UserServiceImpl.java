package com.brais.agilemonkeysshop.user.service;

import java.util.List;

import com.brais.agilemonkeysshop.FullUser;
import com.brais.agilemonkeysshop.user.persistence.UserPersistencePort;
import com.brais.agilemonkeysshop.user.port.UserServicePort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserServiceImpl implements UserServicePort {

  private final UserPersistencePort userPersistencePort;

  @Override
  public List<FullUser> findAll() {
    return userPersistencePort.findAll();
  }
}
