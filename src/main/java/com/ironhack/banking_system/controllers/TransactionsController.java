package com.ironhack.banking_system.controllers;

import com.ironhack.banking_system.controllers.requests.transactions.CreateTransactionRequest;
import com.ironhack.banking_system.controllers.responses.transactions.CreateTransactionResponse;
import com.ironhack.banking_system.security.CustomUserDetails;
import com.ironhack.banking_system.services.interfaces.TransactionService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
public class TransactionsController {

  private final TransactionService transactionService;

  public TransactionsController(TransactionService transactionService) {
    this.transactionService = transactionService;
  }

  @PostMapping
  public CreateTransactionResponse createTransaction(@RequestHeader(value = "X-hash-key", required = false) String hashKey, @RequestBody CreateTransactionRequest transaction, @AuthenticationPrincipal CustomUserDetails principal) throws Exception {
    return CreateTransactionResponse.fromEntity(transactionService.createTransaction(principal.getUser(), transaction, hashKey));
  }
}
