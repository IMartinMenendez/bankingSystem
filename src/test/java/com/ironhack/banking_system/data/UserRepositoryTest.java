package com.ironhack.banking_system.data;

import com.ironhack.banking_system.data.model.users.Admin;
import com.ironhack.banking_system.data.model.users.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private UserRepository userRepository;

  @Test
  public void findByName_returnsAUser() {
    User admin = new Admin("Mark", new HashSet<>(),"123456");
    entityManager.persist(admin);
    entityManager.flush();

    Optional<User> found = userRepository.findByName(admin.getName());

    assertThat(found.isPresent(), is(true));
    assertThat(found.get().getName(), is(admin.getName()));
    assertThat(found.get().getPassword(), is(admin.getPassword()));
  }

  @Test
  public void findByName_returnsNothing() {
    User admin = new Admin("Mark", new HashSet<>(),"123456");
    entityManager.persist(admin);
    entityManager.flush();

    Optional<User> found = userRepository.findByName(admin.getName() + "NOT_FOUND");

    assertThat(found.isPresent(), is(false));
  }

}
