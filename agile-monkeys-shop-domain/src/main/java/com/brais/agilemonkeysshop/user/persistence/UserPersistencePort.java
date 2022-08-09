package com.brais.agilemonkeysshop.user.persistence;

import java.util.List;

import com.brais.agilemonkeysshop.FullUser;

public interface UserPersistencePort {

  List<FullUser> findAll();

}
