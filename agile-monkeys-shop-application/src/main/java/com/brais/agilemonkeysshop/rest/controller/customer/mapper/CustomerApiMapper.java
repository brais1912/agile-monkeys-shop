package com.brais.agilemonkeysshop.rest.controller.customer.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.agilemonkeys.shop.model.CustomerDTO;
import com.agilemonkeys.shop.model.CustomerListResponseDTO;
import com.brais.agilemonkeysshop.customer.FullCustomer;
import org.apache.commons.collections4.CollectionUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface CustomerApiMapper {

  default CustomerListResponseDTO toCustomerListResponseDTO(List<FullCustomer> fullCustomerList) {
    List<CustomerDTO> customerDTOList = CollectionUtils.emptyIfNull(fullCustomerList)
        .stream()
        .map(this::toCustomerDTO)
        .toList();
    return new CustomerListResponseDTO().customerList(customerDTOList);
  }

  CustomerDTO toCustomerDTO(FullCustomer fullCustomer);
}
