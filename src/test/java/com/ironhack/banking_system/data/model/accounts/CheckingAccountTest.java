package com.ironhack.banking_system.data.model.accounts;

import com.ironhack.banking_system.data.model.Address;
import com.ironhack.banking_system.data.model.Money;
import com.ironhack.banking_system.data.model.users.AccountHolder;
import com.ironhack.banking_system.data.model.users.User;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.HashSet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;

public class CheckingAccountTest {

  private final Currency currency = Currency.getInstance("USD");

  private final User primaryOwner = new AccountHolder("Peter", new HashSet<>(), LocalDate.of(1980, 6, 4), new Address("10 Downing Street", "London", "UK","1234512"), "11 Downing Street", "123456");
  private final User secondaryOwner = null;
  private final AccountStatus status = AccountStatus.ACTIVE;
  private final String secretKey = "secret-key";
  private final Money minimumBalance = new Money(new BigDecimal("100"), currency);
  private final Money monthlyMaintenanceFee = new Money(new BigDecimal("20"), currency);

  @Test
  public void refreshShouldNotApplyFees() {
    Money balance = new Money(new BigDecimal("99"), currency);
    LocalDateTime refreshedAt = LocalDateTime.now().minus(Duration.ofDays(29));

    Account account = new CheckingAccount(balance, primaryOwner, secondaryOwner, status, secretKey, minimumBalance, monthlyMaintenanceFee);
    account.setRefreshedAt(refreshedAt);
    account.refresh();

    assertThat(account.balance.getAmount(), comparesEqualTo(new BigDecimal("99")));
  }

  @Test
  public void refreshShouldApplyMaintenanceAndPenaltyFees() {
    Money balance = new Money(new BigDecimal("99"), currency);
    LocalDateTime refreshedAt = LocalDateTime.now().minus(Duration.ofDays(31));

    Account account = new CheckingAccount(balance, primaryOwner, secondaryOwner, status, secretKey, minimumBalance, monthlyMaintenanceFee);
    account.setRefreshedAt(refreshedAt);
    account.refresh();

    assertThat(account.balance.getAmount(), comparesEqualTo(new BigDecimal("39")));
  }

  @Test
  public void refreshShouldApplyMaintenanceFee() {
    Money balance = new Money(new BigDecimal("199"), currency);
    LocalDateTime refreshedAt = LocalDateTime.now().minus(Duration.ofDays(31));

    Account account = new CheckingAccount(balance, primaryOwner, secondaryOwner, status, secretKey, minimumBalance, monthlyMaintenanceFee);
    account.setRefreshedAt(refreshedAt);
    account.refresh();

    assertThat(account.balance.getAmount(), comparesEqualTo(new BigDecimal("179")));
  }
}
