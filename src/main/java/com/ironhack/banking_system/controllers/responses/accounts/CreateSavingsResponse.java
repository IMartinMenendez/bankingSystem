package com.ironhack.banking_system.controllers.responses.accounts;

import com.ironhack.banking_system.data.model.Money;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CreateSavingsResponse extends CreateAccountResponse {

    @Getter
    private Money minimumBalance;
    @Getter
    private BigDecimal interestRate;

    public CreateSavingsResponse() {
    }

    public CreateSavingsResponse(long id, Money balance, long primaryOwnerId, Long secondaryOwnerId, Money penaltyFee, String status, LocalDateTime createdAt, LocalDateTime refreshedAt, Money minimumBalance, BigDecimal interestRate) {
        super(id, balance, primaryOwnerId, secondaryOwnerId, penaltyFee, status, createdAt, refreshedAt);
        this.minimumBalance = minimumBalance;
        this.interestRate = interestRate;
    }
}
