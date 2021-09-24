package com.ironhack.banking_system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.banking_system.controllers.requests.accounts.CreateCheckingRequest;
import com.ironhack.banking_system.data.AccountRepository;
import com.ironhack.banking_system.data.UserRepository;
import com.ironhack.banking_system.data.model.Address;
import com.ironhack.banking_system.data.model.Money;
import com.ironhack.banking_system.data.model.accounts.Account;
import com.ironhack.banking_system.data.model.accounts.AccountStatus;
import com.ironhack.banking_system.data.model.accounts.CheckingAccount;
import com.ironhack.banking_system.data.model.users.AccountHolder;
import com.ironhack.banking_system.data.model.users.Admin;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class AccountControllerImplTest{

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

   LocalDate localDate = LocalDate.of(1980, 6, 4);

    private final Currency currency = Currency.getInstance("USD");
    private final User primaryOwner = new AccountHolder("user2", Set.of(new Role("ADMIN")), LocalDate.of(1980, 6, 4), new Address("10 Downing Street", "London", "UK","1234512"), "11 Downing Street", "123456");
    private final User secondaryOwner = null;
    private final AccountStatus status = AccountStatus.ACTIVE;
    private final String secretKey = "secret-key";
    private final Money minimumBalance = new Money(new BigDecimal("100"), currency);
    private final Money monthlyMaintenanceFee = new Money(new BigDecimal("20"), currency);
    Money balance = new Money(new BigDecimal("99"), currency);

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @AfterEach
    void tearDown() {
        accountRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void access_to_all_accounts_with_admin_identification_should_return_ok() throws Exception {
        User admin = new Admin("admin", new HashSet<>(Collections.singletonList(new Role("ADMIN"))), passwordEncoder.encode("123456"));
        userRepository.save(admin);
        mockMvc.perform(get("/accounts").with(httpBasic("admin", "123456"))).andExpect(status().isOk());
    }

    @Test
    void access_to_all_accounts_without_User_identification_should_return_unauthorized() throws Exception {
        User admin = new Admin("admin", new HashSet<>(Collections.singletonList(new Role("ADMIN"))), passwordEncoder.encode("123456"));
        userRepository.save(admin);
        mockMvc.perform(get("/accounts").with(httpBasic("user", "123456"))).andExpect(status().isUnauthorized());
    }

    @Test
    void access_just_to_own_account_should_return_ok() throws Exception {
        User accountHolder = new AccountHolder("user2", new HashSet<>(Collections.singletonList(new Role("ACCOUNTHOLDER"))), LocalDate.of(1980, 6, 4), new Address("10 Downing Street", "London", "UK","1234512"), "11 Downing Street", passwordEncoder.encode("123456"));
        userRepository.save(accountHolder);
        Account account = new CheckingAccount(balance, accountHolder, secondaryOwner, status, secretKey, minimumBalance, monthlyMaintenanceFee);
        Account savedAccount = accountRepository.save(account);
        mockMvc.perform(get("/accounts/" + savedAccount.getId()).with(httpBasic("user2", "123456"))).andExpect(status().isOk());
    }

    @Test
    void not_access_to_others_accounts_should_return_unauthorized() throws Exception {
        User accountHolder = new AccountHolder("user2", new HashSet<>(Collections.singletonList(new Role("ACCOUNTHOLDER"))), LocalDate.of(1980, 6, 4), new Address("10 Downing Street", "London", "UK","1234512"), "11 Downing Street", "123456");
        userRepository.save(accountHolder);
        mockMvc.perform(get("/accounts/2").with(httpBasic("user2", "123456"))).andExpect(status().isUnauthorized());
    }


    @Test
    void admin_can_create_new_accounts() throws Exception {
        User accountHolder = new AccountHolder("user2", new HashSet<>(Collections.singletonList(new Role("ADMIN"))), localDate, new Address("10 Downing Street", "London", "UK","1234512"), "11 Downing Street", passwordEncoder.encode("123456"));
        userRepository.save(accountHolder);
        CreateCheckingRequest account = new CreateCheckingRequest(balance, accountHolder.getId(), null, status.toString(), secretKey, minimumBalance, monthlyMaintenanceFee);
        String post = objectMapper.writeValueAsString(account);
        MvcResult mvcResult = mockMvc.perform(
                post("/accounts").with(httpBasic("user2", "123456"))
                        .content(post)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8).contains("checking"));
        assertTrue(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8).contains("99"));
    }

    @Test
    void accountHolder_cannot_create_new_accounts() throws Exception {
        User accountHolder = new AccountHolder("user2", new HashSet<>(Collections.singletonList(new Role("ACCOUNTHOLDER"))), localDate, new Address("10 Downing Street", "London", "UK","1234512"), "11 Downing Street", passwordEncoder.encode("123456"));
        userRepository.save(accountHolder);
        CreateCheckingRequest account = new CreateCheckingRequest(balance, accountHolder.getId(), null, status.toString(), secretKey, minimumBalance, monthlyMaintenanceFee);
        String post = objectMapper.writeValueAsString(account);
        MvcResult mvcResult = mockMvc.perform(
                post("/accounts").with(httpBasic("user2", "123456"))
                        .content(post)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        )
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    void if_user_is_younger_than_24_checking_should_be_student_account() throws Exception {
        LocalDate localDate = LocalDate.of(2006, 6, 4);
        User accountHolder = new AccountHolder("user2", new HashSet<>(Collections.singletonList(new Role("ADMIN"))), localDate, new Address("10 Downing Street", "London", "UK","1234512"), "11 Downing Street", passwordEncoder.encode("123456"));
        userRepository.save(accountHolder);
        CreateCheckingRequest account = new CreateCheckingRequest(balance, accountHolder.getId(), null, status.toString(), secretKey, minimumBalance, monthlyMaintenanceFee);
        String post = objectMapper.writeValueAsString(account);
        MvcResult mvcResult = mockMvc.perform(
                post("/accounts").with(httpBasic("user2", "123456"))
                        .content(post)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8).contains("student"));
        assertTrue(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8).contains("99"));
    }

    @Test
    void if_user_is_higher_than_24_checking_should_be_checking_account() throws Exception {
        LocalDate localDate = LocalDate.of(1886, 6, 4);
        User accountHolder = new AccountHolder("user2", new HashSet<>(Collections.singletonList(new Role("ADMIN"))), localDate, new Address("10 Downing Street", "London", "UK","1234512"), "11 Downing Street", passwordEncoder.encode("123456"));
        userRepository.save(accountHolder);
        CreateCheckingRequest account = new CreateCheckingRequest(balance, accountHolder.getId(), null, status.toString(), secretKey, minimumBalance, monthlyMaintenanceFee);
        String post = objectMapper.writeValueAsString(account);
        MvcResult mvcResult = mockMvc.perform(
                post("/accounts").with(httpBasic("user2", "123456"))
                        .content(post)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8).contains("checking"));
        assertTrue(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8).contains("99"));
    }




}
