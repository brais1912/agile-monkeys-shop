package com.brais.agilemonkeysshop.user.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.brais.agilemonkeysshop.user.entity.User;

public interface UserRepository extends MongoRepository<User, String> {

}
