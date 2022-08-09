package com.brais.agilemonkeysshop.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.brais.agilemonkeysshop.user.adapter.UserMongoAdapter;
import com.brais.agilemonkeysshop.user.persistence.UserPersistencePort;
import com.brais.agilemonkeysshop.user.port.UserServicePort;
import com.brais.agilemonkeysshop.user.service.UserServiceImpl;

@Configuration
public class UserConfig {

  @Bean
  public UserPersistencePort userPersistencePort() {
    return new UserMongoAdapter();
  }

  @Bean
  public UserServicePort userServicePort() {
    return new UserServiceImpl(userPersistencePort());
  }
}
