package com.ironhack.banking_system.data.model.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ThirdPartyUser extends User {
  @Getter
  private String hashedKey;

  public ThirdPartyUser(String name, Set<Role> roles, String hashedKey) {
    super(name, roles);
    this.hashedKey = hashedKey;
  }

  @Override
  public String getPassword() {
    return hashedKey;
  }

  @Override
  public void setPassword(String password) {}
}
