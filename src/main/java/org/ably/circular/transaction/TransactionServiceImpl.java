package org.ably.circular.transaction;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ably.circular.enterprise.Enterprise;
import org.ably.circular.exception.NotFoundException;

import org.ably.circular.security.CurrentUserProvider;
import org.ably.circular.transaction.transactionItem.TransactionItem;
import org.ably.circular.transaction.transactionItem.TransactionItemMapper;
import org.ably.circular.transaction.transactionItem.TransactionItemRequest;
import org.ably.circular.transaction.transactionItem.TransactionItemServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final TransactionItemMapper transactionItemMapper;
    private final TransactionItemServiceImpl transactionItemService;
    private final CurrentUserProvider currentUserProvider;

    private void validateTransactionRequest(TransactionRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Transaction request cannot be null");
        }
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new IllegalArgumentException("Transaction must have at least one item");
        }
    }


    private void processTransactionItems(Transaction transaction, List<TransactionItemRequest> itemRequests) {
        List<TransactionItem> items = new ArrayList<>();
        float totalQuantity = 0;
        float totalPrice = 0;

        for (var itemRequest : itemRequests) {
            TransactionItem item = transactionItemMapper.toEntity(itemRequest, transaction);
            items.add(item);

            totalQuantity += item.getQuantity();
            totalPrice += item.getPrice();
        }


        items = transactionItemService.saveAll(items);

        transaction.setItems(items);
        transaction.setTotalQuantity(totalQuantity);
        transaction.setTotalPrice(totalPrice);
         transactionRepository.save(transaction);
    }

    @Override
    @Transactional
    public TransactionResponse save(Transaction transaction) {
        Transaction savedTransaction = transactionRepository.save(transaction);
        return transactionMapper.toResponse(savedTransaction);
    }

    @Override
    @Transactional
    public TransactionResponse create(TransactionRequest transactionRequest) {
        validateTransactionRequest(transactionRequest);

        Transaction transaction = transactionMapper.toEntity(transactionRequest);
        Enterprise seller = currentUserProvider.getCurrentUserEnterpriseOrThrow();
        transaction.setSeller(seller);

        transaction = transactionRepository.save(transaction);


        processTransactionItems(transaction, transactionRequest.getItems());

        return transactionMapper.toResponse(transaction);
    }

    @Override
    @Transactional
    public TransactionResponse update(Long id, TransactionRequest transactionRequest) {
        Transaction existingTransaction = findEntityById(id);
        validateTransactionRequest(transactionRequest);

        transactionMapper.updateEntityFromRequest(transactionRequest, existingTransaction);

        transactionItemService.deleteByTransactionId(id);

        processTransactionItems(existingTransaction, transactionRequest.getItems());

        return transactionMapper.toResponse(existingTransaction);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        existsById(id);
        transactionItemService.deleteByTransactionId(id);
        transactionRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionResponse findById(Long id) {
        Transaction transaction = findEntityById(id);
        return transactionMapper.toResponse(transaction);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionResponse> findAll(Pageable pageable) {
        return transactionRepository.findAll(pageable)
                .map(transactionMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public void existsById(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new NotFoundException("Transaction", id);
        }
    }

    @Override
    @Transactional
    public void changeTransactionStatus(Long id, TransactionStatus status) {
        Transaction transaction = findEntityById(id);
        transaction.setStatus(status);

        if (status == TransactionStatus.COMPLETED) {
            transaction.setCompletedAt(new Date());
        }

        transactionRepository.save(transaction);
    }

    private Transaction findEntityById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Transaction", id));
    }
}