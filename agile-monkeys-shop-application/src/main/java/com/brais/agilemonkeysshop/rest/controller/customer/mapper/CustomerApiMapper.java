package com.brais.agilemonkeysshop.rest.controller.customer.mapper;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.agilemonkeys.shop.model.CustomerDTO;
import com.agilemonkeys.shop.model.CustomerListResponseDTO;
import com.agilemonkeys.shop.model.LiteCustomerDTO;
import com.brais.agilemonkeysshop.customer.FullCustomer;
import com.brais.agilemonkeysshop.customer.LiteCustomer;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface CustomerApiMapper {

  default CustomerListResponseDTO toCustomerListResponseDTO(List<LiteCustomer> liteCustomerList) {
    List<LiteCustomerDTO> liteCustomerDTOList = CollectionUtils.emptyIfNull(liteCustomerList)
        .stream()
        .map(this::toLiteCustomerDTO)
        .toList();
    return new CustomerListResponseDTO().customerList(liteCustomerDTOList);
  }

  @Mapping(target = "photo", source = "photoUrl")
  CustomerDTO toCustomerDTO(FullCustomer fullCustomer);

  LiteCustomerDTO toLiteCustomerDTO(LiteCustomer fullCustomer);

  default FullCustomer toFullCustomer(String id, String name, String surname, MultipartFile photo, String photoUrl, String createdBy,
      String updatedBy) {
    String completePhotoUrl = (photoUrl != null && !photoUrl.isEmpty())
        ? buildPhotoUrl(photoUrl, id, photo.getOriginalFilename())
        : null;
    return new FullCustomer(id, name, surname, photo, completePhotoUrl, createdBy, updatedBy);
  }

  default FullCustomer toFullCustomer(String id, String name, String surname, MultipartFile photo, String photoUrl, String updatedBy) {
    return new FullCustomer(id, name, surname, photo, getCompletePhotoUrl(id, photo, photoUrl), null, updatedBy);
  }

  private String getCompletePhotoUrl(String id, MultipartFile photo, String photoUrl) {
    return photoUrl != null && !photoUrl.isEmpty()
        ? buildPhotoUrl(photoUrl, id, photo.getOriginalFilename())
        : null;
  }

  default String buildPhotoUrl(String photoUrl, String id, String originalFileName) {
    return photoUrl + id + "." + FilenameUtils.getExtension(originalFileName);
  }
}
