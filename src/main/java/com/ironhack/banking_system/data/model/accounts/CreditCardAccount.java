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
public class CreditCardAccount extends Account {
  @Getter
  @AttributeOverrides({
      @AttributeOverride(name = "amount", column = @Column(name = "credit_limit_amount")),
      @AttributeOverride(name = "currency", column = @Column(name = "credit_limit_currency"))
  })
  @Embedded
  private Money creditLimit = new Money(new BigDecimal("100"));
  @Getter
  private BigDecimal interestRate = new BigDecimal("0.2");

  @Transient
  private final static Money MAX_CREDIT_LIMIT = new Money(new BigDecimal("100000"));
  @Transient
  private final static Money MIN_CREDIT_LIMIT = new Money(new BigDecimal("100"));
  @Transient
  private final static BigDecimal MAX_INTEREST_RATE = new BigDecimal("0.2");
  @Transient
  private final static BigDecimal MIN_INTEREST_RATE = new BigDecimal("0.1");

  public CreditCardAccount(Money balance, User primaryOwner, User secondaryOwner, AccountStatus status, Money creditLimit, BigDecimal interestRate) throws Exception {
    super(balance, primaryOwner, secondaryOwner, status);

    if (creditLimit.getAmount().compareTo(MAX_CREDIT_LIMIT.getAmount()) > 0 || creditLimit.getAmount().compareTo(MIN_CREDIT_LIMIT.getAmount()) < 0) {
      throw new Exception("Invalid credit limit: " + creditLimit.getAmount().toPlainString() + ". Minimum credit limit: " + MIN_CREDIT_LIMIT.getAmount().toPlainString() + ", Maximum credit limit allowed: " + MAX_CREDIT_LIMIT.getAmount().toPlainString());
    }
    if (interestRate.compareTo(MAX_INTEREST_RATE) > 0 || interestRate.compareTo(MIN_INTEREST_RATE) < 0) {
      throw new Exception("Invalid interest rate: " + interestRate.toPlainString() + ". Minimum interest rate: " + MIN_INTEREST_RATE.toPlainString() + ", Maximum interest rate allowed: " + MAX_INTEREST_RATE.toPlainString());
    }

    this.creditLimit = creditLimit;
    this.interestRate = interestRate;
  }

  @Override
  public boolean refresh() {
    if (shouldRefresh()) {
      applyInterestRates(ChronoUnit.MONTHS.between(refreshedAt, LocalDateTime.now()));
      checkCreditLimit();
      refreshedAt = LocalDateTime.now();
      return true;
    }
    return false;
  }

  private void checkCreditLimit() {
    if (balance.getAmount().compareTo(creditLimit.getAmount().negate()) < 0) {
      balance.decreaseAmount(penaltyFee.getAmount());
    }
  }

  private void applyInterestRates(long months) {
    if (balance.getAmount().compareTo(BigDecimal.ZERO) < 0) {
      balance.increaseAmount(balance.getAmount().multiply(interestRate).multiply(new BigDecimal(months)));
    }
  }
}
