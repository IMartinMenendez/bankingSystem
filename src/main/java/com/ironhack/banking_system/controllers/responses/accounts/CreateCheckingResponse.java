package com.ironhack.banking_system.controllers.responses.accounts;

import com.ironhack.banking_system.data.model.Money;
import lombok.Getter;
import java.time.LocalDateTime;


public class CreateCheckingResponse extends CreateAccountResponse {

    @Getter
    private Money minimumBalance;
    @Getter
    private Money monthlyMaintenanceFee;

    public CreateCheckingResponse() {
    }

    public CreateCheckingResponse(long id, Money balance, long primaryOwnerId, Long secondaryOwnerId, Money penaltyFee, String status, LocalDateTime createdAt, LocalDateTime refreshedAt, Money minimumBalance, Money monthlyMaintenanceFee) {
        super(id, balance, primaryOwnerId, secondaryOwnerId, penaltyFee, status, createdAt, refreshedAt);
        this.minimumBalance = minimumBalance;
        this.monthlyMaintenanceFee = monthlyMaintenanceFee;
    }
}
