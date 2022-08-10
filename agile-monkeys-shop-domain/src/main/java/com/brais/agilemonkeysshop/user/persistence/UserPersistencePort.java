package com.brais.agilemonkeysshop.user.persistence;

import java.util.List;
import java.util.Optional;

import com.brais.agilemonkeysshop.FullUser;

public interface UserPersistencePort {

  List<FullUser> findAll();

  Optional<FullUser> findById(String userId);

  FullUser create(FullUser fullUser);

  Optional<FullUser> update(FullUser fullUser);

  void delete(String userId);

}
