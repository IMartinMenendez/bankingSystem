package com.ironhack.banking_system.controllers.requests.accounts;

import com.ironhack.banking_system.data.model.Money;
import com.ironhack.banking_system.data.model.accounts.Account;
import com.ironhack.banking_system.data.model.accounts.AccountStatus;
import com.ironhack.banking_system.data.model.accounts.SavingsAccount;
import com.ironhack.banking_system.data.model.users.User;
import lombok.Getter;
import java.math.BigDecimal;

public class CreateSavingsRequest extends CreateAccountRequest{

    @Getter
    private String secretKey;
    @Getter
    private Money minimumBalance;
    @Getter
    private BigDecimal interestRate;

    public CreateSavingsRequest() {
    }

    public CreateSavingsRequest(Money balance, long primaryOwnerId, long secondaryOwnerId, String status, String secretKey, Money minimumBalance, BigDecimal interestRate) {
        super(balance, primaryOwnerId, secondaryOwnerId, status);
        this.secretKey = secretKey;
        this.minimumBalance = minimumBalance;
        this.interestRate = interestRate;
    }

    @Override
    public Account toEntity(User primaryOwner, User secondaryOwner) throws Exception {
        return new SavingsAccount(balance, primaryOwner, secondaryOwner, AccountStatus.fromString(status), minimumBalance, secretKey, interestRate);
    }
}
