package com.brais.agilemonkeysshop.rest.controller.customer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.agilemonkeys.shop.api.CustomerApi;
import com.agilemonkeys.shop.model.CreateCustomerResponseDTO;
import com.agilemonkeys.shop.model.CustomerDTO;
import com.agilemonkeys.shop.model.CustomerListResponseDTO;
import com.agilemonkeys.shop.model.DeleteCustomerResponseDTO;
import com.agilemonkeys.shop.model.UpdateCustomerResponseDTO;
import com.brais.agilemonkeysshop.customer.FullCustomer;
import com.brais.agilemonkeysshop.customer.LiteCustomer;
import com.brais.agilemonkeysshop.customer.port.CustomerServicePort;
import com.brais.agilemonkeysshop.rest.controller.AbstractController;
import com.brais.agilemonkeysshop.rest.controller.customer.mapper.CustomerApiMapper;

@RestController
@RequestMapping("${openapi.agilemonkeysshop.base-path:/agile-monkeys/v1}")
public class CustomerController extends AbstractController implements CustomerApi {

  @Autowired
  private CustomerServicePort customerServicePort;

  @Autowired
  private CustomerApiMapper customerApiMapper;

  @Override
  public ResponseEntity<CustomerListResponseDTO> findAll() {
    List<LiteCustomer> customerList = customerServicePort.findAll();

    CustomerListResponseDTO customerListResponseDTO = customerApiMapper.toCustomerListResponseDTO(customerList);
    return ResponseEntity.ok(customerListResponseDTO);
  }

  @Override
  public ResponseEntity<CustomerDTO> getFullInfo(String customerId) {
    FullCustomer fullCustomer = customerServicePort.findById(customerId);
    CustomerDTO customerDTO = customerApiMapper.toCustomerDTO(fullCustomer);

    return ResponseEntity.ok(customerDTO);
  }

  @Override
  public ResponseEntity<CreateCustomerResponseDTO> create(String id, String name, String surname, MultipartFile photo) {
    String createdBy = getLoggedUserId();
    FullCustomer fullCustomer =
        customerApiMapper.toFullCustomer(id, name, surname, photo, getPhotoUrlIfPhotoExists(photo), createdBy, createdBy);
    FullCustomer fullCustomerCreated = customerServicePort.create(fullCustomer);

    return ResponseEntity.ok(new CreateCustomerResponseDTO().id(fullCustomerCreated.id()));
  }

  @Override
  public ResponseEntity<UpdateCustomerResponseDTO> update(String customerId, String name, String surname, MultipartFile photo) {
    String updatedBy = getLoggedUserId();
    // TODO exception if photo is not MultipartFile (i.e.: photo instanceof Multipartfile)
    FullCustomer fullCustomer =
        customerApiMapper.toFullCustomer(customerId, name, surname, photo, getPhotoUrlIfPhotoExists(photo), updatedBy);
    FullCustomer fullCustomerUpdated = customerServicePort.update(fullCustomer);

    return ResponseEntity.ok(new UpdateCustomerResponseDTO().id(fullCustomerUpdated.id()));
  }

  @Override
  public ResponseEntity<DeleteCustomerResponseDTO> delete(String customerId) {
    customerServicePort.delete(customerId);
    return ResponseEntity.ok(new DeleteCustomerResponseDTO().id(customerId));
  }
}
