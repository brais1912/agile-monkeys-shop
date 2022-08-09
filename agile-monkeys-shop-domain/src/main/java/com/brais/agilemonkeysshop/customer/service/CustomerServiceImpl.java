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
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;

@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerServicePort {

  private final CustomerPersistencePort customerPersistencePort;

  @Override
  public List<LiteCustomer> findAll() {
    return customerPersistencePort.findAll();
  }

  @Override
  public FullCustomer findById(String customerId) {
    return customerPersistencePort.findById(customerId).orElseThrow();
  }

  @Override
  public FullCustomer create(FullCustomer fullCustomer) {
    FullCustomer fullCustomerCreated = customerPersistencePort.create(fullCustomer);
    if (fullCustomerCreated != null) {
      if (fullCustomer.photoFile() != null && !Objects.requireNonNull(fullCustomer.photoFile().getOriginalFilename()).isEmpty()) {
        try {
          uploadCustomerPhoto(fullCustomer);
        } catch (IOException e) {
          e.printStackTrace();
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
    return customerPersistencePort.update(fullCustomer).orElseThrow();
  }

  @Override
  public void delete(String customerId) {
    customerPersistencePort.findById(customerId).orElseThrow();
    customerPersistencePort.delete(customerId);
  }
}
