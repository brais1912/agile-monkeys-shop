package com.brais.agilemonkeysshop.user.persistence;

import java.util.List;
import java.util.Optional;

import com.brais.agilemonkeysshop.FullUser;
import com.brais.agilemonkeysshop.LiteUser;

public interface UserPersistencePort {

  List<FullUser> findAll();

  Optional<LiteUser> findById(String userId);

  Optional<LiteUser> findByUsername(String username);

  FullUser create(FullUser fullUser);

  Optional<FullUser> update(FullUser fullUser);

  void delete(String userId);

}
