package com.brais.agilemonkeysshop.customer.mapper;

import java.util.List;

import com.brais.agilemonkeysshop.customer.FullCustomer;
import com.brais.agilemonkeysshop.customer.entity.Customer;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMongoMapper {

  List<FullCustomer> toFullCustomerList(List<Customer> customerList);

  FullCustomer toFullCustomer(Customer customer);
}
