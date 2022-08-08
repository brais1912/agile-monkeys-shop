package com.brais.agilemonkeysshop.customer.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.brais.agilemonkeysshop.customer.entity.Customer;

public interface CustomerRepository extends MongoRepository<Customer, String> {

}
