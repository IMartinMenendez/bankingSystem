package com.ironhack.banking_system.data.model.accounts;

import com.ironhack.banking_system.data.model.users.User;
import com.ironhack.banking_system.data.model.Money;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class StudentCheckingAccount extends Account {
  @Getter
  private String secretKey;

  public StudentCheckingAccount(Money balance, User primaryOwner, User secondaryOwner, AccountStatus status, String secretKey) {
    super(balance, primaryOwner, secondaryOwner, status);
    this.secretKey = secretKey;
  }

  @Override
  public boolean refresh() {
    return false;
  }
}
