package com.brais.agilemonkeysshop.user.mapper;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.brais.agilemonkeysshop.FullUser;
import com.brais.agilemonkeysshop.LiteUser;
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
    return UserStatusEnum.fromValue(userStatus);
  }

  default UserTypeEnum toUserTypeEnum(String userType) {
    return UserTypeEnum.fromValue(userType);
  }

  FullUser toFullUser(User user);

  LiteUser toLiteUser(User user);

  @Mapping(target = "password", expression = "java(bCryptPasswordEncoder.encode(fullUser.password()))")
  @Mapping(target = "userType",
      expression = "java(fullUser.userType() != null ? fullUser.userType().getValue() : UserTypeEnum.UNDEFINED.getValue())")
  @Mapping(target = "userStatus",
      expression = "java(fullUser.userStatus() != null ? fullUser.userStatus().getValue() : UserStatusEnum.UNKNOWN.getValue())")
  User toUser(FullUser fullUser);
}
