package com.ironhack.banking_system.data;

import com.ironhack.banking_system.data.model.transactions.Transaction;
import com.ironhack.banking_system.data.model.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query(value = "SELECT SUM(t.amount.amount) from Transaction t WHERE t.createdAt >= ?1 AND t.createdAt <= ?2 AND t.user = ?3")
    BigDecimal amountBetween(LocalDateTime from, LocalDateTime to, User user);

    @Query(value = "SELECT SUM(t.amount) AS total FROM account_holder_transaction t WHERE t.user_id = ?1 AND t.created_at < ?2 GROUP BY DATE(t.created_at) ORDER BY total DESC LIMIT 1", nativeQuery = true)
    BigDecimal getHighestTransactionAmount(long userId, LocalDateTime limitDate);

    @Query(value = "SELECT t from Transaction t WHERE t.createdAt >= ?1 AND t.createdAt <= ?2 AND t.user = ?3")
    List<Transaction> between(LocalDateTime from, LocalDateTime to, User user);
}
