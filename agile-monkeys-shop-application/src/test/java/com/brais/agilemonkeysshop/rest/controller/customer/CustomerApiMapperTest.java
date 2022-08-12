package com.brais.agilemonkeysshop.rest.controller.customer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.agilemonkeys.shop.model.CustomerDTO;
import com.agilemonkeys.shop.model.LiteCustomerDTO;
import com.brais.agilemonkeysshop.customer.FullCustomer;
import com.brais.agilemonkeysshop.customer.LiteCustomer;
import com.brais.agilemonkeysshop.rest.controller.customer.mapper.CustomerApiMapper;
import com.brais.agilemonkeysshop.rest.controller.customer.mapper.CustomerApiMapperImpl;
import org.junit.jupiter.api.Test;

class CustomerApiMapperTest {

  private static final String CUSTOMER_ID = "id";

  private static final String NAME = "name";

  private static final String SURNAME = "surname";

  private static final String PHOTO = "photo";

  private static final String CREATED_BY = "admin";

  private static final String UPDATED_BY = "monkey";

  private static final String PHOTO_URL = "test";

  private static final MultipartFile PHOTO_FILE = new MockMultipartFile("name", PHOTO, PHOTO_URL, new byte[]{});

  private static final CustomerDTO CUSTOMER_DTO =
      new CustomerDTO().id(CUSTOMER_ID).name(NAME).surname(SURNAME).photo(PHOTO_URL + CUSTOMER_ID + ".").createdBy(CREATED_BY)
          .updatedBy(UPDATED_BY);

  private static final FullCustomer FULL_CUSTOMER =
      new FullCustomer(CUSTOMER_ID, NAME, SURNAME, PHOTO_FILE, PHOTO_URL + CUSTOMER_ID + ".", CREATED_BY, UPDATED_BY);

  private static final LiteCustomerDTO LITE_CUSTOMER_DTO = new LiteCustomerDTO().id(CUSTOMER_ID).name(NAME).surname(SURNAME);

  private static final LiteCustomer LITE_CUSTOMER = new LiteCustomer(CUSTOMER_ID, NAME, SURNAME);

  private CustomerApiMapper customerApiMapper = new CustomerApiMapperImpl();

  @Test
  void when_callToCustomerDto_expect_callReturnCustomerDto() {
    CustomerDTO actual = customerApiMapper.toCustomerDTO(FULL_CUSTOMER);

    assertEquals(CUSTOMER_DTO, actual);
  }

  @Test
  void when_callToCustomerDto_expect_callReturnNull() {
    CustomerDTO actual = customerApiMapper.toCustomerDTO(null);

    assertNull(actual);
  }

  @Test
  void when_callToLiteCustomerDto_expect_callReturnLiteCustomerDto() {
    LiteCustomerDTO actual = customerApiMapper.toLiteCustomerDTO(LITE_CUSTOMER);

    assertEquals(LITE_CUSTOMER_DTO, actual);
  }

  @Test
  void when_callToLiteCustomerDto_expect_callReturnNull() {
    LiteCustomerDTO actual = customerApiMapper.toLiteCustomerDTO(null);

    assertNull(actual);
  }

  @Test
  void when_callToFullCustomerDto_expect_callReturnFullCustomerDto() {
    FullCustomer actual = customerApiMapper.toFullCustomer(CUSTOMER_ID, NAME, SURNAME, PHOTO_FILE, PHOTO_URL, CREATED_BY, UPDATED_BY);

    assertEquals(FULL_CUSTOMER, actual);
  }

  @Test
  void when_callToFullCustomerDto_expect_callReturnNull() {
    FullCustomer actual = customerApiMapper.toFullCustomer(null, null, null, null, null, null);

    assertEquals(new FullCustomer(null, null, null, null, null, null, null), actual);
  }

}
