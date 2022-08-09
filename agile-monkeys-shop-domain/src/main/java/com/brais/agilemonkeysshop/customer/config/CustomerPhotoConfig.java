package com.brais.agilemonkeysshop.customer.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "agile.monkeys.customer")
@Data
public class CustomerPhotoConfig {

  private String photoUrl;

}
