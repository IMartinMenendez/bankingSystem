package com.ironhack.banking_system.controllers.requests.accounts;

import com.ironhack.banking_system.data.model.accounts.Account;
import com.ironhack.banking_system.data.model.accounts.AccountStatus;
import com.ironhack.banking_system.data.model.accounts.StudentCheckingAccount;
import com.ironhack.banking_system.data.model.users.User;
import lombok.Getter;

public class CreateStudentCheckingRequest extends CreateAccountRequest{
    @Getter
    private String secretKey;

    @Override
    public Account toEntity(User primaryOwner, User secondaryOwner) {
        return new StudentCheckingAccount(balance, primaryOwner, secondaryOwner, AccountStatus.fromString(status), secretKey);
    }
}
