package com.ironhack.banking_system.data.model.transactions;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.ironhack.banking_system.data.model.Money;
import com.ironhack.banking_system.data.model.accounts.Account;
import com.ironhack.banking_system.data.model.users.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = AccountHolderTransaction.class, name = "account_holder"),
    @JsonSubTypes.Type(value = InterestTransaction.class, name = "interest"),
})
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@AllArgsConstructor
@NoArgsConstructor
public abstract class Transaction {
  @Id
  @GeneratedValue
  @Getter
  @Setter
  private Long id;

  @Embedded
  @Getter
  protected Money amount;

  @Getter
  @ManyToOne(fetch = FetchType.LAZY)
  protected Account from;

  @Getter
  @ManyToOne(fetch = FetchType.LAZY)
  protected Account to;

  @Getter
  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  protected User user;

  @CreationTimestamp
  @Getter
  protected LocalDateTime createdAt;

  public Transaction(Money amount, Account from, Account to, User user) {
    this.amount = amount;
    this.from = from;
    this.to = to;
    this.user = user;
  }
}
