package com.ironhack.banking_system.controllers.responses.transactions;

import com.ironhack.banking_system.data.model.Money;

import java.time.LocalDateTime;

public class CreateAccountHolderTransactionResponse extends CreateTransactionResponse {

    public CreateAccountHolderTransactionResponse() {
    }

    public CreateAccountHolderTransactionResponse(Long id, Money amount, long from, long to, long userId, LocalDateTime createdAt) {
        super(id, amount, from, to, userId, createdAt);
    }
}
