package com.ironhack.banking_system.data.model.users;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
public class Role {
  @Id
  @GeneratedValue
  @Getter
  private long id;
  @Getter
  private String name;

  public Role(String name) {
    this.name = name;
  }
}
