package com.brais.agilemonkeysshop.customer.port;

import java.util.List;

import com.brais.agilemonkeysshop.customer.FullCustomer;

public interface CustomerServicePort {

  List<FullCustomer> findAll();

}
