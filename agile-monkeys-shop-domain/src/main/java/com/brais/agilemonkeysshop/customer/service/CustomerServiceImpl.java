package com.brais.agilemonkeysshop.customer.service;

import java.util.List;

import com.brais.agilemonkeysshop.customer.FullCustomer;
import com.brais.agilemonkeysshop.customer.persistence.CustomerPersistencePort;
import com.brais.agilemonkeysshop.customer.port.CustomerServicePort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerServicePort {

  private final CustomerPersistencePort customerPersistencePort;

  @Override
  public List<FullCustomer> findAll() {
    return customerPersistencePort.findAll();
  }
}
