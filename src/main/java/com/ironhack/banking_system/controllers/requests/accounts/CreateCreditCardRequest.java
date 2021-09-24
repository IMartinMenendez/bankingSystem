package com.ironhack.banking_system.controllers.requests.accounts;

import com.ironhack.banking_system.data.model.Money;
import com.ironhack.banking_system.data.model.accounts.Account;
import com.ironhack.banking_system.data.model.accounts.AccountStatus;
import com.ironhack.banking_system.data.model.accounts.CreditCardAccount;
import com.ironhack.banking_system.data.model.users.User;
import lombok.Getter;
import java.math.BigDecimal;


public class CreateCreditCardRequest extends CreateAccountRequest{

    @Getter
    private Money creditLimit = new Money(new BigDecimal("100"));
    @Getter
    private BigDecimal interestRate = new BigDecimal("0.2");


    public CreateCreditCardRequest() {
    }

    public CreateCreditCardRequest(Money balance, long primaryOwnerId, Long secondaryOwnerId, String status, Money creditLimit, BigDecimal interestRate) {
        super(balance, primaryOwnerId, secondaryOwnerId, status);
        this.creditLimit = creditLimit;
        this.interestRate = interestRate;
    }

    @Override
    public Account toEntity(User primaryOwner, User secondaryOwner) throws Exception {
        return new CreditCardAccount(balance, primaryOwner, secondaryOwner, AccountStatus.fromString(status), creditLimit, interestRate);
    }
}
