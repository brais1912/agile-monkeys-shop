package com.brais.agilemonkeysshop.customer.persistence;

import java.util.List;

import com.brais.agilemonkeysshop.customer.FullCustomer;

public interface CustomerPersistencePort {

  List<FullCustomer> findAll();

}
