package com.ironhack.banking_system.services.interfaces;

import com.ironhack.banking_system.data.model.users.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
  List<User> getUsers();

  Optional<User> getUser(long userId);

  User createUser(User user, User principal) throws Exception;
}
