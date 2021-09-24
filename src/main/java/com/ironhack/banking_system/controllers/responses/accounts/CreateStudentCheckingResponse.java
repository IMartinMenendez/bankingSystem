package com.ironhack.banking_system.controllers.responses.accounts;

import com.ironhack.banking_system.data.model.Money;
import java.time.LocalDateTime;

public class CreateStudentCheckingResponse extends CreateAccountResponse {


    public CreateStudentCheckingResponse() {
    }

    public CreateStudentCheckingResponse(long id, Money balance, long primaryOwnerId, Long secondaryOwnerId, Money penaltyFee, String status, LocalDateTime createdAt, LocalDateTime refreshedAt) {
        super(id, balance, primaryOwnerId, secondaryOwnerId, penaltyFee, status, createdAt, refreshedAt);
    }
}
