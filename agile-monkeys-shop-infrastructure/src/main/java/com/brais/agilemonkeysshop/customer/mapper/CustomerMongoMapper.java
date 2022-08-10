package com.brais.agilemonkeysshop.customer.mapper;

import java.util.List;

import com.brais.agilemonkeysshop.customer.FullCustomer;
import com.brais.agilemonkeysshop.customer.LiteCustomer;
import com.brais.agilemonkeysshop.customer.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface CustomerMongoMapper {

  List<LiteCustomer> toLiteCustomerList(List<Customer> customerList);

  LiteCustomer toLiteCustomer(Customer customer);

  @Mapping(target = "photoUrl", source = "photo")
  FullCustomer toFullCustomer(Customer customer);

  @Mapping(target = "photo", source = "photoUrl")
  Customer toCustomer(FullCustomer fullCustomer);
}
