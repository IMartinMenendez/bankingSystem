package com.ironhack.banking_system.controllers.responses.transactions;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.ironhack.banking_system.data.model.Money;
import com.ironhack.banking_system.data.model.transactions.AccountHolderTransaction;
import com.ironhack.banking_system.data.model.transactions.InterestTransaction;
import com.ironhack.banking_system.data.model.transactions.Transaction;
import lombok.Getter;

import java.time.LocalDateTime;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = CreateAccountHolderTransactionResponse.class, name = "account_holder"),
    @JsonSubTypes.Type(value = CreateInterestTransactionResponse.class, name = "interest"),
})
public abstract class CreateTransactionResponse {
  @Getter
  private Long id;

  @Getter
  protected Money amount;

  @Getter
  protected long from;

  @Getter
  protected long to;

  @Getter
  protected long userId;

  @Getter
  protected LocalDateTime createdAt;

  public CreateTransactionResponse() {
  }

  public CreateTransactionResponse(Long id, Money amount, long from, long to, long userId, LocalDateTime createdAt) {
    this.id = id;
    this.amount = amount;
    this.from = from;
    this.to = to;
    this.userId = userId;
    this.createdAt = createdAt;
  }

  public static CreateTransactionResponse fromEntity(Transaction transaction) throws Exception {
    if(transaction instanceof AccountHolderTransaction){
      return new CreateAccountHolderTransactionResponse(transaction.getId(), transaction.getAmount(), transaction.getFrom().getId(), transaction.getTo().getId(),transaction.getUser().getId(), transaction.getCreatedAt());
    } else if(transaction instanceof InterestTransaction){
      return new CreateInterestTransactionResponse(transaction.getId(), transaction.getAmount(), transaction.getFrom().getId(), transaction.getTo().getId(), transaction.getUser().getId(), transaction.getCreatedAt());
    }
    throw new Exception("Unknown Transaction Type");
  }
}
