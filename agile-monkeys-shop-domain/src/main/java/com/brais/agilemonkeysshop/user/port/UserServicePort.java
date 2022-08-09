package com.brais.agilemonkeysshop.user.port;

import java.util.List;

import com.brais.agilemonkeysshop.FullUser;

public interface UserServicePort {

  List<FullUser> findAll();

}
