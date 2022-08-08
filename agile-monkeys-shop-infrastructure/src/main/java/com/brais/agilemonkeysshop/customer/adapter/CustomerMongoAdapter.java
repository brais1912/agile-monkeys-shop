package com.brais.agilemonkeysshop.customer.adapter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brais.agilemonkeysshop.customer.FullCustomer;
import com.brais.agilemonkeysshop.customer.mapper.CustomerMongoMapper;
import com.brais.agilemonkeysshop.customer.persistence.CustomerPersistencePort;
import com.brais.agilemonkeysshop.customer.repository.CustomerRepository;

@Service
public class CustomerMongoAdapter implements CustomerPersistencePort {

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private CustomerMongoMapper customerMongoMapper;

  @Override
  public List<FullCustomer> findAll() {
    return customerMongoMapper.toFullCustomerList(customerRepository.findAll());
  }
}
