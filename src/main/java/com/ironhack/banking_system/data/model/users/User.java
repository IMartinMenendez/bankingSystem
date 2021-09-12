package com.ironhack.banking_system.data.model.users;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@NoArgsConstructor
public abstract class User {
  @Id
  @GeneratedValue
  @Getter
  protected long id;
  @Getter
  protected String name;

  @Getter
  @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  protected Set<Role> roles;

  public User(String name, Set<Role> roles) {
    this.name = name;
    this.roles = roles;
  }

  public abstract String getPassword();
  public abstract void setPassword(String password);
}
