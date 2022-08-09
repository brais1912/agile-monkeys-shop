package com.brais.agilemonkeysshop.customer.port;

import java.util.List;

import com.brais.agilemonkeysshop.customer.FullCustomer;
import com.brais.agilemonkeysshop.customer.LiteCustomer;

public interface CustomerServicePort {

  List<LiteCustomer> findAll();

  FullCustomer findById(String customerId);

  FullCustomer create(FullCustomer fullCustomer);

  FullCustomer update(FullCustomer fullCustomer);

  void delete(String customerId);

}
