package com.ironhack.banking_system.services;

import com.ironhack.banking_system.data.UserRepository;
import com.ironhack.banking_system.data.model.users.User;
import com.ironhack.banking_system.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;


  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> optionalUser = userRepository.findByName(username);
    if (optionalUser.isEmpty()) {
      throw new UsernameNotFoundException("User not found");
    }

    return new CustomUserDetails(optionalUser.get());
  }
}
