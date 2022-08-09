package com.brais.agilemonkeysshop.customer;

import org.springframework.web.multipart.MultipartFile;

public record LiteCustomer(String id,
                          String name,
                          String surname) {

}
