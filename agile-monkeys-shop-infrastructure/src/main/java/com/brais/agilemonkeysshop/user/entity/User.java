package com.brais.agilemonkeysshop.user.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonId;

@Document("user")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class User {

  @BsonId
  private String id;

  private String username;

  private String password;

  private String name;

  private String surname;

  private Integer age;

  private String userType;

  private String userStatus;

}
