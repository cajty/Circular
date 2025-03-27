package org.ably.circular.transaction.transactionItem;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TransactionItemServiceImpl implements TransactionItemService {

    private final TransactionItemRepository transactionItemRepository;
    private final TransactionItemMapper transactionItemMapper;

    @Override
    @Transactional
    public TransactionItemResponse save(TransactionItem items) {
        TransactionItem savedItem = transactionItemRepository.save(items);
        return transactionItemMapper.toResponse(savedItem);
    }

    @Override
    public List<TransactionItem> saveAll(List<TransactionItem> items) {
        return transactionItemRepository.saveAll(items);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionItemResponse> findByTransactionId(Long itemId) {
        List<TransactionItem> items = transactionItemRepository.findByTransactionId(itemId);
        return transactionItemMapper.toResponseList(items);
    }

    @Override
    @Transactional
    public void deleteByTransactionId(Long itemId) {
        transactionItemRepository.deleteByTransactionId(itemId);
    }
}