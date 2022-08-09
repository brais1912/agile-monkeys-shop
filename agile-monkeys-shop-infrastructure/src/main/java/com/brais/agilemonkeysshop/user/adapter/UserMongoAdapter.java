package com.brais.agilemonkeysshop.user.adapter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.brais.agilemonkeysshop.FullUser;
import com.brais.agilemonkeysshop.user.entity.User;
import com.brais.agilemonkeysshop.user.mapper.UserMongoMapper;
import com.brais.agilemonkeysshop.user.persistence.UserPersistencePort;
import com.brais.agilemonkeysshop.user.repository.UserRepository;

public class UserMongoAdapter implements UserPersistencePort {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserMongoMapper userMongoMapper;

  @Override
  public List<FullUser> findAll() {
    List<User> userList = userRepository.findAll();
    return userMongoMapper.toFullUserList(userList);
  }
}
