package com.ironhack.banking_system.data;

import com.ironhack.banking_system.data.model.Money;
import com.ironhack.banking_system.data.model.accounts.Account;
import com.ironhack.banking_system.data.model.accounts.AccountStatus;
import com.ironhack.banking_system.data.model.accounts.StudentCheckingAccount;
import com.ironhack.banking_system.data.model.users.Admin;
import com.ironhack.banking_system.data.model.users.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AccountRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private AccountRepository accountRepository;

  @Test
  public void findByPrimaryOwner_returnsAnAccount() {
    User admin = new Admin("Mark", new HashSet<>(), "123456");
    Account account = new StudentCheckingAccount(
        new Money(new BigDecimal("123"), Currency.getInstance("USD")),
        admin,
        null,
        AccountStatus.ACTIVE,
        "secret-key-123"
    );
    entityManager.persist(admin);
    entityManager.persist(account);
    entityManager.flush();

    List<Account> found = accountRepository.findByPrimaryOwner(admin);

    assertThat(found.size(), is(1));
    assertThat(found.get(0), is(account));
  }

  @Test
  public void findByPrimaryOwner_returnsAnEmptyList() {
    User admin1 = new Admin("Mark", new HashSet<>(),"123456");
    User admin2 = new Admin("Rob", new HashSet<>(),"654321");
    Account account = new StudentCheckingAccount(
        new Money(new BigDecimal("123"), Currency.getInstance("USD")),
        admin2,
        null,
        AccountStatus.ACTIVE,
        "secret-key-123"
    );
    entityManager.persist(admin1);
    entityManager.persist(admin2);
    entityManager.persist(account);
    entityManager.flush();

    List<Account> found = accountRepository.findByPrimaryOwner(admin1);

    assertThat(found.size(), is(0));
  }

  @Test
  public void findByIdAndPrimaryOwner_returnsAnAccount() {
    User admin = new Admin("Mark", new HashSet<>(),"123456");
    Account account = new StudentCheckingAccount(
        new Money(new BigDecimal("123"), Currency.getInstance("USD")),
        admin,
        null,
        AccountStatus.ACTIVE,
        "secret-key-123"
    );
    entityManager.persist(admin);
    entityManager.persist(account);
    entityManager.flush();

    Optional<Account> found = accountRepository.findByIdAndPrimaryOwner(account.getId(), admin);

    assertThat(found.isPresent(), is(true));
    assertThat(found.get(), is(account));
  }

  @Test
  public void findByIdAndPrimaryOwner_returnsAnEmptyListWhenTheUserIsNotTheOwner() {
    User admin1 = new Admin("Mark", new HashSet<>(),"123456");
    User admin2 = new Admin("Rob", new HashSet<>(),"654321");
    Account account = new StudentCheckingAccount(
        new Money(new BigDecimal("123"), Currency.getInstance("USD")),
        admin2,
        null,
        AccountStatus.ACTIVE,
        "secret-key-123"
    );
    entityManager.persist(admin1);
    entityManager.persist(admin2);
    entityManager.persist(account);
    entityManager.flush();

    Optional<Account> found = accountRepository.findByIdAndPrimaryOwner(account.getId(), admin1);

    assertThat(found.isPresent(), is(false));
  }


  @Test
  public void findByIdAndPrimaryOwner_returnsAnEmptyListWhenTheAccountIdDoesNotBelongToTheUser() {
    User admin1 = new Admin("Mark",new HashSet<>(), "123456");
    User admin2 = new Admin("Rob",new HashSet<>(), "654321");
    Account account = new StudentCheckingAccount(
        new Money(new BigDecimal("123"), Currency.getInstance("USD")),
        admin2,
        null,
        AccountStatus.ACTIVE,
        "secret-key-123"
    );
    entityManager.persist(admin1);
    entityManager.persist(admin2);
    entityManager.persist(account);
    entityManager.flush();

    Optional<Account> found = accountRepository.findByIdAndPrimaryOwner(account.getId() + 100, admin2);

    assertThat(found.isPresent(), is(false));
  }

}
