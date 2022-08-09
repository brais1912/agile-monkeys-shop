package com.brais.agilemonkeysshop.user.mapper;

import java.util.List;

import com.brais.agilemonkeysshop.FullUser;
import com.brais.agilemonkeysshop.user.UserStatusEnum;
import com.brais.agilemonkeysshop.user.UserTypeEnum;
import com.brais.agilemonkeysshop.user.entity.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMongoMapper {

  List<FullUser> toFullUserList(List<User> userList);

  default UserStatusEnum toUserStatusEnum(String userStatus) {
    return UserStatusEnum.valueOf(userStatus.toUpperCase());
  }

  default UserTypeEnum toUserTypeEnum(String userType) {
    return UserTypeEnum.valueOf(userType.toUpperCase());
  }

  FullUser toFullUser(User user);
}
