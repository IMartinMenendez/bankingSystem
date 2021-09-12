package com.ironhack.banking_system.data.model.accounts;

import com.ironhack.banking_system.data.model.users.User;
import com.ironhack.banking_system.data.model.Money;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@NoArgsConstructor
public class CheckingAccount extends Account {
  @Getter
  private String secretKey;
  @Getter
  @AttributeOverrides({
      @AttributeOverride(name = "amount", column = @Column(name = "minimum_balance_amount")),
      @AttributeOverride(name = "currency", column = @Column(name = "minimum_balance_currency"))
  })
  @Embedded
  private Money minimumBalance = new Money(new BigDecimal("250"));
  @Getter
  @AttributeOverrides({
      @AttributeOverride(name = "amount", column = @Column(name = "monthly_maintenance_fee_amount")),
      @AttributeOverride(name = "currency", column = @Column(name = "monthly_maintenance_fee_currency"))
  })
  @Embedded
  private Money monthlyMaintenanceFee = new Money(new BigDecimal("12"));

  public CheckingAccount(Money balance, User primaryOwner, User secondaryOwner, AccountStatus status, String secretKey, Money minimumBalance, Money monthlyMaintenanceFee) {
    super(balance, primaryOwner, secondaryOwner, status);
    this.secretKey = secretKey;
    this.minimumBalance = minimumBalance;
    this.monthlyMaintenanceFee = monthlyMaintenanceFee;
  }

  @Override
  public boolean refresh() {
    if (shouldRefresh()) {
      applyMonthlyMaintenance(ChronoUnit.MONTHS.between(refreshedAt, LocalDateTime.now()));
      checkMinimumBalance();
      refreshedAt = LocalDateTime.now();
      return true;
    }
    return false;
  }

  private void applyMonthlyMaintenance(long months) {
    balance.decreaseAmount(monthlyMaintenanceFee.getAmount().multiply(new BigDecimal(months)));
  }

  private void checkMinimumBalance() {
    if (balance.getAmount().compareTo(minimumBalance.getAmount()) < 0) {
      balance.decreaseAmount(penaltyFee.getAmount());
    }
  }
}
