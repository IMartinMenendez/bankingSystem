package com.ironhack.banking_system.services.interfaces;

import com.ironhack.banking_system.controllers.requests.transactions.CreateTransactionRequest;
import com.ironhack.banking_system.data.model.transactions.Transaction;
import com.ironhack.banking_system.data.model.users.User;

public interface TransactionService {
  Transaction createTransaction(User user, CreateTransactionRequest transaction, String hashKey) throws Exception;
}
