package com.brais.agilemonkeysshop.rest.controller.customer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agilemonkeys.shop.api.CustomerApi;
import com.agilemonkeys.shop.model.CustomerListResponseDTO;
import com.brais.agilemonkeysshop.customer.port.CustomerServicePort;
import com.brais.agilemonkeysshop.rest.controller.customer.mapper.CustomerApiMapper;

@RestController
@RequestMapping("${openapi.agilemonkeysshop.base-path:/agile-monkeys/v1}")
public class CustomerController implements CustomerApi {

  @Autowired
  private CustomerServicePort customerServicePort;

  @Autowired
  private CustomerApiMapper customerApiMapper;

  @Override
  public ResponseEntity<CustomerListResponseDTO> findAll() {
    CustomerListResponseDTO customerListResponseDTO = customerApiMapper.toCustomerListResponseDTO(customerServicePort.findAll());
    return ResponseEntity.ok(customerListResponseDTO);
  }

//  @Override
//  public ResponseEntity<CreateCustomerResponseDTO> create(String name, String surname, MultipartFile photo) {
//    if (photo.isEmpty()) {
//      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CreateCustomerResponseDTO());
//    }
//
//    String photoName = "";
//    try {
//
//      // Get the file and save it somewhere
//      byte[] bytes = photo.getBytes();
//      photoName = name + "_" + surname + "_" + "id." + FilenameUtils.getExtension(photo.getOriginalFilename());
//      Path path = Paths.get("C://temp//" + photoName);
//      Files.write(path, bytes);
//
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//    return ResponseEntity.ok(new CreateCustomerResponseDTO().id(photoName));
//  }
}
