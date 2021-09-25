package com.ironhack.banking_system.controllers.requests.transactions;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.ironhack.banking_system.data.model.Money;
import com.ironhack.banking_system.data.model.accounts.Account;
import com.ironhack.banking_system.data.model.transactions.Transaction;
import com.ironhack.banking_system.data.model.users.User;
import lombok.Getter;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = CreateAccountHolderTransactionRequest.class, name = "account_holder"),
    @JsonSubTypes.Type(value = CreateInterestTransactionRequest.class, name = "interest"),
})
public abstract class CreateTransactionRequest {

  @Getter
  protected Money amount;

  @Getter
  protected long fromAccountId;

  @Getter
  protected long toAccountId;

  @Getter
  protected String secretKey;

  public CreateTransactionRequest() {
  }

  public CreateTransactionRequest(Money amount, long fromAccountId, long toAccountId, String secretKey) {
    this.amount = amount;
    this.fromAccountId = fromAccountId;
    this.toAccountId = toAccountId;
    this.secretKey = secretKey;
  }

  public abstract Transaction toEntity(Account from, Account to, User user);
}
