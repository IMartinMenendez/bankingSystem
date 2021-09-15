package com.ironhack.banking_system.data;

import com.ironhack.banking_system.data.model.accounts.Account;
import com.ironhack.banking_system.data.model.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
  List<Account> findByPrimaryOwner(User primaryOwner);

  Optional<Account> findByIdAndPrimaryOwner(long id, User primaryOwner);
}
