package com.brais.agilemonkeysshop.rest.controller.user.mapper;

import java.util.List;

import com.agilemonkeys.shop.model.CreateUserRequestDTO;
import com.agilemonkeys.shop.model.UpdateUserRequestDTO;
import com.agilemonkeys.shop.model.UserDTO;
import com.agilemonkeys.shop.model.UserListResponseDTO;
import com.brais.agilemonkeysshop.FullUser;
import org.apache.commons.collections4.CollectionUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

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

  FullUser toFullUser(CreateUserRequestDTO createUserRequestDTO);

  @Mapping(target = "id", source = "userId")
  FullUser toFullUser(String userId, UpdateUserRequestDTO updateUserRequestDTO);
}
