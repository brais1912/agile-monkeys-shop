package com.brais.agilemonkeysshop.customer.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonId;

@Document("customer")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Customer {

  @BsonId
  private String id;

  private String name;

  private String surname;

  private String photo;

  private String createdBy;

  private String updatedBy;

}
