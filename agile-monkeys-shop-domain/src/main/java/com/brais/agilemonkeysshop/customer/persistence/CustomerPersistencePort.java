package com.brais.agilemonkeysshop.customer.persistence;

import java.util.List;
import java.util.Optional;

import com.brais.agilemonkeysshop.customer.FullCustomer;
import com.brais.agilemonkeysshop.customer.LiteCustomer;

public interface CustomerPersistencePort {

  List<LiteCustomer> findAll();

  Optional<FullCustomer> findById(String customerId);

  FullCustomer create(FullCustomer fullCustomer);

  Optional<FullCustomer> update(FullCustomer fullCustomer);

  void delete(String customerId);
}
