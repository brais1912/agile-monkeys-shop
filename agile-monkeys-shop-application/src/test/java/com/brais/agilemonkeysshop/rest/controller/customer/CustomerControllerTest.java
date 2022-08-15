package com.brais.agilemonkeysshop.rest.controller.customer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

import com.agilemonkeys.shop.model.CreateCustomerResponseDTO;
import com.agilemonkeys.shop.model.CustomerDTO;
import com.agilemonkeys.shop.model.CustomerListResponseDTO;
import com.agilemonkeys.shop.model.DeleteCustomerResponseDTO;
import com.agilemonkeys.shop.model.LiteCustomerDTO;
import com.agilemonkeys.shop.model.UpdateCustomerResponseDTO;
import com.brais.agilemonkeysshop.customer.FullCustomer;
import com.brais.agilemonkeysshop.customer.LiteCustomer;
import com.brais.agilemonkeysshop.customer.config.CustomerPhotoConfig;
import com.brais.agilemonkeysshop.customer.port.CustomerServicePort;
import com.brais.agilemonkeysshop.rest.controller.customer.controller.CustomerController;
import com.brais.agilemonkeysshop.rest.controller.customer.mapper.CustomerApiMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

  private static final String CUSTOMER_ID = "1";

  private static final String NAME = "name";

  private static final String SURNAME = "surname";

  private static final String PHOTO = "photo";

  private static final String CREATED_BY = "admin";

  private static final String UPDATED_BY = "monkey";

  private static final MockMultipartFile PHOTO_FILE = new MockMultipartFile("test", "originalName", "image", new byte[]{});

  private static final LiteCustomerDTO LITE_CUSTOMER_DTO = new LiteCustomerDTO().id(CUSTOMER_ID).name(NAME).surname(SURNAME);

  private static final CustomerDTO CUSTOMER_DTO =
      new CustomerDTO().id(CUSTOMER_ID).name(NAME).surname(SURNAME).photo(PHOTO).createdBy(CREATED_BY).updatedBy(UPDATED_BY);

  @Mock
  private CustomerServicePort customerServicePort;

  @Mock
  private CustomerApiMapper customerApiMapper;

  @Mock
  private CustomerPhotoConfig customerPhotoConfig;

  @InjectMocks
  private CustomerController customerController;

  @Test
  void when_callToFindAll_expect_returnCustomerListResponseDTO() {
    List<LiteCustomer> liteCustomerList = List.of(new LiteCustomer(CUSTOMER_ID, NAME, SURNAME));
    List<LiteCustomerDTO> customerDTOList = List.of(LITE_CUSTOMER_DTO);
    CustomerListResponseDTO expected = new CustomerListResponseDTO().customerList(customerDTOList);

    when(customerServicePort.findAll()).thenReturn(liteCustomerList);
    when(customerApiMapper.toCustomerListResponseDTO(liteCustomerList)).thenReturn(expected);

    ResponseEntity<CustomerListResponseDTO> actual = customerController.findAll();

    assertEquals(HttpStatus.OK, actual.getStatusCode());
    assertEquals(expected, actual.getBody());
  }

  @Test
  void when_callToGetFullInfo_expect_returnCustomerDTO() {
    FullCustomer fullCustomer =
        new FullCustomer(CUSTOMER_ID, NAME, SURNAME, PHOTO_FILE, PHOTO, CREATED_BY, UPDATED_BY);

    when(customerServicePort.findById(CUSTOMER_ID)).thenReturn(fullCustomer);
    when(customerApiMapper.toCustomerDTO(fullCustomer)).thenReturn(CUSTOMER_DTO);

    ResponseEntity<CustomerDTO> actual = customerController.getFullInfo(CUSTOMER_ID);

    assertEquals(HttpStatus.OK, actual.getStatusCode());
    assertEquals(CUSTOMER_DTO, actual.getBody());
  }

  @Test
  void when_callToCreate_expect_returnCreateCustomerResponseDTO() {
    FullCustomer fullCustomer =
        new FullCustomer(CUSTOMER_ID, NAME, SURNAME, PHOTO_FILE, PHOTO, CREATED_BY, CREATED_BY);
    CreateCustomerResponseDTO expected = new CreateCustomerResponseDTO().id(CUSTOMER_ID);

    try (MockedStatic<SecurityContextHolder> securityContextHolderMockedStatic = Mockito.mockStatic(SecurityContextHolder.class)) {
      securityContextHolderMockedStatic.when(SecurityContextHolder::getContext)
          .thenReturn(new SecurityContextImpl(new TestingAuthenticationToken(CREATED_BY, UPDATED_BY)));
      when(customerApiMapper.toFullCustomer(CUSTOMER_ID, NAME, SURNAME, PHOTO_FILE, PHOTO, CREATED_BY,
          CREATED_BY)).thenReturn(fullCustomer);
      when(customerServicePort.create(fullCustomer)).thenReturn(fullCustomer);
      when(customerPhotoConfig.getPhotoUrl()).thenReturn(PHOTO);

      ResponseEntity<CreateCustomerResponseDTO> actual =
          customerController.create(CUSTOMER_ID, NAME, SURNAME, PHOTO_FILE);

      assertEquals(HttpStatus.OK, actual.getStatusCode());
      assertEquals(expected, actual.getBody());
    }
  }

  @Test
  void when_callToUpdate_expect_returnUpdateCustomerResponseDTO() {
    FullCustomer fullCustomer =
        new FullCustomer(CUSTOMER_ID, NAME, SURNAME, PHOTO_FILE, PHOTO, CREATED_BY, UPDATED_BY);
    UpdateCustomerResponseDTO expected = new UpdateCustomerResponseDTO().id(CUSTOMER_ID);

    try (MockedStatic<SecurityContextHolder> securityContextHolderMockedStatic = Mockito.mockStatic(SecurityContextHolder.class)) {
      securityContextHolderMockedStatic.when(SecurityContextHolder::getContext)
          .thenReturn(new SecurityContextImpl(new TestingAuthenticationToken(UPDATED_BY, UPDATED_BY)));
      when(customerApiMapper.toFullCustomer(CUSTOMER_ID, NAME, SURNAME, PHOTO_FILE, PHOTO, UPDATED_BY)).thenReturn(fullCustomer);
      when(customerServicePort.update(fullCustomer)).thenReturn(fullCustomer);
      when(customerPhotoConfig.getPhotoUrl()).thenReturn(PHOTO);

      ResponseEntity<UpdateCustomerResponseDTO> actual =
          customerController.update(CUSTOMER_ID, NAME, SURNAME, PHOTO_FILE);

      assertEquals(HttpStatus.OK, actual.getStatusCode());
      assertEquals(expected, actual.getBody());
    }
  }

  @Test
  void when_callToDelete_expect_returnDeleteCustomerResponseDTO() {
    doNothing().when(customerServicePort).delete(CUSTOMER_ID);

    ResponseEntity<DeleteCustomerResponseDTO> actual = customerController.delete(CUSTOMER_ID);

    assertEquals(HttpStatus.OK, actual.getStatusCode());
    assertNotNull(actual.getBody());
    assertEquals(CUSTOMER_ID, actual.getBody().getId());
  }
}
