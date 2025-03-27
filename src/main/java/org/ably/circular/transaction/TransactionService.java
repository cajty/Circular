package org.ably.circular.transaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionService {

    TransactionResponse save(Transaction transaction);

    TransactionResponse create(TransactionRequest transactionRequest);

    TransactionResponse update(Long id, TransactionRequest transactionRequest);

    void delete(Long id);

    TransactionResponse findById(Long id);

    Page<TransactionResponse> findAll(Pageable pageable);

    void existsById(Long id);

    void changeTransactionStatus(Long id ,TransactionStatus status);
}