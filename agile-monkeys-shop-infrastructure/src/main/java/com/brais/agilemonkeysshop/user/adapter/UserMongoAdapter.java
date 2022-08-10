package com.brais.agilemonkeysshop.user.adapter;

import java.util.List;
import java.util.Optional;

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

  @Override
  public Optional<FullUser> findById(String userId) {
    return userRepository.findById(userId)
        .map(retrievedUser -> userMongoMapper.toFullUser(retrievedUser));
  }

  @Override
  public FullUser create(FullUser fullUser) {
    User user = userMongoMapper.toUser(fullUser);

    return userMongoMapper.toFullUser(userRepository.insert(user));
  }

  @Override
  public Optional<FullUser> update(FullUser fullUser) {
    return userRepository.findById(fullUser.id())
        .map(user -> updateExistingUser(userMongoMapper.toUser(fullUser)))
        .map(user -> fullUser);
  }

  private User updateExistingUser(User existingUser) {
    return userRepository.save(existingUser);
  }

  @Override
  public void delete(String userId) {
    userRepository.deleteById(userId);
  }
}
