package com.ironhack.banking_system.data.model.accounts;

import com.ironhack.banking_system.data.model.users.User;
import com.ironhack.banking_system.data.model.Money;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class SavingsAccount extends Account {

  @Transient
  private static final BigDecimal MAX_INTEREST_RATE = new BigDecimal("0.5");
  @Transient
  private static final Money MIN_MINIMUM_BALANCE = new Money(new BigDecimal("100"));

  @Getter
  @AttributeOverrides({
      @AttributeOverride(name = "amount", column = @Column(name = "minimum_balance_amount")),
      @AttributeOverride(name = "currency", column = @Column(name = "minimum_balance_currency"))
  })
  @Embedded
  private Money minimumBalance;
  @Getter
  private String secretKey;
  @Getter
  private BigDecimal interestRate;

  public SavingsAccount(Money balance, User primaryOwner, User secondaryOwner, AccountStatus status, Money minimumBalance, String secretKey, BigDecimal interestRate) throws Exception {
    super(balance, primaryOwner, secondaryOwner, status);

    if (interestRate.compareTo(MAX_INTEREST_RATE) > 0) {
      throw new Exception("Invalid interest rate: " + interestRate.toPlainString() + ". Maximum interest rate: " + MAX_INTEREST_RATE.toPlainString());
    }
    if (minimumBalance.getAmount().compareTo(MIN_MINIMUM_BALANCE.getAmount()) < 0) {
      throw new Exception("Invalid minimum balance: " + minimumBalance.getAmount().toPlainString() + ". Minimum minimum balanced: " + MIN_MINIMUM_BALANCE.getAmount().toPlainString());
    }

    this.minimumBalance = minimumBalance;
    this.secretKey = secretKey;
    this.interestRate = interestRate;


  }


  @Override
  public boolean refresh() {
    if (shouldRefresh()) {
      applyInterestRates(ChronoUnit.MONTHS.between(refreshedAt, LocalDateTime.now()));
      checkMinimumBalance();
      refreshedAt = LocalDateTime.now();
      return true;
    }
    return false;
  }

  private void checkMinimumBalance() {
    if (balance.getAmount().compareTo(minimumBalance.getAmount()) < 0) {
      balance.decreaseAmount(penaltyFee.getAmount());
    }
  }

  private void applyInterestRates(long months) {
    long years = months / 12;
    balance.increaseAmount(balance.getAmount().multiply(interestRate).multiply(new BigDecimal(years)));
  }
}
