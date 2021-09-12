package com.ironhack.banking_system.data.model.accounts;

import com.ironhack.banking_system.data.model.transactions.Transaction;
import com.ironhack.banking_system.data.model.users.User;
import com.ironhack.banking_system.data.model.Money;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@NoArgsConstructor
public abstract class Account {
  @Id
  @GeneratedValue
  @Getter
  protected long id;
  @Getter
  @Embedded
  protected Money balance;
  @Getter
  @ManyToOne
  @JoinColumn(name = "primary_owner_id", nullable = false)
  protected User primaryOwner;
  @Getter
  @ManyToOne
  @JoinColumn(name = "secondary_owner_id")
  protected User secondaryOwner;
  @Getter
  @AttributeOverrides({
      @AttributeOverride(name = "amount", column = @Column(name = "penalty_fee_amount")),
      @AttributeOverride(name = "currency", column = @Column(name = "penalty_fee_currency"))
  })
  @Embedded
  protected Money penaltyFee = new Money(new BigDecimal("40"));
  @Getter
  @Setter
  @Enumerated(EnumType.STRING)
  protected AccountStatus status;

  @CreationTimestamp
  @Getter
  protected LocalDateTime createdAt;

  @Getter
  @Setter
  @CreationTimestamp
  protected LocalDateTime refreshedAt;

  @Getter
  @OneToMany(mappedBy = "to", fetch = FetchType.LAZY)
  protected List<Transaction> inTransactions;

  @Getter
  @OneToMany(mappedBy = "from", fetch = FetchType.LAZY)
  protected List<Transaction> outTransactions;

  public Account(Money balance, User primaryOwner, User secondaryOwner, AccountStatus status) {
    this.balance = balance;
    this.primaryOwner = primaryOwner;
    this.secondaryOwner = secondaryOwner;
    this.status = status;
  }

  public abstract boolean refresh();

  protected boolean shouldRefresh() {
    LocalDateTime currentTime = LocalDateTime.now();
    Duration sinceLastTime = Duration.between(refreshedAt, currentTime);
    return sinceLastTime.compareTo(Duration.ofDays(30)) > 0;
  }
}
