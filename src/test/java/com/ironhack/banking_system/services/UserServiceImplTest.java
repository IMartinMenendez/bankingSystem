package com.ironhack.banking_system.services;

import com.ironhack.banking_system.data.UserRepository;
import com.ironhack.banking_system.data.model.Address;
import com.ironhack.banking_system.data.model.users.AccountHolder;
import com.ironhack.banking_system.data.model.users.Role;
import com.ironhack.banking_system.data.model.users.User;
import com.ironhack.banking_system.services.impl.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @InjectMocks
  private UserServiceImpl userService;

  @Test
  public void shouldCreateAndReturnAnAccountHolder() throws Exception {
    // given
    User accountHolder = new AccountHolder("Peter", new HashSet<>(), LocalDate.of(1980, 6, 4), new Address("10 Downing Street", "London", "UK","1234512"), "11 Downing Street", "123456");
    User admin = new AccountHolder("Peter", new HashSet<>(Collections.singletonList(new Role("ADMIN"))), LocalDate.of(1980, 6, 4), new Address("10 Downing Street", "London", "UK","1234512"), "11 Downing Street", "123456");

    when(passwordEncoder.encode(accountHolder.getPassword())).thenReturn(accountHolder.getPassword());
    when(userRepository.save(accountHolder)).thenReturn(accountHolder);
    // when
    User createdUser = userService.createUser(accountHolder, admin);
    // then
    assertThat(createdUser, is(accountHolder));
    verify(userRepository).save(accountHolder);
    verify(passwordEncoder).encode(accountHolder.getPassword());
  }

}
