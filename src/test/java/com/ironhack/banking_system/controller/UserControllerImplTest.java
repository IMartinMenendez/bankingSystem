package com.ironhack.banking_system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ironhack.banking_system.controllers.requests.users.CreateAccountHolderRequest;
import com.ironhack.banking_system.data.UserRepository;
import com.ironhack.banking_system.data.model.Address;
import com.ironhack.banking_system.data.model.Money;
import com.ironhack.banking_system.data.model.accounts.AccountStatus;
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
import java.time.LocalDate;
import java.util.Collections;
import java.util.Currency;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class UserControllerImplTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    LocalDate localDate = LocalDate.of(1980, 6, 4);

    private final Currency currency = Currency.getInstance("USD");
    private final User primaryOwner = new AccountHolder("user2", Set.of(new Role("ADMIN")), LocalDate.of(1980, 6, 4), new Address("10 Downing Street", "London", "UK", "1234512"), "11 Downing Street", "123456");
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
        userRepository.deleteAll();
    }

    @Test
    void access_to_all_users_with_admin_identification_should_return_ok() throws Exception {
        User admin = new Admin("admin", new HashSet<>(Collections.singletonList(new Role("ADMIN"))), passwordEncoder.encode("123456"));
        User savedUsed = userRepository.save(admin);
        mockMvc.perform(get("/users").with(httpBasic("admin", "123456"))).andExpect(status().isOk());
    }

    @Test
    void access_to_a_user_with_admin_identification_should_return_ok() throws Exception {
        User admin = new Admin("admin", new HashSet<>(Collections.singletonList(new Role("ADMIN"))), passwordEncoder.encode("123456"));
        User savedUsed = userRepository.save(admin);
        mockMvc.perform(get("/users/" + savedUsed.getId()).with(httpBasic("admin", "123456"))).andExpect(status().isOk());
    }

    @Test
    void admin_can_create_new_user() throws Exception {
        User admin = new Admin("user2", new HashSet<>(Collections.singletonList(new Role("ADMIN"))), passwordEncoder.encode("123456"));
        User savedUsed = userRepository.save(admin);
        CreateAccountHolderRequest accountHolder = new CreateAccountHolderRequest("user9", Set.of("ADMIN"), LocalDate.of(1980, 6, 4), new Address("10 Downing Street", "London", "UK", "1234512"), "11 Downing Street", passwordEncoder.encode("123456"));
        String post = objectMapper.writeValueAsString(accountHolder);
        MvcResult mvcResult = mockMvc.perform(
                post("/users").with(httpBasic("user2", "123456"))
                        .content(post)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    @Test
    void user_cannot_create_new_user() throws Exception {
        CreateAccountHolderRequest accountHolder = new CreateAccountHolderRequest("user2", Set.of("ACCOUNTHOLDER"), LocalDate.of(1980, 6, 4), new Address("10 Downing Street", "London", "UK", "1234512"), "11 Downing Street", "123456");
        String post = objectMapper.writeValueAsString(accountHolder);
        MvcResult mvcResult = mockMvc.perform(
                post("/users").with(httpBasic("user2", "123456"))
                        .content(post)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        )
                .andExpect(status().isUnauthorized())
                .andReturn();
    }


}
