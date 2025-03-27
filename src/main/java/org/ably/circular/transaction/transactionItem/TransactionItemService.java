package org.ably.circular.transaction.transactionItem;

import java.util.List;

public interface TransactionItemService {

    TransactionItemResponse save(TransactionItem transactionItem);
    List<TransactionItem> saveAll(List<TransactionItem> transactionItems);

    List<TransactionItemResponse> findByTransactionId(Long transactionId);

    void deleteByTransactionId(Long transactionId);
}
