package com.brais.agilemonkeysshop.user.port;

import java.util.List;

import com.brais.agilemonkeysshop.FullUser;

public interface UserServicePort {

  List<FullUser> findAll();

  FullUser create(FullUser fullUser);

  FullUser update(FullUser fullUser);

  void delete(String userId);
}
