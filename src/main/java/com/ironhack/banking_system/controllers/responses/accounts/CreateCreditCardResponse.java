package com.ironhack.banking_system.controllers.responses.accounts;

import com.ironhack.banking_system.data.model.Money;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CreateCreditCardResponse extends CreateAccountResponse {

    @Getter
    private Money creditLimit;
    @Getter
    private BigDecimal interestRate;


    public CreateCreditCardResponse() {
    }

    public CreateCreditCardResponse(long id, Money balance, long primaryOwnerId, Long secondaryOwnerId, Money penaltyFee, String status, LocalDateTime createdAt, LocalDateTime refreshedAt, Money creditLimit, BigDecimal interestRate) {
        super(id, balance, primaryOwnerId, secondaryOwnerId, penaltyFee, status, createdAt, refreshedAt);
        this.creditLimit = creditLimit;
        this.interestRate = interestRate;
    }
}
