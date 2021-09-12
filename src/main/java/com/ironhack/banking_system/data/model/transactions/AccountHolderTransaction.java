package com.ironhack.banking_system.data.model.transactions;

import com.ironhack.banking_system.data.model.Money;
import com.ironhack.banking_system.data.model.accounts.Account;
import com.ironhack.banking_system.data.model.users.User;

import javax.persistence.Entity;

@Entity
public class AccountHolderTransaction extends Transaction {

    public AccountHolderTransaction() {

    }

    public AccountHolderTransaction(Money amount, Account from, Account to, User user) {
        super(amount, from, to, user);
    }


}
