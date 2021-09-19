package com.ironhack.banking_system.services.impl;

import com.ironhack.banking_system.controllers.requests.accounts.CreateAccountRequest;
import com.ironhack.banking_system.controllers.requests.accounts.CreateCheckingRequest;
import com.ironhack.banking_system.data.AccountRepository;
import com.ironhack.banking_system.data.UserRepository;
import com.ironhack.banking_system.data.model.accounts.Account;
import com.ironhack.banking_system.data.model.accounts.AccountStatus;
import com.ironhack.banking_system.data.model.accounts.StudentCheckingAccount;
import com.ironhack.banking_system.data.model.users.AccountHolder;
import com.ironhack.banking_system.data.model.users.User;
import com.ironhack.banking_system.services.interfaces.AccountService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

  private final AccountRepository accountRepository;
  private UserRepository userRepository;

  public AccountServiceImpl(AccountRepository accountRepository, UserRepository userRepository) {
    this.accountRepository = accountRepository;
    this.userRepository = userRepository;
  }

  @Override
  public List<Account> getAccounts(User owner) {
    if (owner.getRoles().stream().anyMatch(r -> r.getName().equals("ADMIN"))) {
      return accountRepository.findAll().stream().map(a -> {
        if (a.refresh()) {
          return accountRepository.save(a);
        }
        return a;
      }).toList();
    } else {
      return accountRepository.findByPrimaryOwner(owner).stream().map(a -> {
        if (a.refresh()) {
          return accountRepository.save(a);
        }
        return a;
      }).toList();
    }
  }

  @Override
  public Optional<Account> getAccount(User owner, long accountId) {
    if (owner.getRoles().stream().anyMatch(r -> r.getName().equals("ADMIN"))) {
      return accountRepository.findById(accountId).map(a -> {
        if (a.refresh()) {
          return accountRepository.save(a);
        }
        return a;
      });
    } else {
      return accountRepository.findByIdAndPrimaryOwner(accountId, owner).map(a -> {
        if (a.refresh()) {
          return accountRepository.save(a);
        }
        return a;
      });
    }
  }

  @Override
  public Account createAccount(CreateAccountRequest account) throws Exception {
    Optional<User> maybeUser = userRepository.findById(account.getPrimaryOwnerId());
    if(maybeUser.isEmpty()){
      throw new Exception("Primary Owner doesn't exist");
    }

    User user = maybeUser.get();
    if(user instanceof AccountHolder && ((AccountHolder) user).getAge() < 24 && account instanceof CreateCheckingRequest checking){
      StudentCheckingAccount studentCheckingAccount = new StudentCheckingAccount(checking.getBalance(), user, null, AccountStatus.fromString(checking.getStatus()), checking.getSecretKey());
      return accountRepository.save(studentCheckingAccount);
    }
    return accountRepository.save(account.toEntity(user, null));
  }
}
