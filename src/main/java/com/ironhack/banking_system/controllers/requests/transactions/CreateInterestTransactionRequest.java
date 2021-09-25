package com.ironhack.banking_system.controllers.requests.transactions;

import com.ironhack.banking_system.data.model.accounts.Account;
import com.ironhack.banking_system.data.model.transactions.InterestTransaction;
import com.ironhack.banking_system.data.model.transactions.Transaction;
import com.ironhack.banking_system.data.model.users.User;

public class CreateInterestTransactionRequest extends CreateTransactionRequest {

    @Override
    public Transaction toEntity(Account from, Account to, User user) {
        return new InterestTransaction(amount, from, to, user);
    }
}
