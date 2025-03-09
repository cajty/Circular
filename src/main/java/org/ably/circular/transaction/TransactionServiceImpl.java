package org.ably.circular.transaction;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ably.circular.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

      private void validateTransactionRequest(TransactionRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Transaction request cannot be null");
        }
        // to continu
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
        Transaction savedTransaction = transactionRepository.save(transaction);
        return transactionMapper.toResponse(savedTransaction);
    }

    @Override
    @Transactional
    public TransactionResponse update(Long id, TransactionRequest transactionRequest) {
        Transaction existingTransaction = findEntityById(id);
        validateTransactionRequest(transactionRequest);
        transactionMapper.updateEntityFromRequest(transactionRequest, existingTransaction);
        Transaction updatedTransaction = transactionRepository.save(existingTransaction);
        return transactionMapper.toResponse(updatedTransaction);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new NotFoundException("Transaction", id);
        }
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
          // add that part of that user have that autorite to do this action
        //
         Transaction transaction =  findEntityById(id);
         transaction.setStatus(status);

    }

    private Transaction findEntityById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Transaction", id));
    }

}

