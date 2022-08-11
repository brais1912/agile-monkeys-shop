package com.brais.agilemonkeysshop.user.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.brais.agilemonkeysshop.FullUser;
import com.brais.agilemonkeysshop.LiteUser;
import com.brais.agilemonkeysshop.user.entity.User;
import com.brais.agilemonkeysshop.user.mapper.UserMongoMapper;
import com.brais.agilemonkeysshop.user.persistence.UserPersistencePort;
import com.brais.agilemonkeysshop.user.repository.UserRepository;
import com.brais.agilemonkeysshop.user.service.exception.UserAdapterException.ExistingUserWithSameIdException;
import com.brais.agilemonkeysshop.user.service.exception.UserAdapterException.ExistingUserWithSameUsernameException;

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
  public Optional<LiteUser> findById(String userId) {
    return userRepository.findById(userId)
        .map(retrievedUser -> userMongoMapper.toLiteUser(retrievedUser));
  }

  @Override
  public Optional<LiteUser> findByUsername(String username) {
    return userRepository.findByUsername(username)
        .map(retrievedUser -> userMongoMapper.toLiteUser(retrievedUser));
  }

  @Override
  public FullUser create(FullUser fullUser) {
    if (userRepository.findById(fullUser.id()).isPresent()) {
      throw new ExistingUserWithSameIdException("Existing user with same ID: " + fullUser.id());
    } else if (userNameExists(fullUser.username())) {
      throw new ExistingUserWithSameUsernameException("Username " + fullUser.username() + " already exists");
    }
    User user = userMongoMapper.toUser(fullUser);

    return userMongoMapper.toFullUser(userRepository.insert(user));
  }

  private boolean userNameExists(String username) {
    return userRepository.findByUsername(username).isPresent();
  }

  @Override
  public Optional<FullUser> update(FullUser fullUser) {
    return userRepository.findById(fullUser.id())
        .map(user -> updateExistingUser(user, userMongoMapper.toUser(fullUser)))
        .map(user -> fullUser);
  }

  private User updateExistingUser(User existingUser, User userToUpdate) {
    if (!existingUser.getUsername().equals(userToUpdate.getUsername()) && userNameExists(userToUpdate.getUsername())) {
      throw new ExistingUserWithSameUsernameException("Username " + userToUpdate.getUsername() + " already exists");
    }
    return userRepository.save(userToUpdate);
  }

  @Override
  public void delete(String userId) {
    userRepository.deleteById(userId);
  }
}
