package com.ironhack.banking_system.controllers.responses.transactions;

import com.ironhack.banking_system.data.model.Money;

import java.time.LocalDateTime;

public class CreateInterestTransactionResponse extends CreateTransactionResponse {

    public CreateInterestTransactionResponse() {
    }

    public CreateInterestTransactionResponse(Long id, Money amount, long from, long to, long userId, LocalDateTime createdAt) {
        super(id, amount, from, to, userId, createdAt);
    }
}
