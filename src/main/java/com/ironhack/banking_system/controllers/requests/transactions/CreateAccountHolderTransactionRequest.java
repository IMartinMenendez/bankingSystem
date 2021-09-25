package com.ironhack.banking_system.controllers.requests.transactions;

import com.ironhack.banking_system.data.model.Money;
import com.ironhack.banking_system.data.model.accounts.Account;
import com.ironhack.banking_system.data.model.transactions.AccountHolderTransaction;
import com.ironhack.banking_system.data.model.transactions.Transaction;
import com.ironhack.banking_system.data.model.users.User;

public class CreateAccountHolderTransactionRequest extends CreateTransactionRequest {

    public CreateAccountHolderTransactionRequest(Money amount, long fromAccountId, long toAccountId, String secretKey) {
        super(amount, fromAccountId, toAccountId, secretKey);
    }

    @Override
    public Transaction toEntity(Account from, Account to, User user) {
        return new AccountHolderTransaction(amount, from, to, user);
    }
}
