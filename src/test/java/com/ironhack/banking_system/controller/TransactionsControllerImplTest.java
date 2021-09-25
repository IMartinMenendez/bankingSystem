package com.ironhack.banking_system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.banking_system.controllers.requests.accounts.CreateCreditCardRequest;
import com.ironhack.banking_system.controllers.requests.transactions.CreateAccountHolderTransactionRequest;
import com.ironhack.banking_system.data.AccountRepository;
import com.ironhack.banking_system.data.TransactionRepository;
import com.ironhack.banking_system.data.UserRepository;
import com.ironhack.banking_system.data.model.Address;
import com.ironhack.banking_system.data.model.Money;
import com.ironhack.banking_system.data.model.accounts.Account;
import com.ironhack.banking_system.data.model.accounts.AccountStatus;
import com.ironhack.banking_system.data.model.accounts.CheckingAccount;
import com.ironhack.banking_system.data.model.transactions.AccountHolderTransaction;
import com.ironhack.banking_system.data.model.users.AccountHolder;
import com.ironhack.banking_system.data.model.users.Role;
import com.ironhack.banking_system.data.model.users.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class TransactionsControllerImplTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    LocalDate localDate = LocalDate.of(1980, 6, 4);

    private final Currency currency = Currency.getInstance("USD");
    private final User primaryOwner = new AccountHolder("user2", Set.of(new Role("ADMIN")), LocalDate.of(1980, 6, 4), new Address("10 Downing Street", "London", "UK", "1234512"), "11 Downing Street", "123456");
    private final User primaryOwner2 = new AccountHolder("user3", Set.of(new Role("ADMIN")), LocalDate.of(1980, 6, 4), new Address("10 Downing Street", "London", "UK", "1234512"), "11 Downing Street", "123456");
    private final Money creditLimit = new Money(new BigDecimal("100"), currency);
    private final BigDecimal interestRate = new BigDecimal("0.1");
    private final User secondaryOwner = null;
    private final AccountStatus status = AccountStatus.ACTIVE;
    private final String secretKey = "secret-key";
    private final Money minimumBalance = new Money(new BigDecimal("100"), currency);
    private final Money monthlyMaintenanceFee = new Money(new BigDecimal("20"), currency);
    Money balance = new Money(new BigDecimal("99"), currency);

    Account account = new CheckingAccount(balance, primaryOwner, secondaryOwner, status, secretKey, minimumBalance, monthlyMaintenanceFee);
    Account account2 = new CheckingAccount(balance, primaryOwner, secondaryOwner, status, secretKey, minimumBalance, monthlyMaintenanceFee);

    AccountHolderTransaction transaction = new AccountHolderTransaction(new Money(new BigDecimal(200)), account, account2, primaryOwner);


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
        primaryOwner.setPassword(passwordEncoder.encode(primaryOwner.getPassword()));
        userRepository.save(primaryOwner);
        userRepository.save(primaryOwner2);
        accountRepository.save(account);
        accountRepository.save(account2);
    }

    @AfterEach
    void tearDown() {
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void admin_can_do_transfers_between_accounts() throws Exception {
        CreateAccountHolderTransactionRequest transaction = new CreateAccountHolderTransactionRequest(new Money(new BigDecimal(99)), account.getId(), account2.getId(), null);
        String post = objectMapper.writeValueAsString(transaction);
        mockMvc.perform(
                post("/transactions").with(httpBasic("user2", "123456"))
                        .content(post)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(accountRepository.findById(account.getId()).get().getBalance().getAmount(), new BigDecimal("0.00"));
        assertEquals(accountRepository.findById(account2.getId()).get().getBalance().getAmount(), new BigDecimal("198.00"));
    }

    @Test
    void if_balance_is_lower_than_transaction_should_return_error() throws Exception {
        Exception exception = assertThrows(Exception.class, () -> {
            CreateAccountHolderTransactionRequest transaction = new CreateAccountHolderTransactionRequest(new Money(new BigDecimal(200)), account.getId(), account2.getId(), null);
            String post = objectMapper.writeValueAsString(transaction);
            mockMvc.perform(
                    post("/transactions").with(httpBasic("user2", "123456"))
                            .content(post)
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding("UTF-8")
            )
                    .andReturn();
        });

        String expectedMessage = "Insufficient funds";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void cannot_transfer_if_no_own_account() throws Exception {
        CreateAccountHolderTransactionRequest transaction = new CreateAccountHolderTransactionRequest(new Money(new BigDecimal(99)), account.getId(), account2.getId(), null);
        String post = objectMapper.writeValueAsString(transaction);
        mockMvc.perform(
                post("/transactions").with(httpBasic(account2.getPrimaryOwner().toString(), "123456"))
                        .content(post)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        )
                .andExpect(status().isUnauthorized())
                .andReturn();

    }

    @Test
    void cannot_transfer_to_credit_card_account() throws Exception {
        CreateCreditCardRequest account = new CreateCreditCardRequest(balance, primaryOwner.getId(), null, status.toString(), creditLimit, interestRate);
        CreateAccountHolderTransactionRequest transaction = new CreateAccountHolderTransactionRequest(new Money(new BigDecimal(99)), account.getPrimaryOwnerId(), account2.getId(), null);
        String post = objectMapper.writeValueAsString(transaction);
        mockMvc.perform(
                post("/transactions").with(httpBasic(account2.getPrimaryOwner().toString(), "123456"))
                        .content(post)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        )
                .andExpect(status().isUnauthorized())
                .andReturn();

    }


}
