package com.brais.agilemonkeysshop.user.mapper;

import java.util.List;
import java.util.Optional;

import com.brais.agilemonkeysshop.FullUser;
import com.brais.agilemonkeysshop.user.UserStatusEnum;
import com.brais.agilemonkeysshop.user.UserTypeEnum;
import com.brais.agilemonkeysshop.user.entity.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMongoMapper {

  List<FullUser> toFullUserList(List<User> userList);

  default UserStatusEnum toUserStatusEnum(String userStatus) {
    return Optional.ofNullable(userStatus).map(UserStatusEnum::valueOf).orElse(UserStatusEnum.UNKNOWN);
  }

  default UserTypeEnum toUserTypeEnum(String userType) {
    return Optional.ofNullable(userType).map(UserTypeEnum::valueOf).orElse(UserTypeEnum.UNDEFINED);
  }

  FullUser toFullUser(User user);

  User toUser(FullUser fullUser);
}
