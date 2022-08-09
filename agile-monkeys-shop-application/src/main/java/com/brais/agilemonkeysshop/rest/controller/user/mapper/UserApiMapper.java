package com.brais.agilemonkeysshop.rest.controller.user.mapper;

import java.util.List;

import com.agilemonkeys.shop.model.UserDTO;
import com.agilemonkeys.shop.model.UserListResponseDTO;
import com.brais.agilemonkeysshop.FullUser;
import org.apache.commons.collections4.CollectionUtils;
import org.mapstruct.Mapper;

@Mapper
public interface UserApiMapper {

  default UserListResponseDTO toUserListResponseDTO(List<FullUser> fullUserList) {
    List<UserDTO> userDTOList = CollectionUtils.emptyIfNull(fullUserList)
        .stream()
        .map(this::toUserDTO)
        .toList();
    return new UserListResponseDTO().userList(userDTOList);
  }

  UserDTO toUserDTO(FullUser fullUser);
}
