package com.ironhack.banking_system.services.interfaces;

import com.ironhack.banking_system.controllers.requests.accounts.CreateAccountRequest;
import com.ironhack.banking_system.data.model.accounts.Account;
import com.ironhack.banking_system.data.model.users.User;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    List<Account> getAccounts(User owner);

    Optional<Account> getAccount(User owner, long accountId);

    Account createAccount(CreateAccountRequest account) throws Exception;
}
