package org.ably.circular.transaction.transactionItem;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionItemRepository extends JpaRepository<TransactionItem, Long> {
     List<TransactionItem> findByTransactionId(Long transactionId);
    void deleteByTransactionId(Long transactionId);
}
