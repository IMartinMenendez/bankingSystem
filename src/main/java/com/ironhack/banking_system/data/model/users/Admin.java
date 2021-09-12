package com.ironhack.banking_system.data.model.users;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.util.Set;

@Entity
@NoArgsConstructor
public class Admin extends User {

  private String password;

  public Admin(String name, Set<Role> roles, String password) {
    super(name, roles);
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
}
