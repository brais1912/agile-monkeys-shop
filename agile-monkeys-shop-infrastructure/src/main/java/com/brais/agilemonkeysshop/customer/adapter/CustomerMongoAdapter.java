package com.brais.agilemonkeysshop.customer.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brais.agilemonkeysshop.customer.FullCustomer;
import com.brais.agilemonkeysshop.customer.LiteCustomer;
import com.brais.agilemonkeysshop.customer.entity.Customer;
import com.brais.agilemonkeysshop.customer.mapper.CustomerMongoMapper;
import com.brais.agilemonkeysshop.customer.persistence.CustomerPersistencePort;
import com.brais.agilemonkeysshop.customer.repository.CustomerRepository;
import com.brais.agilemonkeysshop.customer.service.exception.CustomerAdapterException.ExistingCustomerWithSameIdException;

@Service
public class CustomerMongoAdapter implements CustomerPersistencePort {

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private CustomerMongoMapper customerMongoMapper;

  @Override
  public List<LiteCustomer> findAll() {
    List<Customer> fullCustomerList = customerRepository.findAll();

    return customerMongoMapper.toLiteCustomerList(fullCustomerList);
  }

  @Override
  public Optional<FullCustomer> findById(String customerId) {
    return customerRepository.findById(customerId)
        .map(retrievedCustomer -> customerMongoMapper.toFullCustomer(retrievedCustomer));
  }

  @Override
  public FullCustomer create(FullCustomer fullCustomer) {
    if (customerRepository.findById(fullCustomer.id()).isPresent()) {
      throw new ExistingCustomerWithSameIdException("Existing customer with same ID: " + fullCustomer.id());
    }
    Customer customer = customerMongoMapper.toCustomer(fullCustomer);

    return customerMongoMapper.toFullCustomer(customerRepository.insert(customer));
  }

  @Override
  public Optional<FullCustomer> update(FullCustomer fullCustomer) {
    return customerRepository.findById(fullCustomer.id())
        .map(customer -> updateExistingCustomer(customerMongoMapper.toCustomerUpdated(fullCustomer, customer.getCreatedBy())))
        .map(customer -> fullCustomer);
  }

  private Customer updateExistingCustomer(Customer existingCustomer) {
    return customerRepository.save(existingCustomer);
  }

  @Override
  public void delete(String customerId) {
    customerRepository.deleteById(customerId);
  }
}
