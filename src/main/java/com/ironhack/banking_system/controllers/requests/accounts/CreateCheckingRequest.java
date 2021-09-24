package com.ironhack.banking_system.controllers.requests.accounts;

import com.ironhack.banking_system.data.model.Money;
import com.ironhack.banking_system.data.model.accounts.Account;
import com.ironhack.banking_system.data.model.accounts.AccountStatus;
import com.ironhack.banking_system.data.model.accounts.CheckingAccount;
import com.ironhack.banking_system.data.model.users.User;
import lombok.Getter;

import java.math.BigDecimal;


public class CreateCheckingRequest extends CreateAccountRequest {

    @Getter
    private String secretKey;
    @Getter
    private Money minimumBalance = new Money(new BigDecimal("250"));
    @Getter
    private Money monthlyMaintenanceFee = new Money(new BigDecimal("12"));

    public CreateCheckingRequest() {
    }

    public CreateCheckingRequest(Money balance, long primaryOwnerId, Long secondaryOwnerId, String status, String secretKey, Money minimumBalance, Money monthlyMaintenanceFee) {
        super(balance, primaryOwnerId, secondaryOwnerId, status);
        this.secretKey = secretKey;
        this.minimumBalance = minimumBalance;
        this.monthlyMaintenanceFee = monthlyMaintenanceFee;
    }

    @Override
    public Account toEntity(User primaryOwner, User secondaryOwner) {
        return new CheckingAccount(balance, primaryOwner, secondaryOwner, AccountStatus.fromString(status), secretKey, minimumBalance, monthlyMaintenanceFee);
    }
}
