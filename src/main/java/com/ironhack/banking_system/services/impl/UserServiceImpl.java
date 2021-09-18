package com.ironhack.banking_system.services.impl;

import com.ironhack.banking_system.data.UserRepository;
import com.ironhack.banking_system.data.model.users.ThirdPartyUser;
import com.ironhack.banking_system.data.model.users.User;
import com.ironhack.banking_system.services.interfaces.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public List<User> getUsers() {
    return userRepository.findAll();
  }

  @Override
  public Optional<User> getUser(long userId) {
    return userRepository.findById(userId);
  }

  @Override
  public User createUser(User user, User principal) throws Exception {
    if(user instanceof ThirdPartyUser thirdPartyUser){
      if (principal.getRoles().stream().anyMatch(r -> r.getName().equals("ADMIN"))) {
        return userRepository.save(user);
      } else{
        throw new Exception("ThirdPartyUser can only be created by an Admin");
      }
    }
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return userRepository.save(user);
  }
}
