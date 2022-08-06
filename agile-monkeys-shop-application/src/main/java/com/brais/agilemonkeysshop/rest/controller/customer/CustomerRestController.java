package com.brais.agilemonkeysshop.rest.controller.customer;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agilemonkeys.shop.api.CustomerApi;
import com.agilemonkeys.shop.model.CustomerDTO;
import com.agilemonkeys.shop.model.CustomerListResponseDTO;

@RestController
@RequestMapping("${openapi.agilemonkeysshop.base-path:/agile-monkeys/v1}")
public class CustomerRestController implements CustomerApi {

  @Override
  public ResponseEntity<CustomerListResponseDTO> findAll() {
    CustomerListResponseDTO customerListResponseDTO = new CustomerListResponseDTO();
    CustomerDTO customerDTO = new CustomerDTO();
    customerDTO.setId("1111");
    customerDTO.setName("Brais");
    customerDTO.setSurname("González");
    customerDTO.setCreatedBy("2333");
    customerDTO.setUpdatedBy("2333");
    customerDTO.setPhoto("test/112");
    customerListResponseDTO.setCustomerList(List.of(customerDTO));
    return ResponseEntity.ok(customerListResponseDTO);
  }
}
