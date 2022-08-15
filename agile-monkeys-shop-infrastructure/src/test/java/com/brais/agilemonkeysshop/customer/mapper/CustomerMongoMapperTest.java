package com.brais.agilemonkeysshop.customer.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.brais.agilemonkeysshop.customer.FullCustomer;
import com.brais.agilemonkeysshop.customer.LiteCustomer;
import com.brais.agilemonkeysshop.customer.entity.Customer;
import org.junit.jupiter.api.Test;

class CustomerMongoMapperTest {

  private static final String CUSTOMER_ID = "1";

  private static final String NAME = "name";

  private static final String SURNAME = "surname";

  private static final String PHOTO_URL = "test";

  private static final MultipartFile PHOTO_FILE = new MockMultipartFile("photo", null, "image", new byte[]{});

  private static final String CREATED_BY = "admin";

  private static final String UPDATED_BY = "monkey";

  private static final FullCustomer FULL_CUSTOMER_TEST_1 =
      new FullCustomer(CUSTOMER_ID, NAME, SURNAME, PHOTO_FILE, PHOTO_URL, CREATED_BY, UPDATED_BY);

  private static final LiteCustomer LITE_CUSTOMER_TEST = new LiteCustomer(CUSTOMER_ID, NAME, SURNAME);

  private static final Customer CUSTOMER = Customer.builder()
      .id(CUSTOMER_ID)
      .name(NAME)
      .surname(SURNAME)
      .photo(PHOTO_URL)
      .createdBy(CREATED_BY)
      .updatedBy(UPDATED_BY)
      .build();

  private final CustomerMongoMapper customerMongoMapper = new CustomerMongoMapperImpl();

  @Test
  void when_callToLiteCustomerList_expect_returnListLiteCustomer() {
    List<LiteCustomer> actual = customerMongoMapper.toLiteCustomerList(List.of(CUSTOMER));

    assertEquals(List.of(LITE_CUSTOMER_TEST), actual);
  }

  @Test
  void when_callToLiteCustomer_expect_returnLiteCustomer() {
    LiteCustomer actual = customerMongoMapper.toLiteCustomer(CUSTOMER);

    assertEquals(LITE_CUSTOMER_TEST, actual);
  }

  @Test
  void when_callToFullCustomer_expect_returnFullCustomer() {
    FullCustomer actual = customerMongoMapper.toFullCustomer(CUSTOMER);

    assertEquals(new FullCustomer(CUSTOMER_ID, NAME, SURNAME, null, PHOTO_URL, CREATED_BY, UPDATED_BY), actual);
  }

  @Test
  void when_callToCustomer_expect_returnCustomer() {
    Customer actual = customerMongoMapper.toCustomer(FULL_CUSTOMER_TEST_1);

    assertEquals(CUSTOMER, actual);
  }

  @Test
  void when_callToCustomerUpdated_expect_returnCustomer() {
    Customer actual = customerMongoMapper.toCustomerUpdated(FULL_CUSTOMER_TEST_1, CREATED_BY);

    assertEquals(CUSTOMER, actual);
  }

}
