package com.brais.agilemonkeysshop.customer.adapter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.brais.agilemonkeysshop.customer.FullCustomer;
import com.brais.agilemonkeysshop.customer.LiteCustomer;
import com.brais.agilemonkeysshop.customer.entity.Customer;
import com.brais.agilemonkeysshop.customer.mapper.CustomerMongoMapper;
import com.brais.agilemonkeysshop.customer.repository.CustomerRepository;
import com.brais.agilemonkeysshop.customer.service.exception.CustomerAdapterException.ExistingCustomerWithSameIdException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CustomerMongoAdapterTest {

  private static final String CUSTOMER_ID = "1";

  private static final String NAME = "name";

  private static final String SURNAME = "surname";

  private static final String PHOTO_URL = "test";

  private static final MultipartFile PHOTO_FILE = new MockMultipartFile("photo", null, "image", new byte[]{});

  private static final String CREATED_BY = "admin";

  private static final String UPDATED_BY = "monkey";

  private static final FullCustomer FULL_CUSTOMER_TEST_1 =
      new FullCustomer(CUSTOMER_ID, NAME, SURNAME, PHOTO_FILE, null, CREATED_BY, UPDATED_BY);

  private static final LiteCustomer LITE_CUSTOMER_TEST = new LiteCustomer(CUSTOMER_ID, NAME, SURNAME);

  private static final Customer CUSTOMER = Customer.builder()
      .id(CUSTOMER_ID)
      .name(NAME)
      .surname(SURNAME)
      .photo(PHOTO_URL)
      .createdBy(CREATED_BY)
      .updatedBy(UPDATED_BY)
      .build();

  @Mock
  private CustomerRepository customerRepository;

  @Mock
  private CustomerMongoMapper customerMongoMapper;

  @InjectMocks
  private CustomerMongoAdapter customerMongoAdapter;

  @Test
  void when_callToFindAll_expect_returnListLiteCustomer() {
    when(customerRepository.findAll()).thenReturn(List.of(CUSTOMER));
    when(customerMongoMapper.toLiteCustomerList(List.of(CUSTOMER))).thenReturn(List.of(LITE_CUSTOMER_TEST));

    List<LiteCustomer> actual = customerMongoAdapter.findAll();

    assertEquals(List.of(LITE_CUSTOMER_TEST), actual);
  }

  @Test
  void when_callToFindById_expect_returnOptionalFullCustomer() {
    when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.of(CUSTOMER));
    when(customerMongoMapper.toFullCustomer(CUSTOMER)).thenReturn(FULL_CUSTOMER_TEST_1);

    Optional<FullCustomer> actual = customerMongoAdapter.findById(CUSTOMER_ID);

    assertEquals(Optional.of(FULL_CUSTOMER_TEST_1), actual);
  }

  @Test
  void when_callToCreate_expect_returnFulLCustomer() {
    when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.empty());
    when(customerMongoMapper.toCustomer(FULL_CUSTOMER_TEST_1)).thenReturn(CUSTOMER);
    when(customerMongoMapper.toFullCustomer(CUSTOMER)).thenReturn(FULL_CUSTOMER_TEST_1);
    when(customerRepository.insert(CUSTOMER)).thenReturn(CUSTOMER);

    FullCustomer actual = customerMongoAdapter.create(FULL_CUSTOMER_TEST_1);

    assertEquals(FULL_CUSTOMER_TEST_1, actual);
  }

  @Test
  void when_callToCreate_expect_returnExistingCustomerWithSameIdException() {
    when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.of(CUSTOMER));

    Mockito.verify(customerMongoMapper, never()).toCustomer(FULL_CUSTOMER_TEST_1);
    Mockito.verify(customerMongoMapper, never()).toFullCustomer(CUSTOMER);
    assertThrows(ExistingCustomerWithSameIdException.class, () -> customerMongoAdapter.create(FULL_CUSTOMER_TEST_1));
  }

  @Test
  void when_callToUpdate_expect_returnOptionalFullCustomer() {
    when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.of(CUSTOMER));
    when(customerMongoMapper.toCustomerUpdated(FULL_CUSTOMER_TEST_1, CREATED_BY)).thenReturn(CUSTOMER);
    when(customerRepository.save(CUSTOMER)).thenReturn(CUSTOMER);

    Optional<FullCustomer> actual = customerMongoAdapter.update(FULL_CUSTOMER_TEST_1);

    assertEquals(Optional.of(FULL_CUSTOMER_TEST_1), actual);
  }

  @Test
  void when_callToUpdate_expect_returnOptionalEmpty() {
    when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.empty());

    Optional<FullCustomer> actual = customerMongoAdapter.update(FULL_CUSTOMER_TEST_1);

    assertEquals(Optional.empty(), actual);
  }

  @Test
  void when_callToDelete_expect_customerRepositoryIsCalled() {
    doNothing().when(customerRepository).deleteById(CUSTOMER_ID);

    customerMongoAdapter.delete(CUSTOMER_ID);

    Mockito.verify(customerRepository).deleteById(CUSTOMER_ID);
  }
}
