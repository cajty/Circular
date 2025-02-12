package org.ably.circular.transaction;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
@AllArgsConstructor
@Tag(name = "Transaction Controller", description = "Transaction Management APIs")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TransactionController {

    private final TransactionService transactionService;

    @Operation(summary = "Create a new transaction")
    @PostMapping
    public ResponseEntity<TransactionResponse> create(@Valid @RequestBody TransactionRequest transactionRequest) {
        TransactionResponse response = transactionService.create(transactionRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Get a transaction by ID")
    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getById(@PathVariable Long id) {
        TransactionResponse response = transactionService.findById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Update an existing transaction")
    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponse> update(@Valid @PathVariable Long id, @RequestBody TransactionRequest transactionRequest) {
        TransactionResponse response = transactionService.update(id, transactionRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Delete a transaction by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        transactionService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Get all transactions with pagination")
    @GetMapping
    public ResponseEntity<Page<TransactionResponse>> getAll(Pageable pageable) {
        Page<TransactionResponse> response = transactionService.findAll(pageable);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}