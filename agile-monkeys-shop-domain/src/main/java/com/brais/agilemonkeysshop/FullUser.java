package com.brais.agilemonkeysshop;

import com.brais.agilemonkeysshop.user.UserStatusEnum;
import com.brais.agilemonkeysshop.user.UserTypeEnum;

public record FullUser(String id,
                       String username,
                       String password,
                       String name,
                       String surname,
                       Integer age,
                       UserTypeEnum userType,
                       UserStatusEnum userStatus) {

}
