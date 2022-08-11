package com.brais.agilemonkeysshop.security.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.brais.agilemonkeysshop.LiteUser;
import com.brais.agilemonkeysshop.user.UserStatusEnum;
import com.brais.agilemonkeysshop.user.UserTypeEnum;
import com.brais.agilemonkeysshop.user.persistence.UserPersistencePort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserSecurityService implements UserDetailsService {

  private final UserPersistencePort userPersistencePort;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User.UserBuilder userBuilder = User.withUsername(username);
    LiteUser liteUser =
        userPersistencePort.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found"));
    if (UserStatusEnum.fromValue(liteUser.userStatus()) != UserStatusEnum.ACTIVE) {
      throw new RuntimeException("User " + username + " is not active. User status: " + liteUser.userStatus());
    }
    userBuilder.password(liteUser.password()).roles(String.valueOf(UserTypeEnum.fromValue(liteUser.userType())));
    return userBuilder.build();
  }
}
