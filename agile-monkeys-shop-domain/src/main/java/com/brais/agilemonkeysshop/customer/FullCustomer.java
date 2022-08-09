package com.brais.agilemonkeysshop.customer;

import org.springframework.web.multipart.MultipartFile;

public record FullCustomer(String id,
                           String name,
                           String surname,
                           MultipartFile photoFile,
                           String photoUrl,
                           String createdBy,
                           String updatedBy) {

}