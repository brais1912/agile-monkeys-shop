package com.brais.agilemonkeysshop.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.brais.agilemonkeysshop.user.entity.User;

public interface UserRepository extends MongoRepository<User, String> {

  Optional<List<User>> findByUsername(String username);

}
