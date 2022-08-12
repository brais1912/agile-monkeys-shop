package com.brais.agilemonkeysshop.customer.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import com.brais.agilemonkeysshop.customer.FullCustomer;
import com.brais.agilemonkeysshop.customer.LiteCustomer;
import com.brais.agilemonkeysshop.customer.persistence.CustomerPersistencePort;
import com.brais.agilemonkeysshop.customer.port.CustomerServicePort;
import com.brais.agilemonkeysshop.customer.service.exception.CustomerServiceException.CustomerNotFoundServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;

@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerServicePort {

  private static final String LOGGER_PREFIX = "[CUSTOMER SERVICE] ";

  private final CustomerPersistencePort customerPersistencePort;

  @Override
  public List<LiteCustomer> findAll() {
    return customerPersistencePort.findAll();
  }

  @Override
  public FullCustomer findById(String customerId) {
    return customerPersistencePort.findById(customerId)
        .orElseThrow(() -> new CustomerNotFoundServiceException("Customer " + customerId + " not found"));
  }

  @Override
  public FullCustomer create(FullCustomer fullCustomer) {
    FullCustomer fullCustomerCreated = customerPersistencePort.create(fullCustomer);
    if (fullCustomerCreated != null) {
      if (fullCustomer.photoFile() != null && !Objects.requireNonNull(fullCustomer.photoFile().getOriginalFilename()).isEmpty()) {
        try {
          uploadCustomerPhoto(fullCustomer);
        } catch (IOException e) {
          log.error(LOGGER_PREFIX + "Error when uploading the customer's photo. The client's photo could not be uploaded: {}",
              e.getMessage(), e);
        }
      }
    }

    return fullCustomerCreated;
  }

  private void uploadCustomerPhoto(FullCustomer fullCustomer) throws IOException {
    String directory = FilenameUtils.getFullPath(fullCustomer.photoUrl());
    Path directoryPath = Paths.get(directory);
    Files.createDirectories(directoryPath);

    byte[] bytes = fullCustomer.photoFile().getBytes();
    Path path = Paths.get(fullCustomer.photoUrl());

    Files.write(path, bytes);
  }

  @Override
  public FullCustomer update(FullCustomer fullCustomer) {
    return customerPersistencePort.update(fullCustomer)
        .orElseThrow(() -> new CustomerNotFoundServiceException("Customer " + fullCustomer.id() + " not found"));
  }

  @Override
  public void delete(String customerId) {
    FullCustomer fullCustomerToDelete = findById(customerId);
    customerPersistencePort.delete(customerId);

    try {
      if (fullCustomerToDelete.photoUrl() != null && !fullCustomerToDelete.photoUrl().isEmpty()) {
        deleteCustomerPhoto(fullCustomerToDelete.photoUrl());
      }
    } catch (IOException e) {
      log.error(LOGGER_PREFIX + "Error when deleting the customer's photo. The client's photo could not be deleted: {}",
          e.getMessage(), e);
    }
  }

  private void deleteCustomerPhoto(String photoUrl) throws IOException {
    Path pathPhotoUrl = Paths.get(photoUrl);
    Files.delete(pathPhotoUrl);
  }
}
