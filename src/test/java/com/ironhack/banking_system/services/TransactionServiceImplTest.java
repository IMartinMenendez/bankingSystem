package com.ironhack.banking_system.services;

import com.ironhack.banking_system.controllers.requests.transactions.CreateAccountHolderTransactionRequest;
import com.ironhack.banking_system.data.AccountRepository;
import com.ironhack.banking_system.data.TransactionRepository;
import com.ironhack.banking_system.data.model.Address;
import com.ironhack.banking_system.data.model.Money;
import com.ironhack.banking_system.data.model.accounts.AccountStatus;
import com.ironhack.banking_system.data.model.accounts.CreditCardAccount;
import com.ironhack.banking_system.data.model.accounts.StudentCheckingAccount;
import com.ironhack.banking_system.data.model.transactions.AccountHolderTransaction;
import com.ironhack.banking_system.data.model.transactions.Transaction;
import com.ironhack.banking_system.data.model.users.AccountHolder;
import com.ironhack.banking_system.data.model.users.ThirdPartyUser;
import com.ironhack.banking_system.data.model.users.User;
import com.ironhack.banking_system.services.impl.TransactionServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    public void should_save_transaction() throws Exception {
        // given
        User accountHolder = new AccountHolder("Peter", new HashSet<>(), LocalDate.of(1980, 6, 4), new Address("10 Downing Street", "London", "UK", "1234512"), "11 Downing Street", "123456");
        StudentCheckingAccount account1 = new StudentCheckingAccount(
                new Money(new BigDecimal("123"), Currency.getInstance("USD")),
                accountHolder,
                null,
                AccountStatus.ACTIVE,
                "secret-key-123"
        );
        StudentCheckingAccount account2 = new StudentCheckingAccount(
                new Money(new BigDecimal("123"), Currency.getInstance("USD")),
                accountHolder,
                null,
                AccountStatus.ACTIVE,
                "secret-key-123"
        );
        CreateAccountHolderTransactionRequest transactionRequest = new CreateAccountHolderTransactionRequest(new Money(new BigDecimal("100")), account1.getId(), account2.getId(), "123142");

        when(accountRepository.findById(account1.getId())).thenReturn(Optional.of(account1));
        // when
        Transaction createdTransaction = transactionService.createTransaction(accountHolder, transactionRequest, account1.getSecretKey());
        // then
        assertThat(createdTransaction, is(createdTransaction));
    }

    @Test
    public void should_return_insufficient_founds() {
        // given
        User accountHolder = new AccountHolder("Peter", new HashSet<>(), LocalDate.of(1980, 6, 4), new Address("10 Downing Street", "London", "UK", "1234512"), "11 Downing Street", "123456");
        StudentCheckingAccount account = new StudentCheckingAccount(
                new Money(new BigDecimal("123"), Currency.getInstance("USD")),
                accountHolder,
                null,
                AccountStatus.ACTIVE,
                "secret-key-123"
        );
        StudentCheckingAccount account2 = new StudentCheckingAccount(
                new Money(new BigDecimal("123"), Currency.getInstance("USD")),
                accountHolder,
                null,
                AccountStatus.ACTIVE,
                "secret-key-123"
        );
        CreateAccountHolderTransactionRequest transactionRequest = new CreateAccountHolderTransactionRequest(new Money(new BigDecimal("1000")), account.getId(), account2.getId(), "123142");

        when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));
        // when
        Exception exception = assertThrows(Exception.class, () -> {
            transactionService.createTransaction(accountHolder, transactionRequest, account.getSecretKey());
        });

        // then
        String expectedMessage = "Insufficient funds";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void should_return_fraud_detected() {
        // given
        User accountHolder = new AccountHolder("Peter", new HashSet<>(), LocalDate.of(1980, 6, 4), new Address("10 Downing Street", "London", "UK", "1234512"), "11 Downing Street", "123456");
        StudentCheckingAccount account = new StudentCheckingAccount(
                new Money(new BigDecimal("123000"), Currency.getInstance("USD")),
                accountHolder,
                null,
                AccountStatus.ACTIVE,
                "secret-key-123"
        );
        StudentCheckingAccount account2 = new StudentCheckingAccount(
                new Money(new BigDecimal("123"), Currency.getInstance("USD")),
                accountHolder,
                null,
                AccountStatus.ACTIVE,
                "secret-key-123"
        );
        CreateAccountHolderTransactionRequest transactionRequest = new CreateAccountHolderTransactionRequest(new Money(new BigDecimal("1000")), account.getId(), account2.getId(), "123142");

        when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));
        when(transactionRepository.amountBetween(any(), any(), eq(accountHolder))).thenReturn(new BigDecimal("1500"));
        when(transactionRepository.getHighestTransactionAmount(eq(accountHolder.getId()), any())).thenReturn(new BigDecimal("150"));
        // when
        Exception exception = assertThrows(Exception.class, () -> {
            transactionService.createTransaction(accountHolder, transactionRequest, account.getSecretKey());
        });

        // then
        String expectedMessage = "Fraud Alert. Transactions made in the last 24 hours exceed 150%";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void should_return_fraud_detected_two_transactions_made_in_1_second() throws Exception {
        // given
        User accountHolder = new AccountHolder("Peter", new HashSet<>(), LocalDate.of(1980, 6, 4), new Address("10 Downing Street", "London", "UK", "1234512"), "11 Downing Street", "123456");
        StudentCheckingAccount account = new StudentCheckingAccount(
                new Money(new BigDecimal("123000"), Currency.getInstance("USD")),
                accountHolder,
                null,
                AccountStatus.ACTIVE,
                "secret-key-123"
        );
        StudentCheckingAccount account2 = new StudentCheckingAccount(
                new Money(new BigDecimal("123"), Currency.getInstance("USD")),
                accountHolder,
                null,
                AccountStatus.ACTIVE,
                "secret-key-123"
        );

        AccountHolderTransaction from = new AccountHolderTransaction(new Money(new BigDecimal("1000")), account, account2, accountHolder);
        from.setId(1L);
        AccountHolderTransaction to = new AccountHolderTransaction(new Money(new BigDecimal("1000")), account, account2, accountHolder);
        to.setId(2L);
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(from);
        transactions.add(from);
        transactions.add(to);

        when(accountRepository.findById(any())).thenReturn(Optional.of(account));
        when(transactionRepository.between(any(), any(), eq(accountHolder))).thenReturn(transactions);
        when(transactionRepository.getHighestTransactionAmount(eq(accountHolder.getId()), any())).thenReturn(new BigDecimal("150"));
        // when
        Exception exception = assertThrows(Exception.class, () -> {
            transactionService.createTransaction(accountHolder, new CreateAccountHolderTransactionRequest(from.getAmount(), from.getId(), to.getId(), "12588"), account.getSecretKey());
        });

        // then
        String expectedMessage = "Fraud Alert. More than 2 transactions made in the last second";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void should_return_exception_if_account_from_is_frozen() throws Exception {
        // given
        User accountHolder = new AccountHolder("Peter", new HashSet<>(), LocalDate.of(1980, 6, 4), new Address("10 Downing Street", "London", "UK", "1234512"), "11 Downing Street", "123456");
        StudentCheckingAccount account = new StudentCheckingAccount(
                new Money(new BigDecimal("123000"), Currency.getInstance("USD")),
                accountHolder,
                null,
                AccountStatus.FROZEN,
                "secret-key-123"
        );
        StudentCheckingAccount account2 = new StudentCheckingAccount(
                new Money(new BigDecimal("123"), Currency.getInstance("USD")),
                accountHolder,
                null,
                AccountStatus.ACTIVE,
                "secret-key-123"
        );

        AccountHolderTransaction from = new AccountHolderTransaction(new Money(new BigDecimal("1000")), account, account2, accountHolder);
        from.setId(1L);
        AccountHolderTransaction to = new AccountHolderTransaction(new Money(new BigDecimal("1000")), account, account2, accountHolder);
        to.setId(2L);
        List<Transaction> transactions = new ArrayList<>();


        when(accountRepository.findById(any())).thenReturn(Optional.of(account));
        when(transactionRepository.between(any(), any(), eq(accountHolder))).thenReturn(transactions);
        when(transactionRepository.getHighestTransactionAmount(eq(accountHolder.getId()), any())).thenReturn(new BigDecimal("150"));
        // when
        Exception exception = assertThrows(Exception.class, () -> {
            transactionService.createTransaction(accountHolder, new CreateAccountHolderTransactionRequest(from.getAmount(), from.getId(), to.getId(), "12588"), account.getSecretKey());
        });

        // then
        String expectedMessage = "transaction didn't succeed because account from is frozen";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void should_return_exception_if_account_to_is_frozen() throws Exception {
        // given
        User accountHolder = new AccountHolder("Peter", new HashSet<>(), LocalDate.of(1980, 6, 4), new Address("10 Downing Street", "London", "UK", "1234512"), "11 Downing Street", "123456");
        StudentCheckingAccount account = new StudentCheckingAccount(
                new Money(new BigDecimal("123000"), Currency.getInstance("USD")),
                accountHolder,
                null,
                AccountStatus.ACTIVE,
                "secret-key-123"
        );
        StudentCheckingAccount account2 = new StudentCheckingAccount(
                new Money(new BigDecimal("123"), Currency.getInstance("USD")),
                accountHolder,
                null,
                AccountStatus.FROZEN,
                "secret-key-123"
        );

        AccountHolderTransaction from = new AccountHolderTransaction(new Money(new BigDecimal("1000")), account, account2, accountHolder);
        from.setId(1L);
        AccountHolderTransaction to = new AccountHolderTransaction(new Money(new BigDecimal("1000")), account, account2, accountHolder);
        to.setId(2L);
        List<Transaction> transactions = new ArrayList<>();


        when(accountRepository.findById(any())).thenReturn(Optional.of(account), Optional.of(account2));
        when(transactionRepository.between(any(), any(), eq(accountHolder))).thenReturn(transactions);
        when(transactionRepository.getHighestTransactionAmount(eq(accountHolder.getId()), any())).thenReturn(new BigDecimal("150"));
        // when
        Exception exception = assertThrows(Exception.class, () -> {
            transactionService.createTransaction(accountHolder, new CreateAccountHolderTransactionRequest(from.getAmount(), from.getId(), to.getId(), "12588"), account.getSecretKey());
        });

        // then
        String expectedMessage = "transaction didn't succeed because account to is frozen";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void should_return_exception_if_account_does_not_exist() throws Exception {
        // given
        User accountHolder = new AccountHolder("Peter", new HashSet<>(), LocalDate.of(1980, 6, 4), new Address("10 Downing Street", "London", "UK", "1234512"), "11 Downing Street", "123456");
        StudentCheckingAccount account = new StudentCheckingAccount(
                new Money(new BigDecimal("123000"), Currency.getInstance("USD")),
                accountHolder,
                null,
                AccountStatus.ACTIVE,
                "secret-key-123"
        );
        StudentCheckingAccount account2 = new StudentCheckingAccount(
                new Money(new BigDecimal("123"), Currency.getInstance("USD")),
                accountHolder,
                null,
                AccountStatus.FROZEN,
                "secret-key-123"
        );

        AccountHolderTransaction from = new AccountHolderTransaction(new Money(new BigDecimal("1000")), account, account2, accountHolder);
        from.setId(1L);
        AccountHolderTransaction to = new AccountHolderTransaction(new Money(new BigDecimal("1000")), account, account2, accountHolder);
        to.setId(2L);
        List<Transaction> transactions = new ArrayList<>();


        when(accountRepository.findById(any())).thenReturn(Optional.empty());
        when(transactionRepository.getHighestTransactionAmount(eq(accountHolder.getId()), any())).thenReturn(new BigDecimal("150"));
        // when
        Exception exception = assertThrows(Exception.class, () -> {
            transactionService.createTransaction(accountHolder, new CreateAccountHolderTransactionRequest(from.getAmount(), from.getId(), to.getId(), "12588"), account.getSecretKey());
        });

        // then
        String expectedMessage = "Account from doesn't exist";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void account_from_does_not_exist() throws Exception {
        // given
        User accountHolder = new AccountHolder("Peter", new HashSet<>(), LocalDate.of(1980, 6, 4), new Address("10 Downing Street", "London", "UK", "1234512"), "11 Downing Street", "123456");
        User thirdPartyUser = new ThirdPartyUser("Peter", new HashSet<>(),"123456");
        CreditCardAccount account = new CreditCardAccount(
                new Money(new BigDecimal("123000"), Currency.getInstance("USD")),
                accountHolder,
                null,
                AccountStatus.ACTIVE,
                new Money(new BigDecimal("100")),
                new BigDecimal("0.1")
        );
        CreditCardAccount account2 = new CreditCardAccount(
                new Money(new BigDecimal("123000"), Currency.getInstance("USD")),
                accountHolder,
                null,
                AccountStatus.ACTIVE,
                new Money(new BigDecimal("100")),
                new BigDecimal("0.1")
        );

        AccountHolderTransaction from = new AccountHolderTransaction(new Money(new BigDecimal("1000")), account, account2, thirdPartyUser);
        from.setId(1L);
        AccountHolderTransaction to = new AccountHolderTransaction(new Money(new BigDecimal("1000")), account, account2, accountHolder);
        to.setId(2L);
        List<Transaction> transactions = new ArrayList<>();

        when(transactionRepository.getHighestTransactionAmount(eq(accountHolder.getId()), any())).thenReturn(new BigDecimal("150"));
        // when
        Exception exception = assertThrows(Exception.class, () -> {
            transactionService.createTransaction(thirdPartyUser, new CreateAccountHolderTransactionRequest(from.getAmount(), from.getId(), to.getId(), "12588"), null);
        });

        // then
        String expectedMessage = "Account from doesn't exist";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void third_party_can_not_make_transactions_from_or_to_credit_cards() throws Exception {
        // given
        User accountHolder = new AccountHolder("Peter", new HashSet<>(), LocalDate.of(1980, 6, 4), new Address("10 Downing Street", "London", "UK", "1234512"), "11 Downing Street", "123456");
        User thirdPartyUser = new ThirdPartyUser("Peter", new HashSet<>(),"123456");
        CreditCardAccount account = new CreditCardAccount(
                new Money(new BigDecimal("123000"), Currency.getInstance("USD")),
                accountHolder,
                null,
                AccountStatus.ACTIVE,
                new Money(new BigDecimal("100")),
                new BigDecimal("0.1")
        );
        CreditCardAccount account2 = new CreditCardAccount(
                new Money(new BigDecimal("123000"), Currency.getInstance("USD")),
                accountHolder,
                null,
                AccountStatus.ACTIVE,
                new Money(new BigDecimal("100")),
                new BigDecimal("0.1")
        );

        AccountHolderTransaction from = new AccountHolderTransaction(new Money(new BigDecimal("1000")), account, account2, thirdPartyUser);
        from.setId(1L);
        List<Transaction> transactions = new ArrayList<>();

        when(accountRepository.findById(any())).thenReturn(Optional.of(account), Optional.of(account2));
        // when
        Exception exception = assertThrows(Exception.class, () -> {
            transactionService.createTransaction(thirdPartyUser, new CreateAccountHolderTransactionRequest(new Money(new BigDecimal("100")), account.getId(), account2.getId(), "12588"), null);
        });

        // then
        String expectedMessage = "Unauthorized user to make this transaction";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
