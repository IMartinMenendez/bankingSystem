package com.ironhack.banking_system.data.model.users;

import com.ironhack.banking_system.data.model.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import java.time.LocalDate;
import java.time.Period;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class AccountHolder extends User {
  @Getter
  private LocalDate dateOfBirth;

  @Getter
  @Embedded
  private Address primaryAddress;
  @Getter
  private String mailingAddress;

  private String password;

  public AccountHolder(String name, Set<Role> roles, LocalDate dateOfBirth, Address primaryAddress, String mailingAddress, String password) {
    super(name, roles);
    this.dateOfBirth = dateOfBirth;
    this.primaryAddress = primaryAddress;
    this.mailingAddress = mailingAddress;
    this.password = password;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public void setPassword(String password) {
    this.password = password;
  }

  public int getAge(){
    return Period.between(this.dateOfBirth, LocalDate.now()).getYears();
  }
}
