package com.ironhack.banking_system.services;

import com.ironhack.banking_system.controllers.requests.accounts.CreateCheckingRequest;
import com.ironhack.banking_system.data.AccountRepository;
import com.ironhack.banking_system.data.UserRepository;
import com.ironhack.banking_system.data.model.Address;
import com.ironhack.banking_system.data.model.Money;
import com.ironhack.banking_system.data.model.accounts.Account;
import com.ironhack.banking_system.data.model.accounts.AccountStatus;
import com.ironhack.banking_system.data.model.accounts.StudentCheckingAccount;
import com.ironhack.banking_system.data.model.users.AccountHolder;
import com.ironhack.banking_system.data.model.users.Role;
import com.ironhack.banking_system.data.model.users.User;
import com.ironhack.banking_system.services.impl.AccountServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    public void get_all_accounts_should_return_all() throws Exception {
        // given
        User accountHolder = new AccountHolder("Peter", new HashSet<>(), LocalDate.of(1980, 6, 4), new Address("10 Downing Street", "London", "UK","1234512"), "11 Downing Street", "123456");
        User admin = new AccountHolder("Peter", new HashSet<>(Collections.singletonList(new Role("ADMIN"))), LocalDate.of(1980, 6, 4), new Address("10 Downing Street", "London", "UK","1234512"), "11 Downing Street", "123456");
        Account account = new StudentCheckingAccount(
                new Money(new BigDecimal("123"), Currency.getInstance("USD")),
                accountHolder,
                null,
                AccountStatus.ACTIVE,
                "secret-key-123"
        );
        Account account2 = new StudentCheckingAccount(
                new Money(new BigDecimal("123"), Currency.getInstance("USD")),
                admin,
                null,
                AccountStatus.ACTIVE,
                "secret-key-123"
        );
        List<Account> accounts = new ArrayList<>();
        accounts.add(account);
        accounts.add(account2);

        when(accountRepository.findAll()).thenReturn(accounts);

        // when
        List <Account> resultAccount = accountService.getAccounts(admin);
        // then
        assertEquals(resultAccount, accounts);
    }

    @Test
    public void get_account_should_return_one_account() throws Exception {
        // given
        User accountHolder = new AccountHolder("Peter", new HashSet<>(), LocalDate.of(1980, 6, 4), new Address("10 Downing Street", "London", "UK","1234512"), "11 Downing Street", "123456");
        User admin = new AccountHolder("Peter", new HashSet<>(Collections.singletonList(new Role("ADMIN"))), LocalDate.of(1980, 6, 4), new Address("10 Downing Street", "London", "UK","1234512"), "11 Downing Street", "123456");
        Account account = new StudentCheckingAccount(
                new Money(new BigDecimal("123"), Currency.getInstance("USD")),
                accountHolder,
                null,
                AccountStatus.ACTIVE,
                "secret-key-123"
        );
        Account account2 = new StudentCheckingAccount(
                new Money(new BigDecimal("123"), Currency.getInstance("USD")),
                admin,
                null,
                AccountStatus.ACTIVE,
                "secret-key-123"
        );

        when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));
        when(accountRepository.findByIdAndPrimaryOwner(account.getId(), accountHolder)).thenReturn(Optional.of(account));

        assertEquals(accountService.getAccount(accountHolder, account.getId()), Optional.of(account));
        assertEquals(accountService.getAccount(admin, account.getId()), Optional.of(account));
    }

    @Test
    public void create_new_account_should_return_ok() throws Exception {
        // given
        User accountHolder = new AccountHolder("Peter", new HashSet<>(), LocalDate.of(1980, 6, 4), new Address("10 Downing Street", "London", "UK","1234512"), "11 Downing Street", "123456");
        Account account = new StudentCheckingAccount(
                new Money(new BigDecimal("123"), Currency.getInstance("USD")),
                accountHolder,
                null,
                AccountStatus.ACTIVE,
                "secret-key-123"
        );

        when(userRepository.findById(accountHolder.getId())).thenReturn(Optional.of(accountHolder));
        when(accountRepository.save(any())).thenReturn(account);

        // when
        Account actualAccount = accountService.createAccount(new CreateCheckingRequest(account.getBalance(), account.getPrimaryOwner().getId(), null, account.getStatus().toString(), "12453",  new Money(new BigDecimal("120")), new Money(new BigDecimal("12"))));
        // then
        assertEquals(actualAccount, account);
    }
}
