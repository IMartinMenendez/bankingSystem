package com.ironhack.banking_system.services.impl;

import com.ironhack.banking_system.controllers.requests.transactions.CreateTransactionRequest;
import com.ironhack.banking_system.data.AccountRepository;
import com.ironhack.banking_system.data.TransactionRepository;
import com.ironhack.banking_system.data.model.accounts.*;
import com.ironhack.banking_system.data.model.transactions.Transaction;
import com.ironhack.banking_system.data.model.users.ThirdPartyUser;
import com.ironhack.banking_system.data.model.users.User;
import com.ironhack.banking_system.services.interfaces.TransactionService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

  private final AccountRepository accountRepository;

  private final TransactionRepository transactionRepository;

  public TransactionServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository) {
    this.accountRepository = accountRepository;
    this.transactionRepository = transactionRepository;
  }

  @Override
  public Transaction createTransaction(User user, CreateTransactionRequest transaction, String hashKey) throws Exception {
    LocalDateTime now = LocalDateTime.now();
    BigDecimal lastDay = transactionRepository.amountBetween(now.minusDays(1), now, user);
    if (lastDay == null) {
      lastDay = BigDecimal.ZERO;
    }
    BigDecimal highest = transactionRepository.getHighestTransactionAmount(user.getId(), now.minusDays(1));
    if (highest == null) {
      highest = BigDecimal.ZERO;
    }
    BigDecimal maxAmount = highest.multiply(new BigDecimal("1.5"));

    Optional<Account> maybeFrom = accountRepository.findById(transaction.getFromAccountId());
    if(maybeFrom.isEmpty()){
      throw new Exception("Account from doesn't exist");
    }
    Account from = maybeFrom.get();
    if(lastDay.compareTo(maxAmount) > 0){
      from.setStatus(AccountStatus.FROZEN);
      accountRepository.save(from);
      throw new Exception("Fraud Alert. Transactions made in the last 24 hours exceed 150%");
    }

    List<Transaction> recentTransactions = transactionRepository.between(now.minusSeconds(1), now, user);

    if(recentTransactions.size() > 2){
      from.setStatus(AccountStatus.FROZEN);
      accountRepository.save(from);
      throw new Exception("Fraud Alert. More than 2 transactions made in the last second");
    }

    if(from.getStatus().equals(AccountStatus.FROZEN)){
      throw new Exception("transaction didn't succeed because account from is frozen");
    }

    if (user.getRoles().stream().noneMatch(r -> r.getName().equals("ADMIN")) && !from.getPrimaryOwner().equals(user)) {
      throw new Exception("Unauthorized user to make this transaction");
    }

    if (from.refresh()) {
      accountRepository.save(from);
    }
    if (from.getBalance().getAmount().compareTo(transaction.getAmount().getAmount()) < 0) {
      throw new Exception("Insufficient funds");
    }

    Optional<Account> maybeTo = accountRepository.findById(transaction.getToAccountId());
    if(maybeTo.isEmpty()){
      throw new Exception("Account to doesn't exist");
    }
    Account to = maybeTo.get();
    if(from.getStatus().equals(AccountStatus.FROZEN)){
      throw new Exception("transaction didn't succeed because account from is frozen");
    }

    if(user instanceof ThirdPartyUser thirdPartyUser){
      if(!thirdPartyUser.getHashedKey().equals(hashKey)){
        throw new Exception("Unauthorized");
      }
      if(from instanceof CheckingAccount checkingAccount){
        if(!transaction.getSecretKey().equals(checkingAccount.getSecretKey())){
          throw new Exception("Unauthorized");
        }
      }
      if(from instanceof SavingsAccount savingsAccount){
        if(!transaction.getSecretKey().equals(savingsAccount.getSecretKey())){
          throw new Exception("Unauthorized");
        }
      }
      if(from instanceof StudentCheckingAccount studentCheckingAccount){
        if(!transaction.getSecretKey().equals(studentCheckingAccount.getSecretKey())){
          throw new Exception("Unauthorized");
        }
      }
      if(from instanceof CreditCardAccount || to instanceof CreditCardAccount){
          throw new Exception("Third Party Users can't make transactions from or to Credit Cards");
      }
     }

    from.getBalance().decreaseAmount(transaction.getAmount());
    to.getBalance().increaseAmount(transaction.getAmount());

    accountRepository.save(from);
    accountRepository.save(to);
    return transactionRepository.save(transaction.toEntity(from, to, user));
  }
}
