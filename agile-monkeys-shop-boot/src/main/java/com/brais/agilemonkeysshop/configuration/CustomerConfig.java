package com.brais.agilemonkeysshop.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.brais.agilemonkeysshop.customer.adapter.CustomerMongoAdapter;
import com.brais.agilemonkeysshop.customer.persistence.CustomerPersistencePort;
import com.brais.agilemonkeysshop.customer.port.CustomerServicePort;
import com.brais.agilemonkeysshop.customer.service.CustomerServiceImpl;

@Configuration
public class CustomerConfig {

  @Bean
  public CustomerPersistencePort customerPersistencePort() {
    return new CustomerMongoAdapter();
  }

  @Bean
  public CustomerServicePort customerServicePort() {
    return new CustomerServiceImpl(customerPersistencePort());
  }

}
