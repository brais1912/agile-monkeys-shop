package com.brais.agilemonkeysshop.customer.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.brais.agilemonkeysshop.customer.FullCustomer;
import com.brais.agilemonkeysshop.customer.LiteCustomer;
import com.brais.agilemonkeysshop.customer.persistence.CustomerPersistencePort;
import com.brais.agilemonkeysshop.customer.service.exception.CustomerServiceException.CustomerNotFoundServiceException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

  private static final String CUSTOMER_ID = "1";

  private static final String NAME = "name";

  private static final String SURNAME = "surname";

  private static final MultipartFile PHOTO_FILE = new MockMultipartFile("photo", null, "image", new byte[]{});

  private static final String CREATED_BY = "admin";

  private static final String UPDATED_BY = "monkey";

  private static final FullCustomer FULL_CUSTOMER_TEST_1 =
      new FullCustomer(CUSTOMER_ID, NAME, SURNAME, PHOTO_FILE, null, CREATED_BY, UPDATED_BY);

  private static final LiteCustomer LITE_CUSTOMER_TEST = new LiteCustomer(CUSTOMER_ID, NAME, SURNAME);

  @Mock
  private CustomerPersistencePort customerPersistencePort;

  @InjectMocks
  private CustomerServiceImpl customerService;

  @Nested
  class FindAll {

    @ParameterizedTest
    @MethodSource("fromFindAllDataSet")
    void when_callFindAll_expect_returnLiteCustomerList(List<LiteCustomer> testLiteCustomer, List<LiteCustomer> expected) {
      when(customerPersistencePort.findAll()).thenReturn(testLiteCustomer);

      List<LiteCustomer> actual = customerService.findAll();

      Mockito.verify(customerPersistencePort).findAll();
      assertEquals(expected, actual);
    }

    static Stream<Arguments> fromFindAllDataSet() {
      return Stream.of(
          arguments(List.of(), List.of()),
          arguments(List.of(LITE_CUSTOMER_TEST), List.of(LITE_CUSTOMER_TEST)),
          arguments(null, null));
    }
  }

  @Nested
  class FindById {

    @Test
    void when_callFindById_expect_returnFullCustomer() {
      when(customerPersistencePort.findById(CUSTOMER_ID)).thenReturn(Optional.of(FULL_CUSTOMER_TEST_1));

      FullCustomer actual = customerService.findById(CUSTOMER_ID);

      Mockito.verify(customerPersistencePort).findById(CUSTOMER_ID);
      assertEquals(FULL_CUSTOMER_TEST_1, actual);
    }

    @Test
    void when_callFindById_expect_customerNotFoundExceptionThrown() {
      when(customerPersistencePort.findById(CUSTOMER_ID)).thenReturn(Optional.empty());

      assertThrows(CustomerNotFoundServiceException.class, () -> customerService.findById(CUSTOMER_ID));

      Mockito.verify(customerPersistencePort).findById(CUSTOMER_ID);
    }
  }

  @Nested
  class Create {

    @Test
    void when_callCreate_expect_returnFullCustomer() {
      when(customerPersistencePort.create(FULL_CUSTOMER_TEST_1)).thenReturn(FULL_CUSTOMER_TEST_1);

      FullCustomer actual = customerService.create(FULL_CUSTOMER_TEST_1);

      assertEquals(FULL_CUSTOMER_TEST_1, actual);
    }

    @Test
    void when_callCreate_expect_returnVoid() {
      when(customerPersistencePort.create(FULL_CUSTOMER_TEST_1)).thenReturn(null);

      FullCustomer actual = customerService.create(FULL_CUSTOMER_TEST_1);

      assertNull(actual);
    }
  }

  @Nested
  class Update {

    @Test
    void when_callUpdate_expect_callReturnFullCustomer() {
      when(customerPersistencePort.update(FULL_CUSTOMER_TEST_1)).thenReturn(Optional.of(FULL_CUSTOMER_TEST_1));

      FullCustomer actual = customerService.update(FULL_CUSTOMER_TEST_1);

      Mockito.verify(customerPersistencePort).update(FULL_CUSTOMER_TEST_1);
      assertEquals(FULL_CUSTOMER_TEST_1, actual);
    }

    @Test
    void when_callCreate_expect_callReturnOptionalEmpty_customerNotFoundExceptionThrown() {
      when(customerPersistencePort.update(FULL_CUSTOMER_TEST_1)).thenReturn(Optional.empty());

      assertThrows(CustomerNotFoundServiceException.class, () -> customerService.update(FULL_CUSTOMER_TEST_1));
      Mockito.verify(customerPersistencePort).update(FULL_CUSTOMER_TEST_1);
    }
  }

  @Nested
  class Delete {

    @Test
    void when_callDelete_expect_callNoExceptionThrown() {
      when(customerPersistencePort.findById(CUSTOMER_ID)).thenReturn(Optional.of(FULL_CUSTOMER_TEST_1));
      doNothing().when(customerPersistencePort).delete(CUSTOMER_ID);

      customerService.delete(CUSTOMER_ID);

      Mockito.verify(customerPersistencePort).findById(CUSTOMER_ID);
      Mockito.verify(customerPersistencePort).delete(CUSTOMER_ID);
    }

    @Test
    void when_callDelete_expect_callExceptionThrown() {
      when(customerPersistencePort.findById(CUSTOMER_ID)).thenReturn(Optional.empty());

      assertThrows(CustomerNotFoundServiceException.class, () -> customerService.delete(CUSTOMER_ID));
      Mockito.verify(customerPersistencePort).findById(CUSTOMER_ID);
      Mockito.verify(customerPersistencePort, never()).delete(CUSTOMER_ID);
    }
  }
}
