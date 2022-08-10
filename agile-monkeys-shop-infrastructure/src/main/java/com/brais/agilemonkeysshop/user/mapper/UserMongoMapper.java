package com.brais.agilemonkeysshop.user.mapper;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.brais.agilemonkeysshop.FullUser;
import com.brais.agilemonkeysshop.user.UserStatusEnum;
import com.brais.agilemonkeysshop.user.UserTypeEnum;
import com.brais.agilemonkeysshop.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserMongoMapper {

  BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

  List<FullUser> toFullUserList(List<User> userList);

  default UserStatusEnum toUserStatusEnum(String userStatus) {
    return Optional.ofNullable(userStatus).map(UserStatusEnum::valueOf).orElse(UserStatusEnum.UNKNOWN);
  }

  default UserTypeEnum toUserTypeEnum(String userType) {
    return Optional.ofNullable(userType).map(UserTypeEnum::valueOf).orElse(UserTypeEnum.UNDEFINED);
  }

  FullUser toFullUser(User user);

  @Mapping(target = "password", expression = "java(bCryptPasswordEncoder.encode(fullUser.password()))")
  User toUser(FullUser fullUser);
}
